function addItemToMapOfLists(mapOfLists, name, item) {
  // mapOfLists = {
  //   'key1': [item1, item2, ...],
  //   'key2': [item3, item4, ...],
  // }
  if (mapOfLists[name].length > 0) {
    if (!mapOfLists[name].includes(item)) {
      mapOfLists[name].push(item);
    }
  } else {
    mapOfLists[name] = [item];
  }
}

function main() {
  let elems = Array.from(document.getElementsByClassName('elem'));
  let links = Array.from(document.getElementsByClassName('link'));

  let elemsMap = {};
  let linkedFromElems = {};
  let linkedToElems = {};
  let linkedFromLinks = {};
  let linkedToLinks = {};

  elems.forEach(elem => {
    let name = elem.classList[1];
    elemsMap[name] = elem;
    linkedFromElems[name] = [];
    linkedToElems[name] = [];
    linkedFromLinks[name] = [];
    linkedToLinks[name] = [];
  });

  links.forEach(link => {
    let fromName = link.classList[1];
    let toName = link.classList[2];

    if (elemsMap[fromName] && elemsMap[toName]) {
      let fromElem = elemsMap[fromName];
      let toElem = elemsMap[toName];

      addItemToMapOfLists(linkedFromElems, toName, fromElem);
      addItemToMapOfLists(linkedToElems, fromName, toElem);

      addItemToMapOfLists(linkedFromLinks, toName, link);
      addItemToMapOfLists(linkedToLinks, fromName, link);
    }
  });

  let selectedElems = [];
  let selectedLinks = [];
  let selectedElemName = null;

  function clearSelected() {
    selectedElems.forEach(item => {
      item.classList.remove('selected');
    });
    selectedElems = [];

    selectedLinks.forEach(item => {
      item.classList.remove('selected');
    });
    selectedLinks = [];
  }

  function selectAll() {
    selectedElemName = null;
    clearSelected();

    selectedElems = Array.from(elems);
    selectedElems.forEach(item => {
      item.classList.add('selected');
    });

    selectedLinks = Array.from(links);
    selectedLinks.forEach(item => {
      item.classList.add('selected');
    });
  }

  function selectElem(elemName) {
    if (elemName === selectedElemName) {
      selectAll();
    } else {
      clearSelected();
      selectedElemName = elemName;

      elemsMap[elemName].classList.add('selected');
      selectedElems.push(elemsMap[elemName]);

      linkedFromElems[elemName].forEach(linkedElem => {
        // Avoid repetitive processing
        if (selectedElems.includes(linkedElem)) {
          return;
        }
        selectedElems.push(linkedElem);
        linkedElem.classList.add('selected');
      });
      linkedToElems[elemName].forEach(linkedElem => {
        // Avoid repetitive processing
        if (selectedElems.includes(linkedElem)) {
          return;
        }
        selectedElems.push(linkedElem);
        linkedElem.classList.add('selected');
      });

      linkedFromLinks[elemName].forEach(linkedLink => {
        // Avoid repetitive processing
        if (selectedLinks.includes(linkedLink)) {
          return;
        }
        selectedLinks.push(linkedLink);
        linkedLink.classList.add('selected');
      });
      linkedToLinks[elemName].forEach(linkedLink => {
        // Avoid repetitive processing
        if (selectedLinks.includes(linkedLink)) {
          return;
        }
        selectedLinks.push(linkedLink);
        linkedLink.classList.add('selected');
      });
    }
  }

  function selectElemOfLine(elemName) {
    clearSelected();
    selectedElemName = elemName;

    elemsMap[elemName].classList.add('selected');
    selectedElems.push(elemsMap[elemName]);

    selectFromElem(elemName);
    selectToElem(elemName);
  }

  function selectFromElem(elemName) {
    console.log(elemName, "linkedFromLinks", linkedFromLinks[elemName])
    linkedFromLinks[elemName].forEach(linkedLink => {
      // Avoid repetitive processing
      if (selectedLinks.includes(linkedLink)) {
        return;
      }
      selectedLinks.push(linkedLink);
      linkedLink.classList.add('selected');
      console.log(elemName, "from link", linkedLink);
    });

    console.log(elemName, "linkedFromElems", linkedFromElems[elemName])
    linkedFromElems[elemName].forEach(linkedElem => {
      // break loop reference
      if (selectedElems.includes(linkedElem)) {
        return;
      }
      selectedElems.push(linkedElem);
      linkedElem.classList.add('selected');
      console.log(elemName, "from elem", linkedElem);

      let nextName = linkedElem.classList[1];
      selectFromElem(nextName);
    });
  }

  function selectToElem(elemName) {
    console.log(elemName, "linkedToLinks", linkedToLinks[elemName]);
    linkedToLinks[elemName].forEach(linkedLink => {
      // Avoid repetitive processing
      if (selectedLinks.includes(linkedLink)) {
        return;
      }
      selectedLinks.push(linkedLink);
      linkedLink.classList.add('selected');
      console.log(elemName, "to link", linkedLink);
    });

    console.log(elemName, "linkedToElems", linkedToElems[elemName])
    linkedToElems[elemName].forEach(linkedElem => {
      // break loop reference
      if (selectedElems.includes(linkedElem)) {
        return;
      }
      selectedElems.push(linkedElem);
      linkedElem.classList.add('selected');
      console.log(elemName, "to elem", linkedElem);

      let nextName = linkedElem.classList[1];
      selectToElem(nextName);
    });
  }

  Object.keys(elemsMap).forEach(name => {
    elemsMap[name].onclick = (event) => {
      console.log("onclick: ", event);
      selectElem(name);
    };
    // double click, and then selectElemOfLine
    elemsMap[name].ondblclick = (event) => {
      console.log("ondblclick", event);
      selectElemOfLine(name);
    };
  });

  selectAll();

  document.querySelector('svg').addEventListener('keydown', event => {
    console.log('svg keydown: ', event.key);
    // Press Escape, and then selectAll
    // https://www.freecodecamp.org/news/javascript-keycode-list-keypress-event-key-codes/
    if (event.code === "Escape") {
      selectAll();
    }
  });
}

document.addEventListener('DOMContentLoaded', (event) => {
  main();
});
