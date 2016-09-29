package io.github.seanboyy.nbt;

public class SizeTracker {
	/**
	 * Create an infinite SizeTracker that never stops
	 */
	public static final SizeTracker INFINITE = new SizeTracker(0L){
		public void read(long bits){}
	};
	
	private final long max;
	private long read;
	
	public SizeTracker(long max){
		this.max = max;
	}
	
	public void read(long bits){
		this.read += bits / 8L;
		
		if (this.read > this.max){
			throw new RuntimeException("Tried to read tag that was too big; Tried allocating: " + this.read + " bytes where max allowed: " + this.max);
		}
	}
	
	public static void readUTF(SizeTracker tracker, String data){
		tracker.read(16);
		if(data == null) return;
		
		int len = data.length();
		int utflen = 0;
		
		for (int a = 0; a < len; a++){
			int b = data.charAt(a);
			if((b >= 0x0001) && (b <= 0x007F)) utflen +=1;
			else if(b > 0x07FF) utflen += 3;
			else utflen += 2;
		}
		tracker.read(8 * utflen);
	}
}