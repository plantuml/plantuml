import { render } from "./plantuml.js";
import { decodePlantUml, encodePlantUml } from "./plantuml-codec.js";
import { createZoomController } from "./zoom.js";

const editor = document.getElementById("editor");
const defaultSource = editor.value;
const HASH_DEBOUNCE_MS = 300;
let dark = false;
let hashDebounceTimer = null;
let toastTimer = null;
const livePreviews = {
	svg: {format: "SVG", previewWindow: null, button: null, ready: false},
	png: {format: "PNG", previewWindow: null, button: null, ready: false}
};

restoreEditorFromHash();
renderer();
resize();
controls();
contextMenu();
urlSharing();
zoomControls();

function zoomControls() {
	createZoomController({
		viewport: document.getElementById("out"),
		zoomOut: document.getElementById("zoom-out"),
		zoomReset: document.getElementById("zoom-reset"),
		zoomIn: document.getElementById("zoom-in"),
		panToggle: document.getElementById("pan-tool")
	});
}

function renderer() {
	const loading = document.getElementById("loading");

	try {
		editor.addEventListener("input", renderNow);
		renderNow();

		loading.style.display = "none";
	} catch (err) {
		console.error("Error", err);
		loading.textContent = "Error: " + err.message;
	}
}

function renderNow() {
	const lines = editor.value.split(/\r\n|\r|\n/);
	render(lines, "out", {dark: dark});
	updateLivePreviews();
}

function restoreEditorFromHash(useDefaultWhenEmpty = false) {
	const fragment = window.location.hash.slice(1);
	if (!fragment) {
		if (useDefaultWhenEmpty) {
			editor.value = defaultSource;
		}
		return true;
	}

	try {
		editor.value = decodePlantUml(fragment);
		return true;
	} catch (err) {
		console.warn("Could not decode the PlantUML URL fragment:", err);
		return false;
	}
}

function replaceHashFromEditor() {
	clearTimeout(hashDebounceTimer);
	const fragment = editor.value ? `#${encodePlantUml(editor.value)}` : "";
	const url = `${window.location.pathname}${window.location.search}${fragment}`;
	window.history.replaceState(window.history.state, "", url);
}

function urlSharing() {
	editor.addEventListener("input", () => {
		clearTimeout(hashDebounceTimer);
		hashDebounceTimer = setTimeout(replaceHashFromEditor, HASH_DEBOUNCE_MS);
	});

	window.addEventListener("hashchange", () => {
		clearTimeout(hashDebounceTimer);
		if (restoreEditorFromHash(true)) {
			renderNow();
		}
	});

	const share = document.getElementById("share");
	share.addEventListener("click", () => {
		replaceHashFromEditor();
		copyText(window.location.href).then(
			() => {
				showControlResult(share, "success", 300);
				showToast("Copied");
			},
			reason => {
				console.error("Copy shareable link failed:", reason);
				showControlResult(share, "error", 3000);
				showToast("Copy failed", true);
			}
		);
	});
}

function showControlResult(button, result, duration) {
	button.classList.add(result);
	setTimeout(() => button.classList.remove(result), duration);
}

function showToast(message, isError = false) {
	const toast = document.getElementById("toast");
	clearTimeout(toastTimer);
	toast.textContent = message;
	toast.classList.toggle("error", isError);
	toast.classList.add("visible");
	toastTimer = setTimeout(() => toast.classList.remove("visible"), 1600);
}

async function copyText(content) {
	try {
		if (navigator.clipboard?.writeText) {
			await navigator.clipboard.writeText(content);
			return;
		}
	} catch (err) {
		console.warn("Clipboard API unavailable; using selection fallback:", err);
	}

	const textarea = document.createElement("textarea");
	textarea.value = content;
	textarea.setAttribute("readonly", "");
	textarea.style.position = "fixed";
	textarea.style.opacity = "0";
	document.body.appendChild(textarea);
	textarea.select();
	const copied = document.execCommand("copy");
	textarea.remove();
	if (!copied) {
		throw new Error("The browser denied clipboard access");
	}
}

function isLivePreviewOpen(state) {
	if (state.previewWindow == null) {
		return false;
	}
	if (state.previewWindow.closed) {
		releaseLivePreview(state);
		return false;
	}
	return true;
}

function releaseLivePreview(state) {
	state.button?.classList.remove("active");
	state.button = null;
	state.ready = false;
	state.previewWindow = null;
}

function getLivePreviewUrl(state) {
	const url = new URL("preview/", window.location.href);
	url.searchParams.set("format", state.format.toLowerCase());
	url.searchParams.set("theme", dark ? "dark" : "light");
	if (editor.value) {
		url.hash = encodePlantUml(editor.value);
	}
	return url;
}

function updateLivePreviewLocation(state) {
	if (!isLivePreviewOpen(state) || !state.ready) {
		return;
	}
	try {
		const url = getLivePreviewUrl(state);
		if (state.previewWindow.location.href !== url.href) {
			state.previewWindow.history.replaceState(null, "", url);
			state.previewWindow.dispatchEvent(new state.previewWindow.Event("hashchange"));
		}
	} catch (err) {
		console.warn(`Stopped updating live ${state.format} preview:`, err);
		releaseLivePreview(state);
	}
}

