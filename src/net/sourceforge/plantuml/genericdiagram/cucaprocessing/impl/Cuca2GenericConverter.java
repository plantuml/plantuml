
/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 */
package net.sourceforge.plantuml.genericdiagram.cucaprocessing.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.baraye.CucaDiagram;
import net.sourceforge.plantuml.baraye.IEntity;
import net.sourceforge.plantuml.baraye.IGroup;
import net.sourceforge.plantuml.baraye.ILeaf;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.BodierJSon;
import net.sourceforge.plantuml.cucadiagram.BodierLikeClassOrObject;
import net.sourceforge.plantuml.cucadiagram.CucaNote;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.genericdiagram.GenericDiagramType;
import net.sourceforge.plantuml.genericdiagram.GenericEdgeType;
import net.sourceforge.plantuml.genericdiagram.GenericEntityType;
import net.sourceforge.plantuml.genericdiagram.GenericLinkDecor;
import net.sourceforge.plantuml.genericdiagram.GenericLinkStyle;
import net.sourceforge.plantuml.genericdiagram.IGenericEdge;
import net.sourceforge.plantuml.genericdiagram.IGenericModelElement;
import net.sourceforge.plantuml.genericdiagram.MemberVisibility;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaDiagramVisitor;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaDiagramWrapper;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaGroupWrapper;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaLeafWrapper;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaLinkWrapper;
import net.sourceforge.plantuml.genericdiagram.data.GenericDiagram;
import net.sourceforge.plantuml.genericdiagram.data.GenericEdge;
import net.sourceforge.plantuml.genericdiagram.data.GenericGroup;
import net.sourceforge.plantuml.genericdiagram.data.GenericLeaf;
import net.sourceforge.plantuml.genericdiagram.data.GenericLink;
import net.sourceforge.plantuml.genericdiagram.data.GenericMember;
import net.sourceforge.plantuml.genericdiagram.data.GenericModelElement;
import net.sourceforge.plantuml.genericdiagram.data.GenericStereotype;
import net.sourceforge.plantuml.genericdiagram.data.SimpleGenericModel;
import net.sourceforge.plantuml.genericdiagram.genericprocessing.IGenericModelCollector;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.utils.Log;

public class Cuca2GenericConverter implements ICucaDiagramVisitor {

	Map<LeafType, GenericEntityType> leafTypeMap;
	Map<LinkDecor, GenericLinkDecor> decorMap;
	Map<String, GenericLinkStyle> linkTypeMap;
	Map<SName, GenericEntityType> entityTypeMap;
	Map<Character, MemberVisibility> visibilityMap;
	Map<Class<?>, GenericDiagramType> diagramTypeMap;
	Map<String, GenericModelElement> processedCucaElementsMap;
	IGenericModelCollector collector = new SimpleGenericModel();
	GenericModelElementFactory elementFactory = new GenericModelElementFactory();
	String file;  //path to source file
	String graphmlRootDir; // path to directory which is assumed to be the root of the project
	int blockCount; // a file can have multiple @startuml..@enduml blocks, zero based count

	public Cuca2GenericConverter(String file, int blockCount, String graphmlRootDir) {

		this.file = file;
		this.blockCount = blockCount;
		this.graphmlRootDir = graphmlRootDir;

		initLeafTypeMap();
		initDecorMap();
		initLinkTypeMap();
		initEntityTypeMap();
		initLeafTypeMap();
		initVisibilityMap();
		initDiagramTypeMap();
		initCollector();
		checkgraphmlRootDir();
	}

	private void checkgraphmlRootDir(){

		// Verify the root and file name match
		// Verify that root is a directory
		if (this.graphmlRootDir == null)
		{
			throw new IllegalArgumentException("missing command line parameter -graphml-root-dir");
		}
		File root  = new File(this.graphmlRootDir);
		File sourceFile = new File(this.file);
		boolean ok = root.exists() &&
						root.isDirectory() &&
						sourceFile.exists() &&
						sourceFile.isFile() &&
						sourceFile.getPath().startsWith(root.getPath());

		if (!ok) {
			throw new IllegalArgumentException("file and graphml-root-dir don't match");
		}
	}
	private void initCollector() {
		processedCucaElementsMap = new HashMap<>();
		collector.reset();
	}

