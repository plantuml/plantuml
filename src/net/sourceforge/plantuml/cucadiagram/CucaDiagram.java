/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 11914 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramTxtMaker;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.hector.CucaDiagramFileMakerHectorB2;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.xmi.CucaDiagramXmiMaker;

public abstract class CucaDiagram extends UmlDiagram implements GroupHierarchy, PortionShower {

	private int horizontalPages = 1;
	private int verticalPages = 1;

	private final EntityFactory entityFactory = new EntityFactory();
	private IGroup currentGroup = entityFactory.getRootGroup();

	private Rankdir rankdir = Rankdir.TOP_TO_BOTTOM;

	private boolean visibilityModifierPresent;

	public abstract IEntity getOrCreateLeaf(Code code, LeafType type);

	@Override
	public boolean hasUrl() {
		for (IEntity entity : getGroups(true)) {
			if (entity.hasUrl()) {
				return true;
			}
		}
		for (IEntity entity : getLeafs().values()) {
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

	// public ILeaf getOrCreateLeaf1(Code code, LeafType type) {
	// return getOrCreateLeaf1Default(code, type);
	// }

	final protected ILeaf getOrCreateLeafDefault(Code code, LeafType type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		ILeaf result = getLeafs().get(code);
		if (result == null) {
			result = createLeafInternal(code, Display.getWithNewlines(code), type, getCurrentGroup());
		}
		this.lastEntity = result;
		return result;
	}

	public ILeaf createLeaf(Code code, Display display, LeafType type) {
		if (getLeafs().containsKey(code)) {
			throw new IllegalArgumentException("Already known: " + code);
		}
		return createLeafInternal(code, display, type, getCurrentGroup());
	}

	final protected ILeaf createLeafInternal(Code code, Display display, LeafType type, IGroup group) {
		if (display == null) {
			display = Display.getWithNewlines(code);
		}
		final ILeaf leaf = entityFactory.createLeaf(code, display, type, group, getHides());
		entityFactory.addLeaf(leaf);
		this.lastEntity = leaf;
		return leaf;
	}

	public boolean leafExist(Code code) {
		return getLeafs().containsKey(code);
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

	public final IGroup getOrCreateGroup(Code code, Display display, String namespace, GroupType type, IGroup parent) {
		final IGroup g = getOrCreateGroupInternal(code, display, namespace, type, parent);
		currentGroup = g;
		return g;
	}

	protected final IGroup getOrCreateGroupInternal(Code code, Display display, String namespace, GroupType type,
			IGroup parent) {
		IGroup result = entityFactory.getGroups().get(code);
		if (result != null) {
			return result;
		}
		if (entityFactory.getLeafs().containsKey(code)) {
			result = entityFactory.muteToGroup(code, namespace, type, parent);
			result.setDisplay(display);
		} else {
			result = entityFactory.createGroup(code, display, namespace, type, parent, getHides());
		}
		entityFactory.addGroup(result);
		return result;
	}

	public final IGroup getCurrentGroup() {
		return currentGroup;
	}

	public final IGroup getGroup(Code code) {
		final IGroup p = entityFactory.getGroups().get(code);
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
		return entityFactory.getGroups().containsKey(code);
	}

	public final Collection<IGroup> getGroups(boolean withRootGroup) {
		if (withRootGroup == false) {
			return entityFactory.getGroups().values();
		}
		final Collection<IGroup> result = new ArrayList<IGroup>();
		result.add(getRootGroup());
		result.addAll(entityFactory.getGroups().values());
		return Collections.unmodifiableCollection(result);
	}

	public IGroup getRootGroup() {
		return entityFactory.getRootGroup();

	}

	final public Map<Code, ILeaf> getLeafs() {
		return entityFactory.getLeafs();
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

//	final public List<File> createPng2(File pngFile) throws IOException, InterruptedException {
//		final CucaDiagramPngMaker3 maker = new CucaDiagramPngMaker3(this);
//		return maker.createPng(pngFile);
//	}
//
//	final public void createPng2(OutputStream os) throws IOException {
//		final CucaDiagramPngMaker3 maker = new CucaDiagramPngMaker3(this);
//		maker.createPng(os);
//	}

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
		return result.toArray(new String[result.size()]);
	}

	private void createFilesXmi(OutputStream suggestedFile, FileFormat fileFormat) throws IOException {
		final CucaDiagramXmiMaker maker = new CucaDiagramXmiMaker(this, fileFormat);
		maker.createFiles(suggestedFile);
	}

	@Override
	final protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption,
			List<BufferedImage> flashcodes) throws IOException {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();

		if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT) {
			try {
				createFilesTxt(os, index, fileFormat);
			} catch (Throwable t) {
				t.printStackTrace(new PrintStream(os));
			}
			return new ImageDataSimple();
		}

		if (fileFormat.name().startsWith("XMI")) {
			createFilesXmi(os, fileFormat);
			return new ImageDataSimple();
		}

		if (getUmlDiagramType() == UmlDiagramType.COMPOSITE) {
			throw new UnsupportedOperationException();
		}

		final CucaDiagramFileMaker maker = OptionFlags.USE_HECTOR ? new CucaDiagramFileMakerHectorB2(this)
				: new CucaDiagramFileMakerSvek(this, flashcodes);
		final ImageData result = maker.createFile(os, getDotStrings(), fileFormatOption);

		if (result == null) {
			return new ImageDataSimple();
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
		return generalWarningOrError + "\n" + warningOrError;
	}

	private void createFilesTxt(OutputStream os, int index, FileFormat fileFormat) throws IOException {
		final CucaDiagramTxtMaker maker = new CucaDiagramTxtMaker(this, fileFormat);
		maker.createFiles(os, index);
	}

	public final Rankdir getRankdir() {
		return rankdir;
	}

	public final void setRankdir(Rankdir rankdir) {
		this.rankdir = rankdir;
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

	public final boolean showPortion(EntityPortion portion, ILeaf entity) {
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

	public void hideOrShow(ILeaf leaf, boolean show) {
		leaf.setRemoved(!show);
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

	@Override
	public int getNbImages() {
		return this.horizontalPages * this.verticalPages;
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
			if (link.getEntity1().getEntityType() != LeafType.NOTE
					&& link.getEntity2().getEntityType() != LeafType.NOTE) {
				return link;
			}
		}
		return null;
	}

	private ILeaf lastEntity = null;

	final public ILeaf getLastEntity() {
		// for (final Iterator<ILeaf> it = getLeafs().values().iterator(); it.hasNext();) {
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

}
