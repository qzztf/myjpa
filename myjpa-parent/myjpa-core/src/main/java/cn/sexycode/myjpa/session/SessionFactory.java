package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.metamodel.spi.MetamodelImplementor;
import cn.sexycode.myjpa.sql.type.TypeResolver;
import org.apache.ibatis.session.Configuration;

import cn.sexycode.util.core.factory.BeanFactoryUtil;
import cn.sexycode.util.core.object.ObjectUtils;
import cn.sexycode.util.core.service.ServiceRegistry;

import javax.naming.Referenceable;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Metamodel;
import java.io.Closeable;
import java.io.Serializable;

/**
 *
 * @author qzz
 */
public interface SessionFactory extends EntityManagerFactory, Referenceable, Serializable,
        Closeable {
    Configuration getConfiguration();
    default ServiceRegistry getServiceRegistry(){
        return BeanFactoryUtil.getBeanFactory().getBean(ServiceRegistry.class);
    }

    default TypeResolver getTypeResolver(){
        return new TypeResolver();
    }

    /**
     * Return an instance of <code>Metamodel</code> interface for access to the
     * metamodel of the persistence unit.
     *
     * @return Metamodel instance
     * @throws IllegalStateException if the entity manager factory
     *                               has been closed
     * @since Java Persistence 2.0
     */
    @Override
    MetamodelImplementor getMetamodel();
}
