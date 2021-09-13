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
package net.sourceforge.plantuml.security;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Secure replacement for java.io.File.
 * <p>
 * This class should be used instead of java.io.File. There are few exceptions
 * (mainly in the Swing part and in the ANT task)
 * <p>
 * This class does some control access and in secure mode hide the real path of
 * file, so that it cannot be printed to end users.
 *
 */
public class SFile implements Comparable<SFile> {

	public static String separator = File.separator;

	public static String pathSeparator = File.pathSeparator;

	public static char separatorChar = File.separatorChar;

	private final File internal;

	@Override
	public String toString() {
		return "Image42";
	}

	public SFile(String nameOrPath) {
		this(new File(nameOrPath));
	}

	public SFile(String dirname, String name) {
		this(new File(dirname, name));
	}

	public SFile(SFile basedir, String name) {
		this(new File(basedir.internal, name));
	}

	public SFile(URI uri) {
		this(new File(uri));
	}

	private SFile(File internal) {
		this.internal = internal;
	}

	public static SFile fromFile(File internal) {
		if (internal == null) {
			return null;
		}
		return new SFile(internal);
	}

	public SFile file(String name) {
		return new SFile(this, name);
	}

	public boolean exists() {
		if (internal != null && isFileOk())
			return internal.exists();
		return false;
	}

	public SFile getCanonicalFile() throws IOException {
		return new SFile(internal.getCanonicalFile());
	}

	public boolean isAbsolute() {
		return internal != null && internal.isAbsolute();
	}

	public boolean isDirectory() {
		return internal != null && internal.exists() && internal.isDirectory();
	}

	public String getName() {
		return internal.getName();
	}

	public boolean isFile() {
		return internal != null && internal.isFile();
	}

	public long lastModified() {
		return internal.lastModified();
	}

	public int compareTo(SFile other) {
		return this.internal.compareTo(other.internal);
	}

	public String getPath() {
		return internal.getPath();
	}

	public long length() {
		return internal.length();
	}

	public boolean canWrite() {
		return internal.canWrite();
	}

	public void setWritable(boolean b) {
		internal.setWritable(b);
	}

	public void delete() {
		internal.delete();
	}

	public Collection<SFile> listFiles() {
		final File[] tmp = internal.listFiles();
		final List<SFile> result = new ArrayList<>(tmp.length);
		for (File f : tmp) {
			result.add(new SFile(f));
		}
		return Collections.unmodifiableCollection(result);
	}

	public String[] list() {
		return internal.list();
	}

	public SFile getAbsoluteFile() {
		return new SFile(internal.getAbsoluteFile());
	}

	public SFile getParentFile() {
		return new SFile(internal.getParentFile());
	}

	@Override
	public int hashCode() {
		return internal.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return internal.equals(((SFile) obj).internal);
	}

	public String getAbsolutePath() {
		return internal.getAbsolutePath();
	}

	public String getPrintablePath() {
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE) {
			try {
				return internal.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public boolean canRead() {
		return internal.canRead();
	}

	public void deleteOnExit() {
		internal.deleteOnExit();
	}

	public void mkdirs() {
		internal.mkdirs();
	}

	public static SFile createTempFile(String prefix, String suffix) throws IOException {
		return new SFile(File.createTempFile(prefix, suffix));
	}

	public URI toURI() {
		return internal.toURI();
	}

	public boolean renameTo(SFile dest) {
		return internal.renameTo(dest.internal);
	}

	/**
	 * Check SecurityProfile to see if this file can be open.
	 */
	private boolean isFileOk() {
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.SANDBOX) {
			// In SANDBOX, we cannot read any files
			return false;
		}
		// Files in "plantuml.include.path" and "plantuml.allowlist.path" are ok.
		if (isInAllowList(SecurityUtils.getPath("plantuml.include.path"))) {
			return true;
		}
		if (isInAllowList(SecurityUtils.getPath("plantuml.allowlist.path"))) {
			return true;
		}
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.INTERNET) {
			return false;
		}
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.ALLOWLIST) {
			return false;
		}
		if (SecurityUtils.getSecurityProfile() != SecurityProfile.UNSECURE) {
			// For UNSECURE, we did not do those checks
			final String path = getCleanPathSecure();
			if (path.startsWith("/etc/") || path.startsWith("/dev/") || path.startsWith("/boot/")
					|| path.startsWith("/proc/") || path.startsWith("/sys/")) {
				return false;
			}
			if (path.startsWith("//")) {
				return false;
			}
		}
		return true;
	}

	private boolean isInAllowList(List<SFile> allowlist) {
		final String path = getCleanPathSecure();
		for (SFile allow : allowlist) {
			if (path.startsWith(allow.getCleanPathSecure())) {
				// File directory is in the allowlist
				return true;
			}
		}
		return false;
	}

	private String getCleanPathSecure() {
		String result = internal.getAbsolutePath();
		result = result.replace("\0", "");
		result = result.replace("\\\\", "/");
		return result;
	}

	// Reading
	// http://forum.plantuml.net/9048/img-tag-for-sequence-diagram-participants-does-always-render
	public BufferedImage readRasterImageFromFile() {
		// https://www.experts-exchange.com/questions/26171948/Why-are-ImageIO-read-images-losing-their-transparency.html
		// https://stackoverflow.com/questions/18743790/can-java-load-images-with-transparency
		if (isFileOk())
			try {
				return SecurityUtils.readRasterImage(new ImageIcon(this.getAbsolutePath()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	public BufferedReader openBufferedReader() {
		if (isFileOk()) {
			try {
				return new BufferedReader(new FileReader(internal));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public File conv() {
		return internal;
	}

	public InputStream openFile() {
		if (isFileOk())
			try {
				return new BufferedInputStream(new FileInputStream(internal));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		return null;
	}

	// Writing
	public BufferedOutputStream createBufferedOutputStream() throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(internal));
	}

	public PrintWriter createPrintWriter() throws FileNotFoundException {
		return new PrintWriter(internal);
	}

	public PrintWriter createPrintWriter(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		return new PrintWriter(internal, charset);
	}

	public FileOutputStream createFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(internal);
	}

	public FileOutputStream createFileOutputStream(boolean append) throws FileNotFoundException {
		return new FileOutputStream(internal, append);
	}

	public PrintStream createPrintStream() throws FileNotFoundException {
		return new PrintStream(internal);
	}

	public PrintStream createPrintStream(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		return new PrintStream(internal, charset);
	}

	public PrintStream createPrintStream(Charset charset) throws FileNotFoundException, UnsupportedEncodingException {
		return new PrintStream(internal, charset.name());
	}

}
