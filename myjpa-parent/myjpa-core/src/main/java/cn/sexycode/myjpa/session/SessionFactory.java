package cn.sexycode.myjpa.session;

import cn.sexycode.util.core.factory.BeanFactoryUtil;
import cn.sexycode.util.core.object.ObjectUtils;
import cn.sexycode.util.core.service.ServiceRegistry;

import javax.naming.Referenceable;
import javax.persistence.EntityManagerFactory;
import java.io.Closeable;
import java.io.Serializable;

/**
 *
 * @author qzz
 */
public interface SessionFactory extends EntityManagerFactory, Referenceable, Serializable,
        Closeable {
    default ServiceRegistry getServiceRegistry(){
        return BeanFactoryUtil.getBeanFactory().getBean(ServiceRegistry.class);
    }
}
