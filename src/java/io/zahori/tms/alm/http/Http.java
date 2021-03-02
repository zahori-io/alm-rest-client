package io.zahori.tms.alm.http;

/*-
 * #%L
 * alm-rest-client
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2021 PANEL SISTEMAS INFORMATICOS,S.L
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.zahori.tms.alm.restclient.infrastructure.Base64Encoder;

/**
 * The type Http.
 */
public class Http {

	/**
	 * The constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Http.class);

	/**
	 * The constant HEADER_AUTHORIZATION.
	 */
// HTTP Headers
	public static final String HEADER_AUTHORIZATION = "Authorization";
	/**
	 * The constant HEADER_ACCEPT.
	 */
	public static final String HEADER_ACCEPT = "Accept";
	/**
	 * The constant HEADER_CONTENT_TYPE.
	 */
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	/**
	 * The constant HEADER_COOKIE.
	 */
	public static final String HEADER_COOKIE = "Cookie";
	/**
	 * The constant HEADER_SET_COOKIE.
	 */
	public static final String HEADER_SET_COOKIE = "Set-Cookie";

	/**
	 * The constant APPLICATION_XML.
	 */
// HTTP Header values
	public static final String APPLICATION_XML = "application/xml";
	/**
	 * The constant APPLICATION_JSON.
	 */
	public static final String APPLICATION_JSON = "application/json";
	/**
	 * The constant APPLICATION_OCTET_STREAM.
	 */
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	/**
	 * The Headers.
	 */
	private HttpHeaders headers = new HttpHeaders();
	/**
	 * The Cookies.
	 */
	private List<String> cookies = new ArrayList<String>();

	/**
	 * The Rest template.
	 */
// Spring rest template
	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * Instantiates a new Http.
	 */
	public Http() {
	}

