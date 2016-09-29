package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class Tag {
	
	/**
	 * Write data
	 * @param output
	 * @throws IOException
	 */
	abstract void write(DataOutput output) throws IOException;
	
	/**
	 * Read data
	 * @param input
	 * @param depth
	 * @param tracker
	 * @throws IOException
	 */
	abstract void read(DataInput input, int depth, SizeTracker tracker) throws IOException;

	/**
	 * Returns a string representation of the tag
	 */
	public abstract String toString();
	
	/**
	 * Get the tag's ID byte
	 * @return id
	 */
	public abstract byte getId();
	
	/**
	 * Generate a new Tag based on the ID
	 * @param id the id of the tag
	 * @return the tag associated with the ID
	 */
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
	
	/**
	 * Create a copy of the tag
	 * @return copy
	 */
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
	
	/**
	 * Get the tag string
	 * @return string representation of the tag
	 */
	protected String getString(){
		return this.toString();
	}
	
	public abstract static class Primitive extends Tag{
		/**
		 * Get the value of the tag as a long
		 * @return <code>long data</code>
		 */
		public abstract long getLong();
		
		/**
		 * Get the value of the tag as an int
		 * @return <code>int data</code>
		 */
		public abstract int getInt();
		
		/**
		 * Get the value of the tag as a short
		 * @return <code>short data</code>
		 */
		public abstract short getShort();
		
		/**
		 * Get the value of the tag as a byte
		 * @return <code>byte data</code>
		 */
		public abstract byte getByte();
		
		/**
		 * Get the value of the tag as a double
		 * @return <code>double data</code>
		 */
		public abstract double getDouble();
		
		/**
		 * Get the value of the tag as a float
		 * @return <code>float data</code>
		 */
		public abstract float getFloat();
	}
}