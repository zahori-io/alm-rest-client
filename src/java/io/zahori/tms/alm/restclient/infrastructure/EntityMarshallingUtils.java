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
 * The type Entity marshalling utils.
 */
public class EntityMarshallingUtils {

	/**
	 * Instantiates a new Entity marshalling utils.
	 */
	private EntityMarshallingUtils() {
	}

	/**
	 * Marshal t.
	 *
	 * @param <T> the type parameter
	 * @param c   the c
	 * @param xml the xml
	 * @return the t
	 * @throws JAXBException the jaxb exception
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
	 * Unmarshal string.
	 *
	 * @param <T> the type parameter
	 * @param c   the c
	 * @param o   the o
	 * @return the string
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