	private void initDiagramTypeMap() {
		diagramTypeMap = new HashMap<>();
		//TODO add other supported diagram types
		diagramTypeMap.put(DescriptionDiagram.class, GenericDiagramType.COMPONENT);
		diagramTypeMap.put(ClassDiagram.class, GenericDiagramType.CLASS);
	}

	/**
	 * Initializes the Lookup of puml entities to UML Types
	 */
	private void initEntityTypeMap() {

		Map<SName, GenericEntityType> typeMap = new HashMap<>();
		typeMap.put(SName.component, GenericEntityType.COMPONENT);
		typeMap.put(SName.interface_, GenericEntityType.CIRCLE);
		typeMap.put(SName.spotInterface, GenericEntityType.CIRCLE);
		typeMap.put(SName.package_, GenericEntityType.PACKAGE);
		typeMap.put(SName.class_, GenericEntityType.CLASS);
		typeMap.put(SName.node, GenericEntityType.NODE);
		typeMap.put(SName.folder, GenericEntityType.FOLDER);
		typeMap.put(SName.frame, GenericEntityType.FRAME);
		typeMap.put(SName.cloud, GenericEntityType.CLOUD);
		typeMap.put(SName.database, GenericEntityType.DATABASE);
		typeMap.put(SName.rectangle, GenericEntityType.RECTANGLE);
		entityTypeMap = typeMap;

	}


	private void initVisibilityMap() {
		visibilityMap = new HashMap<>();
		visibilityMap.put('-', MemberVisibility.PRIVATE);
		visibilityMap.put('#', MemberVisibility.PROTECTED);
		visibilityMap.put('~', MemberVisibility.PACKAGE_PRIVATE);
		visibilityMap.put('+', MemberVisibility.PUBLIC);
	}
	private void initDecorMap() {

		this.decorMap = new HashMap<>();
		this.decorMap.put(LinkDecor.NONE, GenericLinkDecor.NONE);
		this.decorMap.put(LinkDecor.CIRCLE_CONNECT, GenericLinkDecor.CIRCLE_CONNECT);
		this.decorMap.put(LinkDecor.SQUARE, GenericLinkDecor.SQUARE);
		this.decorMap.put(LinkDecor.CIRCLE, GenericLinkDecor.CIRCLE);
		this.decorMap.put(LinkDecor.CIRCLE_FILL, GenericLinkDecor.CIRCLE_FILL);
		this.decorMap.put(LinkDecor.PARENTHESIS, GenericLinkDecor.PARENTHESIS);
		this.decorMap.put(LinkDecor.COMPOSITION, GenericLinkDecor.COMPOSITION);
		this.decorMap.put(LinkDecor.AGREGATION, GenericLinkDecor.AGGREGATION);
		this.decorMap.put(LinkDecor.PLUS, GenericLinkDecor.PLUS);
		this.decorMap.put(LinkDecor.HALF_ARROW, GenericLinkDecor.HALF_ARROW);
		this.decorMap.put(LinkDecor.ARROW_TRIANGLE, GenericLinkDecor.ARROW_TRIANGLE);
		this.decorMap.put(LinkDecor.EXTENDS, GenericLinkDecor.EXTENDS);
		this.decorMap.put(LinkDecor.DEFINEDBY, GenericLinkDecor.DEFINED_BY);
		this.decorMap.put(LinkDecor.REDEFINES, GenericLinkDecor.REDEFINES);
		this.decorMap.put(LinkDecor.ARROW, GenericLinkDecor.ARROW);
		this.decorMap.put(LinkDecor.NOT_NAVIGABLE, GenericLinkDecor.NOT_NAVIGABLE);
		this.decorMap.put(LinkDecor.CROWFOOT, GenericLinkDecor.CROWFOOT);
	}

