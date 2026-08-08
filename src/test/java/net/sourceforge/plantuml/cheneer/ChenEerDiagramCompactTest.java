package net.sourceforge.plantuml.cheneer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.cheneer.command.CommandCreateEntity;
import net.sourceforge.plantuml.cheneer.command.CommandEndGroup;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.utils.BlocLines;

class ChenEerDiagramCompactTest {

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null,
			new PreprocessingArtifact());

	@Test
	void test_aliasDomainFlagsAndTopology() throws Exception {
		execute(new CommandNotationCompact(), "notation compact");
		execute(new CommandCreateEntity(), "entity Customer {");
		execute(new CommandCreateAttributeDispatch(), "\"Customer number\" as Number : INTEGER <<KEY>><<multi>><<derived>>");
		execute(new CommandCreateAttributeDispatch(), "Partial <<Discriminator>>");
		execute(new CommandCreateAttributeDispatch(), "Ignored <<keyish>>");
		execute(new CommandCreateAttributeDispatch(), "Both <<key>><<discriminator>>");

		final Entity customer = diagram.quarkInContext(true, "Customer").getData();
		final List<CompactChenAttribute> rows = diagram.getCompactAttributes(customer);
		assertEquals(4, rows.size());

		final CompactChenAttribute number = rows.get(0);
		assertEquals("Customer/Number", number.getQualifiedIdentity());
		assertEquals("Customer number", number.getDisplayName());
		assertEquals("INTEGER", number.getDomain());
		assertEquals(0, number.getDepth());
		assertTrue(number.isKey());
		assertTrue(number.isMulti());
		assertTrue(number.isDerived());

		assertTrue(rows.get(1).isDiscriminator());
		assertEquals(CompactChenAttribute.UnderlineStyle.DASHED, rows.get(1).getUnderlineStyle());
		assertFalse(rows.get(1).isKey());
		assertFalse(rows.get(2).isKey());
		assertEquals(CompactChenAttribute.UnderlineStyle.NONE, rows.get(2).getUnderlineStyle());
		assertTrue(rows.get(3).isKey());
		assertTrue(rows.get(3).isDiscriminator());
		assertEquals(CompactChenAttribute.UnderlineStyle.SOLID, rows.get(3).getUnderlineStyle());
		assertNull(diagram.quarkInContext(true, "Customer/Number").getData());
		assertEquals(1, diagram.leafs().size());
		assertTrue(diagram.getLinks().isEmpty());
	}

	@Test
	void test_nestedPathsDepthAndDuplicates() throws Exception {
		execute(new CommandNotationCompact(), "notation compact");
		execute(new CommandCreateEntity(), "entity E {");
		execute(new CommandCreateAttributeDispatch(), "Address {");
		execute(new CommandCreateAttributeDispatch(), "Street");
		execute(new CommandEndGroup(), "}");
		execute(new CommandCreateAttributeDispatch(), "Other {");
		execute(new CommandCreateAttributeDispatch(), "Street");

		final Entity entity = diagram.quarkInContext(true, "E").getData();
		final List<CompactChenAttribute> rows = diagram.getCompactAttributes(entity);
		assertEquals(4, rows.size());
		assertEquals("E/Address/Street", rows.get(1).getQualifiedIdentity());
		assertEquals(1, rows.get(1).getDepth());
		assertEquals("E/Other/Street", rows.get(3).getQualifiedIdentity());

		execute(new CommandEndGroup(), "}");
		final CommandExecutionResult duplicate = new CommandCreateAttributeDispatch().execute(diagram,
				BlocLines.singleString("Address"), ParserPass.ONE);
		assertFalse(duplicate.isOk());
	}

	@Test
	void test_relationshipGetsOneLazyBoxAndDashedLink() throws Exception {
		execute(new CommandNotationCompact(), "notation compact");
		execute(new CommandCreateEntity(), "relationship R #pink;line:red {");
		execute(new CommandCreateAttributeDispatch(), "Started : DATE #lime;line:orange");
		execute(new CommandCreateAttributeDispatch(), "Location <<multi>>");

		final Entity box = diagram.quarkInContext(true,
				"R/__plantuml_compact_relationship_attributes__").getData();
		assertNotNull(box);
		assertEquals(LeafType.CHEN_ATTRIBUTE, box.getLeafType());
		assertEquals(2, diagram.getCompactAttributes(box).size());
		assertEquals(1, diagram.getLinks().size());
		assertEquals("NONE-DASHED(null)-NONE", diagram.getLinks().get(0).getType().toString());
		assertEquals(diagram.quarkInContext(true, "R").getData().getColors(), box.getColors());

		diagram.makeDiagramReady();
		assertInstanceOf(EntityImageChenRelationshipAttribute.class, box.getSvekImage());
	}

	@Test
	void test_relationshipWithoutAttributesHasNoBox() throws Exception {
		execute(new CommandNotationCompact(), "notation compact");
		execute(new CommandCreateEntity(), "relationship R {");

		assertEquals(1, diagram.leafs().size());
		assertTrue(diagram.getLinks().isEmpty());
	}

	@Test
	void test_standardAttributeCommandKeepsLeafTopology() throws Exception {
		execute(new CommandCreateEntity(), "entity E {");
		execute(new CommandCreateAttributeDispatch(), "id");

		assertNotNull(diagram.quarkInContext(true, "E/id").getData());
		assertEquals(2, diagram.leafs().size());
		assertEquals(1, diagram.getLinks().size());
	}

	@Test
	void test_commonDirectivesMayPrecedeNotation() {
		final String source = "@startchen\n" + "skinparam backgroundColor white\n"
				+ "<style>\nchenEerDiagram {\n  BackGroundColor white\n}\n</style>\n" + "left to right direction\n"
				+ "notation compact\n" + "entity E {\n" + "  id <<key>>\n" + "}\n" + "@endchen\n";
		final ChenEerDiagram parsed = assertInstanceOf(ChenEerDiagram.class,
				new SourceStringReader(source).getBlocks().get(0).getDiagram());
		assertInstanceOf(EntityImageChenCompactEntity.class,
				parsed.quarkInContext(true, "E").getData().getSvekImage());
	}

	@Test
	void test_lateNotationProducesSyntaxError() {
		final String source = "@startchen\n" + "entity E {\n" + "}\n" + "notation compact\n" + "@endchen\n";
		assertInstanceOf(PSystemError.class, new SourceStringReader(source).getBlocks().get(0).getDiagram());
	}

	private void execute(Command<ChenEerDiagram> command, String line) throws Exception {
		final CommandExecutionResult result = command.execute(diagram, BlocLines.singleString(line), ParserPass.ONE);
		assertTrue(result.isOk(), line + ": " + result);
	}

}
