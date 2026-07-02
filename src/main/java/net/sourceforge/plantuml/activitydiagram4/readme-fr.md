# Architecture de `activitydiagram4`

> Ce document explique **pourquoi** ce paquet existe et **comment** ses pièces
> s'emboîtent. Il est volontairement pédagogique : on peut le lire sans
> connaître `activitydiagram3` en détail. Le code lui-même n'est pour l'instant
> qu'un squelette (interfaces, classes, Javadoc) ; les corps de méthodes lèvent
> `UnsupportedOperationException`. L'objectif de cette étape est de figer
> l'architecture, pas de produire un moteur fonctionnel.

---

## 1. Le problème qu'on cherche à résoudre

Un diagramme d'activité, c'est un enchaînement d'actions relié par des flèches,
avec des embranchements (`if`), des boucles (`repeat`, `while`), du parallélisme
(`fork`), et éventuellement des **swimlanes** (couloirs qui indiquent « qui fait
quoi »).

Le moteur actuel, `activitydiagram3`, a deux limitations structurelles qu'on
veut lever :

1. **Les swimlanes ne sont pas dans le modèle de placement.** Elles sont
   calculées *après coup*. Le diagramme est d'abord disposé comme si les
   couloirs n'existaient pas, puis un post-traitement redécouvre où chaque
   couloir commence et finit, et redessine tout couloir par couloir. C'est
   fragile, surtout pour les flèches qui traversent d'un couloir à l'autre.

2. **Le sens est câblé « de haut en bas ».** Impossible de produire un
   diagramme qui se déroule de gauche à droite sans réécrire une grande partie
   du code, parce que la verticalité est supposée partout (« le point d'entrée
   est en haut », « on empile vers le bas », etc.).

`activitydiagram4` repart d'une idée différente pour régler les deux d'un coup.

---

## 2. L'idée centrale : décrire, puis résoudre

### 2.1 L'analogie de l'architecte

Imaginez qu'on veuille placer des meubles dans une pièce. Deux méthodes :

- **Méthode « calcul direct » (diagram3)** : « Le canapé fait 2 m, je le mets à
  x=0. Le fauteuil fait 1 m, donc je le mets à x=2. La table… ». Chaque objet
  calcule sa position en fonction du précédent. Si on ajoute un objet au milieu,
  ou si un mur bouge, il faut tout recalculer à la main. C'est rapide à écrire
  mais rigide.

- **Méthode « contraintes » (diagram4)** : on n'écrit pas de positions, on écrit
  des **règles** : « le fauteuil est à droite du canapé, avec au moins 30 cm
  entre les deux » ; « tout doit tenir dans la pièce » ; « le tapis est centré
  sous la table ». Puis un **solveur** cherche des positions qui satisfont
  toutes les règles. Ajouter un objet = ajouter des règles, sans toucher au
  reste.

`activitydiagram4` est la seconde méthode. On appelle ça un **layout à base de
contraintes**.

### 2.2 Pourquoi ça règle nos deux problèmes

- **Swimlanes natives** : un couloir devient une simple règle du type « cet
  élément est à l'intérieur de ce couloir ». La largeur du couloir n'est plus
  calculée après coup : elle *émerge* de la résolution, parce que le couloir est
  poussé par ce qu'il contient. Une flèche qui traverse deux couloirs est une
  flèche comme les autres — ses deux extrémités ont de vraies coordonnées une
  fois le système résolu.

