package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StringTag extends Base {

	private String data;
	
	public StringTag(){
		this.data = "";
	}
	
	public StringTag(String data){
		this.data = data;
		if(data == null){
			throw new IllegalArgumentException("EMPTY STRINGS ARE NOT ALLOWED");
		}
	}
	
	void write(DataOutput output) throws IOException {
		output.writeUTF(this.data);
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(288L);
		this.data = input.readUTF();
		SizeTracker.readUTF(tracker, data);
	}

	public String toString() {
		return "\"" + this.data.replace("\"", "\\\"") + "\"";
	}

	public byte getId() {
		return (byte)8;
	}

	public StringTag copy() {
		return new StringTag(this.data);
	}

	public boolean hatNoTags(){
		return this.data.isEmpty();
	}
	
	public boolean equals(Object obj){
		if(super.equals(obj)){
			StringTag stringTag = (StringTag)obj;
			return this.data == null && stringTag.data == null || this.data != null && this.data.equals(stringTag.data);
		}
		else{
			return false;
		}
	}
	
	public int hashCode(){
		return super.hashCode() ^ this.data.hashCode();
	}
	
	public String getString(){
		return this.data;
	}
	
	public static String quoteAndEscape(String data) {
		StringBuilder stringBuilder = new StringBuilder("\"");
		for(int a = 0; a < data.length(); ++a) {
			char c = data.charAt(a);
			if(c == '\\' || c == '"') {
				stringBuilder.append('\\');
			}
			stringBuilder.append(c);
		}
		return stringBuilder.append('"').toString();
	}
}