	private void initLeafTypeMap() {

		//TODO complete
		this.leafTypeMap = new HashMap<>();
		this.leafTypeMap.put(LeafType.DESCRIPTION, GenericEntityType.COMPONENT);
		this.leafTypeMap.put(LeafType.INTERFACE, GenericEntityType.INTERFACE);
		this.leafTypeMap.put(LeafType.CLASS, GenericEntityType.CLASS);
		this.leafTypeMap.put(LeafType.STATE_CHOICE, GenericEntityType.DIAMOND);
		this.leafTypeMap.put(LeafType.ASSOCIATION, GenericEntityType.DIAMOND);
		this.leafTypeMap.put(LeafType.ENUM, GenericEntityType.ENUM);
		this.leafTypeMap.put(LeafType.NOTE, GenericEntityType.NOTE);
		this.leafTypeMap.put(LeafType.ENTITY, GenericEntityType.ENTITY);
		this.leafTypeMap.put(LeafType.ANNOTATION, GenericEntityType.ANNOTATION);
		this.leafTypeMap.put(LeafType.ABSTRACT_CLASS, GenericEntityType.ABSTRACT_CLASS);
		this.leafTypeMap.put(LeafType.POINT_FOR_ASSOCIATION, GenericEntityType.POINT_FOR_ASSOCIATION);
		this.leafTypeMap.put(LeafType.PORTIN, GenericEntityType.PORT_IN);
		this.leafTypeMap.put(LeafType.PORTOUT, GenericEntityType.PORT_OUT);
		this.leafTypeMap.put(LeafType.JSON, GenericEntityType.JSON);
	}

	private void initLinkTypeMap() {
		//TODO Brittle workaround with strings ... has thickness in it ...will probably break
		this.linkTypeMap = new HashMap<>();
		this.linkTypeMap.put(LinkStyle.BOLD().toString(), GenericLinkStyle.BOLD);
		this.linkTypeMap.put(LinkStyle.DASHED().toString(), GenericLinkStyle.DASHED);
		this.linkTypeMap.put(LinkStyle.DOTTED().toString(), GenericLinkStyle.DOTTED);
		this.linkTypeMap.put(LinkStyle.INVISIBLE().toString(), GenericLinkStyle.INVISIBLE);
		this.linkTypeMap.put(LinkStyle.NORMAL().toString(), GenericLinkStyle.NORMAL);
	}

	private void processStereotype(Stereotype stereotype, GenericModelElement containingEntity) {


		List<String> stereos = stereotype.getLabels(Guillemet.NONE);
		// stereotypes don't have an id, use parent id plus position
		// in list to create one unique for the diagram
		for (int i = 0; i < stereos.size(); i++) {
			String stereotypeId = containingEntity.getPumlId() + "s" + i;
			GenericStereotype genStereotype = createGenericStereotype(stereos.get(i), stereotypeId);
			IGenericEdge genEdge = createGenericEdge(containingEntity, GenericEdgeType.STEREOTYPE, genStereotype);
			collector.addStereotype(genStereotype);
			collector.addEdge(genEdge);
		}
	}

	private void processBodier(Bodier bodier, GenericModelElement owningEntity) {

		// Methods
		List<String> methods = bodier.getMethodsToDisplay().asList().stream()
						.map(cs -> String.valueOf(cs))
						.collect(Collectors.toList());
		this.processMembers(methods, GenericEntityType.METHOD, owningEntity);

		// Fields
		List<String> fields = bodier.getFieldsToDisplay().asList().stream()
						.map(cs -> String.valueOf(cs))
						.collect(Collectors.toList());
		this.processMembers(fields, GenericEntityType.FIELD, owningEntity);
	}

	private void processMembers(List<String> members, GenericEntityType type, GenericModelElement owningEntity) {

		// methods don't have an id, use parent id plus position
		// in list to create one unique for the diagram

		String suffix = null;
		if (type == GenericEntityType.METHOD) {
			suffix = "_m";
		}
		if (type == GenericEntityType.FIELD) {
			suffix = "_f";
		}

		for (int i = 0; i < members.size(); i++) {
			GenericMember genMember = elementFactory.genericMemberSupplier.get();
			genMember.setPumlId(owningEntity.getPumlId() + suffix + i);
			genMember.setLabel(this.stripModifier(members.get(i)));
			genMember.setPumlRootPath(this.getPumlElementRootPath());
			genMember.setAbstract(this.isAbstractMember(members.get(i)));
			genMember.setStatic(this.isStaticMember(members.get(i)));
			genMember.setVisibility(determineMemberVisibility(members.get(i)));
			genMember.setType(type);
			IGenericEdge genEdge = createGenericEdge(owningEntity, GenericEdgeType.MEMBER, genMember);
			collector.addMember(genMember);
			collector.addEdge(genEdge);
		}
	}
	private Boolean isAbstractMember(String member) {
		return member.contains("{abstract}");
	}

