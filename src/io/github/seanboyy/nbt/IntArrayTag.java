package io.github.seanboyy.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class IntArrayTag extends Base {

	private int[] intArray;
	
	IntArrayTag(){}
	
	public IntArrayTag(int[] intArray){
		this.intArray = intArray;
	}
	
	public IntArrayTag(List<Number> array) {
		this(toArray(array));
	}
	
	private static int[] toArray(List<Number> array) {
		int[] aint = new int[array.size()];
		for(int a = 0; a < array.size(); ++a) {
			Number integer = array.get(a);
			aint[a] = integer == null ? 0 : integer.intValue();
		}
		return aint;
	}
	
	void write(DataOutput output) throws IOException {
		output.writeInt(this.intArray.length);
		for(int a = 0; a < this.intArray.length; a++){
			output.writeInt(this.intArray[a]);
		}
	}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(192L);
		int length = input.readInt();
		tracker.read((long)(32 * length));
		this.intArray = new int[length];
		for (int b = 0; b < length; b++){
			this.intArray[b] = input.readInt();
		}
	}

	public String toString() {
		String s = "[I;";
		for(int a : this.intArray){
			s += a + ',';
		}
		return s + ']'; 
	}

	public byte getId() {
		return (byte)11;
	}

	public IntArrayTag copy() {
		int[] aint = new int[this.intArray.length];
		System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
		return new IntArrayTag(aint);
	}
	
	public boolean equals(Object other){
		return super.equals(other) && Arrays.equals(this.intArray, ((IntArrayTag)other).intArray);
	}
	
	public int hashCode(){
		return super.hashCode() ^ Arrays.hashCode(this.intArray);
	}
	
	public int[] getIntArray(){
		return this.intArray;
	}
}
