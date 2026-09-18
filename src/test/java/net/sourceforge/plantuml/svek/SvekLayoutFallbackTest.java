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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.dot.GraphvizVersion;
import net.sourceforge.plantuml.dot.GraphvizVersionFinder;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutBuilder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.GraphSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.RankSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResponse;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Bounds;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.ClusterGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.EdgeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.NodeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Path;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Point;
import net.sourceforge.plantuml.svek.layout.SvekLayoutValidation;
import test.utils.PlantUmlTestUtils;

@ResourceLock(value = "net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment", mode = ResourceAccessMode.READ_WRITE)
class SvekLayoutFallbackTest {

	@ParameterizedTest
	@ValueSource(strings = {
			"class A\nclass B\nA --> B",
			"state Outer {\nstate Inner {\nA --> B\n}\nInner --> C\n}\nOuter --> D",
			"(*) --> {\n(*) --> First\nFirst --> Second\n}\n--> Last\nLast --> (*)"
	})
	void automaticAndExplicitGraphSupportRenderIncludingNestedLayoutsWithoutDot(String body) throws Exception {
		final GraphvizState state = GraphvizState.capture();
		try {
			state.environment.setDotExecutable("/guaranteed-missing/svek-selection-test/dot");
			setField(state.environment, "dotVersion", "dot - graphviz version 2.44.0 (cached)");
			for (String pragma : asList("", "!pragma layout graph-support")) {
				final PlantUmlTestUtils.ExportDiagram export = PlantUmlTestUtils
						.exportDiagram("@startuml", pragma, body, "@enduml").assertNoError();
				final CucaDiagram diagram = (CucaDiagram) export.getDiagram();
				final String svg = export.asString(FileFormat.SVG);

				assertTrue(svg.contains("<svg"));
				assertTrue(svg.contains("<path"));
				assertFalse(svg.contains("Dot Executable"));
				assertFalse(svg.contains("layout declined"));
				// Graphviz is unreachable, so only graph-support can have produced these layouts,
				// nested groups included: a group that declined would have published a decline
				// warning, shown the Graphviz error image, or - when automatic - asked for Smetana.
				assertTrue(diagram.isUseGraphSupport());
				assertFalse(automaticGraphSupportFailed(diagram));
			}
		} finally {
			state.restore();
		}
	}

	@Test
	void defaultLayoutUsesNativeDotWhenAvailable() throws Exception {
		assumeGraphvizAvailable();
		final PlantUmlTestUtils.ExportDiagram export = PlantUmlTestUtils.exportDiagram("@startuml", "class A",
				"class B", "A --> B", "@enduml").assertNoError();
		final CucaDiagram diagram = (CucaDiagram) export.getDiagram();
		assertFalse(diagram.isUseGraphSupport());

		final String svg = export.asString(FileFormat.SVG);

		assertTrue(svg.contains("<svg"));
		assertTrue(svg.contains("<path"));
		assertFalse(svg.contains("Dot Executable"));
		// graph-support was never selected, so no neutral layout result was ever applied and no
		// automatic retry was ever requested.
		assertFalse(automaticGraphSupportFailed(diagram));
	}

	@Test
	void providerSuccessReturnsSvekWithoutCheckingDot() throws Exception {
		final GraphvizState state = GraphvizState.capture();
		try {
			state.environment.setDotExecutable("/guaranteed-missing/svek-provider-test/dot");
			final CountingGraphvizOperations graphviz = new CountingGraphvizOperations() {
				public boolean isAvailable(DotStringFactory factory) {
					throw new AssertionError("a successful graph-support layout must not check Graphviz");
				}
			};
			final Fixture fixture = fixture(successProvider(), true, graphviz, null);

			assertInstanceOf(SvekResult.class, fixture.build());
			assertEquals(0, graphviz.getSvgCalls);
			assertIterableEquals(Collections.<String>emptyList(), warnings(fixture));
		} finally {
			state.restore();
		}
	}

	@Test
	void providerDeclineFallsBackToGraphviz() throws Exception {
		assumeGraphvizAvailable();
		final Fixture fixture = fixture(decliningProvider(), true);

		assertInstanceOf(SvekResult.class, fixture.build());
		assertIterableEquals(
				Collections.singletonList("graph-support layout declined; using Graphviz: fixture declined"),
				warnings(fixture));
	}

	@Test
	void providerRuntimeExceptionFallsBackToGraphviz() throws Exception {
		assumeGraphvizAvailable();
		final Fixture fixture = fixture(new Builder() {
			public SvekLayoutResponse layout() {
				throw new IllegalStateException("fixture exploded");
			}
		}, true);

		assertInstanceOf(SvekResult.class, fixture.build());
		assertIterableEquals(
				Collections.singletonList("graph-support layout declined; using Graphviz: fixture exploded"),
				warnings(fixture));
	}

