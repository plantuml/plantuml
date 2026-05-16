import { render } from "./plantuml.js";

const editor = document.getElementById("editor");
let dark = false;

renderer();
resize();
controls();

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
		navigator.clipboard.writeText(content).then(
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
		return out.innerHTML;
	}
}
