#!/usr/bin/env node
//
// plantuml-mcp-js
//
// Pure Node/JS Model Context Protocol (MCP) server for PlantUML.
//
// It exposes PlantUML capabilities as MCP tools over the stdio transport,
// backed by the TeaVM-compiled PlantUML engine (no Java runtime required).
//
// Tools:
//   - plantuml_version : returns the embedded PlantUML version.
//   - check_syntax     : validates a single diagram without rendering it.
//
// IMPORTANT: stdout is the MCP transport channel in stdio mode. Never write
// anything but JSON-RPC to it. All diagnostics go to stderr.

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

import { fileURLToPath, pathToFileURL } from "node:url";
import { dirname, resolve } from "node:path";
import { existsSync } from "node:fs";

// --- Protect the stdio JSON-RPC channel -------------------------------------
//
// In stdio mode, stdout carries the MCP JSON-RPC stream and NOTHING else. The
// TeaVM-compiled engine maps Java's System.out to console.log, which writes to
// stdout; any stray log line from the engine (or a dependency) would corrupt
// the protocol and make the client fail with "Expected ',' or ']' ..." JSON
// parse errors.
//
// We therefore reroute console.log/info/debug to stderr *before* importing the
// engine, so engine output is harmless. console.error already goes to stderr.
// The MCP SDK writes to stdout through process.stdout directly, so it is
// unaffected.
console.log = (...args) => console.error(...args);
console.info = (...args) => console.error(...args);
console.debug = (...args) => console.error(...args);

const __dirname = dirname(fileURLToPath(import.meta.url));

// Locate the TeaVM-compiled headless engine. Two layouts are supported:
//
//  1. Published / assembled package: the engine sits next to this file as
//     `engine.js` (produced by the `npmPackage` Gradle task).
//  2. Local dev checkout: the engine is the raw TeaVM output under
//     `build/generated/teavm/js/plantuml-mcp-js.js` (produced by
//     `./gradlew :plantuml-mcp-js:generateJavaScript`).
//
// The flattened `engine.js` is preferred so `npx @plantuml/mcp-js` works, with
// a fallback to the build output so `node server.js` works straight from the
// repo without assembling the package first.
const FLAT_ENGINE = resolve(__dirname, "engine.js");
const DEV_ENGINE = resolve(
  __dirname,
  "build/generated/teavm/js/plantuml-mcp-js.js"
);
const ENGINE_PATH = existsSync(FLAT_ENGINE) ? FLAT_ENGINE : DEV_ENGINE;

// The TeaVM ES2015 module exposes the @JSExport-ed static methods of
// PlantUMLHeadless as named exports (e.g. `version`).
// pathToFileURL is required so the dynamic import works with absolute paths on
// Windows (a bare "C:\\..." path is rejected by the ESM loader as an unknown
// URL scheme).
const engine = await import(pathToFileURL(ENGINE_PATH).href);

const server = new McpServer({
  name: "plantuml-mcp-js",
  version: "0.1.0",
});

server.tool(
  "plantuml_version",
  "Returns the version of the embedded PlantUML library (TeaVM JS build).",
  {},
  async () => {
    const version = engine.version();
    return {
      content: [{ type: "text", text: version }],
    };
  }
);

server.tool(
  "check_syntax",
  "Checks the syntax of a single PlantUML diagram without rendering it. " +
    "Returns a JSON object containing: 'valid' (boolean), 'diagramType' (if " +
    "valid), 'lineCount', 'warnings' (list of non-fatal warnings), and on " +
    "failure 'errorLineNumber' (1-based line number), 'errorMessage', " +
    "'errorLine' (the offending source line, when available), and " +
    "'errorContext'.",
  {
    source: z
      .string()
      .describe(
        "The PlantUML source to check, including @start.../@end... (a single diagram)"
      ),
  },
  async ({ source }) => {
    // engine.checkSyntax returns a JSON string built on the Java side.
    const json = engine.checkSyntax(source);
    // Validate it parses (and surface a clear error if the engine ever
    // returns something unexpected), then hand the JSON text to the client.
    let isError = false;
    try {
      const parsed = JSON.parse(json);
      isError = parsed.valid === false;
    } catch (e) {
      return {
        isError: true,
        content: [
          { type: "text", text: `Engine returned invalid JSON: ${String(e)}` },
        ],
      };
    }
    return {
      isError,
      content: [{ type: "text", text: json }],
    };
  }
);

const transport = new StdioServerTransport();
await server.connect(transport);

// Diagnostics on stderr only — stdout is reserved for the JSON-RPC stream.
console.error("plantuml-mcp-js server started (stdio).");
