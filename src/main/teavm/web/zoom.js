const MIN_ZOOM = 0.25;
const MAX_ZOOM = 4;
const BUTTON_STEP = 0.25;
const WHEEL_STEP = 0.05;

export function createZoomController({viewport, zoomOut, zoomReset, zoomIn, panToggle}) {
	let zoom = 1;
	let panEnabled = false;
	let panning = null;
	const platform = navigator.userAgentData?.platform || navigator.platform;
	const platformKey = /Mac|iPhone|iPad|iPod/i.test(platform)
		? "Command"
		: /Win/i.test(platform) ? "Windows" : "Meta";

	zoomReset.title = `Reset zoom; use ${platformKey}+wheel over the diagram to zoom`;
	panToggle.title = `Pan tool; drag or hold ${platformKey} while dragging`;

	function updateControls() {
		const percentage = Math.round(zoom * 100);
		zoomReset.textContent = `${percentage}%`;
		zoomReset.setAttribute("aria-label", `Reset zoom (currently ${percentage}%)`);
		zoomOut.disabled = zoom <= MIN_ZOOM;
		zoomIn.disabled = zoom >= MAX_ZOOM;
	}

	function setZoom(nextZoom, clientX, clientY) {
		nextZoom = Math.min(MAX_ZOOM, Math.max(MIN_ZOOM, nextZoom));
		nextZoom = Math.round(nextZoom * 100) / 100;
		if (nextZoom === zoom) {
			return;
		}

		const rect = viewport.getBoundingClientRect();
		const offsetX = clientX == null ? viewport.clientWidth / 2 : clientX - rect.left;
		const offsetY = clientY == null ? viewport.clientHeight / 2 : clientY - rect.top;
		const contentX = viewport.scrollLeft + offsetX;
		const contentY = viewport.scrollTop + offsetY;
		const ratio = nextZoom / zoom;

		zoom = nextZoom;
		viewport.style.setProperty("--preview-zoom", zoom);
		// Force the new scaled dimensions to be calculated before restoring the
		// point under the cursor.
		void viewport.scrollWidth;
		viewport.scrollLeft = contentX * ratio - offsetX;
		viewport.scrollTop = contentY * ratio - offsetY;
		updateControls();
	}

	function platformPanActive(event = {}) {
		return Boolean(event.metaKey);
	}

	function updatePanCursor(event) {
		viewport.classList.toggle("pan-ready", panEnabled || platformPanActive(event));
		panToggle.classList.toggle("active", panEnabled);
		panToggle.setAttribute("aria-pressed", String(panEnabled));
	}

	function stopPanning(event) {
		if (panning == null || (event?.pointerId != null && event.pointerId !== panning.pointerId)) {
			return;
		}
		panning = null;
		viewport.classList.remove("panning");
		updatePanCursor(event);
	}

	zoomOut.addEventListener("click", () => setZoom(zoom - BUTTON_STEP));
	zoomReset.addEventListener("click", () => setZoom(1));
	zoomIn.addEventListener("click", () => setZoom(zoom + BUTTON_STEP));
	panToggle.addEventListener("click", () => {
		panEnabled = !panEnabled;
		updatePanCursor();
	});
	viewport.addEventListener("wheel", event => {
		if (event.deltaY === 0 || !event.metaKey) {
			return;
		}
		event.preventDefault();
		const direction = event.deltaY < 0 ? 1 : -1;
		setZoom(zoom + direction * WHEEL_STEP, event.clientX, event.clientY);
	}, {passive: false});
	viewport.addEventListener("pointerdown", event => {
		if (event.target instanceof Element && event.target.closest(".zoom-controls")) {
			return;
		}
		if (event.button !== 0 || (!panEnabled && !platformPanActive(event))) {
			return;
		}

		event.preventDefault();
		panning = {
			pointerId: event.pointerId,
			clientX: event.clientX,
			clientY: event.clientY,
			scrollLeft: viewport.scrollLeft,
			scrollTop: viewport.scrollTop
		};
		viewport.setPointerCapture(event.pointerId);
		viewport.classList.add("panning");
	});
	viewport.addEventListener("pointermove", event => {
		if (panning == null || event.pointerId !== panning.pointerId) {
			return;
		}
		viewport.scrollLeft = panning.scrollLeft - (event.clientX - panning.clientX);
		viewport.scrollTop = panning.scrollTop - (event.clientY - panning.clientY);
	});
	viewport.addEventListener("pointerup", stopPanning);
	viewport.addEventListener("pointercancel", stopPanning);
	viewport.addEventListener("lostpointercapture", stopPanning);
	window.addEventListener("keydown", event => {
		if (event.key === "Meta" || event.key === "OS") {
			updatePanCursor(event);
		}
	});
	window.addEventListener("keyup", event => {
		if (event.key === "Meta" || event.key === "OS") {
			updatePanCursor(event);
		}
	});
	window.addEventListener("blur", () => {
		stopPanning();
		updatePanCursor();
	});

	viewport.style.setProperty("--preview-zoom", zoom);
	updateControls();
	updatePanCursor();

	return {
		get value() {
			return zoom;
		},
		reset() {
			setZoom(1);
		}
	};
}
