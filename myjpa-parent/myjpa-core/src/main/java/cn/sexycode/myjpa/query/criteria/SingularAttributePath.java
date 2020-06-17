package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.*;
import java.io.Serializable;

/**
 * Models a path for a {@link SingularAttribute} generally obtained from a
 * {@link javax.persistence.criteria.Path#get(SingularAttribute)} call
 *
 * @author Steve Ebersole
 */
public class SingularAttributePath<X> extends AbstractPathImpl<X> implements Serializable {
    private final SingularAttribute<?, X> attribute;

    private final ManagedType<X> managedType;

    public SingularAttributePath(CriteriaBuilder criteriaBuilder, Class<X> javaType,
            SingularAttribute<?, X> attribute) {
        super(criteriaBuilder, javaType);
        this.attribute = attribute;
        this.managedType = resolveManagedType(attribute);
    }

    private ManagedType<X> resolveManagedType(SingularAttribute<?, X> attribute) {
        if (Attribute.PersistentAttributeType.BASIC == attribute.getPersistentAttributeType()) {
            return null;
        } else if (Attribute.PersistentAttributeType.EMBEDDED == attribute.getPersistentAttributeType()) {
            return (EmbeddableType<X>) attribute.getType();
        } else {
            return (IdentifiableType<X>) attribute.getType();
        }
    }

    @Override
    public Bindable<X> getModel() {
        return attribute;
    }

    /**
     * Return the parent "node" in the path or null if no parent.
     *
     * @return parent
     */
    @Override
    public Path<?> getParentPath() {
        return null;
    }

    @Override
    protected Attribute locateAttributeInternal(String attributeName) {
        final Attribute attribute = managedType.getAttribute(attributeName);
        // ManagedType.locateAttribute should throw exception rather than return
        // null, but just to be safe...
        if (attribute == null) {
            throw new IllegalArgumentException("Could not resolve attribute named " + attributeName);
        }
        return attribute;
    }

    /**
     * 返回条件的sql片段
     *
     * @return SQL片段
     */
    @Override
    public String getSql() {
        return attribute.getName();
    }
}
