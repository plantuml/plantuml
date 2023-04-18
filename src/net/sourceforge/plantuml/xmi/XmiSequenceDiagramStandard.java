package net.sourceforge.plantuml.xmi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Grouping;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class XmiSequenceDiagramStandard extends XmiSequenceDiagram {

	private Stack<HashSet<String>> covered;
	private HashMap<Participant, Stack<Element>> activeParticipants;

	public XmiSequenceDiagramStandard(SequenceDiagram diagram, Document document) {
		super(diagram, document);

		covered = new Stack<>();
		covered.push(new HashSet<String>());

		activeParticipants = new HashMap<>();
	}

	@Override
	public void build() {
		Node packagedElement = document
				.appendChild(createElement("uml:Model",
						new String[][] { { "xmlns:uml", "http://www.omg.org/spec/UML/20110701" },
								{ "xmlns:xmi", "http://schema.omg.org/spec/XMI/2.1" }, { "xmi:version", "2.1" },
								{ "xmi:id", getXmiId("uml:Model", diagram) } }))
				.appendChild(createUmlElement(diagram, "packagedElement", "Interaction"));
		packagedElement.appendChild(createUmlElement(diagram, "nestedClassifier", "Collaboration"));

		for (Participant participant : diagram.participants()) {
			packagedElement.appendChild(createElement(participant, "lifeline",
					new String[][] { { "name", getDisplayString(participant.getDisplay(false)) } }));
		}

		Node currentFragment = packagedElement;
		for (Event event : diagram.events()) {
			if (event instanceof Note) {
				buildNoteEvent(packagedElement, (Note) event);
			} else if (event instanceof LifeEvent) {
				buildLifeEvent(packagedElement, (LifeEvent) event);
			} else if (event instanceof AbstractMessage) {
				buildMessage(packagedElement, currentFragment, (AbstractMessage) event);
			} else if (event instanceof Grouping) {
				currentFragment = buildGrouping(currentFragment, (Grouping) event);
			}
		}
	}

	private void buildLifeEvent(Node packagedElement, LifeEvent event) {
		if (event.getType() == LifeEventType.ACTIVATE) {
			Element execution = createUmlElement(event, "fragment", "BehaviorExecutionSpecification");
			execution.setAttribute("covered", getXmiId("lifeline", event.getParticipant()));
			execution.setAttribute("start", getLifeEventOccurrenceId(event));
			packagedElement.appendChild(execution);
			activeParticipants.putIfAbsent(event.getParticipant(), new Stack<Element>());
			activeParticipants.get(event.getParticipant()).push(execution);
		} else if (event.getType() == LifeEventType.DEACTIVATE) {
			activeParticipants.get(event.getParticipant()).pop().setAttribute("finish",
					getLifeEventOccurrenceId(event));
		}
	}

	private Participant getReceiver(AbstractMessage message) {
		if (message instanceof Message) {
			return message.getParticipant2();
		} else if (message instanceof MessageExo) {
			MessageExo exo = (MessageExo) message;
			if (exo.getType().toString().startsWith("FROM_")) {
				return message.getParticipant1();
			}
		}
		return null;
	}

	private String getLifeEventOccurrenceId(LifeEvent event) {
		if (event.getParticipant() == getReceiver(event.getMessage())) {
			return getXmiId("receiveEvent", event.getMessage());
		} else {
			return getXmiId("sendEvent", event.getMessage());
		}
	}

	private void buildMessage(Node packagedElement, Node currentFragment, AbstractMessage message) {
		if (message instanceof Message) {
			buildMessage(packagedElement, currentFragment, (Message) message);
		} else if (message instanceof MessageExo) {
			buildMessageExo(packagedElement, currentFragment, (MessageExo) message);
		}

		if (message.getParticipant1() != null) {
			covered.peek().add(getXmiId("lifeline", message.getParticipant1()));
		}
		if (message.getParticipant2() != null) {
			covered.peek().add(getXmiId("lifeline", message.getParticipant2()));
		}
	}

	private void buildNoteEvent(Node packagedElement, Note note) {
		HashSet<String> annotated = new HashSet<String>();

		if (note.getParticipant() != null) {
			annotated.add(getXmiId("lifeline", note.getParticipant()));
		}

		if (note.getParticipant2() != null) {
			annotated.add(getXmiId("lifeline", note.getParticipant2()));
		}

		buildNote(packagedElement, note, annotated);
	}

	private HashSet<String> getAnnotatedElements(Note note, Message message) {
		HashSet<String> annotated = new HashSet<String>();
		int p1 = getParticipantNumber(message.getParticipant1());
		int p2 = getParticipantNumber(message.getParticipant2());
		NotePosition senderPosition = p1 < p2 ? NotePosition.LEFT : NotePosition.RIGHT;

		if (note.getPosition() == senderPosition) {
			annotated.add(getXmiId("sendEvent", message));
			annotated.add(getXmiId("lifeline", message.getParticipant1()));
		} else {
			annotated.add(getXmiId("receiveEvent", message));
			annotated.add(getXmiId("lifeline", message.getParticipant2()));
		}
		return annotated;
	}

	private void buildNote(Node packagedElement, Note note, HashSet<String> annotated) {
		Element comment = createUmlElement(note, "ownedComment", "Comment");
		if (!annotated.isEmpty()) {
			comment.setAttribute("annotatedElement", String.join(" ", annotated));
		}
		comment.appendChild(document.createElement("body"))
				.appendChild(document.createTextNode(getDisplayString(note.getDisplay())));
		packagedElement.appendChild(comment);
	}

	private int getParticipantNumber(Participant participant) {
		return Arrays.asList(diagram.participants().toArray()).indexOf(participant);
	}

	private void buildMessageExo(Node packagedElement, Node currentFragment, MessageExo message) {
		String messageEvent = message.getType().toString().startsWith("TO_") ? "sendEvent" : "receiveEvent";
		String messageEventId = getXmiId(messageEvent, message);
		currentFragment.appendChild(createMessageOccurrence(message, messageEvent, message.getParticipant1()));
		packagedElement.appendChild(setAttributes(createUmlElement(message, "message", "Message"),
				new String[][] { { "name", getDisplayString(message.getLabel()) }, { messageEvent, messageEventId } }));

		HashSet<String> annotated = new HashSet<String>();
		annotated.add(messageEventId);
		annotated.add(getXmiId("lifeline", message.getParticipant1()));
		for (Note note : message.getNoteOnMessages()) {
			buildNote(packagedElement, note, annotated);
		}
	}

	private void buildMessage(Node packagedElement, Node currentFragment, Message message) {
		currentFragment.appendChild(createMessageOccurrence(message, "sendEvent", message.getParticipant1()));
		currentFragment.appendChild(createMessageOccurrence(message, "receiveEvent", message.getParticipant2()));
		packagedElement.appendChild(setAttributes(createUmlElement(message, "message", "Message"),
				new String[][] { { "name", getDisplayString(message.getLabel()) },
						{ "receiveEvent", getXmiId("receiveEvent", message) },
						{ "sendEvent", getXmiId("sendEvent", message) } }));

		for (Note note : message.getNoteOnMessages()) {
			buildNote(packagedElement, note, getAnnotatedElements(note, message));
		}
	}

	private Node buildGrouping(Node currentFragment, Grouping grouping) {
		if (grouping.getType() == GroupingType.START) {
			Element group = createUmlElement(grouping, "fragment", "CombinedFragment");
			group.setAttribute("interactionOperator", grouping.getTitle());
			currentFragment.appendChild(group);

			currentFragment = group.appendChild(createElement(grouping, "operand"));
			currentFragment.appendChild(createGuardElement(grouping));
			covered.push(new HashSet<String>());
		} else if (grouping.getType() == GroupingType.ELSE) {
			currentFragment = currentFragment.getParentNode().appendChild(createElement(grouping, "operand"));
			currentFragment.appendChild(createGuardElement(grouping));
		} else if (grouping.getType() == GroupingType.END) {
			Node coveredAttr = document.createAttribute("covered");
			HashSet<String> topCovered = covered.pop();
			coveredAttr.setTextContent(String.join(" ", topCovered));
			currentFragment.getParentNode().getAttributes().setNamedItem(coveredAttr);
			covered.peek().addAll(topCovered);
			currentFragment = currentFragment.getParentNode().getParentNode();
		}
		return currentFragment;
	}

	private Node createGuardElement(Grouping grouping) {
		Node guard = createElement(grouping, "guard");
		guard.appendChild(setAttribute(createUmlElement(grouping, "specification", "LiteralString"), "value",
				grouping.getComment()));
		return guard;
	}

	private Element createElement(Object object, String tag) {
		return createElement(tag, new String[][] { { "xmi:id", getXmiId(tag, object) } });
	}

	private Element createElement(Object object, String tag, String[][] attributes) {
		return setAttributes(createElement(tag, new String[][] { { "xmi:id", getXmiId(tag, object) } }), attributes);
	}

	private Element createUmlElement(Object object, String tag, String type) {
		return createElement(tag,
				new String[][] { { "xmi:type", "uml:" + type }, { "xmi:id", getXmiId(type, object) } });
	}

	private Node createMessageOccurrence(Object event, String type, Participant participant) {
		return setAttributes(createUmlElement(event, "fragment", "MessageOccurrenceSpecification"),
				new String[][] { { "xmi:id", getXmiId(type, event) }, { "covered", getXmiId("lifeline", participant) },
						{ "message", getXmiId("Message", event) }, });
	}
}
