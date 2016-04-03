package com.example.dan.telegraphkeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.JetPlayer;
import android.os.Handler;
import android.view.View;
import java.util.Timer;
import java.util.TimerTask;

public class Telegraph extends InputMethodService implements KeyboardView.OnKeyboardActionListener
{
    private KeyboardView kv;
    private Keyboard keyboard;
    private MorseListener state = new MorseListener();
    private Timer scheduler = new Timer();
    private boolean isBackspace = false;
    private AudioTrack player;

    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);
        return kv;
    }

    public void onPress(int primaryCode) {
        TimerTask backspace = new TimerTask() {
            public void run() {
                isBackspace = true;
                getCurrentInputConnection().deleteSurroundingText(1, 0);
            }
        };

        if (player != null) player.release();
        player = TonePlayer.generate();
        player.play();
        isBackspace = false;
        state.press(System.currentTimeMillis());
        scheduler.cancel();
        scheduler = new Timer();
        scheduler.scheduleAtFixedRate(backspace, (long)(7 * MorseListener.DOT), (long)(2 * MorseListener.DOT));
    }

    public void onRelease(int primaryCode) {
        TimerTask space = new TimerTask() {
            public void run() {
                getCurrentInputConnection().commitText(" ", 1);
            }
        };

        TimerTask print = new TimerTask() {
            public void run() {
                String dotsAndDashes = state.getCurrentState();

                if (dotsAndDashes != null && !isBackspace) {
                    String character = Translator.translate(dotsAndDashes);
                    getCurrentInputConnection().commitText(character, 1);
                }

                scheduler.schedule(space, (long)(4 * MorseListener.DOT));
            }
        };

        player.release();
        state.release(System.currentTimeMillis());
        scheduler.cancel();
        scheduler = new Timer();
        scheduler.schedule(print, (long)MorseListener.DASH);
    }

    // random interface crap
    public void onKey(int primaryCode, int[] keyCodes) { }
    public void onText(CharSequence text) { }
    public void swipeLeft() { }
    public void swipeRight() { }
    public void swipeDown() { }
    public void swipeUp() { }
}
