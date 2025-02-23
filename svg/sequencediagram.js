(function() {
	const SVG_NS = "http://www.w3.org/2000/svg";
    const LOCAL_STORAGE_FLOATING_HEADER_ACTIVE = "net.sourceforge.plantuml.sequence-diagram.floating-header.active";

    function toggleFloatingHeader() {
        try {
            const shouldNowBeActive = ! isFloatingHeaderActive();
            svgRoot.classList.toggle("floating-header-active", shouldNowBeActive);
            window.localStorage?.setItem(LOCAL_STORAGE_FLOATING_HEADER_ACTIVE, `${shouldNowBeActive}`);

            if (shouldNowBeActive) {
                updateFloatingHeaderPosition(svgRoot.querySelector("g.floating-header"));
            }
        } catch (e) {
            console.error("Error while toggling floating header:", e, svgRoot);
            disableFloatingHeaderDueToError();
        }
    }

    function isFloatingHeaderActive() {
        return svgRoot.classList.contains("floating-header-active");
    }

    function disableFloatingHeaderDueToError() {
        try {
            svgRoot.classList.remove("floating-header-active");
            svgRoot.classList.add("floating-header-error");
        } catch(e) {
            console.error("Failed to disable floating header:", e, svgRoot);
        }
    }

	function findAncestorWithTagName(elem, tagName) {
		while (elem && elem.nodeName.toLowerCase() !== tagName) {
			elem = elem.parentElement;
		}
		return elem;
	}

	function groupParticipantHeaders() {
	    const group = document.createElementNS(SVG_NS, "g");
        group.classList.add("header");

	    svgRoot.querySelectorAll("g.participant-head").forEach(participant => {
	        group.appendChild(participant);
	    });

	    svgRoot.querySelector("g").appendChild(group);

	    // Add a background rect, as a hit target
		const headerBounds = group.getBBox();
		const background = document.createElementNS(SVG_NS, "rect")
		background.classList.add("header-background");
		background.setAttribute("x", "0");
		background.setAttribute("y", "0");
		background.setAttribute("width", `${svgRoot.getBBox().width}`);
		background.setAttribute("height", `${headerBounds.y + headerBounds.height + 10}`);
		background.setAttribute("fill", svgRoot.style.backgroundColor);

		group.insertAdjacentElement("afterbegin", background);

	    return group;
	}

	function isScrollableContainer(elem) {
		const overflowY = getComputedStyle(elem).overflowY;
		return (overflowY === "auto" || overflowY === "scroll") && elem.scrollHeight > elem.clientHeight
	}

    function createFloatingHeaderToggleButton(header) {
        const buttonGroup = document.createElementNS(SVG_NS, "g");

        buttonGroup.classList.add("floating-header-toggle-button");
        buttonGroup.innerHTML = `
            <title>Pin the header while scrolling</title>
            <rect class="button-background" fill="#FFFFFF" width="17" height="17" rx="1" ry="1" x="3" y="2" style="stroke:black;stroke-width:0.5;"/>
            <path class="button-icon" fill="#efefef" style="stroke:black;stroke-width:0.5" d="M13 3.5 a.5.5 0 0 1 .354.146l4.95 4.95a.5.5 0 0 1 0 .707c-.48.48-1.072.588-1.503.588-.177 0-.335-.018-.46-.039l-3.134 3.134a5.927 5.927 0 0 1 .16 1.013c.046.702-.032 1.687-.72 2.375a.5.5 0 0 1-.707 0l-2.829-2.828-3.182 3.182c-.195.195-1.219.902-1.414.707-.195-.195.512-1.22.707-1.414l3.182-3.182-2.828-2.829a.5.5 0 0 1 0-.707c.688-.688 1.673-.767 2.375-.72a5.922 5.922 0 0 1 1.013.16l3.134-3.133a2.772 2.772 0 0 1-.04-.461c0-.43.108-1.022.589-1.503a.5.5 0 0 1 .353-.146z"/>
        `;

        header.appendChild(buttonGroup);
        return buttonGroup;
    }

	function createFloatingHeader(originalHeader) {

	    const floatingHeaderGroup = originalHeader.cloneNode(true);
		floatingHeaderGroup.classList.add("floating-header");

		svgRoot.querySelector("g").appendChild(floatingHeaderGroup);
		return floatingHeaderGroup;
	}

	function ancestorsMaxClientY(startElement) {
		let currentMax = 0;
		let parent = startElement.parentElement;

		while (parent) {
			currentMax = Math.max(parent.getBoundingClientRect().y, currentMax);
			parent = parent.parentElement;
		}

		return currentMax;
	}

	function updateFloatingHeaderPosition(floatingHeaderElement) {
	    try {
	        if (!isFloatingHeaderActive()) {
	            return;
	        }

            const svgTop = svgRoot.getBoundingClientRect().y;
            const ancestorsMaxTop = ancestorsMaxClientY(svgRoot);
            const amountOfOverflow = Math.floor(Math.max(0, ancestorsMaxTop - svgTop));

            floatingHeaderElement.setAttribute("transform", `translate(0, ${amountOfOverflow})`);
            floatingHeaderElement.classList.toggle("floating", amountOfOverflow > 0);
	    } catch(e) {
	        console.error("Error while updating floating header position:", e, svgRoot);
	        disableFloatingHeaderDueToError();
	    }
	}

	function initFloatingHeader() {
	    try {
	        const header = groupParticipantHeaders()
            const toggleButton = createFloatingHeaderToggleButton(header);
            const floatingHeaderElement = createFloatingHeader(header)

            svgRoot.querySelectorAll("g.floating-header-toggle-button").forEach(button => {
                button.addEventListener("click", toggleFloatingHeader);
            });

            window.addEventListener("scroll", () => {
                updateFloatingHeaderPosition(floatingHeaderElement);
            });

            let parentElement = svgRoot;

            while (parentElement != null) {
                if (isScrollableContainer(parentElement)) {
                    parentElement.addEventListener("scroll", () => {
                        updateFloatingHeaderPosition(floatingHeaderElement);
                    });
                }
                parentElement = parentElement.parentElement;
            }

            const isFloatingHeaderActive = window.localStorage?.getItem(LOCAL_STORAGE_FLOATING_HEADER_ACTIVE) === "true";
            svgRoot.classList.toggle("floating-header-active", isFloatingHeaderActive);
            console.log("In accordance with local storage, setting floating header active = ", isFloatingHeaderActive);

        } catch(e) {
            console.error("Error while initialising floating header:", e, svgRoot);
            disableFloatingHeaderDueToError();
        }
	}

    function handleParticipantFilterClick(clickedParticipantElem) {
        const clickedParticipantName = clickedParticipantElem.getAttribute("data-participant");

        const allFilteredParticipantsNames = new Set(Array
                .from(svgRoot.querySelectorAll("g.participant.filter-highlight"))
                .map(elem => elem.getAttribute("data-participant")));

        if (allFilteredParticipantsNames.has(clickedParticipantName)) {
            allFilteredParticipantsNames.delete(clickedParticipantName);
        } else {
            allFilteredParticipantsNames.add(clickedParticipantName);
        }

        svgRoot.querySelectorAll("g.participant").forEach(participantElem => {
            const shouldHighlight = allFilteredParticipantsNames.has(participantElem.getAttribute("data-participant"));
            participantElem.classList.toggle("filter-highlight", shouldHighlight);
        });

        svgRoot.querySelectorAll("g.message").forEach(messageElem => {
            const participant1 = messageElem.getAttribute("data-participant-1");
            const participant2 = messageElem.getAttribute("data-participant-2");

            const participant1Matches = allFilteredParticipantsNames.has(participant1);
            const participant2Matches = allFilteredParticipantsNames.has(participant2);

            const shouldHighlight = (allFilteredParticipantsNames.size === 1)
                ? (participant1Matches || participant2Matches)
                : (participant1Matches && participant2Matches && participant1 !== participant2);

            messageElem.classList.toggle("filter-highlight", shouldHighlight);
        });

        if (allFilteredParticipantsNames.size === 0) {
            svgRoot.classList.remove("filter-active", "filter-nomatch");
        } else {
            svgRoot.classList.add("filter-active");
            if (svgRoot.querySelector("g.message.filter-highlight")) {
                svgRoot.classList.remove("filter-nomatch");
            } else {
                svgRoot.classList.add("filter-nomatch");
            }
        }
    }

	function initParticipantFiltering() {
	    svgRoot.querySelectorAll(".participant").forEach(participantElem => {
	        participantElem.addEventListener("click", () => {
	            handleParticipantFilterClick(participantElem);
	        });
	    });

	    svgRoot.querySelector("svg > defs").innerHTML += `
            <filter id="colorize-green">
              <feColorMatrix in="SourceGraphic" type="matrix" values="0 0 0 0 0
                                                                      0.2 0.2 0.2 0.2 0
                                                                      0 0 0 0 0
                                                                      0 0 0 1 0" />
            </filter>
            <filter id="colorize-red">
              <feColorMatrix in="SourceGraphic" type="matrix" values="0.2 0.2 0.2 0.2 0
                                                                      0 0 0 0 0
                                                                      0 0 0 0 0
                                                                      0 0 0 1 0" />
            </filter>`;
	}

	const svgRoot = findAncestorWithTagName(document.currentScript, "svg");

	document.addEventListener("DOMContentLoaded", () => {
		initFloatingHeader();
		initParticipantFiltering();
	});
})();