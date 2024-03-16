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
package net.atmp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.abel.Bag;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityFactory;
import net.sourceforge.plantuml.abel.EntityGender;
import net.sourceforge.plantuml.abel.EntityPortion;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.Together;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.HideOrShow;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.cucadiagram.LinkConstraint;
import net.sourceforge.plantuml.cucadiagram.Magma;
import net.sourceforge.plantuml.cucadiagram.MagmaList;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.dot.CucaDiagramTxtMaker;
import net.sourceforge.plantuml.elk.CucaDiagramFileMakerElk;
import net.sourceforge.plantuml.graphml.CucaDiagramGraphmlMaker;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.sdot.CucaDiagramFileMakerSmetana;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.xmi.CucaDiagramXmiMaker;
import net.sourceforge.plantuml.xmlsc.StateDiagramScxmlMaker;

public abstract class CucaDiagram extends UmlDiagram implements GroupHierarchy, PortionShower, ICucaDiagram {

	private final List<HideOrShow> hides2 = new ArrayList<>();
	private final List<HideOrShow> removed = new ArrayList<>();
	protected final EntityFactory entityFactory = new EntityFactory(hides2, removed, this);

	private List<Bag> stacks = new ArrayList<>();

	private boolean visibilityModifierPresent;

	@Override
	final public void setNamespaceSeparator(String namespaceSeparator) {
		super.setNamespaceSeparator(namespaceSeparator);
		entityFactory.setSeparator(namespaceSeparator);
	}

	public CucaDiagram(UmlSource source, UmlDiagramType type, Map<String, String> orig) {
		super(source, type, orig);
		this.stacks.add(entityFactory.root().getData());
	}

	public String getPortFor(String entString, Quark<Entity> ident) {
		final int x = entString.lastIndexOf("::");
		if (x == -1)
			return null;
		if (entString.startsWith(ident.getName()))
			return entString.substring(x + 2);
		return null;
	}

	public final Entity getCurrentGroup() {
		int pos = stacks.size() - 1;
		while (pos >= 0) {
			final Bag tmp = this.stacks.get(pos);
			if (tmp instanceof Entity)
				return (Entity) tmp;
			pos--;
		}
		throw new IllegalStateException();
	}

	public final Together currentTogether() {
		final int pos = stacks.size() - 1;
		final Bag tmp = this.stacks.get(pos);
		if (tmp instanceof Together)
			return (Together) tmp;
		return null;
	}

	public String cleanId(String id) {
		if (id == null)
			return null;
		return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(id);
	}

	@Override
	public boolean hasUrl() {
		for (Quark<Entity> quark : entityFactory.quarks()) {
			final Entity ent = quark.getData();
			if (ent != null && ent.hasUrl())
				return true;
		}

		return false;
	}

	final public void setLastEntity(Entity foo) {
		this.lastEntity = (Entity) foo;
	}

	protected void updateLasts(Entity result) {
	}

	final public Entity reallyCreateLeaf(Quark<Entity> ident, Display display, LeafType type, USymbol symbol) {
		Objects.requireNonNull(type);
		if (ident.getData() != null)
			throw new IllegalStateException();
		if (Display.isNull(display))
			throw new IllegalArgumentException();

		final Entity result = entityFactory.createLeaf(ident, type, getHides());
		result.setUSymbol(symbol);
		this.lastEntity = result;

		result.setTogether(currentTogether());

		updateLasts(result);
//			if (type == LeafType.OBJECT)
//				((EntityImp) parent.getData()).muteToType2(type);
		result.setDisplay(display);

		if (type.isLikeClass())
			eventuallyBuildPhantomGroups();

		return result;

	}

	final public Quark<Entity> quarkInContext(boolean reuseExistingChild, String full) {
		final String sep = getNamespaceSeparator();
		if (sep == null) {
			final Quark<Entity> result = entityFactory.firstWithName(full);
			if (result != null)
				return result;
			return getCurrentGroup().getQuark().child(full);
		}

		final Quark<Entity> currentQuark = getCurrentGroup().getQuark();
		if (full.startsWith(sep))
			return entityFactory.root().child(full.substring(sep.length()));
		final int x = full.indexOf(sep);
		if (x == -1) {
			if (reuseExistingChild && entityFactory.countByName(full) == 1) {
				final Quark<Entity> byName = entityFactory.firstWithName(full);
				assert byName != null;
				if (byName != currentQuark)
					return byName;
			}
			return currentQuark.child(full);
		}

		final String first = full.substring(0, x);
		final boolean firstPackageDoesExist = entityFactory.root().childIfExists(first) != null;

		if (firstPackageDoesExist)
			return entityFactory.root().child(full);
		return currentQuark.child(full);

	}

