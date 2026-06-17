# @plantuml/mcp-js

A pure Node/JavaScript [Model Context Protocol](https://modelcontextprotocol.io)
(MCP) server for PlantUML, powered by the TeaVM-compiled PlantUML engine.

**No Java required** — the PlantUML engine is compiled to JavaScript with
[TeaVM](https://teavm.org/) from the exact same source as the main PlantUML jar,
so the server runs on a plain Node process.

## What it is for

This is a lightweight alternative to the Java-based [`plantuml-mcp`](https://github.com/plantuml/plantuml-mcp)
server. Both expose PlantUML capabilities to MCP clients (LLM tools, IDEs,
agents), but they differ in one important way:

- `plantuml-mcp` (Java / Spring AI) requires a **Java runtime (JRE)** to run.
- `plantuml-mcp-js` requires only **Node.js** — no Java needed.

The PlantUML engine itself is compiled to JavaScript with
[TeaVM](https://teavm.org/), from the exact same Java source as the main
PlantUML jar. This dramatically lowers the barrier to entry for the `stdio`
transport: a user can run the server with `node` without installing or
configuring a JVM.

## Quick test with the MCP Inspector

To poke at the tools interactively before wiring up a client, use the
[MCP Inspector](https://github.com/modelcontextprotocol/inspector) — no clone,
no install:

```sh
npx @modelcontextprotocol/inspector npx -y @plantuml/mcp-js
```

It opens a browser UI (with a session token in the printed URL). Make sure the
transport is **stdio**, click **Connect**, then open **Tools → List Tools** to
see `plantuml_version`, `check_syntax` and `render_diagram`, and run them with
the inputs shown below.


## Quick start

You don't need to clone anything. Point your MCP client at `npx`, which
downloads and runs the server on demand:

```json
{
  "mcpServers": {
    "plantuml-js": {
      "command": "npx",
      "args": ["-y", "@plantuml/mcp-js"]
    }
  }
}
```

To avoid surprise updates, pin a specific version:

```json
{
  "mcpServers": {
    "plantuml-js": {
      "command": "npx",
      "args": ["-y", "@plantuml/mcp-js@0.1.3"]
    }
  }
}
```

This works in any MCP client that supports the **stdio** transport (LM Studio,
Claude Desktop, and others). The configuration format varies slightly by client,
but the `command` / `args` pair is the same. After adding it, restart or reload
your client's MCP servers and the tools below become available to the model.

You can also run it straight from a terminal to check it starts:

```sh
npx -y @plantuml/mcp-js
```

It prints `plantuml-mcp-js server started (stdio).` to stderr and then waits for
a client to connect — that is normal, a stdio server is driven by its client.

## Tools

| Tool               | Status      | Description                                      |
| ------------------ | ----------- | ------------------------------------------------ |
| `plantuml_version` | available   | Returns the version of the embedded PlantUML.    |
| `check_syntax`     | available   | Validates a diagram and reports syntax errors.   |
| `render_diagram`   | available   | Renders a diagram to a deterministic SVG.        |
| `diagram_explain`  | available   | Explains a diagram line by line.                 |

**`plantuml_version`** takes no parameters and returns the version string.

**`check_syntax`** takes a single `source` parameter. A valid diagram:

```
@startuml
Alice -> Bob
@enduml
```

returns something like:

```json
{ "valid": true, "diagramType": "SequenceDiagram", "lineCount": 3, "warnings": [] }
```

An invalid one reports the offending line and message:

```json
{
  "valid": false,
  "lineCount": 3,
  "warnings": [],
  "errorLineNumber": 2,
  "errorMessage": "...",
  "errorLine": "Alice ->"
}
```

The failure fields are `errorLineNumber` (1-based), `errorMessage`, `errorLine`
(the offending source line, when available) and `errorContext`.

**`render_diagram`** takes a single `source` parameter and renders the diagram
to a deterministic SVG. A valid diagram:

```
@startuml
Alice -> Bob
@enduml
```

returns something like:

```json
{
  "valid": true,
  "diagramType": "SequenceDiagram",
  "lineCount": 3,
  "warnings": [],
  "svg": "<svg ...>...</svg>"
}
```

The rendering is **deterministic**: text dimensions come from a built-in glyph
width table rather than from the host's installed fonts or AWT, so the same
source yields byte-for-byte the same SVG on any machine. On failure it reports
the same fields as `check_syntax` (without `svg`).

**`diagram_explain`** takes a single `source` parameter and explains how the
diagram is parsed, line by line. It returns a JSON array of objects, each with
`input` (the source line(s) that produced the explanation), `explain` (a
human-readable explanation) and `line` (1-based line number, when available).
For example:

```
@startuml
Alice -> Bob
@enduml
```

returns something like:

```json
[
  { "input": "Alice -> Bob", "explain": "...", "line": 2 }
]
```

## Scope and limitations

The JavaScript build is **headless** (no browser DOM, no native Graphviz). It
renders to **SVG only**, via a deterministic, font-metrics-free pipeline (see
`render_diagram` above); raster and document formats (PNG/PDF) are out of scope
here, as is any layout that requires native Graphviz.

If you need those other formats, or a Graphviz-backed layout, use the
Java-based [`plantuml-mcp`](https://github.com/plantuml/plantuml-mcp) server
instead. It exposes the same kind of tools but requires a Java runtime.

## Architecture

```
plantuml (root project)                 net.sourceforge.plantuml.teavm.headless.PlantUMLHeadless
        │                               a minimal, DOM-free entry point (@JSExport)
        ▼
plantuml-mcp-js (this subproject)        TeaVM compiles the engine to ES2015 JavaScript
        │                               -> build/generated/teavm/js/plantuml-mcp-js.js
        ▼
server.js (Node)                         imports the JS module and exposes it as MCP tools
        │                               over the stdio transport
        ▼
MCP client (LM Studio, Claude Desktop, ...)
```

Unlike the root project's TeaVM *browser* build (`PlantUMLBrowser`), the
headless entry point pulls in none of the DOM / Viz.js / worker-thread
machinery: the exported functions are synchronous and return plain strings.


## Links

- Source and issues: <https://github.com/plantuml/plantuml>
- PlantUML website: <https://plantuml.com/>

## License

MIT.
