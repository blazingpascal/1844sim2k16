package com.example.dan.telegraphkeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.View;
//import android.R;
//import com.example.dan.telegraphkeyboard.R;

public class Telegraph extends InputMethodService implements KeyboardView.OnKeyboardActionListener
{
    private KeyboardView kv;
    private Keyboard keyboard;
    private MorseListener state = new MorseListener(); // dj's thing
    private Translator translator = new Translator(); // michael's thing

    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
//        android.R.layout
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

//    private void playSound(int keyCode){
//        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
//        am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
//    }

    public void onPress(int primaryCode) {
        String dotsAndDashes = state.press(System.currentTimeMillis());

        if (dotsAndDashes != null) {
            String character = translator.translate(dotsAndDashes);
            getCurrentInputConnection().commitText(character, 1);
//            playSound(primaryCode);
        }
    }

    public void onRelease(int primaryCode) {
        state.release(System.currentTimeMillis());
    }

    // random interface crap
    public void onKey(int primaryCode, int[] keyCodes) { }
    public void onText(CharSequence text) { }
    public void swipeLeft() { }
    public void swipeRight() { }
    public void swipeDown() { }
    public void swipeUp() { }
}
