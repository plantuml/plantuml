# PLAN-NGDOT : dessiner les diagrammes `dot` sans l'exécutable Graphviz

## 1. Objectif

Aujourd'hui, un diagramme écrit directement en langage DOT

```
@startdot
digraph G {
  a -> b -> c;
  a -> c [label="raccourci"];
}
@enddot
```

est envoyé tel quel à l'exécutable `dot` (`directdot.PSystemDot`), qui produit lui-même
l'image. Cela pose plusieurs problèmes :

- dépendance à une installation de Graphviz (absente, mauvaise version, crash, timeout) ;
- inutilisable en TeaVM (JS / WASM / C natif) : il n'y a pas de processus externe ;
- le SVG produit par `dot` est recopié sans contrôle : c'est pourquoi il est désactivé
  par certains profils de sécurité (issue #2495) ;
- aucun lien avec le reste de PlantUML : ni thème, ni `skinparam`, ni dark mode,
  ni sorties propres à PlantUML (PNG via notre moteur, `debug`, etc.).

Le but est de remplacer cette chaîne par une chaîne 100 % Java :

```
texte DOT ──► parseur DOT ──► modèle (graphe + attributs) ──► Smetana (layout)
                                                                    │
                    image (svg/png/...) ◄── UGraphic ◄── géométrie ─┘
```

On vise à **reproduire les mêmes diagrammes que `dot` pour les fonctionnalités
majeures**, pas une compatibilité pixel-perfect ni la totalité du langage.
Ce qui n'est pas géré doit se dégrader proprement (attribut ignoré, forme de
remplacement, avertissement), jamais faire planter le rendu.

Point de départ : les commits `0eaf541` et `cc3d667` ont créé
`PSystemNgDot` / `PSystemNgDotFactory` et les ont branchés dans
`PSystemBuilder` (ainsi que dans les builders TeaVM JS et C) à la place de
`PSystemDotFactory`.

### Décisions prises

