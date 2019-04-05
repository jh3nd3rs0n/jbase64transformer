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
	
	public static final class Base64TransformerOptions extends Options {
		
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
	
		public Base64TransformerOptions() { }
		
	}
	
	public static void main(final String[] args) {
		Options options = new Base64TransformerOptions();
		ArgsParser argsParser = ArgsParser.newInstance(args, options, false);
		String programName = Base64Transformer.class.getName();
		String programVersion = "1.0";
		String suggestion = String.format("Try '%s %s' for more information", 
				programName, Base64TransformerOptions.HELP_OPTION.getUsage());
		boolean decode = false;
		boolean ignoreGarbage = false;
		final int defaultNumOfColumnsLimit = 76;
		int numOfColumnsLimit = defaultNumOfColumnsLimit;
		InputStream in = System.in;
		while (argsParser.hasNext()) {
			ParseResultHolder parseResultHolder = null;
			try { 
				parseResultHolder = argsParser.parseNext(); 
			} catch (RuntimeException e) {
				System.err.printf("%s: %s%n%s%n", 
						programName, e.toString(), suggestion);
				System.exit(-1);
			}
			if (parseResultHolder.hasOptionOfAnyOf("-d", "--decode")) {
				decode = true;
			}
			if (parseResultHolder.hasOptionOfAnyOf("-i", "--ignore-garbage")) {
				ignoreGarbage = true;
			}
			if (parseResultHolder.hasOptionOfAnyOf("-w", "--wrap")) {
				numOfColumnsLimit = 
						parseResultHolder.getOptionArg().getTypeValue(
								Integer.class).intValue();
			}
			if (parseResultHolder.hasOptionOf("--help")) {
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
			if (parseResultHolder.hasOptionOf("--version")) {
				System.out.printf("%s %s%n", programName, programVersion);
				return;
			}
			if (parseResultHolder.hasNonparsedArg()) {
				String arg = parseResultHolder.getNonparsedArg();
				if (in != null) {
					System.err.printf("%s: extra operand '%s'%n%s%n", 
							programName, arg, suggestion);
					System.exit(-1);
				}
				if (!arg.equals("-")) {
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
		StringBuilder sb = new StringBuilder();
		Base64.Decoder decoder = Base64.getDecoder();
		String base64AlphabetChars = 
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String acceptedWhitespaceChars = "\r\n";
		final int groupSize = 4;
		while (true) {
			int c = reader.read();
			if (c == -1) {
				if (sb.length() > 0) {
					out.write(decoder.decode(sb.toString()));
					sb.delete(0, sb.length());
				}
				break; 
			}
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
		int numOfColumns = 0;
		String lineSeparator = System.getProperty("line.separator");
		Base64.Encoder encoder = Base64.getEncoder();
		while (true) {
			byte[] b = new byte[groupSize];
			int newLength = in.read(b);
			if (newLength == -1) {
				if (numOfColumnsLimit > 0) {
					if (numOfColumns > 0 && numOfColumns < numOfColumnsLimit) {
						writer.write(lineSeparator);
					}
				}
				break; 
			}
			b = Arrays.copyOf(b, newLength);
			String encoded = encoder.encodeToString(b);
			if (numOfColumnsLimit > 0) {
				StringBuilder sb = new StringBuilder();
				for (char c : encoded.toCharArray()) {
					sb.append(c);
					if (++numOfColumns == numOfColumnsLimit) {
						sb.append(lineSeparator);
						numOfColumns = 0;
					}
				}
				encoded = sb.toString();
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
