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
  let linkedElems = {};
  let linkedLinks = {};

  elems.forEach(elem => {
    let name = elem.classList[1];
    elemsMap[name] = elem;
    linkedElems[name] = [];
    linkedLinks[name] = [];
  });

  links.forEach(link => {
    let name1 = link.classList[1];
    let name2 = link.classList[2];

    if (elemsMap[name1]) {
      if (elemsMap[name2]) {
        let elem1 = elemsMap[name1];
        let elem2 = elemsMap[name2];

        addItemToMapOfLists(linkedElems, name1, elem2);
        addItemToMapOfLists(linkedElems, name2, elem1);

        addItemToMapOfLists(linkedLinks, name1, link);
        addItemToMapOfLists(linkedLinks, name2, link);
      }
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

      linkedElems[elemName].forEach(linkedElem => {
        selectedElems.push(linkedElem);
        linkedElem.classList.add('selected');
      });

      linkedLinks[elemName].forEach(linkedLink => {
        selectedLinks.push(linkedLink);
        linkedLink.classList.add('selected');
      });
    }
  }

  Object.keys(elemsMap).forEach(name => {
    elemsMap[name].onclick = () => { selectElem(name); };
  });

  selectAll();
}

document.addEventListener('DOMContentLoaded', (event) => {
  main();
});
