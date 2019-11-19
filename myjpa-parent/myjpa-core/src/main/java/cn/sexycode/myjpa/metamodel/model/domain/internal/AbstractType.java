package cn.sexycode.myjpa.metamodel.model.domain.internal;

import cn.sexycode.myjpa.metamodel.model.domain.spi.DomainTypeDescriptor;

import javax.persistence.metamodel.Type;
import java.io.Serializable;

/**
 * Defines commonality for the JPA {@link Type} hierarchy of interfaces.
 *
 * @author Steve Ebersole
 * @author Brad Koehn
 */
public abstract class AbstractType<X> implements DomainTypeDescriptor<X>, Serializable {
    private final Class<X> javaType;

    private final String typeName;

    /**
     * Instantiates the type based on the given Java type.
     *
     * @param javaType The Java type of the JPA model type.
     */
    protected AbstractType(Class<X> javaType) {
        this(javaType, javaType != null ? javaType.getName() : null);
    }

    /**
     * Instantiates the type based on the given Java type.
     */
    protected AbstractType(Class<X> javaType, String typeName) {
        this.javaType = javaType;
        this.typeName = typeName == null ? "unknown" : typeName;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * IMPL NOTE : The Hibernate version may return {@code null} here in the case of either dynamic models or
     * entity classes mapped multiple times using entity-name.  In these cases, the {@link #getTypeName()} value
     * should be used.
     */
    @Override
    public Class<X> getJavaType() {
        return javaType;
    }

    /**
     * Obtains the type name.  See notes on {@link #getJavaType()} for details
     *
     * @return The type name
     */
    public String getTypeName() {
        return typeName;
    }
}
