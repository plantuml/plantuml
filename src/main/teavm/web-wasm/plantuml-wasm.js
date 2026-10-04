// plantuml-wasm.js -- EXPERIMENTAL Wasm GC flavour of plantuml.js
//
// Same engine (net.sourceforge.plantuml.teavm.browser.PlantUMLBrowser), compiled
// by TeaVM to WebAssembly (Wasm GC) instead of JavaScript. It exposes the same
// API as plantuml.js, so switching is a one-line change:
//
//   import { render } from "./plantuml.js";        // JS engine (works everywhere)
//   import { render } from "./plantuml-wasm.js";   // Wasm GC engine (recent runtimes)
//
// Differences with plantuml.js:
//   - the .wasm module is fetched and compiled lazily, on the first call (or
//     explicitly with ready()), so render() / renderToString() may start a
//     little later on the very first call;
//   - Wasm GC is required (Chrome 119+, Firefox 120+, Safari 18.2+, Node 22+).
//     Use isWasmGCSupported() to fall back to plantuml.js on older runtimes.
//
// The companion files (viz-global.js, themes.js, <lib>.min.js...) are shared
// with the JS build and must sit next to this file, as for plantuml.js.

import { load } from "./plantuml.wasm-runtime.js";

const WASM_URL = new URL("./plantuml.wasm", import.meta.url).href;

// Readable Java stack traces (classes, methods, lines) instead of
// "Throwable$FakeClass.fakeMethod": opt in with '?wasmdebug' in the page URL.
// Needs plantuml.wasm.teadbg and plantuml.wasm-deobfuscator.wasm next to
// plantuml.wasm (built when debugInformation is enabled in build.gradle.kts).
const WASM_DEBUG = typeof location !== "undefined"
	&& new URLSearchParams(location.search).has("wasmdebug");

let enginePromise = null;

function engine() {
	if (enginePromise === null) {
		const options = WASM_DEBUG ? { stackDeobfuscator: { enabled: true } } : {};
		enginePromise = load(WASM_URL, options).then(teavm => teavm.exports);
		// Allow a retry after a failed load (network error, unsupported runtime...).
		enginePromise.catch(() => { enginePromise = null; });
	}
	return enginePromise;
}

/**
 * Smallest possible module declaring a GC struct type: validates only when the
 * runtime supports the Wasm GC proposal. Cheap and synchronous.
 */
export function isWasmGCSupported() {
	try {
		return typeof WebAssembly === "object"
			&& WebAssembly.validate(new Uint8Array([0, 97, 115, 109, 1, 0, 0, 0, 1, 5, 1, 95, 1, 120, 0]));
	} catch (e) {
		return false;
	}
}

/**
 * Fetches and instantiates the Wasm module. Optional: render() and
 * renderToString() call it implicitly. Useful to preload, or to measure the
 * load time separately from the rendering time.
 */
export async function ready() {
	await engine();
}

/**
 * Same contract as render() in plantuml.js: renders the diagram into the DOM
 * element identified by elementId. Returns a Promise that resolves once the
 * request has been QUEUED (not rendered), so it can be ignored like the
 * void-returning JS version.
 */
export async function render(lines, elementId, options) {
	const e = await engine();
	e.render(lines, elementId, options ?? null);
}

/**
 * Same contract as renderToString() in plantuml.js: onSuccess(svg) or
 * onError(message) is called once the diagram has been rendered.
 */
export function renderToString(lines, onSuccess, onError, options) {
	engine().then(
		e => e.renderToString(lines, onSuccess, onError, options ?? null),
		err => onError("Could not load plantuml.wasm: " + err));
}
