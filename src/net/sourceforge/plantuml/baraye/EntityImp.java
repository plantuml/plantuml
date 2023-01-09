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
 * Original Author:  Arnaud Roques
 * Contribution:  Hisashi Miyashita
 * Contribution:  Miguel Esteves
 *
 */
package net.sourceforge.plantuml.baraye;

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

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.CucaNote;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositioned;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereostyles;
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.Neighborhood;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.USymbols;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.Kal;
import net.sourceforge.plantuml.svek.Margins;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.svek.SingleStrategy;
import net.sourceforge.plantuml.svek.image.EntityImageStateCommon;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

final public class EntityImp implements ILeaf, IGroup {

	private final EntityFactory entityFactory;

	// Entity
	private/* final */Code code;
	private/* final */Ident ident;

	private Url url;

	private final Bodier bodier;
	private final String uid;
	private Display display = Display.empty();
	private DisplayPositioned legend = null;

	private LeafType leafType;
	private Stereotype stereotype;
	private Stereostyles stereostyles = Stereostyles.NONE;
	private String generic;
	private IGroup parentContainer;

	// Group
	private Code namespace;

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

	@Override
	public void addNote(Display note, Position position, Colors colors) {
		if (position == Position.TOP)
			notesTop.add(CucaNote.build(note, position, colors));
		else if (position == Position.BOTTOM)
			notesBottom.add(CucaNote.build(note, position, colors));
	}

	@Override
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
	private EntityImp(Ident ident, EntityFactory entityFactory, Code code, Bodier bodier, IGroup parentContainer,
			String namespaceSeparator, int rawLayout) {
		this.ident = Objects.requireNonNull(ident);
		this.uid = StringUtils.getUid("cl", entityFactory.getDiagram().getUniqueSequence());
		if (entityFactory.namespaceSeparator.V1972())
			code = ident;

		this.code = Objects.requireNonNull(code);
		this.entityFactory = entityFactory;
		this.bodier = bodier;
		this.parentContainer = parentContainer;
		this.rawLayout = rawLayout;
	}

	public EntityImp(Ident ident, Code code, EntityFactory entityFactory, Bodier bodier, IGroup parentContainer,
			LeafType leafType, String namespaceSeparator, int rawLayout) {
		this(Objects.requireNonNull(ident), entityFactory, code, bodier, parentContainer, namespaceSeparator,
				rawLayout);
		// System.err.println("ID for leaf=" + code + " " + ident);
		// ident.checkSameAs(code, namespaceSeparator);
		this.leafType = leafType;
	}

	public EntityImp(Ident ident, Code code, EntityFactory entityFactory, Bodier bodier, IGroup parentContainer,
			GroupType groupType, Code namespace, String namespaceSeparator, int rawLayout) {
		this(Objects.requireNonNull(ident), entityFactory, code, bodier, parentContainer, namespaceSeparator,
				rawLayout);
		// System.err.println("ID for group=" + code + " " + ident);
		ident.checkSameAs(code, namespaceSeparator, entityFactory.namespaceSeparator);
		this.groupType = groupType;
		this.namespace = namespace;
	}

	public void setContainer(IGroup container) {
		checkNotGroup();
		this.parentContainer = Objects.requireNonNull(container);
	}

	public LeafType getLeafType() {
		return leafType;
	}

