/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2026, Jamison Jiang
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
 * Original Author:  Jamison Jiang
 *
 *
 */
package net.sourceforge.plantuml.svek;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.dot.CucaDiagramSimplifierActivity;
import net.sourceforge.plantuml.dot.CucaDiagramSimplifierState;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.dot.GraphvizVersion;
import net.sourceforge.plantuml.dot.GraphvizVersionFinder;
import net.sourceforge.plantuml.graphsupport.GraphSupportSvekLayoutBuilder;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResponse;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult;

/**
 * Renders, and lays out without rendering, while Graphviz is unreachable.
 *
 * Every source built by the compatibility fixtures pins {@code !pragma layout graph-support}, so
 * there is no automatic Smetana retry: a diagram that renders without the "Dot Executable" error
 * image can only have been laid out by graph-support.
 */
final class GraphSupportIntegrationTestSupport implements AutoCloseable {

	static final String GRAPHVIZ_RESOURCE = "net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment";

	private final GraphvizState graphvizState;

	GraphSupportIntegrationTestSupport() throws Exception {
		this.graphvizState = GraphvizState.capture();
		graphvizState.environment.setDotExecutable("/guaranteed-missing/graph-support-compatibility/dot");
	}

	String render(String source) throws Exception {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		new SourceStringReader(source).outputImage(output, 0, new FileFormatOption(FileFormat.SVG));
		return new String(output.toByteArray(), UTF_8);
	}

	/**
	 * Produces the layout result of the top-level graph, which a render consumes and then discards.
	 *
	 * The Svek model is assembled exactly as {@link CucaDiagramFileMakerSvek} assembles it, autarkic
	 * activity and state groups turned into images first, and is then handed to the same engine the
	 * render uses, so the returned geometry is the geometry the render draws with.
	 */
	SvekLayoutResult layout(String source) throws Exception {
		final CucaDiagram diagram = (CucaDiagram) new SourceStringReader(source).getBlocks().get(0).getDiagram();
		final StringBounder stringBounder = new FileFormatOption(FileFormat.SVG)
				.getDefaultStringBounder(diagram.getSkinParam(), diagram.getPragma());
		if (diagram.getDiagramType() == DiagramType.ACTIVITY)
			new CucaDiagramSimplifierActivity().simplify(diagram, stringBounder, DotMode.NORMAL);
		else if (diagram.getDiagramType() == DiagramType.STATE)
			new CucaDiagramSimplifierState().simplify(diagram, stringBounder, DotMode.NORMAL);

		final List<Link> links = new ArrayList<>(diagram.getLinks());
		final Bibliotekon bibliotekon = new Bibliotekon(links);
		final Cluster root = new Cluster(diagram.getRootGroup().getLocation(), diagram, bibliotekon.getColorSequence(),
				diagram.getRootGroup());
		final ClusterManager clusterManager = new ClusterManager(bibliotekon, root);
		final DotStringFactory factory = new DotStringFactory(bibliotekon, root, diagram.getDiagramType(),
				diagram.getSkinParam(), GraphvizVersionFinder.DEFAULT);
		final DotData dotData = new DotData(diagram, diagram.getRootGroup(), links, diagram.leafs(), diagram, diagram);
		new GraphvizImageBuilder(dotData, diagram.getSource(), diagram.getPragma(),
				diagram.getDiagramType().getStyleName(), DotMode.NORMAL, factory, clusterManager)
						.buildModel(stringBounder);

		final SvekLayoutResponse response = new SvekLayoutEmitter(factory, diagram.getDiagramType(), stringBounder)
				.emit(new GraphSupportSvekLayoutBuilder());
		assertTrue(response.isSuccess(), response.declineCode + ": " + response.declineMessage);
		return response.result;
	}

	static Object getGraphvizField(String name) throws Exception {
		return getField(GraphvizRuntimeEnvironment.getInstance(), name);
	}

	static void setGraphvizField(String name, Object value) throws Exception {
		setField(GraphvizRuntimeEnvironment.getInstance(), name, value);
	}

	public void close() throws Exception {
		graphvizState.restore();
	}

	private static final class GraphvizState {
		private final GraphvizRuntimeEnvironment environment;
		private final String dotExecutable;
		private final String dotVersion;
		private final Map<File, GraphvizVersion> versions;

		private GraphvizState(GraphvizRuntimeEnvironment environment, String dotExecutable, String dotVersion,
				Map<File, GraphvizVersion> versions) {
			this.environment = environment;
			this.dotExecutable = dotExecutable;
			this.dotVersion = dotVersion;
			this.versions = versions;
		}

		private static GraphvizState capture() throws Exception {
			final GraphvizRuntimeEnvironment environment = GraphvizRuntimeEnvironment.getInstance();
			return new GraphvizState(environment, getField(environment, "dotExecutable"),
					getField(environment, "dotVersion"),
					new HashMap<>(GraphSupportIntegrationTestSupport
							.<Map<File, GraphvizVersion>>getField(environment, "map")));
		}

		private void restore() throws Exception {
			setField(environment, "dotExecutable", dotExecutable);
			setField(environment, "dotVersion", dotVersion);
			final Map<File, GraphvizVersion> cache = getField(environment, "map");
			cache.clear();
			cache.putAll(versions);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T getField(Object owner, String name) throws Exception {
		final Field field = owner.getClass().getDeclaredField(name);
		field.setAccessible(true);
		return (T) field.get(owner);
	}

	private static void setField(Object owner, String name, Object value) throws Exception {
		final Field field = owner.getClass().getDeclaredField(name);
		field.setAccessible(true);
		field.set(owner, value);
	}

}
