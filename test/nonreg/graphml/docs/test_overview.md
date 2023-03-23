# Plantuml GraphML Export Test Case Overview

## classdiagram

<table>
<tr>
<th>File<br>(.java)</th><th>PlantUML</th><th>Text</th>
</tr>
<tr><td>GML0000_Test</td><td><img alt="GML0000_Test" src="./classdiagram/GML0000_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Class
class class1
@enduml
</code></pre></td></tr>
<tr><td>GML0001_Test</td><td><img alt="GML0001_Test" src="./classdiagram/GML0001_Test.svg" width="400"/></td><td><pre><code>@startuml
' Class with Stereotypes
class class1 &laquo;stereo1&raquo; &laquo;stereo2&raquo;
@enduml
</code></pre></td></tr>
<tr><td>GML0002_Test</td><td><img alt="GML0002_Test" src="./classdiagram/GML0002_Test.svg" width="400"/></td><td><pre><code>@startuml
class class1 {
' Basic method and field
' with types and parameters
data:String
method(x:int, y:int):int
}
@enduml
</code></pre></td></tr>
<tr><td>GML0003_Test</td><td><img alt="GML0003_Test" src="./classdiagram/GML0003_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Interface
circle intf1
@enduml
</code></pre></td></tr>
<tr><td>GML0004_Test</td><td><img alt="GML0004_Test" src="./classdiagram/GML0004_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Interface
' Alternative Notation
() intf1
@enduml
</code></pre></td></tr>
<tr><td>GML0005_Test</td><td><img alt="GML0005_Test" src="./classdiagram/GML0005_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Interface
' Alternative Notation
interface intf1
@enduml
</code></pre></td></tr>
<tr><td>GML0006_Test</td><td><img alt="GML0006_Test" src="./classdiagram/GML0006_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: n-Ary Association
diamond d1
@enduml
</code></pre></td></tr>
<tr><td>GML0007_Test</td><td><img alt="GML0007_Test" src="./classdiagram/GML0007_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: N-ary Association
' Alternative Notation
<> d1
@enduml
</code></pre></td></tr>
<tr><td>GML0008_Test</td><td><img alt="GML0008_Test" src="./classdiagram/GML0008_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Enumeration
enum enum1
@enduml
</code></pre></td></tr>
<tr><td>GML0009_Test</td><td><img alt="GML0009_Test" src="./classdiagram/GML0009_Test.svg" width="400"/></td><td><pre><code>@startuml
' Example of a plantUML Entity (non UML Type)
' class needed to make it a class diagram
class class1
entity ent1
@enduml
</code></pre></td></tr>
<tr><td>GML0010_Test</td><td><img alt="GML0010_Test" src="./classdiagram/GML0010_Test.svg" width="400"/></td><td><pre><code>@startuml
' Example of a plantUML Annotation (non UML Type)
' class needed to make it a class diagram
annotation anno1
@enduml
</code></pre></td></tr>
<tr><td>GML0011_Test</td><td><img alt="GML0011_Test" src="./classdiagram/GML0011_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Abstract Class
abstract class abstr1
@enduml
</code></pre></td></tr>
<tr><td>GML0012_Test</td><td><img alt="GML0012_Test" src="./classdiagram/GML0012_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Abstract Class
' alternative notation
abstract abstr1
@enduml
</code></pre></td></tr>
<tr><td>GML0013_Test</td><td><img alt="GML0013_Test" src="./classdiagram/GML0013_Test.svg" width="400"/></td><td><pre><code>@startuml
' Generalization
Class01 <|-- Class02
@enduml
</code></pre></td></tr>
<tr><td>GML0013b_Test</td><td><img alt="GML0013b_Test" src="./classdiagram/GML0013b_Test.svg" width="400"/></td><td><pre><code>@startuml
' Generalization,
' reversed order
Class02 --|> Class01
@enduml
</code></pre></td></tr>
<tr><td>GML0014_Test</td><td><img alt="GML0014_Test" src="./classdiagram/GML0014_Test.svg" width="400"/></td><td><pre><code>@startuml
' Composition
Class03 *-- Class04
@enduml
</code></pre></td></tr>
<tr><td>GML0014b_Test</td><td><img alt="GML0014b_Test" src="./classdiagram/GML0014b_Test.svg" width="400"/></td><td><pre><code>@startuml
' Composition,
' reversed order
Class04 --* Class03
@enduml
</code></pre></td></tr>
<tr><td>GML0015_Test</td><td><img alt="GML0015_Test" src="./classdiagram/GML0015_Test.svg" width="400"/></td><td><pre><code>@startuml
' Aggregation
Class05 o-- Class06
@enduml
</code></pre></td></tr>
<tr><td>GML0015b_Test</td><td><img alt="GML0015b_Test" src="./classdiagram/GML0015b_Test.svg" width="400"/></td><td><pre><code>@startuml
' Aggregation
' reversed order
Class06 --o Class05
@enduml
</code></pre></td></tr>
<tr><td>GML0016_Test</td><td><img alt="GML0016_Test" src="./classdiagram/GML0016_Test.svg" width="400"/></td><td><pre><code>@startuml
' Example of an non UML relation
Class07 .. Class08
@enduml
</code></pre></td></tr>
<tr><td>GML0017_Test</td><td><img alt="GML0017_Test" src="./classdiagram/GML0017_Test.svg" width="400"/></td><td><pre><code>@startuml
' Association
Class09 -- Class10
@enduml
</code></pre></td></tr>
<tr><td>GML0018_Test</td><td><img alt="GML0018_Test" src="./classdiagram/GML0018_Test.svg" width="400"/></td><td><pre><code>@startuml
' Realization
Class11 <|.. Class12
@enduml
</code></pre></td></tr>
<tr><td>GML0019_Test</td><td><img alt="GML0019_Test" src="./classdiagram/GML0019_Test.svg" width="400"/></td><td><pre><code>@startuml
' Association with Navigability
class Class1
Class13 --> Class14
@enduml
</code></pre></td></tr>
<tr><td>GML0020_Test</td><td><img alt="GML0020_Test" src="./classdiagram/GML0020_Test.svg" width="400"/></td><td><pre><code>@startuml
' Dependency
Class15 ..> Class16
@enduml
</code></pre></td></tr>
<tr><td>GML0020b_Test</td><td><img alt="GML0020b_Test" src="./classdiagram/GML0020b_Test.svg" width="400"/></td><td><pre><code>@startuml
' Dependency,
' reversed order
Class16 <.. Class15
@enduml
</code></pre></td></tr>
<tr><td>GML0021_Test</td><td><img alt="GML0021_Test" src="./classdiagram/GML0021_Test.svg" width="400"/></td><td><pre><code>@startuml
' Realization,
' reversed order
Class17 ..|> Class18
@enduml
</code></pre></td></tr>
<tr><td>GML0022_Test</td><td><img alt="GML0022_Test" src="./classdiagram/GML0022_Test.svg" width="400"/></td><td><pre><code>@startuml
' Composition with navigability
Class19 <--* Class20
@enduml
</code></pre></td></tr>
<tr><td>GML0022b_Test</td><td><img alt="GML0022b_Test" src="./classdiagram/GML0022b_Test.svg" width="400"/></td><td><pre><code>@startuml
' Composition with Navigability
' reversed order
Class20 *--> Class19
@enduml
</code></pre></td></tr>
<tr><td>GML0023_Test</td><td><img alt="GML0023_Test" src="./classdiagram/GML0023_Test.svg" width="400"/></td><td><pre><code>@startuml
' Non UML Relation
Class21 #-- Class22
@enduml
</code></pre></td></tr>
<tr><td>GML0024_Test</td><td><img alt="GML0024_Test" src="./classdiagram/GML0024_Test.svg" width="400"/></td><td><pre><code>@startuml
' Association with non navigable end
class Class1
Class23 x-- Class24
@enduml
</code></pre></td></tr>
<tr><td>GML0025_Test</td><td><img alt="GML0025_Test" src="./classdiagram/GML0025_Test.svg" width="400"/></td><td><pre><code>@startuml
' NON UML relation
Class25 }-- Class26
@enduml
</code></pre></td></tr>
<tr><td>GML0026_Test</td><td><img alt="GML0026_Test" src="./classdiagram/GML0026_Test.svg" width="400"/></td><td><pre><code>@startuml
' Nesting
Class27 +-- Class28
@enduml
</code></pre></td></tr>
<tr><td>GML0026b_Test</td><td><img alt="GML0026b_Test" src="./classdiagram/GML0026b_Test.svg" width="400"/></td><td><pre><code>@startuml
' Nesting
' reversed order
Class28 --+ Class27
@enduml
</code></pre></td></tr>
<tr><td>GML0027_Test</td><td><img alt="GML0027_Test" src="./classdiagram/GML0027_Test.svg" width="400"/></td><td><pre><code>@startuml
' Generalization, alternative notation
Class29 ^-- Class30
@enduml
</code></pre></td></tr>
<tr><td>GML0028_Test</td><td><img alt="GML0028_Test" src="./classdiagram/GML0028_Test.svg" width="400"/></td><td><pre><code>@startuml
' Association with multiplicity and name
Class1 "1" -- "*" Class2: "relation"
@enduml
</code></pre></td></tr>
<tr><td>GML0029_Test</td><td><img alt="GML0029_Test" src="./classdiagram/GML0029_Test.svg" width="400"/></td><td><pre><code>@startuml
' Association Class
class Class1
class Class2
class AssoClass1
(Class1, Class2) .. AssoClass1
@enduml
</code></pre></td></tr>
<tr><td>GML0030_Test</td><td><img alt="GML0030_Test" src="./classdiagram/GML0030_Test.svg" width="400"/></td><td><pre><code>@startuml
' n-ary Association
diamond d1
class Class1
class Class2
class Class3
Class1 -r- d1
Class2 -l- d1
Class3 -d- d1
@enduml
</code></pre></td></tr>
<tr><td>GML0031_Test</td><td><img alt="GML0031_Test" src="./classdiagram/GML0031_Test.svg" width="400"/></td><td><pre><code>@startuml
' non UML Relation
Class07 "1" .. "*" Class08: has
@enduml
</code></pre></td></tr>
<tr><td>GML0032_Test</td><td><img alt="GML0032_Test" src="./classdiagram/GML0032_Test.svg" width="400"/></td><td><pre><code>@startuml
' Class names with spaces
class "This is my class" as class1
class class2 as "It works this way too"

