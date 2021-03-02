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

import java.util.Map;

/**
 * The type Response.
 */
public class Response {

	/**
	 * The Response headers.
	 */
	private Map<String, ? extends Iterable<String>> responseHeaders = null;
	/**
	 * The Response data.
	 */
	private byte[] responseData = null;
	/**
	 * The Failure.
	 */
	private Exception failure = null;
	/**
	 * The Status code.
	 */
	private int statusCode = 0;

	/**
	 * Instantiates a new Response.
	 *
	 * @param responseHeaders the response headers
	 * @param responseData    the response data
	 * @param failure         the failure
	 * @param statusCode      the status code
	 */
	public Response(Map<String, Iterable<String>> responseHeaders, byte[] responseData, Exception failure,
			int statusCode) {
		super();
		this.responseHeaders = responseHeaders;
		this.responseData = responseData;
		this.failure = failure;
		this.statusCode = statusCode;
	}

	/**
	 * Instantiates a new Response.
	 */
	public Response() {
	}

	/**
	 * Gets response headers.
	 *
	 * @return the response headers
	 */
	public Map<String, ? extends Iterable<String>> getResponseHeaders() {
		return responseHeaders;
	}

	/**
	 * Sets response headers.
	 *
	 * @param responseHeaders the response headers
	 */
	public void setResponseHeaders(Map<String, ? extends Iterable<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	/**
	 * Get response data byte [ ].
	 *
	 * @return the byte [ ]
	 */
	public byte[] getResponseData() {
		return responseData;
	}

	/**
	 * Sets response data.
	 *
	 * @param responseData the response data
	 */
	public void setResponseData(byte[] responseData) {
		this.responseData = responseData;
	}

	/**
	 * Gets failure.
	 *
	 * @return the failure
	 */
	public Exception getFailure() {
		return failure;
	}

	/**
	 * Sets failure.
	 *
	 * @param failure the failure
	 */
	public void setFailure(Exception failure) {
		this.failure = failure;
	}

	/**
	 * Gets status code.
	 *
	 * @return the status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets status code.
	 *
	 * @param statusCode the status code
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {

		return new String(this.responseData);
	}

}
