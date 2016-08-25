/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

import java.io.File;
import java.nio.charset.Charset;
import java.util.Properties;

import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.syntax.LanguageDescriptor;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.version.Version;

public class OptionPrint {

	static public void printTestDot() throws InterruptedException {
		for (String s : GraphvizUtils.getTestDotStrings(false)) {
			System.out.println(s);
		}
		exit();
	}

	static public void printHelp() throws InterruptedException {

		final String charset = Charset.defaultCharset().displayName();

		System.out.println("Usage: java -jar plantuml.jar [options] -gui");
		System.out.println("\t(to execute the GUI)");
		System.out.println("    or java -jar plantuml.jar [options] [file/dir] [file/dir] [file/dir]");
		System.out.println("\t(to process files or directories)");
		System.out.println();
		System.out.println("You can use the following wildcards in files/dirs:");
		System.out.println("\t*\tmeans any characters but '" + File.separator + "'");
		System.out.println("\t?\tone and only one character but '" + File.separator + "'");
		System.out.println("\t**\tmeans any characters (used to recurse through directories)");
		System.out.println();
		System.out.println("where options include:");
		System.out.println("    -gui\t\tTo run the graphical user interface");
		System.out.println("    -tpng\t\tTo generate images using PNG format (default)");
		System.out.println("    -tsvg\t\tTo generate images using SVG format");
		System.out.println("    -teps\t\tTo generate images using EPS format");
		System.out.println("    -tpdf\t\tTo generate images using PDF format");
		System.out.println("    -tvdx\t\tTo generate images using VDX format");
		System.out.println("    -txmi\t\tTo generate XMI file for class diagram");
		System.out.println("    -tscxml\t\tTo generate SCXML file for state diagram");
		System.out.println("    -thtml\t\tTo generate HTML file for class diagram");
		System.out.println("    -ttxt\t\tTo generate images with ASCII art");
		System.out.println("    -tutxt\t\tTo generate images with ASCII art using Unicode characters");
		System.out.println("    -tlatex\t\tTo generate images using LaTeX/Tikz format");
		System.out.println("    -tlatex:nopreamble\tTo generate images using LaTeX/Tikz format without preamble");
		System.out.println("    -o[utput] \"dir\"\tTo generate images in the specified directory");
		System.out.println("    -DVAR1=value\tTo set a preprocessing variable as if '!define VAR1 value' were used");
		System.out.println("    -Sparam1=value\tTo set a skin parameter as if 'skinparam param1 value' were used");
		System.out.println("    -r[ecurse]\t\trecurse through directories");
		System.out.println("    -config \"file\"\tTo read the provided config file before each diagram");
		System.out.println("    -charset xxx\tTo use a specific charset (default is " + charset + ")");
		System.out.println("    -e[x]clude pattern\tTo exclude files that match the provided pattern");
		System.out.println("    -metadata\t\tTo retrieve PlantUML sources from PNG images");
		System.out.println("    -version\t\tTo display information about PlantUML and Java versions");
		System.out.println("    -checkversion\tTo check if a newer version is available for download");
		System.out.println("    -v[erbose]\t\tTo have log information");
		System.out.println("    -quiet\t\tTo NOT print error message into the console");
		System.out.println("    -debugsvek\t\tTo generate intermediate svek files");
		System.out.println("    -h[elp]\t\tTo display this help message");
		System.out.println("    -testdot\t\tTo test the installation of graphviz");
		System.out.println("    -graphvizdot \"exe\"\tTo specify dot executable");
		System.out.println("    -p[ipe]\t\tTo use stdin for PlantUML source and stdout for PNG/SVG/EPS generation");
		System.out.println("    -encodesprite 4|8|16[z] \"file\"\tTo encode a sprite at gray level (z for compression) from an image");
		System.out.println("    -computeurl|-encodeurl\tTo compute the encoded URL of a PlantUML source file");
		System.out.println("    -decodeurl\t\tTo retrieve the PlantUML source from an encoded URL");
		System.out.println("    -syntax\t\tTo report any syntax error from standard input without generating images");
		System.out.println("    -language\t\tTo print the list of PlantUML keywords");
		System.out.println("    -nosuggestengine\tTo disable the suggest engine when errors in diagrams");
		System.out.println("    -checkonly\t\tTo check the syntax of files without generating images");
		System.out.println("    -failfast\t\tTo stop processing as soon as a syntax error in diagram occurs");
		System.out.println("    -failfast2\t\tTo do a first syntax check before processing files, to fail even faster");
		System.out.println("    -pattern\t\tTo print the list of Regular Expression used by PlantUML");
		System.out.println("    -duration\t\tTo print the duration of complete diagrams processing");
		System.out.println("    -nbthread N\t\tTo use (N) threads for processing");
		System.out.println("    -nbthread auto\tTo use " + Option.defaultNbThreads() + " threads for processing");
		System.out.println("    -author[s]\t\tTo print information about PlantUML authors");
		System.out.println("    -overwrite\t\tTo allow to overwrite read only files");
		System.out.println("    -printfonts\t\tTo print fonts available on your system");
		System.out.println();
		System.out.println("If needed, you can setup the environment variable GRAPHVIZ_DOT.");
		exit();
	}

