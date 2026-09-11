# `graphsupport` — graph-support layout engine for Svek

An in-process layout engine built on [graph-support](https://github.com/jamisonjiang/graph-support), a pure-Java
reimplementation of the Graphviz layout algorithms. It lets Svek-family diagrams keep their Graphviz-style
geometry when no `dot` executable is available.

This package is only the adapter. It feeds Svek's model into graph-support and translates the resulting geometry
back; no Svek shape, decoration or style code is duplicated here.

## Description

Svek normally writes a DOT file, runs Graphviz and parses the SVG it prints. graph-support replaces the middle step:
the same model is emitted through a neutral interface, and the coordinates come back as plain data instead of SVG.

```
DotStringFactory
  SvekLayoutEmitter         <-- walks the Svek model, emits nodes/clusters/ranks/edges
    SvekLayoutBuilder       <-- neutral interface, no graph-support types
      GraphSupportSvekLayoutBuilder      <-- this package: builds the graph-support model
        GraphSupportLayoutResultConverter  <-- this package: DrawGraph -> SvekLayoutResult
  SvekLayoutValidation      <-- rejects geometry Svek cannot draw
  SvekLayoutResultApplier   <-- writes coordinates back into SvekNode / SvekEdge / Cluster
```

Everything outside this package is expressed with PlantUML's own types, so the rest of the codebase never sees
`org.graphper`.

## Files

| File                                    | Role                                                                  |
|-----------------------------------------|-----------------------------------------------------------------------|
| `GraphSupportSvekLayoutBuilder.java`    | Builds the graph-support model and runs the layout                    |
| `GraphSupportLayoutResultConverter.java`| Converts graph-support's `DrawGraph` into a neutral `SvekLayoutResult` |

Supporting types live in `net.sourceforge.plantuml.svek.layout` (neutral model, result, validation, and the
`SvekLayoutBuilders` lookup) and in `net.sourceforge.plantuml.svek` (`SvekLayoutEmitter`, `SvekLayoutResultApplier`).

## Engine selection

`CucaDiagram.isUseGraphSupport()` decides, in this order:

1. TeaVM build — never.
2. `!pragma layout graph-support` — always.
3. `!pragma layout elk` / `!pragma layout smetana`, or a usable `dot` executable — never.
4. Otherwise — yes, if this package is present in the running JAR.

So Graphviz stays the default whenever it is installed. graph-support only takes over the case that used to go
straight to Smetana.

## When layout is refused

`GraphSupportSvekLayoutBuilder` declines instead of guessing whenever the model uses something it cannot express,
and `SvekLayoutValidation` rejects geometry Svek could not draw. What happens next depends on how the engine was
selected:

- Selected automatically — the page is rebuilt from the preprocessed source and laid out with Smetana. The
  rebuild is required because simplification mutates the model in place.
- Selected by pragma — Svek continues with Graphviz and records a warning. Without a `dot` executable that ends
  in the usual "Dot Executable" error diagram, exactly like `!pragma layout elk` without ELK on the classpath.

## Lookup

`SvekLayoutBuilders.graphSupport()` instantiates `GraphSupportSvekLayoutBuilder` by name and returns `null` when
the class is missing. Nothing outside this package references it directly, so a distribution that drops this
folder still compiles and runs; it just falls back to Smetana.

## Distributions

graph-support is bundled in the standard GPLv3 JAR and in `plantuml-epl`. It is absent elsewhere:

| Build                                   | Bundled | Reason                                               |
|-----------------------------------------|---------|------------------------------------------------------|
| Standard Gradle JAR, `plantuml-epl`     | yes     |                                                       |
| `plantuml-asl` / `bsd` / `lgpl` / `mit` | no      | These artifacts ship no third-party code             |
| `plantuml-gplv2`                        | no      | graph-support is Apache-2.0, incompatible with GPLv2-only |
| Ant (`build.xml`)                       | no      | That build resolves no dependencies                  |

The Ant build excludes this folder from `javac`, for the same reason the `openpdf` and `teavm` folders are stripped
from the builds that cannot use them. Diagrams still render there; they fall back to Smetana when `dot` is missing.

## Link

- graph-support: https://github.com/jamisonjiang/graph-support
- Maven Central: `org.graphper:graph-support-core`

## Credit

graph-support is licensed under Apache-2.0.
