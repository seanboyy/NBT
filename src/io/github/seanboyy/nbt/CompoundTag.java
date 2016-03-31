package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CompoundTag extends Tag {
	
	private Map<String, Tag> tagMap = new HashMap<String, Tag>();
	
	void write(DataOutput output) throws IOException {
		for(String s : this.tagMap.keySet()){
			Tag tag = (Tag)this.tagMap.get(s);
			writeEntry(s, tag, output);
		}
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(384L);
		
		if(depth > 512){
			throw new RuntimeException("Tried to read tag with too much complexity, depth > 512");
		}
		else{
			this.tagMap.clear();
			byte b;
			
			while((b = readType(input, tracker)) != 0){
				String s = readKey(input, tracker);
				tracker.read((long)(224 + 16 * s.length()));
				Tag tag = readNBT(b, s, input, depth + 1, tracker);
				
				if (this.tagMap.put(s,  tag) != null){
					tracker.read(288L);
				}
			}
		}
	}

	public Set<String> getKeySet(){
		return this.tagMap.keySet();
	}
	
	public byte getId() {
		return (byte)10;
	}

	public void setTag(String key, Tag value){
		this.tagMap.put(key, value);
	}
	
	public void setByte(String key, byte value){
		this.tagMap.put(key, new ByteTag(value));
	}
	
	public void setShort(String key, short value){
		this.tagMap.put(key, new ShortTag(value));
	}
	
	public void setLong(String key, long value){
		this.tagMap.put(key, new LongTag(value));
	}
	
	public void setFloat(String key, float value){
		this.tagMap.put(key, new FloatTag(value));
	}
	
	public void setDouble(String key, double value){
		this.tagMap.put(key, new DoubleTag(value));
	}
	
	public void setString(String key, String value){
		this.tagMap.put(key, new StringTag(value));
	}
	
	public void setByteArray(String key, byte[] value){
		this.tagMap.put(key, new ByteArrayTag(value));
	}
	
	public void setIntArray(String key, int[] value){
		this.tagMap.put(key, new IntArrayTag(value));
	}
	
	public void setBoolean(String key, boolean value){
		this.setByte(key, (byte)(value ? 1 : 0));
	}
	
	public Tag getTag(String key){
		return (Tag)this.tagMap.get(key);
	}
	
	public byte getTagId(String key){
		Tag tag = (Tag)this.tagMap.get(key);
		return tag != null ? tag.getId() : 0; 
	}
	
	public boolean hasKey(String key){
		return this.tagMap.containsKey(key);
	}
	
	public boolean hasKey(String key, int type){
		int a = this.getTagId(key);
		
		if(a == type){
			return true;
		}
		else if(type != 99){
			if (a > 0){
				;
			}
			return false;
		}
		else{
			return a == 1 || a == 2 || a == 3 || a == 4 || a == 5 || a == 6;
		}
	}
	
	public byte getByte(String key){
		try{
			return !this.hasKey(key, 99) ? 0 : ((Tag.Primitive)this.tagMap.get(key)).getByte();
		} catch(ClassCastException e){
			return (byte)0;
		}
	}
	
	public short getShort(String key){
		try{
			return !this.hasKey(key, 99) ? 0 : ((Tag.Primitive)this.tagMap.get(key)).getShort();
		} catch(ClassCastException e){
			return (short)0;
		}
	}
	
	public int getInteger(String key){
		try{
			return !this.hasKey(key, 99) ? 0 : ((Tag.Primitive)this.tagMap.get(key)).getInt();
		} catch(ClassCastException e){
			return 0;
		}
	}
	
	public long getLong(String key){
		try{
			return !this.hasKey(key, 99) ? 0 : ((Tag.Primitive)this.tagMap.get(key)).getInt();
		} catch(ClassCastException e){
			return 0;
		}
	}
	
	public float getFloat(String key){
		try{
			return !this.hasKey(key, 99) ? 0.0F : ((Tag.Primitive)this.tagMap.get(key)).getFloat();
		}catch(ClassCastException e){
			return 0.0F;
		}
	}
	
	public double getDouble(String key){
		try{
			return !this.hasKey(key, 99) ? 0.0D : ((Tag.Primitive)this.tagMap.get(key)).getDouble();
		}catch(ClassCastException e){
			return 0.0D;
		}
	}
	
	public String getString(String key){
		try{
			return !this.hasKey(key, 8) ? "" : ((Tag)this.tagMap.get(key)).getString();
		}catch(ClassCastException e){
			return "";
		}
	}
	
	public byte[] getByteArray(String key){
		try{
			return !this.hasKey(key, 7) ? new byte[0] : ((ByteArrayTag)this.tagMap.get(key)).getByteArray();
		}catch(ClassCastException e){
			System.out.println("CRITICAL ERROR IN READING BYTE ARRAY TAG");
			e.printStackTrace();
			return null;
		}
	}
	
	public int[] getIntArray(String key){
		try{
			return !this.hasKey(key, 11) ? new int[0] : ((IntArrayTag)this.tagMap.get(key)).getIntArray();
		}catch(ClassCastException e){
			System.out.println("CRITICAL ERROR IN READING INT ARRAY TAG");
			e.printStackTrace();
			return null;
		}
	}
	
	public CompoundTag getCompoundTag(String key){
		try{
			return !this.hasKey(key, 10) ? new CompoundTag() : (CompoundTag)this.tagMap.get(key);
		}catch(ClassCastException e){
			System.out.println("CRITICAL ERROR IN READING COMPOUND TAG");
			e.printStackTrace();
			return null;
		}
	}
	
	public ListTag getListTag(String key, int type){
		try{
			if(this.getTagId(key) != 9){
				return new ListTag();
			}
			else{
				ListTag listTag = (ListTag)this.tagMap.get(key);
				return listTag.tagCount() > 0 && listTag.getTagType() != type ? new ListTag() : listTag;
			}
		}catch(ClassCastException e){
			System.out.println("CRITICAL ERROR IN READING LIST TAG");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean getBoolean(String key){
		return this.getByte(key) != 0;
	}
		
	public String toString() {
		StringBuilder sB = new StringBuilder("{");
		
		for (Entry<String, Tag> entry : this.tagMap.entrySet()){
			if(sB.length() != 1){
				sB.append('.');
			}
			sB.append((String)entry.getKey()).append(':').append(entry.getValue());
		}
		
		return sB.append('}').toString();
	}

	
	public Tag copy() {
		CompoundTag compoundTag = new CompoundTag();
		
		for (String s : this.tagMap.keySet()){
			compoundTag.setTag(s, ((Tag)this.tagMap.get(s)).copy());
		}
		return compoundTag;
	}
	
	public boolean equals(Object obj){
		if(super.equals(obj)){
			CompoundTag compoundTag = (CompoundTag)obj;
			return this.tagMap.entrySet().equals(compoundTag.tagMap.entrySet());
		}
		else{
			return false;
		}
	}
	
	public int hashCode(){
		return super.hashCode() ^ this.tagMap.hashCode();
	}
	
	private static void writeEntry(String name, Tag data, DataOutput output) throws IOException{
		output.writeByte(data.getId());
		
		if(data.getId() != 0){
			output.writeUTF(name);
			data.write(output);
		}
	}
	
	private static byte readType(DataInput input, SizeTracker tracker) throws IOException{
		tracker.read(8);
		return input.readByte();
	}
	
	private static String readKey(DataInput input, SizeTracker tracker) throws IOException{
		return input.readUTF();
	}
	
	static Tag readNBT(byte id, String key, DataInput input, int depth, SizeTracker tracker) throws IOException{
		tracker.read(32);
		Tag tag = Tag.createNewTagByType(id);
		
		try{
			tag.read(input,  depth, tracker);
			return tag;
		}catch(IOException e){
			System.out.println("CRITICAL ERROR READING NBT DATA");
			e.printStackTrace();
			return null;
		}
	}
	
	public void merge(CompoundTag other){
		for(String s : other.tagMap.keySet()){
			Tag tag = (Tag)other.tagMap.get(s);
			
			if(tag.getId() == 10){
				if(this.hasKey(s, 10)){
					CompoundTag compoundTag = this.getCompoundTag(s);
					compoundTag.merge((CompoundTag)tag);
				}
				else{
					this.setTag(s, tag.copy());
				}
			}
			else{
				this.setTag(s, tag.copy());
			}
		}
	}
}