class2 *-- "foo/dummy" : use
@enduml
</code></pre></td></tr>
<tr><td>GML0033_Test</td><td><img alt="GML0033_Test" src="./classdiagram/GML0033_Test.svg" width="400"/></td><td><pre><code>@startuml
' Class with fields and methods:
' visibility inidcators
class MyClass {
-field1
#field2
~method1()
+method2()
+ ~ error()
}
@enduml
</code></pre></td></tr>
<tr><td>GML0034_Test</td><td><img alt="GML0034_Test" src="./classdiagram/GML0034_Test.svg" width="400"/></td><td><pre><code>@startuml
' Class with fields and methods
' static and abstract indicators
class MyClass {
{static} field1
field2
{abstract} method1()
method2()
}
@enduml
</code></pre></td></tr>
<tr><td>GML0035_Test</td><td><img alt="GML0035_Test" src="./classdiagram/GML0035_Test.svg" width="400"/></td><td><pre><code>@startuml
' Class with fields: visibility and static mixed
class MyClass {
# {static} field1
{static} # field2
{static} field3
}
@enduml
</code></pre></td></tr>
<tr><td>GML0036_Test</td><td><img alt="GML0036_Test" src="./classdiagram/GML0036_Test.svg" width="400"/></td><td><pre><code>@startuml
' Class with multiplicity and role
' Example for proper notation
Class07 "1\nitsClass07" --> "*\nitsClass08" Class08: has
@enduml
</code></pre></td></tr>
<tr><td>GML0037_Test</td><td><img alt="GML0037_Test" src="./classdiagram/GML0037_Test.svg" width="400"/></td><td><pre><code>@startuml
package "plantuml" {
package "utils" {
class Class01
}
}
@enduml
</code></pre></td></tr>
<tr><td>GML0038_Test</td><td><img alt="GML0038_Test" src="./classdiagram/GML0038_Test.svg" width="400"/></td><td><pre><code>@startuml
' Floating Note
class class1
note "A note\nwith some\nlong text" as N
@enduml
</code></pre></td></tr>
<tr><td>GML0039_Test</td><td><img alt="GML0039_Test" src="./classdiagram/GML0039_Test.svg" width="400"/></td><td><pre><code>@startuml
' Note connected to one class
class class1
note top of class1: text
@enduml
</code></pre></td></tr>
<tr><td>GML0040_Test</td><td><img alt="GML0040_Test" src="./classdiagram/GML0040_Test.svg" width="400"/></td><td><pre><code>@startuml
' Note on several classes
class class1
class class2
note "text" as N
N .. class1
N .. class2
@enduml
</code></pre></td></tr>
<tr><td>GML0041_Test</td><td><img alt="GML0041_Test" src="./classdiagram/GML0041_Test.svg" width="400"/></td><td><pre><code>@startuml
' Note on field and methods
class A {
{static}  counter
+{abstract} start()
}
note right of A::counter
  This member is annotated
