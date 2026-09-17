/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.klimt.drawing.svg;

import java.io.IOException;
import java.io.OutputStream;

import net.atmp.SvgOption;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.AbstractUGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;
import net.sourceforge.plantuml.klimt.shape.UComment;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.UImageTikz;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPixel;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.url.Url;

public class UGraphicSvg extends AbstractUGraphic<SvgGraphics> implements ClipContainer {

	private final boolean textAsPath;
	private final FileFormat fileFormat;
	private /* final */ SvgOption option;

	public double dpiFactor() {
		return 1;
	}

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicSvg(this);
	}

	// A copy takes its driver table from "other" (see AbstractUGraphic#copy) instead of building
	// one, so this constructor deliberately does not register anything.
	private UGraphicSvg(UGraphicSvg other) {
		super(other.getStringBounder());
		this.fileFormat = other.fileFormat;
		this.textAsPath = other.textAsPath;
		copy(other);
		this.option = other.option;
	}

	private UGraphicSvg(StringBounder stringBounder, boolean textAsPath, FileFormat fileFormat) {
		super(stringBounder);
		this.fileFormat = fileFormat;
		this.textAsPath = textAsPath;
		register();
	}

	public static UGraphicSvg build(SvgOption option, boolean textAsPath, long seed, StringBounder stringBounder,
			FileFormat fileFormat) {
		final UGraphicSvg result = new UGraphicSvg(stringBounder, textAsPath, fileFormat);
		result.copy(option.getBackcolor(), option.getColorMapper(), new SvgGraphics(seed, option));
		result.option = option;
		return result;
	}

	@Override
	protected boolean manageHiddenAutomatically() {
		return false;
	}

	@Override
	protected void beforeDraw() {
		getGraphicObject().setHidden(getParam().isHidden());
	}

	@Override
	protected void afterDraw() {
		getGraphicObject().setHidden(false);
	}

	private void register() {
		registerDriver(URectangle.class, new DriverRectangleSvg());

		if (TeaVM.isTeaVM())
			registerDriver(UText.class, new DriverTextSvg(getStringBounder()));
		else if (textAsPath)
			registerDriver(UText.class, new DriverTextAsPathSvg());
		else
			registerDriver(UText.class, new DriverTextSvg(getStringBounder()));

		registerDriver(ULine.class, new DriverLineSvg());
		registerDriver(UPixel.class, new DriverPixelSvg());
		registerDriver(UPolygon.class, new DriverPolygonSvg());
		registerDriver(UEllipse.class, new DriverEllipseSvg());
		if (!TeaVM.isTeaVM())
			registerDriver(UImage.class, new DriverImagePng());
		registerDriver(UImageSvg.class, new DriverImageSvgSvg());
		ignoreShape(UImageTikz.class);
		registerDriver(UPath.class, new DriverPathSvg());
		registerDriver(DotPath.class, new DriverDotPathSvg());
		if (TeaVM.isTeaVM())
			registerDriver(UCenteredCharacter.class, new DriverCenteredCharacterSvgDeterministic());
		else
			registerDriver(UCenteredCharacter.class, new DriverCenteredCharacterSvg(fileFormat));
	}

	public SvgGraphics getSvgGraphics() {
		return this.getGraphicObject();
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		if (metadata != null)
			getGraphicObject().addCommentMetadata(metadata);

		getGraphicObject().createXml(os);
	}

	@Override
	public void startGroup(UGroup group) {
		getGraphicObject().startGroup(group.asMap());
	}

	@Override
	public void closeGroup() {
		getGraphicObject().closeGroup();
	}

	@Override
	public void startUrl(Url url) {
		getGraphicObject().openLink(url.getUrl(), url.getTooltip(), option.getLinkTarget());
	}

	@Override
	public void closeUrl() {
		getGraphicObject().closeLink();
	}

	@Override
	protected void drawComment(UComment comment) {
		getGraphicObject().addComment(comment.getComment());
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		if (propertyName.equalsIgnoreCase("SVG"))
			return true;

		return super.matchesProperty(propertyName);
	}

	// @Override
	// public String startHiddenGroup() {
	// getGraphicObject().startHiddenGroup();
	// return null;
	// }
	//
	// @Override
	// public String closeHiddenGroup() {
	// getGraphicObject().closeHiddenGroup();
	// return null;
	// }

}
