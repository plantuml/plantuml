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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.ImageIcon;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.authentication.SecurityAccessInterceptor;
import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.security.authentication.SecurityCredentials;
//::uncomment when __CORE__
//import net.sourceforge.plantuml.FileUtils;
//::done

/**
 * Secure replacement for java.net.URL.
 * <p>
 * This class should be used instead of java.net.URL.
 * <p>
 * This class does some control access and manages access-tokens via URL. If a
 * URL contains a access-token, similar to a user prefix, SURL loads the
 * authorization config for this user-token and passes the credentials to the
 * host.
 * <p>
 * Example:<br/>
 *
 * <pre>
 *     SURL url = SURL.create ("https://jenkins-access@jenkins.mycompany.com/api/json")
 * </pre>
 *
 * The {@code jenkins-access} will checked against the Security context access
 * token configuration. If a configuration exists for this token name, the token
 * will be removed from the URL and the credentials will be added to the
 * headers. If the token is not found, the URL remains as it is and no separate
 * authentication will be performed.
 * <p>
 * TODO: Some methods should be moved to a HttpClient implementation, because
 * SURL is not the valid class to manage it. <br/>
 * TODO: BAD_HOSTS implementation should be reviewed and moved to HttpClient
 * implementation with a circuit-breaker. <br/>
 * TODO: Token expiration with refresh should be implemented in future. <br/>
 */
public class SURL {

	/**
	 * Indicates, that we have no authentication to access the URL.
	 */
	public static final String WITHOUT_AUTHENTICATION = SecurityUtils.NO_CREDENTIALS;

	/**
	 * Internal URL, maybe cleaned from user-token.
	 */
	private final URL internal;

	/**
	 * Assigned credentials to this URL.
	 */
	private final String securityIdentifier;

	private SURL(URL url, String securityIdentifier) {
		this.internal = Objects.requireNonNull(url);
		this.securityIdentifier = Objects.requireNonNull(securityIdentifier);
	}

	/**
	 * Create a secure URL from a String.
	 * <p>
	 * The url must be http or https. Return null in case of error or if
	 * <code>url</code> is null
	 *
	 * @param url plain url starting by http:// or https//
	 * @return the secure URL or null
	 */
	public static SURL create(String url) {
		if (url == null)
			return null;

		if (url.startsWith("http://") || url.startsWith("https://"))
			try {
				return create(new URI(url).toURL());
			} catch (MalformedURLException | URISyntaxException e) {
				Logme.error(e);
			}
		return null;
	}

	/**
	 * Create a secure URL from a <code>java.net.URL</code> object.
	 * <p>
	 * It takes into account credentials.
	 *
	 * @param url
	 * @return the secure URL
	 * @throws MalformedURLException if <code>url</code> is null
	 * @throws URISyntaxException
	 */
	public static SURL create(URL url) throws MalformedURLException, URISyntaxException {
		if (url == null)
			throw new MalformedURLException("URL cannot be null");

		// ::comment when __CORE__
		final String credentialId = url.getUserInfo();

		if (credentialId == null || credentialId.indexOf(':') > 0)
			// No user info at all, or a user with password (This is a legacy BasicAuth
			// access, and we bypass it):
			return new SURL(url, WITHOUT_AUTHENTICATION);
		else if (SecurityUtils.existsSecurityCredentials(credentialId))
			// Given userInfo, but without a password. We try to find SecurityCredentials
			return new SURL(removeUserInfo(url), credentialId);
		else
			// ::done
			return new SURL(url, WITHOUT_AUTHENTICATION);
	}

	// ::uncomment when __CORE__
//	public InputStream openStream() {
//	try {
//		return internal.openStream();
//	} catch (IOException e) {
//		System.err.println("SURL::openStream " + e);
//		return null;
//	}
//}
//public byte[] getBytes() {
//	final InputStream is = openStream();
//	if (is != null)
//		try {
//			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			FileUtils.copyInternal(is, baos, true);
//			return baos.toByteArray();
//		} catch (IOException e) {
//			System.err.println("SURL::getBytes " + e);
//		}
//	return null;
//}
	// ::done

