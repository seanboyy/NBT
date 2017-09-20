package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LongArrayTag extends Base{
	private long[] data;
	
	LongArrayTag(){}
	
	public LongArrayTag(long[] data) {
		this.data = data;
	}
	
	public LongArrayTag(List<Number> data) {
		this(toArray(data));
	}
	
	private static long[] toArray(List<Number> list) {
		long[] along = new long[list.size()];
		
		for(int a = 0; a < list.size(); ++a) {
			Number olong = list.get(a);
			along[a] = olong == null ? 0L : olong.longValue();
		}
		
		return along;
	}
	
	void write(DataOutput output) throws IOException{
		output.writeInt(this.data.length);
		
		for(long a : this.data) {
			output.writeLong(a);
		}
	}
	
	void read(DataInput input, int depth, SizeTracker sizeTracker) throws IOException{
		sizeTracker.read(192L);
		int a = input.readInt();
		sizeTracker.read((long)(64 * a));
		this.data = new long[a];
		
		for(int b = 0; b < a; ++b) {
			this.data[b] = input.readLong();
		}
	}
	
	public byte getId() {
		return 12;
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("[L;");
		for(int a = 0; a < this.data.length; ++a) {
			if(a != 0) {
				stringBuilder.append(',');
			}
			stringBuilder.append(this.data[a]).append('L');
		}
		return stringBuilder.append(']').toString();
	}
	
	public LongArrayTag copy() {
		long[] along = new long[this.data.length];
		System.arraycopy(this.data, 0, along, 0, this.data.length);
		return new LongArrayTag(along);
	}
	
	public boolean equals(Object other) {
		return super.equals(other) && Arrays.equals(this.data, ((LongArrayTag)other).data);
	}
	
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.data);
	}
}