	private Boolean isStaticMember(String member) {
		return member.contains("{static}");
	}

	private String stripModifier(String member) {
		// remove visibility and other modifiers from the member name
		return member.replaceAll("([+|#|\\-|~]|\\{static\\}|\\{abstract\\})", "").trim();
	}
	private GenericLeaf processLeaf(ICucaLeafWrapper leaf) {

		GenericLeaf genericLeaf = getOrCreateGenericLeaf(leaf.getLeaf());
		collector.addLeaf(genericLeaf);

		// Stereotypes
		Stereotype stereotype = leaf.getLeaf().getStereotype();
		if (stereotype != null) {
			processStereotype(stereotype, genericLeaf);
		}
		// Members
		Bodier bodier = leaf.getLeaf().getBodier();
		if (bodier instanceof BodierLikeClassOrObject) {
			processBodier(bodier, genericLeaf);
		}
		return genericLeaf;
	}


	private void processLink(ICucaLinkWrapper link) {

		if (!isPumlLayoutLink(link)) {
			// we consider source and target based on the occurrence in the diagram
			// entity1 is ALWAYS source, entity2 is ALWAYS target
			// the semantic interpretation in the UML sense may happen at a later stage
			GenericLink genericLink = createGenericLink(link.getLink());
			GenericModelElement source = getEntityByPumlId(link.getLink().getEntity1().getUid());
			GenericModelElement target = getEntityByPumlId(link.getLink().getEntity2().getUid());
			IGenericEdge sourceEdge = createGenericEdge(source,
							GenericEdgeType.IS_SOURCE, genericLink);
			((GenericEdge) sourceEdge).setPumlIdSource(link.getLink().getEntity1().getUid());
			IGenericEdge targetEdge = createGenericEdge(target,
							GenericEdgeType.IS_TARGET, genericLink);
			((GenericEdge) targetEdge).setPumlIdTarget(link.getLink().getEntity2().getUid());

			collector.addLink(genericLink);
			collector.addEdge(sourceEdge);
			collector.addEdge(targetEdge);
		}
		if (isTipsLink(link)){
			Log.error("Comments on fields, methods and links not supported");
		}
	}

	private boolean isPumlLayoutLink(ICucaLinkWrapper link){
		// plantUML adds some links for layout purposes,
		// these should not be added to the graphML output
		// because they are not user defined links
		return "NONE-INVISIBLE(null)-NONE".equals(link.getLink().getType().toString());
	}

	private boolean isTipsLink(ICucaLinkWrapper link){
		// Notes on fields and methods are realized as invisible links of type TIPS
		CucaNote note = link.getLink().getNote();
		return link.getLink().toString().contains("](TIPS)[") || (note != null);
	}
	private List<ICucaLeafWrapper> getLeafsToProcess(ICucaGroupWrapper group) {
		// only direct children ... other leafs are processed when iterating over the child groups
		return group.getLeafs().stream()
						.filter(l -> l.getLeaf().getParentContainer() == group.getGroup())
						.collect(Collectors.toList());
	}

	private GenericGroup processGroupAndLeafs(ICucaGroupWrapper group) {

		GenericGroup genericGroup = getOrCreateGenericGroup(group.getGroup());
		collector.addGroup(genericGroup);

		// and its leafs
		List<ICucaLeafWrapper> leafs = getLeafsToProcess(group);
		List<GenericLeaf> genericLeafs = leafs.stream()
						.map(l -> processLeaf(l))
						.collect(Collectors.toList());
		genericLeafs.stream()
						.forEach(genericLeaf -> {
							collector.addEdge(createGenericEdge(genericGroup, GenericEdgeType.HIERARCHY, genericLeaf));
						});

		return genericGroup;
	}

	private void processChildGroup(ICucaGroupWrapper childGroup, GenericGroup owningGroup) {

		GenericGroup genericGroup = processGroupAndLeafs(childGroup);
		collector.addEdge(createGenericEdge(owningGroup, GenericEdgeType.HIERARCHY, genericGroup));

		for (ICucaGroupWrapper nextLevelGroup : childGroup.getChildren()) {
			processChildGroup(nextLevelGroup, genericGroup);
		}
	}

