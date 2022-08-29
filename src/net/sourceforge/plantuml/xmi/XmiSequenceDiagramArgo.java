package net.sourceforge.plantuml.xmi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class XmiSequenceDiagramArgo extends XmiSequenceDiagram {
	public XmiSequenceDiagramArgo(SequenceDiagram diagram, Document document) {
		super(diagram, document);
	}

	@Override
	public void build() {
		final Element xmi = document.createElement("XMI");
		xmi.setAttribute("xmi.version", "1.2");
		xmi.setAttribute("xmlns:UML", "href://org.omg/UML/1.3");
		document.appendChild(xmi);

		final Element header = document.createElement("XMI.header");
		xmi.appendChild(header);

		final Element metamodel = document.createElement("XMI.metamodel");
		metamodel.setAttribute("xmi.name", "UML");
		metamodel.setAttribute("xmi.version", "1.4");
		header.appendChild(metamodel);

		final Element content = document.createElement("XMI.content");
		xmi.appendChild(content);

		final Element model = createElement(diagram, "UML:Model");
		model.setAttribute("name", "PlantUML");
		content.appendChild(model);

		Element ownedElement = document.createElement("UML:Namespace.ownedElement");
		model.appendChild(ownedElement);
		ownedElement.appendChild(createCollaborationElement());

		for (Participant participant : diagram.participants()) {
			ownedElement.appendChild(createActorElement(participant));
		}
	}

	private Element createActorElement(Participant participant) {
		Element actor = createElement(participant, "UML:Actor");
		actor.setAttribute("name", String.join(" ", participant.getDisplay(false).asList()));
		return actor;
	}

	private Node createCollaborationElement() {
		Element collaboration = document.createElement("UML:Collaboration");
		Element ownedElement = document.createElement("UML:Namespace.ownedElement");

		for (Participant participant : diagram.participants()) {
			ownedElement.appendChild(createClassifierRole(participant));
		}

		collaboration.appendChild(ownedElement);

		Node messages = collaboration.appendChild((document.createElement("UML:Collaboration.interaction")))
				.appendChild(document.createElement("UML:Interaction"))
				.appendChild(document.createElement("UML:Interaction.message"));

		Message prevMessage = null;
		for (Event event : diagram.events()) {
			if (event instanceof Message) {
				Message message = (Message) event;
				messages.appendChild(createMessage(message, prevMessage));
				ownedElement.appendChild(createSendAction(message));
				prevMessage = message;
			}
		}

		return collaboration;
	}

	private Node createSendAction(Message message) {
		Element sendAction = createElement(message, "UML:SendAction");
		sendAction.appendChild(document.createElement("UML:Action.script"))
				.appendChild(createElement("UML:ActionExpression",
						new String[][] { { "xmi.id", getXmiId("UML:ActionExpression", message) },
								{ "body", getDisplayString(message.getLabel()) } }));
		return sendAction;
	}

	private Element createElement(Object object, String tag) {
		return createElement(tag, new String[][] { { "xmi.id", getXmiId("UML:ActionExpression", object) } });
	}

	private Node createRef(String tag, Object target) {
		Element role = document.createElement(tag);
		role.setAttribute("xmi.idref", getXmiId(tag, target));
		return role;
	}

	private Element createClassifierRole(Participant participant) {
		Element classifierRole = createElement(participant, "UML:ClassifierRole");

		classifierRole.setAttribute("name", participant.getCode());
		classifierRole.appendChild(document.createElement("UML:ClassifierRole.base"))
				.appendChild(createRef("UML:Actor", participant));
		return classifierRole;
	}

	private Element createMessage(Message message, Message prevMessage) {
		Element messageElement = createElement(message, "UML:Message");
		messageElement.appendChild(document.createElement("UML:Message.sender"))
				.appendChild(createRef("UML:ClassifierRole", message.getParticipant1()));
		messageElement.appendChild(document.createElement("UML:Message.receiver"))
				.appendChild(createRef("UML:ClassifierRole", message.getParticipant2()));
		messageElement.appendChild(document.createElement("UML:Message.action"))
				.appendChild(createRef("UML:SendAction", message));

		if (prevMessage != null) {
			messageElement.appendChild(document.createElement("UML:Message.predecessor"))
					.appendChild(createRef("UML:Message", prevMessage));
		}

		return messageElement;
	}

}
