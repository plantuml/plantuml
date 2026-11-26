'use strict';
// Builds viz-global-nowasm.js: the very same Viz.js as viz-global.js, with its
// embedded WebAssembly module translated to plain JavaScript by binaryen's
// wasm2js, for hosts where WebAssembly cannot run (a Content Security Policy
// without 'wasm-unsafe-eval', WebAssembly disabled by policy, jitless mode).
//
// usage: node build.js [in=<viz-global.js>] [out=<viz-global-nowasm.js>] [minify=false]
//
//   in      default: ../../src/main/teavm/vendor/viz-global.js
//   out     default: ../../src/main/teavm/nowasm/viz-global-nowasm.js
//   minify  default: true (terser, compress off, mangle on: see below)
//
// The tools come from this folder's package.json, at pinned versions
// (npm ci first). Same input + same tool versions = same output, byte for
// byte: nothing here depends on the date, the machine or the file system.
//
// How it works. viz-global.js is an Emscripten build: the Graphviz module is
// a base64 data: URL, decoded and handed to WebAssembly.instantiate(bytes,
// imports); only `instance.exports` is used afterwards. This script
//   1. decodes that module,
//   2. runs wasm2js on it (ES module form, -O), which gives a function of the
//      imports returning the same exports, memory included,
//   3. puts that function inside the UMD factory of viz-global.js, replaces
//      the WebAssembly.instantiate call with it, and drops the base64 payload.
// Every rewrite must match exactly once, or the script stops: a new Viz.js
// release whose glue changed shape needs this script to be looked at again,
// rather than a silently broken output.
const fs = require('fs');
const path = require('path');
const zlib = require('zlib');
const crypto = require('crypto');
const { execFileSync } = require('child_process');

const args = {};
for (const a of process.argv.slice(2)) {
	const m = /^(\w+)=(.*)$/.exec(a);
	if (!m) {
		console.error('usage: node build.js [in=<viz-global.js>] [out=<file>] [minify=false]');
		process.exit(2);
	}
	args[m[1]] = m[2];
}
const repo = path.resolve(__dirname, '..', '..');
const input = path.resolve(args.in || path.join(repo, 'src/main/teavm/vendor/viz-global.js'));
const output = path.resolve(args.out || path.join(repo, 'src/main/teavm/nowasm/viz-global-nowasm.js'));
const minify = args.minify !== 'false';

function fail(msg) {
	console.error('build.js: ' + msg);
	process.exit(1);
}

// Replaces `search` (a string) by `replacement`, which must occur exactly once.
function replaceOnce(text, search, replacement, what) {
	const first = text.indexOf(search);
	if (first < 0)
		fail(what + ': not found (did the Viz.js glue change?)');
	if (text.indexOf(search, first + 1) >= 0)
		fail(what + ': found more than once (did the Viz.js glue change?)');
	return text.slice(0, first) + replacement + text.slice(first + search.length);
}

function binaryenBin(name) {
	const pkg = path.join(__dirname, 'node_modules', 'binaryen');
	const bin = path.join(pkg, 'bin', name);
	if (!fs.existsSync(bin))
		fail('binaryen not installed: run `npm ci` in ' + __dirname);
	return { bin, version: JSON.parse(fs.readFileSync(path.join(pkg, 'package.json'), 'utf8')).version };
}

const source = fs.readFileSync(input, 'utf8').replace(/\r\n/g, '\n');
const header = /^\/\*![\s\S]*?\*\//.exec(source);
if (!header)
	fail('no license header comment at the top of ' + input);
const vizVersion = (/Viz\.js (\S+)/.exec(header[0]) || [])[1] || '?';

// 1. The embedded module.
const PREFIX = 'data:application/octet-stream;base64,';
// The prefix alone also appears, as the constant the glue tests URLs against.
const embedded = [...source.matchAll(/"data:application\/octet-stream;base64,([A-Za-z0-9+\/=]+)"/g)];
if (embedded.length !== 1)
	fail('expected exactly one embedded base64 WebAssembly module, found ' + embedded.length);
const b64Start = embedded[0].index + 1 + PREFIX.length;
const b64End = b64Start + embedded[0][1].length;
const wasm = Buffer.from(source.slice(b64Start, b64End), 'base64');
if (wasm.readUInt32LE(0) !== 0x6d736100)
	fail('the embedded data is not a WebAssembly module');

