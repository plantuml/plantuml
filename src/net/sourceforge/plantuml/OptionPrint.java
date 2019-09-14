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

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.syntax.LanguageDescriptor;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.version.Version;

public class OptionPrint {

	static public void printTestDot() throws InterruptedException {
		final List<String> result = new ArrayList<String>();
		final int errorCode = GraphvizUtils.addDotStatus(result, false);
		for (String s : result) {
			if (errorCode == 0) {
				System.out.println(s);
			} else {
				System.err.println(s);
			}
		}
		exit(errorCode);
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
		// System.out.println("    -config \"file\"\tTo read the provided config file before each diagram");
		final char separator = File.separatorChar;
		System.out.println("    -I" + separator + "path" + separator + "to" + separator
				+ "file\tTo include file as if '!include file' were used");
		System.out.println("    -I" + separator + "path" + separator + "to" + separator
				+ "*.puml\tTo include files with pattern");
		System.out.println("    -charset xxx\tTo use a specific charset (default is " + charset + ")");
		System.out.println("    -e[x]clude pattern\tTo exclude files that match the provided pattern");
		System.out.println("    -metadata\t\tTo retrieve PlantUML sources from PNG images");
		System.out.println("    -nometadata\t\tTo NOT export metadata in PNG/SVG generated files");
		System.out.println("    -checkmetadata\t\tSkip PNG files that don't need to be regenerated");
		System.out.println("    -version\t\tTo display information about PlantUML and Java versions");
		System.out.println("    -checkversion\tTo check if a newer version is available for download");
		System.out.println("    -v[erbose]\t\tTo have log information");
		System.out.println("    -quiet\t\tTo NOT print error message into the console");
		System.out.println("    -debugsvek\t\tTo generate intermediate svek files");
		System.out.println("    -h[elp]\t\tTo display this help message");
		System.out.println("    -testdot\t\tTo test the installation of graphviz");
		System.out.println("    -graphvizdot \"exe\"\tTo specify dot executable");
		System.out.println("    -p[ipe]\t\tTo use stdin for PlantUML source and stdout for PNG/SVG/EPS generation");
		System.out
				.println("    -encodesprite 4|8|16[z] \"file\"\tTo encode a sprite at gray level (z for compression) from an image");
		System.out.println("    -computeurl|-encodeurl\tTo compute the encoded URL of a PlantUML source file");
		System.out.println("    -decodeurl\t\tTo retrieve the PlantUML source from an encoded URL");
		System.out.println("    -syntax\t\tTo report any syntax error from standard input without generating images");
		System.out.println("    -language\t\tTo print the list of PlantUML keywords");
		// System.out.println("    -nosuggestengine\tTo disable the suggest engine when errors in diagrams");
		System.out.println("    -checkonly\t\tTo check the syntax of files without generating images");
		System.out.println("    -failfast\t\tTo stop processing as soon as a syntax error in diagram occurs");
		System.out.println("    -failfast2\t\tTo do a first syntax check before processing files, to fail even faster");
		System.out.println("    -pattern\t\tTo print the list of Regular Expression used by PlantUML");
		System.out.println("    -duration\t\tTo print the duration of complete diagrams processing");
		System.out.println("    -nbthread N\t\tTo use (N) threads for processing");
		System.out.println("    -nbthread auto\tTo use " + Option.defaultNbThreads() + " threads for processing");
		System.out
				.println("    -timeout N\t\tProcessing timeout in (N) seconds. Defaults to 15 minutes (900 seconds).");
		System.out.println("    -author[s]\t\tTo print information about PlantUML authors");
		System.out.println("    -overwrite\t\tTo allow to overwrite read only files");
		System.out.println("    -printfonts\t\tTo print fonts available on your system");
		System.out.println("    -enablestats\tTo enable statistics computation");
		System.out.println("    -disablestats\tTo disable statistics computation (default)");
		System.out.println("    -htmlstats\t\tTo output general statistics in file plantuml-stats.html");
		System.out.println("    -xmlstats\t\tTo output general statistics in file plantuml-stats.xml");
		System.out.println("    -realtimestats\tTo generate statistics on the fly rather than at the end");
		System.out.println("    -loopstats\t\tTo continuously print statistics about usage");
		System.out.println("    -splash\t\tTo display a splash screen with some progress bar");
		System.out.println("    -progress\t\tTo display a textual progress bar in console");
		System.out.println("    -pipeimageindex N\tTo generate the Nth image with pipe option");
		System.out.println("    -stdlib\t\tTo print standard library info");
		System.out.println("    -extractstdlib\tTo extract PlantUML Standard Library into stdlib folder");
		System.out.println("    -filename \"example.puml\"\tTo override %filename% variable");
		System.out.println("    -preproc\t\tTo output preprocessor text of diagrams");
		System.out.println("    -cypher\t\tTo cypher texts of diagrams so that you can share them");
		System.out.println();
		System.out.println("If needed, you can setup the environment variable GRAPHVIZ_DOT.");
		exit(0);
	}

