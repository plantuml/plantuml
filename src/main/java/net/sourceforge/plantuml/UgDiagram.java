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
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.core.AbstractDiagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.TextBlockExporter12026;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.crash.CrashReportHandler;
import net.sourceforge.plantuml.crash.GraphvizCrash;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.OptionKey;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.stats.StatsUtilsIncrement;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.teavm.TeaVM;

public abstract class UgDiagram extends AbstractDiagram {

	public UgDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
	}

	public abstract TextBlock getTextBlock12026(int num, FileFormatOption fileFormatOption) throws Exception;

	public TextBlock addChrome(TextBlock result, FileFormatOption fileFormatOption) {
		return result;
	}

	@Override
	final public ImageData exportDiagram(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		ImageData specialResult = null;
		if (fileFormat.isXmi())
			specialResult = exportXmi(os, fileFormat);
		else if (fileFormat == FileFormat.SCXML)
			specialResult = exportScxml(os);
		else if (fileFormat == FileFormat.GRAPHML)
			specialResult = exportGraphml(os);
		else if (fileFormat == FileFormat.ATXT || fileFormat == FileFormat.UTXT)
			specialResult = exportTxt(os, index, fileFormat);
		if (specialResult != null)
			return specialResult;

		final long now = System.currentTimeMillis();
		try {
			return getExporter(index, fileFormatOption).exportTo(os);
		} catch (Throwable e) {
			Logme.error(e);
			final CrashReportHandler report = new CrashReportHandler(e, getMetadata(), getFlashData());
			report.anErrorHasOccured(e, getFlashData());
			report.addProperties();
			report.addEmptyLine();
			report.youShouldSendThisDiagram();
			report.addEmptyLine();
			report.exportDiagramError(fileFormatOption, seed(), os);
			return ImageDataSimple.error(e);
		} finally {
			if (!TeaVM.isTeaVM()) {
				if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.ENABLE_STATS))
					StatsUtilsIncrement.onceMoreGenerate(System.currentTimeMillis() - now, getClass(),
							fileFormatOption.getFileFormat());

			}
		}
	}

	public boolean isHandwritten() {
		if (getPreprocessingArtifact().getOption().isTrue(OptionKey.HANDWRITTEN))
			return true;
		return false;
	}

	protected TextBlockExporter12026 getExporter(int index, FileFormatOption fileFormatOption) throws Exception {
		TextBlock result = getTextBlock12026(index, fileFormatOption);

		final int status = computeStatus(result);

		result = addChrome(result, fileFormatOption);

		final HColor backColor = calculateBackColor();
		if (backColor != null)
			result = TextBlockUtils.addBackcolor(result, backColor);

		final ColorMapper mutedMapper = muteColorMapper(fileFormatOption.getColorMapper());
		final FileFormatOption effectiveFormat = fileFormatOption.withColorMapper(mutedMapper);

		final TextBlockExporter12026.Builder builder = TextBlockExporter12026.builder(result, effectiveFormat,
				isHandwritten());
		if (this instanceof TitledDiagram)
			builder.styled((TitledDiagram) this);
		else
			builder.scale(getScale()) //
					.metadata(fileFormatOption.isWithMetadata() ? getMetadata() : null) //
					.seed(seed()) //
					.warningOrError(getWarningOrError()) //
					.margin(getDefaultMargins());

		builder.status(status);

		return builder.build();
	}

	private int computeStatus(TextBlock textBlock) {
		if (this instanceof PSystemError)
			return FileImageData.ERROR;
		if (textBlock instanceof GraphvizCrash)
			return FileImageData.CRASH;
		if (textBlock instanceof IEntityImage && ((IEntityImage) textBlock).isCrash())
			return FileImageData.CRASH;
		return 0;
	}

}
