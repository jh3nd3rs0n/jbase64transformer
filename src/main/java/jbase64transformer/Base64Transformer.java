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

import argmatey.ArgMatey.ArgsParser;
import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.NonparsedArgSink;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.OptionArgSpecBuilder;
import argmatey.ArgMatey.OptionBuilder;
import argmatey.ArgMatey.OptionSink;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.PosixOption;
import argmatey.ArgMatey.StringConverter;

public enum Base64Transformer {
	
	INSTANCE;
	
	public static final class Cli {
		
		private static final int DECODE_OPTION_ORDINAL = 0;
		private static final int IGNORE_GARBAGE_OPTION_ORDINAL = 1;
		private static final int WRAP_OPTION_ORDINAL = 2;
		private static final int HELP_OPTION_ORDINAL = 3;
		private static final int VERSION_OPTION_ORDINAL = 4;
		
		private ArgsParser argsParser;
		private int columnLimit;
		private boolean decodingMode;
		private String file;
		private boolean garbageIgnored;
		private final Options options;
		private final String programName;
		private final String programVersion;
		
		Cli() {
			Options opts = Options.newInstanceFrom(this.getClass());
			this.argsParser = null;
			this.columnLimit = 76;
			this.decodingMode = false;
			this.file = null;
			this.garbageIgnored = false;
			this.options = opts;
			this.programName = Base64Transformer.class.getName();
			this.programVersion = "1.0";
		}
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "display this help and exit",
						name = "help", 
						ordinal = HELP_OPTION_ORDINAL,
						special = true,
						type = GnuLongOption.class 
				)
		)
		public void displayHelp() {
			System.out.printf("Usage: %s [OPTION]... [FILE]%n", 
					this.programName);
			System.out.printf("Base64 encode or decode FILE, or standard "
					+ "input, to standard output.%n%n");
			System.out.println("OPTIONS:");
			this.options.printHelpText();
			System.out.printf("%n%nWith no FILE, or when FILE is -, read "
					+ "standard input.%n");
			System.exit(0);
		}
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "display version information and exit",
						name = "version", 
						ordinal = VERSION_OPTION_ORDINAL,
						special = true,
						type = GnuLongOption.class 
				)
		)
		public void displayVersion() {
			System.out.printf("%s %s%n", this.programName, this.programVersion);
			System.exit(0);
		}
		
		public void process(final String[] args) {
			Option helpOption = this.options.toList().get(HELP_OPTION_ORDINAL);
			String suggestion = String.format(
					"Try '%s %s' for more information.", 
					this.programName, 
					helpOption.getUsage());
			this.argsParser = ArgsParser.newInstance(args, this.options, false);
			try {
				argsParser.parseRemainingTo(this);
			} catch (Throwable t) {
				System.err.printf("%s: %s%n", this.programName, t);
				System.err.println(suggestion);
				t.printStackTrace(System.err);
				System.exit(-1);
			}
			InputStream in = null;
			if (this.file != null) {
				if (this.file.equals("-")) {
					in = System.in;
				} else {
					File f = new File(this.file);
					try {
						in = new FileInputStream(f);
					} catch (FileNotFoundException e) {
						System.err.printf("%s: %s%n", this.programName, e);
						e.printStackTrace(System.err);
						System.exit(-1);
					}
				}		
			}
			if (in == null) { in = System.in; } 
			Base64Transformer base64Transformer = Base64Transformer.INSTANCE;
			if (this.decodingMode) {
				Reader reader = new InputStreamReader(in);
				try {
					base64Transformer.decode(
							reader, System.out, this.garbageIgnored);
				} catch (IOException e) {
					System.err.printf("%n%s: %s%n", this.programName, e);
					e.printStackTrace(System.err);
					System.exit(-1);
				} finally {
					if (in instanceof FileInputStream) {
						try {
							in.close();
						} catch (IOException e) {
							System.err.printf("%s: %s%n", this.programName, e);
							e.printStackTrace(System.err);
							System.exit(-1);
						}
					}
				}
			} else {
				Writer writer = new OutputStreamWriter(System.out);
				try {
					base64Transformer.encode(in, writer, this.columnLimit);
				} catch (IOException e) {
					System.err.printf("%n%s: %s%n", this.programName, e);
					e.printStackTrace(System.err);
					System.exit(-1);
				} finally {
					if (in instanceof FileInputStream) {
						try {
							in.close();
						} catch (IOException e) {
							System.err.printf("%s: %s%n", this.programName, e);
							e.printStackTrace(System.err);
							System.exit(-1);
						}
					}
				}
			}
		}
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "wrap encoded lines after COLS character "
								+ "(default 76)." 
								+ "\r\n      Use 0 to disable line wrapping",
						name = "w",
						optionArgSpecBuilder = @OptionArgSpecBuilder(
								name = "COLS",
								stringConverter = NonnegativeIntegerStringConverter.class
						), 
						ordinal = WRAP_OPTION_ORDINAL,
						type = PosixOption.class 
				),
				otherOptionBuilders = {
						@OptionBuilder(
								name = "wrap",
								type = GnuLongOption.class
						)
				}
		)
		public void setColumnLimit(final int colLimit) {
			this.columnLimit = colLimit;
		}
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "decode data",
						name = "d", 
						ordinal = DECODE_OPTION_ORDINAL,
						type = PosixOption.class 
				),
				otherOptionBuilders = {
						@OptionBuilder(
								name = "decode",
								type = GnuLongOption.class
						)
				}
		)
		public void setDecodingMode(final boolean b) {
			this.decodingMode = b;
		}
		
		@NonparsedArgSink
		public void setFile(final String f) {
			if (this.file != null) {
				throw new IllegalArgumentException(String.format(
						"extra operand '%s'", f));
			}
			this.file = f;
		}

		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "when decoding, ignore non-alphabet characters",
						name = "i", 
						ordinal = IGNORE_GARBAGE_OPTION_ORDINAL,
						type = PosixOption.class 
				),
				otherOptionBuilders = {
						@OptionBuilder(
								name = "ignore-garbage",
								type = GnuLongOption.class
						)
				}
		)
		public void setGarbageIgnored(final boolean b) {
			this.garbageIgnored = b;
		}
		
	}
	
	public static final class NonnegativeIntegerStringConverter 
		extends StringConverter {

		public NonnegativeIntegerStringConverter() { }
		
		@Override
		public Object convert(final String string) {
			String message = String.format(
					"must be an integer between %s and %s (inclusive)", 
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
		
	}
	
	public static void main(final String[] args) {
		Cli cli = new Cli();
		cli.process(args);
	}
	
	public void decode(
			final Reader reader, 
			final OutputStream out, 
			final boolean garbageIgnored) throws IOException {
		StringBuilder sb = new StringBuilder();
		Base64.Decoder decoder = Base64.getDecoder();
		String base64AlphabetChars = 
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvwxyz"
				+ "0123456789"
				+ "+/=";
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
						&& !garbageIgnored) {
					throw new IOException(String.format(
							"non-alphabet character found: '%s'", (char) c));
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
			final int columnLimit) throws IOException {
		if (columnLimit < 0) {
			throw new IllegalArgumentException(String.format(
					"integer must be between %s and %s (inclusive)", 
					0, Integer.MAX_VALUE));
		}
		final int groupSize = 3;
		int column = 0;
		String lineSeparator = System.getProperty("line.separator");
		Base64.Encoder encoder = Base64.getEncoder();
		while (true) {
			byte[] b = new byte[groupSize];
			int newLength = in.read(b);
			if (newLength == -1) {
				if (columnLimit > 0 && column > 0 && column < columnLimit) {
					writer.write(lineSeparator);
				}
				break; 
			}
			b = Arrays.copyOf(b, newLength);
			String encoded = encoder.encodeToString(b);
			if (columnLimit > 0) {
				StringBuilder sb = new StringBuilder();
				for (char c : encoded.toCharArray()) {
					sb.append(c);
					if (++column == columnLimit) {
						sb.append(lineSeparator);
						column = 0;
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
