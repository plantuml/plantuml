#include "plantuml_http.h"

#include <stdlib.h>
#include <string.h>

#ifdef _WIN32
#ifndef WIN32_LEAN_AND_MEAN
#define WIN32_LEAN_AND_MEAN
#endif
#include <winsock2.h>
#include <ws2tcpip.h>
#include <windows.h>
#include <winhttp.h>
#else
#include <arpa/inet.h>
#include <netdb.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/types.h>
#ifdef PLANTUML_HAVE_CURL
#include <curl/curl.h>
#endif
#endif

/* The response of the last fetch */
static uint8_t* g_data = NULL;
static size_t g_capacity = 0;
static int32_t g_size = 0;
static int32_t g_cursor = 0;

static void reset_response(void) {
	free(g_data);
	g_data = NULL;
	g_capacity = 0;
	g_size = 0;
	g_cursor = 0;
}

/* Appends to the response. Returns 0, PLANTUML_HTTP_TOO_BIG or PLANTUML_HTTP_INVALID (no memory). */
static int append_data(const void* src, size_t length, int32_t max_bytes) {
	size_t needed;
	if (length == 0)
		return 0;
	if ((uint64_t)g_size + (uint64_t)length > (uint64_t)max_bytes)
		return PLANTUML_HTTP_TOO_BIG;
	needed = (size_t)g_size + length;
	if (needed > g_capacity) {
		size_t capacity = g_capacity == 0 ? 16384 : g_capacity;
		uint8_t* grown;
		while (capacity < needed)
			capacity *= 2;
		grown = (uint8_t*)realloc(g_data, capacity);
		if (grown == NULL)
			return PLANTUML_HTTP_INVALID;
		g_data = grown;
		g_capacity = capacity;
	}
	memcpy(g_data + g_size, src, length);
	g_size += (int32_t)length;
	return 0;
}

/* ---- Refusal of the inner addresses (loopback, link-local, private) ----
 *
 * As the JVM does for the profiles other than INSECURE. The address is checked
 * before the connection: like on the JVM, a DNS answer that changes between the
 * check and the connection is not detected. */

static void network_init(void) {
#ifdef _WIN32
	static int done = 0;
	WSADATA data;
	if (done == 0) {
		WSAStartup(MAKEWORD(2, 2), &data);
		done = 1;
	}
#endif
}

/* Host of an http(s) URL, without the brackets of an IPv6 address. Returns 0 if there is none. */
static int extract_host(const char* url, char* host, size_t capacity) {
	const char* p = strstr(url, "://");
	const char* q;
	const char* at = NULL;
	size_t n = 0;
	if (p == NULL)
		return 0;
	p += 3;
	for (q = p; *q != 0 && *q != '/' && *q != '?' && *q != '#'; q++)
		if (*q == '@')
			at = q;
	if (at != NULL)
		p = at + 1;
	if (*p == '[') {
		p++;
		while (*p != 0 && *p != ']' && n + 1 < capacity)
			host[n++] = *p++;
	} else {
		while (*p != 0 && *p != ':' && *p != '/' && *p != '?' && *p != '#' && n + 1 < capacity)
			host[n++] = *p++;
	}
	host[n] = 0;
	return n > 0 && n + 1 < capacity;
}

static int is_inner_v4(uint32_t a) {
	const unsigned b0 = (a >> 24) & 0xFF;
	const unsigned b1 = (a >> 16) & 0xFF;
	if (b0 == 0 || b0 == 127 || b0 == 10)
		return 1;
	if (b0 == 172 && b1 >= 16 && b1 <= 31)
		return 1;
	if (b0 == 192 && b1 == 168)
		return 1;
	if (b0 == 169 && b1 == 254)
		return 1;
	return 0;
}

