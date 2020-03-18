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
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramTxtMaker;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.jdot.CucaDiagramFileMakerJDot;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.xmi.CucaDiagramXmiMaker;
import net.sourceforge.plantuml.xmlsc.StateDiagramScxmlMaker;

public abstract class CucaDiagram extends UmlDiagram implements GroupHierarchy, PortionShower {

	static private final boolean G1972 = false;

	// private String namespaceSeparator = ".";
	// private String namespaceSeparator1 = GO1972 ? "::" : ".";
	private String namespaceSeparator = null;
	private boolean namespaceSeparatorHasBeenSet = false;

	public final boolean V1972() {
		if (getPragma().backToLegacyPackage()) {
			return false;
		}
		if (getPragma().useNewPackage()) {
			return true;
		}
		if (G1972)
			return true;
		return false;
	}

	public final boolean mergeIntricated() {
		if (getNamespaceSeparator() == null) {
			return false;
		}
		return this.V1972() && this.getUmlDiagramType() == UmlDiagramType.CLASS;
	}

	public Set<SuperGroup> getAllSuperGroups() {
		return entityFactory.getAllSuperGroups();
	}

	private int horizontalPages = 1;
	private int verticalPages = 1;

	private final List<HideOrShow2> hides2 = new ArrayList<HideOrShow2>();
	private final List<HideOrShow2> removed = new ArrayList<HideOrShow2>();
	protected final EntityFactory entityFactory = new EntityFactory(hides2, removed, this);
	private IGroup currentGroup = entityFactory.getRootGroup();
	private List<Ident> stacks2 = new ArrayList<Ident>();
	private List<IGroup> stacks = new ArrayList<IGroup>();

	private boolean visibilityModifierPresent;

	public abstract IEntity getOrCreateLeaf(Ident ident, Code code, LeafType type, USymbol symbol);

	public Ident cleanIdent(Ident ident) {
		return ident;
	}

	public CucaDiagram(ISkinSimple orig) {
		super(orig);
		this.stacks2.add(Ident.empty());
	}

	private Ident getLastID() {
		if (stacks2.size() == 0) {
			// Thread.dumpStack();
			return Ident.empty();
			// throw new IllegalArgumentException();
		}
		return this.stacks2.get(stacks2.size() - 1);
	}

	final public void setNamespaceSeparator(String namespaceSeparator) {
		this.namespaceSeparatorHasBeenSet = true;
		this.namespaceSeparator = namespaceSeparator;
	}

	final public String getNamespaceSeparator() {
		if (namespaceSeparatorHasBeenSet == false) {
			return V1972() ? "::" : ".";
		}
		return namespaceSeparator;
	}

