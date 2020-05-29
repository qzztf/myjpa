package cn.sexycode.myjpa.binding;

import cn.sexycode.util.core.factory.BeanFactoryUtil;
import cn.sexycode.util.core.service.Service;
import cn.sexycode.util.core.service.ServiceRegistry;
import cn.sexycode.util.core.service.StandardServiceRegistry;

public class StandardServiceRegistryImpl implements StandardServiceRegistry {
    /**
     * Retrieve this registry's parent registry.
     *
     * @return The parent registry.  May be null.
     */
    @Override
    public ServiceRegistry getParentServiceRegistry() {
        return null;
    }

    /**
     * Retrieve a service by role.  If service is not found, but a {@link org.hibernate.service.spi.ServiceInitiator} is
     * registered for this service role, the service will be initialized and returned.
     * <p/>
     * NOTE: We cannot return {@code <R extends Service<T>>} here because the service might come from the parent...
     *
     * @param serviceRole The service role
     * @return The requested service or null if the service was not found.
     * @throws UnknownServiceException Indicates the service was not known.
     */
    @Override
    public <R extends Service> R getService(Class<R> serviceRole) {
        return BeanFactoryUtil.getBeanFactory().getBean(serviceRole);
    }

    @Override
    public void close() {

    }
}
