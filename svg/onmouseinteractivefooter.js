(function (){
	/**
	 * @param {SVGElement} node
	 * @param {SVGElement} topG
	 * @return {Set<SVGElement>}
	 */
	function getEdgesAndDistance1Nodes(node, topG) {
		const nodeName = node.id.match(/elem_(.+)/)[1];
		let results = new Set();

		topG.querySelectorAll(`.link.${nodeName}`).forEach(link => {
			const matchResult = link.id.match(/link_([A-Za-z\d]+)_([A-Za-z\d]+)/);
			if (matchResult?.length === 3) {
				const linkStartNodeName = matchResult[1];
				const linkEndNodeName = matchResult[2];

				if (linkStartNodeName === nodeName) {
					results.add(topG.querySelector(`#elem_${linkEndNodeName}`));
					results.add(link);
				} else if (linkEndNodeName === nodeName) {
					results.add(topG.querySelector(`#elem_${linkStartNodeName}`));
					results.add(link);
				}
			}
		});

		return results;
	}

	/**
	 * @param {SVGElement} elem
	 * @param tagName in lowercase, e.g. "g" or "svg"
	 * @return {{SVGElement}} or null if no matching ancestor is found
	 */
	function findAncestorWithTagName(elem, tagName) {
		while (elem && elem.nodeName.toLowerCase() !== tagName) {
			elem = elem.parentElement;
		}
		return elem;
	}

	function findEnclosingG(elem) {
		return findAncestorWithTagName(elem, "g");
	}

	function onMouseOverElem() {
		topG.querySelectorAll("[data-mouse-over-selected]").forEach(elem => {
			elem.removeAttribute("data-mouse-over-selected");
		})

		let hoveredElem = findEnclosingG(this);

		topG.setAttribute("data-mouse-over-active", "true");
		hoveredElem.setAttribute("data-mouse-over-selected", "true");

		getEdgesAndDistance1Nodes(hoveredElem, topG).forEach(node => {
			node.setAttribute("data-mouse-over-selected", "true");
		});
	}

	function onMouseOutElem() {
		topG.removeAttribute("data-mouse-over-active");
	}


	const topG = findAncestorWithTagName(document.currentScript, "svg").querySelector("svg > g");

	topG.querySelectorAll("g.elem").forEach(g => {
		g.addEventListener("mouseover", onMouseOverElem);
		g.addEventListener("mouseout", onMouseOutElem);
	});

})();
