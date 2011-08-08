/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SignatureUtils;

public class GraphvizCached implements Graphviz {

	private final AbstractGraphviz graphviz;

	public GraphvizCached(AbstractGraphviz graphviz) {
		this.graphviz = graphviz;
	}

	public void createFile(OutputStream os) throws IOException, InterruptedException {
		final File f = getCachedFile();

		if (f.exists()) {
			Log.info("Using " + f);
			FileUtils.copyToStream(f, os);
			return;
		}

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		graphviz.createFile(baos);
		baos.close();
		final byte data[] = baos.toByteArray();

		Log.info("Creating " + f);
		final OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
		fos.write(data);
		fos.close();

		final InputStream is = new ByteArrayInputStream(data);
		FileUtils.copyToStream(is, os);
		is.close();
	}

	private File getCachedFile() throws FileNotFoundException {
		final String dot = graphviz.getDotString();
		final List<String> types = graphviz.getType();
		final String sign = SignatureUtils.getSignature(dot + types);

		final File source = new File("__graphviz", sign + ".txt");
		source.getParentFile().mkdirs();
		final PrintWriter pw = new PrintWriter(source);
		pw.println(types.toString());
		pw.println(dot);
		pw.close();

		final File result = new File("__graphviz", sign);
		result.getParentFile().mkdirs();
		return result;
	}

	public String dotVersion() throws IOException, InterruptedException {
		return graphviz.dotVersion();
	}

	public File getDotExe() {
		return graphviz.getDotExe();
	}

	public String testFile(String dotfilename, String outfile) throws IOException, InterruptedException {
		return graphviz.testFile(dotfilename, outfile);
	}

}