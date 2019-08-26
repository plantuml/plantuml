/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramTxtMaker;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.jdot.CucaDiagramFileMakerJDot;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.xmi.CucaDiagramXmiMaker;
import net.sourceforge.plantuml.xmlsc.StateDiagramScxmlMaker;

public abstract class CucaDiagram extends UmlDiagram implements GroupHierarchy, PortionShower {

	private int horizontalPages = 1;
	private int verticalPages = 1;

	private final List<HideOrShow2> hides2 = new ArrayList<HideOrShow2>();
	private final List<HideOrShow2> removed = new ArrayList<HideOrShow2>();
	protected final EntityFactory entityFactory = new EntityFactory(hides2, removed);
	private IGroup currentGroup = entityFactory.getRootGroup();

	private boolean visibilityModifierPresent;

	public abstract IEntity getOrCreateLeaf(Code code, LeafType type, USymbol symbol);

	public CucaDiagram(ISkinSimple orig) {
		super(orig);
	}

	private String namespaceSeparator = ".";

	final public void setNamespaceSeparator(String namespaceSeparator) {
		this.namespaceSeparator = namespaceSeparator;
	}

	final public String getNamespaceSeparator() {
		return namespaceSeparator;
	}

	@Override
	public boolean hasUrl() {
		for (IEntity entity : getGroups(true)) {
			if (entity.hasUrl()) {
				return true;
			}
		}
		for (IEntity entity : entityFactory.getLeafsvalues()) {
			if (entity.hasUrl()) {
				return true;
			}
		}
		for (Link link : getLinks()) {
			if (link.hasUrl()) {
				return true;
			}
		}
		return false;
	}

