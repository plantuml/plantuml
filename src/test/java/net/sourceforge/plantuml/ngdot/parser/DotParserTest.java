package net.sourceforge.plantuml.ngdot.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.ngdot.model.DotEdge;
import net.sourceforge.plantuml.ngdot.model.DotGraph;
import net.sourceforge.plantuml.ngdot.model.DotSubgraph;

class DotParserTest {

	private static String edges(DotGraph graph) {
		final StringBuilder sb = new StringBuilder();
		for (DotEdge edge : graph.getEdges()) {
			if (sb.length() > 0)
				sb.append(' ');
			sb.append(edge);
		}
		return sb.toString();
	}

	private static String color(DotGraph graph, String node) {
		return graph.getNode(node).getAttributes().getString("color");
	}

	private static DotSubgraph subgraph(DotGraph graph, String id) {
		for (DotSubgraph subgraph : graph.getAllSubgraphs())
			if (subgraph.getId().equals(id))
				return subgraph;
		throw new IllegalArgumentException(id);
	}

	private static int errorLine(String text) {
		return assertThrows(DotSyntaxException.class, () -> DotParser.parse(text)).getLine();
	}

	@Test
	void simpleDigraph() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph G { a -> b -> c; a -> c }");
		assertTrue(graph.isDirected());
		assertFalse(graph.isStrict());
		assertEquals("G", graph.getId());
		assertEquals("[a, b, c]", graph.getNodes().toString());
		assertEquals("a->b b->c a->c", edges(graph));
	}

	@Test
	void undirectedAndAnonymous() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("graph { a -- b }");
		assertFalse(graph.isDirected());
		assertNull(graph.getId());
		assertEquals(1, graph.getEdges().size());
	}

	@Test
	void quotedGraphNameAndConcatenation() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph \"my graph\" { \"ab\" + \"cd\" -> e [label=\"x\" + \"y\"] }");
		assertEquals("my graph", graph.getId());
		assertEquals("[abcd, e]", graph.getNodes().toString());
		assertEquals("xy", graph.getEdges().get(0).getAttributes().getString("label"));
	}

	@Test
	void edgeChainSharesAttributes() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { a -> b -> c [color=red, style=dashed; penwidth=2] }");
		for (DotEdge edge : graph.getEdges()) {
			assertEquals("red", edge.getAttributes().getString("color"));
			assertEquals("dashed", edge.getAttributes().getString("style"));
			assertEquals("2", edge.getAttributes().getString("penwidth"));
		}
	}

	@Test
	void attributeWithoutValue() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { a [bold] }");
		assertEquals("true", graph.getNode("a").getAttributes().getString("bold"));
	}

	@Test
	void htmlLabel() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { a [label=<<b>A</b>>]; b [label=\"<b>\"] }");
		assertTrue(graph.getNode("a").getAttributes().get("label").isHtml());
		assertEquals("<b>A</b>", graph.getNode("a").getAttributes().getString("label"));
		assertFalse(graph.getNode("b").getAttributes().get("label").isHtml());
	}

	@Test
	void strictGraphMergesMultiEdges() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("strict digraph { a -> b [color=red]; a -> b [style=bold]; b -> a; a -> a }");
		assertEquals("a->b b->a a->a", edges(graph));
		assertEquals("red", graph.getEdges().get(0).getAttributes().getString("color"));
		assertEquals("bold", graph.getEdges().get(0).getAttributes().getString("style"));

		final DotGraph undirected = DotParser.parse("strict graph { a -- b; b -- a }");
		assertEquals(1, undirected.getEdges().size());

		final DotGraph notStrict = DotParser.parse("digraph { a -> b; a -> b }");
		assertEquals(2, notStrict.getEdges().size());
	}

	@Test
	void nodeDefaultsApplyOnlyToLaterNodes() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { a; node [color=red]; b; a; c -> a }");
		assertNull(color(graph, "a"));
		assertEquals("red", color(graph, "b"));
		assertEquals("red", color(graph, "c"));
	}

	@Test
	void explicitAttributesOverrideDefaults() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { node [color=red, shape=box]; a [color=blue]; a [shape=circle] }");
		assertEquals("blue", color(graph, "a"));
		assertEquals("circle", graph.getNode("a").getAttributes().getString("shape"));
	}

	@Test
	void edgeDefaults() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { edge [color=blue]; a -> b; edge [color=green]; c -> d [color=red]; e -> f }");
		assertEquals("blue", graph.getEdges().get(0).getAttributes().getString("color"));
		assertEquals("red", graph.getEdges().get(1).getAttributes().getString("color"));
		assertEquals("green", graph.getEdges().get(2).getAttributes().getString("color"));
	}

	@Test
	void subgraphDefaultsDoNotLeak() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { subgraph s { node [color=red]; a } b }");
		assertEquals("red", color(graph, "a"));
		assertNull(color(graph, "b"));
	}

	@Test
	void subgraphDefaultsAreAViewOverTheParent() throws DotSyntaxException {
		// As in cgraph, b is created in s after the root default changed: it sees it
		final DotGraph graph = DotParser.parse("digraph { subgraph s { a } node [color=red]; subgraph s { b } }");
		assertNull(color(graph, "a"));
		assertEquals("red", color(graph, "b"));
		assertEquals(1, graph.getRoot().getChildren().size());
	}

	@Test
	void graphAttributesAreCopiedWhenASubgraphIsCreated() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse(
				"digraph { label=\"Title\"; subgraph cluster_a { x } graph [color=red]; subgraph cluster_b { y; label=\"B\" } }");
		assertEquals("Title", subgraph(graph, "cluster_a").getAttributes().getString("label"));
		assertNull(subgraph(graph, "cluster_a").getAttributes().getString("color"));
		assertEquals("B", subgraph(graph, "cluster_b").getAttributes().getString("label"));
		assertEquals("red", subgraph(graph, "cluster_b").getAttributes().getString("color"));
		assertEquals("Title", graph.getAttributes().getString("label"));
	}

	@Test
	void clusters() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { subgraph cluster_a { x } subgraph Cluster_b { y } subgraph s { z } }");
		assertTrue(subgraph(graph, "cluster_a").isCluster());
		// Case-sensitive, as in Graphviz 2.38
		assertFalse(subgraph(graph, "Cluster_b").isCluster());
		assertFalse(subgraph(graph, "s").isCluster());
		assertFalse(graph.getRoot().isCluster());
	}

	@Test
	void nodeMembership() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { subgraph cluster_a { x; subgraph cluster_b { y } } x -> z; subgraph cluster_c { z; x } }");
		final DotSubgraph a = subgraph(graph, "cluster_a");
		final DotSubgraph c = subgraph(graph, "cluster_c");
		assertEquals("[x, y]", a.getNodes().toString());
		assertEquals("[y]", subgraph(graph, "cluster_b").getNodes().toString());
		// Listed in creation order, not in the order they entered the subgraph
		assertEquals("[x, z]", c.getNodes().toString());
		assertSame(a, graph.getNode("x").getOwner());
		assertSame(graph.getRoot(), graph.getNode("z").getOwner());
		// The edge was written at the top level
		assertEquals(0, c.getEdges().size());
		assertEquals(1, graph.getRoot().getEdges().size());
	}

	@Test
	void edgesToSubgraphs() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { a -> {b c}; {d e} -> {f g} }");
		assertEquals("a->b a->c d->f d->g e->f e->g", edges(graph));
		assertEquals("[a, b, c, d, e, f, g]", graph.getNodes().toString());
	}

	@Test
	void sameRankSubgraph() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { {rank=same; a; b} {rank=same b c} }");
		final List<DotSubgraph> children = graph.getRoot().getChildren();
		assertEquals(2, children.size());
		assertTrue(children.get(0).isAnonymous());
		assertEquals("%1", children.get(0).getId());
		assertEquals("%2", children.get(1).getId());
		assertEquals("same", children.get(0).getAttributes().getString("rank"));
		assertEquals("[b, c]", children.get(1).getNodes().toString());
	}

	@Test
	void ports() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { a:p1:ne -> b:s [tailport=w, headport=n]; c -> d [headport=e]; e:f0 }");
		final DotEdge first = graph.getEdges().get(0);
		assertEquals("p1:ne", first.getTailPort());
		assertEquals("s", first.getHeadPort());
		assertEquals("e", graph.getEdges().get(1).getHeadPort());
		assertNull(graph.getEdges().get(1).getTailPort());
		assertEquals("[a, b, c, d, e]", graph.getNodes().toString());
	}

	@Test
	void keywordsCanBeQuoted() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("digraph { \"node\" -> \"edge\" }");
		assertEquals("[node, edge]", graph.getNodes().toString());
	}

	@Test
	void numericNodeIds() throws DotSyntaxException {
		final DotGraph graph = DotParser.parse("graph { 1 -- 2.5 -- -3 }");
		assertEquals("[1, 2.5, -3]", graph.getNodes().toString());
	}

	@Test
	void emptyGraphAndSemicolons() throws DotSyntaxException {
		assertEquals(0, DotParser.parse("digraph {}").getNodes().size());
		assertEquals(2, DotParser.parse("digraph { a ; b ; }").getNodes().size());
		// As in Graphviz, a statement list cannot start with ';' nor hold ';;'
		assertThrows(DotSyntaxException.class, () -> DotParser.parse("digraph { ; a }"));
		assertThrows(DotSyntaxException.class, () -> DotParser.parse("digraph { a ;; b }"));
	}

	@Test
	void errors() {
		assertEquals(2, errorLine("digraph {\n a -> b\n c -- d\n}"));
		assertEquals(1, errorLine("graph {\n a -> b }"));
		assertEquals(2, errorLine("digraph {\n a -> b\n"));
		assertEquals(0, errorLine("digraph { a -> }"));
		assertEquals(0, errorLine("digraph { node color=red }"));
		assertEquals(1, errorLine("digraph { a }\ndigraph { b }"));
		assertEquals(0, errorLine("a -> b"));
		assertEquals(0, errorLine("digraph { a [label=] }"));
		assertEquals(0, errorLine("digraph { \"a\" + b }"));
	}

}