- **Orientation libre** : si les règles sont écrites en termes de « sens du
  flux » et « sens transverse » plutôt que « y » et « x », alors « de haut en
  bas » et « de gauche à droite » sont littéralement le même code. Seule la
  toute dernière étape (projeter sur l'écran) diffère.

### 2.3 Ce n'est pas nouveau dans PlantUML

Cette approche existe déjà dans le code, en **1 dimension**, pour les diagrammes
de séquence : c'est le moteur **teoz**, bâti sur le paquet
`net.sourceforge.plantuml.real`. Les « lifelines » y sont des variables, reliées
par des contraintes, résolues par relaxation. `activitydiagram4` généralise
cette idée en **2 dimensions**.

---

## 3. La brique de base : le solveur `real`

Avant de décrire nos classes, il faut comprendre l'outil sur lequel tout repose,
le paquet `net.sourceforge.plantuml.real` (déjà existant, non modifié).

- Un **`Real`** est une variable de position sur **une** droite (1D). Sa valeur
  n'est pas connue tant qu'on n'a pas résolu.
- On pose des contraintes **monotones** entre `Real`, du type
  **« A est au moins à la position de B plus une constante »**
  (par ex. `A ≥ B + 30`).
- Il existe des variables **dérivées** : `RealMax(x, y, …)` vaut le maximum de
  plusieurs autres, `RealMiddle(a, b)` vaut leur milieu, un décalage fixe donne
  « B, mais 10 pixels plus loin ».
- La résolution (`compile`) procède par **relaxation** : on pousse
  itérativement les variables jusqu'à ce que toutes les contraintes soient
  satisfaites (le « point fixe »).

**Le mot « monotone » est capital.** Toutes nos contraintes ne font que
*repousser* les choses dans un seul sens (« au moins »), jamais tirer dans les
deux sens à la fois. Conséquence : tant que le graphe des dépendances n'a pas de
cycle, la relaxation **converge toujours**, et le résultat **ne dépend pas de
l'ordre** dans lequel on a ajouté les contraintes (seul le nombre d'itérations
en dépend). C'est ce qui rend le système prévisible et débuggable. On y revient
au §7.

---

## 4. Le vocabulaire des axes : FLOW et CROSS

C'est le pivot de la neutralité d'orientation. On n'écrit jamais « x » ni « y »
dans la logique de placement. On écrit :

| Axe logique | Rôle | En mode « haut→bas » | En mode « gauche→droite » |
|-------------|------|----------------------|---------------------------|
| **FLOW**  | sens de progression des activités | vertical (y) | horizontal (x) |
| **CROSS** | sens transverse ; les swimlanes sont toujours des bandes sur cet axe | horizontal (x) | vertical (y) |

C'est l'enum **`PlanAxis`**.

L'enum **`FlowDirection`** (`TOP_TO_BOTTOM` ou `LEFT_TO_RIGHT`) fait le lien
final avec l'écran : sa méthode `toScreen(flow, cross)` traduit une position
logique résolue en un vrai point `(x, y)`.

> **Piège important à comprendre** : on ne peut pas se contenter de dessiner le
> diagramme vertical puis de « faire pivoter l'image » pour l'obtenir horizontal.
> Le **texte ne pivote pas** : un libellé d'activité reste horizontal dans les
> deux cas. Donc la neutralité d'orientation doit être gérée **au moment de
> construire les contraintes**, pas au moment de dessiner. C'est le rôle de la
> classe suivante.

**`FlowDimension`** est un adaptateur : une taille écran (largeur × hauteur,
mesurée sur le texte) projetée sur les axes logiques (extension le long de FLOW,
extension le long de CROSS). En mode vertical, « le long de FLOW » = la hauteur ;
en mode horizontal, « le long de FLOW » = la largeur. Toutes les tailles sont
**connues d'avance** (on mesure le texte avant de placer quoi que ce soit) :
seules les *positions* sont des variables. C'est pourquoi `FlowDimension` ne
contient que des `double`, pas des `Real`.

---

## 5. La couche « plan » : les objets géométriques

Ce sont les classes à la racine du paquet. Elles ne connaissent rien aux
activités ni aux boucles : ce sont des primitives géométriques réutilisables.
Le chef d'orchestre est **`RealPlan`**.

### 5.1 `RealPlan` — le plan partagé

C'est **l'objet central**, l'équivalent 2D de ce que teoz fait en 1D. Il
possède :

- **deux droites de contraintes** (une par axe : `flowOrigin`, `crossOrigin`),
- les **registres** de tout ce qui a été déclaré (figures à dessiner,
  connexions, bandes, scopes, corridors),
