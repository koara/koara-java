<!-- HEADER -->
[![Koara](http://www.koara.io/logo.png)](http://www.koara.io)

[![Build Status](https://img.shields.io/travis/koara/koara-java.svg)](https://travis-ci.org/koara/koara-java)
[![Coverage Status](https://img.shields.io/coveralls/koara/koara-java.svg)](https://coveralls.io/github/koara/koara-java?branch=master)
[![Latest Version](https://img.shields.io/maven-central/v/io.koara/koara.svg?label=Maven Central)](http://search.maven.org/#search%7Cga%7C1%7Ckoara)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/koara/koara-java/blob/master/LICENSE)
<!-- HEADER:END -->

# Koara HTML5

## Getting Started
- Download [JAR file](http://repo1.maven.org/maven2/io/koara/koara-html5/0.9.0/koara-html5-0.9.0.jar)
- Gradle

  ```groovy
  dependencies {
	compile "io.koara:koara-html5:0.9.0"
  }
  ```
  
- Maven

  ```xml
  <dependency>
    <groupId>io.koara</groupId>
    <artifactId>koara-html5</artifactId>
    <version>0.9.0</version>
  </dependency>
  ```
  
## Usage
```java
package io.koara;

import io.koara.ast.Document;
import io.koara.renderer.Html5Renderer;
import static io.koara.Module.*;

public class Demo {

	public static void main(String[] args) {
		
		Parser parser = new Parser();
		
		// Enable which modules to parse (all are parsed by default)
		parser.setModules(PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
		
		// Parse string or file and generate AST
		Document document = parser.parse("Hello World!"); 
		
		// Render AST as HTML
		Html5Renderer renderer = new Html5Renderer();
		document.accept(renderer);
		
		System.out.println(renderer.getOutput());
	}
	
}
```