[![Koara](http://www.codeaddslife.com/koara.png)](http://www.codeaddslife.com/koara)

[![Build Status](https://img.shields.io/travis/koara/koara-java.svg)](https://travis-ci.org/koara/koara-java)
[![Coverage Status](https://img.shields.io/coveralls/koara/koara-java.svg)](https://coveralls.io/github/koara/koara-java?branch=master)
[![Latest Version](https://img.shields.io/maven-central/v/com.codeaddslife.koara/koara.svg?label=Maven Central)](http://search.maven.org/#search%7Cga%7C1%7Ckoara)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/koara/koara-java/blob/master/LICENSE)




# Koara-java
[Koara](http://www.codeaddslife.com/koara) is a modular lightweight markup language. This project is the core koara parser written in Java.  
If you are interested in converting koara to a specific outputFormat, please look the [Related Projects](#related-projects) section.

## Getting started
- Download [JAR file](http://repo1.maven.org/maven2/com/codeaddslife/koara/0.15.0/koara-0.15.0.jar)
- Gradle

  ```groovy
  dependencies {
	compile "com.codeaddslife.koara:koara:0.15.0"
  }
  ```
  
- Maven

  ```xml
  <dependency>
    <groupId>com.codeaddslife.koara</groupId>
    <artifactId>koara</artifactId>
    <version>0.15.0</version>
  </dependency>
  ```

## Usage
```java
package demo;

import java.io.File;
import java.io.IOException;
import Parser;
import Document;

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
