package nonreg.xmi.clazz;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.xmi.XmiTest;

/*


https://forum.plantuml.net/12644/class-access-modifiers-not-in-xmi-export

Test diagram MUST be put between triple quotes

"""
@startuml
-class foo{
    # field1 : type
}
@enduml
"""

Expected result MUST be put between triple brackets

{{{star
<?xml version="1.0" encoding="UTF-8"?><XMI xmlns:UML="href://org.omg/UML/1.3" xmi.version="1.1">
    <XMI.header>
        <XMI.documentation>
            <XMI.exporter>PlantUML</XMI.exporter>
            
        </XMI.documentation>
        <XMI.metamodel xmi.name="UML" xmi.version="1.4"/>
    </XMI.header>
    <XMI.content>
        <UML:Model name="PlantUML" xmi.id="model1">
            <UML:Namespace.ownedElement>
                <UML:Class name="foo" visibility="private" xmi.id="ent0002">
                    <UML:Classifier.feature>
                        <UML:Attribute name="field1 : type" visibility="protected" xmi.id="att3"/>
                    </UML:Classifier.feature>
                </UML:Class>
            </UML:Namespace.ownedElement>
        </UML:Model>
    </XMI.content>
</XMI>
}}}star

{{{argo
<?xml version="1.0" encoding="UTF-8"?><XMI xmlns:UML="href://org.omg/UML/1.3" xmi.version="1.1">
    <XMI.header>
        <XMI.documentation>
            <XMI.exporter>PlantUML</XMI.exporter>
            
        </XMI.documentation>
        <XMI.metamodel xmi.name="UML" xmi.version="1.4"/>
    </XMI.header>
    <XMI.content>
        <UML:Model name="PlantUML" xmi.id="model1">
            <UML:Namespace.ownedElement>
                <UML:Class name="foo" visibility="private" xmi.id="ent0002">
                    <UML:Classifier.feature>
                        <UML:Attribute name="field1 : type" visibility="protected" xmi.id="att3"/>
                    </UML:Classifier.feature>
                </UML:Class>
            </UML:Namespace.ownedElement>
        </UML:Model>
    </XMI.content>
</XMI>
}}}argo

{{{script
<?xml version="1.0" encoding="UTF-8"?><XMI xmlns:UML="href://org.omg/UML/1.3" xmi.version="1.1">
    <XMI.header>
        <XMI.documentation>
            <XMI.exporter>PlantUML</XMI.exporter>
            
        </XMI.documentation>
        <XMI.metamodel xmi.name="UML" xmi.version="1.4"/>
    </XMI.header>
    <XMI.content>
        <UML:Model name="PlantUML" xmi.id="model1">
            <UML:Namespace.ownedElement>
                <UML:Class name="foo" visibility="private" xmi.id="ent0002">
                    <UML:Classifier.feature>
                        <UML:Attribute name="field1 : type" visibility="protected" xmi.id="att3"/>
                    </UML:Classifier.feature>
                </UML:Class>
            </UML:Namespace.ownedElement>
        </UML:Model>
    </XMI.content>
</XMI>
}}}script


 */
public class XMI0000_Test extends XmiTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(1 entities)");
	}

}
