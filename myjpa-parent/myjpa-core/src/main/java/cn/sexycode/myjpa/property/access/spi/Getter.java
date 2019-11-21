package cn.sexycode.myjpa.property.access.spi;

import cn.sexycode.myjpa.session.Session;

import java.io.Serializable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * The contract for getting value for a persistent property from its container/owner
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface Getter extends Serializable {
	/**
	 * Get the property value from the given owner instance.
	 *
	 * @param owner The instance containing the property value to be retrieved.
	 *
	 * @return The extracted value.
	 *
	 * @throws org.hibernate.HibernateException
	 */
	Object get(Object owner);

	/**
	 * Get the property value from the given owner instance.
	 *
	 * @param owner The instance containing the value to be retrieved.
	 * @param mergeMap a map of merged persistent instances to detached instances
	 * @param session The session from which this request originated.
	 *
	 * @return The extracted value.
	 *
	 * @throws cn.sexycode.myjpa.MyjpaException
	 */
	Object getForInsert(Object owner, Map mergeMap, Session session);

	/**
	 * Retrieve the declared Java type
	 *
	 * @return The declared java type.
	 */
	Class getReturnType();

	/**
	 * Retrieve the member to which this property maps.  This might be the
	 * field or it might be the getter method.
	 * <p/>
	 * Optional operation (may return {@code null})
	 *
	 * @return The mapped member, or {@code null}.
	 */
	Member getMember();

	/**
	 * Retrieve the getter-method name.
	 * <p/>
	 * Optional operation (may return {@code null})
	 *
	 * @return The name of the getter method, or {@code null}.
	 */
	String getMethodName();

	/**
	 * Retrieve the getter-method.
	 * <p/>
	 * Optional operation (may return {@code null})
	 *
	 * @return The getter method, or {@code null}.
	 */
	Method getMethod();
}
