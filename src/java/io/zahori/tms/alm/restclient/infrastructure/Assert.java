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
 * The type Assert.
 */
public class Assert {

	/**
	 * Assert true.
	 *
	 * @param errorMessage the error message
	 * @param assertee     the assertee
	 */
	public static final void assertTrue(final String errorMessage, boolean assertee) {
		if (!assertee) {
			throw new RuntimeException(errorMessage);
		}
	}

	/**
	 * Assert equals.
	 *
	 * @param errorMessage  the error message
	 * @param expressionOne the expression one
	 * @param expressionTwo the expression two
	 */
	public static final void assertEquals(final String errorMessage, final String expressionOne,
			final String expressionTwo) {
		if (!expressionOne.equals(expressionTwo)) {
			throw new RuntimeException(errorMessage);
		}
	}

	/**
	 * Assert equals.
	 *
	 * @param errorMessage  the error message
	 * @param expressionOne the expression one
	 * @param expressionTwo the expression two
	 */
	public static void assertEquals(String errorMessage, int expressionOne, int expressionTwo) {
		if (expressionOne != expressionTwo) {
			throw new RuntimeException(errorMessage);
		}
	}

	/**
	 * Assert null.
	 *
	 * @param errorMessage the error message
	 * @param assertee     the assertee
	 */
	public static void assertNull(String errorMessage, String assertee) {
		if (assertee != null) {
			throw new RuntimeException(errorMessage);
		}
	}

	/**
	 * Assert not null.
	 *
	 * @param errorMessage the error message
	 * @param assertee     the assertee
	 */
	public static void assertNotNull(String errorMessage, String assertee) {
		if (assertee == null) {
			throw new RuntimeException(errorMessage);
		}
	}
}
