package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FloatTag extends Primitive {

	/**
	 * Data stored in the tag
	 */
	private float data;
	
	/**
	 * Create a new empty float tag
	 */
	FloatTag(){}
	
	/**
	 * Initialize a new float tag
	 * @param data value to set the tag to
	 */
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
		return this.data + "f";
	}

	public byte getId() {
		return (byte)5;
	}

	public FloatTag copy() {
		return new FloatTag(this.data);
	}

	public boolean equals(Object other){
		return super.equals(other) && this.data == ((FloatTag)other).data;
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
