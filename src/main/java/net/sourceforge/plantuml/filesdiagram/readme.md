# Directory Documentation for `filesdiagram`

## Description
This package provides classes used to manage [[Tree] Files diagram](http://alphadoc.plantuml.com/doc/markdown/en/files-diagram).

## Link
- [[Tree] Files diagram _(on alpha-doc)_](http://alphadoc.plantuml.com/doc/markdown/en/files-diagram)

## Reference
- [GH-1448](https://github.com/plantuml/plantuml/issues/1448)
- [GH-3010](https://github.com/plantuml/plantuml/issues/3010): `tree` output as input
- Inspired by (and mostly compatible with) [Mermaid treeView](https://mermaid.js.org/syntax/treeView.html)

## Syntax

Several syntaxes are accepted, and can be mixed (`FilesParser`):

```
@startfiles
/src/main/App.java              ' absolute path (historical syntax)

project/                        ' indentation (spaces or tabs)
    src/
        App.java

├── src/                        ' GNU "tree" output, light or heavy box drawing
│   └── App.java
┣━━ src/
┃   ┗━━ App.java

|   readme.md                   ' Windows "tree /f" output (/a or not, | or ¦)
+---bin
|       a.exe
\---etc
@endfiles
```

The depth of an entry is the column where its name starts, once tabs are
expanded and tree characters removed (`TreePrefix`). An entry is the child of
the closest previous entry starting on its left, so indentation does not need to
be regular. Lines such as `.`, `C:.`, `Folder PATH listing...` or
`3 directories, 5 files` are ignored.

An entry is a folder if its name ends with `/`, if it has children, or if it
comes after a Windows branch glued to its name (`+---bin`, `├───bin`).
A name containing `/` creates the intermediate folders.

### Annotations (`FLabel`), in any order after the name

| Syntax | Effect |
|---|---|
| `## text` | inline description (creole allowed) |
| `:::cls` or `<<cls>>` | stereotype, styled by `.cls` in `<style>` (`highlight` is predefined) |
| `#color` | background color of the row |
| `icon(...)` | explicit icon: `<:coffee:>`, `:coffee:`, `<&folder>`, `&folder`, `<$sprite>`, `none` |
| `"..."` | quoted name, to use one of the markers above in a name |

### Icons (`FilesIcons`)

Files get an icon from their name or extension (`.java`, `.py`, `.md`, `Dockerfile`...).
Rules can be declared, and take precedence over the built-in ones:

```
[icon *.java <:coffee:>]
[icon Makefile <:hammer:>]
[icon build/ <&wrench>]
[icon * <:1f4c4:>]    ' any other file (restores the historical look)
[icon */ none]        ' any other folder
```

### Style

```
<style>
filesDiagram {
  LineColor #A0A0A0      ' connector lines (LineThickness 0 removes them)
  folder { FontStyle bold }
  file { }
  description { FontColor #6A9955 }
  note { }
  .highlight { BackGroundColor #FFF3CD; LineColor #FFC107 }
}
</style>
```

### Text output

`-tutxt` and `-ttxt` print the tree as the `tree` command does (`FilesTxt`),
with a trailing `/` on folders and the `##` descriptions, so that the output can
be pasted back into a diagram.

## Credit

## Misc.