1. **Migration en douceur : l'ancien rendu par l'exécutable `dot` est conservé
   pour l'usage normal en Java**, ng-dot est utilisé par TeaVM (où l'exécutable
   n'existe pas) et par les tests Vega. L'aiguillage est fait, diagramme par
   diagramme, par `directdot.PSystemDotSelectorFactory` (inscrit dans
   `PSystemBuilder`) :
   - `TitledDiagram.FORCE_SMETANA` vrai (mis par `VegaTest`, et aussi par
     `nonreg.BasicTest` et `SvgTest`) → `ngdot.PSystemNgDotFactory` ;
   - sinon → `directdot.PSystemDotFactory`, l'ancien comportement à l'identique
     (en-tête reconnu par la regex, texte brut envoyé à `dot`, profil de sécurité
     `allowDotSvg()` pour le SVG).
   `PSystemBuilderJs` et `PSystemBuilderC` inscrivent directement
   `PSystemNgDotFactory`, pour que rien de `directdot` (lancement d'un processus)
   n'entre dans la compilation TeaVM. La bascule de l'usage normal vers ng-dot
   sera décidée plus tard (phase 6).
2. **Apparence : thème PlantUML.** Les couleurs, polices et traits par défaut
   viennent du style PlantUML ; un attribut DOT explicite (`color`, `fontname`…)
   l'emporte.
3. **Nouveau package `net.sourceforge.plantuml.ngdot`.**
4. **Le préprocesseur reste actif dans `@startdot`, Python compris** (`!$var`,
   `!include`, `!for x in ...:`, `!{expr}`…). C'est déjà le cas sans rien faire :
   le texte DOT reçu par la factory est le texte préprocessé.

## 2. Ce que Smetana sait faire (et ne sait pas faire)

Constaté dans le code (`gen.*`, `smetana.core.Globals`) :

| Sujet | État |
|---|---|
| Graphe orienté | oui (`zz.Agdirected`). Pas d'`Agundirected` : un `graph` non orienté sera mis en page comme un `digraph` avec `dir=none`, ce que fait `dot` en pratique pour le layout. |
| Formes | seulement `box`, `ellipse`, `record` dans `Globals.Shapes`. `Mrecord` n'est pas porté (`UNSUPPORTED`). |
| Mesure du texte | **aucune** : `textspan_size` est un bouchon, `storeline` « triche ». PlantUML contourne cela en passant des dimensions précalculées (`width`/`height` sur les nœuds, `Macro.createHackInitDimensionFromLabel` pour les labels d'arêtes et de clusters). |
| Clusters | oui (`subgraph cluster_*`), utilisés par `CucaDiagramFileMakerSmetana`. |
| Labels d'arêtes | `label`, `taillabel`, `headlabel` via la même astuce de dimension. |
| Attributs déjà utilisés en production | `rankdir=LR`, `nodesep`, `ranksep`, `margin`, `minlen`, `constraint=false`, `arrowhead/arrowtail`, `arrowsize`. |
| À vérifier | levé par la phase 0, voir §2.1. |
| Rendu | Smetana ne dessine rien : on récupère les coordonnées (`ST_Agnodeinfo_t.coord`, `width`, `height`), les splines (`ST_Agedgeinfo_t.spl`), les labels (`ST_textlabel_t.pos`) et la boîte des clusters (`ST_Agraphinfo_t.bb`). Le code de lecture existe déjà : `sdot.BoxInfo`, `sdot.SmetanaEdge`, `sdot.YMirror`, `gitlog.SmetanaForGit`, `gitlog.GitCurve`. |
| Concurrence | `Globals.open()` / `Globals.close()` + un verrou global (voir `CucaDiagramFileMakerSmetana.layoutAndGetTextBlock`, sans verrou en TeaVM C). |

### 2.1 Inventaire (phase 0)

Harnais jetable : chaque cas est un petit graphe DOT, lu par notre parseur, copié
tel quel dans Smetana (tous les attributs passés par `agsafeset`), mis en page dans
une JVM à part avec un délai maximal ; on relève plantage / coordonnées.
70 cas, aucun ne boucle (pas de timeout).

| Fonctionne | Remarque |
|---|---|
| `rankdir=LR` | |
| `nodesep`, `ranksep` | en pouces |
| `minlen`, `weight` (y compris 0), `constraint=false` | |
| `label` d'arête, `headlabel`/`taillabel` | dimension passée par `_dim_w_h_` |
| boucles `a -> a` (avec label), arêtes multiples (avec labels), arcs retour | |
| clusters, imbriqués, avec label, deux clusters reliés, cluster vide | un cluster vide a une `bb` nulle |
| `shape=box/ellipse`, `fixedsize`, `width`/`height` | |
| `shape=record` | les champs acceptent aussi `_dim_w_h_` : `{_dim_30_16_\|<p>_dim_60_16_}` donne la bonne taille et le port `a:p` fonctionne → piste pour la phase 5 |
| graphe vide, nœuds isolés, composantes séparées, grand éventail | |

| Ne fonctionne pas | Cause (clé `UNSUPPORTED` / fonction) | Plan |
|---|---|---|
| `rankdir=BT`, `RL` | ignorés (rendu identique à TB / LR) | **contourné** : TB/LR puis miroir (fait en phase 2) |
| `rank=same/min/max/source/sink` (donc toute arête « plate ») | `collapse_rankset`, `minmax_edges` (`rank__c`) | **porté** (phase 4) |
| nom de sous-graphe ou de nœud commençant par `%` | `aginternalmaplookup` (`imap__c`) | **contourné** : noms générés `n0`, `cluster_0`… |
| `headport`/`tailport` compass (`n`, `se`…) | `poly_port` / `compassPort` (`shapes__c`) | **porté** (phase 4), sauf sur une boucle (`selfTop`/`selfLeft`/`selfBottom`) |
| `dir` | `arrow_flags` (`arrows__c`) | **contourné** : PlantUML gère `dir` |
| noms de flèches autres que `normal` | non reconnus (`eflag=0`, pas de découpe) | **contourné** : Smetana reçoit toujours `none`, PlantUML découpe la courbe (`BezierClip`) et dessine la flèche |
| `arrow_flags` par défaut | met une flèche `normal` aux **deux** bouts | `arrowtail` toujours forcé |
| `shape=circle/point/diamond/plaintext/Mrecord…` | `bind_shape` renvoie `null` → NPE | **contourné** : Smetana ne voit que `box`/`ellipse` |
| `peripheries>1` | `shapes__c` | dessiné par PlantUML (phase 3) |
| `group` | `xpenalty` dans `dotinit__c` | **porté** (phase 4) |
| `label` du graphe | `input__c` | dessiné par PlantUML (phase 3) |
| `size`, `ratio` | `input__c` | mise à l'échelle par PlantUML si besoin (phase 6) |
| `margin`/`pad` du graphe | sans effet sur la `bb` | gérés par PlantUML |
| `splines=ortho/polyline/line/false` | `edgeType` (`utils__c`) | phase 6 |
| `concentrate`, `compound`+`lhead`, `samehead`, `ordering`, `newrank`, `xlabel` | `dot_concentrate`, `dot_compoundEdges`, `dotinit__c`, `mincross__c`, `dot2_rank`, `utils__c` | phase 6 ou ignorés avec avertissement |

Conséquence de conception : **PlantUML garde la main sur la géométrie des nœuds**.
Pour chaque nœud on calcule nous-mêmes la taille du label (avec le `StringBounder`)
et la taille de la forme, on donne à Smetana une `box` ou une `ellipse` de cette
taille (`fixedsize=true`, `label=""`), et on dessine ensuite la vraie forme.

Quand une fonctionnalité de layout manque dans Smetana, la règle du `CLAUDE.md`
s'applique : la porter fidèlement depuis Graphviz 2.38, de façon minimale,
avec la ligne C d'origine en commentaire et un test.

## 3. Architecture proposée

Nouveau package `net.sourceforge.plantuml.ngdot` (le package `sdot` reste celui
du moteur Smetana pour les diagrammes UML) :

```
ngdot/
  PSystemNgDot, PSystemNgDotFactory          (fait, phase 1)
  parser/    DotLexer, DotToken, DotTokenType,
             DotParser (descente récursive), DotSyntaxException (fait, phase 1)
  model/     DotGraph, DotGraphBuilder, DotSubgraph, DotNode, DotEdge,
             DotAttributes, DotValue                                   (fait, phase 1)
  attr/      lecture typée des attributs : couleurs, styles, formes, flèches, nombres, ports
  layout/    DotToSmetana (modèle -> ST_Agraph_s), SmetanaResult (géométrie lue en retour)
  draw/      NodeShapeDrawer, EdgeDrawer, ArrowDrawer, ClusterDrawer, DotLabel
  DotDiagramRenderer   (TextBlock final, utilisé par PSystemNgDot)
```

### 3.1 Lexer

Conforme à la grammaire officielle (https://graphviz.org/doc/info/lang.html) :

- mots-clés insensibles à la casse : `strict`, `graph`, `digraph`, `node`, `edge`, `subgraph` ;
- identifiants : `[a-zA-Z\200-\377_][a-zA-Z\200-\377_0-9]*`, nombres `-?(.[0-9]+|[0-9]+(.[0-9]*)?)`,
  chaînes `"..."` (avec `\"` et continuation `\`+fin de ligne, concaténation `"a" + "b"`),
  chaînes HTML `<...>` (chevrons équilibrés, conservées brutes) ;
- opérateurs `{ } [ ] = ; , : -> --` ;
- commentaires `//`, `/* */`, et lignes commençant par `#` (sortie du préprocesseur C) ;
- position (ligne, colonne) sur chaque token pour les messages d'erreur.

### 3.2 Parseur

Descente récursive sur :

```
graph     : [strict] (graph|digraph) [ID] '{' stmt_list '}'
stmt_list : [stmt [';'] stmt_list]
stmt      : node_stmt | edge_stmt | attr_stmt | ID '=' ID | subgraph
attr_stmt : (graph|node|edge) attr_list
attr_list : '[' [a_list] ']' [attr_list]
a_list    : ID '=' ID [(';'|',')] [a_list]
edge_stmt : (node_id|subgraph) edgeRHS [attr_list]
edgeRHS   : edgeop (node_id|subgraph) [edgeRHS]
node_stmt : node_id [attr_list]
node_id   : ID [port]
port      : ':' ID [':' compass_pt] | ':' compass_pt
subgraph  : [subgraph [ID]] '{' stmt_list '}'
```

Sémantique à respecter (c'est là que se trouvent les pièges) :

- **portée des attributs par défaut** : `node [...]`, `edge [...]`, `graph [...]` s'appliquent
  aux éléments créés *après*, dans le sous-graphe courant et ses descendants ; un nœud
  déjà existant n'est pas modifié par un `node [...]` ultérieur ;
- un nœud est créé à sa première mention (y compris dans une arête) et appartient
  au sous-graphe où il apparaît la première fois ; une mention ultérieure ne fait que
  fusionner les attributs explicites ;
- arêtes en chaîne `a -> b -> c` et arêtes vers des sous-graphes `a -> {b c}`
  (produit cartésien) ;
- `strict` : pas d'arêtes multiples ni de boucles ;
- `--` dans un `digraph` (ou `->` dans un `graph`) est une erreur ;
- sous-graphe dont le nom commence par `cluster` = cluster dessiné ;
- les échappements de label `\n`, `\l`, `\r`, `\N`, `\G`, `\E`, `\T`, `\H` sont résolus au dessin.

Erreur de syntaxe → `PSystemError` classique avec la ligne fautive (et non un
diagramme vide). Comme le parseur a tout le bloc, l'en-tête n'a plus besoin
d'être sur une seule ligne (`digraph G` puis `{` à la ligne suivante fonctionne).

### 3.3 Modèle

Chaque nœud et chaque arête porte ses attributs **effectifs** (défauts hérités +
explicites) dans un `DotAttributes` (noms sensibles à la casse, valeurs `DotValue`
qui distinguent les chaînes HTML), lus ensuite par des accesseurs typés (`attr/`).
On garde l'ordre de création des nœuds et arêtes : il influence le layout de `dot`
comme celui de Smetana.

Sémantique reproduite de cgraph (vérifiée par `DotParserTest`) :

- les défauts `node [...]`/`edge [...]` d'un sous-graphe sont une **vue** sur ceux
  du parent, résolue au moment où le nœud/l'arête est créé ;
- les attributs de graphe d'un sous-graphe sont **copiés** depuis le parent à sa
  création (d'où le `label` de racine qui se retrouve sur les clusters définis après) ;
- un sous-graphe liste ses nœuds dans l'ordre global de création (comme
  `agfstnode`/`agnxtnode`), quel que soit l'ordre où ils y sont entrés ;
- rouvrir un sous-graphe nommé dans le même parent le continue ;
- les ports écrits dans l'arête (`a:p:ne`) deviennent `tailport`/`headport` et
  l'emportent sur les attributs ;
- est un cluster tout sous-graphe dont le nom commence par `cluster`
  (sensible à la casse, comme Graphviz 2.38).

### 3.4 Layout (modèle → Smetana)

`DotToSmetana`, sur le modèle de `SmetanaForGit` et `CucaDiagramFileMakerSmetana` :

1. `Globals.open()`, sous le même verrou que le reste de Smetana ;
2. `agopen` ; attributs de graphe transmis : `rankdir`, `nodesep`, `ranksep`, `margin`,
   puis ceux validés en phase 4 ;
3. pour chaque cluster : `agsubg("cluster_...")`, label dimensionné par l'astuce
   `createHackInitDimensionFromLabel`, `margin` ;
4. pour chaque nœud : calcul de la taille réelle (label mesuré + marges + forme,
   en respectant `width`/`height`/`fixedsize`/`margin` du DOT, en pouces) →
   `shape=box|ellipse`, `label=""`, `width`, `height`, `fixedsize=true` ;
   `record` passe par la forme `record` de Smetana (phase 5) ;
5. pour chaque arête : `agedge`, `minlen`, `weight`, `constraint`, `label`/`headlabel`/
   `taillabel` dimensionnés, `headport`/`tailport` ; pour un graphe non orienté
   `arrowhead=none` ;
6. `gvLayoutJobs`, lecture de la géométrie dans un objet Java simple (`SmetanaResult`),
   puis `Globals.close()` : le dessin ne doit plus dépendre de Smetana.

Unités : Smetana travaille en points (1/72 pouce) ; les tailles DOT sont en pouces ;
inversion de l'axe Y via `YMirror`.

### 3.5 Dessin (UGraphic)

- **Nœuds** : formes dessinées par nous à partir de la boîte calculée par Smetana.
  Lot 1 : `box`/`rect`/`rectangle`/`square`, `ellipse`/`oval`, `circle`, `doublecircle`,
  `point`, `plaintext`/`plain`/`none`, `diamond`. Lot 2 : `triangle`, `invtriangle`,
  `trapezium`, `parallelogram`, `house`, `invhouse`, `hexagon`, `octagon`, `cylinder`,
  `note`, `tab`, `folder`, `box3d`, `component`, `Mdiamond`, `Msquare`, `star`…
  (polygones génériques `sides`/`skew`/`distortion`/`orientation` si besoin).
  Forme inconnue → `box` + avertissement.
- **Styles** : `filled`, `dashed`, `dotted`, `bold`, `solid`, `invis`, `rounded`,
  `diagonals` ; `penwidth`, `color`, `fillcolor`, `fontcolor`, `fontname`, `fontsize`,
  `bgcolor` (graphe et clusters), `peripheries`.
- **Couleurs** : noms X11 (déjà dans `HColorSet`), `#rrggbb[aa]`, format HSV
  `"h,s,v"` / `"h s v"`, listes `"red:blue"` (dégradé ou multi-traits plus tard).
- **Arêtes** : courbes de Bézier issues de `ST_splines` (réutiliser la logique de
  `SmetanaEdge` / `GitCurve`), `style`, `color`, `penwidth`, `dir`
  (`forward/back/both/none`), `arrowhead`/`arrowtail` : `normal`, `inv`, `vee`, `dot`,
  `odot`, `diamond`, `odiamond`, `box`, `obox`, `tee`, `empty`, `open`, `none`,
  `arrowsize`. Les arêtes `invis` participent au layout mais ne sont pas dessinées.
- **Labels** : texte multi-lignes avec justification `\l`/`\r`/`\n`,
  `labelloc`/`labeljust` pour graphe et clusters, positions des labels d'arêtes
  données par Smetana.
- **Labels HTML** (`label=<...>`) : hors périmètre initial. D'abord : texte brut
  extrait + avertissement ; plus tard, conversion vers Creole / `TextBlock` pour
  `<b>`, `<i>`, `<br/>`, `<font>`, puis éventuellement `<table>`.
- **Liens** : `URL`/`href`, `tooltip`, `id` → `UGraphic` sait déjà produire des liens SVG.

Le résultat est un `TextBlock` exporté par `TextBlockExporter`, ce qui donne
gratuitement tous les formats raster/vecteur de PlantUML et le dark mode.

### 3.6 Intégration

- Le parsing est fait dans `PSystemNgDotFactory.createSystem` (l'erreur est
  rattachée à la bonne ligne) ; `PSystemNgDot.exportDiagramNow` fera layout →
  `TextBlockExporter`. En phase 1, il affiche un résumé texte du graphe parsé.
- La restriction `allowDotSvg` (#2495) visait le SVG brut de `dot` ; elle reste
  appliquée par l'ancien chemin et ne concerne pas le rendu fait par PlantUML.
- `PSystemErrorUtils` reconnaît l'en-tête DOT écrit dans un `@startuml` et
  suggère `@startdot`, désormais aussi en TeaVM.
- Compatibilité TeaVM et Java 8 : pas de dépendance externe, pas d'API post-Java 8.

## 4. Tests

- **Tests unitaires du lexer/parseur** (`src/test/java/.../ngdot/`) : chaque règle de
  grammaire, portée des attributs par défaut, chaînes concaténées, ports, `strict`,
  arêtes vers sous-graphes, erreurs avec numéro de ligne.
- **Vega** : nouveau répertoire `src/test/resources/vega/ngdot/`, un `.puml` par
  fonctionnalité (forme, flèche, style, cluster, rankdir…), en `output: svg` et
  `expected-description: (Dot)`. Références générées avec `VEGA_FORCE_WRITE=true`,
  relues à l'œil.
- **Corpus de comparaison** : quelques exemples classiques de la galerie Graphviz
  (cluster, fsm, unix, crazy simplifié, datastruct pour les records) rendus avec
  `dot` réel et avec ngdot, côte à côte, pour juger de la ressemblance — à garder
  hors Vega (images PNG dans un dossier de travail, non committées) ou dans `issues/`.
- **Robustesse** : graphe vide, nœud isolé, boucle `a -> a`, arêtes multiples,
  très grands graphes (temps), entrées qui faisaient planter Smetana → le diagramme
  doit afficher une erreur propre, jamais d'exception non rattrapée.

## 5. Phases

| Phase | Contenu | Critère de fin |
|---|---|---|
| **0** ✅ | Ce plan ; inventaire de ce que Smetana accepte (petit harnais qui passe un attribut à la fois et regarde si le layout change / plante). | Tableau §2 complété (« À vérifier » levé). **Fait** : §2.1. |
| **1** ✅ | Lexer + parseur + modèle, tests unitaires. `PSystemNgDot` affiche une erreur de syntaxe propre ou, en attendant le layout, un résumé texte. | Toute la grammaire DOT est parsée ; corpus galerie parsé sans erreur. **Fait** : `DotLexerTest` + `DotParserTest` (32 tests), les 61 graphes de `graphs/directed` et `graphs/undirected` du dépôt Graphviz sont parsés (aussi à travers `@startdot` et le préprocesseur), tests Vega `vega/ngdot/E0*` pour les erreurs. |
| **2** ✅ | Squelette de bout en bout : nœuds `box`/`ellipse` avec label texte, arêtes avec flèche `normal`, `rankdir`, `nodesep`, `ranksep`. Premiers tests Vega. | `digraph G { a -> b -> c; a -> c }` rendu en SVG/PNG, sans Graphviz installé. **Fait** : `ngdot.layout` (`SmetanaLayout`, `LayoutInput`, `LayoutResult`, `BezierClip`, `Rankdir`), `ngdot.draw` (`DotRenderer`, `DotLabel`, `NodeShape`), `ngdot.attr.DotNumbers` ; formes `box/rect/rectangle/square/ellipse/oval/circle/plaintext/plain/none` (inconnue → ellipse), labels `\n \l \r \N \G \E \T \H`, `fontsize`, `width/height/fixedsize/margin/regular`, `dir`, `arrowhead/arrowtail=none`, `arrowsize`, `minlen/weight/constraint`, labels d'arêtes, clusters avec label, 4 `rankdir`, thème PlantUML (`dotDiagram` dans le style) et dark mode. Les 61 graphes de la galerie Graphviz sont rendus sans plantage. Tests Vega `vega/ngdot/P01`…`P15`, tests unitaires `BezierClipTest`, `DotNumbersTest`, `DotLabelTest`. |
| **3** ✅ | Dessin : formes lot 1, styles, couleurs, polices, flèches, `dir`, labels d'arêtes (`label`, `headlabel`, `taillabel`), graphe non orienté, label du graphe, `bgcolor`. | Exemples simples de la galerie visuellement proches de `dot`. **Fait** (sans `fontname` ni HSV, reportés à plus tard) : `DotColors` (`#rrggbb[aa]`, noms CSS avec les valeurs X11 de Graphviz pour gray/green/maroon/purple, `grayN`, variantes `red1..4`, `transparent`, listes réduites à la 1re couleur), `DotStyle` (`filled dashed dotted bold solid invis rounded diagonals`, `penwidth`, `setlinewidth`), `ArrowShape` (toutes les flèches de `arrows.c` 2.38 avec `o`/`l`/`r`, synonymes, séquences, longueur propre à chaque type), formes `doublecircle point diamond`, `peripheries`, `color/fillcolor/fontcolor/bgcolor/pencolor`, `headlabel`/`taillabel` (`labelangle`, `labeldistance`, `labelfontcolor`, `labelfontsize`), label du graphe (`labelloc`, `labeljust`), clusters (`labelloc`, `labeljust`, styles). Deux portages Smetana : `ED_tail_port(e).clip = FALSE` / `ED_head_port` (`utils__c.common_init_edge`, pour découper les arêtes sur le vrai contour du losange) et la branche « label en bas » de `postproc__c.place_graph_label`. Tests Vega `S01`…`S09`, tests unitaires `DotColorsTest`, `DotStyleTest`, `ArrowShapeTest`, `BezierClipTest.clipOnAContour`. |
| **4** ✅ | Structure : clusters (imbriqués, label, couleurs, style), `rank=same/min/max`, `weight`, `constraint`, `minlen`, ports compass, `group`. Portage Graphviz 2.38 là où Smetana a des `UNSUPPORTED`. | Exemple `cluster` de la galerie fidèle. **Fait** : `SmetanaLayout` transmet les sous-graphes `rank=…` (noms générés `rank_N`, dans le cluster qui les contient), les ports compas (`n ne e se s sw w nw c _`, partie compas de `nom:compas`, inversés pour BT/RL) et `group`. Portages Graphviz 2.38 : `collapse_rankset`, `minmax_edges`, `minmax_edges2` (+ `decompose`) dans `rank__c` ; les deux branches manquantes de `UF_union` ; `compassPort` en entier et `compassPoint`, `invflip_side`, `invflip_angle`, le contexte `inside` de `poly_port` (`shapes__c`) ; toutes les branches de côtés de `beginpath`/`endpath` (`splines__c`, ~200 lignes, avec la ligne C en commentaire) ; `local_cross` (`mincross__c`) ; `xpenalty`/`weight` de `group` (`dotinit__c`). Les 61 graphes de la galerie passent, `world.gv` place bien ses rangs. Limites connues : les ports d'une boucle sont ignorés (la boucle reste à droite) ; deux arêtes plates opposées entre les mêmes nœuds se superposent. Tests Vega `R01`…`R10` (9 sur 10 échouent sans les portages ; `R08` vérifie qu'une boucle avec ports ne plante pas). |
| **5** | Formes lot 2, `record` (labels `{a|b|<p>c}` et ports), labels HTML simples, `URL`/`tooltip`. | Exemple `datastruct` rendu. |
| **6** | Finitions : `splines` (au moins `line`/`polyline`), `concentrate`, `compound`/`lhead`/`ltail` si portables, dark mode, documentation utilisateur, décision de basculer l'usage normal Java vers ng-dot (et de retirer `directdot`). | Plus de régression connue sur le corpus ; doc publiée. |

Chaque phase = une série de petits commits avec leurs tests Vega.

## 6. Risques

- **Robustesse de Smetana** : certaines entrées font planter ou boucler le portage ;
  il faut un garde-fou (exception rattrapée → image d'erreur ; peut-être un temps
  maximal).
- **Fonctionnalités de layout absentes** (`ortho`, `concentrate`, `compound`, `newrank`) :
  portage coûteux ; on peut les ignorer avec un avertissement.
- **Différences de taille de texte** avec `dot` (polices différentes) : les diagrammes
  seront proches mais jamais identiques — acceptable, c'est déjà le cas entre
  les moteurs PlantUML.
- **Labels HTML / `record`** : la partie la plus coûteuse du langage ; volontairement
  repoussée.
- **Verrou global de Smetana** : tous les rendus DOT passeront par le même verrou
  que les diagrammes UML en mode Smetana.

## 7. Questions ouvertes

Les quatre questions initiales sont tranchées (voir « Décisions prises » en §1).
