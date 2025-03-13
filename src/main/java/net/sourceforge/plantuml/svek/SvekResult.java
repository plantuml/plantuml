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
package net.sourceforge.plantuml.svek;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UHidden;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public final class SvekResult extends AbstractTextBlock implements IEntityImage {
	// ::remove file when __CORE__

	private final DotData dotData;
	private final DotStringFactory clusterManager;

	public SvekResult(DotData dotData, DotStringFactory clusterManager) {
		this.dotData = dotData;
		this.clusterManager = clusterManager;
	}

	public void drawU(UGraphic ug) {

		for (Cluster cluster : clusterManager.getBibliotekon().allCluster())
			if (cluster.getGroup().isPacked() == false)
				cluster.drawU(ug);

		final Style style2 = getDefaultStyleDefinition(null)
				.getMergedStyle(dotData.getSkinParam().getCurrentStyleBuilder());

		final HColor borderColor = HColors
				.noGradient(style2.value(PName.LineColor).asColor(dotData.getSkinParam().getIHtmlColorSet()));

		for (SvekNode node : clusterManager.getBibliotekon().allNodes()) {
			final double minX = node.getMinX();
			final double minY = node.getMinY();
			final UGraphic ug2 = node.isHidden() ? ug.apply(UHidden.HIDDEN) : ug;
			final IEntityImage image = node.getImage();
			image.drawU(ug2.apply(new UTranslate(minX, minY)));
			if (image instanceof Untranslated)
				((Untranslated) image).drawUntranslated(ug.apply(borderColor), minX, minY);

		}

		final Set<String> ids = new HashSet<>();

		computeKal();

		for (SvekEdge svekEdge : clusterManager.getBibliotekon().allLines()) {
			final UGraphic ug2 = svekEdge.isHidden() ? ug.apply(UHidden.HIDDEN) : ug;
			svekEdge.setSharedIds(ids);
			svekEdge.drawU(ug2);
		}
	}

	private void computeKal() {
		for (SvekEdge line : clusterManager.getBibliotekon().allLines())
			line.computeKal();
		for (SvekNode node : clusterManager.getBibliotekon().allNodes())
			node.fixOverlap();
	}

	private StyleSignature getDefaultStyleDefinition(Stereotype stereotype) {
		StyleSignature result = StyleSignatureBasic.of(SName.root, SName.element,
				dotData.getUmlDiagramType().getStyleName(), SName.arrow);

		return result.withTOBECHANGED(stereotype);
	}

	// Duplicate SvekResult / GeneralImageBuilder
	public HColor getBackcolor() {
		final Style style = StyleSignatureBasic.of(SName.root, SName.document)
				.getMergedStyle(dotData.getSkinParam().getCurrentStyleBuilder());
		return style.value(PName.BackGroundColor).asColor(dotData.getSkinParam().getIHtmlColorSet());
	}

	private MinMax minMax;

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		if (minMax == null) {
			minMax = TextBlockUtils.getMinMax(this, stringBounder, false);
			clusterManager.moveDelta(6 - minMax.getMinX(), 6 - minMax.getMinY());
		}
		return minMax.getDimension().delta(0, 12);
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public boolean isHidden() {
		return false;
	}

	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

}
