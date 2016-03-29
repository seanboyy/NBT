package org.seanb.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ByteArrayTag extends Tag {

	private byte[] data;
	
	ByteArrayTag(){}
	
	public ByteArrayTag(byte[] data){
		this.data = data;
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
		return "[" + this.data.length + " bytes]";
	}

	public byte getId() {
		return (byte)7;
	}

	public Tag copy() {
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
