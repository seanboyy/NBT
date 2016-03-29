package org.seanb.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FloatTag extends Tag {

	private float data;
	
	FloatTag(){}
	
	public FloatTag(float data){
		this.data = data;
	}
	
	void write(DataOutput output) throws IOException {
		output.writeFloat(this.data);
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(96L);
		this.data = input.readFloat();
	}

	public String toString() {
		return "" + this.data + "f";
	}

	public byte getId() {
		return (byte)5;
	}

	public Tag copy() {
		return new FloatTag(this.data);
	}

	public boolean equals(Object obj){
		if(super.equals(obj)){
			FloatTag floatTag = (FloatTag)obj;
			return this.data == floatTag.data;
		}
		else{
			return false;
		}
	}
	
	public int hashCode(){
		return super.hashCode() ^ Float.floatToIntBits(this.data);
	}
	
	public long getLong(){
		return (long)this.data;
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
		return (double)this.data;
	}
	
	public float getFloat(){
		return this.data;
	}
}
