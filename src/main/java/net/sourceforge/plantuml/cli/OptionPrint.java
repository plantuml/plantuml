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
package net.sourceforge.plantuml.cli;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.plantuml.crash.ReportLog;
import net.sourceforge.plantuml.dot.GraphvizUtils;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.syntax.LanguageDescriptor;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.version.Version;

public class OptionPrint {
	// ::remove file when __CORE__ or __TEAVM__
	// ::remove file when __HAXE__

	static public void printCheckGraphviz() {
		final ReportLog result = new ReportLog();
		final int errorCode = GraphvizUtils.addDotStatus(result, false);
		for (String s : result)
			if (errorCode == 0)
				System.out.println(s);
			else
				System.err.println(s);

		Exit.exit(errorCode);
	}


	public static void printLicense() {
		for (String s : License.getCurrent().getTextFull())
			System.out.println(s);
	}

	public static void printVersion() {
		System.out.println(Version.fullDescription());
		System.out.println("(" + License.getCurrent() + " source distribution)");
		for (String v : interestingProperties())
			System.out.println(v);

		for (String v : interestingValues())
			System.out.println(v);

		System.out.println();
		final ReportLog result = new ReportLog();
		final int errorCode = GraphvizUtils.addDotStatus(result, false);
		for (String s : result)
			System.out.println(s);

		Exit.exit(errorCode);
	}

	public static Collection<String> interestingProperties() {
		final Properties p = System.getProperties();
//		final List<String> list1 = Arrays.asList("java.runtime.name", "Java Runtime", "java.vm.name", "JVM",
//				"java.runtime.version", "Java Version", "os.name", "Operating System", "file.encoding",
//				"Default Encoding", "user.language", "Language", "user.country", "Country");
//		final List<String> list2 = Arrays.asList("java.runtime.name", "Java Runtime", "java.vm.name", "JVM",
//				"java.runtime.version", "Java Version", "os.name", "Operating System", /* "os.version", "OS Version", */
//				"file.encoding", "Default Encoding", "user.language", "Language", "user.country", "Country");
//		final List<String> all = withIp() ? list1 : list2;
		final List<String> all;
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE) {
			all = Arrays.asList("java.runtime.name", "Java Runtime", "java.vm.name", "JVM", "java.runtime.version",
					"Java Version", "os.name", "Operating System", "os.version", "OS Version", "file.encoding",
					"Default Encoding", "user.language", "Language", "user.country", "Country");
		} else {
			all = Arrays.asList("java.runtime.name", "Java Runtime", "java.vm.name", "JVM", "file.encoding",
					"Default Encoding", "user.language", "Language", "user.country", "Country");
		}
		final List<String> result = new ArrayList<>();
		for (int i = 0; i < all.size(); i += 2) {
			result.add(all.get(i + 1) + ": " + p.getProperty(all.get(i)));
		}
		return result;
	}

	public static Collection<String> interestingValues() {
		final List<String> strings = new ArrayList<>();
//		if (withIp() == false) {
//			strings.add("Machine: " + getHostName());
//		}
//		strings.add(" ");
//		strings.add("Current Security Profile: " + SecurityUtils.getSecurityProfile());
//		strings.add(SecurityUtils.getSecurityProfile().longDescription());
		strings.add(" ");
		strings.add("PLANTUML_LIMIT_SIZE: " + GraphvizUtils.getenvImageLimit());
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE) {
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
		}
		return Collections.unmodifiableCollection(strings);
	}

//	private static boolean withIp() {
//		return getHostName().startsWith("ip-");
//	}

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

	public static void printAbout() {
		for (String s : PSystemVersion.getAuthorsStrings(false))
			System.out.println(s);

	}

	public static void printListKeywords() {
		new LanguageDescriptor().print(System.out);
	}

}
