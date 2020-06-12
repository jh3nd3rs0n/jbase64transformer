# JBase64Transformer

JBase64Transformer is a Java implementation of GNU's base64 utility and an example of using ArgMatey.

## Contents

-   [Requirements](#requirements)
-   [Building](#building)
-   [Running JBase64Transformer](#running-jbase64transformer)
-   [Usage](#usage)

## Requirements

-   Apache Maven&#8482; 3.3.9 or higher
-   Java&#8482; SDK 1.8 or higher

## Building

To build and package JBase64Transformer as an executable jar file, run the following commands:

```bash

    cd jbase64transformer
    mvn package

```

## Running JBase64Transformer 

To run JBase64Transformer, you can run the following command:

```bash

    java -jar target/jbase64transformer-${VERSION}.jar [OPTION]... [FILE]

```

Be sure to remove or replace the following:

-   Replace `${VERSION}` with the actual version shown within the name of the executable jar file.
-   Remove `[OPTION]...` or replace `[OPTION]...` with one or more of the command line options described in the usage below.
-   Remove `[FILE]` or replace `[FILE]` with the file you would like to be transformed to standard output. (Removing `[FILE]` will cause JBase64Transformer to use standard input as input instead of a file.)

## Usage

```text

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

```