- les méthodes de **fabrique** (`createPoint`, `createRectangle`, `createBand`,
  `createScope`, `createCorridor`) : toute variable passe par le plan, pour être
  rattachée à la bonne droite et recevoir un **nom** traçable,
- le cycle de vie : `compile()` (résout), `beautify()` (embellit), `drawAll()`
  (dessine).

Tout le reste du paquet gravite autour de cet objet : les autres classes ne font
qu'y **déclarer** des choses.

### 5.2 `RealPoint` et `RealRectangle` — positions variables, tailles fixes

- **`RealPoint`** : un point 2D = un `Real` sur FLOW + un `Real` sur CROSS.

  > **Le mécanisme le plus important à retenir** : deux points sont *alignés*
  > quand ils **partagent le même objet `Real`** sur un axe. Pas « quand une
  > force les rapproche » — quand c'est *littéralement la même variable*. C'est
  > l'« alignement par identité ». On en reparle au §6.2 avec la notion de
  > *spine*.

- **`RealRectangle`** : un rectangle de **taille fixe** dont seule la **position**
  est variable (un `Real` par axe, plus une `FlowDimension` constante). C'est la
  brique des figures « feuilles » : une boîte d'activité, un losange de
  condition, un cercle de départ. Comme la taille est connue, un seul `Real` par
  axe suffit — d'où deux fois moins de variables qu'une représentation à deux
  coins. Ses coordonnées dérivées (`getFlowMax`, `getCrossCenter`…) sont
  elles-mêmes des `Real`, donc réutilisables dans d'autres contraintes.

  Attention : `RealRectangle` est réservé aux éléments **de taille connue**. Les
  conteneurs dont la taille dépend du contenu (un groupe, le corps d'une boucle)
  n'utilisent **pas** cette classe : ils sont représentés par un `RealScope`
  (§5.4).

### 5.3 `RealBand` — une swimlane (et plus)

Une **`RealBand`** est un intervalle `[min, max]` sur un axe, les deux bornes
étant des variables. C'est la représentation **native** d'une swimlane (une
bande sur l'axe CROSS). Mais c'est générique : une colonne de branche d'un `if`,
ou une branche de `fork`, est aussi une bande.

Les méthodes disent tout du modèle :

- `mustContain(figure, margin)` : émet deux contraintes — la figure est après le
  bord bas de la bande, **et** le bord haut de la bande est après la figure. La
  bande est donc *poussée* par son contenu.
- `mustFollow(previous, gap)` : la bande commence après la fin de la précédente
  (largeur du séparateur de couloir).
- `ensureMinSize(minSize)` : largeur minimale (pour `skinparam SwimlaneWidth`).

**Ce que ça remplace** : dans diagram3, la largeur d'un couloir était découverte
en redessinant tout le diagramme dans des détecteurs de limites, couloir par
couloir. Ici, la largeur *est* le résultat de la résolution.

### 5.4 `RealScope` — le remplaçant de la « géométrie » de diagram3

C'est un concept subtil mais fondamental. Un **`RealScope`** est la **boîte
englobante d'un sous-arbre**, mais exprimée en **variables** (des `RealMax` /
`RealMin` sur son contenu).

La différence avec diagram3 est un **renversement du sens de calcul** :

- **diagram3** : chaque tuile *calcule* sa dimension de bas en haut, puis
  *translate* ses enfants de haut en bas. L'information descend, rigide.
- **diagram4** : le contenu *pousse* les bornes de son scope. On enregistre une
  figure ou un sous-scope (`register(...)`), et ça repousse automatiquement les
  bords. On *lit* les bornes seulement après la résolution.

Un scope peut être **imbriqué** dans un autre (le scope du corps d'une boucle
est dans le scope global) : un scope enfant pousse les bornes de son parent.

Fonction spéciale : `getCrossMaxInside(band)` donne le bord d'un scope
**restreint aux figures d'une bande donnée**. On en a besoin pour placer
finement une flèche de retour (cf. §8).

### 5.5 `RealCorridor` — réserver la place d'une flèche

Une **`RealCorridor`** est un **canal de routage** : la position (sur un axe) du
long segment d'une flèche coudée — typiquement le segment vertical d'une flèche
de retour de boucle — accompagné de l'**épaisseur** qu'il réserve (dégagement de
la flèche + largeur de son éventuel libellé).

Pourquoi en faire un objet de premier plan ? Parce que ça règle **par
construction** un bug de diagram3 : là-bas, une flèche qui traverse les couloirs
ne « comptait » dans la largeur d'aucun couloir, donc sa place n'était réservée
nulle part, d'où des chevauchements. Ici, le corridor *pousse* le scope et la
bande qui le contiennent (`register`, `mustContain`) : sa place est prise en
compte *pendant* la résolution.

- `mustClear(scope, margin)` : le corridor se place **au-delà** de tout un
  sous-arbre (routage « conservateur » : on contourne le bloc entier plutôt que
  de chercher le plus court chemin — cf. §7).

### 5.6 Les connexions : `PlanPort`, `PortDirection`, `PlanConnection`, `Route`, `ConnectionRouter`

C'est la chaîne qui gère les **flèches**.

- **`PortDirection`** : le côté d'une figure où une flèche s'accroche, en termes
  logiques : `FLOW_IN` (par où le flux entre — le « nord » en vertical),
  `FLOW_OUT` (par où il sort — le « sud »), `CROSS_MIN` / `CROSS_MAX` (les côtés
  latéraux — « ouest » / « est » en vertical). C'est l'héritage direct des
  « hooks » nommés du paquet `gtile` (une tentative précédente), reformulé sur
  FLOW/CROSS.

- **`PlanPort`** : un point d'accroche nommé = un `RealPoint` + une
  `PortDirection`. **C'est l'interface de composition** : assembler deux tuiles,
  c'est relier le port `FLOW_OUT` de l'une au port `FLOW_IN` de l'autre. Comme
  la position d'un port est faite de `Real`, ses coordonnées résolues sont
  **absolues** — une flèche inter-couloirs est donc une flèche ordinaire.

- **`PlanConnection`** : une flèche de premier plan entre deux ports, avec un
  libellé optionnel, une couleur, et un `RealCorridor` optionnel (pour les
  détours latéraux). Elle est **déclarée** pendant la planification mais
  **routée seulement après** la résolution.

- **`ConnectionRouter`** : l'unique routeur, qui s'exécute **après** `compile()`.
  Comme tout a des coordonnées absolues à ce moment-là, les décisions que
  diagram3 devait deviner à l'avance (« la flèche sort-elle à gauche ou à
  droite du losange ? ») deviennent de **simples comparaisons de nombres**.

