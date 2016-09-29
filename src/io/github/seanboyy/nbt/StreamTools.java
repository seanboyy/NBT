package io.github.seanboyy.nbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class StreamTools {
	/**
	 * Read compressed NBT data
	 * @param is Stream of input data
	 * @return <code>CompoundTag compoundTag</code>
	 * @throws IOException
	 */
	public static CompoundTag readCompressed(InputStream is) throws IOException{
		DataInputStream dis = new DataInputStream(new BufferedInputStream(new InflaterInputStream(is)));
		CompoundTag compoundTag;
		try{
			compoundTag = read(dis, SizeTracker.INFINITE);
		}
		finally{
			dis.close();
		}
		return compoundTag;
	}
	
	/**
	 * Write compressed NBT data
	 * @param tag Tag to be compressed
	 * @param os Output Stream to write data to
	 * @throws IOException
	 */
	public static void writeCompressed(CompoundTag tag, OutputStream os) throws IOException{
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(os)));
		try{
			write(tag, dos);
		}
		finally{
			dos.close();
		}
	}
	
	/**
	 * Write the tag to a temp file, and then make the temp file the actual file
	 * @param tag Tag data to write
	 * @param file File to replace
	 * @throws IOException
	 */
	public static void safeWrite(CompoundTag tag, File file) throws IOException{
		File file1 = new File(file.getAbsolutePath() + "_tmp");
		if(file1.exists()){
			file1.delete();
		}
		
		write(tag, file1);
		
		if(file.exists()){
			file.delete();
		}
		
		if(file.exists()){
			throw new IOException("Failed to delete " + file);
		}
		else{
			file1.renameTo(file);
		}
	}
	
	/**
	 * Read until the data stream is done
	 * @param dis Data Stream to read from
	 * @return <code>CompoundTag tag</code>
	 * @throws IOException
	 */
	public static CompoundTag read(DataInputStream dis) throws IOException{
		return read(dis, SizeTracker.INFINITE);
	}
	
	/**
	 * Read the set number of bits from the stream
	 * @param input Data input to read
	 * @param tracker keeps track of the size of the tag read
	 * @return <code>CompoundTag tag</code>
	 * @throws IOException
	 */
	public static CompoundTag read(DataInput input, SizeTracker tracker) throws IOException{
		Tag tag = read(input, 0, tracker);
		if(tag instanceof CompoundTag){
			return (CompoundTag)tag;
		}
		else{
			throw new IOException("ROOT TAG MUST BE NAMED COMPOUND TAG");
		}
	}
	
	/**
	 * Write a tag to the output
	 * @param tag CompoundTag to write to output
	 * @param output output to write to
	 * @throws IOException
	 */
	public static void write(CompoundTag tag, DataOutput output) throws IOException{
		writeTag(tag, output);
	}
	
	/**
	 * Write tag to output
	 * @param tag Tag to write
	 * @param output location to write to
	 * @throws IOException
	 */
	private static void writeTag(Tag tag, DataOutput output) throws IOException{
		output.writeByte(tag.getId());
		if(tag.getId() != 0){
			output.writeUTF("");
			tag.write(output);
		}
	}
	
	private static Tag read(DataInput input, int a, SizeTracker tracker) throws IOException{
		byte b = input.readByte();
		tracker.read(8);
		if(b == 0){
			return new EndTag();
		}
		else{
			SizeTracker.readUTF(tracker, input.readUTF());
			tracker.read(32);
			Tag tag = Tag.createNewTagByType(b);
			try{
				tag.read(input, a, tracker);
				return tag;
			}catch(IOException e){
				System.out.println("ERROR READING NBT DATA");
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static void write(CompoundTag tag, File file) throws IOException{
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
		try{
			write(tag, dos);
		}
		finally{
			dos.close();
		}
	}
	
	public static CompoundTag read(File file) throws IOException{
		if(!file.exists()){
			return null;
		}
		else{
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			CompoundTag compoundTag;
			try{
				compoundTag = read(dis, SizeTracker.INFINITE);
			}
			finally{
				dis.close();
			}
			return compoundTag;
		}
	}
}
