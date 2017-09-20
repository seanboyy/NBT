package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ShortTag extends Primitive {

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

	public ShortTag copy() {
		return new ShortTag(this.data);
	}
	
	public boolean equals(Object other){
		return super.equals(other) && this.data == ((ShortTag)other).data;
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
