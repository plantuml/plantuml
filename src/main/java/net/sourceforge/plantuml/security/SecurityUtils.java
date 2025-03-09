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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.authentication.SecurityAccessInterceptor;
import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.security.authentication.SecurityAuthorizeManager;
import net.sourceforge.plantuml.security.authentication.SecurityCredentials;
import net.sourceforge.plantuml.security.authentication.SecurityDefaultNoopAccessInterceptor;
import net.sourceforge.plantuml.security.authentication.SecurityDefaultNoopAuthorizeManager;
import net.sourceforge.plantuml.security.authentication.basicauth.BasicAuthAccessInterceptor;
import net.sourceforge.plantuml.security.authentication.basicauth.BasicAuthAuthorizeManager;
import net.sourceforge.plantuml.security.authentication.oauth.OAuth2AccessInterceptor;
import net.sourceforge.plantuml.security.authentication.oauth.OAuth2ClientAccessAuthorizeManager;
import net.sourceforge.plantuml.security.authentication.oauth.OAuth2ResourceOwnerAccessAuthorizeManager;
import net.sourceforge.plantuml.security.authentication.token.TokenAuthAccessInterceptor;
import net.sourceforge.plantuml.security.authentication.token.TokenAuthAuthorizeManager;
import net.sourceforge.plantuml.utils.Log;

public class SecurityUtils {

	// ::uncomment when __CORE__
//	public static SecurityProfile getSecurityProfile() {
//		return SecurityProfile.UNSECURE;
//	}
	// ::done

	public static boolean ignoreThisLink(String url) {
		// ::comment when __CORE__
		if (allowJavascriptInLink() == false && isJavascriptLink(url))
			return true;
		// ::done
		return false;
	}

	/**
	 * Indicates, that we have no authentication and credentials to access the URL.
	 */
	public static final String NO_CREDENTIALS = "<none>";

	public synchronized static BufferedImage readRasterImage(final ImageIcon imageIcon) {
		final Image tmpImage = imageIcon.getImage();
		if (imageIcon.getIconWidth() == -1)
			return null;

		final BufferedImage image = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().drawImage(tmpImage, 0, 0, null);
		tmpImage.flush();
		return image;
	}

	// ::comment when __CORE__
	/**
	 * Java class paths to import files from.
	 */
	public static final String PATHS_CLASSES = "java.class.path";

	/**
	 * Paths to include files.
	 */
	public static final String PATHS_INCLUDES = "plantuml.include.path";

	/**
	 * Whitelist of paths from where scripts can load data.
	 */
	public static final String ALLOWLIST_LOCAL_PATHS = "plantuml.allowlist.path";

	/**
	 * Whitelist of urls
	 */
	public static final String ALLOWLIST_URL = "plantuml.allowlist.url";

	/**
	 * Paths to folders with security specific content (not allowed to read via
	 * SFile).
	 */
	public static final String PATHS_SECURITY = "plantuml.security.credentials.path";

	public static final String SECURITY_ALLOW_NONSSL_AUTH = "plantuml.security.allowNonSSLAuth";

	/**
	 * Standard BasicAuth authentication interceptor, to generate a
	 * SecurityAuthentication from credentials.
	 */
	private static final SecurityAuthorizeManager PUBLIC_AUTH_MANAGER = new SecurityDefaultNoopAuthorizeManager();

	/**
	 * Standard interceptor for public endpoint access.
	 */
	private static final SecurityAccessInterceptor PUBLIC_ACCESS_INTERCEPTOR = new SecurityDefaultNoopAccessInterceptor();

	/**
	 * Standard TokenAuth authorize manager, to generate a SecurityAuthentication
	 * from credentials.
	 */
	private static final SecurityAuthorizeManager TOKEN_AUTH_MANAGER = new TokenAuthAuthorizeManager();

	/**
	 * Standard token access interceptor.
	 */
	private static final SecurityAccessInterceptor TOKEN_ACCESS_INTERCEPTOR = new TokenAuthAccessInterceptor();

	/**
	 * Standard BasicAuth authorize manager, to generate a SecurityAuthentication
	 * from credentials.
	 */
	private static final SecurityAuthorizeManager BASICAUTH_AUTH_MANAGER = new BasicAuthAuthorizeManager();

	/**
	 * Standard BasicAuth access interceptor.
	 */
	private static final SecurityAccessInterceptor BASICAUTH_ACCESS_INTERCEPTOR = new BasicAuthAccessInterceptor();

	/**
	 * OAuth2 client credentials authorization manager.
	 */
	private static final SecurityAuthorizeManager OAUTH2_CLIENT_AUTH_MANAGER = new OAuth2ClientAccessAuthorizeManager();

