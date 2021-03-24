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
 * The type Constants.
 */
public class Constants {
	/**
	 * Instantiates a new Constants.
	 */
	private Constants() {
	}

	/**
	 * Is versioned boolean.
	 *
	 * @param entityType the entity type
	 * @param domain     the domain
	 * @param project    the project
	 * @return the boolean
	 * @throws Exception the exception
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

	/**
	 * Generate field xml string.
	 *
	 * @param field the field
	 * @param value the value
	 * @return the string
	 */
	public static String generateFieldXml(String field, String value) {
		return "<Field Name=\"" + field + "\"><Value>" + value + "</Value></Field>";
	}

	/**
	 * The constant entityToPostName.
	 */
	public static final String entityToPostName = "req" + Double.toHexString(Math.random());
	/**
	 * The constant entityToPostFieldName.
	 */
	public static final String entityToPostFieldName = "type-id";
	/**
	 * The constant entityToPostFieldValue.
	 */
	public static final String entityToPostFieldValue = "1";
	/**
	 * The constant entityToPostFormat.
	 */
	public static final String entityToPostFormat = "<Entity Type=\"requirement\">" + "<Fields>"
			+ Constants.generateFieldXml("%s", "%s") + Constants.generateFieldXml("%s", "%s") + "</Fields>"
			+ "</Entity>";

	/**
	 * The constant entityToPostXml.
	 */
	public static final String entityToPostXml = String.format(entityToPostFormat, "name", entityToPostName,
			entityToPostFieldName, entityToPostFieldValue);

	/**
	 * The constant entityToPostFieldXml.
	 */
	public static final CharSequence entityToPostFieldXml = generateFieldXml(Constants.entityToPostFieldName,
			Constants.entityToPostFieldValue);

}
