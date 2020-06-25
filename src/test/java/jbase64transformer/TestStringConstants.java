package jbase64transformer;

public final class TestStringConstants {

	public static final String ENCODED_STRING_01 = "SGVsbG8sIFdvcmxkCg==";
	public static final String ENCODED_STRING_02 = 
			"VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cK";
	public static final String ENCODED_STRING_03 = "R29vZGJ5ZSwgV29ybGQK";

	public static final String ENCODED_STRING_01_WITH_GARBAGE = 
			"%SGVsbG8sIFdvcmxkCg==";
	public static final String ENCODED_STRING_02_WITH_GARBAGE = 
			"VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQg!b3ZlciB0aGUgbGF6eSBkb2cK";
	public static final String ENCODED_STRING_03_WITH_GARBAGE = 
			"R29vZGJ5ZSwgV29ybGQK&";
	
	public static final String ORIGINAL_STRING_01 = "Hello, World\n";
	public static final String ORIGINAL_STRING_02 = 
			"The quick brown fox jumped over the lazy dog\n";
	public static final String ORIGINAL_STRING_03 = "Goodbye, World\n";

	private TestStringConstants() { }
	
}