	/**
	 * OAuth2 resource owner authorization manager.
	 */
	private static final SecurityAuthorizeManager OAUTH2_RESOURCEOWNER_AUTH_MANAGER = new OAuth2ResourceOwnerAccessAuthorizeManager();

	/**
	 * Standard 'bearer' OAuth2 access interceptor.
	 */
	private static final SecurityAccessInterceptor OAUTH2_ACCESS_INTERCEPTOR = new OAuth2AccessInterceptor();

	/**
	 * Filesystem-save characters.
	 */
	private static final Pattern SECURE_CHARS = Pattern.compile("^[a-zA-Z0-9\\-]+$");

	static private SecurityProfile current = null;

	public static synchronized SecurityProfile getSecurityProfile() {
		if (current == null)
			current = SecurityProfile.init();

		return current;
	}

	private static boolean isJavascriptLink(String url) {
		return url.toLowerCase().replaceAll("[^a-z]", "").startsWith("javascript");
	}

	private static boolean allowJavascriptInLink() {
		final String env = getenv("PLANTUML_ALLOW_JAVASCRIPT_IN_LINK");
		return "true".equalsIgnoreCase(env);
	}

	public static String getenv(String name) {
		String result = System.getProperty(name);
		if (StringUtils.isNotEmpty(result))
			return result;

		result = System.getenv(name);
		if (StringUtils.isNotEmpty(result))
			return result;

		final String alternateName = name.replace(".", "_").toUpperCase();
		return System.getenv(alternateName);
	}

//	/**
//	 * Checks the environment variable and returns true if the variable is used in
//	 * security context. In this case, the value should not be displayed in scripts.
//	 *
//	 * @param name Environment variable to check
//	 * @return true, if this is a secret variable
//	 */
//	public static boolean isSecurityEnv(String name) {
//		return name != null && name.toLowerCase().startsWith("plantuml.security.");
//	}

	/**
	 * Configuration for Non-SSL authentication methods.
	 *
	 * @return true, if plantUML should allow authentication in plain connections
	 *         (without encryption).
	 * @see #SECURITY_ALLOW_NONSSL_AUTH
	 */
	public static boolean isNonSSLAuthenticationAllowed() {
		return Boolean.parseBoolean(getenv(SECURITY_ALLOW_NONSSL_AUTH));
	}