function openLivePreview(kind, button) {
	const state = livePreviews[kind];
	if (isLivePreviewOpen(state)) {
		state.previewWindow.focus();
		updateLivePreviewLocation(state);
		return;
	}

	const previewWindow = window.open(getLivePreviewUrl(state), "_blank");
	if (previewWindow == null) {
		showControlResult(button, "error", 3000);
		showToast("Popup blocked", true);
		return;
	}

	state.previewWindow = previewWindow;
	state.button = button;
	state.ready = false;
	button.classList.add("active");
	previewWindow.addEventListener("load", () => {
		if (state.previewWindow !== previewWindow) {
			return;
		}
		state.ready = true;
		previewWindow.addEventListener("pagehide", () => {
			if (state.previewWindow === previewWindow) {
				releaseLivePreview(state);
			}
		}, {once: true});
		updateLivePreviewLocation(state);
	}, {once: true});
	previewWindow.focus();
}

function updateLivePreviews() {
	updateLivePreviewLocation(livePreviews.svg);
	updateLivePreviewLocation(livePreviews.png);
}

function resize() {
	const resize = document.getElementById("resize");
	let resizingOffset = null;

	function updateLayout(clientX) {
		if (resizingOffset === null) {
			return;
		}

		let pos = clientX - editor.getBoundingClientRect().left - resizingOffset;
		let clampedPos = Math.min(pos, document.documentElement.clientWidth * 0.85);
		editor.style.flexBasis = `${clampedPos}px`;
	}

	resize.addEventListener("mousedown", e => {
		resizingOffset = e.clientX - editor.getBoundingClientRect().right;
	});
	document.addEventListener("mouseup", () => (resizingOffset = null));
	document.addEventListener("mousemove", e => updateLayout(e.clientX));

}

function controls() {
	const copy = document.getElementById("copy");
	copy.addEventListener("click", () => {
		const content = getContent();
		copyText(content).then(
			() => {
				copy.classList.add("success");
				setTimeout(() => (copy.classList.remove("success")), 300);
			},
			reason => {
				console.error("Copy to clipboard failed:", reason);
				copy.classList.add("error");
				setTimeout(() => (copy.classList.remove("error")), 3000);
			}
		);

	});

	const copyBitmap = document.getElementById("copy-bitmap");
	copyBitmap.addEventListener("click", async () => {
		try {
			const out = document.getElementById("out");
			const svg = out.querySelector("svg");
			if (svg == null) {
				throw new Error("No SVG to copy");
			}

			// Serialize SVG with proper xmlns (required for standalone rendering)
			const clone = svg.cloneNode(true);
			if (clone.getAttribute("xmlns") == null) {
				clone.setAttribute("xmlns", "http://www.w3.org/2000/svg");
			}
			const svgString = new XMLSerializer().serializeToString(clone);
			const svgBlob = new Blob([svgString], {type: "image/svg+xml;charset=utf-8"});
			const url = URL.createObjectURL(svgBlob);

			// Determine target dimensions (account for devicePixelRatio for crisp output)
			const rect = svg.getBoundingClientRect();
			const width = rect.width || svg.viewBox.baseVal.width;
			const height = rect.height || svg.viewBox.baseVal.height;
			const ratio = window.devicePixelRatio || 1;

			// Load SVG into an Image
			const img = new Image();
			img.width = width;
			img.height = height;
			await new Promise((resolve, reject) => {
				img.onload = resolve;
				img.onerror = () => reject(new Error("Image load failed"));
				img.src = url;
			});

			// Draw on canvas with white background
			const canvas = document.createElement("canvas");
			canvas.width = Math.ceil(width * ratio);
			canvas.height = Math.ceil(height * ratio);
			const ctx = canvas.getContext("2d");
			// Use the theme's background color so the PNG looks right when pasted
			const bg = getComputedStyle(document.body).backgroundColor || "white";
			ctx.fillStyle = bg;
			ctx.fillRect(0, 0, canvas.width, canvas.height);
			ctx.scale(ratio, ratio);
			ctx.drawImage(img, 0, 0, width, height);
			URL.revokeObjectURL(url);

			// Convert canvas to PNG blob and copy to clipboard
			const blob = await new Promise((resolve, reject) => {
				canvas.toBlob(b => b == null ? reject(new Error("toBlob failed")) : resolve(b), "image/png");
			});
			await navigator.clipboard.write([new ClipboardItem({"image/png": blob})]);

			copyBitmap.classList.add("success");
			setTimeout(() => (copyBitmap.classList.remove("success")), 300);
		} catch (err) {
			console.error("Copy bitmap failed:", err);
			copyBitmap.classList.add("error");
			setTimeout(() => (copyBitmap.classList.remove("error")), 3000);
		}
	});

	const openSvg = document.getElementById("open-svg");
	openSvg.addEventListener("click", () => openLivePreview("svg", openSvg));

	const openPng = document.getElementById("open-png");
	openPng.addEventListener("click", () => openLivePreview("png", openPng));

	const theme = document.getElementById("theme");
	theme.addEventListener("click", () => {
		dark = !dark;
		document.documentElement.classList.toggle("dark", dark);
		document.documentElement.style.colorScheme = dark ? "dark" : "light";
		renderNow();
	});

	const save = document.getElementById("save");
	save.addEventListener("click", () => {
		const content = getContent();
		const blob = new Blob([content], {type: "image/svg+xml"});
		const url = URL.createObjectURL(blob);
		const a = document.createElement("a");
		a.href = url;
		a.download = "diagram.svg";
		a.click();
		URL.revokeObjectURL(url);
	});

	function getContent() {
		const out = document.getElementById("out");
		const svg = out.querySelector("svg");
		if (svg == null) {
			return out.innerHTML;
		}
		// Serialize via XMLSerializer rather than reading innerHTML.
		// innerHTML uses the HTML serialization algorithm, and Firefox's HTML
		// serializer drops the closing "?" of a processing instruction, turning
		// <?plantuml-src ...?> into <?plantuml-src ...>, which produces invalid
		// XML that browsers and image viewers refuse to open.
		// XMLSerializer always emits a well-formed "?>" in every browser.
		// https://github.com/plantuml/plantuml/issues/2769
		const clone = svg.cloneNode(true);
		if (clone.getAttribute("xmlns") == null) {
			clone.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		}
		return new XMLSerializer().serializeToString(clone);
	}
}

