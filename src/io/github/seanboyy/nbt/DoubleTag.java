package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DoubleTag extends Primitive {

	private double data;
	
	DoubleTag(){}
	
	public DoubleTag(double data){
		this.data = data;
	}
	
	void write(DataOutput output) throws IOException {
		output.writeDouble(this.data);
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(128L);
		this.data = input.readDouble();
	}

	public String toString() {
		return "" + this.data + "d";
	}

	public byte getId() {
		return (byte)6;
	}

	public DoubleTag copy() {
		return new DoubleTag(this.data);
	}
	
	public boolean equals(Object other){
		return super.equals(other) && this.data == ((DoubleTag)other).data;
	}
	
	public int hashCode(){
		long a = Double.doubleToLongBits(this.data);
		return super.hashCode() ^ (int)(a ^ a >>> 32);
	}
	
	public long getLong(){
		return (long)Math.floor(this.data);
	}
	
	public int getInt(){
		return (int)Math.floor(this.data);
	}
	
	public short getShort(){
		return (short)((int)Math.floor(this.data) & 65535);
	}
	
	public byte getByte(){
		return (byte)((int)Math.floor(this.data) & 255);
	}
	
	public double getDouble(){
		return this.data;
	}
	
	public float getFloat(){
		return (float)this.data;
	}
}
