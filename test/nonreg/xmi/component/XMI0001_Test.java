package nonreg.xmi.component;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.xmi.XmiTest;

/*


https://forum.plantuml.net/12972/only-the-first-line-any-component-description-exported-file


Test diagram MUST be put between triple quotes

"""
@startuml
component test
note top of test: first line1\nsecond line2
note bottom of test
    first line3
    second line4
end note
@enduml
"""

Expected result MUST be put between triple brackets

{{{star
<?xml version="1.0" encoding="UTF-8"?><XMI xmlns:UML="href://org.omg/UML/1.3" xmi.version="1.1">
    <XMI.header>
        <XMI.metamodel xmi.name="UML" xmi.version="1.3"/>
    </XMI.header>
    <XMI.content>
        <UML:Model name="PlantUML" xmi.id="model1">
            <UML:Namespace.ownedElement>
                <UML:Component name="test" namespace="model1" xmi.id="ent0002">
                    <UML:Classifier.feature/>
                </UML:Component>
                <UML:Component name="first line1" namespace="model1" xmi.id="ent0004">
                    <UML:Classifier.feature/>
                </UML:Component>
                <UML:Component name="first line3" namespace="model1" xmi.id="ent0007">
                    <UML:Classifier.feature/>
                </UML:Component>
                <UML:Association namespace="model1" xmi.id="ass9">
                    <UML:Association.connection>
                        <UML:AssociationEnd association="ass9" type="ent0004" xmi.id="end10">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                        <UML:AssociationEnd association="ass9" type="ent0002" xmi.id="end11">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                    </UML:Association.connection>
                </UML:Association>
                <UML:Association namespace="model1" xmi.id="ass12">
                    <UML:Association.connection>
                        <UML:AssociationEnd association="ass12" type="ent0002" xmi.id="end13">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                        <UML:AssociationEnd association="ass12" type="ent0007" xmi.id="end14">
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
        <XMI.metamodel xmi.name="UML" xmi.version="1.3"/>
    </XMI.header>
    <XMI.content>
        <UML:Model name="PlantUML" xmi.id="model1">
            <UML:Namespace.ownedElement>
                <UML:Component name="test" namespace="model1" xmi.id="ent0002">
                    <UML:Classifier.feature/>
                </UML:Component>
                <UML:Component name="first line1" namespace="model1" xmi.id="ent0004">
                    <UML:Classifier.feature/>
                </UML:Component>
                <UML:Component name="first line3" namespace="model1" xmi.id="ent0007">
                    <UML:Classifier.feature/>
                </UML:Component>
                <UML:Association namespace="model1" xmi.id="ass9">
                    <UML:Association.connection>
                        <UML:AssociationEnd association="ass9" type="ent0004" xmi.id="end10">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                        <UML:AssociationEnd association="ass9" type="ent0002" xmi.id="end11">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                    </UML:Association.connection>
                </UML:Association>
                <UML:Association namespace="model1" xmi.id="ass12">
                    <UML:Association.connection>
                        <UML:AssociationEnd association="ass12" type="ent0002" xmi.id="end13">
                            <UML:AssociationEnd.participant/>
                        </UML:AssociationEnd>
                        <UML:AssociationEnd association="ass12" type="ent0007" xmi.id="end14">
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
        <XMI.metamodel xmi.name="UML" xmi.version="1.3"/>
    </XMI.header>
    <XMI.content>
        <UML:Model name="PlantUML" xmi.id="model1">
            <UML:Namespace.ownedElement>
                <UML:Component name="test" namespace="model1" xmi.id="ent0002">
                    <UML:Classifier.feature/>
                </UML:Component>
                <UML:Component name="first line1" namespace="model1" xmi.id="ent0004">
                    <UML:Classifier.feature/>
                </UML:Component>
                <UML:Component name="first line3" namespace="model1" xmi.id="ent0007">
                    <UML:Classifier.feature/>
                </UML:Component>
                    <UML:Dependency xmi.id="dep9">
                        <UML:Dependency.client>
                            <UML:Component xmi.idref="ent0004"/>
                        </UML:Dependency.client>
                        <UML:Dependency.supplier>
                            <UML:Component xmi.idref="ent0002"/>
                        </UML:Dependency.supplier>
                    </UML:Dependency>
                    <UML:Dependency xmi.id="dep10">
                        <UML:Dependency.client>
                            <UML:Component xmi.idref="ent0002"/>
                        </UML:Dependency.client>
                        <UML:Dependency.supplier>
                            <UML:Component xmi.idref="ent0007"/>
                        </UML:Dependency.supplier>
                    </UML:Dependency>
            </UML:Namespace.ownedElement>
        </UML:Model>
    </XMI.content>
</XMI>
}}}script


 */
public class XMI0001_Test extends XmiTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(3 entities)");
	}

}
