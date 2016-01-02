[![Koara](http://www.koara.io/logo.png)](http://www.koara.io)

[![Build Status](https://img.shields.io/travis/koara/koara-java.svg)](https://travis-ci.org/koara/koara-java)
[![Coverage Status](https://img.shields.io/coveralls/koara/koara-java.svg)](https://coveralls.io/github/koara/koara-java?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/io.koara/koara.svg?label=Maven Central)](http://search.maven.org/#search%7Cga%7C1%7Ckoara)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/koara/koara-java/blob/master/LICENSE)

> Koara to HTML parser written in Java

## Getting Started
- Download [JAR file](http://repo1.maven.org/maven2/io/koara/koara/0.8.0/koara-0.8.0.jar)
- Gradle

  ```groovy
  dependencies {
	compile "io.koara:koara:0.8.0"
  }
  ```
  
- Maven

  ```xml
  <dependency>
    <groupId>io.koara</groupId>
    <artifactId>koara</artifactId>
    <version>0.8.0</version>
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
		// Parse string or file and generate AST
		Document document = parser.parse("Hello World!"); 
		
		// Enable which modules to parse (all are parsed by default)
		//parser.setModules(PARAGRAPHS, HEADINGS, LISTS, LINKS, IMAGES, FORMATTING, BLOCKQUOTES, CODE);
		
		// Render AST as HTML
		Html5Renderer renderer = new Html5Renderer();
		document.accept(renderer);
		
		// Prints '<p>Hello World!</p>'
		System.out.println(renderer.getOutput());
		
	}
	
}
```


## Community
- Mailing Lists: [archive](http://groups.google.com/group/koara-users/topics), [subscribe](mailto:koara-users+subscribe@googlegroups.com), [unsubscribe](mailto:koara-users+unsubscribe@googlegroups.com)
- Projects: [http://koara.io/projects.html](http://koara.io/projects)