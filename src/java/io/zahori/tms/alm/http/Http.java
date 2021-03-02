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

public class Http {

	private static final Logger LOG = LoggerFactory.getLogger(Http.class);

	// HTTP Headers
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_COOKIE = "Cookie";
	public static final String HEADER_SET_COOKIE = "Set-Cookie";

	// HTTP Header values
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	private HttpHeaders headers = new HttpHeaders();
	private List<String> cookies = new ArrayList<String>();

	// Spring rest template
	private RestTemplate restTemplate = new RestTemplate();

	public Http() {
	}

	public <T> T getXml(String url, Map<String, String> urlParams, Class<T> responseType) {
		URI uri = prepareUriWithParams(url, urlParams);

		addHeader(HEADER_ACCEPT, APPLICATION_XML);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_XML);
		return get(uri, responseType);
	}

	public <T> T getJson(String url, Map<String, String> urlParams, Class<T> responseType) {
		URI uri = prepareUriWithParams(url, urlParams);

		addHeader(HEADER_ACCEPT, APPLICATION_JSON);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_JSON);
		return get(uri, responseType);
	}

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

	public <T> T getXml(String url, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_XML);
		return get(url, responseType);
	}

	public <T> T getJson(String url, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_JSON);
		return get(url, responseType);
	}

	public <T> T getOctetStream(String url, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_OCTET_STREAM);
		return get(url, responseType);
	}

	public <T> T postXml(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_XML);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_XML);
		return post(url, body, responseType);
	}

	public <T> T postJson(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_JSON);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_JSON);

		return post(url, body, responseType);
	}

	public <T> T postOctetStream(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_OCTET_STREAM);
		return post(url, body, responseType);
	}

	public <T> T get(String url, Class<T> responseType) {
		return http(url, HttpMethod.GET, null, responseType);
	}

	public <T> T get(String url, Map<String, String> urlParams, Class<T> responseType) {
		URI uri = prepareUriWithParams(url, urlParams);
		return get(uri, responseType);
	}

	public <T> T get(URI uri, Class<T> responseType) {
		return http(uri, HttpMethod.GET, null, responseType);
	}

	public <T> T post(String url, Object body, Class<T> responseType) {
		return http(url, HttpMethod.POST, body, responseType);
	}

	public <T> T putXml(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_XML);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_XML);
		return put(url, body, responseType);
	}

	public <T> T putJson(String url, Object body, Class<T> responseType) {
		addHeader(HEADER_ACCEPT, APPLICATION_JSON);
		addHeader(HEADER_CONTENT_TYPE, APPLICATION_JSON);
		return put(url, body, responseType);
	}

	public <T> T put(String url, Object body, Class<T> responseType) {
		return http(url, HttpMethod.PUT, body, responseType);
	}

	public <T> T http(String url, HttpMethod method, Object requestBody, Class<T> responseType) {
		try {
			URI uri = new URI(url);
			return http(uri, method, requestBody, responseType);
		} catch (URISyntaxException e) {
			throw new RuntimeException("\n" + method + " " + url + "\n" + "--> " + e.getMessage());
		}
	}

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

	public void loginWithForm(String url, String userFieldName, String user, String passwordFieldName,
			String password) {

		// body
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
		requestBody.add(userFieldName, user);
		requestBody.add(passwordFieldName, password);

		// request
		post(url, requestBody, String.class);
	}

	public void loginWithBasicAuth(String url, String username, String password) {
		addHeader(HEADER_AUTHORIZATION, getBasicAuthInBase64(username, password));
		get(url, String.class);
	}

	private String getBasicAuthInBase64(String username, String password) {
		byte[] credBytes = (username + ":" + password).getBytes();
		return "Basic " + Base64Encoder.encode(credBytes);
	}

	public void addHeader(String name, String value) {
		headers.add(name, value);
	}

	public void removeHeader(String name) {
		headers.remove(name);
	}

	private void setCookiesInHeaders() {
		for (String cookie : cookies) {
			addHeader(HEADER_COOKIE, cookie);
		}
	}

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
