// render-test.mjs — run from plantuml-mcp-js/
import * as vizModule from "@viz-js/viz";
let p = null;
globalThis.Viz = { instance: () => (p ??= vizModule.instance()) };

const engine = await import(
  (await import("node:url")).pathToFileURL(
    "./build/generated/teavm/js/plantuml-mcp-js.js"
  ).href
);

function render(source) {
  return new Promise((resolve) => engine.renderSvg(source, resolve));
}

for (const src of [
  "@startuml\nAlice -> Bob\n@enduml",                       // sequence (no graphviz)
  "@startuml\nclass A\nclass B\nA --> B\n@enduml",          // class (graphviz/viz.js)
]) {
  const json = await render(src);
  const o = JSON.parse(json);
  console.log(o.diagramType, "valid:", o.valid, "svgLen:", o.svg ? o.svg.length : "(none)", o.errorMessage || "");
}