	@Test
	void providerLinkageErrorFallsBackToGraphviz() throws Exception {
		assumeGraphvizAvailable();
		final Fixture fixture = fixture(new Builder() {
			public SvekLayoutResponse layout() {
				throw new NoClassDefFoundError("missing-layout-class");
			}
		}, true);

		assertInstanceOf(SvekResult.class, fixture.build());
		assertIterableEquals(
				Collections.singletonList("graph-support layout declined; using Graphviz: missing-layout-class"),
				warnings(fixture));
	}

	@Test
	void virtualMachineErrorIsNotSwallowed() throws Exception {
		final Fixture fixture = fixture(new Builder() {
			public SvekLayoutResponse layout() {
				throw new OutOfMemoryError("fatal");
			}
		}, true);

		assertEquals("fatal", assertThrows(OutOfMemoryError.class, fixture::build).getMessage());
	}

	@Test
	void automaticVirtualMachineErrorIsNeitherRetriedNorTurnedIntoSmetanaFallback() throws Exception {
		final CountingGraphvizOperations graphviz = new CountingGraphvizOperations();
		final Fixture fixture = fixture(new Builder() {
			public SvekLayoutResponse layout() {
				throw new OutOfMemoryError("fatal");
			}
		}, true, graphviz, null, null, "@startuml\nclass A\nclass B\nA --> B\n@enduml", null, false, true);

		assertEquals("fatal", assertThrows(OutOfMemoryError.class, fixture::build).getMessage());
		assertEquals(0, graphviz.getSvgCalls);
		assertIterableEquals(Collections.<String>emptyList(), warnings(fixture));
		assertEquals("previous Svek layout failed",
				assertThrows(IllegalStateException.class, fixture::build).getMessage());
	}

	@Test
	void applyFailurePropagatesWithoutGraphvizFallback() throws Exception {
		final CountingGraphvizOperations graphviz = new CountingGraphvizOperations();
		final Fixture fixture = fixture(successProvider(), true, graphviz,
				new GraphvizImageBuilder.LayoutApplierFactory() {
					public GraphvizImageBuilder.LayoutApplier create(DotStringFactory factory) {
						return new GraphvizImageBuilder.LayoutApplier() {
							public GraphvizImageBuilder.PreparedLayout prepare(SvekLayoutResult result) {
								return new GraphvizImageBuilder.PreparedLayout() {
									public SvekLayoutValidation getValidation() {
										return SvekLayoutValidation.valid();
									}

									public void apply() {
										throw new IllegalStateException("apply failed");
									}
								};
							}
						};
					}
				});

		assertEquals("apply failed", assertThrows(IllegalStateException.class, fixture::build).getMessage());
		assertEquals(0, graphviz.getSvgCalls);
		assertIterableEquals(Collections.<String>emptyList(), warnings(fixture));
		assertEquals("previous Svek layout failed",
				assertThrows(IllegalStateException.class, fixture::build).getMessage());
	}

