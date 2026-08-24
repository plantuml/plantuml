import { render } from "../plantuml.js";
import { decodePlantUml } from "../plantuml-codec.js";
import { createZoomController } from "../zoom.js";

const params = new URLSearchParams(window.location.search);
const format = params.get("format") === "png" ? "png" : "svg";
let dark = false;
const output = document.getElementById("out");
const pngOutput = document.getElementById("png-output");
const loading = document.getElementById("loading");
const error = document.getElementById("error");
let outputTimer = null;
let pngRevision = 0;
let pngUrl = null;

createZoomController({
	viewport: document.getElementById("preview"),
	zoomOut: document.getElementById("zoom-out"),
	zoomReset: document.getElementById("zoom-reset"),
	zoomIn: document.getElementById("zoom-in"),
	panToggle: document.getElementById("pan-tool")
});

document.title = `PlantUML — Live ${format.toUpperCase()}`;
output.classList.toggle("png-source", format === "png");

new MutationObserver(scheduleOutputUpdate).observe(output, {
	attributes: true,
	characterData: true,
	childList: true,
	subtree: true
});

window.addEventListener("hashchange", renderFromFragment);
renderFromFragment();

function renderFromFragment() {
	updateThemeFromLocation();
	const fragment = window.location.hash.slice(1);
	loading.hidden = false;
	error.hidden = true;
	if (!fragment) {
		showError("This preview URL does not contain a diagram fragment.");
		return;
	}

	try {
		const source = decodePlantUml(fragment);
		render(source.split(/\r\n|\r|\n/), "out", {dark: dark});
		scheduleOutputUpdate();
	} catch (err) {
		console.error("Could not decode live preview URL:", err);
		showError("The diagram fragment in this preview URL is invalid.");
	}
}

function updateThemeFromLocation() {
	dark = new URLSearchParams(window.location.search).get("theme") === "dark";
	document.documentElement.classList.toggle("dark", dark);
	document.documentElement.style.colorScheme = dark ? "dark" : "light";
}

function scheduleOutputUpdate() {
	clearTimeout(outputTimer);
	outputTimer = setTimeout(updateOutput, 0);
}

function updateOutput() {
	outputTimer = null;
	const svg = output.querySelector("svg");
	if (svg == null) {
		return;
	}
	if (format === "svg") {
		loading.hidden = true;
		return;
	}

	const revision = ++pngRevision;
	createPngBlob(svg).then(
		blob => {
			if (revision !== pngRevision) {
				return;
			}
			const nextUrl = URL.createObjectURL(blob);
			const previousUrl = pngUrl;
			pngUrl = nextUrl;
			pngOutput.onload = () => {
				if (previousUrl != null) {
					URL.revokeObjectURL(previousUrl);
				}
				loading.hidden = true;
				pngOutput.hidden = false;
			};
			pngOutput.src = nextUrl;
		},
		err => {
			console.error("Could not create PNG preview:", err);
			if (revision === pngRevision) {
				showError("The PNG preview could not be created.");
			}
		}
	);
}

function serializeSvg(svg) {
	const clone = svg.cloneNode(true);
	if (clone.getAttribute("xmlns") == null) {
		clone.setAttribute("xmlns", "http://www.w3.org/2000/svg");
	}
	return new XMLSerializer().serializeToString(clone);
}

async function createPngBlob(svg) {
	const svgBlob = new Blob([serializeSvg(svg)], {type: "image/svg+xml;charset=utf-8"});
	const svgUrl = URL.createObjectURL(svgBlob);
	try {
		const image = new Image();
		await new Promise((resolve, reject) => {
			image.onload = resolve;
			image.onerror = () => reject(new Error("SVG image load failed"));
			image.src = svgUrl;
		});

		const viewBox = svg.viewBox.baseVal;
		const width = viewBox.width || image.naturalWidth || 800;
		const height = viewBox.height || image.naturalHeight || 600;
		const ratio = Math.max(1, window.devicePixelRatio || 1);
		const canvas = document.createElement("canvas");
		canvas.width = Math.max(1, Math.ceil(width * ratio));
		canvas.height = Math.max(1, Math.ceil(height * ratio));
		const context = canvas.getContext("2d");
		if (context == null) {
			throw new Error("Canvas is unavailable");
		}

		context.fillStyle = getComputedStyle(document.body).backgroundColor || "white";
		context.fillRect(0, 0, canvas.width, canvas.height);
		context.scale(ratio, ratio);
		context.drawImage(image, 0, 0, width, height);
		return await new Promise((resolve, reject) => {
			canvas.toBlob(
				blob => blob == null ? reject(new Error("PNG conversion failed")) : resolve(blob),
				"image/png"
			);
		});
	} finally {
		URL.revokeObjectURL(svgUrl);
	}
}

function showError(message) {
	loading.hidden = true;
	error.textContent = message;
	error.hidden = false;
}
