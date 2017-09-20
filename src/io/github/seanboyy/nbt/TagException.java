package io.github.seanboyy.nbt;

public class TagException extends Exception {

	private static final long serialVersionUID = -8695079368277503835L;

	public TagException(String message, String json, int cursor) {
		super(message + " at: " + slice(json, cursor));
	}
	
	private static String slice(String json, int cursor) {
		StringBuilder stringbuilder = new StringBuilder();
		int a = Math.min(json.length(), cursor);
		if(a > 35) {
			stringbuilder.append("...");
		}
		stringbuilder.append(json.substring(Math.max(0, a - 35), a));
		stringbuilder.append("<--[HERE]");
		return stringbuilder.toString();
	}
}
