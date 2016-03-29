package org.seanb.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LongTag extends Tag {

	private long data;
	
	LongTag(){}
	
	public LongTag(long data){
		this.data = data;
	}
	
	void write(DataOutput output) throws IOException {
		output.writeLong(this.data);
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(128L);
		this.data = input.readLong();
	}

	public String toString() {
		return "" + this.data + "L";
	}

	public byte getId() {
		return (byte)4;
	}

	public Tag copy() {
		return new LongTag(this.data);
	}
	
	public boolean equals(Object obj){
		if(super.equals(obj)){
			LongTag longTag = (LongTag)obj;
			return this.data == longTag.data;
		}
		else{
			return false;
		}
	}
	
    public int hashCode()
    {
        return super.hashCode() ^ (int)(this.data ^ this.data >>> 32);
    }

    public long getLong()
    {
        return this.data;
    }

    public int getInt()
    {
        return (int)(this.data & -1L);
    }

    public short getShort()
    {
        return (short)((int)(this.data & 65535L));
    }

    public byte getByte()
    {
        return (byte)((int)(this.data & 255L));
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