- **`Route`** : le résultat = une polyligne orthogonale en coordonnées écran. Du
  pur tracé ; le rendu final (têtes de flèche, couleurs) est délégué à la couche
  de dessin, qui peut réutiliser la machinerie `Snake` de diagram3.

> **Le gain concret** : dans diagram3, une seule flèche de retour de boucle
> nécessite **cinq classes** de connexion différentes, choisies à l'avance selon
> l'ordre des couloirs (utilisé comme approximation d'une géométrie qui n'existe
> pas encore), et **chaque** connexion a **deux** algorithmes de tracé (un pour
> le cas mono-couloir, un pour le cas multi-couloirs). Ici : **un** routeur, **un**
> algorithme, des décisions prises sur de vraies coordonnées.

### 5.7 `PlanFigure` — ce qui se dessine

Interface d'un **dessinable** enregistré dans le plan : une boîte, un losange, un
séparateur de couloir… Elle expose son `RealRectangle` et sait se dessiner
(`drawSolved`) une fois les positions résolues.

Point clé : une fois le plan résolu, le dessin est **plat**. Le plan parcourt
ses figures et ses flèches routées en **une seule passe**, dans **un seul
système de coordonnées absolu**. Plus de dessin récursif, plus de passes par
couloir, plus de reconstruction de translations.

