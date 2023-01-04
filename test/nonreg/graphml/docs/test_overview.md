# Plantuml GraphML Export Test Case Overview

## classdiagram

<table>
<tr>
<th>File<br>(.java)</th><th>PlantUML</th><th>Text</th>
</tr>
<tr><td>GML0000_Test</td><td><img alt="GML0000_Test" src="./classdiagram/GML0000_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class class1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0001_Test</td><td><img alt="GML0001_Test" src="./classdiagram/GML0001_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class class1 &laquo;stereo1&raquo; &laquo;stereo2&raquo;<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0002_Test</td><td><img alt="GML0002_Test" src="./classdiagram/GML0002_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class class1 {<br>String data<br>void method()<br>}<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0003_Test</td><td><img alt="GML0003_Test" src="./classdiagram/GML0003_Test.svg" width="400"/></td><td><pre><code>@startuml<br>circle intf1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0004_Test</td><td><img alt="GML0004_Test" src="./classdiagram/GML0004_Test.svg" width="400"/></td><td><pre><code>@startuml<br>() intf1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0005_Test</td><td><img alt="GML0005_Test" src="./classdiagram/GML0005_Test.svg" width="400"/></td><td><pre><code>@startuml<br>interface intf1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0006_Test</td><td><img alt="GML0006_Test" src="./classdiagram/GML0006_Test.svg" width="400"/></td><td><pre><code>@startuml<br>diamond d1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0007_Test</td><td><img alt="GML0007_Test" src="./classdiagram/GML0007_Test.svg" width="400"/></td><td><pre><code>@startuml<br><> d1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0008_Test</td><td><img alt="GML0008_Test" src="./classdiagram/GML0008_Test.svg" width="400"/></td><td><pre><code>@startuml<br>enum enum1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0009_Test</td><td><img alt="GML0009_Test" src="./classdiagram/GML0009_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class class1<br>entity ent1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0010_Test</td><td><img alt="GML0010_Test" src="./classdiagram/GML0010_Test.svg" width="400"/></td><td><pre><code>@startuml<br>annotation anno1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0011_Test</td><td><img alt="GML0011_Test" src="./classdiagram/GML0011_Test.svg" width="400"/></td><td><pre><code>@startuml<br>abstract class abstr1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0012_Test</td><td><img alt="GML0012_Test" src="./classdiagram/GML0012_Test.svg" width="400"/></td><td><pre><code>@startuml<br>abstract abstr1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0013_Test</td><td><img alt="GML0013_Test" src="./classdiagram/GML0013_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class01 <|-- Class02<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0013b_Test</td><td><img alt="GML0013b_Test" src="./classdiagram/GML0013b_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class02 --|> Class01<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0014_Test</td><td><img alt="GML0014_Test" src="./classdiagram/GML0014_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class03 *-- Class04<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0014b_Test</td><td><img alt="GML0014b_Test" src="./classdiagram/GML0014b_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class04 --* Class03<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0015_Test</td><td><img alt="GML0015_Test" src="./classdiagram/GML0015_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class05 o-- Class06<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0016_Test</td><td><img alt="GML0016_Test" src="./classdiagram/GML0016_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class07 .. Class08<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0017_Test</td><td><img alt="GML0017_Test" src="./classdiagram/GML0017_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class09 -- Class10<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0018_Test</td><td><img alt="GML0018_Test" src="./classdiagram/GML0018_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class11 <|.. Class12<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0019_Test</td><td><img alt="GML0019_Test" src="./classdiagram/GML0019_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class Class1<br>Class13 --> Class14<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0020_Test</td><td><img alt="GML0020_Test" src="./classdiagram/GML0020_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class15 ..> Class16<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0020b_Test</td><td><img alt="GML0020b_Test" src="./classdiagram/GML0020b_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class16 <.. Class15<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0021_Test</td><td><img alt="GML0021_Test" src="./classdiagram/GML0021_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class17 ..|> Class18<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0022_Test</td><td><img alt="GML0022_Test" src="./classdiagram/GML0022_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class19 <--* Class20<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0022b_Test</td><td><img alt="GML0022b_Test" src="./classdiagram/GML0022b_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class20 *--> Class19<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0023_Test</td><td><img alt="GML0023_Test" src="./classdiagram/GML0023_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class21 #-- Class22<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0024_Test</td><td><img alt="GML0024_Test" src="./classdiagram/GML0024_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class Class1<br>Class23 x-- Class24<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0025_Test</td><td><img alt="GML0025_Test" src="./classdiagram/GML0025_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class25 }-- Class26<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0026_Test</td><td><img alt="GML0026_Test" src="./classdiagram/GML0026_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class27 +-- Class28<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0026b_Test</td><td><img alt="GML0026b_Test" src="./classdiagram/GML0026b_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class28 --+ Class27<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0027_Test</td><td><img alt="GML0027_Test" src="./classdiagram/GML0027_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class29 ^-- Class30<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0028_Test</td><td><img alt="GML0028_Test" src="./classdiagram/GML0028_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class1 "1" -- "*" Class2: "relation"<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0029_Test</td><td><img alt="GML0029_Test" src="./classdiagram/GML0029_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class Class1<br>class Class2<br>class AssoClass1<br>(Class1, Class2) .. AssoClass1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0030_Test</td><td><img alt="GML0030_Test" src="./classdiagram/GML0030_Test.svg" width="400"/></td><td><pre><code>@startuml<br>diamond d1<br>class Class1<br>class Class2<br>class Class3<br>Class1 -r- d1<br>Class2 -l- d1<br>Class3 -d- d1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0031_Test</td><td><img alt="GML0031_Test" src="./classdiagram/GML0031_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class07 "1" .. "*" Class08: has<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0032_Test</td><td><img alt="GML0032_Test" src="./classdiagram/GML0032_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class "This is my class" as class1<br>class class2 as "It works this way too"<br><br>class2 *-- "foo/dummy" : use<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0033_Test</td><td><img alt="GML0033_Test" src="./classdiagram/GML0033_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class MyClass {<br>-field1<br>#field2<br>~method1()<br>+method2()<br>+ ~ error()<br>}<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0034_Test</td><td><img alt="GML0034_Test" src="./classdiagram/GML0034_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class MyClass {<br>{static} field1<br>field2<br>{abstract} method1()<br>method2()<br>}<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0035_Test</td><td><img alt="GML0035_Test" src="./classdiagram/GML0035_Test.svg" width="400"/></td><td><pre><code>@startuml<br>class MyClass {<br># {static} field1<br>{static} # field2<br>{static} field3<br>}<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0036_Test</td><td><img alt="GML0036_Test" src="./classdiagram/GML0036_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class07 "1\nitsClass07" --> "*\nitsClass08" Class08: has<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0037_Test</td><td><img alt="GML0037_Test" src="./classdiagram/GML0037_Test.svg" width="400"/></td><td><pre><code>@startuml<br>Class07 "itsClass07\n1" --> "itsClass08\n*" Class08: has<br>@enduml<br></code></pre></td></tr>
</table>

## component

<table>
<tr>
<th>File<br>(.java)</th><th>PlantUML</th><th>Text</th>
</tr>
<tr><td>GML0000_Test</td><td><img alt="GML0000_Test" src="./component/GML0000_Test.svg" width="400"/></td><td><pre><code>@startuml<br>component comp1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0001_Test</td><td><img alt="GML0001_Test" src="./component/GML0001_Test.svg" width="400"/></td><td><pre><code>@startuml<br>component comp1<br>component comp2<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0002_Test</td><td><img alt="GML0002_Test" src="./component/GML0002_Test.svg" width="400"/></td><td><pre><code>@startuml<br>package pack1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0003_Test</td><td><img alt="GML0003_Test" src="./component/GML0003_Test.svg" width="400"/></td><td><pre><code>@startuml<br>[comp1]<br>() "intf1"<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0004_Test</td><td><img alt="GML0004_Test" src="./component/GML0004_Test.svg" width="400"/></td><td><pre><code>@startuml<br>[comp1]<br>interface intf1<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0005_Test</td><td><img alt="GML0005_Test" src="./component/GML0005_Test.svg" width="400"/></td><td><pre><code>@startuml<br>intf1 - [comp1]<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0006_Test</td><td><img alt="GML0006_Test" src="./component/GML0006_Test.svg" width="400"/></td><td><pre><code>@startuml<br>[comp1] ..> HTTP : use<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0007_Test</td><td><img alt="GML0007_Test" src="./component/GML0007_Test.svg" width="400"/></td><td><pre><code>@startuml<br>package "pack1" {<br>	component comp1<br>}<br>@enduml<br></code></pre></td></tr>
<tr><td>GML0008_Test</td><td><img alt="GML0008_Test" src="./component/GML0008_Test.svg" width="400"/></td><td><pre><code>@startuml<br>component comp1<br>note right of comp1: A note\nwith some\nlong text<br>@enduml<br></code></pre></td></tr>
</table>

