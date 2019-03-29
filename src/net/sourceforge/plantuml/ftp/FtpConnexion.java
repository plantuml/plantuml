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
package net.sourceforge.plantuml.ftp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

public class FtpConnexion {

	private final String user;
	private final Map<String, String> incoming = new HashMap<String, String>();
	private final Map<String, byte[]> outgoing = new HashMap<String, byte[]>();
	private final Set<String> futureOutgoing = new HashSet<String>();

	private FileFormat fileFormat;

	public FtpConnexion(String user, FileFormat defaultfileFormat) {
		this.user = user;
		this.fileFormat = defaultfileFormat;
	}

	public synchronized void addIncoming(String fileName, String data) {
		if (fileName.startsWith("/")) {
			throw new IllegalArgumentException();
		}
		incoming.put(fileName, data);
	}

	public synchronized void futureOutgoing(String fileName) {
		outgoing.remove(fileName);
		futureOutgoing.add(fileName);
	}

	public synchronized Collection<String> getFiles() {
		final List<String> result = new ArrayList<String>(incoming.keySet());
		result.addAll(outgoing.keySet());
		result.addAll(futureOutgoing);
		return Collections.unmodifiableCollection(result);
	}

	public synchronized boolean willExist(String fileName) {
		if (incoming.containsKey(fileName)) {
			return true;
		}
		if (outgoing.containsKey(fileName)) {
			return true;
		}
		if (futureOutgoing.contains(fileName)) {
			return true;
		}
		return false;
	}

	public synchronized boolean doesExist(String fileName) {
		if (incoming.containsKey(fileName)) {
			return true;
		}
		if (outgoing.containsKey(fileName)) {
			return true;
		}
		return false;
	}

	public synchronized byte[] getData(String fileName) throws InterruptedException {
		if (fileName.startsWith("/")) {
			throw new IllegalArgumentException();
		}
		final String data = incoming.get(fileName);
		if (data != null) {
			return data.getBytes();
		}
		// do {
		// if (willExist(fileName) == false) {
		// return null;
		// }
		final byte data2[] = outgoing.get(fileName);
		if (data2 == null) {
			return new byte[1];
		}
		// if (data2 != null) {
		return data2;
		// }
		// Thread.sleep(200L);
		// } while (true);
	}

	public synchronized int getSize(String fileName) {
		if (fileName.startsWith("/")) {
			throw new IllegalArgumentException();
		}
		final String data = incoming.get(fileName);
		if (data != null) {
			return data.length();
		}
		final byte data2[] = outgoing.get(fileName);
		if (data2 != null) {
			return data2.length;
		}
		return 0;
	}

	public void processImage(String fileName) throws IOException {
		if (fileName.startsWith("/")) {
			throw new IllegalArgumentException();
		}
		final String pngFileName = getFutureFileName(fileName);
		boolean done = false;
		try {
			final SourceStringReader sourceStringReader = new SourceStringReader(incoming.get(fileName));
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final FileFormat format = getFileFormat();
			final DiagramDescription desc = sourceStringReader.generateDiagramDescription(new FileFormatOption(format));
			final List<BlockUml> blocks = sourceStringReader.getBlocks();
			if (blocks.size() > 0) {
				blocks.get(0).getDiagram().exportDiagram(baos, 0, new FileFormatOption(format));
			}
			final String errorFileName = pngFileName.substring(0, pngFileName.length() - 4) + ".err";
			synchronized (this) {
				outgoing.remove(pngFileName);
				futureOutgoing.remove(pngFileName);
				outgoing.remove(errorFileName);
				if (desc != null && desc.getDescription() != null) {
					outgoing.put(pngFileName, baos.toByteArray());
					done = true;
					if (desc.getDescription().startsWith("(Error)")) {
						final ByteArrayOutputStream errBaos = new ByteArrayOutputStream();
						sourceStringReader.outputImage(errBaos, new FileFormatOption(FileFormat.ATXT));
						errBaos.close();
						outgoing.put(errorFileName, errBaos.toByteArray());
					}
				}
			}
		} finally {
			if (done == false) {
				outgoing.put(pngFileName, new byte[0]);
			}
		}
	}

	public String getFutureFileName(String fileName) {
		return getFileFormat().changeName(fileName, 0);
	}

	private FileFormat getFileFormat() {
		return fileFormat;
	}

	public synchronized void delete(String fileName) {
		if (fileName.contains("*")) {
			incoming.clear();
			outgoing.clear();
			futureOutgoing.clear();
		} else {
			incoming.remove(fileName);
			outgoing.remove(fileName);
			futureOutgoing.add(fileName);
		}
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;

	}

}