---

## 6. La couche « planner » : du diagramme aux contraintes

Les classes précédentes ne savent pas ce qu'est une activité. Le sous-paquet
`planner` fait le pont entre **l'arbre d'instructions** (réutilisé tel quel
depuis `activitydiagram3` : c'est beaucoup de code éprouvé qu'on ne veut pas
réécrire) et le plan de contraintes.

### 6.1 `PlannedTile` — volontairement presque vide

C'est l'équivalent de la « tuile » (`Ftile`) de diagram3, mais **réduit à
l'essentiel** : quelques ports (`portIn`, `portOut`, ports nommés) et un scope.

Ce qu'elle **n'a pas**, et c'est délibéré : pas de calcul de dimension, pas de
translation d'enfants, pas de méthode de dessin, pas d'ensemble de swimlanes à
propager vers le haut. **Toute la vérité est dans le `RealPlan` partagé** ; la
tuile ne fait qu'indiquer à son assembleur *où brancher les flèches*.

`portOut` peut être `null` : cela signifie que le sous-arbre **arrête le flux**
(`stop`, `end`, `detach`) — l'équivalent du « pas de point de sortie » de
diagram3.

> Garder cette classe aussi maigre est un **garde-fou de conception** : ça évite
> de reconstruire subrepticement un arbre qui aurait autorité sur la géométrie,
> ce qui nous ramènerait aux problèmes de diagram3.

### 6.2 `PlanContext` — le contexte de déclaration, et la *spine*

Quand un planner déclare sa portion de diagramme, il a besoin de savoir :
dans quel plan ? dans quel couloir ? dans quel scope ? Et surtout : **aligné sur
quoi ?** C'est le rôle de `PlanContext`, qui transporte le plan, la lane
courante, le scope courant, et la **spine**.