end note
note right of A::start
  This method is now explained in a UML note
end note
@enduml

</code></pre></td></tr>
<tr><td>GML0042_Test</td><td><img alt="GML0042_Test" src="./classdiagram/GML0042_Test.svg" width="400"/></td><td><pre><code>@startuml
' Note on field and methods
' NOT SUPPORTED FEATURE
class Dummy
Dummy --> Foo : A link
note on link #red: note that is red
@enduml
</code></pre></td></tr>
<tr><td>GML0043_Test</td><td><img alt="GML0043_Test" src="./classdiagram/GML0043_Test.svg" width="400"/></td><td><pre><code>@startuml
' JSON Data
class Class
json JSON {
   "size":"Large",
   "color": ["Red", "Green"]
}
@enduml
</code></pre></td></tr>
<tr><td>GML0044_Test</td><td><img alt="GML0044_Test" src="./classdiagram/GML0044_Test.svg" width="400"/></td><td><pre><code>@startuml
' Nesting
package "plantuml" {
class Class01
}
@enduml
</code></pre></td></tr>
<tr><td>GML0045_Test</td><td><img alt="GML0045_Test" src="./classdiagram/GML0045_Test.svg" width="400"/></td><td><pre><code>@startuml
' Deployment
package "plantuml" &laquo;Node&raquo; {
class Class01
}
@enduml
</code></pre></td></tr>
<tr><td>GML0046_Test</td><td><img alt="GML0046_Test" src="./classdiagram/GML0046_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package Style: non UML
package "plantuml" &laquo;Rectangle&raquo; {
class Class01
}
@enduml
</code></pre></td></tr>
<tr><td>GML0047_Test</td><td><img alt="GML0047_Test" src="./classdiagram/GML0047_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package Style: non UML
package "plantuml" &laquo;Folder&raquo; {
class Class01
}
@enduml
</code></pre></td></tr>
<tr><td>GML0048_Test</td><td><img alt="GML0048_Test" src="./classdiagram/GML0048_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package Style: non UML
package "plantuml" &laquo;Frame&raquo; {
class Class01
}
@enduml
</code></pre></td></tr>
<tr><td>GML0049_Test</td><td><img alt="GML0049_Test" src="./classdiagram/GML0049_Test.svg" width="400"/></td><td><pre><code>@startuml
' Deployment
package "plantuml" &laquo;Cloud&raquo; {
class Class01
}
@enduml
</code></pre></td></tr>
<tr><td>GML0050_Test</td><td><img alt="GML0050_Test" src="./classdiagram/GML0050_Test.svg" width="400"/></td><td><pre><code>@startuml
' Deployment
package "plantuml" &laquo;Database&raquo; {
class Class01
}
@enduml
</code></pre></td></tr>
<tr><td>GML0051_Test</td><td><img alt="GML0051_Test" src="./classdiagram/GML0051_Test.svg" width="400"/></td><td><pre><code>@startuml
' Association with read order
Driver - Car: drives >
Car -- Person: < owns
@enduml
</code></pre></td></tr>
</table>

