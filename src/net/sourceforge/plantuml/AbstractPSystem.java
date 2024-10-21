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
package net.sourceforge.plantuml;

import static net.atmp.ImageBuilder.imageBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.abel.DisplayPositionned;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ProtectedCommand;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.stats.StatsUtilsIncrement;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.Version;

/**
 * An abstract class for all diagram classes.
 * 
 * <p>
 * Short for "{@link net.sourceforge.plantuml.plasma plasma} system", although
 * most newer diagram types do not use entities stored in a plasma.
 *
 * @see PSystemBuilder
 */
public abstract class AbstractPSystem implements Diagram {
	// ::remove file when __HAXE__

	private final UmlSource source;
	private Scale scale;
	private int splitPagesHorizontal = 1;
	private int splitPagesVertical = 1;

	private String namespaceSeparator = null;

	public void setNamespaceSeparator(String namespaceSeparator) {
		this.namespaceSeparator = namespaceSeparator;
	}

	final public String getNamespaceSeparator() {
		return namespaceSeparator;
	}

	public AbstractPSystem(UmlSource source) {
		this.source = Objects.requireNonNull(source);
	}

	private String getVersion() {
		final StringBuilder toAppend = new StringBuilder();
		toAppend.append("PlantUML version ");
		toAppend.append(Version.versionString());
		toAppend.append("(" + Version.compileTimeString() + ")\n");
		toAppend.append("(" + License.getCurrent() + " source distribution)\n");
		// ::comment when __CORE__
		for (String name : OptionPrint.interestingProperties()) {
			toAppend.append(name);
			toAppend.append(BackSlash.CHAR_NEWLINE);
		}
		// ::done
		return toAppend.toString();
	}

	final public String getMetadata() {
		if (source == null)
			return getVersion();

		final String rawString = source.getRawString("\n");
		final String plainString = source.getPlainString("\n");
		if (rawString != null && rawString.equals(plainString))
			return rawString + BackSlash.NEWLINE + getVersion();

		return rawString + BackSlash.NEWLINE + plainString + BackSlash.NEWLINE + getVersion();
	}

	final public UmlSource getSource() {
		return source;
	}

	final public long seed() {
		if (source == null)
			return 42;

		return getSource().seed();
	}

	public int getNbImages() {
		return 1;
	}

	@Override
	public int getSplitPagesHorizontal() {
		return splitPagesHorizontal;
	}

	public void setSplitPagesHorizontal(int splitPagesHorizontal) {
		this.splitPagesHorizontal = splitPagesHorizontal;
	}

	@Override
	public int getSplitPagesVertical() {
		return splitPagesVertical;
	}

	public void setSplitPagesVertical(int splitPagesVertical) {
		this.splitPagesVertical = splitPagesVertical;
	}

	public DisplayPositionned getTitle() {
		if (source == null)
			return DisplayPositioned.single(Display.empty(), HorizontalAlignment.CENTER, VerticalAlignment.TOP);

		return DisplayPositioned.single(source.getTitle(), HorizontalAlignment.CENTER, VerticalAlignment.TOP);
	}

	public String getWarningOrError() {
		return null;
	}

	public String checkFinalError() {
		return null;
	}

	public void makeDiagramReady() {
	}

	public boolean isOk() {
		return true;
	}

	public CommandExecutionResult executeCommand(Command cmd, BlocLines lines) {
		cmd = new ProtectedCommand(cmd);
		try {
			return cmd.execute(this, lines);
		} catch (NoSuchColorException e) {
			return CommandExecutionResult.badColor();
		}
	}

	public boolean hasUrl() {
		return false;
	}

	final public ImageData exportDiagram(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final long now = System.currentTimeMillis();
		try {
//			if (this instanceof TitledDiagram) {
//				final TitledDiagram titledDiagram = (TitledDiagram) this;
//				final StyleBuilder styleBuilder = titledDiagram.getCurrentStyleBuilder();
//				if (styleBuilder != null) {
//					styleBuilder.printMe();
//				}
//			}
			return exportDiagramNow(os, index, fileFormatOption);
		} finally {
			// ::comment when __CORE__
			if (OptionFlags.getInstance().isEnableStats())
				StatsUtilsIncrement.onceMoreGenerate(System.currentTimeMillis() - now, getClass(),
						fileFormatOption.getFileFormat());

			// ::done
		}
	}

	final public void setScale(Scale scale) {
		this.scale = scale;
	}

	final public Scale getScale() {
		return scale;
	}

	public ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException {
		final ColorMapper init = fileFormatOption.getColorMapper();
		final ColorMapper newColorMappter = muteColorMapper(init);
		return imageBuilder(fileFormatOption.withColorMapper(newColorMappter));
	}

	protected ColorMapper muteColorMapper(ColorMapper init) {
		return init;
	}

	// TODO "index" isnt really being used
	protected abstract ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException;

	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.same(0);
	}

	@Override
	public Display getTitleDisplay() {
		return Display.NULL;
	}

	@Override
	public void exportDiagramGraphic(UGraphic ug, FileFormatOption fileFormatOption) {
		final UFont font = UFont.monospaced(14);
		final FontConfiguration fc = FontConfiguration.blackBlueTrue(font);
		final UText text = UText.build("Not implemented yet for " + getClass().getName(), fc);
		ug.apply(new UTranslate(10, 10)).draw(text);
	}

	public int getRequiredPassCount() {
		return 1;
	}

}
