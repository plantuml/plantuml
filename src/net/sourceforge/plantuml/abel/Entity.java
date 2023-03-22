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
 * Contribution:  Hisashi Miyashita
 * Contribution:  Miguel Esteves
 *
 */
package net.sourceforge.plantuml.abel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.dot.Neighborhood;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockEmpty;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.stereo.Stereostyles;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.Kal;
import net.sourceforge.plantuml.svek.Margins;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.svek.SingleStrategy;
import net.sourceforge.plantuml.svek.image.EntityImageStateCommon;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.utils.Position;

final public class Entity implements SpecificBackcolorable, Hideable, Removeable, LineConfigurable, Bag {

	private final EntityFactory entityFactory;

	private final Quark<Entity> quark;

	private Url url;

	private final Bodier bodier;
	private final String uid;
	private Display display = Display.empty();
	private DisplayPositioned legend = null;

	private LeafType leafType;
	private Stereotype stereotype;
	private Stereostyles stereostyles = Stereostyles.NONE;
	private String generic;

	private GroupType groupType;

	// Other
	private Margins margins = Margins.NONE;
	private final Collection<String> portShortNames = new HashSet<>();
	private int xposition;
	private IEntityImage svekImage;

	private USymbol symbol;
	private final int rawLayout;
	private char concurrentSeparator;
	private LineLocation codeLine;

	private Set<Stereotag> tags = new LinkedHashSet<>();
	private final List<CucaNote> notesTop = new ArrayList<>();
	private final List<CucaNote> notesBottom = new ArrayList<>();

	private Together together;

	//
	public void addNote(Display note, Position position, Colors colors) {
		if (position == Position.TOP)
			notesTop.add(CucaNote.build(note, position, colors));
		else if (position == Position.BOTTOM)
			notesBottom.add(CucaNote.build(note, position, colors));
	}

	//
	public List<CucaNote> getNotes(Position position) {
		if (position == Position.TOP)
			return Collections.unmodifiableList(notesTop);
		if (position == Position.BOTTOM)
			return Collections.unmodifiableList(notesBottom);
		throw new IllegalArgumentException();
	}

	public void addStereotag(Stereotag tag) {
		this.tags.add(tag);
	}

	public Set<Stereotag> stereotags() {
		return Collections.unmodifiableSet(tags);
	}

	// Back to Entity
	private Entity(Quark<Entity> quark, EntityFactory entityFactory, Bodier bodier, int rawLayout) {
		this.quark = Objects.requireNonNull(quark);
		if (quark.isRoot())
			this.uid = "clroot";
		else
			this.uid = StringUtils.getUid("cl", entityFactory.getDiagram().getUniqueSequence());
		this.entityFactory = entityFactory;
		this.bodier = bodier;
		this.rawLayout = rawLayout;
		this.quark.setData(this);
	}

	Entity(Quark<Entity> quark, EntityFactory entityFactory, Bodier bodier, LeafType leafType, int rawLayout) {
		this(Objects.requireNonNull(quark), entityFactory, bodier, rawLayout);
		this.leafType = leafType;
	}

	Entity(Quark<Entity> quark, EntityFactory entityFactory, Bodier bodier, GroupType groupType, int rawLayout) {
		this(Objects.requireNonNull(quark), entityFactory, bodier, rawLayout);
		this.groupType = groupType;
	}

	public LeafType getLeafType() {
		return leafType;
	}

	public void muteToType(LeafType newType) {
		if (leafType == LeafType.CLASS && newType == LeafType.OBJECT)
			bodier.muteClassToObject();
		this.groupType = null;
		this.leafType = newType;
	}

	public void muteToGroupType(GroupType newType) {
		this.groupType = newType;
		this.leafType = null;
	}

