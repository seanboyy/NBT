package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListTag extends Tag {

	private List<Tag> data = new ArrayList<Tag>();
	private byte tagType = 0;
	
	void write(DataOutput output) throws IOException {
		if(!this.data.isEmpty()){
			this.tagType = ((Tag)this.data.get(0)).getId();
		}
		else{
			this.tagType = 0;
		}
		output.writeByte(this.tagType);
		output.writeInt(this.data.size());
		for(int a = 0; a < this.data.size(); a++){
			((Tag)this.data.get(a)).write(output);
		}
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(296L);
		if(depth > 512){
			throw new RuntimeException("Tried to read a list with too high complexity: Depth > 512");
		}
		else{
			this.tagType = input.readByte();
			int a = input.readInt();
			if(this.tagType == 0 && a > 0){
				throw new RuntimeException("Missing type on ListTag");
			}
			else{
				tracker.read(32L * (long)a);
				this.data = new ArrayList<Tag>();
				for(int b = 0; b < a; b++){
					Tag tag = Tag.createNewTagByType(this.tagType);
					tag.read(input, depth + 1, tracker);
					this.data.add(tag);
				}
			}
		}
	}

	public String toString() {
		StringBuilder sB = new StringBuilder("[");
		for(int a = 0; a < this.data.size(); a++){
			if(a != 0){
				sB.append(',');
			}
			sB.append(a).append(':').append(this.data.get(a));
		}
		return sB.append(']').toString();
	}

	public byte getId() {
		return (byte)9;
	}
	
	public void appendTag(Tag tag){
		if(tag.getId() == 0){
			System.out.println("WARNING!!!! INVALID EndTag ADDED TO ListTag");
		}
		else{
			if(this.tagType == 0){
				this.tagType = tag.getId();
			}
			else if(this.tagType != tag.getId()){
				System.out.println("WARNING!!!! ADDING MISMATCHED TAG TYPES TO ListTag");
				return;
			}
			this.data.add(tag);
		}
	}

	public void set(int index, Tag tag){
		if(tag.getId() == 0){
			System.out.println("WARNING!!!! INVALID EndTag ADDED TO ListTag");
		}
		else if(index >= 0 && index < this.data.size()){
			if(this.tagType == 0){
				this.tagType = tag.getId();
			}
			else if(this.tagType != tag.getId()){
				System.out.println("WARNING!!!! ADDING MISMATCHED TAG TYPES TO ListTag");
				return;
			}
			this.data.set(index, tag);
		}
		else{
			System.out.println("WARNING!!!! INDEX OUT OF BOUNDS TO SET TAG IN ListTag");
		}
	}
	
	public Tag removeTag(int a){
		return (Tag)this.data.remove(a);
	}
	
	public boolean hasNoTags(){
		return this.data.isEmpty();
	}
	
	public CompoundTag getCompoundTagAt(int index){
		if(index >= 0 && index < this.data.size()){
			Tag tag = (Tag)this.data.get(index);
			return tag.getId() == 10 ? (CompoundTag)tag : new CompoundTag();
		}
		else{
			return new CompoundTag();
		}
	}
	
	public int[] getIntArrayAt(int index){
		if(index >= 0 && index < this.data.size()){
			Tag tag = (Tag)this.data.get(index);
			return tag.getId() == 11 ? ((IntArrayTag)tag).getIntArray() : new int[0];
		}
		else{
			return new int[0];
		}
	}
	
	public double getDoubleAt(int index){
		if(index >= 0 && index < this.data.size()){
			Tag tag = (Tag)this.data.get(index);
			return tag.getId() == 6 ? ((DoubleTag)tag).getDouble() : 0.0D; 
		}
		else{
			return 0.0D;
		}
	}
	
	public float getFloatAt(int index){
		if(index >= 0 && index < this.data.size()){
			Tag tag = (Tag)this.data.get(index);
			return tag.getId() == 5 ? ((FloatTag)tag).getFloat() : 0.0F;
		}
		else{
			return 0.0F;
		}
	}
	
	public String getStringAt(int index){
		if(index >= 0 && index < this.data.size()){
			Tag tag = (Tag)this.data.get(index);
			return tag.getId() == 8 ? tag.getString() : tag.toString();
		}
		else{
			return "";
		}
	}
	
	public int tagCount(){
		return this.data.size();
	}
	
	public Tag get(int index){
		return (Tag)(index >= 0 && index < this.data.size() ? (Tag)this.data.get(index) : new EndTag());
	}
	
	public Tag copy() {
		ListTag listTag = new ListTag();
		listTag.tagType = this.tagType;
		for(Tag tag : this.data){
			Tag tag1 = tag.copy();
			listTag.data.add(tag1);
		}
		return listTag;
	}
	
	public boolean equals(Object obj){
		if(super.equals(obj)){
			ListTag listTag = (ListTag)obj;
			if(this.tagType == listTag.tagType){
				return this.data.equals(listTag.data);
			}
		}
		return false;
	}

	public int hashCode(){
		return super.hashCode() ^ this.data.hashCode();
	}
	
	public int getTagType(){
		return this.tagType;
	}
}
