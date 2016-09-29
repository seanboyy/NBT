package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class IntArrayTag extends Tag {

	private int[] data;
	
	IntArrayTag(){}
	
	public IntArrayTag(int[] data){
		this.data = data;
	}
	
	void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);
		for(int a = 0; a < this.data.length; a++){
			output.writeInt(this.data[a]);
		}
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(192L);
		int length = input.readInt();
		tracker.read((long)(32 * length));
		this.data = new int[length];
		for (int b = 0; b < length; b++){
			this.data[b] = input.readInt();
		}
	}

	public String toString() {
		String s = "[";
		for(int a : this.data){
			s += a + ",";
		}
		return s + "]"; 
	}

	public byte getId() {
		return (byte)11;
	}

	public Tag copy() {
		int[] aint = new int[this.data.length];
		System.arraycopy(this.data, 0, aint, 0, this.data.length);
		return new IntArrayTag(aint);
	}
	
	public boolean equals(Object obj){
		if(super.equals(obj)){
			IntArrayTag intArrayTag = (IntArrayTag)obj;
			return this.data == intArrayTag.data;
		}
		else{
			return false;
		}
	}
	
	public int hashCode(){
		return super.hashCode() ^ Arrays.hashCode(this.data);
	}
	
	public int[] getIntArray(){
		return this.data;
	}
}
