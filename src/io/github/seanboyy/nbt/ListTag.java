package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListTag extends Base implements java.lang.Iterable<Base> {

	private List<Base> tagList = new ArrayList<Base>();
	private byte tagType = 0;
	
	void write(DataOutput output) throws IOException {
		if(!this.tagList.isEmpty()){
			this.tagType = (this.tagList.get(0)).getId();
		}
		else{
			this.tagType = 0;
		}
		output.writeByte(this.tagType);
		output.writeInt(this.tagList.size());
		for(int a = 0; a < this.tagList.size(); a++){
			(this.tagList.get(a)).write(output);
		}
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(296L);
		if(depth > 512){
			throw new RuntimeException("Tried to read a tag with too high complexity: Depth > 512");
		}
		else{
			this.tagType = input.readByte();
			int a = input.readInt();
			if(this.tagType == 0 && a > 0){
				throw new RuntimeException("Missing type on ListTag");
			}
			else{
				tracker.read(32L * (long)a);
				this.tagList = new ArrayList<Base>(a);
				for(int b = 0; b < a; b++){
					Base tag = Base.createNewByType(this.tagType);
					tag.read(input, depth + 1, tracker);
					this.tagList.add(tag);
				}
			}
		}
	}

	public String toString() {
		StringBuilder sB = new StringBuilder("[");
		for(int a = 0; a < this.tagList.size(); a++){
			if(a != 0){
				sB.append(',');
			}
			sB.append(a).append(':').append(this.tagList.get(a));
		}
		return sB.append(']').toString();
	}

	public byte getId() {
		return (byte)9;
	}
	
	public void appendTag(Base tag){
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
			this.tagList.add(tag);
		}
	}

	public void set(int index, Base tag){
		if(tag.getId() == 0){
			System.out.println("WARNING!!!! INVALID EndTag ADDED TO ListTag");
		}
		else if(index >= 0 && index < this.tagList.size()){
			if(this.tagType == 0){
				this.tagType = tag.getId();
			}
			else if(this.tagType != tag.getId()){
				System.out.println("WARNING!!!! ADDING MISMATCHED TAG TYPES TO ListTag");
				return;
			}
			this.tagList.set(index, tag);
		}
		else{
			System.out.println("WARNING!!!! INDEX OUT OF BOUNDS TO SET TAG IN ListTag");
		}
	}
	
	public Base removeTag(int a){
		return this.tagList.remove(a);
	}
	
	public boolean hasNoTags(){
		return this.tagList.isEmpty();
	}
	
	public CompoundTag getCompoundTagAt(int index){
		if(index >= 0 && index < this.tagList.size()){
			Base tag = this.tagList.get(index);
			return tag.getId() == 10 ? (CompoundTag)tag : new CompoundTag();
		}
		else{
			return new CompoundTag();
		}
	}
	
	public int getIntAt(int index) {
		if(index >= 0 && index < this.tagList.size()) {
			Base tag = this.tagList.get(index);
			return tag.getId() == 3 ? ((IntTag)tag).getInt() : 0;
		}
		return 0;
	}
	
	public int[] getIntArrayAt(int index){
		if(index >= 0 && index < this.tagList.size()){
			Base tag = this.tagList.get(index);
			return tag.getId() == 11 ? ((IntArrayTag)tag).getIntArray() : new int[0];
		}
		else{
			return new int[0];
		}
	}
	
	public double getDoubleAt(int index){
		if(index >= 0 && index < this.tagList.size()){
			Base tag = this.tagList.get(index);
			return tag.getId() == 6 ? ((DoubleTag)tag).getDouble() : 0.0D; 
		}
		else{
			return 0.0D;
		}
	}
	
	public float getFloatAt(int index){
		if(index >= 0 && index < this.tagList.size()){
			Base tag = this.tagList.get(index);
			return tag.getId() == 5 ? ((FloatTag)tag).getFloat() : 0.0F;
		}
		return 0.0F;
	}
	
	public String getStringAt(int index){
		if(index >= 0 && index < this.tagList.size()){
			Base tag = this.tagList.get(index);
			return tag.getId() == 8 ? tag.getString() : tag.toString();
		}
		else{
			return "";
		}
	}
	
	public int tagCount(){
		return this.tagList.size();
	}
	
	public Base get(int index){
		return (Base)(index >= 0 && index < this.tagList.size() ? (Base)this.tagList.get(index) : new EndTag());
	}
	
	public ListTag copy() {
		ListTag listTag = new ListTag();
		listTag.tagType = this.tagType;
		for(Base tag : this.tagList){
			Base tag1 = tag.copy();
			listTag.tagList.add(tag1);
		}
		return listTag;
	}
	
	public boolean equals(Object obj){
		if(super.equals(obj)){
			ListTag listTag = (ListTag)obj;
			if(this.tagType == listTag.tagType){
				return this.tagList.equals(listTag.tagList);
			}
		}
		return false;
	}

	public int hashCode(){
		return super.hashCode() ^ this.tagList.hashCode();
	}
	
	public int getTagType(){
		return this.tagType;
	}
	
	@Override
	public java.util.Iterator<Base> iterator(){
		return tagList.iterator();
	}
}
