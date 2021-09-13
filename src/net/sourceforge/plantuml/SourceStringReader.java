/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.ugraphic.ImageBuilder.plainImageBuilder;
import static net.sourceforge.plantuml.utils.CharsetUtils.charsetOrDefault;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;

public class SourceStringReader {

	final private List<BlockUml> blocks;

	public SourceStringReader(String source) {
		this(Defines.createEmpty(), source, Collections.<String>emptyList());
	}

	public SourceStringReader(String source, String charset) {
		this(Defines.createEmpty(), source, UTF_8.name(), Collections.<String>emptyList());
	}

	public SourceStringReader(String source, Charset charset) {
		this(Defines.createEmpty(), source, charset.name(), Collections.<String>emptyList());
	}

	public SourceStringReader(Defines defines, String source, List<String> config) {
		this(defines, source, UTF_8.name(), config);
	}

	public SourceStringReader(Defines defines, String source) {
		this(defines, source, UTF_8.name(), Collections.<String>emptyList());
	}

	public SourceStringReader(String source, SFile newCurrentDir) {
		this(Defines.createEmpty(), source, UTF_8, Collections.<String>emptyList(), newCurrentDir);
	}

	public SourceStringReader(Defines defines, String source, String charset, List<String> config) {
		this(defines, source, charset, config, FileSystem.getInstance().getCurrentDir());
	}

	public SourceStringReader(Defines defines, String source, String charset, List<String> config, SFile newCurrentDir) {
		this(defines, source, charsetOrDefault(charset), config, newCurrentDir);
	}
	
	public SourceStringReader(Defines defines, String source, Charset charset, List<String> config,
			SFile newCurrentDir) {
		// // WARNING GLOBAL LOCK HERE
		// synchronized (SourceStringReader.class) {
		try {
			final BlockUmlBuilder builder = new BlockUmlBuilder(config, charset, defines, new StringReader(source),
					newCurrentDir, "string");
			this.blocks = builder.getBlockUmls();
		} catch (IOException e) {
			Log.error("error " + e);
			throw new IllegalStateException(e);
		}
		// }
	}

	@Deprecated
	public String generateImage(OutputStream os) throws IOException {
		return outputImage(os).getDescription();
	}

	public DiagramDescription outputImage(OutputStream os) throws IOException {
		return outputImage(os, 0);
	}

	@Deprecated
	public String generateImage(SFile f) throws IOException {
		return outputImage(f).getDescription();
	}

	public DiagramDescription outputImage(SFile f) throws IOException {
		try (OutputStream os = f.createBufferedOutputStream()) {
			return outputImage(os, 0);
		}
	}

	@Deprecated
	public String generateImage(OutputStream os, FileFormatOption fileFormatOption) throws IOException {
		return outputImage(os, fileFormatOption).getDescription();
	}

	public DiagramDescription outputImage(OutputStream os, FileFormatOption fileFormatOption) throws IOException {
		return outputImage(os, 0, fileFormatOption);
	}

	@Deprecated
	public String generateImage(OutputStream os, int numImage) throws IOException {
		return outputImage(os, numImage).getDescription();
	}

	public DiagramDescription outputImage(OutputStream os, int numImage) throws IOException {
		return outputImage(os, numImage, new FileFormatOption(FileFormat.PNG));
	}

	@Deprecated
	public String generateImage(OutputStream os, int numImage, FileFormatOption fileFormatOption) throws IOException {
		return outputImage(os, numImage, fileFormatOption).getDescription();
	}

	public DiagramDescription outputImage(OutputStream os, int numImage, FileFormatOption fileFormatOption)
			throws IOException {
		if (blocks.size() == 0) {
			noStartumlFound(os, fileFormatOption);
			return null;
		}
		for (BlockUml b : blocks) {
			final Diagram system = b.getDiagram();
			final int nbInSystem = system.getNbImages();
			if (numImage < nbInSystem) {
				// final CMapData cmap = new CMapData();
				final ImageData imageData = system.exportDiagram(os, numImage, fileFormatOption);
				// if (imageData.containsCMapData()) {
				// return system.getDescription().getDescription() + BackSlash.BS_N +
				// imageData.getCMapData("plantuml");
				// }
				return system.getDescription();
			}
			numImage -= nbInSystem;
		}
		Log.error("numImage is too big = " + numImage);
		return null;

	}

	public DiagramDescription generateDiagramDescription(int numImage, FileFormatOption fileFormatOption) {
		if (blocks.size() == 0) {
			return null;
		}
		for (BlockUml b : blocks) {
			final Diagram system = b.getDiagram();
			final int nbInSystem = system.getNbImages();
			if (numImage < nbInSystem) {
				// final ImageData imageData = system.exportDiagram(os, numImage,
				// fileFormatOption);
				// if (imageData.containsCMapData()) {
				// return
				// system.getDescription().withCMapData(imageData.getCMapData("plantuml"));
				// }
				return system.getDescription();
			}
			numImage -= nbInSystem;
		}
		Log.error("numImage is too big = " + numImage);
		return null;
	}

	public DiagramDescription generateDiagramDescription() {
		return generateDiagramDescription(0);
	}

	public DiagramDescription generateDiagramDescription(FileFormatOption fileFormatOption) {
		return generateDiagramDescription(0, fileFormatOption);
	}

	public DiagramDescription generateDiagramDescription(int numImage) {
		return generateDiagramDescription(numImage, new FileFormatOption(FileFormat.PNG));
	}

	public String getCMapData(int numImage, FileFormatOption fileFormatOption) throws IOException {
		if (blocks.size() == 0) {
			return null;
		}
		for (BlockUml b : blocks) {
			final Diagram system = b.getDiagram();
			final int nbInSystem = system.getNbImages();
			if (numImage < nbInSystem) {
				final ImageData imageData = system.exportDiagram(new NullOutputStream(), numImage, fileFormatOption);
				if (imageData.containsCMapData()) {
					return imageData.getCMapData("plantuml");
				}
				return null;
			}
			numImage -= nbInSystem;
		}
		return null;

	}

	public ImageData noStartumlFound(OutputStream os, FileFormatOption fileFormatOption) throws IOException {
		final TextBlockBackcolored error = GraphicStrings.createForError(Arrays.asList("No @startuml/@enduml found"),
				fileFormatOption.isUseRedForError());

		return plainImageBuilder(error, fileFormatOption)
				.write(os);
	}

	public final List<BlockUml> getBlocks() {
		return Collections.unmodifiableList(blocks);
	}

}