	final protected ILeaf getOrCreateLeafDefault(Code code, LeafType type, USymbol symbol) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		ILeaf result = entityFactory.getLeafsget(code);
		if (result == null) {
			result = createLeafInternal(code, Display.getWithNewlines(code), type, symbol);
			result.setUSymbol(symbol);
		}
		if (result.getLeafType() == LeafType.CLASS && type == LeafType.OBJECT) {
			if (result.muteToType(type, symbol) == false) {
				return null;
			}
		}
		this.lastEntity = result;
		return result;
	}

	public ILeaf createLeaf(Code code, Display display, LeafType type, USymbol symbol) {
		if (entityFactory.getLeafsget(code) != null) {
			return null;
			// throw new IllegalArgumentException("Already known: " + code);
		}
		return createLeafInternal(code, display, type, symbol);
	}

	final protected ILeaf createLeafInternal(Code code, Display display, LeafType type, USymbol symbol) {
		if (Display.isNull(display)) {
			display = Display.getWithNewlines(code);
		}
		final ILeaf leaf = entityFactory.createLeaf(code, display, type, getCurrentGroup(), getHides(),
				getNamespaceSeparator());
		entityFactory.addLeaf(leaf);
		this.lastEntity = leaf;
		leaf.setUSymbol(symbol);
		return leaf;
	}

	public boolean leafExist(Code code) {
		return entityFactory.getLeafsget(code) != null;
	}

	final public Collection<IGroup> getChildrenGroups(IGroup parent) {
		final Collection<IGroup> result = new ArrayList<IGroup>();
		for (IGroup gg : getGroups(false)) {
			if (gg.getParentContainer() == parent) {
				result.add(gg);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	final public void gotoGroup2(Code code, Display display, GroupType type, IGroup parent, NamespaceStrategy strategy) {
		if (strategy == NamespaceStrategy.MULTIPLE) {
			if (getNamespaceSeparator() != null) {
				code = getFullyQualifiedCode(code.withSeparator(getNamespaceSeparator()));
			}
			gotoGroupInternalWithNamespace(code, display, code, type, parent);
		} else if (strategy == NamespaceStrategy.SINGLE) {
			gotoGroupInternal(code, display, null, type, parent);
		} else {
			throw new IllegalArgumentException();
		}
	}

	protected final String getNamespace(Code fullyCode, String separator) {
		String name = fullyCode.getFullName();
		if (separator == null) {
			throw new IllegalArgumentException(toString());
		}
		do {
			final int x = name.lastIndexOf(separator);
			if (x == -1) {
				return null;
			}
			name = name.substring(0, x);
		} while (entityFactory.getLeafsget(Code.of(name, separator)) != null);
		return name;
	}

	final protected void gotoGroupInternalWithNamespace(final Code code, Display display, final Code namespace2,
			GroupType type, IGroup parent) {
		if (getNamespaceSeparator() == null) {
			gotoGroupInternal(code, display, namespace2, type, parent);
			return;
		}

		final String namespace = getNamespace(code, getNamespaceSeparator());
		if (namespace == null) {
			gotoGroupInternal(code, display, namespace2, type, parent);
			return;
		}
		final IGroup realParent = entityFactory.getGroupsget(Code.of(namespace));
		if (realParent == null) {
			gotoGroupInternal(code, display, namespace2, type, parent);
			return;
		}
		display = Display.create(code.getLastPart());
		IGroup result = entityFactory.createGroup(code, display, namespace2, type, realParent, getHides(),
				getNamespaceSeparator());

		entityFactory.addGroup(result);
		currentGroup = result;

	}

	final protected void gotoGroupInternal(final Code code, Display display, final Code namespace2, GroupType type,
			IGroup parent) {
		IGroup result = entityFactory.getGroupsget(code);
		if (result != null) {
			currentGroup = result;
			return;
		}
		if (entityFactory.getLeafsget(code) != null) {
			result = entityFactory.muteToGroup(code, namespace2, type, parent);
			result.setDisplay(display);
		} else {
			result = entityFactory.createGroup(code, display, namespace2, type, parent, getHides(),
					getNamespaceSeparator());
		}
		entityFactory.addGroup(result);
		currentGroup = result;
	}

	public final void gotoThisGroup(IGroup group) {
		currentGroup = group;
	}

	final protected Code getFullyQualifiedCode(Code code) {
		final String separator = code.getSeparator();
		if (separator == null) {
			throw new IllegalArgumentException();
		}
		final String full = code.getFullName();
		if (full.startsWith(separator)) {
			return Code.of(full.substring(separator.length()), separator);
		}
		if (full.contains(separator)) {
			return Code.of(full, separator);
		}
		if (EntityUtils.groupRoot(currentGroup)) {
			return Code.of(full, separator);
		}
		final Code namespace2 = currentGroup.getNamespace2();
		if (namespace2 == null) {
			return Code.of(full, separator);
		}
		return Code.of(namespace2.getFullName() + separator + full, separator);
	}

	public final IGroup getCurrentGroup() {
		return currentGroup;
	}

	public final IGroup getGroup(Code code) {
		final IGroup p = entityFactory.getGroupsget(code);
		if (p == null) {
			throw new IllegalArgumentException();
			// return null;
		}
		return p;
	}

	public void endGroup() {
		if (EntityUtils.groupRoot(currentGroup)) {
			Log.error("No parent group");
			return;
		}
		currentGroup = currentGroup.getParentContainer();
	}

	public final boolean isGroup(Code code) {
		return leafExist(code) == false && entityFactory.getGroupsget(code) != null;
	}

	public final Collection<IGroup> getGroups(boolean withRootGroup) {
		if (withRootGroup == false) {
			return entityFactory.getGroupsvalues();
		}
		final Collection<IGroup> result = new ArrayList<IGroup>();
		result.add(getRootGroup());
		result.addAll(entityFactory.getGroupsvalues());
		return Collections.unmodifiableCollection(result);
	}

	public IGroup getRootGroup() {
		return entityFactory.getRootGroup();
	}

	public Collection<ILeaf> getLeafsvalues() {
		return entityFactory.getLeafsvalues();
	}

	public final int getLeafssize() {
		return getLeafsvalues().size();
	}

	public final ILeaf getLeafsget(Code code) {
		return entityFactory.getLeafsget(code);
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

	final public int getHorizontalPages() {
		return horizontalPages;
	}

	final public void setHorizontalPages(int horizontalPages) {
		this.horizontalPages = horizontalPages;
	}

	final public int getVerticalPages() {
		return verticalPages;
	}

	final public void setVerticalPages(int verticalPages) {
		this.verticalPages = verticalPages;
	}

	@Override
	public int getNbImages() {
		return this.horizontalPages * this.verticalPages;
	}

	// final public List<File> createPng2(File pngFile) throws IOException,
	// InterruptedException {
	// final CucaDiagramPngMaker3 maker = new CucaDiagramPngMaker3(this);
	// return maker.createPng(pngFile);
	// }
	//
	// final public void createPng2(OutputStream os) throws IOException {
	// final CucaDiagramPngMaker3 maker = new CucaDiagramPngMaker3(this);
	// maker.createPng(os);
	// }

	abstract protected List<String> getDotStrings();

	final public String[] getDotStringSkek() {
		final List<String> result = new ArrayList<String>();
		for (String s : getDotStrings()) {
			if (s.startsWith("nodesep") || s.startsWith("ranksep")) {
				result.add(s);
			}
		}
		final String aspect = getPragma().getValue("aspect");
		if (aspect != null) {
			result.add("aspect=" + aspect + ";");
		}
		final String ratio = getPragma().getValue("ratio");
		if (ratio != null) {
			result.add("ratio=" + ratio + ";");
		}
		return result.toArray(new String[result.size()]);
	}

	private void createFilesXmi(OutputStream suggestedFile, FileFormat fileFormat) throws IOException {
		final CucaDiagramXmiMaker maker = new CucaDiagramXmiMaker(this, fileFormat);
		maker.createFiles(suggestedFile);
	}

	private void createFilesScxml(OutputStream suggestedFile) throws IOException {
		final StateDiagramScxmlMaker maker = new StateDiagramScxmlMaker((StateDiagram) this);
		maker.createFiles(suggestedFile);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();

		if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT) {
			try {
				createFilesTxt(os, index, fileFormat);
			} catch (Throwable t) {
				t.printStackTrace(new PrintStream(os));
			}
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

		if (getUmlDiagramType() == UmlDiagramType.COMPOSITE) {
			throw new UnsupportedOperationException();
		}

		// final CucaDiagramFileMaker maker = OptionFlags.USE_HECTOR ? new
		// CucaDiagramFileMakerHectorC1(this)
		// : new CucaDiagramFileMakerSvek(this);
		final CucaDiagramFileMaker maker = this.isUseJDot() ? new CucaDiagramFileMakerJDot(this,
				fileFormatOption.getDefaultStringBounder()) : new CucaDiagramFileMakerSvek(this);
		final ImageData result = maker.createFile(os, getDotStrings(), fileFormatOption);

		if (result == null) {
			return ImageDataSimple.error();
		}
		this.warningOrError = result.getWarningOrError();
		return result;
	}

	private String warningOrError;

	@Override
	public String getWarningOrError() {
		final String generalWarningOrError = super.getWarningOrError();
		if (warningOrError == null) {
			return generalWarningOrError;
		}
		if (generalWarningOrError == null) {
			return warningOrError;
		}
		return generalWarningOrError + BackSlash.NEWLINE + warningOrError;
	}

	private void createFilesTxt(OutputStream os, int index, FileFormat fileFormat) throws IOException {
		final CucaDiagramTxtMaker maker = new CucaDiagramTxtMaker(this, fileFormat);
		maker.createFiles(os, index);
	}

	// public final Rankdir getRankdir() {
	// return rankdir;
	// }
	//
	// public final void setRankdir(Rankdir rankdir) {
	// this.rankdir = rankdir;
	// }

	public boolean isAutarkic(IGroup g) {
		if (g.getGroupType() == GroupType.PACKAGE) {
			return false;
		}
		if (g.getGroupType() == GroupType.INNER_ACTIVITY) {
			return true;
		}
		if (g.getGroupType() == GroupType.CONCURRENT_ACTIVITY) {
			return true;
		}
		if (g.getGroupType() == GroupType.CONCURRENT_STATE) {
			return true;
		}
		if (getChildrenGroups(g).size() > 0) {
			return false;
		}
		for (Link link : getLinks()) {
			if (EntityUtils.isPureInnerLink3(g, link) == false) {
				return false;
			}
		}
		for (ILeaf leaf : g.getLeafsDirect()) {
			if (leaf.getEntityPosition() != EntityPosition.NORMAL) {
				return false;
			}
		}
		return true;
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
			if (isNumber(s)) {
				return s;
			}
		}
		if (getPragma().isDefine("defaultlabeldistance")) {
			final String s = getPragma().getValue("defaultlabeldistance");
			if (isNumber(s)) {
				return s;
			}
		}
		// Default in dot 1.0
		return "1.7";
	}

	public String getLabelangle() {
		if (getPragma().isDefine("labelangle")) {
			final String s = getPragma().getValue("labelangle");
			if (isNumber(s)) {
				return s;
			}
		}
		if (getPragma().isDefine("defaultlabelangle")) {
			final String s = getPragma().getValue("defaultlabelangle");
			if (isNumber(s)) {
				return s;
			}
		}
		// Default in dot -25
		return "25";
	}

	final public boolean isEmpty(IGroup gToTest) {
		for (IEntity gg : getGroups(false)) {
			if (gg == gToTest) {
				continue;
			}
			if (gg.getParentContainer() == gToTest) {
				return false;
			}
		}
		return gToTest.size() == 0;
	}

	public final boolean isVisibilityModifierPresent() {
		return visibilityModifierPresent;
	}

	public final void setVisibilityModifierPresent(boolean visibilityModifierPresent) {
		this.visibilityModifierPresent = visibilityModifierPresent;
	}

	public final boolean showPortion(EntityPortion portion, IEntity entity) {
		if (getSkinParam().strictUmlStyle() && portion == EntityPortion.CIRCLED_CHARACTER) {
			return false;
		}
		boolean result = true;
		for (HideOrShow cmd : hideOrShows) {
			if (cmd.portion == portion && cmd.gender.contains(entity)) {
				result = cmd.show;
			}
		}
		return result;
	}

	public final void hideOrShow(EntityGender gender, Set<EntityPortion> portions, boolean show) {
		for (EntityPortion portion : portions) {
			this.hideOrShows.add(new HideOrShow(gender, portion, show));
		}
	}

	public void hideOrShow(Set<VisibilityModifier> visibilities, boolean show) {
		if (show) {
			hides.removeAll(visibilities);
		} else {
			hides.addAll(visibilities);
		}
	}

	// public void hideOrShow(IEntity leaf, boolean show) {
	// leaf.setRemoved(!show);
	// }

	// public void hideOrShow(Stereotype stereotype, boolean show) {
	// if (show) {
	// hiddenStereotype.remove(stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
	// } else {
	// hiddenStereotype.add(stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
	// }
	// }
	//
	// public void hideOrShow(LeafType leafType, boolean show) {
	// if (show) {
	// hiddenType.remove(leafType);
	// } else {
	// hiddenType.add(leafType);
	// }
	// }

	public void hideOrShow2(String what, boolean show) {
		this.hides2.add(new HideOrShow2(what, show));
	}

	public void removeOrRestore(String what, boolean show) {
		this.removed.add(new HideOrShow2(what, show));
	}

	private final List<HideOrShow> hideOrShows = new ArrayList<HideOrShow>();
	private final Set<VisibilityModifier> hides = new HashSet<VisibilityModifier>();

	static class HideOrShow {
		private final EntityGender gender;
		private final EntityPortion portion;
		private final boolean show;

		public HideOrShow(EntityGender gender, EntityPortion portion, boolean show) {
			this.gender = gender;
			this.portion = portion;
			this.show = show;
		}
	}

	public final Set<VisibilityModifier> getHides() {
		return Collections.unmodifiableSet(hides);
	}

	public ColorMapper getColorMapper() {
		return getSkinParam().getColorMapper();
	}

	final public boolean isStandalone(IEntity ent) {
		for (final Link link : getLinks()) {
			if (link.getEntity1() == ent || link.getEntity2() == ent) {
				return false;
			}
		}
		return true;
	}

	final public Link getLastLink() {
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link link = links.get(i);
			if (link.getEntity1().getLeafType() != LeafType.NOTE && link.getEntity2().getLeafType() != LeafType.NOTE) {
				return link;
			}
		}
		return null;
	}

	private ILeaf lastEntity = null;

	final public ILeaf getLastEntity() {
		// for (final Iterator<ILeaf> it = getLeafs().values().iterator();
		// it.hasNext();) {
		// final ILeaf ent = it.next();
		// if (it.hasNext() == false) {
		// return ent;
		// }
		// }
		// return null;
		return lastEntity;
	}

	final public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	public void applySingleStrategy() {
		final MagmaList magmaList = new MagmaList();

		for (IGroup g : getGroups(true)) {
			final List<ILeaf> standalones = new ArrayList<ILeaf>();
			// final SingleStrategy singleStrategy = g.getSingleStrategy();

			for (ILeaf ent : g.getLeafsDirect()) {
				if (isStandalone(ent)) {
					standalones.add(ent);
				}
			}
			if (standalones.size() < 3) {
				continue;
			}
			final Magma magma = new Magma(this, standalones);
			magma.putInSquare();
			magmaList.add(magma);

			// for (Link link : singleStrategy.generateLinks(standalones)) {
			// addLink(link);
			// }
		}

		for (IGroup g : getGroups(true)) {
			final MagmaList magmas = magmaList.getMagmas(g);
			if (magmas.size() < 3) {
				continue;
			}
			magmas.putInSquare();
		}

	}

	public boolean isHideEmptyDescriptionForState() {
		return false;
	}

	protected void incRawLayout() {
		entityFactory.incRawLayout();
	}

}
