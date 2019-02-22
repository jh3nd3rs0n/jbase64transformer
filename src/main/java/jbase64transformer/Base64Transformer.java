package jbase64transformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Base64;

import argmatey.ArgsParser;
import argmatey.GnuLongOption;
import argmatey.Option;
import argmatey.OptionArgSpec;
import argmatey.Options;
import argmatey.ParseResultHolder;
import argmatey.PosixOption;
import argmatey.StringConverter;

public enum Base64Transformer {
	
	INSTANCE;
	
	public static final class Base64TransformerOptions {
		
		public static final Option DECODE_OPTION = new PosixOption.Builder('d')
				.doc("decode data")
				.ordinal(0)
				.otherBuilders(new GnuLongOption.Builder("decode"))
				.build();
		
		public static final Option IGNORE_GARBAGE_OPTION = 
				new PosixOption.Builder('i')
				.doc("when decoding, ignore non-alphabet characters")
				.ordinal(1)
				.otherBuilders(new GnuLongOption.Builder("ignore-garbage"))
				.build();
		
		public static final Option WRAP_OPTION = new PosixOption.Builder('w')
				.doc(String.format(
						"wrap encoded lines after COLS character (default 76)." 
						+ "%n      Use 0 to disable line wrapping"))
				.optionArgSpec(new OptionArgSpec.Builder()
						.name("COLS")
						.stringConverter(new StringConverter() {

							@Override
							public Object convert(String string) {
								String message = String.format(
										"must be an integer between "
										+ "%s (inclusive) and %s (inclusive)", 
										0,
										Integer.MAX_VALUE);
								int intValue;
								try {
									intValue = Integer.parseInt(string);
								} catch (NumberFormatException e) {
									throw new IllegalArgumentException(
											message, e);
								}
								if (intValue < 0) {
									throw new IllegalArgumentException(message);
								}
								return Integer.valueOf(intValue);
							}
							
						})
						.type(Integer.class)
						.build())
				.ordinal(2)
				.otherBuilders(new GnuLongOption.Builder("wrap"))
				.build();
		
		public static final Option HELP_OPTION = new GnuLongOption.Builder(
				"help")
				.doc("display this help and exit")
				.ordinal(3)
				.special(true)
				.build();
		
		public static final Option VERSION_OPTION = new GnuLongOption.Builder(
				"version")
				.doc("display version information and exit")
				.ordinal(4)
				.special(true)
				.build();
		
		private Base64TransformerOptions() { }
		
	}
	
