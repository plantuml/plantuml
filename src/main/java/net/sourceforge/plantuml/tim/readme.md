# Directory Documentation for `tim`

## Description
This package provides classes used to manage [PlantUML Preprocessing](https://plantuml.com/preprocessing).

## Link of Current Preprocessing (documentation)
- [PlantUML Preprocessing](https://plantuml.com/preprocessing)
- [PlantUML Preprocessing JSON](https://plantuml.com/preprocessing-json)

## See also [legacy] Preprocessing (directory)
- [`preproc`](../preproc/)
- [`preproc2`](../preproc2/)

## Architecture of `TValue`
```mermaid
flowchart LR
A[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/tim/expression/TValue.java'>TValue</a>]
A --> I[Integer]
A --> S[String]
A ---> J[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonValue.java'>JsonValue</a>]
J --> JI[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonNumber.java'>JSON_Number</a>]
J --> JS[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonString.java'>JSON_String</a>]
J --> JB[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/Json.java'>JSON_Boolean</a>]
J --> JN[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/Json.java'>JSON_Null</a>]
J --> JA[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonArray.java'>JSON_Array</a>]
J --> JO[<a href='https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/json/JsonObject.java'>JSON_Object</a>]
```

## Reference
- [Shunting yard algorithm _(on Wikipedia)_](https://en.wikipedia.org/wiki/Shunting_yard_algorithm)
- [Reverse Polish notation _(on Wikipedia)_](https://en.wikipedia.org/wiki/Reverse_Polish_notation)
- [Boyer–Moore–Horspool algorithm _(on Wikipedia)_](https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore%E2%80%93Horspool_algorithm)
- [String-searching algorithm _(on Wikipedia)_](https://en.wikipedia.org/wiki/String-searching_algorithm)
- [Trie _(on Wikipedia)_](https://en.wikipedia.org/wiki/Trie)

## Misc.
- [Tim (given name) _(on Wikipedia)_](https://en.wikipedia.org/wiki/Tim_(given_name))
