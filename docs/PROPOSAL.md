# Motivation

The current implementation of `\n` for escaping newlines in PlantUML has proven to be unintuitive and prone to confusion, particularly in scenarios involving both the preprocessor and string literals. This overlap often leads to errors or unexpected behavior, making it a less effective solution for handling line breaks in multi-line strings or labels.

To address these issues and simplify syntax, the `\n` escape sequence will be removed from the language.

# Multiline text blocks

A new syntax for defining multiline text blocks is introduced, inspired by the triple-quoted strings found in Python and Java (`'''` and `"""`) by also allowing the use of the `!!!` delimiter as a block separator. This approach aligns closely with established standards while adding flexibility for use in existing source code. Explicit newlines are preserved in the rendered output, ensuring readability and consistency.  

### Syntax and example

Consider the following syntax:

```
Alice -> Bob : """012
                  345
                  678""" <<blue>>   
```

The content enclosed between the `"""` delimiters is treated as a multiline string, with each line explicitly separated by `%newline()` in the rendered output. This ensures that the visual structure of the string in the source code is faithfully preserved in the diagram.

The above block will render as:

```
Alice -> Bob : 012%newline()345%newline()678 <<blue>>   
```

You can also write it with less indentation, as shown below:

```
Alice -> Bob : """012
   345
   678""" <<blue>>   
```


This makes it easy to include explicit line breaks in diagram labels or annotations while maintaining a clear and consistent syntax in the source code.


### Handling extra spaces

The syntax also preserves spaces at the beginning of each line within the block. For instance:

```
Alice -> Bob : !!!012
    345
   678!!! <<blue>>   
```

This will render as:

```
Alice -> Bob : 012%newline() 345%newline()678 <<blue>>   
```

# Concatenated text blocks

In the proposed update, multi-line text blocks within diagram elements will be treated as a single continuous string, allowing for simpler syntax
while maintaining flexibility in formatting.

Consider the following syntax:

```
Alice -> Bob : ###012
   345
   678### <<blue>>   
```

The concatenated content inside the `###` block will render as a single line in the diagram:

```
Alice -> Bob : 012345678 <<blue>>   
```

This approach makes it easier for users to format long strings without compromising readability or output accuracy.


# Typed text blocks

By default, the [Creole parser](https://en.wikipedia.org/wiki/Creole_(markup)) is used for processing all text in PlantUML. This decision dates back to 2009, a time when the Creole syntax offered a neutral and widely compatible solution for wiki-style text formatting. It’s worth noting that in 2009, the dominance of Markdown as the standard markup language was far from certain. The choice of [Creole](https://en.wikipedia.org/wiki/Creole_(markup)) reflected the uncertainty of that era and PlantUML's aim to provide a simple, interoperable solution.

With the introduction of *Typed Text Blocks*, users now have the ability to specify a parser type explicitly for their multiline text blocks. This allows greater flexibility and ensures compatibility with different text formatting styles.

### Raw text example

When a text block is explicitly typed as `raw`, no wiki or markup parsing is applied. Characters are displayed exactly as written, allowing for precise control over the output.
This will be probably more useful in *Concatenated Text Blocks* than in *Multiline Text Blocks*

For example:

```
Alice -> Bob : ##raw#**|4|**### <<blue>>   
```

Here, the `**` characters and `|4|` content are rendered as-is, without being interpreted as bold or table elements.

### Markdown example

Typed text blocks also support Markdown syntax, allowing users to take advantage of its rich text formatting capabilities. For instance:

```
Alice -> Bob : !!markdown!
| **Column A** | *Column B* |
|--------------|------------|
| Value 1      | Value 2    |
| Value 3      | *Value 4*  |
   !!! <<blue>>   
```

This block will render as a Markdown-styled table, interpreted and displayed according to Markdown conventions.

### Versatility of typed blocks

One of the key advantages of this feature is the ability to specify a wide range of block types, tailored to the user's needs. Supported types may include:
- **raw**: Outputs text exactly as written, with no parsing.
- **markdown**: Enables Markdown formatting, useful for tables, lists, and styled text.
- **creole**: Maintains compatibility with PlantUML's default formatting.


# What will still be working

The existing `%newline()` function will remain supported in PlantUML. Users who prefer its simplicity or are accustomed to its behavior can continue to use it for creating line breaks in their text.

For example, the following syntax will still render as expected:

```
Alice -> Bob : 012%newline()345%newline()678 <<blue>>   
```

This ensures backward compatibility and provides an alternative for those who do not wish to adopt the new multiline text block features. `%newline()` remains a versatile and explicit tool for managing line breaks, particularly in cases where users want to control text layout without relying on multiline syntax delimiters.


# Why new separators

The introduction of new separators for multiline text blocks in PlantUML addresses a practical challenge: ensuring clarity and compatibility when embedding PlantUML diagrams within other programming languages.

In languages like Java or Python, multiline strings are often delimited by `"""` or `'''`. If PlantUML were to use these same delimiters for its multiline text blocks, it would create ambiguity and confusion when PlantUML diagrams are embedded directly within such strings. For instance:

### The problem with shared delimiters

Consider the following Java code:

```java
String diagram = """
Alice -> Bob : """
   345
   678""" <<blue>>   
""";
```

In this example, the use of `"""` both for Java's multiline string and PlantUML's text block would lead to parsing issues. The programming language's interpreter would have difficulty distinguishing between the diagram's delimiters and the string delimiters.

### A clearer alternative

By introducing distinct separators such as `###`, `!!!`, and `!!<type>!`, PlantUML avoids this ambiguity. For example:

```java
String diagram = """
Alice -> Bob : !!!012
   345
   678!!! <<blue>>   
""";
```

In this format, the `!!!` delimiters are unique to PlantUML, ensuring that the diagram content is clearly separated from the host language's syntax. This makes it easier to write, read, and debug embedded diagrams without introducing conflicts or confusion.

### Benefits of new separators

- **Clarity**: Unique separators make it easier to identify PlantUML syntax within source code.
- **Compatibility**: Avoids conflicts with existing string delimiters in popular programming languages.
- **Readability**: Improves the visual distinction between PlantUML syntax and surrounding code.
- **Flexibility**: Supports advanced features like typed blocks (`!!markdown!`, `!!raw!`) without sacrificing compatibility or usability.

The adoption of new separators is a deliberate design choice to ensure that PlantUML remains a practical and intuitive tool for developers embedding diagrams directly into their code.

# Key considerations

1. **Separation from Concatenated Text Blocks:** The `!!!` syntax is distinct from the `###` syntax used for concatenated text blocks. While `###` combines multiline content into a single line, the `!!!` syntax retains explicit line breaks using `%newline()`.
2. **Use Cases:** The `!!!` syntax is particularly suited for scenarios where users want to represent structured multiline text, such as in labels, notes, or comments within diagrams.
3. **Flexibility:** This feature provides additional flexibility without introducing ambiguity, complementing the existing `%newline()` functionality and improving PlantUML's ability to handle complex text formatting.  

