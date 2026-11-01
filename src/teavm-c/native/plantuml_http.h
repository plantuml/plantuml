/*
 * HTTP(S) GET, called from Java through TeaVM's @Import (see
 * net.sourceforge.plantuml.teavm.c.NativeHttp), for the !include of a URL.
 *
 * The classlib of the TeaVM C backend has no network: java.net.URL goes through
 * an @Async method (an XHR), which would turn most of PlantUML into coroutines.
 * Here the call is blocking and the policy (security profile, allowlist) stays
 * in Java (net.sourceforge.plantuml.security.UrlPolicy).
 *
 * Windows uses WinHTTP, the other systems libcurl (when PLANTUML_HAVE_CURL is
 * defined: without it every fetch fails with PLANTUML_HTTP_NOT_AVAILABLE).
 *
 * The response of the last fetch is kept here until plantuml_http_release.
 */
#ifndef PLANTUML_HTTP_H
#define PLANTUML_HTTP_H

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/* flags of plantuml_http_fetch */
/* The host may resolve to a loopback, link-local or private address (INSECURE
 * profile, or URL of the allowlist). Without it such a host is refused. */
#define PLANTUML_HTTP_ALLOW_INNER 1
/* Follow the redirections (INSECURE profile only). Without it a 3xx is an error. */
#define PLANTUML_HTTP_FOLLOW_REDIRECTS 2

/* negative results of plantuml_http_fetch */
#define PLANTUML_HTTP_INVALID -1       /* not an http(s) URL, or no memory */
#define PLANTUML_HTTP_TOO_BIG -2       /* the response is larger than maxBytes */
#define PLANTUML_HTTP_NETWORK -3       /* connection, TLS, timeout... */
#define PLANTUML_HTTP_STATUS -4        /* HTTP status >= 400 (or a redirection not followed) */
#define PLANTUML_HTTP_INNER -5         /* the host resolves to an inner address */
#define PLANTUML_HTTP_NOT_AVAILABLE -10 /* built without an HTTP client */

/* Downloads the url (url is not NUL terminated: urlLength bytes of UTF-8).
 * timeoutMs applies to the connection and to the whole transfer. Returns the
 * size of the response (kept for plantuml_http_read), or a negative code above. */
int32_t plantuml_http_fetch(const int8_t* url, int32_t urlLength, int32_t timeoutMs, int32_t maxBytes,
		int32_t flags);

/* Copies the next bytes of the response into the start of dest (at most length).
 * Returns the number of bytes copied, 0 when there is nothing left. */
int32_t plantuml_http_read(int8_t* dest, int32_t length);

/* Forgets the response. */
void plantuml_http_release(void);

#ifdef __cplusplus
}
#endif

#endif