## component

<table>
<tr>
<th>File<br>(.java)</th><th>PlantUML</th><th>Text</th>
</tr>
<tr><td>GML0000_Test</td><td><img alt="GML0000_Test" src="./component/GML0000_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Component
component comp1
@enduml
</code></pre></td></tr>
<tr><td>GML0001_Test</td><td><img alt="GML0001_Test" src="./component/GML0001_Test.svg" width="400"/></td><td><pre><code>@startuml
' Two components, not related
component comp1
component comp2
@enduml
</code></pre></td></tr>
<tr><td>GML0001b_Test</td><td><img alt="GML0001b_Test" src="./component/GML0001b_Test.svg" width="400"/></td><td><pre><code>@startuml
' Multiple umrelated components
[First component]
[Another component] as Comp2
component Comp3
component [Last\ncomponent] as Comp4
@enduml
</code></pre></td></tr>
<tr><td>GML0002_Test</td><td><img alt="GML0002_Test" src="./component/GML0002_Test.svg" width="400"/></td><td><pre><code>@startuml
' Single Entity: Package
package pack1
@enduml
</code></pre></td></tr>
<tr><td>GML0003_Test</td><td><img alt="GML0003_Test" src="./component/GML0003_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package with interface, not connected
[comp1]
() "intf1"
@enduml
</code></pre></td></tr>
<tr><td>GML0004_Test</td><td><img alt="GML0004_Test" src="./component/GML0004_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package with interface, not connected
' alternative interface notation
[comp1]
interface intf1
@enduml
</code></pre></td></tr>
<tr><td>GML0005_Test</td><td><img alt="GML0005_Test" src="./component/GML0005_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package with interface: provides interface relation
intf1 - [comp1]
@enduml
</code></pre></td></tr>
<tr><td>GML0006_Test</td><td><img alt="GML0006_Test" src="./component/GML0006_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package with interface, requires interface relation
[comp1] ..> HTTP : use
@enduml
</code></pre></td></tr>
<tr><td>GML0007_Test</td><td><img alt="GML0007_Test" src="./component/GML0007_Test.svg" width="400"/></td><td><pre><code>@startuml
' Nesting
package "pack1" {
	component comp1
}
@enduml
</code></pre></td></tr>
<tr><td>GML0008_Test</td><td><img alt="GML0008_Test" src="./component/GML0008_Test.svg" width="400"/></td><td><pre><code>@startuml
' Component with Note
component comp1
note right of comp1: A note\nwith some\nlong text
@enduml
</code></pre></td></tr>
<tr><td>GML0008b_Test</td><td><img alt="GML0008b_Test" src="./component/GML0008b_Test.svg" width="400"/></td><td><pre><code>@startuml
' floating note
component comp1
note "A note\nwith some\nlong text" as N
@enduml
</code></pre></td></tr>
<tr><td>GML0009_Test</td><td><img alt="GML0009_Test" src="./component/GML0009_Test.svg" width="400"/></td><td><pre><code>@startuml
' Deployment
node "node1" {
	component comp1
}
@enduml
</code></pre></td></tr>
<tr><td>GML0010_Test</td><td><img alt="GML0010_Test" src="./component/GML0010_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package Style: non UML
folder "folder1" {
	component comp1
}
@enduml

