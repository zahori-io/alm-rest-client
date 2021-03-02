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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Entities")
@XmlAccessorType(XmlAccessType.FIELD)
public class Entities<T extends Entity> {
	@XmlElementRefs({ @XmlElementRef(name = "Entity"), @XmlElementRef(name = "Entity") })
	private List<T> entities;

	public Entities() {
		this(new ArrayList<T>());
	}

	public Entities(Collection<T> entities) {
		if (entities instanceof List) {
			this.entities = (List<T>) entities;
		} else {
			this.entities = new ArrayList<T>(entities);
		}
	}

	@Override
	public String toString() {
		return "Entities: " + entities;
	}

	public List<T> entities() {
		return entities;
	}

	public void entities(List<T> entities) {
		this.entities = entities;
	}

	public void addEntity(T entity) {
		entities.add(entity);
	}
}
