/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: PropertyInterceptor.java 7700 2005-07-30 05:02:47Z oneovthafew $
package org.hibernate.orm.test.interceptor;
import java.io.Serializable;
import java.util.Calendar;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class PropertyInterceptor extends EmptyInterceptor {

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		currentState[1] = Calendar.getInstance();
		return true;
	}

	@Override
	public boolean onSave(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) {
		state[2] = Calendar.getInstance();
		return true;
	}

}
