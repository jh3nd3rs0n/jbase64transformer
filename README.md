# JBase64Transformer

JBase64Transformer is a Java implementation of GNU's base64 utility and an example of using ArgMatey.

## Contents

- <a href="#requirements">Requirements</a>

- <a href="#building">Building</a>

- <a href="#running">Running JBase64Transformer</a>

- <a href="#usage">Usage</a>

<a name="requirements"></a>

## Requirements

- Apache Maven&#8482; 3.3.9 or higher 

- Java&#8482; SDK 1.8 or higher

<a name="building"></a>

## Building

To build and package JBase64Transformer as an executable jar file, run the following commands:

<pre>

$ cd jbase64transformer
$ mvn package

</pre>

<a name="running"></a>

## Running JBase64Transformer 

To run JBase64Transformer, you can run the following command:

<pre>

$ mvn exec:java -Dexec.args="[OPTION]... [FILE]"

</pre>

Replace `[OPTION]...` with command line options described in the usage below and replace `[FILE]` with the file you would like transformed.

If you have JBase64Transformer packaged as an executable jar file, you can run the following command:

<pre>

$ java -jar jbase64transformer-1.0-SNAPSHOT.jar [OPTION]... [FILE]

</pre>

Again, replace `[OPTION]...` with command line options described in the usage below and replace `[FILE]` with the file you would like transformed.

<a name="usage"></a>

## Usage

<pre>

Usage: jbase64transformer.Base64Transformer [OPTION]... [FILE]
Base64 encode or decode FILE, or standard input, to standard output.

OPTIONS:
  -d, --decode
      decode data
  -i, --ignore-garbage
      when decoding, ignore non-alphabet characters
  -w COLS, --wrap=COLS
      wrap encoded lines after COLS character (default 76).
      Use 0 to disable line wrapping
  --help
      display this help and exit
  --version
      display version information and exit

With no FILE, or when FILE is -, read standard input.

</pre>
