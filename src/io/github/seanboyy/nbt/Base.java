package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class Base {
	public static final String[] TYPES = new String[] {"END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LSIT", "COMPOUND", "INT[]", "LONG[]"};
	
	abstract void write(DataOutput output) throws IOException;
	
	abstract void read(DataInput input, int depth, SizeTracker sizeTracker) throws IOException;
	
	public abstract String toString();
	
	public abstract byte getId();
	
	protected static Base createNewByType(byte id) {
		switch(id) {
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
		case 12:
			return new LongArrayTag();
		default:
			return null;
		}
	}
	
	public static String getTagTypeName(int type) {
		switch(type) {
		case 0:
			return "TAG_End";
		case 1:
			return "TAG_Byte";
		case 2:
			return "TAG_Short";
		case 3:
			return "TAG_Int";
		case 4:
			return "TAG_Long";
		case 5:
			return "TAG_Float";
		case 6:
			return "TAG_Double";
		case 7:
			return "TAG_Byte_Array";
		case 8:
			return "TAG_String";
		case 9:
			return "TAG_List";
		case 10:
			return "TAG_Compound";
		case 11:
			return "TAG_Int_Array";
		case 12:
			return "TAG_Long_Array";
		case 99:
			return "Any Numeric Tag";
		default:
			return "Unknown";
		}
	}
	
	public abstract Base copy();
	
	public boolean hasNoTags() {
		return false;
	}
	
	public boolean equals(Object other) {
		return other instanceof Base && this.getId() == ((Base)other).getId();
	}
	
	public int hashCode() {
		return this.getId();
	}
	
	protected String getString() {
		return this.toString();
	}
}