La **spine** est la **variable `Real` d'alignement partagée du contexte
courant** (sur l'axe CROSS). C'est le cœur du mécanisme d'alignement :

> Deux activités qui se suivent sont alignées verticalement parce qu'elles se
> centrent sur **la même** variable `spine`. Pas parce qu'une force les
> rapproche : parce que c'est le **même objet**.

Pourquoi c'est important (et pas juste élégant) :

- **moins de variables et de forces** → plus rapide ;
- **impossible de diverger** : il n'y a rien à faire converger, c'est la même
  valeur des deux côtés ;
- **compatible avec le solveur monotone** : exprimer « A = B » avec des forces
  demanderait de pousser dans les deux sens, ce qui créerait un **cycle** et
  casserait la garantie de convergence. Le partage de variable contourne le
  problème.

Le contexte est **immuable** ; les constructions imbriquées en dérivent un
enfant : `withNewSpine` (une branche de `if` a son propre axe d'alignement),
`withNewScope` (le corps d'une boucle a sa propre boîte englobante),
`withLane` (un changement de couloir).

### 6.3 `PlannerFactory` — le miroir de `FtileFactory`

Interface qui **construit** les `PlannedTile` à partir de l'arbre
d'instructions. Elle est le pendant de `FtileFactory` de diagram3, ce qui permet
de réutiliser toute la couche « commandes » et « instructions » existante (un
futur `Instruction.createPlanned(...)` fera écho à `Instruction.createFtile(...)`).

Les méthodes reflètent les constructions du langage : `start`, `stop`, `end`,
`activity`, `assembly` (l'enchaînement de deux tuiles), `repeat`, `createWhile`,
`createIf`, `createParallel`, `createGroup`.

Deux différences de fond avec `FtileFactory`, déjà actées :

1. Chaque méthode prend le **`PlanContext`**, parce que planifier une
   construction imbriquée, c'est *dériver un contexte* (nouvelle spine, nouveau
   scope, changement de couloir). Diagram3 s'appuyait au contraire sur le fait
   que chaque tuile transportait sa swimlane.
2. Pas de `decorateIn`/`decorateOut` : les libellés de flèche s'attachent aux
   **connexions** créées au moment de l'assemblage, pas aux tuiles.

> Les signatures sont des **esquisses** : elles grossiront pour refléter celles
> de `FtileFactory` (link renderings, stéréotypes, styles, notes, urls…).
> Un point reste ouvert : garder le motif « chaîne de délégateurs » de diagram3
> ou passer à une classe de planner par construction. La chaîne compensait
> surtout le fait que la factory était bâtie *avant* le parcours de l'arbre, ce
> qui n'est plus nécessaire ici.

### 6.4 `Lane` et `LaneSet` — les swimlanes, vues d'en haut

- **`Lane`** : une swimlane comme citoyen de plein droit = une `RealBand`
  ordonnée sur l'axe CROSS, plus ses attributs de présentation (titre,
  couleurs). Contrairement au `Swimlane` de diagram3 (un sac mutable rempli
  pendant le post-traitement), elle est **immuable après planification** ; sa
  géométrie émerge de la résolution. L'ordre des couloirs sert **uniquement** à
  contraindre l'ordre des bandes — jamais comme approximation géométrique pour
  décider d'un routage.

- **`LaneSet`** : la collection ordonnée. Ses responsabilités se réduisent à la
  phase de planification : créer une lane (donc une bande) par couloir déclaré,
  émettre les contraintes d'ordre entre bandes adjacentes, émettre les largeurs
  minimales (titres, `skinparam`), enregistrer la décoration (séparateurs,
  fonds, bandeau de titre) comme figures ordinaires.

  > `SwimlaneWidth same` (tous les couloirs à la même largeur) demande **deux
  > passes** de résolution : résoudre, mesurer le couloir le plus large, ajouter
  > des largeurs minimales, re-résoudre. C'est sûr précisément parce que le
  > système est monotone : la seconde passe ne peut que *grandir*, jamais entrer
  > en conflit.

---

## 7. Les principes de conception (et pourquoi ils tiennent)

1. **Déclarer, ne pas calculer.** Aucune méthode ne calcule une dimension ni ne
   translate un enfant. La seule vérité est dans le plan. (À comparer avec
   `calculateDimension` / `getTranslateFor` de diagram3, disséminés partout.)

2. **Aligner par identité, pas par force.** Le partage de la spine, expliqué au
   §6.2.

3. **Contraintes monotones uniquement**, sur un **graphe acyclique**. Le sens de
   circulation naturel est : *contenu → scope → corridor → bande → bande
   suivante*. Aucun cycle, donc convergence garantie et résultat indépendant de
   l'ordre d'émission. Ce dernier point (**indépendance à l'ordre**) doit rester
   un **invariant testé** : c'est lui qui garantit que la composition reste
   vraiment locale (un planner n'a pas à se coordonner avec les autres).

4. **Routage conservateur.** Un corridor contourne un scope entier plutôt que de
   chercher le plus court chemin. On perd un peu en compacité, on gagne en
   simplicité et en robustesse (chaque contrainte reste monotone). L'évitement
   fin des collisions reste une affaire d'après-résolution.

5. **Dessin plat.** Une seule passe, un seul repère absolu, après résolution.

6. **Débuggabilité d'abord.** Chaque variable et chaque force porte un **nom**
   issu de son chemin de construction (ex. `repeat_1/diamond2/crossCenter`).
   Quand une contrainte est fausse, le symptôme peut apparaître loin de la
   cause : les noms et le `dump()` du plan résolu sont là pour ça.

---

## 8. Un exemple déroulé : une boucle `repeat` sur deux couloirs

Pour rendre tout ça concret, voici comment se planifie une boucle dont le corps
traverse deux couloirs A et B, avec une activité « backward » de correction et
sa flèche de retour. (C'est le cas qui mettait le plus en difficulté diagram3.)

```
repeat
  :étape 1;          <- couloir A
  :étape 2;          <- couloir B
backward :corriger;  <- couloir B
repeat while (encore ?)
```

1. **Les couloirs.** Le plan crée deux bandes sur l'axe CROSS : A puis B, avec
   `B.mustFollow(A, séparateur)`. Personne ne calcule « la largeur de A » : on la
   lira à la fin.

2. **Les figures.** Les losanges d'entrée/sortie et « étape 1 » sont dans le
   couloir A et **partagent la même spine** (donc alignés d'office, sans effort).
   « étape 2 » et « corriger » sont dans le couloir B. Toutes les tailles sont
   connues ; seules les positions sont variables.

3. **L'axe du flux.** Chaîne triviale de contraintes « au moins » : chaque
   élément est après le précédent, plus un écart qui inclut la hauteur du
   libellé de flèche. Pas d'espace résiduel à centrer : il n'y en a pas.

4. **Le couloir de retour.** La flèche `sortie → corriger → entrée` a besoin
   d'un canal vertical à droite du corps. Le corps est un `RealScope` ; on écrit
   « le corridor est au-delà du bord droit de ce scope »
   (`corridor.mustClear(bodyScope, marge)`), puis « la bande B s'étend jusqu'au
   corridor » (`B.mustContain(corridor, marge)`). **Et voilà le bug de diagram3
   réglé** : le corridor pousse la bande, donc la largeur du couloir tient compte
   de la flèche de retour.

5. **Le libellé de la flèche.** Sa largeur est une constante connue : une
   contrainte de plus sur la bande B. (À comparer avec les rustines de diagram3
   pour les libellés qui débordent.)

6. **Résolution puis routage.** `compile()` fait converger le tout. *Ensuite*
   seulement, on route les flèches avec de vraies coordonnées : « la flèche
   sort-elle du losange par la gauche ou la droite ? » devient une comparaison
   de nombres, pas une heuristique sur l'ordre des couloirs.

Le seul motif qui ajoute une difficulté vraiment nouvelle est le **`fork`** : les
barres de synchronisation sont les premières figures dont la **taille elle-même**
(pas seulement la position) est variable, car elles doivent s'étendre sur
l'intervalle transverse couvert par les branches. C'est le banc d'essai de la
représentation à bornes-variables (`RealScope` plutôt que `RealRectangle`).

---

## 9. Points encore ouverts

- **Tassement du solveur.** Un solveur monotone pousse tout vers le minimum :
  tout se retrouve « collé en haut à gauche ». Le recentrage esthétique (une
  boîte au milieu de l'espace disponible, par ex.) se fait dans une **passe
  cosmétique post-résolution** (`RealPlan.beautify()`) : un calcul local sur des
  valeurs déjà figées, sans risque de divergence. On préfère ça à des contraintes
  de centrage, qui alourdiraient le système.

- **`SwimlaneWidth same`.** Deux passes de résolution (cf. §6.4).

- **Skins intrinsèquement orientés.** Certains habillages restent liés au sens :
  les branches d'un losange partent à gauche/droite en vertical mais en haut/bas
  en horizontal ; le placement des libellés « oui/non » change. La librairie
  masque **100 % du positionnement**, mais **pas 100 % de l'orientation** : ces
  cas-là consultent encore `FlowDirection`.

- **Forme exacte de `PlannerFactory`** et **chaîne de délégateurs vs planner par
  construction** (cf. §6.3).

---

## 10. Filiation

| Origine | Ce qu'on en garde | Ce qu'on abandonne |
|---------|-------------------|--------------------|
| `activitydiagram3.ftile` (vcompact) | la couche instructions/commandes, la machinerie de tracé `Snake` | le layout par translation rigide, la compression, les swimlanes en post-traitement |
| `activitydiagram3.gtile` | l'idée des points d'accroche nommés (`GPoint` → `PlanPort`/`PortDirection`) | le layout par arbre de translations (jamais activé, `USE_GTILE = false`) |
| `sequencediagram.teoz` | le précédent 1D du layout par contraintes sur `real` ; le modèle de déploiement (nouveau moteur derrière un pragma, l'ancien reste par défaut) | — |

---

## Lien

- [Activity Diagram (New Syntax)](https://plantuml.com/activity-diagram-beta)