	@Override
	public boolean hasUrl() {
		for (IEntity entity : getGroups(true)) {
			if (entity.hasUrl()) {
				return true;
			}
		}
		for (IEntity entity : entityFactory.leafs()) {
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

	final public void setLastEntity(ILeaf foo) {
		this.lastEntity = foo;
	}

	final protected ILeaf getOrCreateLeafDefault(Ident idNewLong, Code code, LeafType type, USymbol symbol) {
		checkNotNull(idNewLong);
		if (type == null) {
			throw new IllegalArgumentException();
		}
		ILeaf result;
		if (this.V1972())
			result = entityFactory.getLeafStrict(idNewLong);
		else
			result = entityFactory.getLeaf(code);
		if (result == null) {
			result = createLeafInternal(idNewLong, code, Display.getWithNewlines(code), type, symbol);
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

	public ILeaf createLeaf(Ident idNewLong, Code code, Display display, LeafType type, USymbol symbol) {
		checkNotNull(idNewLong);
		if (entityFactory.getLeafStrict(idNewLong) != null) {
			return null;
			// throw new IllegalArgumentException("Already known: " + code);
		}
		return createLeafInternal(idNewLong, code, display, type, symbol);
	}

	final protected ILeaf createLeafInternal(Ident newIdent, Code code, Display display, LeafType type,
			USymbol symbol) {
		checkNotNull(newIdent);
		if (Display.isNull(display)) {
			display = Display.getWithNewlines(code).withCreoleMode(CreoleMode.SIMPLE_LINE);
		}
		final ILeaf leaf = entityFactory.createLeaf(newIdent, code, display, type, getCurrentGroup(), getHides(),
				getNamespaceSeparator());
		entityFactory.addLeaf(leaf);
		this.lastEntity = leaf;
		leaf.setUSymbol(symbol);
		return leaf;
	}

	final public Ident buildLeafIdent(String id) {
		return getLastID().add(id, getNamespaceSeparator());
	}

	final public Ident buildLeafIdentSpecial(String id) {
		return buildFullyQualified(id);
		// if (namespaceSeparator != null) {
		// if (id.contains(namespaceSeparator)) {
		// return Ident.empty().add(id, namespaceSeparator);
		// }
		// }
		// return getLastID().add(id, namespaceSeparator);
	}

	final public Ident buildFullyQualified(String id) {
		return entityFactory.buildFullyQualified(getLastID(), Ident.empty().add(id, getNamespaceSeparator()));
	}

	final public Code buildCode(String s) {
		if (this.V1972())
			throw new UnsupportedOperationException();
		return CodeImpl.of(s);
	}

	protected final void checkNotNull(Object id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
	}

	public boolean leafExist(Code code) {
		if (this.V1972())
			throw new UnsupportedOperationException();
		return entityFactory.getLeaf(code) != null;
	}

	public boolean leafExistSmart(Ident ident) {
		return entityFactory.getLeafSmart(ident) != null;
	}

	public boolean leafExistStrict(Ident ident) {
		return entityFactory.getLeafStrict(ident) != null;
	}

	final public Collection<IGroup> getChildrenGroups(IGroup parent) {
		if (this.V1972())
			return getChildrenGroupsIdent1972(parent);
		final Collection<IGroup> result = new ArrayList<IGroup>();
		for (IGroup gg : getGroups(false)) {
			if (gg.getParentContainer() == parent) {
				result.add(gg);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	private Collection<IGroup> getChildrenGroupsIdent1972(IGroup parent) {
		final Collection<IGroup> result = new ArrayList<IGroup>();
		for (IGroup gg : entityFactory.groups2()) {
			if (gg.getIdent().parent().equals(parent.getIdent())) {
				result.add(gg);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	final public void gotoGroup(Ident ident, Code code, Display display, GroupType type, IGroup parent,
			NamespaceStrategy strategy) {
		if (this.V1972()) {
			gotoGroupInternalWithNamespace(ident, code, display, code, type, parent);
			return;

		}
		if (strategy == NamespaceStrategy.MULTIPLE) {
			if (getNamespaceSeparator() != null) {
				code = getFullyQualifiedCode1972(code);
			}
			gotoGroupInternalWithNamespace(ident, code, display, code, type, parent);
		} else if (strategy == NamespaceStrategy.SINGLE) {
			final Ident newIdLong = buildLeafIdentSpecial(ident.toString(this.getNamespaceSeparator()));
			gotoGroupExternal(newIdLong, code, display, null, type, parent);
			stacks2.add(newIdLong);
		} else {
			throw new IllegalArgumentException();
		}
	}

	protected final String getNamespace1972(Code fullyCode, String separator) {
		String name = fullyCode.getName();
		if (separator == null) {
			throw new IllegalArgumentException(toString());
		}
		do {
			final int x = name.lastIndexOf(separator);
			if (x == -1) {
				return null;
			}
			name = name.substring(0, x);
		} while (entityFactory.getLeaf(buildCode(name)) != null);
		return name;
	}

	private void gotoGroupInternalWithNamespace(Ident idNewLong, Code code, Display display, Code namespaceNew,
			GroupType type, IGroup parent) {
		this.stacks.add(currentGroup);
		this.stacks2.add(idNewLong);

		if (this.V1972()) {
			gotoGroupInternal(idNewLong, code, display, namespaceNew, type, parent);
			return;
		}
		if (getNamespaceSeparator() == null) {
			gotoGroupInternal(idNewLong, code, display, namespaceNew, type, parent);
			return;
		}

		final String namespaceCurrent = getNamespace1972(code, getNamespaceSeparator());
		if (namespaceCurrent == null) {
			gotoGroupInternal(idNewLong, code, display, namespaceNew, type, parent);
			return;
		}
		final IGroup realParent = entityFactory.getGroup(buildCode(namespaceCurrent));
		if (realParent == null) {
			gotoGroupInternal(idNewLong, code, display, namespaceNew, type, parent);
			return;
		}
		display = Display.create(idNewLong.getLast());
		IGroup result = entityFactory.createGroup(idNewLong, code, display, namespaceNew, type, realParent, getHides(),
				getNamespaceSeparator());

		entityFactory.addGroup(result);
		currentGroup = result;

	}

	public void endGroup() {
		if (stacks2.size() > 0) {
			// Thread.dumpStack();
			stacks2.remove(stacks2.size() - 1);
		}
		if (EntityUtils.groupRoot(currentGroup)) {
			Log.error("No parent group");
			return;
		}
		if (stacks.size() > 0) {
			currentGroup = stacks.remove(stacks.size() - 1);
		} else {
			currentGroup = currentGroup.getParentContainer();
		}
	}

	private void gotoGroupInternal(Ident idNewLong, final Code code, Display display, final Code namespace,
			GroupType type, IGroup parent) {
		if (this.V1972()) {
			gotoGroupInternal1972(idNewLong, code, display, namespace, type, parent);
			return;
		}

		IGroup result = entityFactory.getGroup(code);
		if (result != null) {
			currentGroup = result;
			return;
		}
		if (entityFactory.getLeafStrict(idNewLong) != null) {
			result = entityFactory.muteToGroup(code.getName(), namespace, type, parent);
			result.setDisplay(display);
		} else {
			result = entityFactory.createGroup(idNewLong, code, display, namespace, type, parent, getHides(),
					getNamespaceSeparator());
		}
		entityFactory.addGroup(result);
		currentGroup = result;
	}

	private void gotoGroupInternal1972(Ident idNewLong, final Code code, Display display, final Code namespace,
			GroupType type, IGroup parent) {
		IGroup result = entityFactory.getGroupStrict(idNewLong);
		if (result != null) {
			currentGroup = result;
			return;
		}
		final boolean mutation;
		if (getNamespaceSeparator() == null)
			mutation = entityFactory.getLeafVerySmart(idNewLong) != null;
		else
			mutation = entityFactory.getLeafStrict(idNewLong) != null;
		if (mutation) {
			result = entityFactory.muteToGroup1972(idNewLong, namespace, type, parent);
			result.setDisplay(display);
		} else {
			result = entityFactory.createGroup(idNewLong, code, display, namespace, type, parent, getHides(),
					getNamespaceSeparator());
		}
		entityFactory.addGroup(result);
		currentGroup = result;
		stacks2.set(stacks2.size() - 1, result.getIdent());
	}

	final protected void gotoGroupExternal(Ident newIdLong, final Code code, Display display, final Code namespace,
			GroupType type, IGroup parent) {
		IGroup result = entityFactory.getGroup(code);
		if (result != null) {
			currentGroup = result;
			return;
		}
		if (entityFactory.getLeaf(code) != null) {
			result = entityFactory.muteToGroup(code.getName(), namespace, type, parent);
			result.setDisplay(display);
		} else {
			result = entityFactory.createGroup(newIdLong, code, display, namespace, type, parent, getHides(),
					getNamespaceSeparator());
		}
		entityFactory.addGroup(result);
		// entityFactory.thisIsNotArealGroup(newIdLong);
		currentGroup = result;
	}

	public final void gotoThisGroup(IGroup group) {
		currentGroup = group;
	}

	final protected Code getFullyQualifiedCode1972(Code code) {
		final String separator = getNamespaceSeparator();
		if (separator == null) {
			throw new IllegalArgumentException();
		}
		final String full = code.getName();
		if (full.startsWith(separator)) {
			return buildCode(full.substring(separator.length()));
		}
		if (full.contains(separator)) {
			return buildCode(full);
		}
		if (EntityUtils.groupRoot(currentGroup)) {
			return buildCode(full);
		}
		final Code namespace = currentGroup.getNamespace();
		if (namespace == null) {
			return buildCode(full);
		}
		return buildCode(namespace.getName() + separator + full);
	}

	public final IGroup getCurrentGroup() {
		return currentGroup;
	}

	public final IGroup getGroup(Code code) {
		final IGroup p = entityFactory.getGroup(code);
		if (p == null) {
			throw new IllegalArgumentException();
			// return null;
		}
		return p;
	}

	public final IGroup getGroupStrict(Ident ident) {
		if (!this.V1972())
			throw new UnsupportedOperationException();
		final IGroup p = entityFactory.getGroupStrict(ident);
		if (p == null) {
			throw new IllegalArgumentException();
			// return null;
		}
		return p;
	}

	public final IGroup getGroupVerySmart(Ident ident) {
		if (!this.V1972())
			throw new UnsupportedOperationException();
		final IGroup p = entityFactory.getGroupVerySmart(ident);
		if (p == null) {
			throw new IllegalArgumentException();
			// return null;
		}
		return p;
	}

	public final boolean isGroup(Code code) {
		if (this.V1972())
			return isGroupStrict((Ident) code);
		return leafExist(code) == false && entityFactory.getGroup(code) != null;
	}

	public final boolean isGroupStrict(Ident ident) {
		if (!this.V1972())
			throw new UnsupportedOperationException();
		return leafExistStrict(ident) == false && entityFactory.getGroupStrict(ident) != null;
	}

	public final boolean isGroupVerySmart(Ident ident) {
		if (!this.V1972())
			throw new UnsupportedOperationException();
		return leafExistSmart(ident) == false && entityFactory.getGroupVerySmart(ident) != null;
	}

	public final Collection<IGroup> getGroups(boolean withRootGroup) {
		if (withRootGroup == false) {
			return entityFactory.groups();
		}
		final Collection<IGroup> result = new ArrayList<IGroup>();
		result.add(getRootGroup());
		result.addAll(entityFactory.groups());
		return Collections.unmodifiableCollection(result);
	}

	public IGroup getRootGroup() {
		return entityFactory.getRootGroup();
	}

	public SuperGroup getRootSuperGroup() {
		return entityFactory.getRootSuperGroup();
	}

	public final Collection<ILeaf> getLeafsvalues() {
		return entityFactory.leafs2();
	}

	public final int getLeafssize() {
		return getLeafsvalues().size();
	}

	public final ILeaf getLeaf(Code code) {
		return entityFactory.getLeaf(code);
	}

	public final ILeaf getLeafStrict(Ident ident) {
		return entityFactory.getLeafStrict(ident);
	}

	public final ILeaf getLeafSmart(Ident ident) {
		return entityFactory.getLeafSmart(ident);
	}

	public /* final */ ILeaf getLeafVerySmart(Ident ident) {
		return entityFactory.getLeafVerySmart(ident);
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

		entityFactory.buildSuperGroups();

		final CucaDiagramFileMaker maker = this.isUseJDot()
				? new CucaDiagramFileMakerJDot(this, fileFormatOption.getDefaultStringBounder())
				: new CucaDiagramFileMakerSvek(this);
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

	public final void hideOrShow(EntityGender gender, EntityPortion portions, boolean show) {
		for (EntityPortion portion : portions.asSet()) {
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

	final public List<Link> getTwoLastLinks() {
		final List<Link> result = new ArrayList<Link>();
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link link = links.get(i);
			if (link.getEntity1().getLeafType() != LeafType.NOTE && link.getEntity2().getLeafType() != LeafType.NOTE) {
				result.add(link);
				if (result.size() == 2) {
					return Collections.unmodifiableList(result);
				}
			}
		}
		return null;
	}

	private ILeaf lastEntity = null;

	final public ILeaf getLastEntity() {
		return lastEntity;
	}

	final public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	public void applySingleStrategy() {
		final MagmaList magmaList = new MagmaList();

		for (IGroup g : getGroups(true)) {
			final List<ILeaf> standalones = new ArrayList<ILeaf>();

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

	public CommandExecutionResult constraintOnLinks(Link link1, Link link2, Display display) {
		final LinkConstraint linkConstraint = new LinkConstraint(link1, link2, display);
		link1.setLinkConstraint(linkConstraint);
		link2.setLinkConstraint(linkConstraint);
		return CommandExecutionResult.ok();
	}

}
