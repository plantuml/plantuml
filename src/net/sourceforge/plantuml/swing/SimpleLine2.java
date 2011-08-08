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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.sourceforge.plantuml.GeneratedImage;

class SimpleLine2 implements Comparable<SimpleLine2> {

	private final File file;
	private final GeneratedImage generatedImage;
	private final Future<List<GeneratedImage>> future;

	public SimpleLine2(File file, GeneratedImage generatedImage, Future<List<GeneratedImage>> future) {
		this.generatedImage = generatedImage;
		this.file = file;
		this.future = future;
	}

	public File getFile() {
		return file;
	}

	public boolean pendingAndFinished() {
		return generatedImage == null && future.isDone();
	}

	public List<SimpleLine2> getFutureTerminated() throws InterruptedException, ExecutionException {
		if (future == null) {
			throw new IllegalStateException();
		}
		final List<GeneratedImage> list = future.get();
		if (list == null) {
			return null;
		}
		final List<SimpleLine2> result = new ArrayList<SimpleLine2>();
		for (GeneratedImage im : list) {
			result.add(new SimpleLine2(file, im, null));
		}
		return Collections.unmodifiableList(result);
	}

	@Override
	public String toString() {
		if (generatedImage == null) {
			return file.getName() + " (...pending...)";
		}
		final StringBuilder sb = new StringBuilder(generatedImage.getPngFile().getName());
		sb.append(" ");
		sb.append(generatedImage.getDescription());
		return sb.toString();
	}

	public Future<List<GeneratedImage>> getFuture() {
		return future;
	}

	public int compareTo(SimpleLine2 other) {
		return toString().compareTo(other.toString());
	}

	public GeneratedImage getGeneratedImage() {
		return generatedImage;
	}

}
