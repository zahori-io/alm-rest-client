package io.zahori.tms.alm.restclient.infrastructure;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The type Rest connector.
 */
public class RestConnector {

	/**
	 * The Cookies.
	 */
	protected Map<String, String> cookies;
	/**
	 * The Server url.
	 */
	protected String serverUrl;
	/**
	 * The Domain.
	 */
	protected String domain;
	/**
	 * The Project.
	 */
	protected String project;

	/**
	 * Init rest connector.
	 *
	 * @param cookies   the cookies
	 * @param serverUrl the server url
	 * @param domain    the domain
	 * @param project   the project
	 * @return the rest connector
	 */
	public RestConnector init(Map<String, String> cookies, String serverUrl, String domain, String project) {

		this.cookies = cookies;
		this.serverUrl = serverUrl;
		this.domain = domain;
		this.project = project;

		return this;
	}

	/**
	 * Instantiates a new Rest connector.
	 */
	private RestConnector() {
	}

	/**
	 * The constant instance.
	 */
	private static RestConnector instance = new RestConnector();

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static RestConnector getInstance() {
		return instance;
	}

	/**
	 * Build entity collection url string.
	 *
	 * @param entityType the entity type
	 * @return the string
	 */
	public String buildEntityCollectionUrl(String entityType) {
		return buildUrl("rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s");
	}

	/**
	 * Build url string.
	 *
	 * @param path the path
	 * @return the string
	 */
	public String buildUrl(String path) {

		return String.format("%1$s/%2$s", serverUrl, path);
	}

	/**
	 * Gets cookies.
	 *
	 * @return the cookies
	 */
	public Map<String, String> getCookies() {
		return cookies;
	}

	/**
	 * Sets cookies.
	 *
	 * @param cookies the cookies
	 */
	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	/**
	 * Http put response.
	 *
	 * @param url     the url
	 * @param data    the data
	 * @param headers the headers
	 * @return the response
	 * @throws Exception the exception
	 */
	public Response httpPut(String url, byte[] data, Map<String, String> headers) throws Exception {

		return doHttp("PUT", url, null, data, headers, cookies);
	}

	/**
	 * Http post response.
	 *
	 * @param url     the url
	 * @param data    the data
	 * @param headers the headers
	 * @return the response
	 * @throws Exception the exception
	 */
	public Response httpPost(String url, byte[] data, Map<String, String> headers) throws Exception {

		return doHttp("POST", url, null, data, headers, cookies);
	}

	/**
	 * Http delete response.
	 *
	 * @param url     the url
	 * @param headers the headers
	 * @return the response
	 * @throws Exception the exception
	 */
	public Response httpDelete(String url, Map<String, String> headers) throws Exception {

		return doHttp("DELETE", url, null, null, headers, cookies);
	}

	/**
	 * Http get response.
	 *
	 * @param url         the url
	 * @param queryString the query string
	 * @param headers     the headers
	 * @return the response
	 * @throws Exception the exception
	 */
	public Response httpGet(String url, String queryString, Map<String, String> headers) throws Exception {

		return doHttp("GET", url, queryString, null, headers, cookies);
	}

	/**
	 * Do http response.
	 *
	 * @param type        the type
	 * @param url         the url
	 * @param queryString the query string
	 * @param data        the data
	 * @param headers     the headers
	 * @param cookies     the cookies
	 * @return the response
	 * @throws Exception the exception
	 */
	private Response doHttp(String type, String url, String queryString, byte[] data, Map<String, String> headers,
			Map<String, String> cookies) throws Exception {

		if ((queryString != null) && !queryString.isEmpty()) {

			url += "?" + queryString;
		}

		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

		con.setRequestMethod(type);
		String cookieString = getCookieString();

		prepareHttpRequest(con, headers, data, cookieString);
		con.connect();
		Response ret = retrieveHtmlResponse(con);

		updateCookies(ret);

		return ret;
	}

	/**
	 * Prepare http request.
	 *
	 * @param con          the con
	 * @param headers      the headers
	 * @param bytes        the bytes
	 * @param cookieString the cookie string
	 * @throws IOException the io exception
	 */
	private void prepareHttpRequest(HttpURLConnection con, Map<String, String> headers, byte[] bytes,
			String cookieString) throws IOException {

		String contentType = null;

		//attach cookie information if such exists
		if ((cookieString != null) && !cookieString.isEmpty()) {

			con.setRequestProperty("Cookie", cookieString);
		}

		//send data from headers
		if (headers != null) {

			//Skip the content-type header - should only be sent
			//if you actually have any content to send. see below.
			contentType = headers.remove("Content-Type");

			Iterator<Entry<String, String>> headersIterator = headers.entrySet().iterator();
			while (headersIterator.hasNext()) {
				Entry<String, String> header = headersIterator.next();
				con.setRequestProperty(header.getKey(), header.getValue());
			}
		}

		// If there's data to attach to the request, it's handled here.
		// Note that if data exists, we take into account previously removed
		// content-type.
		if ((bytes != null) && (bytes.length > 0)) {

			con.setDoOutput(true);

			//warning: if you add content-type header then you MUST send
			// information or receive error.
			//so only do so if you're writing information...
			if (contentType != null) {
				con.setRequestProperty("Content-Type", contentType);
			}

			OutputStream out = con.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
		}
	}

	/**
	 * Retrieve html response response.
	 *
	 * @param con the con
	 * @return the response
	 * @throws Exception the exception
	 */
	private Response retrieveHtmlResponse(HttpURLConnection con) throws Exception {

		Response ret = new Response();

		ret.setStatusCode(con.getResponseCode());
		ret.setResponseHeaders(con.getHeaderFields());

		InputStream inputStream;
		//select the source of the input bytes, first try 'regular' input
		try {
			inputStream = con.getInputStream();
		}

		/*
		 If the connection to the server somehow failed, for example 404 or 500,
		 con.getInputStream() will throw an exception, which we'll keep.
		 We'll also store the body of the exception page, in the response data.
		 */
		catch (Exception e) {

			inputStream = con.getErrorStream();
			ret.setFailure(e);
		}

		// This actually takes the data from the previously set stream
		// (error or input) and stores it in a byte[] inside the response
		ByteArrayOutputStream container = new ByteArrayOutputStream();

		byte[] buf = new byte[1024];
		int read;
		while ((read = inputStream.read(buf, 0, 1024)) > 0) {
			container.write(buf, 0, read);
		}

		ret.setResponseData(container.toByteArray());

		return ret;
	}

	/**
	 * Update cookies.
	 *
	 * @param response the response
	 */
	private void updateCookies(Response response) {

		Iterable<String> newCookies = response.getResponseHeaders().get("Set-Cookie");
		if (newCookies != null) {

			for (String cookie : newCookies) {
				int equalIndex = cookie.indexOf('=');
				int semicolonIndex = cookie.indexOf(';');

				String cookieKey = cookie.substring(0, equalIndex);
				String cookieValue = cookie.substring(equalIndex + 1, semicolonIndex);

				cookies.put(cookieKey, cookieValue);
			}
		}
	}

	/**
	 * Gets cookie string.
	 *
	 * @return the cookie string
	 */
	public String getCookieString() {

		StringBuilder sb = new StringBuilder();

		if (!cookies.isEmpty()) {

			Set<Entry<String, String>> cookieEntries = cookies.entrySet();
			for (Entry<String, String> entry : cookieEntries) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
			}
		}

		String ret = sb.toString();

		return ret;
	}

}
