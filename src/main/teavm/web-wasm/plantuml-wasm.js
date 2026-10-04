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

// TeaVM builds Java stack traces from the JS Error stack, which V8 truncates to
// 10 frames by default: far too few to reach PlantUML code from the runtime.
if (WASM_DEBUG && typeof Error.stackTraceLimit === "number" && Error.stackTraceLimit < 200)
	Error.stackTraceLimit = 200;

let enginePromise = null;

// renderToString() requests whose callbacks have not been called yet.
const pending = new Set();

// A Wasm trap (e.g. an integer division by zero: TeaVM's Wasm GC backend does
// not turn it into an ArithmeticException) cannot be caught by Java code. It
// escapes as a WebAssembly.RuntimeError from a TeaVM event-loop callback and
// leaves the instance unusable: every later request would hang forever. So
// fail the pending requests and drop the instance: the next call loads a
// fresh one.
function onWasmTrap(error) {
	if (typeof WebAssembly === "undefined" || error instanceof WebAssembly.RuntimeError === false)
		return;
	enginePromise = null;
	// Copy first: each fail() removes its request from the set.
	for (const request of [...pending])
		request.fail("PlantUML Wasm engine crashed (" + error.message + "); it has been reset.");
}

if (typeof window !== "undefined" && typeof window.addEventListener === "function")
	window.addEventListener("error", ev => onWasmTrap(ev.error));

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
	const request = {};
	const settle = callback => value => {
		if (pending.delete(request))
			callback(value);
	};
	request.fail = settle(onError);
	pending.add(request);
	engine().then(
		e => e.renderToString(lines, settle(onSuccess), request.fail, options ?? null),
		err => request.fail("Could not load plantuml.wasm: " + err));
}