	private void processGroup(ICucaGroupWrapper group) {

		// the group
		GenericGroup genericGroup = processGroupAndLeafs(group);

		for (ICucaGroupWrapper nextLevelGroup : group.getChildren()) {
			processChildGroup(nextLevelGroup, genericGroup);
		}
	}

	private void processDiagram(ICucaDiagramWrapper diagram) {

		initCollector();

		// the diagram
		final GenericDiagram genericDiagram = createGenericDiagram(diagram.getDiagram());
		collector.addDiagram(genericDiagram);

		// direct leafs of diagram
		diagram.getLeafs().stream()
						.filter(leaf -> leaf.getLeaf().getParentContainer().toString().equals("ROOT"))
						.forEach(leaf -> visitCucaLeaf(leaf));

		// direct child groups
		diagram.getGroups().stream()
						.filter(group -> group.getGroup().getParentContainer().toString().equals("ROOT"))
						.forEach(group -> visitCucaGroup(group));

		// the links on the diagram
		diagram.getLinks().stream()
						.forEach(link -> visitCucaLink(link));

		// diagram contains relations
		((SimpleGenericModel) collector).getGroups().stream()
						.forEach(genericGroup ->
										collector.addEdge(createGenericEdge(genericDiagram, GenericEdgeType.DIAGRAM_CONTAINS, genericGroup)));
		((SimpleGenericModel) collector).getLeafs().stream()
						.forEach(genericLeaf ->
										collector.addEdge(createGenericEdge(genericDiagram, GenericEdgeType.DIAGRAM_CONTAINS, genericLeaf)));
		((SimpleGenericModel) collector).getLinks().stream()
						.forEach(genericLink ->
										collector.addEdge(createGenericEdge(genericDiagram, GenericEdgeType.DIAGRAM_CONTAINS, genericLink)));
		((SimpleGenericModel) collector).getStereotypes().stream()
						.forEach(genericStereotype ->
										collector.addEdge(createGenericEdge(genericDiagram, GenericEdgeType.DIAGRAM_CONTAINS, genericStereotype)));
		((SimpleGenericModel) collector).getMembers().stream()
						.forEach(genericMember ->
										collector.addEdge(createGenericEdge(genericDiagram, GenericEdgeType.DIAGRAM_CONTAINS, genericMember)));

		// mark origin of information ... the diagram ID
		((SimpleGenericModel) collector).getEdges().stream()
						.forEach(edge -> {
							((GenericEdge) edge).setDiagram(genericDiagram.getId());
						});

	}

	private GenericModelElement getEntityByPumlId(String pumlId) {
		return processedCucaElementsMap.values().stream()
						.filter(e -> e.getPumlId().equals(pumlId))
						.findFirst().get();
	}


	private GenericLeaf getOrCreateGenericLeaf(ILeaf leaf) {
		GenericModelElement elem = processedCucaElementsMap.get(leaf.getUid());
		if (elem != null) {
			return (GenericLeaf) elem;
		} else {
			return createGenericLeaf(leaf);
		}
	}

	private GenericLeaf createGenericLeaf(ILeaf leaf) {

		GenericLeaf genericLeaf = elementFactory.genericLeafSupplier.get();
		transferCommonAttributes(genericLeaf, leaf);
		if (leaf.getLeafType() == LeafType.JSON) {
			String json = ((BodierJSon) leaf.getBodier()).getJson().toString();
			genericLeaf.setJson(json);
		}
		processedCucaElementsMap.put(leaf.getUid(), genericLeaf);

		return genericLeaf;
	}

	private GenericDiagram createGenericDiagram(CucaDiagram diagram) {

		GenericDiagram genericDiagram = elementFactory.genericDiagramSupplier.get();

		String pumlId = "diag" + this.blockCount;
		String label = this.getFileName();
		genericDiagram.setSourceFile(this.getSourceFile());
		genericDiagram.setPumlId(pumlId);
		genericDiagram.setLabel(label);
		genericDiagram.setPumlRootPath(this.getPumlElementRootPath());
		genericDiagram.setTitle(displayToString(diagram.getTitle().getDisplay()));
		genericDiagram.setHeader(displayToString(diagram.getHeader().getDisplay()));
		genericDiagram.setFooter(displayToString(diagram.getFooter().getDisplay()));
		genericDiagram.setCaption(displayToString(diagram.getCaption().getDisplay()));
		genericDiagram.setLegend(displayToString(diagram.getLegend().getDisplay()));
		genericDiagram.setDiagramType(diagramTypeMap.get(diagram.getClass()));

		return genericDiagram;
	}

