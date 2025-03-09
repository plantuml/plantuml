(function (){
    function escapeForCssAttributeSelector(entityName) {
        return entityName
            ?.replaceAll('\\', '\\\\')
            ?.replaceAll('"', '\\"');
    }

	function getEdgesAndDistance1Nodes(startingNode) {
		const nodeName = startingNode.getAttribute("data-entity");
		const escapedNodeName = escapeForCssAttributeSelector(nodeName);
		let results = new Set();

        svg.querySelectorAll(`.link[data-entity-1="${escapedNodeName}"], .link[data-entity-2="${escapedNodeName}"]`).forEach(link => {
            const linkStartNodeName = link.getAttribute("data-entity-1");
            const linkEndNodeName = link.getAttribute("data-entity-2");

            if (linkStartNodeName === nodeName) {
                results.add(svg.querySelector(`[data-entity="${escapeForCssAttributeSelector(linkEndNodeName)}"]`));
                results.add(link);
            } else if (linkEndNodeName === nodeName) {
                results.add(svg.querySelector(`[data-entity="${escapeForCssAttributeSelector(linkStartNodeName)}"]`));
                results.add(link);
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

    function onClickEntity() {
        if (svg.classList.contains("click-active") && this.classList.contains("click-selected")) {
            handleDeselect("click");
        } else {
            handleSelect(this, "click");
        }
    }

	function onMouseOverEntity() {
	    handleSelect(this, "mouseover");
	}

	function onMouseOutEntity() {
	    handleDeselect("mouseover");
	}

    // CSS class descriptions:
    //   xxx-active: highlight styles will be ignored when this is not added to the the parent <svg>.
    //   xxx-selected: lets us track which entity `<g>` was clicked/hovered.
    //   xxx-highlighted: when highlighting is 'active', elements without this class will be dimmed.
    //
    // 'xxx' event type will be 'hover' or 'mouseover'.
	function handleSelect(selectedEntityElem, eventType) {
	    svg.querySelectorAll(`.${eventType}-selected, .${eventType}-highlighted`).forEach(elem => {
            elem.classList.remove(`${eventType}-selected`, `${eventType}-highlighted`);
        });

        svg.classList.add(`${eventType}-active`);
        selectedEntityElem.classList.add(`${eventType}-selected`, `${eventType}-highlighted`);

        getEdgesAndDistance1Nodes(selectedEntityElem).forEach(node => {
            node.classList.add(`${eventType}-highlighted`);
        });
	}

    function handleDeselect(eventType) {
		svg.classList.remove(`${eventType}-active`);
    }

	const svg = findAncestorWithTagName(document.currentScript, "svg");

    document.addEventListener("DOMContentLoaded", () => {
        svg.querySelectorAll("g.entity").forEach(entity => {
            entity.addEventListener("mouseover", onMouseOverEntity);
            entity.addEventListener("mouseout", onMouseOutEntity);
            entity.addEventListener("click", onClickEntity);
        });

        svg.addEventListener("keydown", event => {
            if (event.code === "Escape") {
              handleDeselect("click");
            }
          });
    });
})();