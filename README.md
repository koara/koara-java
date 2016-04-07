[![Koara](http://www.koara.io/logo.png)](http://www.koara.io)

[![Build Status](https://img.shields.io/travis/koara/koara-java.svg)](https://travis-ci.org/koara/koara-java)
[![Coverage Status](https://img.shields.io/coveralls/koara/koara-java.svg)](https://coveralls.io/github/koara/koara-java?branch=master)
[![Latest Version](https://img.shields.io/maven-central/v/io.koara/koara.svg?label=Maven Central)](http://search.maven.org/#search%7Cga%7C1%7Ckoara)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/koara/koara-java/blob/master/LICENSE)

# Koara-java
[Koara](http://www.koara.io) is a modular lightweight markup language. This project is the core koara parser written in Java.  
If you are interested in converting koara to a specific outputFormat, please look the [Related Projects](#related-projects) section.

## Getting started
- Download [JAR file](http://repo1.maven.org/maven2/io/koara/koara/0.10/koara-0.10.jar)
- Gradle

  ```groovy
  dependencies {
	compile "io.koara:koara:0.10"
  }
  ```
  
- Maven

  ```xml
  <dependency>
    <groupId>io.koara</groupId>
    <artifactId>koara</artifactId>
    <version>0.10</version>
  </dependency>
  ```

## Usage
```
```

### Configuration
You can configure the Parser:

#### setModules(String... modules)
Default: ["paragraphs", "headings", "lists", "links", "images", "formatting", "blockquotes", "code"]

## Related Projects

- [koara / koara-java-html](http://www.github.com/koara/koara-java-html): Koara to Html renderer written in Java
- [koara / koara-java-xml](http://www.github.com/koara/koara-java-html): Koara to Xml renderer written in Java