	static private void exit(int errorCode) throws InterruptedException {
		if (OptionFlags.getInstance().isSystemExit() || errorCode != 0) {
			System.exit(errorCode);
		}
		throw new InterruptedException("exit");
	}

	public static void printLicense() throws InterruptedException {
		for (String s : License.getCurrent().getTextFull()) {
			System.out.println(s);
		}
		exit(0);
	}

	public static void printVersion() throws InterruptedException {
		System.out.println(Version.fullDescription());
		System.out.println("(" + License.getCurrent() + " source distribution)");
		for (String v : interestingProperties()) {
			System.out.println(v);
		}
		for (String v : interestingValues()) {
			System.out.println(v);
		}
		System.out.println();
		final List<String> result = new ArrayList<String>();
		final int errorCode = GraphvizUtils.addDotStatus(result, false);
		for (String s : result) {
			System.out.println(s);
		}
		exit(errorCode);
	}

	public static Collection<String> interestingProperties() {
		final Properties p = System.getProperties();
		final List<String> list1 = Arrays.asList("java.runtime.name", "Java Runtime", "java.vm.name", "JVM",
				"java.runtime.version", "Java Version", "os.name", "Operating System", "file.encoding",
				"Default Encoding", "user.language", "Language", "user.country", "Country");
		final List<String> list2 = Arrays.asList("java.runtime.name", "Java Runtime", "java.vm.name", "JVM",
				"java.runtime.version", "Java Version", "os.name", "Operating System", /* "os.version", "OS Version", */
				"file.encoding", "Default Encoding", "user.language", "Language", "user.country", "Country");
		final List<String> all = withIp() ? list1 : list2;
		final List<String> result = new ArrayList<String>();
		for (int i = 0; i < all.size(); i += 2) {
			result.add(all.get(i + 1) + ": " + p.getProperty(all.get(i)));
		}
		return result;
	}

	public static Collection<String> interestingValues() {
		final List<String> strings = new ArrayList<String>();
		if (withIp() == false) {
			strings.add("Machine: " + getHostName());
		}
		strings.add("PLANTUML_LIMIT_SIZE: " + GraphvizUtils.getenvImageLimit());
		strings.add("Processors: " + Runtime.getRuntime().availableProcessors());
		final long freeMemory = Runtime.getRuntime().freeMemory();
		final long maxMemory = Runtime.getRuntime().maxMemory();
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final long usedMemory = totalMemory - freeMemory;
		final int threadActiveCount = Thread.activeCount();
		strings.add("Max Memory: " + format(maxMemory));
		strings.add("Total Memory: " + format(totalMemory));
		strings.add("Free Memory: " + format(freeMemory));
		strings.add("Used Memory: " + format(usedMemory));
		strings.add("Thread Active Count: " + threadActiveCount);
		return Collections.unmodifiableCollection(strings);
	}

	private static boolean withIp() {
		return getHostName().startsWith("ip-");
	}

	private static String hostname;

	public static synchronized String getHostName() {
		if (hostname == null) {
			hostname = getHostNameSlow();
		}
		return hostname;
	}

	private static String getHostNameSlow() {
		try {
			final InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (Throwable e) {
			final Map<String, String> env = System.getenv();
			if (env.containsKey("COMPUTERNAME")) {
				return env.get("COMPUTERNAME");
			} else if (env.containsKey("HOSTNAME")) {
				return env.get("HOSTNAME");
			}
		}
		return "Unknown Computer";
	}

	private static String format(final long value) {
		return String.format(Locale.US, "%,d", value);
	}

	public static void checkVersion() throws InterruptedException {
		System.out.println(Version.fullDescription());
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

		exit(0);
	}

	public static void printAbout() throws InterruptedException {
		for (String s : PSystemVersion.getAuthorsStrings(false)) {
			System.out.println(s);
		}
		exit(0);
	}

	public static void printLanguage() throws InterruptedException {
		new LanguageDescriptor().print(System.out);
		exit(0);
	}

}
