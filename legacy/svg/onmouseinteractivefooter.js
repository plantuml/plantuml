(function (){
	/**
	 * @param {SVG.G} node
	 * @param {SVG.G} topG
	 * @return {{node: Set<SVG.G>, edges:Set<SVG.G>}}
	 */
	function getEdgesAndDistance1Nodes(node, topG) {
		const nodeName = node.attr("id").match(/elem_(.+)/)[1];
		const selector = "[id^=link_]"
		const candidates = topG.find(selector)
		let edges = new Set();
		let nodes = new Set();
		for (let link of candidates) {
			const res = link.attr("id").match(/link_([A-Za-z\d]+)_([A-Za-z\d]+)/);
			if (res && res.length==3) {
				const N1 = res[1];
				const N2 = res[2];
				if (N1==nodeName) {
					const N2selector = `[id^=elem_${N2}]`;
					nodes.add(topG.findOne(N2selector));
					edges.add(link);
				} else if (N2==nodeName) {
					const N1selector = `[id^=elem_${N1}]`;
					nodes.add(topG.findOne(N1selector));
					edges.add(link);
				}
			}
		}
		return {
			"nodes" : nodes,
			"edges" : edges
		};
	}

	/**
	 * @param {SVG.G} node
	 * @param {function(SVG.Dom)}
	 * @return {{node: Set<SVG.G>, edges:Set<SVG.G>}}
	 */
	function walk(node, func) {
		let children = node.children();
		for (let child of children) {
			walk(child, func)
		}
		func(node);
	}
	let s = SVG("svg > g")
	/**
	 * @param {SVGElement} domEl
	 * @return {{SVGElement}}
	 */
	function findEnclosingG(domEl) {
		let curEl = domEl;
		while (curEl.nodeName != "g") {
			curEl = curEl.parentElement;
		}
		return curEl;
	}
	function onMouseOverElem(domEl) {
		let e = SVG(findEnclosingG(domEl.target));
		walk(s,
			e => { if (SVG(e)!=s)
				SVG(e).attr('data-mouse-over-selected',"false");
			});
		walk(e, e => SVG(e).attr('data-mouse-over-selected',"true"));
		let {nodes, edges} = getEdgesAndDistance1Nodes(SVG(e), s);
		for (let node of nodes) {
			walk(node, e => SVG(e).attr('data-mouse-over-selected',"true"));
		}
		for (let edge of edges) {
			walk(edge, e => SVG(e).attr('data-mouse-over-selected',"true"));
		}
	}

	function onMouseOutElem(domEl) {
		let e = SVG(findEnclosingG(domEl.target));
		walk(s, e => e.attr('data-mouse-over-selected',null));
	}
	let gs = s.find("g[id^=elem_]");
	for (let g of gs) {
		g.on("mouseover", onMouseOverElem);
		g.on("mouseout", onMouseOutElem);
	}
})();