	public static void main(final String[] args) {
		Options options = Options.newInstance(Base64TransformerOptions.class);
		ArgsParser argsParser = ArgsParser.newInstance(args, options, false);
		String programName = Base64Transformer.class.getName();
		String programVersion = "1.0";
		String suggestion = String.format("Try '%s %s' for more information", 
				programName, Base64TransformerOptions.HELP_OPTION.getUsage());
		boolean decode = false;
		boolean ignoreGarbage = false;
		final int defaultNumOfColumnsLimit = 76;
		int numOfColumnsLimit = defaultNumOfColumnsLimit;
		InputStream in = null;
		while (argsParser.hasNext()) {
			ParseResultHolder parseResultHolder = null;
			try { 
				parseResultHolder = argsParser.parseNext(); 
			} catch (RuntimeException e) {
				System.err.printf("%s: %s%n%s%n", 
						programName, e.toString(), suggestion);
				System.exit(-1);
			}
			if (parseResultHolder.hasOptionFrom(
					Base64TransformerOptions.HELP_OPTION)) {
				System.out.printf("Usage: %s [OPTION]... [FILE]%n", 
						programName);
				System.out.printf("Base64 encode or decode FILE, or standard "
						+ "input, to standard output.%n%n");
				System.out.println("OPTIONS:");
				options.printHelpText();
				System.out.printf("%n%nWith no FILE, or when FILE is -, read "
						+ "standard input.%n");
				return;
			}
			if (parseResultHolder.hasOptionFrom(
					Base64TransformerOptions.VERSION_OPTION)) {
				System.out.printf("%s %s%n", programName, programVersion);
				return;
			}
			if (parseResultHolder.hasOptionFrom(
					Base64TransformerOptions.DECODE_OPTION)) {
				decode = true;
			}
			if (parseResultHolder.hasOptionFrom(
					Base64TransformerOptions.IGNORE_GARBAGE_OPTION)) {
				ignoreGarbage = true;
			}
			if (parseResultHolder.hasOptionFrom(
					Base64TransformerOptions.WRAP_OPTION)) {
				numOfColumnsLimit = 
						parseResultHolder.getOptionArg().getTypeValue(
								Integer.class).intValue();
			}
			if (parseResultHolder.hasNonparsedArg()) {
				String arg = parseResultHolder.getNonparsedArg();
				if (in != null) {
					System.err.printf("%s: extra operand '%s'%n%s%n", 
							programName, arg, suggestion);
					System.exit(-1);
				}
				if (arg.equals("-")) {
					in = System.in;
				} else {
					File file = new File(arg);
					try {
						in = new FileInputStream(file);
					} catch (FileNotFoundException e) {
						System.err.printf("%s: %s%n", 
								programName, e.toString());
						System.exit(-1);
					}
				}
			}
		}
		if (in == null) { in = System.in; }
		Base64Transformer base64Transformer = Base64Transformer.INSTANCE;
		if (decode) {
			Reader reader = new InputStreamReader(in);
			try {
				base64Transformer.decode(reader, System.out, ignoreGarbage);
			} catch (IOException e) {
				System.err.printf("%s: %s%n", programName, e.toString());
				System.exit(-1);
			}
		} else {
			Writer writer = new OutputStreamWriter(System.out);
			try {
				base64Transformer.encode(in, writer, numOfColumnsLimit);
			} catch (IOException e) {
				System.err.printf("%s: %s%n", programName, e.toString());
				System.exit(-1);
			}
		}
	}
	
	public void decode(
			final Reader reader, 
			final OutputStream out, 
			final boolean ignoreGarbage) throws IOException {
		String base64AlphabetChars = 
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String acceptedWhitespaceChars = "\r\n";
		final int groupSize = 4;
		StringBuilder sb = new StringBuilder();
		Base64.Decoder decoder = Base64.getDecoder();
		while (true) {
			int c = reader.read();
			if (c == -1) { break; }
			if (base64AlphabetChars.indexOf(c) == -1) {
				if (acceptedWhitespaceChars.indexOf(c) == -1 
						&& !ignoreGarbage) {
					throw new IOException("non-alphabet character found");
				}
				continue;
			}
			sb.append((char) c);
			if (sb.length() < groupSize) {
				continue;
			}
			out.write(decoder.decode(sb.toString()));
			sb.delete(0, groupSize);
		}
		out.flush();
	}
	
	public void encode(
			final InputStream in,
			final Writer writer,
			final int numOfColumnsLimit) throws IOException {
		if (numOfColumnsLimit < 0) {
			throw new IllegalArgumentException(String.format(
					"integer must be between %s (inclusive) and %s (inclusive)", 
					0, Integer.MAX_VALUE));
		}
		final int groupSize = 3;
		Base64.Encoder encoder = Base64.getEncoder();
		int numOfColumns = 0;
		String lineSeparator = System.getProperty("line.separator");
		while (true) {
			byte[] b = new byte[groupSize];
			int newLength = in.read(b);
			if (newLength == -1) {
				if (numOfColumnsLimit > 0) {
					if (numOfColumns < numOfColumnsLimit) {
						writer.write(lineSeparator);
					}
				}
				break; 
			}
			b = Arrays.copyOf(b, newLength);
			String encoded = encoder.encodeToString(b);
			if (numOfColumnsLimit > 0) {
				int encodedLength = encoded.length();
				numOfColumns += encodedLength;
				if (numOfColumns >= numOfColumnsLimit) {
					int diff = numOfColumns - numOfColumnsLimit;
					StringBuilder sb = new StringBuilder(encoded);
					sb.insert(encodedLength - diff, lineSeparator);
					encoded = sb.toString();
					numOfColumns = diff;
				}
			}
			writer.write(encoded);
		}
		writer.flush();
	}
	
	@Override
	public String toString() {
		return Base64Transformer.class.getSimpleName();
	}
}