	/**
	 * Gets xml.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param urlParams    the url params
	 * @param responseType the response type
	 * @return the xml
	 */
	public <T> T getXml(String url, Map<String, String> urlParams, Class<T> responseType) {
		URI uri = prepareUriWithParams(url, urlParams);

		addHeader(HEADER_ACCEPT, APPLICATION_XML);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_XML);
		return get(uri, responseType);
	}

	/**
	 * Gets json.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param urlParams    the url params
	 * @param responseType the response type
	 * @return the json
	 */
	public <T> T getJson(String url, Map<String, String> urlParams, Class<T> responseType) {
		URI uri = prepareUriWithParams(url, urlParams);

		addHeader(HEADER_ACCEPT, APPLICATION_JSON);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_JSON);
		return get(uri, responseType);
	}

	/**
	 * Prepare uri with params uri.
	 *
	 * @param url       the url
	 * @param urlParams the url params
	 * @return the uri
	 */
	private URI prepareUriWithParams(String url, Map<String, String> urlParams) {
		// Url parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		if (urlParams != null) {
			for (Map.Entry<String, String> param : urlParams.entrySet()) {
				builder.queryParam(param.getKey(), param.getValue());
			}
		}
		return builder.build().encode().toUri();
	}

	/**
	 * Gets xml.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param responseType the response type
	 * @return the xml
	 */
	public <T> T getXml(String url, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_XML);
		return get(url, responseType);
	}

	/**
	 * Gets json.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param responseType the response type
	 * @return the json
	 */
	public <T> T getJson(String url, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_JSON);
		return get(url, responseType);
	}

	/**
	 * Gets octet stream.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param responseType the response type
	 * @return the octet stream
	 */
	public <T> T getOctetStream(String url, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_OCTET_STREAM);
		return get(url, responseType);
	}

	/**
	 * Post xml t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param body         the body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T postXml(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_XML);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_XML);
		return post(url, body, responseType);
	}

	/**
	 * Post json t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param body         the body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T postJson(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_JSON);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_JSON);

		return post(url, body, responseType);
	}

	/**
	 * Post octet stream t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param body         the body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T postOctetStream(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_OCTET_STREAM);
		return post(url, body, responseType);
	}

	/**
	 * Get t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T get(String url, Class<T> responseType) {
		return http(url, HttpMethod.GET, null, responseType);
	}

	/**
	 * Get t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param urlParams    the url params
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T get(String url, Map<String, String> urlParams, Class<T> responseType) {
		URI uri = prepareUriWithParams(url, urlParams);
		return get(uri, responseType);
	}

	/**
	 * Get t.
	 *
	 * @param <T>          the type parameter
	 * @param uri          the uri
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T get(URI uri, Class<T> responseType) {
		return http(uri, HttpMethod.GET, null, responseType);
	}

	/**
	 * Post t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param body         the body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T post(String url, Object body, Class<T> responseType) {
		return http(url, HttpMethod.POST, body, responseType);
	}

	/**
	 * Put xml t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param body         the body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T putXml(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_XML);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_XML);
		return put(url, body, responseType);
	}

	/**
	 * Put json t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param body         the body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T putJson(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_JSON);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_JSON);
		return put(url, body, responseType);
	}

	/**
	 * Put t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param body         the body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T put(String url, Object body, Class<T> responseType) {
		return http(url, HttpMethod.PUT, body, responseType);
	}

	/**
	 * Http t.
	 *
	 * @param <T>          the type parameter
	 * @param url          the url
	 * @param method       the method
	 * @param requestBody  the request body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T http(String url, HttpMethod method, Object requestBody, Class<T> responseType) {
		try {
			URI uri = new URI(url);
			return http(uri, method, requestBody, responseType);
		} catch (URISyntaxException e) {
			throw new RuntimeException("\n" + method + " " + url + "\n" + "--> " + e.getMessage());
		}
	}

	/**
	 * Http t.
	 *
	 * @param <T>          the type parameter
	 * @param uri          the uri
	 * @param method       the method
	 * @param requestBody  the request body
	 * @param responseType the response type
	 * @return the t
	 */
	public <T> T http(URI uri, HttpMethod method, Object requestBody, Class<T> responseType) {

		// body and headers
		HttpEntity bodyAndHeaders = new HttpEntity(requestBody, headers);

		try {

			// request
			ResponseEntity<T> response = restTemplate.exchange(uri, method, bodyAndHeaders, responseType);

			// response
			LOG.info(response.getStatusCode() + " " + method + " " + uri);
			T body = response.getBody();
			LOG.debug("    --> " + body);

			// save cookies
			saveNewCookies(response);

			return body;

		} catch (RestClientException e) {
			throw new RuntimeException("\n" + method + " " + uri + "\n" + "--> " + e.getMessage());
		} finally {

			// clear headers for next requests
			headers.clear();

			// Set cookies in headers for next requests
			setCookiesInHeaders();
		}
	}

	/**
	 * Login with form.
	 *
	 * @param url               the url
	 * @param userFieldName     the user field name
	 * @param user              the user
	 * @param passwordFieldName the password field name
	 * @param password          the password
	 */
	public void loginWithForm(String url, String userFieldName, String user, String passwordFieldName,
			String password) {

		// body
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
		requestBody.add(userFieldName, user);
		requestBody.add(passwordFieldName, password);

		// request
		post(url, requestBody, String.class);
	}

	/**
	 * Login with basic auth.
	 *
	 * @param url      the url
	 * @param username the username
	 * @param password the password
	 */
	public void loginWithBasicAuth(String url, String username, String password) {
		addHeader(HEADER_AUTHORIZATION, getBasicAuthInBase64(username, password));
		get(url, String.class);
	}

	/**
	 * Gets basic auth in base 64.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the basic auth in base 64
	 */
	private String getBasicAuthInBase64(String username, String password) {
		byte[] credBytes = (username + ":" + password).getBytes();
		return "Basic " + Base64Encoder.encode(credBytes);
	}

	/**
	 * Add header.
	 *
	 * @param name  the name
	 * @param value the value
	 */
	public void addHeader(String name, String value) {
		headers.add(name, value);
	}

	/**
	 * Remove header.
	 *
	 * @param name the name
	 */
	public void removeHeader(String name) {
		headers.remove(name);
	}

	/**
	 * Sets cookies in headers.
	 */
	private void setCookiesInHeaders() {
		for (String cookie : cookies) {
			addHeader(HEADER_COOKIE, cookie);
		}
	}

	/**
	 * Save new cookies.
	 *
	 * @param httpEntity the http entity
	 */
	private void saveNewCookies(HttpEntity httpEntity) {
		if (httpEntity == null) {
			return;
		}

		// get new cookies from response
		List<String> newCookies = httpEntity.getHeaders().get(HEADER_SET_COOKIE);

		// save new cookies
		cookies.addAll(newCookies);
	}

}