	public static List<SFile> getPath(String prop) {
		final List<SFile> result = new ArrayList<>();
		String paths = getenv(prop);
		if (paths == null) {
			return Collections.unmodifiableList(result);
		}
		paths = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(paths);
		final StringTokenizer st = new StringTokenizer(paths, System.getProperty("path.separator"));
		while (st.hasMoreTokens()) {
			final String tmp = st.nextToken();
			try {
				final SFile f = new SFile(tmp).getCanonicalFile();
				if (f.isDirectory())
					result.add(f);
			} catch (IOException e) {
				Log.info("Cannot access to " + tmp + ". " + e);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public static boolean allowSvgText() {
		return true;
	}

	public static java.io.PrintWriter createPrintWriter(OutputStream os) {
		return new PrintWriter(os);
	}

	public static java.io.PrintWriter createPrintWriter(OutputStream os, boolean append) {
		return new PrintWriter(os, append);
	}

	public static PrintStream createPrintStream(OutputStream os) {
		return new PrintStream(os);
	}

	public static PrintStream createPrintStream(OutputStream os, boolean autoFlush, String charset)
			throws UnsupportedEncodingException {
		return new PrintStream(os, autoFlush, charset);
	}

	public static PrintStream createPrintStream(OutputStream os, boolean autoFlush, Charset charset)
			throws UnsupportedEncodingException {
		return new PrintStream(os, autoFlush, charset.name());
	}

	// ----
	public static FileReader createFileReader(String path) throws FileNotFoundException {
		return new FileReader(path);
	}

	public static java.io.PrintWriter createPrintWriter(String path) throws FileNotFoundException {
		return new PrintWriter(path);
	}

	public static FileOutputStream createFileOutputStream(String path) throws FileNotFoundException {
		return new FileOutputStream(path);
	}

	/**
	 * Returns the authorize-manager for a security credentials configuration.
	 *
	 * @param credentialConfiguration the credentials
	 * @return the manager.
	 */
	public static SecurityAuthorizeManager getAuthenticationManager(SecurityCredentials credentialConfiguration) {
		if (credentialConfiguration == SecurityCredentials.NONE) {
			return PUBLIC_AUTH_MANAGER;
		} else if ("tokenauth".equalsIgnoreCase(credentialConfiguration.getType())) {
			return TOKEN_AUTH_MANAGER;
		} else if ("basicauth".equalsIgnoreCase(credentialConfiguration.getType())) {
			return BASICAUTH_AUTH_MANAGER;
		} else if ("oauth2".equalsIgnoreCase(credentialConfiguration.getType())) {
			String grantType = credentialConfiguration.getPropertyStr("grantType");
			if ("client_credentials".equalsIgnoreCase(grantType)) {
				return OAUTH2_CLIENT_AUTH_MANAGER;
			} else if ("password".equalsIgnoreCase(grantType)) {
				return OAUTH2_RESOURCEOWNER_AUTH_MANAGER;
			}
		}
		return PUBLIC_AUTH_MANAGER;
	}

	/**
	 * Returns the authentication interceptor for a {@link SecurityAuthentication}.
	 *
	 * @param authentication the authentication data
	 * @return the interceptor.
	 */
	public static SecurityAccessInterceptor getAccessInterceptor(SecurityAuthentication authentication) {
		if (authentication != null) {
			String type = authentication.getType();
			if ("public".equals(type)) {
				return PUBLIC_ACCESS_INTERCEPTOR;
			} else if ("tokenauth".equalsIgnoreCase(type)) {
				return TOKEN_ACCESS_INTERCEPTOR;
			} else if ("basicauth".equalsIgnoreCase(type)) {
				return BASICAUTH_ACCESS_INTERCEPTOR;
			} else if ("oauth2".equalsIgnoreCase(type)) {
				return OAUTH2_ACCESS_INTERCEPTOR;
			}
		}
		// Unknown? Fall back to public:
		return PUBLIC_ACCESS_INTERCEPTOR;
	}

	/**
	 * Checks if user credentials existing.
	 *
	 * @param userToken name of the credential file
	 * @return boolean, if exists
	 */
	public static boolean existsSecurityCredentials(String userToken) {
		SFile securityPath = getSecurityPath();
		if (securityPath != null) {
			// SFile does not allow access to the security path (to hide the credentials in
			// DSL scripts)
			File securityFilePath = securityPath.conv();
			File userCredentials = new File(securityFilePath, userToken + ".credential");
			return userCredentials.exists() && userCredentials.canRead() && !userCredentials.isDirectory()
					&& userCredentials.length() > 2;

		}
		return false;
	}

	/**
	 * Loads the user credentials from the file system.
	 *
	 * @param userToken name of the credential file
	 * @return the credentials or NONE
	 */
	public static SecurityCredentials loadSecurityCredentials(String userToken) {
		if (userToken != null && checkFileSystemSaveCharactersStrict(userToken) && !NO_CREDENTIALS.equals(userToken)) {
			final SFile securityPath = getSecurityPath();
			if (securityPath != null) {
				// SFile does not allow access to the security path (to hide the credentials in
				// DSL scripts)
				File securityFilePath = securityPath.conv();
				File userCredentials = new File(securityFilePath, userToken + ".credential");
				JsonValue jsonValue = loadJson(userCredentials);
				return SecurityCredentials.fromJson(jsonValue);
			}
		}
		return SecurityCredentials.NONE;
	}

	/**
	 * Checks, if the token of a pathname (filename, ext, directory-name) uses only
	 * a very strict set of characters and not longer than 64 characters.
	 * <p>
	 * Only characters from a to Z, Numbers and - are allowed.
	 *
	 * @param pathNameToken filename, ext, directory-name
	 * @return true, if the string fits to the strict allow-list of characters
	 * @see #SECURE_CHARS
	 */
	private static boolean checkFileSystemSaveCharactersStrict(String pathNameToken) {
		return StringUtils.isNotEmpty(pathNameToken) && SECURE_CHARS.matcher(pathNameToken).matches()
				&& pathNameToken.length() <= 64;
	}

	/**
	 * Loads the path to the configured security folder, if existing.
	 * <p>
	 * Please note: A SFile referenced to a security folder cannot access the files.
	 * The content of the files in the security path should never have passed to DSL
	 * scripts.
	 *
	 * @return SFile folder or null
	 */
	public static SFile getSecurityPath() {
		List<SFile> paths = getPath(PATHS_SECURITY);
		if (!paths.isEmpty()) {
			SFile secureSFile = paths.get(0);
			File securityFolder = secureSFile.conv();
			if (securityFolder.exists() && securityFolder.isDirectory()) {
				return secureSFile;
			}
		}
		return null;
	}

	/**
	 * Loads a file as JSON object. If no file exists or the file is not parsable,
	 * the method returns an empty JSON.
	 *
	 * @param jsonFile file path to the JSON file
	 * @return a Json vale (maybe empty)
	 */
	private static JsonValue loadJson(File jsonFile) {
		if (jsonFile.exists() && jsonFile.canRead() && jsonFile.length() > 2) {
			// we have a file with at least two bytes and readable, hopefully it's a JSON
			try (Reader r = new BufferedReader(new FileReader(jsonFile))) {
				return Json.parse(r);
			} catch (IOException e) {
				Logme.error(e);
			}
		}
		return Json.object();
	}
	// ::done

}