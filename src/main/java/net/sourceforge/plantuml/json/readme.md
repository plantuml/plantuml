# Directory Documentation for `json`

## Description
This package provides classes used to manage [JSON](https://www.json.org) Data _(with the [minimal-json API](https://github.com/ralfstx/minimal-json))_.

## Link

## Reference
- [JSON _(on Wikipedia)_](https://en.wikipedia.org/wiki/JSON)

## Architecture of `JsonValue`
```mermaid
flowchart LR
J[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonValue.java'>JsonValue</a>]
J --> JI[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonNumber.java'>JSON_Number</a>]
J --> JS[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonString.java'>JSON_String</a>]
J --> JB[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/Json.java'>JSON_Boolean</a>]
J --> JN[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/Json.java'>JSON_Null</a>]
J --> JA[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonArray.java'>JSON_Array</a>]
J --> JO[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonObject.java'>JSON_Object</a>]
```


## Credit
- :octocat: [ralfstx/minimal-json](https://github.com/ralfstx/minimal-json)
- [`json`](../json/) _(included in PlantUML)_

## Misc.

