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
import java.util.HashMap;
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
import net.sourceforge.plantuml.teavm.browser.TeaVmScriptLoader;
//::done

// Replacement for FileSystem
//See ImportedFiles
//See TContext::executeInclude
//See PreprocessorUtils

public class PathSystem {

	public static PathSystem fetch() {
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__ JAVA8
		if (TeaVM.isTeaVM())
			return new PathSystem(null);
		// ::done
		return new PathSystem(new NFolderRegular(Paths.get("")));
	}

	public JsonValue getTeaVMStdlibJson(String path) {
		// ::revert when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__ JAVA8
		// return null;
		path = path.replaceAll("\\.json$", "");
		final String full = path.toLowerCase();
		final String libname = full.substring(0, full.indexOf('/'));
		final String filepath = full.substring(libname.length() + 1);
		TeaVmScriptLoader.loadOnceSync(libname + ".min.js");

		final JSObject data = TeaVmScriptLoader.getRaw_PLANTUML_STDLIB_JSON(libname, filepath);
		if (data == null)
			return null;
		final String json = TeaVmScriptLoader.stringify(data);
		return Json.parse(json);
		// ::done
	}

	public InputStream getTeaVMStdlibInputStream(String path) {
		// ::revert when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__ JAVA8
		// return null;
		final String full = path.substring(1, path.length() - 1).toLowerCase();
		String libname = full.substring(0, full.indexOf('/'));
		final String filepath = full.substring(libname.length() + 1);
		TeaVmScriptLoader.loadOnceSync(libname + ".min.js");

		final Map<String, String> infos = getInfo(libname);
		final String link = infos.get("link");

		if (link != null) {
			libname = link;
			BrowserLog.consoleLog(getClass(), "Following link to " + libname);
			TeaVmScriptLoader.loadOnceSync(libname + ".min.js");
		}

		final JSObject data = TeaVmScriptLoader.getRaw_PLANTUML_STDLIB(libname, filepath);
		if (data == null)
			return null;
		final String content = TeaVmScriptLoader.joinLines(data);
		return new ByteArrayInputStream(content.getBytes(java.nio.charset.StandardCharsets.UTF_8));
		// ::done
	}

	// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__ JAVA8
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

	private PathSystem(NFolder currentFolder) {
		this.currentFolder = currentFolder;
	}

	public PathSystem changeCurrentDirectory(NFolder newCurrentDir) {
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__ JAVA8
		if (TeaVM.isTeaVM())
			return this;
		// ::done

		return new PathSystem(newCurrentDir);
	}

	public PathSystem changeCurrentDirectory(SFile newCurrentDir) throws IOException {
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__ JAVA8
		if (TeaVM.isTeaVM())
			return this;
		// ::done

		if (newCurrentDir == null)
			return this;

		final Path path = newCurrentDir.toPath();
		if (path == null)
			return this;

		final NFolder folder = currentFolder.getSubfolder(path);
		return new PathSystem(folder);
	}

	public PathSystem withCurrentDir(NFolder parentFile) {
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__ JAVA8
		if (TeaVM.isTeaVM())
			return this;
		// ::done

		return new PathSystem(parentFile);
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

		return currentFolder.getInputFile(Paths.get(path));
	}

	public static void main(String[] args) {
		System.out.println(PathSystem.fetch());
	}

	public void addImportFile(SFile file) {
		// Nothing right now

	}

}