</code></pre></td></tr>
<tr><td>GML0011_Test</td><td><img alt="GML0011_Test" src="./component/GML0011_Test.svg" width="400"/></td><td><pre><code>@startuml
' Package Style: non UML
frame "frame1" {
	component comp1
}
@enduml
</code></pre></td></tr>
<tr><td>GML0012_Test</td><td><img alt="GML0012_Test" src="./component/GML0012_Test.svg" width="400"/></td><td><pre><code>@startuml
' Deployment
cloud "cloud1" {
	component comp1
}
@enduml
</code></pre></td></tr>
<tr><td>GML0013_Test</td><td><img alt="GML0013_Test" src="./component/GML0013_Test.svg" width="400"/></td><td><pre><code>@startuml
' Deployment
database "database1" {
	component comp1
}
@enduml
</code></pre></td></tr>
<tr><td>GML0014_Test</td><td><img alt="GML0014_Test" src="./component/GML0014_Test.svg" width="400"/></td><td><pre><code>@startuml
' Component Nesting
component C1 {
component C2
component C3
}
@enduml
</code></pre></td></tr>
<tr><td>GML0015_Test</td><td><img alt="GML0015_Test" src="./component/GML0015_Test.svg" width="400"/></td><td><pre><code>@startuml
' several ports on component
component comp1 {
  port port1
  portin port2_in
  portout port3_out
}
@enduml
</code></pre></td></tr>
<tr><td>GML0016_Test</td><td><img alt="GML0016_Test" src="./component/GML0016_Test.svg" width="400"/></td><td><pre><code>@startuml
'Connected ports on component
[i]
component C {
  portin p1
  portin p2
  portout po1
  portout po2
  component c1
}
[o]
i --> p1
i --> p2
p1 --> c1
po1 --> o
po2 --> o
c1 --> po1
@enduml
</code></pre></td></tr>
</table>

