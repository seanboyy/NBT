package org.seanb.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EndTag extends Tag {

	void write(DataOutput output) throws IOException {}

	void read(DataInput input, int depth, SizeTracker tracker) throws IOException {
		tracker.read(64L);
	}

	public String toString() {
		return "END";
	}

	public byte getId() {
		return (byte)0;
	}

	public Tag copy() {
		return new EndTag();
	}

}