static int is_inner_address(const struct sockaddr* sa) {
	if (sa->sa_family == AF_INET) {
		const struct sockaddr_in* s4 = (const struct sockaddr_in*)sa;
		return is_inner_v4(ntohl(s4->sin_addr.s_addr));
	}
	if (sa->sa_family == AF_INET6) {
		const struct sockaddr_in6* s6 = (const struct sockaddr_in6*)sa;
		const uint8_t* b = (const uint8_t*)&s6->sin6_addr;
		int i;
		int zeros = 1;
		for (i = 0; i < 15; i++)
			if (b[i] != 0)
				zeros = 0;
		/* :: (any) and ::1 (loopback) */
		if (zeros && (b[15] == 0 || b[15] == 1))
			return 1;
		/* fe80::/10 (link-local), fec0::/10 (site-local) */
		if (b[0] == 0xFE && ((b[1] & 0xC0) == 0x80 || (b[1] & 0xC0) == 0xC0))
			return 1;
		/* fc00::/7 (unique local) */
		if ((b[0] & 0xFE) == 0xFC)
			return 1;
		/* ::ffff:a.b.c.d (IPv4 mapped) */
		zeros = 1;
		for (i = 0; i < 10; i++)
			if (b[i] != 0)
				zeros = 0;
		if (zeros && b[10] == 0xFF && b[11] == 0xFF)
			return is_inner_v4(((uint32_t)b[12] << 24) | ((uint32_t)b[13] << 16) | ((uint32_t)b[14] << 8)
					| (uint32_t)b[15]);
	}
	return 0;
}

/* 1 if the host of the URL is, or resolves to, an inner address (or is unusable) */
static int host_is_inner(const char* url) {
	char host[256];
	struct addrinfo hints;
	struct addrinfo* result = NULL;
	struct addrinfo* item;
	int inner = 0;
	if (extract_host(url, host, sizeof host) == 0)
		return 1;
	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	/* A host that does not resolve is not inner: the client reports the failure */
	if (getaddrinfo(host, NULL, &hints, &result) != 0)
		return 0;
	for (item = result; item != NULL; item = item->ai_next)
		if (is_inner_address(item->ai_addr)) {
			inner = 1;
			break;
		}
	freeaddrinfo(result);
	return inner;
}

/* ---- The transfer. Returns 0 (the response is in g_data) or a negative code ---- */

#ifdef _WIN32

