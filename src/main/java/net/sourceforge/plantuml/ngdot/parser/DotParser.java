/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.ngdot.parser;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ngdot.model.DotAttributes;
import net.sourceforge.plantuml.ngdot.model.DotGraph;
import net.sourceforge.plantuml.ngdot.model.DotGraphBuilder;
import net.sourceforge.plantuml.ngdot.model.DotNode;
import net.sourceforge.plantuml.ngdot.model.DotSubgraph;
import net.sourceforge.plantuml.ngdot.model.DotValue;
import net.sourceforge.plantuml.ngdot.parser.DotToken.IdKind;

/**
 * Recursive descent parser for the DOT language
 * (https://graphviz.org/doc/info/lang.html):
 *
 * <pre>
 * graph     : [strict] (graph|digraph) [ID] '{' stmt_list '}'
 * stmt_list : [stmt [';'] stmt_list]
 * stmt      : node_stmt | edge_stmt | attr_stmt | ID '=' ID | subgraph
 * attr_stmt : (graph|node|edge) attr_list
 * attr_list : '[' [a_list] ']' [attr_list]
 * a_list    : ID ['=' ID] [(';'|',')] [a_list]
 * edge_stmt : (node_id|subgraph) edgeRHS [attr_list]
 * edgeRHS   : edgeop (node_id|subgraph) [edgeRHS]
 * node_stmt : node_id [attr_list]
 * node_id   : ID [':' ID [':' ID]]
 * subgraph  : [subgraph [ID]] '{' stmt_list '}'
 * ID        : ... | quoted_string ('+' quoted_string)*
 * </pre>
 *
 * An attribute written without a value ({@code [bold]}) is read as
 * {@code bold=true}. The semantics (defaults, node creation, strict graphs) is
 * delegated to {@link DotGraphBuilder}.
 */
public final class DotParser {

	private final List<DotToken> tokens;
	private int pos;
	private DotGraphBuilder builder;

	private DotParser(List<DotToken> tokens) {
		this.tokens = tokens;
	}

	public static DotGraph parse(String text) throws DotSyntaxException {
		return new DotParser(DotLexer.tokenize(text)).graph();
	}

	private DotToken peek() {
		return tokens.get(pos);
	}

	private DotToken peek(int offset) {
		final int i = Math.min(pos + offset, tokens.size() - 1);
		return tokens.get(i);
	}

	private boolean at(DotTokenType type) {
		return peek().getType() == type;
	}

	private DotToken next() {
		final DotToken result = tokens.get(pos);
		if (result.getType() != DotTokenType.EOF)
			pos++;
		return result;
	}

	private boolean accept(DotTokenType type) {
		if (at(type)) {
			next();
			return true;
		}
		return false;
	}

	private DotToken expect(DotTokenType type, String what) throws DotSyntaxException {
		if (at(type))
			return next();
		throw error("Expected " + what + " but found " + peek().describe(), peek());
	}

	private static DotSyntaxException error(String message, DotToken token) {
		return new DotSyntaxException(message, token.getLine(), token.getColumn());
	}

	private DotGraph graph() throws DotSyntaxException {
		final boolean strict = accept(DotTokenType.STRICT);
		final boolean directed;
		if (accept(DotTokenType.DIGRAPH))
			directed = true;
		else if (accept(DotTokenType.GRAPH))
			directed = false;
		else
			throw error("Expected 'graph' or 'digraph' but found " + peek().describe(), peek());

		String id = null;
		if (at(DotTokenType.ID))
			id = readId().getText();

		expect(DotTokenType.LBRACE, "'{'");
		builder = new DotGraphBuilder(strict, directed, id);
		stmtList();
		expect(DotTokenType.RBRACE, "'}'");
		if (at(DotTokenType.EOF) == false)
			throw error("Only one graph is allowed, found " + peek().describe() + " after its closing '}'", peek());

		return builder.build();
	}

	private void stmtList() throws DotSyntaxException {
		while (at(DotTokenType.RBRACE) == false && at(DotTokenType.EOF) == false) {
			stmt();
			accept(DotTokenType.SEMICOLON);
		}
	}

	private void stmt() throws DotSyntaxException {
		final DotToken token = peek();
		switch (token.getType()) {
		case GRAPH:
		case NODE:
		case EDGE:
			attrStmt();
			return;
		case SUBGRAPH:
		case LBRACE:
			final DotSubgraph subgraph = subgraph();
			if (isEdgeOp())
				edgeStmt(new Endpoint(subgraph));
			return;
		case ID:
			if (peek(1).getType() == DotTokenType.EQUAL) {
				final String name = next().getText();
				next();
				builder.setGraphAttribute(name, readValue());
				return;
			}
			final Endpoint first = nodeId();
			if (isEdgeOp()) {
				edgeStmt(first);
			} else {
				final DotAttributes attributes = attrListOptional();
				builder.setNodeAttributes(first.node, attributes);
			}
			return;
		default:
			throw error("Unexpected " + token.describe(), token);
		}
	}

