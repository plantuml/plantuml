# Directory Documentation for `syntax`

## Description
This package provides classes used to manage PlantUML Syntax and Language _(use to print language, and 'cypher')_.

## Reference
- [QA-3741](https://forum.plantuml.net/3741/plantuml-jar-language-does-not-report-all-keywords)
- [QA-5329](https://forum.plantuml.net/5329/language-definition)
- [QA-10648](https://forum.plantuml.net/10648/keywords-predefined-symbols-codeless-language-module-bbedit)

## Usage
### With the `-language` option
```sh
java -jar plantuml.jar -language
```
In order to have the PlantUML list of:
- `type`,
- `keyword`,
- `preprocessor`,
- `skinparameter`,
- `color`.

### With the `-cypher` option
```sh
java -jar plantuml.jar -cypher file.puml
```
That generates a `file.preproc`.

Ref.:
- [Cypher option](https://plantuml.com/en/faq#76ee48737d9f7a1a)

See also code here:
- [`utils/Cypher.java`](../utils/Cypher.java)