static int do_fetch(const char* url, int32_t timeout_ms, int32_t max_bytes, int32_t flags) {
	int result = PLANTUML_HTTP_NETWORK;
	int follow = (flags & PLANTUML_HTTP_FOLLOW_REDIRECTS) != 0;
	int wide_length;
	wchar_t* wide = NULL;
	wchar_t* host = NULL;
	wchar_t* path = NULL;
	size_t at = 0;
	URL_COMPONENTS parts;
	HINTERNET session = NULL;
	HINTERNET connection = NULL;
	HINTERNET request = NULL;
	DWORD status = 0;
	DWORD status_size = sizeof status;
	DWORD available = 0;
	DWORD got = 0;
	unsigned char buffer[16384];
	int appended;

	wide_length = MultiByteToWideChar(CP_UTF8, 0, url, -1, NULL, 0);
	if (wide_length <= 0)
		return PLANTUML_HTTP_INVALID;
	wide = (wchar_t*)malloc(sizeof(wchar_t) * (size_t)wide_length);
	if (wide == NULL)
		return PLANTUML_HTTP_INVALID;
	MultiByteToWideChar(CP_UTF8, 0, url, -1, wide, wide_length);

	memset(&parts, 0, sizeof parts);
	parts.dwStructSize = sizeof parts;
	parts.dwSchemeLength = (DWORD)-1;
	parts.dwHostNameLength = (DWORD)-1;
	parts.dwUrlPathLength = (DWORD)-1;
	parts.dwExtraInfoLength = (DWORD)-1;
	if (!WinHttpCrackUrl(wide, 0, 0, &parts)
			|| (parts.nScheme != INTERNET_SCHEME_HTTP && parts.nScheme != INTERNET_SCHEME_HTTPS)) {
		result = PLANTUML_HTTP_INVALID;
		goto done;
	}

	host = (wchar_t*)calloc((size_t)parts.dwHostNameLength + 1, sizeof(wchar_t));
	path = (wchar_t*)calloc((size_t)parts.dwUrlPathLength + (size_t)parts.dwExtraInfoLength + 2, sizeof(wchar_t));
	if (host == NULL || path == NULL) {
		result = PLANTUML_HTTP_INVALID;
		goto done;
	}
	memcpy(host, parts.lpszHostName, (size_t)parts.dwHostNameLength * sizeof(wchar_t));
	if (parts.dwUrlPathLength == 0) {
		path[at++] = L'/';
	} else {
		memcpy(path, parts.lpszUrlPath, (size_t)parts.dwUrlPathLength * sizeof(wchar_t));
		at = parts.dwUrlPathLength;
	}
	if (parts.dwExtraInfoLength > 0)
		memcpy(path + at, parts.lpszExtraInfo, (size_t)parts.dwExtraInfoLength * sizeof(wchar_t));

	session = WinHttpOpen(L"PlantUML", WINHTTP_ACCESS_TYPE_AUTOMATIC_PROXY, WINHTTP_NO_PROXY_NAME,
			WINHTTP_NO_PROXY_BYPASS, 0);
	if (session == NULL)
		goto done;
	WinHttpSetTimeouts(session, timeout_ms, timeout_ms, timeout_ms, timeout_ms);

	connection = WinHttpConnect(session, host, parts.nPort, 0);
	if (connection == NULL)
		goto done;
	request = WinHttpOpenRequest(connection, L"GET", path, NULL, WINHTTP_NO_REFERER, WINHTTP_DEFAULT_ACCEPT_TYPES,
			parts.nScheme == INTERNET_SCHEME_HTTPS ? WINHTTP_FLAG_SECURE : 0);
	if (request == NULL)
		goto done;
	if (!follow) {
		DWORD policy = WINHTTP_OPTION_REDIRECT_POLICY_NEVER;
		WinHttpSetOption(request, WINHTTP_OPTION_REDIRECT_POLICY, &policy, sizeof policy);
	}

	if (!WinHttpSendRequest(request, WINHTTP_NO_ADDITIONAL_HEADERS, 0, WINHTTP_NO_REQUEST_DATA, 0, 0, 0)
			|| !WinHttpReceiveResponse(request, NULL))
		goto done;
	if (!WinHttpQueryHeaders(request, WINHTTP_QUERY_STATUS_CODE | WINHTTP_QUERY_FLAG_NUMBER,
			WINHTTP_HEADER_NAME_BY_INDEX, &status, &status_size, WINHTTP_NO_HEADER_INDEX))
		goto done;
	if (status >= 400 || (status >= 300 && !follow)) {
		result = PLANTUML_HTTP_STATUS;
		goto done;
	}

	for (;;) {
		if (!WinHttpQueryDataAvailable(request, &available))
			goto done;
		if (available == 0)
			break;
		if (available > sizeof buffer)
			available = sizeof buffer;
		if (!WinHttpReadData(request, buffer, available, &got))
			goto done;
		if (got == 0)
			break;
		appended = append_data(buffer, got, max_bytes);
		if (appended < 0) {
			result = appended;
			goto done;
		}
	}
	result = 0;

done:
	if (request != NULL)
		WinHttpCloseHandle(request);
	if (connection != NULL)
		WinHttpCloseHandle(connection);
	if (session != NULL)
		WinHttpCloseHandle(session);
	free(path);
	free(host);
	free(wide);
	return result;
}

#elif defined(PLANTUML_HAVE_CURL)

struct sink {
	int32_t max_bytes;
	int rc;
};

static size_t on_data(char* ptr, size_t size, size_t count, void* user) {
	struct sink* sink = (struct sink*)user;
	const size_t length = size * count;
	const int rc = append_data(ptr, length, sink->max_bytes);
	if (rc < 0) {
		/* Returning less than length makes curl stop with CURLE_WRITE_ERROR */
		sink->rc = rc;
		return 0;
	}
	return length;
}