	static private void exit() throws InterruptedException {
		if (OptionFlags.getInstance().isSystemExit()) {
			System.exit(0);
		}
		throw new InterruptedException("exit");
	}

	public static void printLicense() throws InterruptedException {
		for (String s : License.getCurrent().getText()) {
			System.out.println(s);
		}
		exit();
	}

	public static void printVersion() throws InterruptedException {
		System.out
				.println("PlantUML version " + Version.versionString() + " (" + Version.compileTimeString() + ")");
		System.out.println("(" + License.getCurrent() + " source distribution)");
		final Properties p = System.getProperties();
		System.out.println(p.getProperty("java.runtime.name"));
		System.out.println(p.getProperty("java.vm.name"));
		System.out.println(p.getProperty("java.runtime.version"));
		System.out.println(p.getProperty("os.name"));
		System.out.println();
		for (String s : GraphvizUtils.getTestDotStrings(false)) {
			System.out.println(s);
		}
		exit();
	}

	public static void checkVersion() throws InterruptedException {
		System.out
				.println("PlantUML version " + Version.versionString() + " (" + Version.compileTimeString() + ")");
		System.out.println();
		final int lastversion = PSystemVersion.extractDownloadableVersion(null, null);
		if (lastversion == -1) {
			System.out.println("Error");
			System.out.println("Cannot connect to http://plantuml.com/");
			System.out.println("Maybe you should set your proxy ?");
		} else if (lastversion == 0) {
			System.out.println("Error");
			System.out.println("Cannot retrieve last version from http://plantuml.com/");
		} else {
			System.out.println("Last available version for download : " + lastversion);
			System.out.println();
			if (Version.version() >= lastversion) {
				System.out.println("Your version is up to date.");
			} else {
				System.out.println("A newer version is available for download.");
			}
		}

		exit();
	}

	public static void printAbout() throws InterruptedException {
		// Duplicate in PSystemVersion
		System.out
				.println("PlantUML version " + Version.versionString() + " (" + Version.compileTimeString() + ")");
		System.out.println();
		System.out.println("Original idea: Arnaud Roques");
		System.out.println("Word Macro: Alain Bertucat & Matthieu Sabatier");
		System.out.println("Word Add-in: Adriaan van den Brand");
		System.out.println("Eclipse Plugin: Claude Durif & Anne Pecoil");
		System.out.println("Servlet & XWiki: Maxime Sinclair");
		System.out.println("Site design: Raphael Cotisson");
		System.out.println("Logo: Benjamin Croizet");
		System.out.println();
		System.out.println("http://plantuml.sourceforge.net");
		exit();
	}

	public static void printLanguage() throws InterruptedException {
		new LanguageDescriptor().print(System.out);
		exit();
	}

}
