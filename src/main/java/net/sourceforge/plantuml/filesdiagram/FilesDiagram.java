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
package net.sourceforge.plantuml.filesdiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.jsondiagram.StyleExtractor;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.text.StringLocated;

public class FilesDiagram extends UmlDiagram {

	private final FilesListing list;

	public FilesDiagram(UmlSource source, StyleExtractor styleExtractor, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.FILES, null, preprocessing);

		final ISkinParam skinParam = getSkinParam();
		try {
			styleExtractor.applyStyles(skinParam);
		} catch (StyleParsingException e) {
			e.printStackTrace();
		}
		final Style style = StyleSignatureBasic.of(SName.root, SName.element, SName.filesDiagram) //
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// final FontConfiguration fontConfiguration =
		// FontConfiguration.blackBlueTrue(UFont.courier(14));
		final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());

		this.list = new FilesListing(skinParam, fontConfiguration);

		final Iterator<StringLocated> it = source.iterator2();
		it.next();
		while (it.hasNext()) {
			final String line = it.next().getString();
			if (line.startsWith("/"))
				this.list.addRawEntry(line.substring(1));
			else if (line.startsWith("<note>"))
				this.list.addNote(getNote(it));
		}

	}

	private List<String> getNote(Iterator<StringLocated> it) {
		final List<String> result = new ArrayList<String>();
		while (it.hasNext()) {
			final String line = it.next().getString();
			if (line.startsWith("</note>"))
				return result;
			result.add(line);
		}
		return Collections.unmodifiableList(result);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Files)");
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return list;
	}

}
