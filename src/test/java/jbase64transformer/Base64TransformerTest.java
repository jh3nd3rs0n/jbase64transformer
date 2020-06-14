package jbase64transformer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

public class Base64TransformerTest {

	private static final String ENCODED_STRING_01 = "SGVsbG8sIFdvcmxkCg==";
	private static final String ENCODED_STRING_02 = 
			"VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cK";
	private static final String ENCODED_STRING_03 = "R29vZGJ5ZSwgV29ybGQK";
	private static final String ORIGINAL_STRING_01 = "Hello, World\n";
	private static final String ORIGINAL_STRING_02 = 
			"The quick brown fox jumped over the lazy dog\n";
	private static final String ORIGINAL_STRING_03 = "Goodbye, World\n";

	private static String wrap(final String string, final int columnLimit) {
		int column = 0;
		String lineSeparator = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		char[] chars = string.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			sb.append(c);
			if (columnLimit > 0) {
				if (i == chars.length - 1 && column + 1 < columnLimit) {
					sb.append(lineSeparator);
				}
				if (++column == columnLimit) {
					sb.append(lineSeparator);
					column = 0;
				}
			}
		}
		return sb.toString();
	}
	
	@Test
	public void testDecode01() throws IOException {
		String expectedString = ORIGINAL_STRING_01;
		String base64String = ENCODED_STRING_01;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new StringReader(base64String), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testDecode02() throws IOException {
		String expectedString = ORIGINAL_STRING_02;
		String base64String = ENCODED_STRING_02;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new StringReader(base64String), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testDecode03() throws IOException {
		String expectedString = ORIGINAL_STRING_03;
		String base64String = ENCODED_STRING_03;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new StringReader(base64String), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test(expected = IOException.class)
	public void testDecodeThrowingIOException01() throws IOException {
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new StringReader("%SGVsbG8sIFdvcmxkCg=="), 
				decodedOut, 
				false);
	}
	
	@Test(expected = IOException.class)
	public void testDecodeThrowingIOException02() throws IOException {
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new StringReader(
						"VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQg!b3ZlciB0aGUgbGF6eSBkb2cK"), 
				decodedOut, 
				false);
	}
	
	@Test(expected = IOException.class)
	public void testDecodeThrowingIOException03() throws IOException {
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new StringReader("R29vZGJ5ZSwgV29ybGQK&"), 
				decodedOut, 
				false);
	}
	
	@Test
	public void testEncode01() throws IOException {
		String expectedString = ENCODED_STRING_01;
		String originalString = ORIGINAL_STRING_01;
		StringWriter stringWriter = new StringWriter();
		Base64Transformer.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), stringWriter, 0);
		assertEquals(expectedString, stringWriter.toString());
	}
	
	@Test
	public void testEncode02() throws IOException {
		String expectedString = ENCODED_STRING_02;
		String originalString = ORIGINAL_STRING_02;
		StringWriter stringWriter = new StringWriter();
		Base64Transformer.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), stringWriter, 0);
		assertEquals(expectedString, stringWriter.toString());
	}
	
	@Test
	public void testEncode03() throws IOException {
		String expectedString = ENCODED_STRING_03;
		String originalString = ORIGINAL_STRING_03;
		StringWriter stringWriter = new StringWriter();
		Base64Transformer.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), stringWriter, 0);
		assertEquals(expectedString, stringWriter.toString());
	}
	
	@Test
	public void testEncodeWithWrapping01() throws IOException {
		int columnLimit = 5;
		String expectedString = wrap(ENCODED_STRING_01, columnLimit);
		String originalString = ORIGINAL_STRING_01;
		StringWriter stringWriter = new StringWriter();
		Base64Transformer.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), stringWriter, columnLimit);
		assertEquals(expectedString, stringWriter.toString());
	}
	
	@Test
	public void testEncodeWithWrapping02() throws IOException {
		int columnLimit = 10;
		String expectedString = wrap(ENCODED_STRING_02, columnLimit);
		String originalString = ORIGINAL_STRING_02;
		StringWriter stringWriter = new StringWriter();
		Base64Transformer.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), stringWriter, columnLimit);
		assertEquals(expectedString, stringWriter.toString());
	}
	
	@Test
	public void testEncodeWithWrapping03() throws IOException {
		int columnLimit = 20;
		String expectedString = wrap(ENCODED_STRING_03, columnLimit);
		String originalString = ORIGINAL_STRING_03;
		StringWriter stringWriter = new StringWriter();
		Base64Transformer.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), stringWriter, columnLimit);
		assertEquals(expectedString, stringWriter.toString());
	}
	
	@Test
	public void testWithRoundtripping01() throws IOException {
		String originalString = ORIGINAL_STRING_01;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				new OutputStreamWriter(encodedOut), 
				0);
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new InputStreamReader(
						new ByteArrayInputStream(encodedOut.toByteArray())), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(originalString, decodedString);
	}
	
	@Test
	public void testWithRoundtripping02() throws IOException {
		String originalString = ORIGINAL_STRING_02;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				new OutputStreamWriter(encodedOut), 
				0);
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new InputStreamReader(
						new ByteArrayInputStream(encodedOut.toByteArray())), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(originalString, decodedString);
	}
	
	
	@Test
	public void testWithRoundtripping03() throws IOException {
		String originalString = ORIGINAL_STRING_03;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				new OutputStreamWriter(encodedOut), 
				0);
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new InputStreamReader(
						new ByteArrayInputStream(encodedOut.toByteArray())), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(originalString, decodedString);
	}
	

	
}
