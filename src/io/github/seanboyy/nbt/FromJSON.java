package io.github.seanboyy.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.lang.Number;

public class FromJSON {
	private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
	private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
	private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
	private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?0|[1-9][0-9]*)b", 2);
	private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
	private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
	private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
	private final String string;
	private int cursor;
	
	
	public static CompoundTag getTagFromJSON(String jsonString) throws TagException{
		return (new FromJSON(jsonString)).readSingleStruct();
	}
	
	@VisibleForTesting
	CompoundTag readSingleStruct() throws TagException{
		CompoundTag tag = this.readStruct();
		this.skipWhitespace();
		if(this.canRead()) {
			++this.cursor;
			throw this.exception("Trailing data found");
		}
		else {
			return tag;
		}
	}
	
	@VisibleForTesting
	FromJSON(String stringIn){
		this.string = stringIn;
	}
	
	protected String readKey() throws TagException{
		this.skipWhitespace();
		if(!this.canRead()) {
			throw this.exception("Expected key");
		}
		else {
			return this.peek() == '"' ? this.readQuotedString() : this.readString();
		}
	}
	
	private TagException exception(String message) {
		return new TagException(message, this.string, this.cursor);
	}
	
	protected Base readTypedValue() throws TagException{
		this.skipWhitespace();
		if(this.peek() == '"') {
			return new StringTag(this.readQuotedString());
		}
		else {
			String s = this.readString();
			if(s.isEmpty()) {
				throw this.exception("Expected value");
			}
			else {
				return this.type(s);
			}
		}
	}
	
	private Base type(String stringin) {
		try {
			if(FLOAT_PATTERN.matcher(stringin).matches()) {
				return new FloatTag(Float.parseFloat(stringin.substring(0, stringin.length() - 1)));
			}
			if(BYTE_PATTERN.matcher(stringin).matches()) {
				return new ByteTag(Byte.parseByte(stringin.substring(0, stringin.length() - 1)));
			}
			if(LONG_PATTERN.matcher(stringin).matches()) {
				return new LongTag(Long.parseLong(stringin.substring(0, stringin.length() - 1)));
			}
			if(SHORT_PATTERN.matcher(stringin).matches()) {
				return new ShortTag(Short.parseShort(stringin.substring(0, stringin.length() - 1)));
			}
			if(INT_PATTERN.matcher(stringin).matches()) {
				return new IntTag(Integer.parseInt(stringin));
			}
			if(DOUBLE_PATTERN.matcher(stringin).matches()) {
				return new DoubleTag(Double.parseDouble(stringin.substring(0, stringin.length() - 1)));
			}
			if(DOUBLE_PATTERN_NOSUFFIX.matcher(stringin).matches()) {
				return new DoubleTag(Double.parseDouble(stringin));
			}
			if("true".equalsIgnoreCase(stringin)) {
				return new ByteTag((byte)1);
			}
			if("false".equalsIgnoreCase(stringin)) {
				return new ByteTag((byte)0);
			}
		}
		catch(NumberFormatException e) {
			;
		}
		return new StringTag(stringin);
	}
	
	private String readQuotedString() throws TagException{
		int a = ++this.cursor;
		StringBuilder stringbuilder = null;
		boolean flag = false;
		while(this.canRead()) {
			char c = this.pop();
			if(flag) {
				if(c != '\\' && c != '"') {
					throw this.exception("Invalid escape of '" + c + "'");
				}
				flag = false;
			}
			else {
				if(c == '\\') {
					flag = true;
					if(stringbuilder == null) {
						stringbuilder = new StringBuilder(this.string.substring(a, this.cursor - 1));
					}
					continue;
				}
				if(c == '"') {
					return stringbuilder == null ? this.string.substring(a, this.cursor - 1) : stringbuilder.toString();
				}
			}
			if(stringbuilder != null) {
				stringbuilder.append(c);
			}
		}
		throw this.exception("Missing termination quote");
	}
	
	private String readString() {
		int a;
		for(a = this.cursor; this.canRead() && this.isAllowedInKey(this.peek()); ++ this.cursor) {
			;
		}
		return this.string.substring(a, this.cursor);
	}
	
	protected Base readValue() throws TagException{
		this.skipWhitespace();
		if(!this.canRead()) {
			throw this.exception("Expected value");
		}
		else {
			char c = this.peek();
			if(c == '{') {
				return this.readStruct();
			}
			else {
				return c == '[' ? this.readList() : this.readTypedValue();
			}
		}
	}
	
	protected Base readList() throws TagException{
		return this.canRead(3) && this.peek(1) != '"' && this.peek(2) == ';' ? this.readArrayTag() : this.readListTag();
	}
	
	protected CompoundTag readStruct() throws TagException{
		this.expect('{');
		CompoundTag tag = new CompoundTag();
		this.skipWhitespace();
		while(this.canRead() && this.peek() != '}') {
			String s = this.readKey();
			if(s.isEmpty()) {
				throw this.exception("Expected non-empty key");
			}
			this.expect(':');
			tag.setTag(s, this.readValue());
			if(!this.hasElementSeparator()) {
				break;
			}	
			if(!this.canRead()) {
				throw this.exception("Expected key");
			}
		}
		this.expect('}');
		return tag;
	}
	
	private Base readListTag() throws TagException{
		this.expect('[');
		this.skipWhitespace();
		if(!this.canRead()) {
			throw this.exception("Expected value");
		}
		else {
			ListTag tag = new ListTag();
			int a = -1;
			while(this.peek() != ']') {
				Base base = this.readValue();
				int b = base.getId();
				if(a < 0) {
					a = b;
				}
				else if(b != a) {
					throw this.exception("Unable to insert" + Base.getTagTypeName(b) + " into ListTag of type " + Base.getTagTypeName(a));
				}
				tag.appendTag(base);
				if(!this.hasElementSeparator()) {
					break;
				}
				if(!this.canRead()) {
					throw this.exception("Expected value");
				}
			}
			this.expect(']');
			return tag;
		}
	}
	
	private Base readArrayTag() throws TagException{
		this.expect('[');
		char c = this.pop();
		this.pop();
		this.skipWhitespace();
		if(!this.canRead()) {
			throw this.exception("Expected value");
		}
		else if(c == 'B') {
			return new ByteArrayTag(this.readArray((byte)7, (byte)1));
		}
		else if(c == 'L') {
			return new LongArrayTag(this.readArray((byte)12, (byte)4));
		}
		else if(c == 'I') {
			return new IntArrayTag(this.readArray((byte)11, (byte)3));
		}
		else {
			throw this.exception("Invalid array type '" + c + "' found");
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Number> List<T> readArray(byte type, byte length) throws TagException{
		List<T> list = new ArrayList<T>();
		while(true) {
			if(this.peek() != ']') {
				Base base = this.readValue();
				int a = base.getId();
				if(a != length) {
					throw this.exception("Unable to insert " + Base.getTagTypeName(a) + " into " + Base.getTagTypeName(type));
				}
				if(length == 1) {
					list.add((T)Byte.valueOf(((Primitive)base).getByte()));
				}
				else if(length == 4) {
					list.add((T)Long.valueOf(((Primitive)base).getLong()));
				}
				else {
					list.add((T)Integer.valueOf(((Primitive)base).getInt()));
				}
				if(this.hasElementSeparator()) {
					if(!this.canRead()) {
						throw this.exception("Expected value");
					}
					continue;
				}
			}
			this.expect(']');
			return list;
		}
	}
	
	private void skipWhitespace() {
		while(this.canRead() && Character.isWhitespace(this.peek())) {
			++this.cursor;
		}
	}
	
	private boolean hasElementSeparator() {
		this.skipWhitespace();
		if(this.canRead() && this.peek() == ',') {
			++this.cursor;
			this.skipWhitespace();
			return true;
		}
		else {
			return false;
		}
	}
	
	private void expect(char expected) throws TagException{
		this.skipWhitespace();
		boolean flag = this.canRead();
		if(flag && this.peek() == expected) {
			++this.cursor;
		}
		else {
			throw new TagException("Expected '" + expected + "' but got '" + (flag ? this.peek() : "<EOF>") + "'", this.string, this.cursor + 1);
		}
	}
	
	protected boolean isAllowedInKey(char charIn) {
		return charIn >= '0' && charIn <= '9' || charIn >= 'A' && charIn <= 'Z' || charIn >= 'a' && charIn <= 'z' || charIn == '_' || charIn == '-' || charIn == '.' || charIn == '+';
	}
	
	private boolean canRead(int offset) {
		return this.cursor + offset < this.string.length();
	}
	
	boolean canRead() {
		return this.canRead(0);
	}
	
	private char peek(int offset) {
		return this.string.charAt(this.cursor + offset);
	}
	
	private char peek() {
		return this.peek(0);
	}
	
	private char pop() {
		return this.string.charAt(this.cursor++);
	}
}