	private String displayToString(Display display) {
		// puml uses Lists for textual representations
		// where the user has created line breaks
		// convert to a single JSON String
		if ((display == null) || display.toString().equals("NULL")) {
			return "NULL";
		} else {
			return this.stringToJSONString(display.asList().stream()
							.map(cs -> String.valueOf(cs))
							.collect(Collectors.toList()));
		}
	}
	private String stringToJSONString(String text) {
		// puml uses Lists for textual representations
		// where the user has created line breaks
		// convert to a single JSON String
		// to be consistent in the output we'll apply this also
		// to other strings, e.g. multiplicity annotations at links

		if (text == null) {
			return null;
		}
		// Strings contain "\n" not a newline character
		String[] subs = text.split(Pattern.quote("\\n"));
		return stringToJSONString(Arrays.asList(subs));
	}


	private String stringToJSONString(List<String> texts) {
		// puml uses Lists for textual representations
		// where the user has created line breaks
		// convert to a single JSON String
		// to be consistent in the output we'll apply this also
		// to other strings, e.g. multiplicity annotations at links
		if (texts.size() == 1) {
			return texts.get(0);
		} else {
			return texts.stream()
							.map(s -> "\"" + s + "\"")
							.collect(Collectors.joining(",", "{[", "]}"));
		}
	}
	private GenericGroup getOrCreateGenericGroup(IGroup group) {
		GenericModelElement elem = processedCucaElementsMap.get(group.getUid());
		if (elem != null) {
			return (GenericGroup) elem;
		} else {
			return createGenericGroup(group);
		}
	}

	private GenericGroup createGenericGroup(IGroup group) {

		GenericGroup genericGroup = elementFactory.genericGroupSupplier.get();
		transferCommonAttributes(genericGroup, group);
		processedCucaElementsMap.put(group.getUid(), genericGroup);

		return genericGroup;

	}

	private void transferCommonAttributes(GenericModelElement genericModelElement, IEntity entity){

		genericModelElement.setPumlId(entity.getUid());
		genericModelElement.setPumlRootPath(this.getPumlElementRootPath());
		genericModelElement.setLabel(displayToString(entity.getDisplay()));
		genericModelElement.setType(determineEntityType(entity));
	}

	private GenericStereotype createGenericStereotype(String stereotype, String id) {

		GenericStereotype genericStereotype = elementFactory.genericStereotypeSupplier.get();
		genericStereotype.setLabel(stereotype);
		genericStereotype.setPumlId(id);
		genericStereotype.setPumlRootPath(this.getPumlElementRootPath());
		genericStereotype.setType(GenericEntityType.STEREOTYPE);
		return genericStereotype;
	}

	private List<IGenericEdge> createEdges(GenericModelElement parent, GenericEdgeType type, List<GenericModelElement> children) {

		List<IGenericEdge> edges = new ArrayList<>();
		for (GenericModelElement child : children) {
			IGenericEdge genericEdge = createGenericEdge(parent, type, child);
			edges.add(genericEdge);
		}
		return edges;
	}

	private IGenericEdge createGenericEdge(IGenericModelElement parent, GenericEdgeType type, IGenericModelElement child) {

		GenericEdge genericEdge = elementFactory.genericEdgeSupplier.get();
		genericEdge.setPumlId(parent.getPumlId() + "_edge_" + child.getPumlId());
		genericEdge.setLabel(type.toString());
		genericEdge.setSource(parent.getId());
		genericEdge.setTarget(child.getId());
		genericEdge.setEdgeType(type);
		return genericEdge;
	}

	private GenericEntityType determineEntityType(IEntity entity) {

		IGroup group = null;
		try {
			group = (IGroup) entity;
		} catch (ClassCastException e) {
			// dont'care
		}

		if (entity.getUSymbol() != null) {
			return this.entityTypeMap.getOrDefault(entity.getUSymbol().getSName(), GenericEntityType.UNKNOWN);
		} else if (entity.getLeafType() != null) {
			return this.leafTypeMap.getOrDefault(entity.getLeafType(), GenericEntityType.UNKNOWN);
		}

		if (group != null) {
			if (group.getGroupType().equals(GroupType.PACKAGE)) {
				return GenericEntityType.PACKAGE;
			}
		}

		return GenericEntityType.UNKNOWN;
	}

