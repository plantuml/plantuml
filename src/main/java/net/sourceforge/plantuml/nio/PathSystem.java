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
package net.sourceforge.plantuml.nio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;

//::comment when JAVA8
import org.teavm.jso.JSObject;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.teavm.browser.BrowserLog;
import net.sourceforge.plantuml.teavm.browser.TeaVmFileLoader;
import net.sourceforge.plantuml.teavm.browser.TeaVmScriptLoader;
//::done

// Replacement for FileSystem
//See ImportedFiles
//See TContext::executeInclude
//See PreprocessorUtils

public class PathSystem {

	public static PathSystem fetch() {
		// ::comment when JAVA8
		if (TeaVM.isTeaVM())
			return new PathSystem(null, new ArrayList<NFolderZip>(), true);
		// ::done
		return new PathSystem(new NFolderRegular(Paths.get("")), new ArrayList<NFolderZip>(), true);
	}

	// Same resolution as for !include (see loadTeaVMStdlib): a library such as
	// material7 only carries a link to the versioned one that holds the data.
	public JsonValue getTeaVMStdlibJson(String path) {
		// ::revert when JAVA8
		// return null;
		path = path.replaceAll("\\.json$", "");
		final String full = path.toLowerCase();
		final int slash = full.indexOf('/');
		if (slash == -1)
			return null;

		final String filepath = full.substring(slash + 1);
		final String libname = loadTeaVMStdlib(full.substring(0, slash));
		if (libname == null)
			return null;

		final JSObject data = TeaVmScriptLoader.getRaw_PLANTUML_STDLIB_JSON(libname, filepath);
		if (data == null)
			return null;
		final String json = TeaVmScriptLoader.stringify(data);
		return Json.parse(json);
		// ::done
	}

	public InputStream getTeaVMStdlibInputStream(String path) {
		// ::revert when JAVA8
		// return null;
		final String full = path.substring(1, path.length() - 1).toLowerCase();
		final int slash = full.indexOf('/');
		if (slash == -1)
			return null;

		final String filepath = full.substring(slash + 1);
		final String libname = loadTeaVMStdlib(full.substring(0, slash));
		if (libname == null)
			return null;

		final JSObject data = TeaVmScriptLoader.getRaw_PLANTUML_STDLIB(libname, filepath);
		if (data == null)
			return null;
		final String content = TeaVmScriptLoader.joinLines(data);
		return new ByteArrayInputStream(content.getBytes(java.nio.charset.StandardCharsets.UTF_8));
		// ::done
	}

	/**
	 * The identifier the browser host gave to a file its file loader delivered,
	 * which is what an include strategy compares for such a file; <code>null</code>
	 * for any other file, and always on the JVM, which never reaches the browser
	 * classes.
	 */
	public String getTeaVMFileId(InputFile file) {
		// ::revert when JAVA8
		// return null;
		if (TeaVM.isTeaVM() == false)
			return null;

		return TeaVmFileLoader.getId(file);
		// ::done
	}

	/**
	 * This path system with the host's file loader out of reach. The browser
	 * build evaluates a standard-library file or a bundled theme in it: those
	 * are the engine's own, so a relative include written in one of them is not
	 * handed to the host as if the diagram had written it. The host is reached
	 * again when the caller restores the previous path system.
	 */
	public PathSystem withoutHostFiles() {
		return new PathSystem(currentFolder, importedFolders, false);
	}

	// ::comment when JAVA8
	/**
	 * Loads the bundle <code>&lt;libname&gt;.min.js</code> and, when its info
	 * carries a <code>link</code> (e.g. material7 -&gt; material7.4.47), the
	 * bundle it points to.
	 *
	 * @param libname the library name, lower case
	 * @return the name of the library that actually holds the data, or
	 *         <code>null</code> if a bundle could not be loaded (unknown library,
	 *         network error, refused by a PLANTUML_STDLIB_LOADER hook). The
	 *         caller then reports an ordinary "cannot include" error instead of
	 *         letting the loader's exception abort the whole rendering.
	 */
	private String loadTeaVMStdlib(String libname) {
		if (loadTeaVMStdlibBundle(libname) == false)
			return null;

		final String link = getInfo(libname).get("link");
		if (link == null)
			return libname;

		BrowserLog.consoleLog(getClass(), "Following link to " + link);
		if (loadTeaVMStdlibBundle(link) == false)
			return null;

		return link;
	}

	private boolean loadTeaVMStdlibBundle(String libname) {
		try {
			TeaVmScriptLoader.loadOnceSync(libname + ".min.js");
			return true;
		} catch (RuntimeException e) {
			TeaVmScriptLoader
					.consoleWarn("PlantUML: cannot load stdlib bundle " + libname + ".min.js: " + e.getMessage());
			return false;
		}
	}

	private Map<String, String> getInfo(final String libname) {
		final JSObject info = TeaVmScriptLoader.getRaw_PLANTUML_STDLIB_INFO(libname);
		final Map<String, String> map = new HashMap<>();
		if (info != null) {
			final String keys = TeaVmScriptLoader.getObjectKeys(info);
			for (String key : keys.split(",")) {
				final String value = TeaVmScriptLoader.getStringProperty(info, key);
				BrowserLog.consoleLog(getClass(), "info[" + libname + "] " + key + " = " + value);
				map.put(key, value);
			}
		} else {
			BrowserLog.consoleLog(getClass(), "No info found for " + libname);
		}
		return map;
	}
	// ::done

