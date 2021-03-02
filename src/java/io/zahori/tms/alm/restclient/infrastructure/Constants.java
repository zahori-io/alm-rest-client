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

/**
 *
 * These constants are used throughout the code to set the server to work with.
 * To execute this code, change these settings to fit those of your server.
 */
public class Constants {
	private Constants() {
	}

	public static final String HOST = "alm1.produban.gs.corp";
	public static final String PORT = "80";

	public static final String USERNAME = "XI320654";
	public static final String PASSWORD = "Asturias10";

	public static final String DOMAIN = "SW_COMPONENTS";
	public static final String PROJECT = "QTP_DESARROLLO";

	/**
	 * Supports running tests correctly on both versioned and non-versioned
	 * projects.
	 * 
	 * @return true if entities of entityType support versioning
	 */
	public static boolean isVersioned(String entityType, final String domain, final String project) throws Exception {

		RestConnector con = RestConnector.getInstance();
		String descriptorUrl = con.buildUrl("rest/domains/" + domain + "/projects/" + project
				+ "/customization/entities/" + entityType);

		String descriptorXml = con.httpGet(descriptorUrl, null, null).toString();
		EntityDescriptor descriptor = EntityMarshallingUtils.marshal(EntityDescriptor.class, descriptorXml);

		boolean isVersioned = descriptor.getSupportsVC().getValue();

		return isVersioned;
	}

	public static String generateFieldXml(String field, String value) {
		return "<Field Name=\"" + field + "\"><Value>" + value + "</Value></Field>";
	}

	/**
	 * This string used to create new "requirement" type entities.
	 */
	public static final String entityToPostName = "req" + Double.toHexString(Math.random());
	public static final String entityToPostFieldName = "type-id";
	public static final String entityToPostFieldValue = "1";
	public static final String entityToPostFormat = "<Entity Type=\"requirement\">" + "<Fields>"
			+ Constants.generateFieldXml("%s", "%s") + Constants.generateFieldXml("%s", "%s") + "</Fields>"
			+ "</Entity>";

	public static final String entityToPostXml = String.format(entityToPostFormat, "name", entityToPostName,
			entityToPostFieldName, entityToPostFieldValue);

	public static final CharSequence entityToPostFieldXml = generateFieldXml(Constants.entityToPostFieldName,
			Constants.entityToPostFieldValue);

}
