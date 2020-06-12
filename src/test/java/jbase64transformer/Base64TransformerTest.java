package jbase64transformer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.junit.Test;

public class Base64TransformerTest {

	@Test
	public void test01() throws IOException {
		String originalString = "Hello, World";
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				new OutputStreamWriter(encodedOut), 
				126);
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.decode(
				new InputStreamReader(
						new ByteArrayInputStream(encodedOut.toByteArray())), 
				decodedOut, 
				true);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(originalString, decodedString);
	}
	
	@Test
	public void test02() throws IOException {
		String originalString = "The quick brown fox jumped over the lazy dog";
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				new OutputStreamWriter(encodedOut), 
				76);
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
	public void test03() throws IOException {
		String originalString = "Goodbye, World";
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
				true);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(originalString, decodedString);
	}
	
	@Test
	public void test04() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Copyright (c) 2019-2020 Jonathan K. Henderson\n");
		sb.append("\n");
		sb.append("Permission is hereby granted, free of charge, to any person\n");
		sb.append("obtaining a copy of this software and associated documentation\n");
		sb.append("files (the \"Software\"), to deal in the Software without\n");
		sb.append("restriction, including without limitation the rights to use,\n");
		sb.append("copy, modify, merge, publish, distribute, sublicense, and/or sell\n");
		sb.append("copies of the Software, and to permit persons to whom the\n");
		sb.append("Software is furnished to do so, subject to the following\n");
		sb.append("conditions:\n");
		sb.append("\n");
		sb.append("The above copyright notice and this permission notice shall be\n");
		sb.append("included in all copies or substantial portions of the Software.\n");
		sb.append("\n");
		sb.append("THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,\n");
		sb.append("EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES\n");
		sb.append("OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND\n");
		sb.append("NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT\n");
		sb.append("HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,\n");
		sb.append("WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING\n");
		sb.append("FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR\n");
		sb.append("OTHER DEALINGS IN THE SOFTWARE.");
		String originalString = sb.toString();
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		Base64Transformer.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				new OutputStreamWriter(encodedOut), 
				10);
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
