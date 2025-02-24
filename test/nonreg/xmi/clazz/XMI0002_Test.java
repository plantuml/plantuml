package nonreg.xmi.clazz;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.xmi.XmiTest;

/*


https://forum.plantuml.net/12972/only-the-first-line-any-component-description-exported-file


Test diagram MUST be put between triple quotes

"""
@startuml
class A {
}

class B{
}

A -->B
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
                <UML:Class name="A" xmi.id="ent0002">
                    <UML:Classifier.feature/>
                </UML:Class>
                <UML:Class name="B" xmi.id="ent0003">
                    <UML:Classifier.feature/>
                </UML:Class>
                <UML:Association namespace="model1" xmi.id="ass5">
                    <UML:Association.connection>
                        <UML:AssociationEnd association="ass5" type="ent0002" xmi.id="end6">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                        <UML:AssociationEnd association="ass5" type="ent0003" xmi.id="end7">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                    </UML:Association.connection>
                </UML:Association>
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
                <UML:Class name="A" xmi.id="ent0002">
                    <UML:Classifier.feature/>
                </UML:Class>
                <UML:Class name="B" xmi.id="ent0003">
                    <UML:Classifier.feature/>
                </UML:Class>
                <UML:Association xmi.id="ass5">
                    <UML:Association.connection>
                        <UML:AssociationEnd association="ass5" type="ent0002" xmi.id="end6">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                        <UML:AssociationEnd association="ass5" type="ent0003" xmi.id="end7">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                    </UML:Association.connection>
                </UML:Association>
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
                <UML:Class name="A" xmi.id="ent0002">
                    <UML:Classifier.feature/>
                </UML:Class>
                <UML:Class name="B" xmi.id="ent0003">
                    <UML:Classifier.feature/>
                </UML:Class>
                <UML:Association xmi.id="ass5">
                    <UML:Association.connection>
                        <UML:AssociationEnd isNavigable="false" association="ass5" participant="ent0002" xmi.id="end6"/>
                        <UML:AssociationEnd isNavigable="true" association="ass5" participant="ent0003" xmi.id="end7"/>
                    </UML:Association.connection>
                </UML:Association>
            </UML:Namespace.ownedElement>
        </UML:Model>
    </XMI.content>
</XMI>
}}}script


 */
public class XMI0002_Test extends XmiTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(2 entities)");
	}

}