	public boolean muteToType(LeafType newType, USymbol newSymbol) {
		// checkNotGroup();
		Objects.requireNonNull(newType);
		if (leafType != LeafType.STILL_UNKNOWN) {
			if (newType == this.leafType)
				return true;

			if (leafType != LeafType.ANNOTATION && leafType != LeafType.ABSTRACT_CLASS && leafType != LeafType.CLASS
					&& leafType != LeafType.ENUM && leafType != LeafType.INTERFACE) {
				return false;
				// throw new IllegalArgumentException("type=" + leafType);
			}
			if (newType != LeafType.ANNOTATION && newType != LeafType.ABSTRACT_CLASS && newType != LeafType.CLASS
					&& newType != LeafType.ENUM && newType != LeafType.INTERFACE && newType != LeafType.OBJECT) {
				return false;
				// throw new IllegalArgumentException("newtype=" + newType);
			}
		}
		if (leafType == LeafType.CLASS && newType == LeafType.OBJECT)
			bodier.muteClassToObject();

		this.leafType = newType;
		this.symbol = newSymbol;
		return true;
	}

	public Quark<Entity> getQuark() {
		return quark;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public String getUid() {
		return uid;
	}

	public Stereotype getStereotype() {
		return stereotype;
	}

	public final void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}

	public String toString() {
		return quark.toString() + " " + display + "(" + leafType + ")[" + groupType + "] " + getUid();
	}

	public final Url getUrl99() {
		return url;
	}

	public boolean hasUrl() {
		if (Display.isNull(display) == false && display.hasUrl())
			return true;

		if (bodier == null)
			return false;

		if (bodier.hasUrl())
			return true;

		return url != null;
	}

	public final void addUrl(Url url) {
		this.url = url;
	}

	public final Margins getMargins() {
		checkNotGroup();
		return margins;
	}

	public final void ensureMargins(Margins newMargins) {
		// checkNotGroup();
		this.margins = this.margins.merge(newMargins);
	}

	public int getXposition() {
		checkNotGroup();
		return xposition;
	}

	public void setXposition(int pos) {
		checkNotGroup();
		xposition = pos;
	}

	public final IEntityImage getSvekImage() {
		checkNotGroup();
		return svekImage;
	}

	public final void setSvekImage(IEntityImage svekImage) {
		checkNotGroup();
		this.svekImage = svekImage;
	}

	public final void setGeneric(String generic) {
		checkNotGroup();
		this.generic = generic;
	}

	public final String getGeneric() {
		checkNotGroup();
		return generic;
	}

	public Bodier getBodier() {
		return bodier;
	}