// 2. wasm2js. The binaryen npm package is itself compiled with Emscripten: its
// `-o` writes into an in-memory file system, so the output is read from stdout.
const tmp = fs.mkdtempSync(path.join(require('os').tmpdir(), 'viz-nowasm-'));
const wasmFile = path.join(tmp, 'viz.wasm');
fs.writeFileSync(wasmFile, wasm);
const wasm2js = binaryenBin('wasm2js');
let asm = execFileSync(process.execPath, [wasm2js.bin, wasmFile, '-O'], { maxBuffer: 1 << 30, encoding: 'utf8' });
fs.rmSync(tmp, { recursive: true, force: true });

// The ES module form starts with `import * as a from 'a';` (the imports object
// of the Emscripten glue is { a: ... }) and ends with the instantiation and one
// `export var` per export. Keep what is between, as the body of a function of
// the imports.
asm = replaceOnce(asm, "import * as a from 'a';\n", '', 'wasm2js import line');
const tail = asm.indexOf('\nvar retasmFunc = asmFunc(');
if (tail < 0)
	fail('wasm2js output: no `var retasmFunc = asmFunc(`');
asm = asm.slice(0, tail);
if (/\beval\b|new Function|\bFunction\(/.test(asm))
	fail('wasm2js output uses eval or the Function constructor: it would need unsafe-eval');
if (/\bWebAssembly\b/.test(asm))
	fail('wasm2js output still refers to WebAssembly');
// asmFunc reads the imports through the free variable `a`.
const factory = 'var __plantumlVizAsm=function(imports){var a=imports.a;\n' + asm + '\nreturn asmFunc(imports);};';

// 3. The glue. No module to decode any more: an empty data: URL decodes to an
// empty array, which keeps the loading path of the glue unchanged.
let js = source.slice(header[0].length, b64Start) + source.slice(b64End);
// The factory goes first in the UMD factory function, in strict mode like the
// ES module it comes from.
js = replaceOnce(js, '(this,function(A){"use strict";', '(this,function(A){"use strict";' + factory, 'UMD factory');
js = replaceOnce(js, 'WebAssembly.instantiate(A,I)', 'Promise.resolve({instance:{exports:__plantumlVizAsm(I)}})',
	'WebAssembly.instantiate call');
// Where WebAssembly does not exist at all, aborting must still throw an error.
js = replaceOnce(js, 'new WebAssembly.RuntimeError(A)',
	'new("undefined"!=typeof WebAssembly?WebAssembly.RuntimeError:Error)(A)', 'abort error');
if (/WebAssembly\.(instantiate|compile|Module|Instance|validate)/.test(js))
	fail('the result still compiles WebAssembly somewhere');

let tools = 'binaryen ' + wasm2js.version + ' (wasm2js -O)';
if (minify) {
	// Mangling alone takes the size down by about a third. `compress` is left
	// off: it is slow on this much code and gains little after wasm2js -O.
	const terserDir = path.join(__dirname, 'node_modules', 'terser');
	if (!fs.existsSync(terserDir))
		fail('terser not installed: run `npm ci` in ' + __dirname + ' (or pass minify=false)');
	const terser = require(terserDir);
	const terserVersion = JSON.parse(fs.readFileSync(path.join(terserDir, 'package.json'), 'utf8')).version;
	tools += ', terser ' + terserVersion + ' (mangle)';
	const done = terser.minify_sync
		? terser.minify_sync(js, { compress: false, mangle: true, format: { comments: false } })
		: null;
	if (!done)
		fail('this terser has no minify_sync: pin terser 5.33 or later');
	js = done.code;
}

const banner = header[0].replace(/\*\/$/,
	'\nviz-global-nowasm.js: the WebAssembly module of viz-global.js translated to\n' +
	'JavaScript, for hosts where WebAssembly cannot run. Same Graphviz, same API.\n' +
	'Generated by plantuml/tools/viz-nowasm/build.js with ' + tools + '\n' +
	'from viz-global.js sha256 ' + crypto.createHash('sha256').update(source).digest('hex') + '\n' +
	'The runtime helpers emitted by wasm2js come from Binaryen\n' +
	'https://github.com/WebAssembly/binaryen (Apache License 2.0)\n*/');
const result = banner + '\n' + js.replace(/^\n+/, '') + (js.endsWith('\n') ? '' : '\n');
fs.writeFileSync(output, result);

function sizes(buf) {
	return (buf.length / 1048576).toFixed(2) + ' MB, gzip ' + (zlib.gzipSync(buf, { level: 9 }).length / 1048576).toFixed(2) + ' MB';
}
console.log('Viz.js ' + vizVersion + ', ' + tools);
console.log('  in : ' + path.relative(process.cwd(), input) + ' (' + sizes(Buffer.from(source)) + ')');
console.log('  out: ' + path.relative(process.cwd(), output) + ' (' + sizes(Buffer.from(result)) + ')');
