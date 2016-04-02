
public class MorseListener {
	private StringBuilder sb = new StringBuilder();
	private long timeSince;
	private long start;
	private long end;
	public static final double MOD = 1000;
	public static final double DOT = .25 * MOD;
	public static final double DASH = 3 * DOT;
	
	String press(long millis){
		start = millis;
		timeSince =  System.currentTimeMillis() - end;
		if (timeSince > 7 * DOT){
			sb.append(" / ");
			String s = sb.toString();
			sb = new StringBuilder();
			return s;
		} else if(timeSince > DASH){
			sb.append(" ");
			String s = sb.toString();
			sb = new StringBuilder();	
			return s;
		}
		return null;
		
	}
	void release(long millis){
		end = millis;
		if (end - start <= DOT) {
			sb.append(".");
		} else {
			sb.append("-");
		}
	}
}