	public String removePortId(String id) {
		// To be kept
		if ("::".equals(getNamespaceSeparator()))
			return id;
		final int x = id.lastIndexOf("::");
		if (x == -1)
			return id;
		return id.substring(0, x);
	}

	public String getPortId(String id) {
		// To be kept
		if ("::".equals(getNamespaceSeparator()))
			return null;
		final int x = id.lastIndexOf("::");
		if (x == -1)
			return null;
		return id.substring(x + 2);
	}

	public Quark<Entity> firstWithName(String name) {
		return entityFactory.firstWithName(name);
	}

	@Override
	final public Collection<Entity> getChildrenGroups(Entity entity) {
		return entity.groups();
	}

	private void eventuallyBuildPhantomGroups() {
		for (Quark<Entity> quark : entityFactory.quarks()) {
			if (quark.getData() != null)
				continue;
			int countChildren = quark.countChildren();
			if (countChildren > 0) {
				// final Display display = Display.getWithNewlines(quark.getQualifiedName());
				final Display display = Display.getWithNewlines(quark.getName());
				final Entity result = entityFactory.createGroup(quark, GroupType.PACKAGE, getHides());
				result.setDisplay(display);
			}
		}
	}

	final public CommandExecutionResult gotoTogether() {
		this.stacks.add(new Together(currentTogether()));
		return CommandExecutionResult.ok();
	}

	final public CommandExecutionResult gotoGroup(Quark<Entity> quark, Display display, GroupType type) {
		if (quark.getData() == null) {
			final Entity result = entityFactory.createGroup(quark, type, getHides());
			result.setTogether(currentTogether());
			result.setDisplay(display);
		}
		final Entity ent = quark.getData();
		ent.muteToGroupType(type);

		this.stacks.add(quark.getData());

		return CommandExecutionResult.ok();

	}

	public boolean endGroup() {
		if (stacks.size() > 0) {
			stacks.remove(stacks.size() - 1);
			return true;
		}
		return false;

	}

	public final Entity getGroup(String code) {
		final Quark<Entity> quark = entityFactory.firstWithName(code);
		if (quark == null)
			return null;
		return quark.getData();
	}

	public final boolean isGroup(String code) {
		final Quark<Entity> quark = entityFactory.firstWithName(code);
		if (quark == null)
			return false;
		return isGroup(quark);
	}

	public final boolean isGroup(Quark<Entity> quark) {
		final Entity ent = quark.getData();
		if (ent == null)
			return false;
		return ent.isGroup();
	}

	@Override
	public Entity getRootGroup() {
		return entityFactory.root().getData();
	}

	final public void addLink(Link link) {
		entityFactory.addLink(link);
	}

	final protected void removeLink(Link link) {
		entityFactory.removeLink(link);
	}

	final public List<Link> getLinks() {
		return entityFactory.getLinks();
	}

	abstract protected List<String> getDotStrings();

	final public String[] getDotStringSkek() {
		final List<String> result = new ArrayList<>();
		for (String s : getDotStrings())
			if (s.startsWith("nodesep") || s.startsWith("ranksep") || s.startsWith("layout"))
				result.add(s);

		String aspect = getPragma().getValue("aspect");
		if (aspect != null) {
			aspect = aspect.replace(',', '.');
			result.add("aspect=" + aspect + ";");
		}
		final String ratio = getPragma().getValue("ratio");
		if (ratio != null)
			result.add("ratio=" + ratio + ";");

		return result.toArray(new String[result.size()]);
	}

	// ::comment when __CORE__
	private void createFilesGraphml(OutputStream suggestedFile) throws IOException {
		final CucaDiagramGraphmlMaker maker = new CucaDiagramGraphmlMaker(this);
		maker.createFiles(suggestedFile);
	}

	private void createFilesXmi(OutputStream suggestedFile, FileFormat fileFormat) throws IOException {
		final CucaDiagramXmiMaker maker = new CucaDiagramXmiMaker(this, fileFormat);
		maker.createFiles(suggestedFile);
	}

	private void createFilesScxml(OutputStream suggestedFile) throws IOException {
		final StateDiagramScxmlMaker maker = new StateDiagramScxmlMaker((StateDiagram) this);
		maker.createFiles(suggestedFile);
	}

	private void createFilesTxt(OutputStream os, int index, FileFormat fileFormat) throws IOException {
		final CucaDiagramTxtMaker maker = new CucaDiagramTxtMaker(this, fileFormat);
		maker.createFiles(os, index);
	}
	// ::done

