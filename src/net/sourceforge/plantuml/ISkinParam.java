/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotSplines;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.SkinParameter;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;

public interface ISkinParam extends ISkinSimple {

	HtmlColor getHyperlinkColor();

	boolean useUnderlineForHyperlink();

	HtmlColor getBackgroundColor();

	HtmlColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable);

	Colors getColors(ColorParam param, Stereotype stereotype);

	HtmlColor getFontHtmlColor(Stereotype stereotype, FontParam... param);

	UStroke getThickness(LineParam param, Stereotype stereotype);

	UFont getFont(Stereotype stereotype, boolean inPackageTitle, FontParam... fontParam);

	HorizontalAlignment getHorizontalAlignment(AlignParam param, ArrowDirection arrowDirection);

	HorizontalAlignment getDefaultTextAlignment(HorizontalAlignment defaultValue);

	int getCircledCharacterRadius();

	int classAttributeIconSize();

	ColorMapper getColorMapper();

	DotSplines getDotSplines();

	String getDotExecutable();

	boolean shadowing();

	boolean shadowingForNote(Stereotype stereotype);

	boolean shadowing2(SkinParameter skinParameter);

	PackageStyle getPackageStyle();

	boolean useUml2ForComponent();

	boolean stereotypePositionTop();

	boolean useSwimlanes(UmlDiagramType type);

	double getNodesep();

	double getRanksep();

	double getRoundCorner(String param, Stereotype stereotype);

	LineBreakStrategy maxMessageSize();

	boolean strictUmlStyle();

	boolean forceSequenceParticipantUnderlined();

	ConditionStyle getConditionStyle();

	double minClassWidth();

	boolean sameClassWidth();

	Rankdir getRankdir();

	boolean useOctagonForActivity(Stereotype stereotype);

	int groupInheritance();

	boolean useGuillemet();

	boolean handwritten();

	String getSvgLinkTarget();

	int getTabSize();

	int maxAsciiMessageLength();

	int colorArrowSeparationSpace();

	SplitParam getSplitParam();

	int swimlaneWidth();

	UmlDiagramType getUmlDiagramType();

	HtmlColor getHoverPathColor();

	TikzFontDistortion getTikzFontDistortion();

	double getPadding(PaddingParam param);

	boolean useRankSame();

	boolean displayGenericWithOldFashion();

}
