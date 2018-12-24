[![Build Status](https://travis-ci.org/SpaiR/byond-dme-parser.svg?branch=master)](https://travis-ci.org/SpaiR/byond-dme-parser)
[![Javadocs](https://www.javadoc.io/badge/io.github.spair/byond-dme-parser.svg)](https://www.javadoc.io/doc/io.github.spair/byond-dme-parser)
[![License](http://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)

# BYOND Dme Parser

## About

Library to parse BYOND project and deserialize whole object tree into `Dme.class` object.

## Installation
[![Maven Central](https://img.shields.io/maven-central/v/io.github.spair/byond-dme-parser.svg?style=flat)](https://search.maven.org/search?q=a:byond-dme-parser)
[![JCenter](https://img.shields.io/bintray/v/spair/io.github.spair/byond-dme-parser.svg?label=jcenter)](https://bintray.com/spair/io.github.spair/byond-dme-parser/_latestVersion)

Library deployed to Maven Central and JCenter repositories.

#### pom.xml
```
<dependency>
    <groupId>io.github.spair</groupId>
    <artifactId>byond-dme-parser</artifactId>
    <version>${last.version}</version>
</dependency>
```

#### build.gradle:
```
compile 'io.github.spair:byond-dme-parser:${last.version}'
```

## How To Use

Project parsing process should be started from the root `.dme` file.

`Dme dme = DmeParser.parse(new File(${path/to/root/dme/file}));`

Also `Dme` object has method to merge himself with provided json.

JSON should have next format:
```
{
  "macroses": {
    "NUMBER_MACROS": 100,
    "TEXT_MACROS": "{text}one hundred",     << this will result in quoted text
    "RAW_MACROS": "one hundred"
  },
  "definitions": [
    {
      "type": "/obj",
      "parent": "/datum",
      "subtypes": [],
      "vars": {
        "number_var": 100,
        "text_var": "{text}one hundred",    << this will result in quoted text
        "raw_var": "one hundred"
      }
    }
  ]
}
```

Next fields for object definitions are mandatory: `type`, `parent`, `subtypes`, `vars`.
If you don't wan't provide value to one of these field, it should still exist and have empty value (empty string for `type` and `parent`, empty array for `subtypes`, empty object for `vars`).

More could be found in [JavaDoc](https://www.javadoc.io/doc/io.github.spair/byond-dme-parser).

## Credits

The parsing algorithm itself is a pretty straightforward port of [@monster860](https://github.com/monster860) JS library [byond-parser](https://github.com/monster860/byond-parser),
big thanks to him for that.
