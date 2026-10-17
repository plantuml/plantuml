// Smoke test of the Wasm GC engine as shipped in @plantuml/core.
//
//   node smoke-test-wasm.mjs [path-to-npm-package-dir]
//
// The directory defaults to ../build/npm-plantuml (the output of
// ':plantuml-mit:npmPackage'). The script:
//   1. runs 'npm pack' on it and unpacks the tarball like an installation would,
//   2. checks that every file listed in "exports" is really in the tarball,
//   3. serves the unpacked package over HTTP and renders diagrams with
//      plantuml-wasm.js in a headless Chrome/Chromium/Edge, once for each way of
//      giving it the module: by URL, as bytes, as a Blob, with a good integrity
//      and with a wrong one (which must be refused).
// No dependency is needed except Node 18+, npm and one of those browsers (set
// the CHROME environment variable to use a specific executable).
//
// Node.js itself cannot run the engine: it loads its resources (viz-global.js,
// themes.js...) through <script> tags, so a browser is required.

import { execSync, execFileSync, spawn } from "node:child_process";
import { createHash } from "node:crypto";
import fs from "node:fs";
import http from "node:http";
import os from "node:os";
import path from "node:path";
import { fileURLToPath } from "node:url";

const here = path.dirname(fileURLToPath(import.meta.url));
const pkgDir = path.resolve(process.argv[2] ?? path.join(here, "..", "build", "npm-plantuml"));
const failures = [];
const check = (ok, what) => { console.log((ok ? "  ok   " : "  FAIL ") + what); if (!ok) failures.push(what); };

if (!fs.existsSync(path.join(pkgDir, "package.json"))) {
	console.error("No package.json in " + pkgDir + " (run ':plantuml-mit:npmPackage' first).");
	process.exit(2);
}

// --- 1. pack and unpack --------------------------------------------------
const work = fs.mkdtempSync(path.join(os.tmpdir(), "plantuml-core-wasm-"));
console.log("Packing " + pkgDir);
// One command string: 'npm' is npm.cmd on Windows, which needs a shell.
const packed = execSync(`npm pack --json --pack-destination "${work}"`, { cwd: pkgDir });
const tgz = path.join(work, JSON.parse(packed)[0].filename);
const root = path.join(work, "site");
const install = path.join(root, "node_modules", "@plantuml", "core");
fs.mkdirSync(install, { recursive: true });
execFileSync("tar", ["-xzf", tgz, "-C", install, "--strip-components=1"]);

// --- 2. exports ----------------------------------------------------------
console.log("Checking the tarball");
const pkg = JSON.parse(fs.readFileSync(path.join(install, "package.json"), "utf8"));
for (const [key, target] of Object.entries(pkg.exports))
	check(fs.existsSync(path.join(install, target)), `exports["${key}"] -> ${target}`);
for (const f of ["plantuml-wasm.js", "plantuml.wasm", "plantuml.wasm-runtime.js"])
	check((pkg.files ?? []).includes(f) && fs.existsSync(path.join(install, f)), `${f} is packed`);

// --- 3. render in a browser ---------------------------------------------
fs.writeFileSync(path.join(root, "index.html"), `<!doctype html><meta charset="utf-8">
<div id="target"></div><pre id="result">running</pre>
<script type="module">
import { render, renderToString, ready, isWasmGCSupported } from "/node_modules/@plantuml/core/plantuml-wasm.js";
const log = [];
const result = document.getElementById("result");
const report = text => { result.textContent = text; fetch("/result", { method: "POST", body: text }); };
try {
  log.push("wasmgc=" + isWasmGCSupported());
  const mode = new URLSearchParams(location.search).get("mode") ?? "url";
  const sri = new URLSearchParams(location.search).get("sri");
  const wasmUrl = "/node_modules/@plantuml/core/plantuml.wasm";
  log.push("mode=" + mode);
  if (mode === "bytes")
    await ready({ wasm: (await fetch(wasmUrl)).arrayBuffer() });
  else if (mode === "blob")
    await ready({ wasm: await (await fetch(wasmUrl)).blob() });
  else if (mode === "integrity")
    await ready({ wasm: wasmUrl, integrity: sri });
  else if (mode === "badintegrity") {
    let refused = false;
    try { await ready({ wasm: wasmUrl, integrity: sri }); } catch (e) { refused = true; }
    if (!refused) throw new Error("a wrong integrity was accepted");
    report("PASS " + log.join(", ") + ", wrong integrity refused");
    throw "done";
  } else
    await ready();
  const svg = await new Promise((ok, ko) => renderToString(["@startuml", "Alice -> Bob: hello", "@enduml"], ok, ko));
  if (!svg.includes("<svg")) throw new Error("renderToString: no <svg>");
  log.push("renderToString=" + svg.length + " chars");
  render(["@startuml", "class A", "A --> B", "@enduml"], "target");
  for (let i = 0; i < 100 && !document.querySelector("#target svg"); i++)
    await new Promise(r => setTimeout(r, 100));
  if (!document.querySelector("#target svg")) throw new Error("render: no <svg> in the element");
  log.push("render=ok");
  const bad = await new Promise(ok => renderToString(["@startuml", "this is not (valid", "@enduml"], ok, ok));
  log.push("invalid source answered=" + (typeof bad === "string"));
  report("PASS " + log.join(", "));
} catch (e) { if (e !== "done") report("FAIL " + e + " [" + log.join(", ") + "]"); }
</script>`);

