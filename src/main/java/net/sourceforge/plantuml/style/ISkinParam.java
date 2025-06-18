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
package net.sourceforge.plantuml.style;

import java.util.Collection;

import net.sourceforge.plantuml.TikzFontDistortion;
import net.sourceforge.plantuml.dot.DotSplines;
import net.sourceforge.plantuml.klimt.Arrows;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.drawing.svg.LengthAdjust;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.ComponentStyle;
import net.sourceforge.plantuml.skin.CornerParam;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.skin.Padder;
import net.sourceforge.plantuml.skin.PaddingParam;
import net.sourceforge.plantuml.skin.SplitParam;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.text.Guillemet;

public interface ISkinParam extends ISkinSimple {
    // ::remove file when __HAXE__

	public static final int SWIMLANE_WIDTH_SAME = -1;

	public HColor getHyperlinkColor();

	public UStroke useUnderlineForHyperlink();

	public HColor getBackgroundColor();

	public HColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable);

	public Colors getColors(ColorParam param, Stereotype stereotype) throws NoSuchColorException;

	public HColor getFontHtmlColor(Stereotype stereotype, FontParam... param);

	public UStroke getThickness(LineParam param, Stereotype stereotype);

	public UFont getFont(Stereotype stereotype, boolean inPackageTitle, FontParam... fontParam);

	public HorizontalAlignment getHorizontalAlignment(AlignmentParam param, ArrowDirection arrowDirection,
			boolean isReverseDefine, HorizontalAlignment overrideDefault);

	public HorizontalAlignment getDefaultTextAlignment(HorizontalAlignment defaultValue);

	public HorizontalAlignment getStereotypeAlignment();

	public int getCircledCharacterRadius();

	public char getCircledCharacter(Stereotype stereotype);

	public int classAttributeIconSize();

	public DotSplines getDotSplines();

	public boolean shadowing(Stereotype stereotype);

	public boolean shadowingForNote(Stereotype stereotype);

	public PackageStyle packageStyle();

	public ComponentStyle componentStyle();

	public boolean stereotypePositionTop();

	public boolean useSwimlanes(UmlDiagramType type);

	public double getNodesep();

	public double getRanksep();

	public double getRoundCorner(CornerParam param, Stereotype stereotype);

	public double getDiagonalCorner(CornerParam param, Stereotype stereotype);

	public LineBreakStrategy maxMessageSize();

	public LineBreakStrategy swimlaneWrapTitleWidth();

	public boolean strictUmlStyle();

	public boolean forceSequenceParticipantUnderlined();

	public ConditionStyle getConditionStyle();

	public ConditionEndStyle getConditionEndStyle();

	public boolean sameClassWidth();

	public Rankdir getRankdir();

	public boolean useOctagonForActivity(Stereotype stereotype);

	public int groupInheritance();

	public Guillemet guillemet();

	public boolean handwritten();

	public String getSvgLinkTarget();

	public String getPreserveAspectRatio();

	public int getTabSize();

	public int maxAsciiMessageLength();

	public int colorArrowSeparationSpace();

	public SplitParam getSplitParam();

	public int swimlaneWidth();

	public UmlDiagramType getUmlDiagramType();

	public HColor hoverPathColor();

	public TikzFontDistortion getTikzFontDistortion();

	public double getPadding(PaddingParam param);

	public boolean useRankSame();

	public boolean displayGenericWithOldFashion();

	public boolean responseMessageBelowArrow();

	public boolean svgDimensionStyle();

	public boolean fixCircleLabelOverlapping();

	public void setUseVizJs(boolean useVizJs);

	public boolean isUseVizJs();

	public Padder sequenceDiagramPadder();

	public StyleBuilder getCurrentStyleBuilder();

	public void muteStyle(Collection<Style> modifiedStyles);

	public Collection<String> getAllSpriteNames();

	public String getDefaultSkin();

	public void setDefaultSkin(String newSkin);

	public ActorStyle actorStyle();

	public void setSvgSize(String origin, String sizeToUse);

	public LengthAdjust getlengthAdjust();

	public double getParamSameClassWidth();

	public Arrows arrows();
}