	public BufferedImage readRasterImageFromURL() {
		if (isUrlOk())
			try {
				final byte[] bytes = getBytes();
				if (bytes == null || bytes.length == 0)
					return null;
				final ImageIcon tmp = new ImageIcon(bytes);
				return SecurityUtils.readRasterImage(tmp);
			} catch (Exception e) {
				Logme.error(e);
			}
		return null;
	}

	/**
	 * Check SecurityProfile to see if this URL can be opened.
	 */
	public boolean isUrlOk() {
		// ::comment when __CORE__
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.SANDBOX)
			// In SANDBOX, we cannot read any URL
			return false;

		if (isInUrlAllowList())
			// ::done
			return true;
		// ::comment when __CORE__

		if (SecurityUtils.getSecurityProfile() == SecurityProfile.LEGACY) {
			if (URLCheck.isURLforbidden(cleanPath(internal.toString())))
				return false;
			return true;
		}

		if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE)
			// We are UNSECURE anyway
			return true;

		if (SecurityUtils.getSecurityProfile() == SecurityProfile.INTERNET) {
			if (URLCheck.isURLforbidden(cleanPath(internal.toString())))
				return false;

			final int port = internal.getPort();
			// Using INTERNET profile, port 80 and 443 are ok
			return port == 80 || port == 443 || port == -1;
		}
		return false;
		// ::done
	}

	// ::comment when __CORE__
	/**
	 * Regex to remove the UserInfo part from a URL.
	 */
	private static final Pattern PATTERN_USERINFO = Pattern.compile("(^https?://)([-_0-9a-zA-Z]+@)([^@]*)$");

	private static final ExecutorService EXE = Executors.newCachedThreadPool(new ThreadFactory() {
		public Thread newThread(Runnable r) {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		}
	});

	private static final Map<String, Long> BAD_HOSTS = new ConcurrentHashMap<String, Long>();

	/**
	 * Creates a URL without UserInfo part and without SecurityCredentials.
	 *
	 * @param url plain URL
	 * @return SURL without any user credential information.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	static SURL createWithoutUser(URL url) throws MalformedURLException, URISyntaxException {
		return new SURL(removeUserInfo(url), WITHOUT_AUTHENTICATION);
	}

	/**
	 * Clears the bad hosts cache.
	 * <p>
	 * In some test cases (and maybe also needed for other functionality) the bad
	 * hosts cache must be cleared.<br/>
	 * E.g., in a test we check the failure on missing credentials and then a test
	 * with existing credentials. With a bad host cache the second test will fail,
	 * or we have unpredicted results.
	 */
	static void resetBadHosts() {
		BAD_HOSTS.clear();
	}

	@Override
	public String toString() {
		return internal.toString();
	}

	private boolean isInUrlAllowList() {
		final String full = cleanPath(internal.toString());
		// Thanks to Agasthya Kasturi
		if (full.contains("@"))
			return false;
		for (String allow : getUrlAllowList())
			if (full.startsWith(cleanPath(allow)))
				return true;

		return false;
	}

	private String cleanPath(String path) {
		// Remove user information, because we don't like to store user/password or
		// userTokens in allow-list
		path = removeUserInfoFromUrlPath(path);
		path = path.trim().toLowerCase(Locale.US);
		// We simplify/normalize the url, removing default ports
		path = path.replace(":80/", "");
		path = path.replace(":443/", "");
		return path;
	}

	private List<String> getUrlAllowList() {
		final String env = SecurityUtils.getenv(SecurityUtils.ALLOWLIST_URL);
		if (env == null)
			return Collections.emptyList();

		return Arrays.asList(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(env).split(";"));
	}

	/**
	 * Reads from an endpoint (with configured credentials and proxy) the response
	 * as blob.
	 * <p>
	 * This method allows access to an endpoint, with a configured
	 * SecurityCredentials object. The credentials will load on the fly and
	 * authentication fetched from an authentication-manager. Caching of tokens is
	 * not supported.
	 * <p>
	 * authors: Alain Corbiere, Aljoscha Rittner
	 *
	 * @return data loaded data from endpoint
	 */
	public byte[] getBytes() {
		if (isUrlOk() == false)
			return null;

		final SecurityCredentials credentials = SecurityUtils.loadSecurityCredentials(securityIdentifier);
		final SecurityAuthentication authentication = SecurityUtils.getAuthenticationManager(credentials)
				.create(credentials);
		try {
			final String host = internal.getHost();
			final Long bad = BAD_HOSTS.get(host);
			if (bad != null) {
				if ((System.currentTimeMillis() - bad) < 1000L * 60)
					return null;
				BAD_HOSTS.remove(host);
			}

			try {
				final Future<byte[]> result = EXE
						.submit(requestWithGetAndResponse(internal, credentials.getProxy(), authentication, null));
				final byte[] data = result.get(SecurityUtils.getSecurityProfile().getTimeout(), TimeUnit.MILLISECONDS);
				if (data != null)
					return data;

			} catch (Exception e) {
				System.err.println("issue " + host + " " + e);
			}

			BAD_HOSTS.put(host, System.currentTimeMillis());
			return null;
		} finally {
			// clean up. We don't cache tokens, no expire handling. All time a re-request.
			credentials.eraseCredentials();
			authentication.eraseCredentials();
		}
	}

	/**
	 * Reads from an endpoint with a given authentication and proxy the response as
	 * blob.
	 * <p>
	 * This method allows a parametrized access to an endpoint, without a configured
	 * SecurityCredentials object. This is useful to access internally identity
	 * providers (IDP), or authorization servers (to request access tokens).
	 * <p>
	 * This method don't use the "bad-host" functionality, because the access to
	 * infrastructure services should not be obfuscated by some internal management.
	 * <p>
	 * <strong>Please don't use this method directly from DSL scripts.</strong>
	 *
	 * @param authentication authentication object data. Caller is responsible to
	 *                       erase credentials
	 * @param proxy          proxy configuration
	 * @param headers        additional headers, if needed
	 * @return loaded data from endpoint
	 */
	private byte[] getBytes(Proxy proxy, SecurityAuthentication authentication, Map<String, Object> headers) {
		if (isUrlOk() == false)
			return null;

		final Future<byte[]> result = EXE.submit(requestWithGetAndResponse(internal, proxy, authentication, headers));

		try {
			return result.get(SecurityUtils.getSecurityProfile().getTimeout(), TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			System.err.println("SURL response issue to " + internal.getHost() + " " + e);
			return null;
		}
	}

	/**
	 * Post to an endpoint with a given authentication and proxy the response as
	 * blob.
	 * <p>
	 * This method allows a parametrized access to an endpoint, without a configured
	 * SecurityCredentials object. This is useful to access internally identity
	 * providers (IDP), or authorization servers (to request access tokens).
	 * <p>
	 * This method don't use the "bad-host" functionality, because the access to
	 * infrastructure services should not be obfuscated by some internal management.
	 * <p>
	 * <strong>Please don't use this method directly from DSL scripts.</strong>
	 *
	 * @param authentication authentication object data. Caller is responsible to
	 *                       erase credentials
	 * @param proxy          proxy configuration
	 * @param data           content to post
	 * @param headers        headers, if needed
	 * @return loaded data from endpoint
	 */
	public byte[] getBytesOnPost(Proxy proxy, SecurityAuthentication authentication, String data,
			Map<String, Object> headers) {
		if (isUrlOk() == false)
			return null;

		final Future<byte[]> result = EXE
				.submit(requestWithPostAndResponse(internal, proxy, authentication, data, headers));

		try {
			return result.get(SecurityUtils.getSecurityProfile().getTimeout(), TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			System.err.println("SURL response issue to " + internal.getHost() + " " + e);
			return null;
		}
	}

    /**
     * Configures the specified {@link URLConnection} with security-related settings.
     * <p>
     * This method disables user interactions for the connection and, if the connection 
     * is an instance of {@link HttpURLConnection}, it also disables automatic 
     * following of HTTP redirects, unless the security profile is set to 
     * {@link SecurityProfile#UNSECURE}.
     * </p>
     *
     * @param connection the {@link URLConnection} to be configured
     *
     * @see URLConnection#setAllowUserInteraction(boolean)
     * @see HttpURLConnection#setInstanceFollowRedirects(boolean)
     * @see SecurityUtils#getSecurityProfile()
     * @see SecurityProfile
     */
	protected static void configure(URLConnection connection) {
		connection.setAllowUserInteraction(false);

		if (SecurityUtils.getSecurityProfile() != SecurityProfile.UNSECURE && connection instanceof HttpURLConnection)
			((HttpURLConnection) connection).setInstanceFollowRedirects(false);
	}

	/**
	 * Creates a GET request and response handler
	 *
	 * @param url            URL to request
	 * @param proxy          proxy to apply
	 * @param authentication the authentication to use
	 * @param headers        additional headers, if needed
	 * @return the callable handler.
	 */
	private static Callable<byte[]> requestWithGetAndResponse(final URL url, final Proxy proxy,
			final SecurityAuthentication authentication, final Map<String, Object> headers) {
		return new Callable<byte[]>() {

			private HttpURLConnection openConnection(final URL url) throws IOException {
				// Add proxy, if passed throw parameters
				final URLConnection connection = proxy == null ? url.openConnection() : url.openConnection(proxy);
				if (connection == null)
					return null;
				configure(connection);

				final HttpURLConnection http = (HttpURLConnection) connection;

				applyEndpointAccessAuthentication(http, authentication);
				applyAdditionalHeaders(http, headers);
				return http;
			}

			public byte[] call() throws IOException, URISyntaxException {
				final HttpURLConnection http = openConnection(url);
				// final int responseCode = http.getResponseCode();

//				if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
//						|| responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
//					final String newUrl = http.getHeaderField("Location");
//					http = openConnection(new URI(newUrl).toURL());
//				}

				return retrieveResponseAsBytes(http);
			}
		};
	}

	/**
	 * Creates a POST request and response handler with a simple String content. The
	 * content will be identified as form or JSON data. The charset encoding can be
	 * set by header parameters or will be set to UTF-8. The method to some fancy
	 * logic to simplify it for the user.
	 *
	 * @param url            URL to request via POST method
	 * @param proxy          proxy to apply
	 * @param authentication the authentication to use
	 * @param headers        additional headers, if needed
	 * @return the callable handler.
	 */
	private static Callable<byte[]> requestWithPostAndResponse(final URL url, final Proxy proxy,
			final SecurityAuthentication authentication, final String data, final Map<String, Object> headers) {
		return new Callable<byte[]>() {
			public byte[] call() throws IOException {
				// Add proxy, if passed throw parameters
				final URLConnection connection = proxy == null ? url.openConnection() : url.openConnection(proxy);
				if (connection == null)
					return null;

				configure(connection);
				final boolean withContent = StringUtils.isNotEmpty(data);

				final HttpURLConnection http = (HttpURLConnection) connection;
				http.setRequestMethod("POST");
				if (withContent)
					http.setDoOutput(true);

				applyEndpointAccessAuthentication(http, authentication);
				applyAdditionalHeaders(http, headers);

				final Charset charSet = extractCharset(http.getRequestProperty("Content-Type"));

				if (withContent)
					sendRequestAsBytes(http, data.getBytes(charSet != null ? charSet : StandardCharsets.UTF_8));

				return retrieveResponseAsBytes(http);
			}
		};
	}

	private static final Pattern pattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");
	
	private static Charset extractCharset(String contentType) {
		if (StringUtils.isEmpty(contentType))
			return null;

		final Matcher matcher = pattern.matcher(contentType);
		if (matcher.find())
			try {
				return Charset.forName(matcher.group(1));
			} catch (Exception e) {
				Logme.error(e);
			}

		return null;
	}

	/**
	 * Loads a response from an endpoint as a byte[] array.
	 *
	 * @param connection the URL connection
	 * @return the loaded byte arrays
	 * @throws IOException an exception, if the connection cannot establish or the
	 *                     download was broken
	 */
	private static byte[] retrieveResponseAsBytes(HttpURLConnection connection) throws IOException {
		final int responseCode = connection.getResponseCode();
		if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
			try (InputStream input = connection.getInputStream()) {
				return retrieveData(input);
			}
		} else {
			try (InputStream error = connection.getErrorStream()) {
				final byte[] bytes = retrieveData(error);
				throw new IOException(
						"HTTP error " + responseCode + " with " + new String(bytes, StandardCharsets.UTF_8));
			}
		}
	}

	/**
	 * Reads data in a byte[] array.
	 *
	 * @param input input stream
	 * @return byte data
	 * @throws IOException if something went wrong
	 */
	private static byte[] retrieveData(InputStream input) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int read;
		while ((read = input.read(buffer)) > 0) {
			out.write(buffer, 0, read);
		}
		out.close();
		return out.toByteArray();
	}

	/**
	 * Sends a request content payload to an endpoint.
	 *
	 * @param connection HTTP connection
	 * @param data       data as byte array
	 * @throws IOException if something went wrong
	 */
	private static void sendRequestAsBytes(HttpURLConnection connection, byte[] data) throws IOException {
		connection.setFixedLengthStreamingMode(data.length);
		try (OutputStream os = connection.getOutputStream()) {
			os.write(data);
		}
	}

	public InputStream openStream() {
		if (isUrlOk()) {
			final byte[] data = getBytes();
			if (data != null)
				return new ByteArrayInputStream(data);

		}
		return null;
	}

	/**
	 * Informs, if SecurityCredentials are configured for this connection.
	 *
	 * @return true, if credentials will be used for a connection
	 */
	public boolean isAuthorizationConfigured() {
		return WITHOUT_AUTHENTICATION.equals(securityIdentifier) == false;
	}

	/**
	 * Applies the given authentication data to the http connection.
	 *
	 * @param http           HTTP URL connection (must be an encrypted https-TLS/SSL
	 *                       connection, or http must be activated with a property)
	 * @param authentication the data to request the access
	 * @see SecurityUtils#getAccessInterceptor(SecurityAuthentication)
	 * @see SecurityUtils#isNonSSLAuthenticationAllowed()
	 */
	private static void applyEndpointAccessAuthentication(URLConnection http, SecurityAuthentication authentication) {
		if (authentication.isPublic())
			// Shortcut: No need to apply authentication.
			return;

		if (http instanceof HttpsURLConnection || SecurityUtils.isNonSSLAuthenticationAllowed()) {
			SecurityAccessInterceptor accessInterceptor = SecurityUtils.getAccessInterceptor(authentication);
			accessInterceptor.apply(authentication, http);
		} else {
			// We cannot allow applying secret tokens on plain connections. Everyone can
			// read the data.
			throw new IllegalStateException(
					"The transport of authentication data over an unencrypted http connection is not allowed");
		}
	}

	/**
	 * Set the headers for a URL connection
	 *
	 * @param headers map Keys with values (can be String or list of String)
	 */
	private static void applyAdditionalHeaders(URLConnection http, Map<String, Object> headers) {
		if (headers == null || headers.isEmpty())
			return;

		for (Map.Entry<String, Object> header : headers.entrySet()) {
			final Object value = header.getValue();
			if (value instanceof String)
				http.setRequestProperty(header.getKey(), (String) value);
			else if (value instanceof List)
				for (Object item : (List<?>) value)
					if (item != null)
						http.addRequestProperty(header.getKey(), item.toString());

		}
	}

	/**
	 * Removes the userInfo part from the URL, because we want to use the
	 * SecurityCredentials instead.
	 *
	 * @param url URL with UserInfo part
	 * @return url without UserInfo part
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	private static URL removeUserInfo(URL url) throws MalformedURLException, URISyntaxException {
		return new URI(removeUserInfoFromUrlPath(url.toExternalForm())).toURL();
	}

	/**
	 * Removes the userInfo part from the URL, because we want to use the
	 * SecurityCredentials instead.
	 *
	 * @param url URL with UserInfo part
	 * @return url without UserInfo part
	 */
	private static String removeUserInfoFromUrlPath(String url) {
		// Simple solution:
		final Matcher matcher = PATTERN_USERINFO.matcher(url);
		if (matcher.find())
			return matcher.replaceFirst("$1$3");

		return url;
	}
	// ::done
}
