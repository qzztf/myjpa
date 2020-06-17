package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPathImpl<X> extends AbstractExpressionImpl<X> implements Path<X>, Serializable {

        private final Expression<Class<? extends X>> typeExpression;
        private Map<String,Path> attributePathRegistry;

        public AbstractPathImpl(
                CriteriaBuilder criteriaBuilder,
                Class<X> javaType) {
            super( criteriaBuilder, javaType );
            this.typeExpression =  new PathTypeExpression( criteriaBuilder(), getJavaType(), this );
        }

        @Override
        @SuppressWarnings({ "unchecked" })
        public Expression<Class<? extends X>> type() {
            return typeExpression;
        }

        protected final Path resolveCachedAttributePath(String attributeName) {
            return attributePathRegistry == null
                    ? null
                    : attributePathRegistry.get( attributeName );
        }

        protected final void registerAttributePath(String attributeName, Path path) {
            if ( attributePathRegistry == null ) {
                attributePathRegistry = new HashMap<String,Path>();
            }
            attributePathRegistry.put( attributeName, path );
        }

        @Override
        @SuppressWarnings({ "unchecked" })
        public <Y> Path<Y> get(SingularAttribute<? super X, Y> attribute) {


            SingularAttributePath<Y> path = (SingularAttributePath<Y>) resolveCachedAttributePath( attribute.getName() );
            if ( path == null ) {
                path = new SingularAttributePath<Y>(
                        criteriaBuilder(),
                        attribute.getJavaType(),
                        attribute
                );
                registerAttributePath( attribute.getName(), path );
            }
            return path;
        }

        @Override
        public <E, C extends Collection<E>> Expression<C> get(PluralAttribute<X, C, E> attribute) {
            return null;
        }

        @Override
        public <K, V, M extends Map<K, V>> Expression<M> get(MapAttribute<X, K, V> attribute) {
            return null;
        }

        @Override
        public <Y> Path<Y> get(String attributeName) {

            Attribute attribute = locateAttribute( attributeName );

            return get( (SingularAttribute<X,Y>) attribute );
        }

        /**
         * Get the attribute by name from the underlying model.  This allows subclasses to
         * define exactly how the attribute is derived.
         *
         * @param attributeName The name of the attribute to locate
         *
         * @return The attribute; should never return null.
         *
         * @throws IllegalArgumentException If no such attribute exists
         */
        protected  final Attribute locateAttribute(String attributeName) {
            final Attribute attribute = locateAttributeInternal( attributeName );
            if ( attribute == null ) {
                String message = "Unable to resolve attribute [" + attributeName + "] against path";
                throw new IllegalArgumentException(message);
            }
            return attribute;
        }

        /**
         * Get the attribute by name from the underlying model.  This allows subclasses to
         * define exactly how the attribute is derived.  Called from {@link #locateAttribute}
         * which also applies nullness checking for proper error reporting.
         *
         * @param attributeName The name of the attribute to locate
         *
         * @return The attribute; may be null.
         *
         * @throws IllegalArgumentException If no such attribute exists
         */
        protected abstract Attribute locateAttributeInternal(String attributeName);

}