	private final NFolder currentFolder;

	/**
	 * Archives registered with <code>!import</code>. The list is deliberately
	 * shared between all the PathSystem derived from the same root (when the
	 * current directory changes while including a file), so that an import stays
	 * effective for the rest of the diagram, whatever the include depth.
	 */
	private final List<NFolderZip> importedFolders;

	/**
	 * Whether the browser host's file loader may be asked for a local file. False
	 * while a standard-library file or a bundled theme is being evaluated (see
	 * {@link #withoutHostFiles()}); meaningless on the JVM.
	 */
	private final boolean hostFiles;

	private PathSystem(NFolder currentFolder, List<NFolderZip> importedFolders, boolean hostFiles) {
		this.currentFolder = currentFolder;
		this.importedFolders = importedFolders;
		this.hostFiles = hostFiles;
	}

	public PathSystem changeCurrentDirectory(NFolder newCurrentDir) {
		// ::comment when JAVA8
		if (TeaVM.isTeaVM())
			return this;
		// ::done

		return new PathSystem(newCurrentDir, importedFolders, hostFiles);
	}

	public PathSystem changeCurrentDirectory(SFile newCurrentDir) throws IOException {
		// ::comment when JAVA8
		if (TeaVM.isTeaVM())
			return this;
		// ::done

		if (newCurrentDir == null)
			return this;

		final Path path = newCurrentDir.toPath();
		if (path == null)
			return this;

		final NFolder folder = currentFolder.getSubfolder(path);
		return new PathSystem(folder, importedFolders, hostFiles);
	}

	public PathSystem withCurrentDir(NFolder parentFile) {
		return new PathSystem(parentFile, importedFolders, hostFiles);
	}

	public NFolder getCurrentDir() {
		return currentFolder;
	}

	public InputFile getFile(String filename, String suffix) throws IOException {
		return getInputFile(filename);
	}

	@Override
	public String toString() {
		return currentFolder.toString();
	}

	public InputFile getInputFile(String path) throws IOException {
		// ::comment when JAVA8
		// The browser build has no file system: a local file is whatever the host's
		// file loader delivers, or null when there is none. A URL and a
		// standard-library path keep their own routes (TContext handles both
		// before coming here), and the engine's own files have no host to ask.
		if (TeaVM.isTeaVM()) {
			if (hostFiles == false || path.startsWith("http://") || path.startsWith("https://")
					|| (path.startsWith("<") && path.endsWith(">")))
				return null;
			return TeaVmFileLoader.getInputFile(path, currentFolder);
		}
		// ::done

		if (path.startsWith("http://") || path.startsWith("https://")) {
			final SURL url = SURL.create(path);
			if (url == null)
				throw new IOException("Cannot open URL " + path);
			return new InputFileUrl(url);
		}

		if (path.startsWith("<") && path.endsWith(">")) {
			final String full = path.substring(1, path.length() - 1).toLowerCase();
			final String libname = full.substring(0, full.indexOf('/'));
			final String filepath = full.substring(libname.length() + 1);
			return new InputFileStdlib(Stdlib.retrieve(libname), Paths.get(filepath));
		}

		if (path.startsWith("::")) {
			// non-standard syntax: resolve from process launch directory
			String rel = path.substring(2); // remove leading "::"
			// allow both ::/foo.puml and ::./foo.puml
			if (rel.startsWith("/"))
				rel = rel.substring(1);
			final Path target = Paths.get("").toAbsolutePath().resolve(rel).normalize();
			final SFile result = SFile.fromFile(target.toFile());
			if (result.isFileOk())
				return result;
			return null;
		}
		if (path.startsWith("~/")) {
			// Expand to the user's home directory
			final String home = System.getProperty("user.home");
			final Path homePath = Paths.get(home).resolve(path.substring(2)).normalize();
			final SFile result = SFile.fromFile(homePath.toFile());
			if (result.isFileOk())
				return result;
			return null;
		}

		final InputFile result = currentFolder.getInputFile(Paths.get(path));
		// NFolderZip never returns null, so check that the entry really exists
		final boolean missingInCurrentArchive = currentFolder instanceof NFolderZip
				&& ((NFolderZip) currentFolder).contains(Paths.get(path)) == false;
		if (result != null && missingInCurrentArchive == false)
			return result;

		// Not found from the current directory: look inside the archives given to
		// !import, using the path as is (relative to the root of each archive).
		for (NFolderZip imported : importedFolders)
			if (imported.contains(Paths.get(path)))
				return imported.getInputFile(Paths.get(path));

		// null for a regular folder; for an archive, an InputFile that reports the
		// missing entry when read
		return result;
	}

	public static void main(String[] args) {
		System.out.println(PathSystem.fetch());
	}

	public void addImportFile(SFile file) {
		final NFolderZip zip = new NFolderZip(file.conv());
		for (NFolderZip already : importedFolders)
			if (already.toString().equals(zip.toString()))
				return;
		importedFolders.add(zip);
	}

}
