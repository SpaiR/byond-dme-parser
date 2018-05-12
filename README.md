[![Build Status](https://travis-ci.org/SpaiR/byond-dme-parser.svg?branch=master)](https://travis-ci.org/SpaiR/byond-dme-parser)
[![Javadocs](https://www.javadoc.io/badge/io.github.spair/byond-dme-parser.svg)](https://www.javadoc.io/doc/io.github.spair/byond-dme-parser)
[![License](http://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)

# BYOND Dme Parser

## About

Library to parse BYOND project and deserialize whole object tree into `Dme.class` object.

#### Disclaimer

Some functionality, like expression evaluation isn't implemented yet. It may be provided in the future.

## Installation
[![Maven Central](https://img.shields.io/maven-central/v/io.github.spair/byond-dme-parser.svg?style=flat)](http://search.maven.org/#search|ga|1|g:"io.github.spair"a:"byond-dme-parser")
[![JCenter](https://img.shields.io/bintray/v/spair/io.github.spair/byond-dme-parser.svg?label=jcenter)](https://bintray.com/spair/io.github.spair/byond-dme-parser/_latestVersion)

Library deployed to Maven Central and JCenter repositories.

#### pom.xml
```
<dependency>
    <groupId>io.github.spair</groupId>
    <artifactId>byond-dme-parser</artifactId>
    <version>0.1.4</version>
</dependency>
```

#### build.gradle:
```
compile 'io.github.spair:byond-dme-parser:0.1.4'
```

## How To Use

Project parsing process should be started from the root `.dme` file.

`Dme dme = DmeParser.parse(new File(${path/to/root/dme/file}));`

More could be found in [JavaDoc](https://www.javadoc.io/doc/io.github.spair/byond-dme-parser).

## Credits

The parsing algorithm itself is a pretty straightforward port of [@monster860](https://github.com/monster860) JS library [byond-parser](https://github.com/monster860/byond-parser),
big thanks to him for that.
