package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ByteArrayTag extends Base {

	private byte[] data;
	
	ByteArrayTag(){}
	
	public ByteArrayTag(byte[] data){
		this.data = data;
	}

	public ByteArrayTag(List<Number> data) {
		this(toArray(data));
	}
	
	private static byte[] toArray(List<Number> data) {
		byte[] abyte = new byte[data.size()];
		for(int a = 0; a < data.size(); ++a) {
			Number obyte = data.get(a);
			abyte[a] = obyte == null ? 0 : obyte.byteValue();
		}
		return abyte;
	}

	void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);
		output.write(this.data);
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(192L);
		int a = input.readInt();
		tracker.read((long)(8 * a));
		this.data = new byte[a];
		input.readFully(this.data);
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("[B;");
		for(int a = 0; a < this.data.length; ++a) {
			if(a != 0) {
				stringBuilder.append(',');
			}
			stringBuilder.append((int)this.data[a]).append('B');
		}
		return stringBuilder.append(']').toString();
	}

	public byte getId() {
		return (byte)7;
	}

	public Base copy() {
		byte[] abyte = new byte[this.data.length];
		System.arraycopy(this.data, 0, abyte, 0, this.data.length);
		return new ByteArrayTag(abyte);
	}
	
	public boolean equals(Object obj){
		return super.equals(obj) ? Arrays.equals(this.data, ((ByteArrayTag)obj).data) : false;
	}
	
	public int hashCode(){
		return super.hashCode() ^ Arrays.hashCode(this.data);
	}
	
	public byte[] getByteArray(){
		return this.data;
	}
}