	@Override
	final public void exportDiagramGraphic(UGraphic ug, FileFormatOption fileFormatOption) {
		final CucaDiagramFileMaker maker = new CucaDiagramFileMakerSmetana(this, ug.getStringBounder());
		maker.createOneGraphic(ug);
	}

	@Override
	final protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();

		// ::comment when __CORE__
		if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT) {
			try {
				createFilesTxt(os, index, fileFormat);
			} catch (Throwable t) {
				t.printStackTrace(SecurityUtils.createPrintStream(os));
			}
			return ImageDataSimple.ok();
		}

		if (fileFormat == FileFormat.GRAPHML) {
			createFilesGraphml(os);
			return ImageDataSimple.ok();
		}

		if (fileFormat.name().startsWith("XMI")) {
			createFilesXmi(os, fileFormat);
			return ImageDataSimple.ok();
		}

		if (fileFormat == FileFormat.SCXML) {
			createFilesScxml(os);
			return ImageDataSimple.ok();
		}
		// ::done

		if (getUmlDiagramType() == UmlDiagramType.COMPOSITE) {
			throw new UnsupportedOperationException();
		}

		this.eventuallyBuildPhantomGroups();
		final CucaDiagramFileMaker maker;
		// ::comment when __CORE__
		if (this.isUseElk())
			maker = new CucaDiagramFileMakerElk(this, fileFormatOption.getDefaultStringBounder(getSkinParam()));
		else if (this.isUseSmetana())
			// ::done
			maker = new CucaDiagramFileMakerSmetana(this, fileFormatOption.getDefaultStringBounder(getSkinParam()));
		// ::comment when __CORE__
		else
			maker = new CucaDiagramFileMakerSvek(this);
		// ::done

		final ImageData result = maker.createFile(os, getDotStrings(), fileFormatOption);

		if (result == null)
			return ImageDataSimple.error();

		this.warningOrError = result.getWarningOrError();
		return result;
	}

	private String warningOrError;

	@Override
	public String getWarningOrError() {
		final String generalWarningOrError = super.getWarningOrError();
		if (warningOrError == null)
			return generalWarningOrError;

		if (generalWarningOrError == null)
			return warningOrError;

		return generalWarningOrError + BackSlash.NEWLINE + warningOrError;
	}

	private static boolean isNumber(String s) {
		return s.matches("[+-]?(\\.?\\d+|\\d+\\.\\d*)");
	}

	public void resetPragmaLabel() {
		getPragma().undefine("labeldistance");
		getPragma().undefine("labelangle");
	}

	public String getLabeldistance() {
		if (getPragma().isDefine("labeldistance")) {
			final String s = getPragma().getValue("labeldistance");
			if (isNumber(s))
				return s;

		}
		if (getPragma().isDefine("defaultlabeldistance")) {
			final String s = getPragma().getValue("defaultlabeldistance");
			if (isNumber(s))
				return s;

		}
		// Default in dot 1.0
		return "1.7";
	}

	public String getLabelangle() {
		if (getPragma().isDefine("labelangle")) {
			final String s = getPragma().getValue("labelangle");
			if (isNumber(s))
				return s;

		}
		if (getPragma().isDefine("defaultlabelangle")) {
			final String s = getPragma().getValue("defaultlabelangle");
			if (isNumber(s))
				return s;

		}
		// Default in dot -25
		return "25";
	}

	@Override
	final public boolean isEmpty(Entity entity) {
		return entity.isEmpty();
	}

	public final boolean isVisibilityModifierPresent() {
		return visibilityModifierPresent;
	}

	public final void setVisibilityModifierPresent(boolean visibilityModifierPresent) {
		this.visibilityModifierPresent = visibilityModifierPresent;
	}

	public final boolean showPortion(EntityPortion portion, Entity entity) {
		if (getSkinParam().strictUmlStyle() && portion == EntityPortion.CIRCLED_CHARACTER)
			return false;

		boolean result = true;
		for (EntityHideOrShow cmd : hideOrShows)
			if (cmd.portion == portion && cmd.gender.contains(entity))
				result = cmd.show;

		return result;
	}

	@Override
	public List<String> getVisibleStereotypeLabels(Entity entity) {
		Stereotype stereotype = entity.getStereotype();

		if (stereotype == null) {
			return null;
		}

		List<EntityHideOrShow> commands = new ArrayList<>();
		for (EntityHideOrShow hideOrShowEntry : hideOrShows) {
			if (hideOrShowEntry.portion == EntityPortion.STEREOTYPE) {
				commands.add(hideOrShowEntry);
			}
		}

		List<String> visibleStereotypeLabels = new ArrayList<>();
		for (String stereoTypeLabel : entity.getStereotype().getLabels(Guillemet.DOUBLE_COMPARATOR)) {
			if (isHiddenStereotypeLabel(stereoTypeLabel, commands)) {
				visibleStereotypeLabels.add(stereoTypeLabel);
			}
		}

		return visibleStereotypeLabels;
	}

	private boolean isHiddenStereotypeLabel(String stereoTypeLabel, List<EntityHideOrShow> commands) {
		for (EntityHideOrShow cmd : commands) {
			String gender = cmd.gender.getGender();
			if (gender != null && gender.equals(stereoTypeLabel)) {
				return false;
			}
		}
		return true;
	}

	public final void hideOrShow(EntityGender gender, EntityPortion portions, boolean show) {
		for (EntityPortion portion : portions.asSet())
			this.hideOrShows.add(new EntityHideOrShow(gender, portion, show));

	}

	public void hideOrShow(Set<VisibilityModifier> visibilities, boolean show) {
		if (show)
			hides.removeAll(visibilities);
		else
			hides.addAll(visibilities);
	}

	public void hideOrShow2(String what, boolean show) {
		this.hides2.add(new HideOrShow(what, show));
	}

	public void removeOrRestore(String what, boolean show) {
		this.removed.add(new HideOrShow(what, show));
	}

	private final List<EntityHideOrShow> hideOrShows = new ArrayList<>();
	private final Set<VisibilityModifier> hides = new HashSet<>();

	static class EntityHideOrShow {
		private final EntityGender gender;
		private final EntityPortion portion;
		private final boolean show;

		public EntityHideOrShow(EntityGender gender, EntityPortion portion, boolean show) {
			this.gender = gender;
			this.portion = portion;
			this.show = show;
		}
	}

	public final Set<VisibilityModifier> getHides() {
		return Collections.unmodifiableSet(hides);
	}

	final public boolean isStandalone(Entity ent) {
		for (final Link link : getLinks())
			if (link.getEntity1() == ent || link.getEntity2() == ent)
				return false;

		return true;
	}

	final public boolean isStandaloneForArgo(Entity ent) {
		for (final Link link : getLinks()) {
			if (link.isHidden() || link.isInvis())
				continue;
			if (link.getEntity1() == ent || link.getEntity2() == ent)
				return false;
		}

		return true;
	}

	final public Link getLastLink() {
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link link = links.get(i);
			if (link.getEntity1().getLeafType() != LeafType.NOTE && link.getEntity2().getLeafType() != LeafType.NOTE)
				return link;

		}
		return null;
	}

	final public List<Link> getTwoLastLinks() {
		final List<Link> result = new ArrayList<>();
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link link = links.get(i);
			if (link.getEntity1().getLeafType() != LeafType.NOTE && link.getEntity2().getLeafType() != LeafType.NOTE) {
				result.add(link);
				if (result.size() == 2)
					return Collections.unmodifiableList(result);

			}
		}
		return null;
	}

	protected Entity lastEntity = null;

	final public Entity getLastEntity() {
		return lastEntity;
	}

	final public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	public void applySingleStrategy() {
		final MagmaList magmaList = new MagmaList();

		final Collection<Entity> groups = getEntityFactory().groupsAndRoot();
		for (Entity g : groups) {
			final List<Entity> standalones = new ArrayList<>();

			for (Entity ent : g.leafs())
				if (isStandalone(ent))
					standalones.add(ent);

			if (standalones.size() < 3)
				continue;

			final Magma magma = new Magma(this, standalones);
			magma.putInSquare();
			magmaList.add(magma);
		}

		for (Entity g : groups) {
			final MagmaList magmas = magmaList.getMagmas(g);
			if (magmas.size() < 3)
				continue;

			magmas.putInSquare();
		}

	}

	public boolean isHideEmptyDescriptionForState() {
		return false;
	}

	protected void incRawLayout() {
		entityFactory.incRawLayout();
	}

	public CommandExecutionResult constraintOnLinks(Link link1, Link link2, Display display) {
		final LinkConstraint linkConstraint = new LinkConstraint(link1, link2, display);
		link1.setLinkConstraint(linkConstraint);
		link2.setLinkConstraint(linkConstraint);
		return CommandExecutionResult.ok();
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		// Strange numbers here for backwards compatibility
		return ClockwiseTopRightBottomLeft.topRightBottomLeft(0, 5, 5, 0);
	}

	private final AtomicInteger cpt = new AtomicInteger(1);

	public int getUniqueSequence() {
		return cpt.addAndGet(1);
	}

	public String getUniqueSequence(String prefix) {
		return prefix + getUniqueSequence();
	}

}
