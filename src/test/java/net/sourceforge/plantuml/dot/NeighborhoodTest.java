package net.sourceforge.plantuml.dot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;

/**
 * With {@code groupInheritance}, the visible class is drawn inside a padded node,
 * and {@link Neighborhood} extends every other link of that class from the
 * visible shape to the point where Graphviz made it touch the padded node.
 * <p>
 * The contact points are given here directly, so no Graphviz is needed.
 */
class NeighborhoodTest {

	private static final double DELTA = 0.001;

	/** The visible class: center (50, 20). */
	private static final XRectangle2D RECT = new XRectangle2D(0, 0, 100, 40);

	/** On the right of the visible class. */
	private static final XPoint2D START = new XPoint2D(130, 10);

	/** Below the visible class. */
	private static final XPoint2D END = new XPoint2D(50, 70);

	private static final Neighborhood.ContactPoints CONTACTS = new Neighborhood.ContactPoints() {
		public XPoint2D start(Link link) {
			return START;
		}

		public XPoint2D end(Link link) {
			return END;
		}
	};

	// https://github.com/plantuml/plantuml/issues/3023
	@Test
	void self_link_is_connected_at_both_ends() {
		final List<XPoint2D[]> connectors = connectorsOfUsuario("Usuario \"0..*\" -- \"0..*\" Usuario");

		assertEquals(2, connectors.size());
		assertConnector(connectors.get(0), new XPoint2D(100, 13.75), START);
		assertConnector(connectors.get(1), new XPoint2D(50, 40), END);
	}

	@Test
	void link_starting_from_the_leaf_uses_its_start_point() {
		final List<XPoint2D[]> connectors = connectorsOfUsuario("Usuario -- Other");

		assertEquals(1, connectors.size());
		assertConnector(connectors.get(0), new XPoint2D(100, 13.75), START);
	}

	@Test
	void link_ending_on_the_leaf_uses_its_end_point() {
		final List<XPoint2D[]> connectors = connectorsOfUsuario("Other -- Usuario");

		assertEquals(1, connectors.size());
		assertConnector(connectors.get(0), new XPoint2D(50, 40), END);
	}

	@Test
	void missing_contact_point_is_ignored() {
		final Neighborhood neighborhood = neighborhoodOfUsuario("Usuario -- Usuario");
		final List<XPoint2D[]> connectors = neighborhood.linkConnectors(RECT, new Neighborhood.ContactPoints() {
			public XPoint2D start(Link link) {
				return null;
			}

			public XPoint2D end(Link link) {
				return END;
			}
		});

		assertEquals(1, connectors.size());
		assertConnector(connectors.get(0), new XPoint2D(50, 40), END);
	}

	private static List<XPoint2D[]> connectorsOfUsuario(String link) {
		return neighborhoodOfUsuario(link).linkConnectors(RECT, CONTACTS);
	}

	private static Neighborhood neighborhoodOfUsuario(String link) {
		final String source = String.join("\n", //
				"@startuml", //
				"skinparam groupInheritance 2", //
				"Usuario <|-- Cliente", //
				"Usuario <|-- Administrador", //
				link, //
				"@enduml");
		final CucaDiagram diagram = (CucaDiagram) new SourceStringReader(source).getBlocks().get(0).getDiagram();
		final Entity usuario = diagram.firstWithName("Usuario").getData();

		final List<Link> inheritances = new ArrayList<>();
		final List<Link> all = new ArrayList<>();
		for (Link each : diagram.getLinks()) {
			if (each.contains(usuario) == false)
				continue;
			all.add(each);
			if (each.getType().getDecor2().isExtendsLike())
				inheritances.add(each);
		}
		assertEquals(2, inheritances.size(), "the two grouped inheritances");
		return new Neighborhood(usuario, inheritances, all);
	}

	private static void assertConnector(XPoint2D[] connector, XPoint2D border, XPoint2D contact) {
		assertEquals(border.getX(), connector[0].getX(), DELTA, "border x");
		assertEquals(border.getY(), connector[0].getY(), DELTA, "border y");
		assertEquals(contact.getX(), connector[1].getX(), DELTA, "contact x");
		assertEquals(contact.getY(), connector[1].getY(), DELTA, "contact y");
	}

}
