// plantuml-wasm.js -- EXPERIMENTAL Wasm GC flavour of plantuml.js
//
// Same engine (net.sourceforge.plantuml.teavm.js.browser.PlantUMLBrowser), compiled
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
//   - Wasm GC is required (Chrome 119+, Firefox 120+, Safari 18.2+). Like
//     plantuml.js, the engine loads its resources through <script> tags, so it
//     needs a browser, not bare Node.js.
//     Use isWasmGCSupported() to fall back to plantuml.js on older runtimes.
//
// If the engine crashes (a Wasm trap, a stack overflow on a very long statement),
// the pending renderToString() requests fail with an explicit message, the
// elements waiting for a render() show that message, and the next call loads a
// fresh instance. This also works when the file is loaded in a worker.
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

// render() requests into a DOM element that have not produced anything yet. The
// engine does not tell when a render() is done, so a request is considered
// pending as long as the first child of its element is the one it had when the
// request was made (a finished rendering, even an error message, replaces it).
const pendingElements = new Set();

function prunePendingElements() {
	for (const entry of [...pendingElements])
		if (entry.element.firstChild !== entry.before || entry.element.isConnected === false)
			pendingElements.delete(entry);
}

// An error that escapes from the engine cannot be caught by Java code, and
// leaves the instance unusable, so that every later request would hang forever:
//   - a Wasm trap (WebAssembly.RuntimeError), e.g. an integer division by zero,
//     which TeaVM's Wasm GC backend does not turn into an ArithmeticException;
//   - a stack overflow of the host (RangeError, or InternalError in Firefox: "too
//     much recursion"), which is not a WebAssembly.RuntimeError either.
// So any error that reaches the global scope while a request is waiting for its
// answer is treated as fatal: the pending requests fail with an explicit
// message, and the instance is dropped, the next call loading a fresh one.
// Errors raised while nothing is pending are not the engine's business.
function onEngineError(error) {
	prunePendingElements();
	if (pending.size === 0 && pendingElements.size === 0)
		return;
	enginePromise = null;
	const reason = error && error.message ? error.message : String(error);
	const message = "PlantUML Wasm engine crashed (" + reason + "); it has been reset.";
	// Copy first: each fail() removes its request from the set.
	for (const request of [...pending])
		request.fail(message);
	for (const entry of [...pendingElements]) {
		pendingElements.delete(entry);
		entry.element.textContent = message;
	}
}

// globalThis, not window: the same file runs in a page and in a (module) worker,
// and a worker has no window.
if (typeof globalThis.addEventListener === "function")
	globalThis.addEventListener("error", ev => onEngineError(ev.error ?? ev.message));

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
	// Only in a page: a worker has no DOM element to render into.
	const element = typeof document === "undefined" ? null : document.getElementById(elementId);
	if (element !== null) {
		prunePendingElements();
		pendingElements.add({ element, before: element.firstChild });
	}
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