	public EntityPosition getEntityPosition() {
//		if (leafType == LeafType.PORT)
//			return EntityPosition.PORT;

		if (leafType == LeafType.PORTIN)
			return EntityPosition.PORTIN;

		if (leafType == LeafType.PORTOUT)
			return EntityPosition.PORTOUT;

		if (leafType != LeafType.STATE)
			return EntityPosition.NORMAL;

		if (quark.isRoot())
			return EntityPosition.NORMAL;

		final Stereotype stereotype = getStereotype();
		if (stereotype == null)
			return EntityPosition.NORMAL;

		return EntityPosition.fromStereotype(stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));

	}

	// ----------

	private void checkGroup() {
		if (isGroup() == false)
			throw new UnsupportedOperationException();

	}

	private void checkNotGroup() {
		if (isGroup())
			throw new UnsupportedOperationException();

	}

	public GroupType getGroupType() {
		checkGroup();
		return groupType;
	}

	public PackageStyle getPackageStyle() {
		checkGroup();
		if (stereotype == null)
			return null;

		return stereotype.getPackageStyle();
	}

	public boolean isGroup() {
		if (groupType != null && leafType != null)
			throw new IllegalStateException();

		assert groupType == null || leafType == null;
		if (groupType != null)
			return true;

		if (leafType != null)
			return false;

		throw new IllegalStateException();
	}

	// ---- other

	public void overrideImage(IEntityImage img, LeafType leafType) {
		checkGroup();
		this.svekImage = img;
		this.url = null;

		for (final Link link : new ArrayList<>(entityFactory.getLinks()))
			if (EntityUtils.isPureInnerLink12(this, link))
				entityFactory.removeLink(link);

		this.groupType = null;
		this.leafType = leafType;
	}

	public USymbol getUSymbol() {
		if (getLeafType() == LeafType.CIRCLE)
			return USymbols.INTERFACE;

		return symbol;
	}

	public void setUSymbol(USymbol symbol) {
		this.symbol = symbol;
	}

	public SingleStrategy getSingleStrategy() {
		return SingleStrategy.SQUARE;
	}

	public boolean isHidden() {
		if (getParentContainer() != null && getParentContainer().isHidden())
			return true;

		return isHiddenInternal();
	}

	private boolean isHiddenInternal() {
		if (quark.isRoot())
			return false;
		if (isGroup()) {
			if (entityFactory.isHidden(this))
				return true;

			if (leafs().size() == 0)
				return false;

			for (Entity leaf : leafs())
				if (leaf.isHiddenInternal() == false)
					return false;

			for (Entity g : groups())
				if (g.isHiddenInternal() == false)
					return false;

			return true;
		}
		return entityFactory.isHidden(this);
	}

	public boolean isRemoved() {
		if (getParentContainer() != null && getParentContainer().isRemoved())
			return true;

		return isRemovedInternal();
	}

	private boolean isRemovedInternal() {
		if (isGroup()) {
			if (entityFactory.isRemoved(this))
				return true;

			if (leafs().size() == 0 && groups().size() == 0)
				return false;

			for (Entity leaf : leafs())
				if (((Entity) leaf).isRemovedInternal() == false)
					return false;

			for (Entity g : groups())
				if (((Entity) g).isRemovedInternal() == false)
					return false;

			return true;
		}
		return entityFactory.isRemoved(this);
	}

	public boolean isAloneAndUnlinked() {
		if (isGroup())
			return false;

		for (Link link : entityFactory.getLinks())
			if (link.contains(this)) {
				final Entity other = (Entity) link.getOther(this);
				final boolean removed = entityFactory.isRemovedIgnoreUnlinked(other);
				if (removed == false && link.getType().isInvisible() == false)
					return false;
			}

		return true;
	}

	private FontParam getTitleFontParam() {
		return getGroupType() == GroupType.STATE ? FontParam.STATE : FontParam.PACKAGE;
	}

	public FontConfiguration getFontConfigurationForTitle(final ISkinParam skinParam) {
		final FontParam fontParam = getTitleFontParam();
		final HColor fontHtmlColor = skinParam.getFontHtmlColor(getStereotype(), fontParam, FontParam.PACKAGE);
		final UFont font = skinParam.getFont(getStereotype(), true, fontParam, FontParam.PACKAGE);
		final FontConfiguration fontConfiguration = FontConfiguration.create(font, fontHtmlColor,
				skinParam.getHyperlinkColor(), skinParam.useUnderlineForHyperlink(), skinParam.getTabSize());
		return fontConfiguration;
	}

	public final int getRawLayout() {
		return rawLayout;
	}

	public char getConcurrentSeparator() {
		return concurrentSeparator;
	}

	public void setConcurrentSeparator(char separator) {
		this.concurrentSeparator = separator;
	}

	private Neighborhood neighborhood;

	public void setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	private final Map<String, Display> tips = new LinkedHashMap<String, Display>();

	public void putTip(String member, Display display) {
		tips.put(member, display);
	}

	public Map<String, Display> getTips() {
		return Collections.unmodifiableMap(tips);
	}

	private Colors colors = Colors.empty();

	public Colors getColors() {
		return colors;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	public void setSpecificColorTOBEREMOVED(ColorType type, HColor color) {
		if (color != null)
			this.colors = colors.add(type, color);

	}

	public Collection<String> getPortShortNames() {
		checkNotGroup();
		return Collections.unmodifiableCollection(portShortNames);
	}

	public void addPortShortName(String portShortName) {
		portShortNames.add(portShortName);
	}

	private VisibilityModifier visibility;

	public void setVisibilityModifier(VisibilityModifier visibility) {
		this.visibility = visibility;

	}

	public VisibilityModifier getVisibilityModifier() {
		return visibility;
	}

	public void setLegend(DisplayPositioned legend) {
		checkGroup();
		this.legend = legend;
	}

	public DisplayPositioned getLegend() {
		return legend;
	}

	public String getCodeLine() {
		if (this.codeLine == null)
			return null;

		return "" + this.codeLine.getPosition();
	}

	public void setCodeLine(LineLocation codeLine) {
		this.codeLine = codeLine;
	}

	//
	public void setStereostyle(String stereo) {
		this.stereostyles = Stereostyles.build(stereo);
	}

	//
	public Stereostyles getStereostyles() {
		return stereostyles;
	}

	private final Map<Direction, List<Kal>> kals = new EnumMap<>(Direction.class);

	public void addKal(Kal kal) {
		final Direction position = kal.getPosition();
		List<Kal> list = kals.get(position);
		if (list == null) {
			list = new ArrayList<>();
			kals.put(position, list);
		}
		list.add(kal);
	}

	public List<Kal> getKals(Direction position) {
		final List<Kal> result = kals.get(position);
		if (result == null)
			return Collections.emptyList();
		return Collections.unmodifiableList(result);
	}

	public ICucaDiagram getDiagram() {
		return entityFactory.getDiagram();
	}

	private boolean isStatic;

	//
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	//
	public boolean isStatic() {
		return isStatic;
	}

	// For group

	public TextBlock getStateHeader(ISkinParam skinParam) {
		checkGroup();
		final Style style = EntityImageStateCommon.getStyleStateHeader(this, skinParam);
		final List<CharSequence> details = getBodier().getRawBody();

		if (details.size() == 0)
			return new TextBlockEmpty();

		if (style == null)
			throw new IllegalArgumentException();
		final FontConfiguration fontConfiguration = FontConfiguration.create(skinParam, style);

		Display display = null;
		for (CharSequence s : details)
			if (display == null)
				display = Display.getWithNewlines(s.toString());
			else
				display = display.addAll(Display.getWithNewlines(s.toString()));

		return display.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam);

	}

	public void setTogether(Together together) {
		this.together = together;
	}

	public Together getTogether() {
		return this.together;
	}

	public Entity getParentContainer() {
		if (quark.isRoot())
			return null;
		return quark.getParent().getData();
	}

	public Collection<Entity> leafs() {
		final List<Entity> result = new ArrayList<>();
		for (Quark<Entity> child : quark.getChildren()) {
			final Entity data = child.getData();
			if (data != null && data.isGroup() == false)
				result.add(data);
		}
		return Collections.unmodifiableCollection(result);
	}

	public Collection<Entity> groups() {
		final List<Entity> result = new ArrayList<>();
		for (Quark<Entity> child : quark.getChildren()) {
			final Entity data = child.getData();
			if (data != null && data.isGroup())
				result.add(data);
		}
		return Collections.unmodifiableCollection(result);
	}

	public int countChildren() {
		return getQuark().countChildren();
	}

	public boolean isRoot() {
		return getQuark().isRoot();
	}

	final public boolean isEmpty() {
		return countChildren() == 0;
	}

	public String getName() {
		return getQuark().getName();
	}

	public boolean isAutarkic() {
		if (getGroupType() == GroupType.PACKAGE)
			return false;

		if (getGroupType() == GroupType.INNER_ACTIVITY)
			return true;

		if (getGroupType() == GroupType.CONCURRENT_ACTIVITY)
			return true;

		if (getGroupType() == GroupType.CONCURRENT_STATE)
			return true;

//		if (diag.getChildrenGroups(this).size() > 0)
//			return false;

		for (Link link : entityFactory.getLinks())
			if (EntityUtils.isPureInnerLink3(this, link) == false)
				return false;

		for (Entity leaf : leafs())
			if (leaf.getEntityPosition() != EntityPosition.NORMAL)
				return false;

		return true;
	}

	public boolean canBePacked() {
		if (isPacked())
			return false;
		if (countChildren() != 1)
			return false;
		if (leafs().size() != 0)
			return false;
		for (Link link : entityFactory.getLinks())
			if (link.contains(this))
				return false;

		final Entity child = groups().iterator().next();
		if (child.countChildren() == 0)
			return false;

		return true;
	}

	private boolean packed;

	public final void setPacked(boolean packed) {
		this.packed = true;
	}

	public final boolean isPacked() {
		return packed;
	}

}
