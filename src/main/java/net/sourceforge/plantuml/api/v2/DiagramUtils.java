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
package net.sourceforge.plantuml.api.v2;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.BlockUmlBuilder;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SImageIO;

public abstract class DiagramUtils {

	public static DiagramReturn exportDiagram(String... source) throws IOException {

		final Reader reader = new StringReader(String.join("\n", source));

		final BlockUmlBuilder builder = new BlockUmlBuilder(Collections.<String>emptyList(),
				java.nio.charset.StandardCharsets.UTF_8, Defines.createEmpty(), reader,
				FileSystem.getInstance().getCurrentDir(), "string");

		final List<BlockUml> blocks = builder.getBlockUmls();
		if (blocks.size() == 0)
			return new DiagramReturnError("No @start/@end found");

		final BlockUml blockUml = blocks.get(0);

		final Diagram diagram = blockUml.getDiagram();

		return new DiagramReturn() {
			@Override
			public Diagram getDiagram() {
				return diagram;
			}

			@Override
			public String error() {
				if (diagram instanceof PSystemError) {
					final PSystemError diagramError = (PSystemError) diagram;
					final ErrorUml error = diagramError.getFirstError();
					return error.getError();
				}
				return null;
			}

			@Override
			public Optional<Integer> getErrorLine() {
				if (diagram instanceof PSystemError) {
					final PSystemError diagramError = (PSystemError) diagram;
					final ErrorUml error = diagramError.getFirstError();
					return Optional.of(error.getLineLocation().getPosition());
				}
				return Optional.empty();
			}

			@Override
			public BufferedImage asImage() throws IOException {
				try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
					final FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.PNG);
					final ImageData imageData = diagram.exportDiagram(os, 0, fileFormatOption);
					return SImageIO.read(os.toByteArray());
				}
			}

			@Override
			public Throwable getRootCause() {
				try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
					final FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.PNG);
					final ImageData imageData = diagram.exportDiagram(os, 0, fileFormatOption);
					return imageData.getRootCause();
				} catch (IOException e) {
					e.printStackTrace();
					return e;
				}
			}

		};
	}

}