	@Test
	void preparationFailureFallsBackWithoutMutatingSvekModel() throws Exception {
		final CountingGraphvizOperations graphviz = new CountingGraphvizOperations();
		final boolean[] unchanged = new boolean[1];
		final Fixture fixture = fixture(successProvider(), true, graphviz,
				new GraphvizImageBuilder.LayoutApplierFactory() {
					public GraphvizImageBuilder.LayoutApplier create(final DotStringFactory factory) {
						return new GraphvizImageBuilder.LayoutApplier() {
							public GraphvizImageBuilder.PreparedLayout prepare(SvekLayoutResult result) {
								unchanged[0] = true;
								for (SvekNode node : factory.getBibliotekon().allNodes())
									unchanged[0] &= node.getPosition().equals(new net.sourceforge.plantuml.klimt.geom.XPoint2D(0, 0));
								throw new IllegalStateException("conversion failed");
							}
						};
					}
				});

		assertInstanceOf(SvekResult.class, fixture.build());
		assertTrue(unchanged[0]);
		assertEquals(1, graphviz.getSvgCalls);
		assertIterableEquals(
				Collections.singletonList("graph-support layout declined; using Graphviz: conversion failed"),
				warnings(fixture));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void automaticPreparationOrApplyFailureRequestsFreshSmetanaInsteadOfReusingModel(boolean apply) throws Exception {
		final CountingGraphvizOperations graphviz = new CountingGraphvizOperations();
		final Fixture fixture = fixture(successProvider(), true, graphviz, factory -> result -> {
			if (apply == false)
				throw new IllegalStateException("prepare failed");
			return new GraphvizImageBuilder.PreparedLayout() {
				public SvekLayoutValidation getValidation() {
					return SvekLayoutValidation.valid();
				}

				public void apply() {
					factory.getBibliotekon().allNodes().iterator().next().moveDelta(123, 456);
					throw new IllegalStateException("partial apply failed");
				}
			};
		}, null, "@startuml\nclass A\nclass B\nA --> B\n@enduml", null, false, true);

		assertEquals(apply ? "partial apply failed" : "prepare failed",
				assertThrows(GraphvizImageBuilder.SmetanaFallback.class, fixture::build).getMessage());
		assertEquals(0, graphviz.getSvgCalls);
		// The automatic retry replays from source, so no Graphviz warning is published.
		assertIterableEquals(Collections.<String>emptyList(), warnings(fixture));
		assertEquals("previous Svek layout failed",
				assertThrows(IllegalStateException.class, fixture::build).getMessage());
	}

	@Test
	void invalidProviderResultFallsBackToGraphviz() throws Exception {
		assumeGraphvizAvailable();
		final Fixture fixture = fixture(new Builder() {
			public SvekLayoutResponse layout() {
				return SvekLayoutResponse.success(new SvekLayoutResult(new Bounds(0, 0, 100, 100),
						Direction.TOP_TO_BOTTOM,
						new LinkedHashMap<>(), new LinkedHashMap<>(),
						new LinkedHashMap<>()));
			}
		}, true);

		assertInstanceOf(SvekResult.class, fixture.build());
		final List<String> warnings = warnings(fixture);
		assertEquals(1, warnings.size());
		assertTrue(warnings.get(0).startsWith("graph-support layout declined; using Graphviz: "));
		assertTrue(warnings.get(0).contains("node"));
	}

	@Test
	void absentProviderFallsBackToGraphviz() throws Exception {
		assumeGraphvizAvailable();
		final Fixture fixture = fixture(null, true);

		assertInstanceOf(SvekResult.class, fixture.build());
		assertIterableEquals(
				Collections.singletonList("graph-support layout declined; using Graphviz: provider not found"),
				warnings(fixture));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void missingGraphvizPreservesErrorImageWhenGraphSupportDoesNotLayout(boolean engineAbsent) throws Exception {
		final GraphvizState state = GraphvizState.capture();
		try {
			state.environment.setDotExecutable("/guaranteed-missing/svek-fallback-test/dot");
			final Fixture fixture = fixture(engineAbsent ? null : decliningProvider(), true);

			final IEntityImage image = fixture.build();
			assertNotNull(image);
			assertFalse(image instanceof SvekResult);
			assertIterableEquals(Collections.singletonList("graph-support layout declined; using Graphviz: "
					+ (engineAbsent ? "provider not found" : "fixture declined")), warnings(fixture));
		} finally {
			state.restore();
		}
	}

	@Test
	void graphvizIoFailureProducesACrashImage() throws Exception {
		final Fixture fixture = fixture(decliningProvider(), true, new CountingGraphvizOperations() {
			public String getSvg(StringBounder stringBounder, DotMode dotMode, BaseFile basefile, String[] dotStrings)
					throws IOException {
				throw new IOException("broken pipe");
			}
		}, null);

		final IEntityImage image = fixture.build();
		assertNotNull(image);
		assertFalse(image instanceof SvekResult);
	}

	@Test
	void emptyGraphvizSvgProducesACrashImage() throws Exception {
		final Fixture fixture = fixture(decliningProvider(), true, new CountingGraphvizOperations() {
			public String getSvg(StringBounder stringBounder, DotMode dotMode, BaseFile basefile, String[] dotStrings) {
				return "";
			}
		}, null);

		final IEntityImage image = fixture.build();
		assertNotNull(image);
		assertFalse(image instanceof SvekResult);
	}

	@Test
	void unparsableGraphvizSvgThrowsAndPoisonsTheBuilder() throws Exception {
		final Fixture fixture = fixture(decliningProvider(), true, new CountingGraphvizOperations() {
			public String getSvg(StringBounder stringBounder, DotMode dotMode, BaseFile basefile, String[] dotStrings) {
				return "<svg/>";
			}

			public void solve(String svg) {
				throw new IllegalStateException("bad svg");
			}
		}, null);

		assertThrows(net.sourceforge.plantuml.dot.UnparsableGraphvizException.class, fixture::build);
		assertEquals("previous Svek layout failed",
				assertThrows(IllegalStateException.class, fixture::build).getMessage());
	}

	@Test
	void buildImageIsSingleUseAfterSuccess() throws Exception {
		final Fixture fixture = fixture(successProvider(), true);
		assertInstanceOf(SvekResult.class, fixture.build());

		assertEquals("Svek layout has already completed",
				assertThrows(IllegalStateException.class, fixture::build).getMessage());
	}

	@Test
	void graphvizFallbackRefreshesProvisionalVersionBeforeSerialization() throws Exception {
		final GraphvizVersion actual = version(false);
		final Fixture fixture = fixture(decliningProvider(), true, new CountingGraphvizOperations(), null, actual);

		assertInstanceOf(SvekResult.class, fixture.build());
		assertSame(actual, fixture.factory.getGraphvizVersion());
	}

	@Test
	void providerQuantifierLayoutDoesNotMutateEntityMargins() throws Exception {
		final CountingGraphvizOperations graphviz = new CountingGraphvizOperations() {
			public boolean isAvailable(DotStringFactory factory) {
				throw new AssertionError("provider must not check Graphviz availability");
			}
		};
		final Fixture fixture = fixture(successProvider(), true, graphviz, null, null,
				"@startuml\nclass A\nclass B\nA \"tail role\" --> \"head role\" B\n@enduml",
				new DotStringFactory.GraphvizVersionResolver() {
					public GraphvizVersion resolve() {
						throw new AssertionError("provider must not detect the Graphviz version");
					}
				});

		assertInstanceOf(SvekResult.class, fixture.build());
		assertEquals(0, graphviz.getSvgCalls);
		assertTrue(entity(fixture.diagram, "A").getMargins().isZero());
		assertTrue(entity(fixture.diagram, "B").getMargins().isZero());
		fixture.factory.getBibliotekon().allNodes().forEach(node -> {
			assertFalse(node.isShielded());
			assertNull(node.toLayoutSpec().bodyCellId);
		});
		fixture.factory.getBibliotekon().allLines().forEach(edge -> {
			assertNull(edge.toLayoutSpec().tailCellId);
			assertNull(edge.toLayoutSpec().headCellId);
		});
	}

	@Test
	void fallbackQuantifierMarginsFollowDetectedFalseVersion() throws Exception {
		final Fixture fixture = quantifierFixture(version(false));
		fixture.build();

		assertTrue(entity(fixture.diagram, "A").getMargins().isZero());
		assertTrue(entity(fixture.diagram, "B").getMargins().isZero());
	}

	@ParameterizedTest
	@CsvSource({ "false,false,false", "false,true,false", "true,false,false", "true,true,false",
			"false,false,true", "false,true,true", "true,false,true", "true,true,true" })
	void graphvizSerializationRefreshesSharedQuantifierShields(boolean requested, boolean shield, boolean roles)
			throws Exception {
		final String labelledLink = roles ? "A /tail --> /head B" : "A \"tail\" --> \"head\" B";
		final Fixture fixture = fixture(requested ? decliningProvider() : null, requested,
				new CountingGraphvizOperations(), null, version(shield),
				"@startuml\nclass A\nclass B\nclass C\nC --> A\nA --> A\n"
						+ labelledLink + "\n@enduml");
		if (requested == false)
			fixture.factory.useDetectedGraphvizVersion();
		fixture.imageBuilder.buildModel(fixture.stringBounder);
		final Bibliotekon bibliotekon = fixture.factory.getBibliotekon();
		assertEquals(3, bibliotekon.allLines().size());
		for (SvekNode node : bibliotekon.allNodes()) {
			assertFalse(node.isShielded());
			node.getWidth();
		}
		final String a = bibliotekon.getNode(entity(fixture.diagram, "A")).getUid();
		final String b = bibliotekon.getNode(entity(fixture.diagram, "B")).getUid();
		final String c = bibliotekon.getNode(entity(fixture.diagram, "C")).getUid();

		assertInstanceOf(SvekResult.class, fixture.build());
		final String dot = serializedDot(fixture);
		final String port = shield ? ":h" : "";
		assertTrue(dot.contains(c + "->" + a + port + "["));
		assertTrue(dot.contains(a + port + "->" + a + port + "["));
		assertTrue(dot.contains(a + port + "->" + b + port + "["));
		assertFalse(dot.contains(":h:h"));
		for (String name : asList("A", "B")) {
			final SvekNode node = bibliotekon.getNode(entity(fixture.diagram, name));
			assertEquals(shield, node.isShielded());
			if (shield) {
				assertTrue(dot.contains(node.getUid() + " [shape=plaintext,"));
				assertTrue(dot.contains("PORT=\"h\""));
				final NodeSpec spec = node.toLayoutSpec();
				assertEquals(1, spec.cells.size());
				assertEquals(16, spec.cells.get(0).x);
				assertEquals(16, spec.cells.get(0).y);
			} else
				assertFalse(dot.contains("PORT=\"h\""));
		}
		assertFalse(bibliotekon.getNode(entity(fixture.diagram, "C")).isShielded());
		fixture.factory.prepareForGraphviz();
		assertEquals(dot, serializedDot(fixture));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void graphvizQuantifiedGroupEndpointsKeepTheirCenterIds(boolean requested) throws Exception {
		final Fixture fixture = fixture(requested ? decliningProvider() : null, requested,
				new CountingGraphvizOperations(), null, version(true),
				"@startuml\npackage Left {\nclass A\n}\npackage Right {\nclass B\n}\n"
						+ "Left \"tail\" --> \"head\" Right\nRight \"tail\" --> \"head\" A\n"
						+ "B \"tail\" --> \"head\" Left\n@enduml");
		if (requested == false)
			fixture.factory.useDetectedGraphvizVersion();
		final List<Entity> groups = new ArrayList<>();
		for (Link link : fixture.diagram.getLinks()) {
			if (link.getEntity1().isGroup())
				groups.add(link.getEntity1());
			if (link.getEntity2().isGroup())
				groups.add(link.getEntity2());
		}
		assertEquals(4, groups.size());

		assertInstanceOf(SvekResult.class, fixture.build());
		assertEquals(3, fixture.factory.getBibliotekon().allLines().size());
		final String dot = serializedDot(fixture);
		for (Entity group : groups) {
			final String center = Cluster.getSpecialPointId(group);
			assertTrue(dot.contains(center));
			assertFalse(dot.contains(center + ":h"));
			// The public margin getter is leaf-only; verify the original group-safe merge.
			final Margins margins = getField(group, "margins");
			assertEquals(16, margins.getX1());
			assertEquals(16, margins.getX2());
			assertEquals(16, margins.getY1());
			assertEquals(16, margins.getY2());
		}
		for (Link link : fixture.diagram.getLinks())
			assertTrue(dot.contains(link.getEntityPort1(fixture.factory.getBibliotekon()).getFullString() + "->"
					+ link.getEntityPort2(fixture.factory.getBibliotekon()).getFullString() + "["));
		fixture.factory.prepareForGraphviz();
		assertEquals(dot, serializedDot(fixture));
	}

	@Test
	void fallbackQuantifierMarginsFollowDetectedTrueVersionAndInvalidateDimensions() throws Exception {
		final SvekNode[] nodeAtDotBoundary = new SvekNode[1];
		final Fixture fixture = fixture(decliningProvider(), true, new CountingGraphvizOperations() {
			public String getSvg(StringBounder stringBounder, DotMode dotMode, BaseFile basefile, String[] dotStrings) {
				assertNull(cachedDimension(nodeAtDotBoundary[0]));
				return "<svg/>";
			}
		}, null, version(true), "@startuml\nclass A\nclass B\nA \"tail role\" --> \"head role\" B\n@enduml");
		fixture.imageBuilder.buildModel(fixture.stringBounder);
		final SvekNode node = fixture.factory.getBibliotekon().getNode(entity(fixture.diagram, "A"));
		nodeAtDotBoundary[0] = node;
		node.getWidth();
		assertNotNull(cachedDimension(node));

		fixture.build();

		assertEquals(16, entity(fixture.diagram, "A").getMargins().getX1());
		assertEquals(16, entity(fixture.diagram, "B").getMargins().getX1());
	}

	@Test
	void fallbackMergesAsymmetricQuantifierMarginsAndInvalidatesDimensions() throws Exception {
		final SvekNode[] nodeAtDotBoundary = new SvekNode[1];
		final Fixture fixture = fixture(decliningProvider(), true, new CountingGraphvizOperations() {
			public String getSvg(StringBounder stringBounder, DotMode dotMode, BaseFile basefile, String[] dotStrings) {
				assertNull(cachedDimension(nodeAtDotBoundary[0]));
				return "<svg/>";
			}
		}, null, version(true), "@startuml\nclass A\nclass B\nA \"tail role\" --> B\n@enduml");
		final Entity endpoint = entity(fixture.diagram, "A");
		endpoint.ensureMargins(new Margins(20, 3, 18, 4));
		fixture.imageBuilder.buildModel(fixture.stringBounder);
		final SvekNode node = fixture.factory.getBibliotekon().getNode(endpoint);
		nodeAtDotBoundary[0] = node;
		node.getWidth();

		fixture.build();

		assertEquals(20, endpoint.getMargins().getX1());
		assertEquals(16, endpoint.getMargins().getX2());
		assertEquals(18, endpoint.getMargins().getY1());
		assertEquals(16, endpoint.getMargins().getY2());
		final NodeSpec spec = node.toLayoutSpec();
		assertEquals(1, spec.cells.size());
		assertEquals(20, spec.cells.get(0).x);
		assertEquals(18, spec.cells.get(0).y);
		assertEquals(36, spec.width - node.getWidth());
		assertEquals(34, spec.height - node.getHeight());
		final String dot = serializedDot(fixture);
		assertTrue(dot.contains(node.getUid() + ":h->"));
		assertTrue(dot.contains("WIDTH=\"16.0\" HEIGHT=\"1.0\""));
		assertTrue(dot.contains("WIDTH=\"1.0\" HEIGHT=\"16.0\""));
		node.getWidth();
		fixture.factory.prepareForGraphviz();
		assertNotNull(cachedDimension(node));
		assertEquals(dot, serializedDot(fixture));
	}

	@Test
	void zeroEntityDegenerateLayoutIsSingleUse() throws Exception {
		final Fixture fixture = fixture(null, true, null, null, null,
				"@startuml\nclass A\n@enduml", null, true);

		assertInstanceOf(GraphvizImageBuilder.EntityImageSimpleEmpty.class, fixture.build());
		assertEquals("Svek layout has already completed",
				assertThrows(IllegalStateException.class, fixture::build).getMessage());
	}

	@Test
	void oneEntityDegenerateLayoutIsSingleUse() throws Exception {
		final Fixture fixture = fixture(null, true, null, null, null,
				"@startuml\nclass A\n@enduml");

		assertInstanceOf(EntityImageDegenerated.class, fixture.build());
		assertEquals("Svek layout has already completed",
				assertThrows(IllegalStateException.class, fixture::build).getMessage());
	}

	@Test
	void uncheckedGraphvizPreparationFailureRethrowsAndPoisonsTheBuilder() throws Exception {
		final Fixture fixture = fixture(decliningProvider(), true, new CountingGraphvizOperations(), null, null,
				"@startuml\nclass A\nclass B\nA --> B\n@enduml", new DotStringFactory.GraphvizVersionResolver() {
					public GraphvizVersion resolve() {
						throw new IllegalStateException("version failed");
					}
				});

		assertEquals("version failed", assertThrows(IllegalStateException.class, fixture::build).getMessage());
		assertEquals("previous Svek layout failed",
				assertThrows(IllegalStateException.class, fixture::build).getMessage());
	}

	@Test
	void providerDeclineAddsOneVisibleWarningWithReason() throws Exception {
		final Fixture fixture = fixture(decliningProvider(), true, new CountingGraphvizOperations(), null);
		fixture.build();

		assertIterableEquals(
				Collections.singletonList("graph-support layout declined; using Graphviz: fixture declined"),
				warnings(fixture));
	}

	private static List<String> warnings(Fixture fixture) {
		return fixture.diagram.getPragma().getWarnings().stream().map(warning -> warning.asSingleLine())
				.collect(Collectors.toList());
	}

	private static Fixture quantifierFixture(GraphvizVersion detectedVersion) throws Exception {
		return fixture(decliningProvider(), true, new CountingGraphvizOperations(), null, detectedVersion,
				"@startuml\nclass A\nclass B\nA \"tail role\" --> \"head role\" B\n@enduml");
	}

	private static Entity entity(CucaDiagram diagram, String name) {
		for (Entity entity : diagram.leafs())
			if (name.equals(entity.getName()))
				return entity;
		throw new IllegalArgumentException(name);
	}

	private static Object cachedDimension(SvekNode node) {
		try {
			return getField(node, "dimImage");
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private static String serializedDot(Fixture fixture) throws Exception {
		final Method method = DotStringFactory.class.getDeclaredMethod("createDotString", StringBounder.class,
				DotMode.class, String[].class);
		method.setAccessible(true);
		return (String) method.invoke(fixture.factory, fixture.stringBounder, DotMode.NORMAL, new String[0]);
	}

	private static void assumeGraphvizAvailable() {
		final File dot = GraphvizRuntimeEnvironment.getInstance().getDotExe();
		assumeTrue(dot != null && dot.isFile() && dot.canExecute(), "Graphviz is required for fallback verification");
	}

	private static SvekLayoutBuilder successProvider() {
		return new Builder() {
			public SvekLayoutResponse layout() {
				final Map<String, NodeGeometry> nodes = new LinkedHashMap<>();
				for (int i = 0; i < nodeSpecs.size(); i++) {
					final NodeSpec node = nodeSpecs.get(i);
					final double x = 20 + i * 160;
					nodes.put(node.id, new NodeGeometry(node.id,
							new Bounds(x, 20, x + node.width, 20 + node.height), null));
				}
				final Map<String, ClusterGeometry> clusters = new LinkedHashMap<>();
				for (ClusterSpec cluster : clusterSpecs)
					clusters.put(cluster.id, new ClusterGeometry(cluster.id, new Bounds(0, 0, 400, 150),
							cluster.titleWidth == 0 && cluster.titleHeight == 0 ? null : new Point(5, 5)));

				final Map<String, EdgeGeometry> edges = new LinkedHashMap<>();
				for (EdgeSpec edge : edgeSpecs)
					edges.put(edge.id, new EdgeGeometry(edge.id, edge.tailId, edge.headId,
							new Path(true, asList(new Point(60, 80), new Point(90, 80), new Point(130, 80),
									new Point(160, 80))), new Point(100, 90), new Point(60, 90), new Point(160, 90)));
				return SvekLayoutResponse.success(new SvekLayoutResult(new Bounds(0, 0, 400, 150), direction, nodes,
						clusters, edges));
			}
		};
	}

	private static SvekLayoutBuilder decliningProvider() {
		return new Builder() {
			public SvekLayoutResponse layout() {
				return SvekLayoutResponse.declined("unsupported", "fixture declined");
			}
		};
	}

	private static Fixture fixture(SvekLayoutBuilder layoutBuilder, boolean requested)
			throws Exception {
		return fixture(layoutBuilder, requested, null, null);
	}

	private static Fixture fixture(SvekLayoutBuilder layoutBuilder, boolean requested,
			GraphvizImageBuilder.GraphvizOperations graphvizOperations,
			GraphvizImageBuilder.LayoutApplierFactory applierFactory) throws Exception {
		return fixture(layoutBuilder, requested, graphvizOperations, applierFactory, null);
	}

	private static Fixture fixture(SvekLayoutBuilder layoutBuilder, boolean requested,
			GraphvizImageBuilder.GraphvizOperations graphvizOperations,
			GraphvizImageBuilder.LayoutApplierFactory applierFactory, final GraphvizVersion detectedVersion) throws Exception {
		return fixture(layoutBuilder, requested, graphvizOperations, applierFactory, detectedVersion,
				"@startuml\nclass A\nclass B\nA --> B\n@enduml");
	}

	private static Fixture fixture(SvekLayoutBuilder layoutBuilder, boolean requested,
			GraphvizImageBuilder.GraphvizOperations graphvizOperations,
			GraphvizImageBuilder.LayoutApplierFactory applierFactory, final GraphvizVersion detectedVersion,
			String source) throws Exception {
		return fixture(layoutBuilder, requested, graphvizOperations, applierFactory, detectedVersion, source,
				null, false);
	}

	private static Fixture fixture(SvekLayoutBuilder layoutBuilder, boolean requested,
			GraphvizImageBuilder.GraphvizOperations graphvizOperations,
			GraphvizImageBuilder.LayoutApplierFactory applierFactory, final GraphvizVersion detectedVersion,
			String source, DotStringFactory.GraphvizVersionResolver resolver) throws Exception {
		return fixture(layoutBuilder, requested, graphvizOperations, applierFactory, detectedVersion, source,
				resolver, false);
	}

	private static Fixture fixture(SvekLayoutBuilder layoutBuilder, boolean requested,
			GraphvizImageBuilder.GraphvizOperations graphvizOperations,
			GraphvizImageBuilder.LayoutApplierFactory applierFactory, final GraphvizVersion detectedVersion,
			String source, DotStringFactory.GraphvizVersionResolver resolver, boolean emptyLeafs) throws Exception {
		return fixture(layoutBuilder, requested, graphvizOperations, applierFactory, detectedVersion,
				source, resolver, emptyLeafs, false);
	}

	private static Fixture fixture(SvekLayoutBuilder layoutBuilder, boolean requested,
			GraphvizImageBuilder.GraphvizOperations graphvizOperations,
			GraphvizImageBuilder.LayoutApplierFactory applierFactory, final GraphvizVersion detectedVersion,
			String source, DotStringFactory.GraphvizVersionResolver resolver, boolean emptyLeafs,
			boolean automatic) throws Exception {
		final SourceStringReader reader = new SourceStringReader(source);
		final List<BlockUml> blocks = reader.getBlocks();
		final Diagram parsed = blocks.get(0).getDiagram();
		final CucaDiagram diagram = (CucaDiagram) parsed;
		final StringBounder stringBounder = new FileFormatOption(FileFormat.SVG)
				.getDefaultStringBounder(diagram.getSkinParam(), diagram.getPragma());
		final List<Link> links = new ArrayList<>(diagram.getLinks());
		final Bibliotekon bibliotekon = new Bibliotekon(links);
		final Cluster root = new Cluster(diagram.getRootGroup().getLocation(), diagram, bibliotekon.getColorSequence(),
				diagram.getRootGroup());
		final ClusterManager clusterManager = new ClusterManager(bibliotekon, root);
		final DotStringFactory.GraphvizVersionResolver effectiveResolver = resolver != null ? resolver
				: detectedVersion == null ? null : new DotStringFactory.GraphvizVersionResolver() {
					public GraphvizVersion resolve() {
						return detectedVersion;
					}
				};
		final DotStringFactory factory = effectiveResolver == null
				? new DotStringFactory(bibliotekon, root, diagram.getDiagramType(), diagram.getSkinParam(),
						GraphvizVersionFinder.DEFAULT)
				: new DotStringFactory(bibliotekon, root, diagram.getDiagramType(), diagram.getSkinParam(),
						GraphvizVersionFinder.DEFAULT, effectiveResolver);
		final DotData dotData = new DotData(diagram, diagram.getRootGroup(), links,
				emptyLeafs ? Collections.<Entity>emptyList() : diagram.leafs(), diagram, diagram);
		final GraphvizImageBuilder imageBuilder = new GraphvizImageBuilder(dotData, diagram.getSource(),
				diagram.getPragma(), diagram.getDiagramType().getStyleName(), DotMode.NORMAL, factory, clusterManager,
				layoutBuilder, requested, graphvizOperations, applierFactory, automatic);
		return new Fixture(imageBuilder, stringBounder, factory, diagram);
	}

	private static GraphvizVersion version(final boolean shield) {
		return new GraphvizVersion() {
			public boolean useShieldForQuantifier() {
				return shield;
			}

			public boolean useProtectionWhenThereALinkFromOrToGroup() {
				return false;
			}

			public boolean useXLabelInsteadOfLabel() {
				return false;
			}

			public boolean isVizjs() {
				return false;
			}

			public boolean ignoreHorizontalLinks() {
				return false;
			}
		};
	}

	private static class Builder implements SvekLayoutBuilder {
		protected Direction direction;
		protected final List<NodeSpec> nodeSpecs = new ArrayList<>();
		protected final List<ClusterSpec> clusterSpecs = new ArrayList<>();
		protected final List<EdgeSpec> edgeSpecs = new ArrayList<>();

		public void graph(GraphSpec graph) {
			direction = graph.direction;
		}

		public void node(NodeSpec node) {
			nodeSpecs.add(node);
		}

		public void beginCluster(ClusterSpec cluster) {
			clusterSpecs.add(cluster);
		}

		public void endCluster() {
		}

		public void rankGroup(RankSpec rank) {
		}

		public void edge(EdgeSpec edge) {
			edgeSpecs.add(edge);
		}

		public SvekLayoutResponse layout() {
			throw new UnsupportedOperationException();
		}
	}

	private static final class Fixture {
		private final GraphvizImageBuilder imageBuilder;
		private final StringBounder stringBounder;
		private final DotStringFactory factory;
		private final CucaDiagram diagram;

		private Fixture(GraphvizImageBuilder imageBuilder, StringBounder stringBounder, DotStringFactory factory,
				CucaDiagram diagram) {
			this.imageBuilder = imageBuilder;
			this.stringBounder = stringBounder;
			this.factory = factory;
			this.diagram = diagram;
		}

		private IEntityImage build() {
			return imageBuilder.buildImage(stringBounder, null, new String[0], false);
		}
	}

	private static class CountingGraphvizOperations implements GraphvizImageBuilder.GraphvizOperations {
		private int getSvgCalls;

		public boolean isAvailable(DotStringFactory factory) {
			return true;
		}

		public String getSvg(StringBounder stringBounder, DotMode dotMode, BaseFile basefile, String[] dotStrings)
				throws IOException {
			getSvgCalls++;
			return "<svg/>";
		}

		public void solve(String svg) throws IOException, InterruptedException {
		}
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
			return new GraphvizState(environment, getField(environment, "dotExecutable"), getField(environment, "dotVersion"),
					new HashMap<>(SvekLayoutFallbackTest.<Map<File, GraphvizVersion>>getField(environment,
							"map")));
		}

		private void restore() throws Exception {
			setField(environment, "dotExecutable", dotExecutable);
			setField(environment, "dotVersion", dotVersion);
			final Map<File, GraphvizVersion> cache = getField(environment, "map");
			cache.clear();
			cache.putAll(versions);
		}
	}

	/** True once an automatic graph-support layout gave up and asked for a Smetana rebuild. */
	private static boolean automaticGraphSupportFailed(CucaDiagram diagram) throws Exception {
		final Field field = CucaDiagram.class.getDeclaredField("automaticGraphSupportFailed");
		field.setAccessible(true);
		return (Boolean) field.get(diagram);
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