static int do_fetch(const char* url, int32_t timeout_ms, int32_t max_bytes, int32_t flags) {
	static int initialized = 0;
	const int follow = (flags & PLANTUML_HTTP_FOLLOW_REDIRECTS) != 0;
	struct sink sink;
	CURL* curl;
	CURLcode code;
	long status = 0;
	int result;

	if (initialized == 0) {
		if (curl_global_init(CURL_GLOBAL_DEFAULT) != 0)
			return PLANTUML_HTTP_NETWORK;
		initialized = 1;
	}
	curl = curl_easy_init();
	if (curl == NULL)
		return PLANTUML_HTTP_NETWORK;

	sink.max_bytes = max_bytes;
	sink.rc = 0;
	curl_easy_setopt(curl, CURLOPT_URL, url);
	curl_easy_setopt(curl, CURLOPT_USERAGENT, "PlantUML");
	curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1L);
	curl_easy_setopt(curl, CURLOPT_TIMEOUT_MS, (long)timeout_ms);
	curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT_MS, (long)timeout_ms);
	curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, follow ? 1L : 0L);
	curl_easy_setopt(curl, CURLOPT_MAXREDIRS, 5L);
#if LIBCURL_VERSION_NUM >= 0x075500
	curl_easy_setopt(curl, CURLOPT_PROTOCOLS_STR, "http,https");
	curl_easy_setopt(curl, CURLOPT_REDIR_PROTOCOLS_STR, "http,https");
#else
	curl_easy_setopt(curl, CURLOPT_PROTOCOLS, (long)(CURLPROTO_HTTP | CURLPROTO_HTTPS));
	curl_easy_setopt(curl, CURLOPT_REDIR_PROTOCOLS, (long)(CURLPROTO_HTTP | CURLPROTO_HTTPS));
#endif
	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, on_data);
	curl_easy_setopt(curl, CURLOPT_WRITEDATA, &sink);

	code = curl_easy_perform(curl);
	if (code == CURLE_OK) {
		curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, &status);
		result = (status >= 400 || (status >= 300 && !follow)) ? PLANTUML_HTTP_STATUS : 0;
	} else {
		result = sink.rc < 0 ? sink.rc : PLANTUML_HTTP_NETWORK;
	}
	curl_easy_cleanup(curl);
	return result;
}

#else

static int do_fetch(const char* url, int32_t timeout_ms, int32_t max_bytes, int32_t flags) {
	return PLANTUML_HTTP_NOT_AVAILABLE;
}

#endif

int32_t plantuml_http_fetch(const int8_t* url, int32_t urlLength, int32_t timeoutMs, int32_t maxBytes,
		int32_t flags) {
	char* copy;
	int32_t i;
	int result;

	reset_response();
	if (url == NULL || urlLength <= 0 || urlLength > 8192 || maxBytes <= 0)
		return PLANTUML_HTTP_INVALID;
	if (timeoutMs <= 0)
		timeoutMs = 60000;

	copy = (char*)malloc((size_t)urlLength + 1);
	if (copy == NULL)
		return PLANTUML_HTTP_INVALID;
	memcpy(copy, url, (size_t)urlLength);
	copy[urlLength] = 0;

	/* http(s) only, and no space, control character or NUL (java.net.URI refuses them too) */
	if (strncmp(copy, "http://", 7) != 0 && strncmp(copy, "https://", 8) != 0) {
		free(copy);
		return PLANTUML_HTTP_INVALID;
	}
	for (i = 0; i < urlLength; i++)
		if ((unsigned char)copy[i] <= 0x20 || (unsigned char)copy[i] == 0x7F) {
			free(copy);
			return PLANTUML_HTTP_INVALID;
		}

	if ((flags & PLANTUML_HTTP_ALLOW_INNER) == 0) {
		network_init();
		if (host_is_inner(copy)) {
			free(copy);
			return PLANTUML_HTTP_INNER;
		}
	}

	result = do_fetch(copy, timeoutMs, maxBytes, flags);
	free(copy);
	if (result < 0) {
		reset_response();
		return result;
	}
	return g_size;
}

int32_t plantuml_http_read(int8_t* dest, int32_t length) {
	const int32_t remaining = g_size - g_cursor;
	if (dest == NULL || length <= 0 || remaining <= 0)
		return 0;
	if (length > remaining)
		length = remaining;
	memcpy(dest, g_data + g_cursor, (size_t)length);
	g_cursor += length;
	return length;
}

void plantuml_http_release(void) {
	reset_response();
}
