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
package net.sourceforge.plantuml.security;

/**
 * There are 4 different security profile defined.
 * <p>
 * The security profile to be used is set at the launch of PlantUML and cannot
 * be changed by users. The security profile defines what an instance of
 * PlantUML is allowed to do:<br>
 * - access some local file <br>
 * - connection to some remote URL <br>
 * - print some technical information to the users.
 * <p>
 * <p>
 * The security profile is defined: <br>
 * - either by an environment variable<br>
 * - or an option at command line
 * <p>
 * There is also a default value, which is LEGACY in this current
 * implementation.
 * 
 */
public enum SecurityProfile {
	// ::remove folder when __HAXE__

	/**
	 * Running in SANDBOX mode is completely secure. No local file can be read
	 * (except dot executable) No remote URL access can be used No technical
	 * information are print to users.
	 * <p>
	 * This mode is defined for test and debug, since it's not very useful for
	 * users. However, you can use it if you need to.
	 */
	SANDBOX,

	/**
	 * 
	 */
	ALLOWLIST,

	/**
	 * This mode is designed for PlantUML running in a web server.
	 * 
	 */
	INTERNET,

	/**
	 * This mode reproduce old PlantUML version behaviour.
	 * <p>
	 * Right now, this is the default Security Profile but this will be removed from
	 * future version because it is now full secure, especially on Internet server.
	 */
	LEGACY,

	/**
	 * Running in UNSECURE mode means that PlantUML can access to any local file and
	 * can connect to any URL.
	 * <p>
	 * Some technical information (file path, Java version) are also printed in some
	 * error messages. This is not an issue if you are running PlantUML locally. But
	 * you should not use this mode if PlantUML is running on some server,
	 * especially if the server is accessible from Internet.
	 */
	UNSECURE;

	/**
	 * Initialize the default value.
	 * <p>
	 * It search in some config variable if the user has defined a some default
	 * value.
	 * 
	 * @return the value
	 */
	static SecurityProfile init() {
		// ::comment when __CORE__
		final String env = SecurityUtils.getenv("PLANTUML_SECURITY_PROFILE");
		if ("SANDBOX".equalsIgnoreCase(env))
			return SANDBOX;
		else if ("ALLOWLIST".equalsIgnoreCase(env))
			return ALLOWLIST;
		else if ("INTERNET".equalsIgnoreCase(env))
			return INTERNET;
		else if ("UNSECURE".equalsIgnoreCase(env))
			// ::done
			return UNSECURE;
		// ::comment when __CORE__

		return LEGACY;
		// ::done
	}

	/**
	 * A Human understandable description.
	 */
	public String longDescription() {
		switch (this) {
		case SANDBOX:
			return "This is completely safe: no access to local files or to distant URL.";
		case ALLOWLIST:
			return "Some local resource may be accessible.";
		case INTERNET:
			return "<i>Mode designed for server connected to Internet.";
		case LEGACY:
			return "<b>Warning: this mode will be removed in future version";
		case UNSECURE:
			return "<b>Make sure that this server is not accessible from Internet";
		}
		return "<i>This is completely safe: no access on local files or on distant URL.";
	}

	/**
	 * Retrieve the timeout for URL.
	 */
	public long getTimeout() {
		switch (this) {
		case SANDBOX:
			return 1000L;
		case ALLOWLIST:
			return 1000L * 60 * 5;
		case INTERNET:
			return 1000L * 10;
		case LEGACY:
			return 1000L * 60;
		case UNSECURE:
			return 1000L * 60 * 5;
		}
		throw new AssertionError();
	}

	public boolean canWeReadThisEnvironmentVariable(String name) {
		if (name == null)
			return false;

		final String lname = name.toLowerCase();
		if (lname.startsWith("plantuml.security"))
			return false;

		if (lname.startsWith("plantuml"))
			return true;

		if (lname.equals("path.separator") || lname.equals("line.separator"))
			return true;

		return this == UNSECURE;
	}

}
