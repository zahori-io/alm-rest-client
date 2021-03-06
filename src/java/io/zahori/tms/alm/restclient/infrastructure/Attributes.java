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

//
//This file was generated by the JavaTM Architecture for XML Binding(JAXB)
//Reference Implementation, vhudson-jaxb-ri-2.1-456
//See http://www.oracle.com/technetwork/articles/javase/index-140168.html
//Any modifications to this file will be lost upon recompilation of the
//source schema.
//Generated on: 2010.05.31 at 03:35:17 PM IDT
//

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The type Attributes.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "attribute" })
@XmlRootElement(name = "Attributes")
public class Attributes {

	/**
	 * The Attribute.
	 */
	@XmlElement(name = "Attribute")
	protected List<Attribute> attribute;

	/**
	 * Gets attribute.
	 *
	 * @return the attribute
	 */
	public List<Attribute> getAttribute() {
		if (attribute == null) {
			attribute = new ArrayList<Attribute>();
		}
		return this.attribute;
	}

}
