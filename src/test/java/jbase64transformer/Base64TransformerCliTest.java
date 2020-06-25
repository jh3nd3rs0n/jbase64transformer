package jbase64transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

public class Base64TransformerCliTest {

	private static int process(
			final String[] args, 
			final PrintStream err, 
			final InputStream in,
			final PrintStream out) throws IOException {
		PrintStream formerErr = System.err;
		InputStream formerIn = System.in;
		PrintStream formerOut = System.out;
		Base64Transformer.Cli cli = new Base64Transformer.Cli();
		if (err != null) { System.setErr(err); }
		if (out != null) { System.setOut(out); }
		if (in != null) { System.setIn(in); } 
		int status;
		try {
			status = cli.process(args);
		} finally {
			if (err != null) { System.setErr(formerErr); }
			if (out != null) { System.setOut(formerOut); }
			if (in != null) { System.setIn(formerIn); }
		}
		return status;
	}
	
	@Test
	public void test01() throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		String expectedString = TestStringConstants.ENCODED_STRING_01 + lineSeparator;
		String originalString = TestStringConstants.ORIGINAL_STRING_01;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		process(new String[] { }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void test02() throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		String expectedString = TestStringConstants.ENCODED_STRING_02 + lineSeparator;
		String originalString = TestStringConstants.ORIGINAL_STRING_02;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		process(new String[] { }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void test03() throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		String expectedString = TestStringConstants.ENCODED_STRING_03 + lineSeparator;
		String originalString = TestStringConstants.ORIGINAL_STRING_03;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		process(new String[] { }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testWithBogusOption() throws IOException {
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
		InputStream in = new ByteArrayInputStream(new byte[] { });
		PrintStream out = new PrintStream(new ByteArrayOutputStream());
		int status = process(new String[] { "--bogus" }, err, in, out);
		assertTrue(status != 0);
	}
	
	@Test
	public void testWithDecodeOption01() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_01;
		String base64String = TestStringConstants.ENCODED_STRING_01;
		InputStream in = new ByteArrayInputStream(base64String.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		process(new String[] { "-d" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testWithDecodeOption02() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_02;
		String base64String = TestStringConstants.ENCODED_STRING_02;
		InputStream in = new ByteArrayInputStream(base64String.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		process(new String[] { "-d" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testWithDecodeOption03() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_03;
		String base64String = TestStringConstants.ENCODED_STRING_03;
		InputStream in = new ByteArrayInputStream(base64String.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		process(new String[] { "--decode" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test()
	public void testWithDecodeOptionAndIgnoreGarbageOption01() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_01;
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_01_WITH_GARBAGE;
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		process(new String[] { "-d", "-i" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test()
	public void testWithDecodeOptionAndIgnoreGarbageOption02() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_02;
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_02_WITH_GARBAGE;
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		process(new String[] { "-di" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test()
	public void testWithDecodeOptionAndIgnoreGarbageOption03() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_03;
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_03_WITH_GARBAGE;
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		process(new String[] { "--decode", "--ignore-garbage" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);		
	}
	
	@Test
	public void testWithDecodeOptionWithGarbage01() throws IOException {
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_01_WITH_GARBAGE;
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		PrintStream out = new PrintStream(new ByteArrayOutputStream());
		int status = process(new String[] { "-d" }, err, in, out);
		assertTrue(status != 0);
	}
	
	@Test
	public void testWithDecodeOptionWithGarbage02() throws IOException {
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_02_WITH_GARBAGE;
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		PrintStream out = new PrintStream(new ByteArrayOutputStream());
		int status = process(new String[] { "-d" }, err, in, out);
		assertTrue(status != 0);
	}
	
	@Test
	public void testWithDecodeOptionWithGarbage03() throws IOException {
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_03_WITH_GARBAGE;
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		PrintStream out = new PrintStream(new ByteArrayOutputStream());
		int status = process(new String[] { "--decode" }, err, in, out);
		assertTrue(status != 0);		
	}
	
	@Test
	public void testWithHelpOption() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Usage: jbase64transformer.Base64Transformer [OPTION]... [FILE]%n"));
		sb.append(String.format("Base64 encode or decode FILE, or standard input, to standard output.%n"));
		sb.append(String.format("%n"));
		sb.append(String.format("OPTIONS:%n"));
		sb.append(String.format("  -d, --decode%n"));
		sb.append(String.format("      decode data%n"));
		sb.append(String.format("  -i, --ignore-garbage%n"));
		sb.append(String.format("      when decoding, ignore non-alphabet characters%n"));
		sb.append(String.format("  -w COLS, --wrap=COLS%n"));
		sb.append(String.format("      wrap encoded lines after COLS character (default 76).\r\n"));
		sb.append(String.format("      Use 0 to disable line wrapping%n"));
		sb.append(String.format("  --help%n"));
		sb.append(String.format("      display this help and exit%n"));
		sb.append(String.format("  --version%n"));
		sb.append(String.format("      display version information and exit%n"));
		sb.append(String.format("%n"));
		sb.append(String.format("With no FILE, or when FILE is -, read standard input.%n"));
		String expectedString = sb.toString();
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		process(new String[] { "--help" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithVersionOption() throws IOException {
		String expectedString = String.format(
				"jbase64transformer.Base64Transformer 1.0%n");
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		process(new String[] { "--version" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithWrapOption01() throws IOException {
		int columnLimit = 5;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_01, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_01;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		process(new String[] { "-w", Integer.toString(columnLimit) }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testWithWrapOption02() throws IOException {
		int columnLimit = 10;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_02, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_02;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		process(new String[] { String.format("-w%s", columnLimit) }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testWithWrapOption03() throws IOException {
		int columnLimit = 20;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_03, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_03;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		process(new String[] { String.format("--wrap=%s", columnLimit) }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}

}
