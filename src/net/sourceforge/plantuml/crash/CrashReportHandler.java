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
package net.sourceforge.plantuml.crash;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.security.SecurityUtils;

public class CrashReportHandler extends ReportLog {

	private final Throwable exception;
	private final String metadata;
	private final String flash;

	public CrashReportHandler(Throwable exception, String metadata, String flash) {
		this.metadata = metadata;
		this.exception = exception;
		this.flash = flash;
	}

	public void exportDiagramError(FileFormatOption fileFormat, long seed, OutputStream os) throws IOException {

		// ::comment when __CORE__
		if (fileFormat.getFileFormat() == FileFormat.ATXT || fileFormat.getFileFormat() == FileFormat.UTXT) {
			exportDiagramErrorText(os, exception);
			return;
		}
		// ::done

		final CrashImage image = new CrashImage(exception, flash, this);

		ImageBuilder.create(fileFormat, image).metadata(metadata).seed(seed).write(os);
	}

	// ::comment when __CORE__
	private void exportDiagramErrorText(OutputStream os, Throwable exception) {
		final PrintWriter pw = SecurityUtils.createPrintWriter(os);
		exception.printStackTrace(pw);
		pw.println();
		pw.println();
		for (String s : this) {
			s = s.replaceAll("\\</?\\w+?\\>", "");
			pw.println(s);
		}
		pw.flush();
	}
	// ::done

}
