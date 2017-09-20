package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntTag extends Primitive {

	private int data;
	
	IntTag(){}
	
	public IntTag(int data){
		this.data = data;
	}
	
	void write(DataOutput output) throws IOException {
		output.writeInt(this.data);
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(96L);
		this.data = input.readInt();
	}

	public String toString() {
		return "" + this.data;
	}

	public byte getId() {
		return (byte)3;
	}

	public IntTag copy() {
		return new IntTag(this.data);
	}
	
	public boolean equals(Object other){
		return super.equals(other) && this.data == ((IntTag)other).data;
	}

	public int hashCode(){
		return super.hashCode() ^ this.data;
	}
	
	public long getLong(){
		return (long)this.data;
	}
	
	public int getInt(){
		return this.data;
	}
	
	public short getShort(){
		return (short)(this.data & 65535);
	}
	
	public byte getByte(){
		return (byte)(this.data & 255);
	}
	
	public double getDouble(){
		return (double)this.data;
	}
	
	public float getFloat(){
		return (float)this.data;
	}
}
