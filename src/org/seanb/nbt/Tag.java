package org.seanb.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class Tag {
	public static final String[] TAG_TYPES = new String[] {"END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]"};
	
	abstract void write(DataOutput output) throws IOException;
	
	abstract void read(DataInput input, int depth, SizeTracker tracker) throws IOException;

	public abstract String toString();
	
	public abstract byte getId();
	
	protected static Tag createNewTagByType(byte id){
		switch (id){
		case 0:
			return new EndTag();
		case 1:
			return new ByteTag();
		case 2:
			return new ShortTag();
		case 3:
			return new IntTag();
		case 4:
			return new LongTag();
		case 5:
			return new FloatTag();
		case 6:
			return new DoubleTag();
		case 7:
			return new ByteArrayTag();
		case 8:
			return new StringTag();
		case 9:
			return new ListTag();
		case 10:
			return new CompoundTag();
		case 11:
			return new IntArrayTag();
		default:
			return null;
		}
	}
	
	public abstract Tag copy();
	
	public boolean hasNoTags(){
		return false;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof Tag)){
			return false;
		}
		else{
			Tag tag = (Tag)obj;
			return this.getId() == tag.getId();
		}
	}
	
	public int hashCode(){
		return this.getId();
	}
	
	protected String getString(){
		return this.toString();
	}
	
	public abstract static class Primitive extends Tag{
		public abstract long getLong();
		
		public abstract int getInt();
		
		public abstract short getShort();
		
		public abstract byte getByte();
		
		public abstract double getDouble();
		
		public abstract float getFloat();
	}
}