	public boolean muteToType(LeafType newType, USymbol newSymbol) {
		checkNotGroup();
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

	public Code getCode() {
		return code;
	}

	public String getCodeGetName() {
		return getCode().getName();
	}

	public Ident getIdent() {
		return ident;
	}

	public Display getDisplay() {
		if (intricated)
			return entityFactory.getIntricatedDisplay(ident);

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

	public final IGroup getParentContainer() {
		return entityFactory.getParentContainer(ident, parentContainer);
		// Objects.requireNonNull(parentContainer);
		// return parentContainer;
	}

	@Override
	public String toString() {
		// return super.toString() + code + " " + display + "(" + leafType + ")[" +
		// groupType + "] " + xposition + " "
		// + getUid();
		if (entityFactory.namespaceSeparator.V1972())
			return getUid() + " " + ident + " " + display + "(" + leafType + ")[" + groupType + "]";
		return "EntityImpl " + code + ident + " " + display + "(" + leafType + ")[" + groupType + "] " + getUid();
	}

	public final Url getUrl99() {
		return url;
	}

	public boolean hasUrl() {
		if (Display.isNull(display) == false && display.hasUrl())
			return true;

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

		if (getParentContainer() instanceof GroupRoot)
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

	public boolean containsLeafRecurse(ILeaf leaf) {
		if (Objects.requireNonNull(leaf).isGroup())
			throw new IllegalArgumentException();

		checkGroup();
		if (leaf.getParentContainer() == this)
			return true;

		for (IGroup child : getChildren())
			if (child.containsLeafRecurse(leaf))
				return true;

		return false;
	}

	public Collection<ILeaf> getLeafsDirect() {
		checkGroup();
		final List<ILeaf> result = new ArrayList<>();
		for (ILeaf ent : entityFactory.leafs()) {
			if (ent.isGroup())
				throw new IllegalStateException();

			if (ent.getParentContainer() == this)
				result.add(ent);

		}
		return Collections.unmodifiableCollection(result);
	}

	public Collection<IGroup> getChildren() {
		checkGroup();
		final Collection<IGroup> result = new ArrayList<>();
		for (IGroup g : entityFactory.groups())
			if (g != this && g.getParentContainer() == this)
				result.add(g);

		return Collections.unmodifiableCollection(result);
	}

	public void moveEntitiesTo(IGroup dest) {
		if (entityFactory.namespaceSeparator.V1972()) {
			moveEntitiesTo1972(dest);
			return;
		}
		checkGroup();
		if (dest.isGroup() == false)
			throw new UnsupportedOperationException();

		for (ILeaf ent : getLeafsDirect())
			((EntityImp) ent).parentContainer = dest;

		for (IGroup g : dest.getChildren())
			// ((EntityImpl) g).parentContainer = dest;
			throw new IllegalStateException();

		for (IGroup g : getChildren()) {
			if (g == dest)
				continue;

			((EntityImp) g).parentContainer = dest;
		}

	}

	private void moveEntitiesTo1972(IGroup dest) {
		checkGroup();
		if (dest.isGroup() == false)
			throw new UnsupportedOperationException();

		// System.err.println("moveEntitiesTo1972::before1::groups2=" +
		// entityFactory.groups2());
		final Ident firstIdent = getIdent();
		final Ident destIdent = dest.getIdent();
		// System.err.println("moveEntitiesTo1972::this=" + firstIdent);
		// System.err.println("moveEntitiesTo1972::dest=" + destIdent);
		if (destIdent.startsWith(firstIdent) == false)
			throw new UnsupportedOperationException();

		// System.err.println("moveEntitiesTo1972::before2::groups2=" +
		// entityFactory.groups2());
		for (ILeaf ent : new ArrayList<>(entityFactory.leafs2())) {
			Ident ident = ent.getIdent();
			if (ident.equals(firstIdent) == false && ident.startsWith(firstIdent)
					&& ident.startsWith(destIdent) == false) {
				// System.err.print("moving leaf ident1=" + ident);
				entityFactory.leafs2.remove(ident);
				ident = ident.move(firstIdent, destIdent);
				// System.err.println(" to ident2=" + ident);
				((EntityImp) ent).ident = ident;
				((EntityImp) ent).code = ident;
				entityFactory.leafs2.put(ident, ent);
			}
		}
		// System.err.println("moveEntitiesTo1972::before3::groups2=" +
		// entityFactory.groups2());
		for (IGroup ent : new ArrayList<>(entityFactory.groups2())) {
			Ident ident = ent.getIdent();
			// System.err.println("found=" + ident + " " + ident.startsWith(firstIdent) + "
			// "
			// + ident.startsWith(destIdent));
			if (ident.equals(firstIdent) == false && ident.startsWith(firstIdent)
					&& ident.startsWith(destIdent) == false) {
				// System.err.print("moving gr ident1=" + ident);
				entityFactory.groups2.remove(ident);
				ident = ident.move(firstIdent, destIdent);
				// System.err.println(" to ident2=" + ident);
				((EntityImp) ent).ident = ident;
				((EntityImp) ent).code = ident;
				entityFactory.groups2.put(ident, ent);
				// System.err.println("-->groups2=" + entityFactory.groups2());
			}
		}
		// System.err.println("moveEntitiesTo1972::after::groups2=" +
		// entityFactory.groups2());
		// for (IGroup g : dest.getChildren()) {
		// // ((EntityImpl) g).parentContainer = dest;
		// throw new IllegalStateException();
		// }
		//
		// for (IGroup g : getChildren()) {
		// if (g == dest) {
		// continue;
		// }
		// ((EntityImpl) g).parentContainer = dest;
		// }

	}

	public int size() {
		checkGroup();
		return getLeafsDirect().size();
	}

	public GroupType getGroupType() {
		checkGroup();
		return groupType;
	}

	public Code getNamespace() {
		checkGroup();
		return namespace;
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

		if (entityFactory.namespaceSeparator.V1972()) {
			entityFactory.removeGroup(getIdent());
			for (ILeaf ent : new ArrayList<>(entityFactory.leafs()))
				if (this != ent && getIdent().equals(ent.getIdent().parent()))
					entityFactory.removeLeaf(ent.getIdent());

		} else {
			entityFactory.removeGroup(getCodeGetName());
			for (ILeaf ent : new ArrayList<>(entityFactory.leafs()))
				if (this != ent && this == ent.getParentContainer())
					entityFactory.removeLeaf(ent.getCodeGetName());
		}

		entityFactory.addLeaf(this);
		this.groupType = null;
		this.leafType = leafType;
	}

	public /* private */ void muteToGroup(Code namespaceNew, GroupType groupType, IGroup parentContainer) {
		checkNotGroup();
		if (parentContainer.isGroup() == false)
			throw new IllegalArgumentException();

		this.namespace = namespaceNew;
		this.groupType = groupType;
		this.leafType = null;
		this.parentContainer = parentContainer;
	}

	public USymbol getUSymbol() {
		if (getLeafType() == LeafType.CIRCLE)
			return USymbols.INTERFACE;

		// if (symbol != null && stereotype != null && stereotype.getSprite() != null) {
		// return symbol.withStereoAlignment(HorizontalAlignment.RIGHT);
		// }
		return symbol;
	}

	public void setUSymbol(USymbol symbol) {
		this.symbol = symbol;
	}

	public SingleStrategy getSingleStrategy() {
		return SingleStrategy.SQUARE;
	}

	public boolean isHidden() {
		if (parentContainer != null && parentContainer.isHidden())
			return true;

		return isHiddenInternal();
	}

	private boolean isHiddenInternal() {
		if (isGroup()) {
			if (entityFactory.isHidden(this))
				return true;

			if (getLeafsDirect().size() == 0)
				return false;

			for (ILeaf leaf : getLeafsDirect())
				if (((EntityImp) leaf).isHiddenInternal() == false)
					return false;

			for (IGroup g : getChildren())
				if (((EntityImp) g).isHiddenInternal() == false)
					return false;

			return true;
		}
		return entityFactory.isHidden(this);
	}

	public boolean isRemoved() {
		if (parentContainer != null && parentContainer.isRemoved())
			return true;

		return isRemovedInternal();
	}

	private boolean isRemovedInternal() {
		if (isGroup()) {
			if (entityFactory.isRemoved(this))
				return true;

			if (getLeafsDirect().size() == 0 && getChildren().size() == 0)
				return false;

			for (ILeaf leaf : getLeafsDirect())
				if (((EntityImp) leaf).isRemovedInternal() == false)
					return false;

			for (IGroup g : getChildren())
				if (((EntityImp) g).isRemovedInternal() == false)
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
				final ILeaf other = (ILeaf) link.getOther(this);
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

	private boolean intricated;

	public void setIntricated(boolean intricated) {
		this.intricated = intricated;

	}

	private IGroup originalGroup;

	public void setOriginalGroup(IGroup originalGroup) {
		this.originalGroup = originalGroup;
		this.legend = originalGroup.getLegend();
	}

	public IGroup getOriginalGroup() {
		return originalGroup;
	}

	private boolean together;

	public void setThisIsTogether() {
		this.together = true;
		setUSymbol(USymbols.TOGETHER);
	}

	public String getCodeLine() {
		if (this.codeLine == null)
			return null;

		return "" + this.codeLine.getPosition();
	}

	public void setCodeLine(LineLocation codeLine) {
		this.codeLine = codeLine;
	}

	@Override
	public void setStereostyle(String stereo) {
		this.stereostyles = Stereostyles.build(stereo);
	}

	@Override
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

	public CucaDiagram getDiagram() {
		return entityFactory.getDiagram();
	}

	private boolean isStatic;

	@Override
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	@Override
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

}