	private MemberVisibility determineMemberVisibility(String member) {

		int visibilityCount = 0;
		MemberVisibility visibility = null;
		for (Character key : visibilityMap.keySet()) {
			if (member.contains(Character.toString(key))) {
				visibilityCount++;
				visibility = visibilityMap.getOrDefault(key, MemberVisibility.UNDEFINED);
			}
		}
		if (visibilityCount == 1) {
			return visibility;
		}

		if (visibilityCount > 1) {
			Log.error("Multiple visibility modifiers at member " + member);
		}

		// answer undefined if we have multiple or none
		return MemberVisibility.UNDEFINED;
	}

	private GenericLinkStyle determineLinkType(Link link) {

		// The map lookup approach does not work here
		// see TODO at initLinkTypeMap()
		String linkStyleString = link.getType().getStyle().toString();

		if (linkStyleString.contains("INVISIBLE")) {
			return GenericLinkStyle.INVISIBLE;
		}
		if (linkStyleString.contains("DASHED")) {
			return GenericLinkStyle.DASHED;
		}
		if (linkStyleString.contains("DOTTED")) {
			return GenericLinkStyle.DOTTED;
		}
		if (linkStyleString.contains("BOLD")) {
			return GenericLinkStyle.BOLD;
		}
		if (linkStyleString.contains("NORMAL")) {
			return GenericLinkStyle.NORMAL;
		}

		return GenericLinkStyle.UNKNOWN;

	}

	private GenericLinkDecor determineDecorType(LinkDecor linkDecor) {

		return this.decorMap.getOrDefault(linkDecor, GenericLinkDecor.UNKNOWN);

	}

	private GenericLink createGenericLink(Link link) {

		String linkId = link.getEntity1().getUid() + "_" + link.getUid() + "_" + link.getEntity2().getUid();

		GenericLink genericLink = elementFactory.genericLinkSupplier.get();
		genericLink.setPumlId(linkId);
		genericLink.setPumlRootPath(this.getPumlElementRootPath());
		//TODO check later type Display
		genericLink.setLabel(displayToString(link.getLabel()));
		// apparently the decor item is order is reversed compared to the entities
		// we want to have the info in the model consistent, so that everything
		// that is at the source side appears as source (target side similar)
		genericLink.setSourceDecor(determineDecorType(link.getType().getDecor2()));
		genericLink.setTargetDecor(determineDecorType(link.getType().getDecor1()));
		// TODO deal with Middle Decor Classes ... has different type
		genericLink.setMiddleDecor(null);
		genericLink.setSourceLabel(stringToJSONString(link.getQuantifier1()));
		genericLink.setTargetLabel(stringToJSONString(link.getQuantifier2()));
		genericLink.setStyle(determineLinkType(link));
		// TODO deal with direction later
		genericLink.setDirection(link.getLinkArrow().toString());

		return genericLink;
	}

	private String getPumlElementRootPath() {
		// since we may have multiple blocks within a diagram,
		// we add the block count to have a unique path to all puml elements
		return stripRoot(file.substring(0, file.lastIndexOf('.')) + "/" + this.blockCount + "/");

	}

	private String getSourceFile() {
		return stripRoot(file);
	}

	private String stripRoot(String filePath) {
		// to achieve file names relative to a root folder in the project
		// aims at avoiding having user names in the path
		String replace = ".";
		if (graphmlRootDir.endsWith("/")) {
			replace = "./";
		}
		return filePath.replace(graphmlRootDir, replace);
	}
	private String getFileName() {
		String[] tmp = file.split("/");
		return tmp[tmp.length - 1];
	}

	public SimpleGenericModel getModel() {
		return (SimpleGenericModel) collector;
	}

	@Override
	public void visitCucaDiagram(ICucaDiagramWrapper diagram) {
		processDiagram(diagram);
	}

	@Override
	public void visitCucaGroup(ICucaGroupWrapper group) {
		processGroup(group);
	}

	@Override
	public void visitCucaLeaf(ICucaLeafWrapper leaf) {
		processLeaf(leaf);
	}

	@Override
	public void visitCucaLink(ICucaLinkWrapper link) {
		processLink(link);
	}
}


