(function() {
	const SVG_NS = "http://www.w3.org/2000/svg";

	function findAncestorWithTagName(elem, tagName) {
		while (elem && elem.nodeName.toLowerCase() !== tagName) {
			elem = elem.parentElement;
		}
		return elem;
	}

	function isScrollable(elem) {
		const overflowY = getComputedStyle(elem).overflowY;
		return (overflowY === "auto" || overflowY === "scroll") && elem.scrollHeight > elem.clientHeight
	}

	function createGroupedHeader(svgElement) {
		const floatingHeaderGroup = document.createElementNS(SVG_NS, "g");
		floatingHeaderGroup.classList.add("sequence-diagram-header");
		
		svgElement.querySelector("g").appendChild(floatingHeaderGroup);
		
		svgElement.querySelectorAll("g.participant-head").forEach(participant => {
			floatingHeaderGroup.appendChild(participant);
		});
		
		const headerBounds = floatingHeaderGroup.getBBox();
		const background = document.createElementNS(SVG_NS, "rect")
		background.classList.add("header-background");
		background.setAttribute("x", "0");
		background.setAttribute("y", "0");
		background.setAttribute("width", `${svgElement.getBBox().width}`);
		background.setAttribute("height", `${headerBounds.y + headerBounds.height}`); 
		background.setAttribute("fill", svgElement.style.backgroundColor);
		
		floatingHeaderGroup.insertAdjacentElement("afterbegin", background);
		return floatingHeaderGroup;
	}

	function ancestorsMaxClientY(svgElement) {
		let currentMax = 0;
		let parent = svgElement.parentElement;

		while (parent) {
			currentMax = Math.max(parent.getBoundingClientRect().y, currentMax);
			parent = parent.parentElement;
		}

		return currentMax;
	}

	const updateFloatingHeaderPosition = (svgElement, floatingHeaderElement) => {
		const svgTop = svgElement.getBoundingClientRect().y;
		const ancestorsMaxTop = ancestorsMaxClientY(svgElement);
		const amountOfOverflow = Math.floor(Math.max(0, ancestorsMaxTop - svgTop));

		floatingHeaderElement.setAttribute("transform", `translate(0, ${amountOfOverflow})`);
		floatingHeaderElement.classList.toggle("floating", amountOfOverflow > 0);
	}

	function init(svgElement) {
		if (window) {
			const floatingHeaderElement = createGroupedHeader(svgElement)

			window.addEventListener("scroll", () => {
				updateFloatingHeaderPosition(svgElement, floatingHeaderElement);
			});

			let parentElement = svgElement;

			while (parentElement != null) {
				if (isScrollable(parentElement)) {
					parentElement.addEventListener("scroll", () => {
						updateFloatingHeaderPosition(svgElement, floatingHeaderElement);
					});
				}
				parentElement = parentElement.parentElement;
			}
		}
	}

	const svgRoot = findAncestorWithTagName(document.currentScript, "svg");

	document.addEventListener("DOMContentLoaded", () => {
		init(svgRoot);
	});
})();