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

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * A utility class for converting between jaxb annotated objects and xml.
 */
public class EntityMarshallingUtils {

	private EntityMarshallingUtils() {
	}

	/**
	 * @param <T>
	 *            the type we want to convert our xml into
	 * @param c
	 *            the class of the parameterized type
	 * @param xml
	 *            the instance xml description
	 * @return a deserialization of the xml into an object of type T of class Class<T>
	 * @throws javax.xml.bind.JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T marshal(Class<T> c, String xml) throws JAXBException {
		T res;

		if (c == xml.getClass()) {
			res = (T) xml;
		}

		else {
			JAXBContext ctx = JAXBContext.newInstance(c);
			Unmarshaller marshaller = ctx.createUnmarshaller();
			res = (T) marshaller.unmarshal(new StringReader(xml));
		}

		return res;
	}

	/**
	 * @param <T>
	 *            the type to serialize
	 * @param c
	 *            the class of the type to serialize
	 * @param o
	 *            the instance containing the data to serialize
	 * @return a string representation of the data.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> String unmarshal(Class<T> c, Object o) {

		String entityString = "";
		JAXBContext ctx;
		try {
			ctx = JAXBContext.newInstance(c);

			Marshaller marshaller = ctx.createMarshaller();
			StringWriter entityXml = new StringWriter();
			marshaller.marshal(o, entityXml);

			entityString = entityXml.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		return entityString;
	}
}