const types = { ".js": "text/javascript", ".html": "text/html", ".wasm": "application/wasm", ".json": "application/json" };
let onResult = () => {};
const server = http.createServer((req, res) => {
	if (req.method === "POST" && req.url === "/result") {
		let body = "";
		req.on("data", chunk => body += chunk);
		req.on("end", () => { res.writeHead(204); res.end(); onResult(body); });
		return;
	}
	const file = path.join(root, decodeURIComponent(req.url.split("?")[0]).replace(/^\/$/, "/index.html"));
	if (!file.startsWith(root)) { res.writeHead(403); return res.end(); }
	fs.readFile(file, (err, data) => {
		if (err) { res.writeHead(404); return res.end(); }
		res.writeHead(200, { "content-type": types[path.extname(file)] ?? "application/octet-stream" });
		res.end(data);
	});
});
await new Promise(r => server.listen(0, "127.0.0.1", r));
const url = `http://127.0.0.1:${server.address().port}/`;

function findBrowser() {
	const candidates = [process.env.CHROME,
		"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
		"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",
		"C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe",
		"C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe",
		"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
		"/usr/bin/google-chrome", "/usr/bin/chromium", "/usr/bin/chromium-browser"];
	return candidates.find(c => c && fs.existsSync(c));
}
const browser = findBrowser();
if (!browser) {
	console.log("No Chrome/Chromium/Edge found (set CHROME). Serving on " + url + " -- open it: it must show 'PASS'. Ctrl+C to stop.");
} else {
	console.log("Rendering with " + browser);
	const sha384 = "sha384-" + createHash("sha384").update(fs.readFileSync(path.join(install, "plantuml.wasm"))).digest("base64");
	const wrongSha384 = "sha384-" + createHash("sha384").update("not the module").digest("base64");
	const runs = [["url", ""], ["bytes", ""], ["blob", ""], ["integrity", sha384], ["badintegrity", wrongSha384]];
	for (const [mode, sri] of runs) {
		const args = ["--headless=new", "--disable-gpu", "--no-sandbox", "--no-first-run",
			"--user-data-dir=" + path.join(work, "profile-" + mode),
			url + "?mode=" + mode + "&sri=" + encodeURIComponent(sri)];
		// The page POSTs its verdict to this server (the HTTP server runs in this
		// very process, so the browser must be started asynchronously).
		const child = spawn(browser, args, { stdio: "ignore" });
		const line = await new Promise(resolve => {
			const timer = setTimeout(() => resolve("no answer from the page after 90 s"), 90000);
			onResult = body => { clearTimeout(timer); resolve(body); };
			child.on("error", err => { clearTimeout(timer); resolve("cannot start the browser: " + err.message); });
		});
		child.kill();
		check(line.startsWith("PASS"), line);
	}
	server.close();
	await new Promise(r => setTimeout(r, 1000)); // let the browser release its profile
	fs.rmSync(work, { recursive: true, force: true, maxRetries: 5 });
	console.log(failures.length === 0 ? "\nALL OK" : "\n" + failures.length + " FAILURE(S)");
	process.exit(failures.length === 0 ? 0 : 1);
}
