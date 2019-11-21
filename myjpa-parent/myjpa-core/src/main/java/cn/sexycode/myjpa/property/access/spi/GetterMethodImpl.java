package cn.sexycode.myjpa.property.access.spi;

import cn.sexycode.myjpa.session.Session;
import cn.sexycode.util.core.object.ReflectHelper;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * @author Steve Ebersole
 */
public class GetterMethodImpl implements Getter {

	private final Class containerClass;
	private final String propertyName;
	private final Method getterMethod;

	public GetterMethodImpl(Class containerClass, String propertyName, Method getterMethod) {
		this.containerClass = containerClass;
		this.propertyName = propertyName;
		this.getterMethod = getterMethod;
	}

	@Override
	public Object get(Object owner) {
		try {
			return getterMethod.invoke( owner );
		}
		catch (InvocationTargetException ite) {
			throw new PropertyAccessException(
					ite,
					"Exception occurred inside",
					false,
					containerClass,
					propertyName
			);
		}
		catch (IllegalAccessException iae) {
			throw new PropertyAccessException(
					iae,
					"IllegalAccessException occurred while calling",
					false,
					containerClass,
					propertyName
			);
			//cannot occur
		}
		catch (IllegalArgumentException iae) {
//			LOG.illegalPropertyGetterArgument( containerClass.getName(), propertyName );
			throw new PropertyAccessException(
					iae,
					"IllegalArgumentException occurred calling",
					false,
					containerClass,
					propertyName
			);
		}
	}

	@Override
	public Object getForInsert(Object owner, Map mergeMap, Session session) {
		return get( owner );
	}

	@Override
	public Class getReturnType() {
		return getterMethod.getReturnType();
	}

	@Override
	public Member getMember() {
		return getterMethod;
	}

	@Override
	public String getMethodName() {
		return getterMethod.getName();
	}

	@Override
	public Method getMethod() {
		return getterMethod;
	}

	private Object writeReplace() throws ObjectStreamException {
		return new SerialForm( containerClass, propertyName, getterMethod );
	}

	private static class SerialForm implements Serializable {
		private final Class containerClass;
		private final String propertyName;

		private final Class declaringClass;
		private final String methodName;

		private SerialForm(Class containerClass, String propertyName, Method method) {
			this.containerClass = containerClass;
			this.propertyName = propertyName;
			this.declaringClass = method.getDeclaringClass();
			this.methodName = method.getName();
		}

		private Object readResolve() {
			return new GetterMethodImpl( containerClass, propertyName, resolveMethod() );
		}

		@SuppressWarnings("unchecked")
		private Method resolveMethod() {
			try {
				final Method method = declaringClass.getDeclaredMethod( methodName );
				ReflectHelper.ensureAccessibility( method );
				return method;
			}
			catch (NoSuchMethodException e) {
				throw new PropertyAccessSerializationException(
						"Unable to resolve getter method on deserialization : " + declaringClass.getName() + "#" + methodName
				);
			}
		}
	}
}
