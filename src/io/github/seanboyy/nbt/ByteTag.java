package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ByteTag extends Primitive {

	private byte data;
	
	ByteTag(){}
	
	public ByteTag(byte data){
		this.data = data;
	}
	
	void write(DataOutput output) throws IOException {
		output.writeByte(this.data);
	}


	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(72L);
		this.data = input.readByte();
	}


	public String toString() {
		return "" + this.data + "b";
	}

	public byte getId() {
		return (byte)1;
	}

	public ByteTag copy() {
		return new ByteTag(this.data);
	}

	public boolean equals(Object obj){
		if(super.equals(obj)){
			ByteTag byteTag = (ByteTag)obj;
			return this.data == byteTag.data;
		}
		else{
			return false;
		}
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
		return (short)this.data;
	}
	
	public byte getByte(){
		return this.data;
	}
	
	public double getDouble(){
		return (double)this.data;
	}
	
	public float getFloat(){
		return (float)this.data;
	}
}
