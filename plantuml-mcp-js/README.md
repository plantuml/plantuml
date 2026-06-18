# plantuml-mcp-js

A pure Node/JavaScript [Model Context Protocol](https://modelcontextprotocol.io)
(MCP) server for PlantUML, powered by the TeaVM-compiled PlantUML engine.

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

### Scope and limitations

The JavaScript build is **headless** (no browser DOM, no native Graphviz). It
renders to **SVG only**, via a deterministic, font-metrics-free pipeline; raster
and document formats (PNG/PDF) are out of scope here, as is any layout that
requires native Graphviz.

Tools:

| Tool               | Status      | Description                                      |
| ------------------ | ----------- | ------------------------------------------------ |
| `plantuml_version` | available   | Returns the version of the embedded PlantUML.    |
| `check_syntax`     | available   | Validates a diagram and reports syntax errors.   |
| `render_diagram`   | available   | Renders a diagram to a deterministic SVG.        |
| `explain_diagram`  | available   | Explains a diagram line by line.                 |

If you need raster/document formats (PNG/PDF) or a Graphviz-backed layout, use
the Java-based `plantuml-mcp` server instead.

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

## Building

The JavaScript engine is built by the `plantuml-mcp-js` Gradle subproject,
which requires **Java 11+** (like the other TeaVM tooling). The build only
needs Java; *running* the server does not.

From the repository root:

```sh
./gradlew :plantuml-mcp-js:generateJavaScript
```

This produces the compiled engine at:

```
plantuml-mcp-js/build/generated/teavm/js/plantuml-mcp-js.js
```

Then install the Node dependencies:

```sh
cd plantuml-mcp-js
npm install
```

## Running

```sh
node server.js
```

The server communicates over **stdio** (JSON-RPC on stdout, diagnostics on
stderr). On a successful start it prints to stderr:

```
plantuml-mcp-js server started (stdio).
```

It then waits for an MCP client to connect. This is normal — a stdio server is
driven by its client, not run interactively.

### Quick test with the MCP Inspector

The [MCP Inspector](https://github.com/modelcontextprotocol/inspector) lets you
list and call the tools interactively, without wiring up a full client. From
the subproject directory (after `npm install`):

```sh
npx @modelcontextprotocol/inspector node server.js
```

This starts a small proxy and opens a browser UI (it prints a URL with a
session token). In the UI:

1. Make sure the transport is **stdio** and click **Connect**.
2. Open the **Tools** tab and click **List Tools** — you should see
   `plantuml_version`, `check_syntax`, `render_diagram` and `explain_diagram`.
3. Select a tool, fill in its parameters, and click **Run Tool**.

**`plantuml_version`** takes no parameters and returns the version string.

**`check_syntax`** takes a single `source` parameter. Try a valid diagram:

```
@startuml
Alice -> Bob
@enduml
```

which returns something like:

```json
{ "valid": true, "diagramType": "SequenceDiagram", "lineCount": 3, "warnings": [] }
```

Then try an invalid one:

```
@startuml
Alice ->
@enduml
```

which returns the offending line and message:

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

The failure fields are: `errorLineNumber` (the 1-based line number),
`errorMessage` (what went wrong), `errorLine` (the offending source line, when
available), and `errorContext`.

> **Note:** in a local (non-CI) build, the version string contains unresolved
> placeholders such as `PlantUML version $version$ / $git.commit.id$ [...]`.
> This is expected: the placeholders are patched only in CI builds. The
> JSON-RPC round trip is still fully validated.

## Using it in LM Studio

LM Studio supports MCP servers via its `mcp.json` configuration. Add an entry
pointing `node` at the `server.js` of this subproject.

```json
{
  "mcpServers": {
    "plantuml-js": {
      "command": "node",
      "args": ["C:\\github\\plantuml\\plantuml-mcp-js\\server.js"]
    }
  }
}
```

Notes:

- Adjust the path to `server.js` to match your checkout location. On
  Windows, escape backslashes (`\\`) in JSON, or use forward slashes.
- `node` must be on your `PATH`. If not, replace `"node"` with the absolute
  path to your Node executable.
- Make sure you have built the engine (`./gradlew
  :plantuml-mcp-js:generateJavaScript`) and run `npm install` at least once
  before LM Studio launches the server.

Once configured, restart LM Studio (or reload the MCP servers). The
`plantuml_version` tool will then be available to the model.

## Using it in other MCP clients

Any MCP client that supports the `stdio` transport works the same way: configure
it to launch `node /path/to/plantuml-mcp-js/server.js`. The configuration
format varies by client, but the command and arguments are identical to the
LM Studio example above.

## Publishing to npm (maintainer notes)

> This section is for maintainers releasing the package to the public npm
> registry. End users do **not** need any of this — they just `npx
> @plantuml/mcp-js` (see below).

Publishing is driven by the `:plantuml-mcp-js:npmPackage` Gradle task, which
assembles a clean, self-contained publish directory. You never publish from the
source tree directly: the task gathers the source files and the freshly
compiled engine into `build/npm-mcp-js/`, and you publish from there.

### How the package is assembled

Running the task:

```sh
./gradlew :plantuml-mcp-js:npmPackage
```

produces `plantuml-mcp-js/build/npm-mcp-js/` containing exactly five files:

```
build/npm-mcp-js/
  +- server.js       (copied from the subproject, as-is)
  +- engine.js       (the TeaVM output, flattened from
  |                   build/generated/teavm/js/plantuml-mcp-js.js)
  +- package.json    (copied from the subproject, as-is)
  +- README.md       (this file, copied as-is)
  +- LICENSE         (copied from the subproject, as-is; MIT)
```

The engine is renamed to `engine.js` and placed next to `server.js`, which is
why `server.js` loads `./engine.js` first (and only falls back to the raw
`build/generated/teavm/js/` output for local `node server.js` runs from the
checkout). The `"files"` allow-list in `package.json` lists `server.js`,
`engine.js`, `README.md` and `LICENSE`, so the published tarball is
self-contained.

Because `build/` is git-ignored, the assembled directory never pollutes the
repository.

### One-time setup

1. **Create an npm account** at <https://www.npmjs.com/signup> (if you don't
   already have one) and **verify your email** — npm refuses to publish from an
   unverified account.
2. **Be a member of the `@plantuml` org** on npm with publish rights (the same
   org that owns `@plantuml/core`). The package is scoped to `@plantuml/mcp-js`.
3. **Log in from the CLI**, which stores a token in `~/.npmrc`:
   ```sh
   npm login
   ```
   Confirm you are logged in with `npm whoami`.
4. **Enable two-factor authentication (2FA)** on your npm account. npm prompts
   for a one-time code (OTP) at publish time; you can also pass it with
   `--otp=123456`.

### Releasing a version

1. **Set the version.** The published version comes from the `version` field in
   the subproject's `package.json`. Either edit it there and commit, or override
   it at assembly time without touching the file:
   ```sh
   ./gradlew :plantuml-mcp-js:npmPackage -PnpmVersion=0.2.0
   ```
   npm refuses to republish an existing version, so this must be bumped for
   every release. A version containing a hyphen (e.g. `0.2.0-beta.1`) is treated
   as a prerelease and the task reminds you to publish it under the `beta` tag.

2. **Assemble the package** (rebuilds the engine via its `generateJavaScript`
   dependency, so the engine is never stale):
   ```sh
   ./gradlew :plantuml-mcp-js:npmPackage
   ```
   The task prints a summary with the resolved name, version and the exact
   publish command to run.

3. **Move into the assembled directory and install the runtime dependency**
   (`@modelcontextprotocol/sdk`), so it is recorded for the published package:
   ```sh
   cd plantuml-mcp-js/build/npm-mcp-js
   npm install
   ```

4. **Preview the tarball** without publishing — this lists exactly what would
   ship. Verify `server.js`, `engine.js`, `package.json`, `README.md` and
   `LICENSE` are all present:
   ```sh
   npm pack --dry-run
   ```

5. **Publish.** Scoped packages are private by default, so the **first** publish
   needs `--access public`:
   ```sh
   npm publish --access public
   ```
   For a prerelease version, publish under the `beta` tag instead so it does not
   become the default `latest`:
   ```sh
   npm publish --tag beta --access public
   ```
   You'll be prompted for your 2FA OTP. On success the package appears at
   <https://www.npmjs.com/package/@plantuml/mcp-js>.

For subsequent releases, `--access public` is no longer required (the package
is already public): bump the version, re-run `npmPackage`, `npm install`, and
`npm publish` from the assembled directory.

### After publishing

Users can then run the server without cloning the repo, via `npx`:

```sh
npx @plantuml/mcp-js
```

This works because `package.json` declares a `bin` entry pointing at
`server.js`. The LM Studio / MCP client config can then use `npx` as the
command instead of an absolute path:

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

## License

MIT License.
