S'inspirer de ce qui est fait dans ce projet: https://github.com/plantuml/plantuml-doc

Il faut écrire dans le répertoire /tools/alphadoc-to-vega un programme python qui va récupérer la version "en" d'alphadoc en partant de https://alphadoc.plantuml.com/toc/markdown/en

On récupère dans chaque page les diagrammes plantuml et on les sauve dans /src/test/resources/vega/site en tant que fichier .puml

Par exemple:


---
output: svg
---
@startuml
Alice->Bob : foo
@enduml


Ca ne tournera pas sous github, mais sur la machine de l'utilisateur.
Il sera lancé à la main.

L'idée est d'utiliser tous les diagrammes de la documentation comme exemple pour la non-regression.

Récupère les sources depuis https://github.com/plantuml/plantuml/tree/alphadoc-to-vega et travaille dans ta sandbox.
Tu produiras un .patch avec le(s) fichier(s) python
