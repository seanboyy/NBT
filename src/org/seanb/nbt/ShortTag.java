package org.seanb.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ShortTag extends Tag {

	private short data;
	
	ShortTag(){}
	
	public ShortTag(short data){
		this.data = data;
	}
	
	void write(DataOutput output) throws IOException {
		output.writeShort(this.data);
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(80L);
		this.data = input.readShort();
	}

	public String toString() {
		return "" + this.data + "s";
	}

	public byte getId() {
		return (byte)2;
	}

	public Tag copy() {
		return new ShortTag(this.data);
	}
	
	public boolean equals(Object obj){
		if(super.equals(obj)){
			ShortTag shortTag = (ShortTag)obj;
			return this.data == shortTag.data;
		}
		
		else{
			return false;
		}
	}
	
	public int hashCode()
    {
        return super.hashCode() ^ this.data;
    }

    public long getLong()
    {
        return (long)this.data;
    }

    public int getInt()
    {
        return this.data;
    }

    public short getShort()
    {
        return this.data;
    }

    public byte getByte()
    {
        return (byte)(this.data & 255);
    }

    public double getDouble()
    {
        return (double)this.data;
    }

    public float getFloat()
    {
        return (float)this.data;
    }
}
