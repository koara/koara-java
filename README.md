[![Koara](http://koara.io/logo.png)](http://www.koara.io)

[![Build Status](https://img.shields.io/travis/koara/koara-java.svg)](https://travis-ci.org/koara/koara-java)
[![Coverage Status](https://img.shields.io/coveralls/koara/koara-java.svg)](https://coveralls.io/github/koara/koara-java?branch=master)
[![Latest Version](https://img.shields.io/maven-central/v/io.koara/koara.svg?label=Maven Central)](http://search.maven.org/#search%7Cga%7C1%7Ckoara)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/koara/koara-java/blob/master/LICENSE)

# Koara-java
[Koara](http://www.koara.io) is a modular lightweight markup language. This project is the core koara parser written in Java.  
If you are interested in converting koara to a specific outputFormat, please look the [Related Projects](#related-projects) section.

## Getting started
- Download [JAR file](http://repo1.maven.org/maven2/io/koara/koara/0.14.0/koara-0.14.0.jar)
- Gradle

  ```groovy
  dependencies {
	compile "io.koara:koara:0.14.0"
  }
  ```
  
- Maven

  ```xml
  <dependency>
    <groupId>io.koara</groupId>
    <artifactId>koara</artifactId>
    <version>0.14.0</version>
  </dependency>
  ```

## Usage
```java
package demo;

import java.io.File;
import java.io.IOException;
import io.koara.Parser;
import io.koara.ast.Document;

public class App {

	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		Document result1 = parser.parse("Hello World!"); // parse a string
		Document result2 = parser.parseFile(new File("hello.kd")); // parse a file
	}
	
}

```

## Configuration
You can configure the Parser:

-  **parser.setModules(String... modules)**  
   Default:	`{"paragraphs", "headings", "lists", "links", "images", "formatting", "blockquotes", "code"}`
   
   Specify which parts of the syntax are allowed to be parsed. The rest will render as plain text.

## Related Projects

- [koara / koara-java-html](http://www.github.com/koara/koara-java-html): Koara to Html renderer written in Java
- [koara / koara-java-xml](http://www.github.com/koara/koara-java-html): Koara to Xml renderer written in Java