	private void attrStmt() throws DotSyntaxException {
		final DotToken keyword = next();
		if (at(DotTokenType.LBRACKET) == false)
			throw error("Expected '[' after '" + keyword.getText() + "' but found " + peek().describe(), peek());
		final DotAttributes attributes = attrListOptional();
		for (String name : attributes.names()) {
			final DotValue value = attributes.get(name);
			if (keyword.getType() == DotTokenType.GRAPH)
				builder.setGraphAttribute(name, value);
			else if (keyword.getType() == DotTokenType.NODE)
				builder.setNodeDefault(name, value);
			else
				builder.setEdgeDefault(name, value);
		}
	}

	private DotAttributes attrListOptional() throws DotSyntaxException {
		final DotAttributes result = new DotAttributes();
		while (accept(DotTokenType.LBRACKET)) {
			while (accept(DotTokenType.RBRACKET) == false) {
				if (at(DotTokenType.ID) == false)
					throw error("Expected an attribute name but found " + peek().describe(), peek());
				final String name = readId().getText();
				final DotValue value;
				if (accept(DotTokenType.EQUAL))
					value = readValue();
				else
					value = DotValue.TRUE;
				result.put(name, value);
				if (accept(DotTokenType.SEMICOLON) == false)
					accept(DotTokenType.COMMA);
			}
		}
		return result;
	}

	private DotSubgraph subgraph() throws DotSyntaxException {
		String id = null;
		if (accept(DotTokenType.SUBGRAPH)) {
			if (at(DotTokenType.ID))
				id = readId().getText();
		}
		expect(DotTokenType.LBRACE, "'{'");
		final DotSubgraph result = builder.openSubgraph(id);
		stmtList();
		expect(DotTokenType.RBRACE, "'}'");
		builder.closeSubgraph();
		return result;
	}

	private boolean isEdgeOp() {
		return at(DotTokenType.ARROW) || at(DotTokenType.LINE);
	}

	private void checkEdgeOp(DotToken op) throws DotSyntaxException {
		if (builder.isDirected() && op.getType() == DotTokenType.LINE)
			throw error("'--' is not allowed in a digraph, use '->'", op);
		if (builder.isDirected() == false && op.getType() == DotTokenType.ARROW)
			throw error("'->' is not allowed in an undirected graph, use '--' (or declare a digraph)", op);
	}

	private void edgeStmt(Endpoint first) throws DotSyntaxException {
		final List<Endpoint> endpoints = new ArrayList<Endpoint>();
		endpoints.add(first);
		while (isEdgeOp()) {
			checkEdgeOp(next());
			if (at(DotTokenType.SUBGRAPH) || at(DotTokenType.LBRACE))
				endpoints.add(new Endpoint(subgraph()));
			else if (at(DotTokenType.ID))
				endpoints.add(nodeId());
			else
				throw error("Expected a node or a subgraph but found " + peek().describe(), peek());
		}
		final DotAttributes attributes = attrListOptional();

		for (int i = 0; i < endpoints.size() - 1; i++) {
			final Endpoint tail = endpoints.get(i);
			final Endpoint head = endpoints.get(i + 1);
			for (DotNode t : tail.nodes())
				for (DotNode h : head.nodes())
					builder.addEdge(t, tail.port, h, head.port, attributes);
		}
	}

	private Endpoint nodeId() throws DotSyntaxException {
		final DotToken id = readId();
		String port = null;
		if (accept(DotTokenType.COLON)) {
			port = expectId("a port name").getText();
			if (accept(DotTokenType.COLON))
				port = port + ":" + expectId("a compass point").getText();
		}
		return new Endpoint(builder.mentionNode(id.getText()), port);
	}

	private DotToken expectId(String what) throws DotSyntaxException {
		if (at(DotTokenType.ID))
			return readId();
		throw error("Expected " + what + " but found " + peek().describe(), peek());
	}

	private DotValue readValue() throws DotSyntaxException {
		final DotToken token = expectId("a value");
		if (token.getIdKind() == IdKind.HTML)
			return DotValue.html(token.getText());
		return DotValue.of(token.getText());
	}

	/**
	 * Reads an ID, joining {@code "a" + "b"} concatenations.
	 */
	private DotToken readId() throws DotSyntaxException {
		final DotToken first = next();
		if (first.getIdKind() != IdKind.QUOTED || at(DotTokenType.PLUS) == false)
			return first;

		final StringBuilder sb = new StringBuilder(first.getText());
		while (accept(DotTokenType.PLUS)) {
			final DotToken other = peek();
			if (other.getIdKind() != IdKind.QUOTED)
				throw error("Expected a double-quoted string after '+' but found " + other.describe(), other);
			sb.append(next().getText());
		}
		return new DotToken(DotTokenType.ID, sb.toString(), IdKind.QUOTED, first.getLine(), first.getColumn());
	}

	/**
	 * One side of an edge: a node (with an optional port) or a subgraph, which
	 * stands for every node it contains when the edges are created.
	 */
	private static final class Endpoint {

		private final DotNode node;
		private final String port;
		private final DotSubgraph subgraph;

		Endpoint(DotNode node, String port) {
			this.node = node;
			this.port = port;
			this.subgraph = null;
		}

		Endpoint(DotSubgraph subgraph) {
			this.node = null;
			this.port = null;
			this.subgraph = subgraph;
		}

		List<DotNode> nodes() {
			if (subgraph != null)
				return subgraph.getNodes();
			final List<DotNode> result = new ArrayList<DotNode>();
			result.add(node);
			return result;
		}

	}

}