// ---------------------------------------------------------------------------
// Right-click context menu shown over the rendered diagram. Entries simply
// click() the matching toolbar buttons, so all copy logic and visual feedback
// (.success / .error outlines) stay in the controls() handlers above -- no
// duplication and nothing to keep in sync.
// ---------------------------------------------------------------------------
function contextMenu() {
	const out = document.getElementById("out");
	let menuEl = null;

	function close() {
		if (menuEl) {
			menuEl.remove();
			menuEl = null;
			document.removeEventListener("mousedown", onOutsideMouseDown, true);
			document.removeEventListener("keydown",   onKeyDown,           true);
			window.removeEventListener("blur",        close);
			window.removeEventListener("scroll",      close, true);
			window.removeEventListener("resize",      close);
		}
	}

	function onOutsideMouseDown(e) {
		if (menuEl && !menuEl.contains(e.target)) {
			close();
		}
	}

	function onKeyDown(e) {
		if (e.key === "Escape") {
			e.preventDefault();
			close();
		}
	}

	function open(clientX, clientY) {
		close();

		const menu = document.createElement("ul");
		menu.className = "ctx-menu";
		menu.setAttribute("role", "menu");

		// Each entry delegates to the existing toolbar button so the
		// real action, error handling and visual feedback live in one
		// place (the click handlers installed by controls()).
		const ENTRIES = [
			{ label: "Copy as bitmap",       buttonId: "copy-bitmap" },
			{ label: "Copy as SVG",          buttonId: "copy"        },
			{ label: "Open live PNG preview", buttonId: "open-png"    },
			{ label: "Open live SVG preview", buttonId: "open-svg"    }
		];
		for (const entry of ENTRIES) {
			const li = document.createElement("li");
			li.setAttribute("role", "menuitem");
			li.textContent = entry.label;
			li.addEventListener("click", () => {
				const btn = document.getElementById(entry.buttonId);
				if (btn) {
					btn.click();
				} else {
					console.warn("ctx-menu: button not found:", entry.buttonId);
				}
				close();
			});
			menu.appendChild(li);
		}

		// Mount off-screen first to measure, then clamp inside the viewport
		// so the menu doesn't get cut off near the right/bottom edges.
		menu.style.left = "-9999px";
		menu.style.top  = "-9999px";
		document.body.appendChild(menu);
		const rect = menu.getBoundingClientRect();
		const vw   = document.documentElement.clientWidth;
		const vh   = document.documentElement.clientHeight;
		let   x    = clientX;
		let   y    = clientY;
		if (x + rect.width  > vw) x = Math.max(0, vw - rect.width  - 2);
		if (y + rect.height > vh) y = Math.max(0, vh - rect.height - 2);
		menu.style.left = x + "px";
		menu.style.top  = y + "px";

		menuEl = menu;

		document.addEventListener("mousedown", onOutsideMouseDown, true);
		document.addEventListener("keydown",   onKeyDown,           true);
		window.addEventListener("blur",   close);
		window.addEventListener("scroll", close, true);
		window.addEventListener("resize", close);
	}

	// Delegated right-click handler on #out so it keeps working after every
	// re-render (render() rebuilds the SVG on each keystroke).
	out.addEventListener("contextmenu", e => {
		const target = e.target;
		if (!target || (target.nodeName !== "svg" && !target.closest("svg"))) {
			return;
		}
		e.preventDefault();
		open(e.clientX, e.clientY);
	});
}
