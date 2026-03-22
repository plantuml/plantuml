const editor = document.getElementById("editor");

renderer();
resize();
controls();

function renderer() {
	const loading = document.getElementById("loading");

	try {
		main();

		editor.addEventListener("input", render);
		render();

		loading.style.display = "none";
	} catch (err) {
		console.error("Error", err);
		loading.textContent = "Error: " + err.message;
	}

	function render() {
		const lines = editor.value.split(/\r\n|\r|\n/);
		plantumlRender(lines, "out");
	}
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
