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
see `plantuml_version` and `check_syntax`, and run them with the inputs shown
below.


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
      "args": ["-y", "@plantuml/mcp-js@0.1.1"]
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
| `diagram_explain`  | planned     | Explains a diagram line by line.                 |

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

## Scope and limitations

Because the JavaScript build is **headless** (no browser DOM, no native
Graphviz), it deliberately does **not** render diagrams: the SVG output pipeline
depends on the browser DOM and is out of scope here. This server focuses on the
text-only capabilities of the engine.

If you need diagram **rendering** (SVG/PNG/PDF), use the Java-based
[`plantuml-mcp`](https://github.com/plantuml/plantuml-mcp) server instead. It
exposes the same kind of tools but requires a Java runtime.

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
