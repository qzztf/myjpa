package cn.sexycode.mybatis.jpa.service;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractServiceRegistry implements ServiceRegistry {
    private ConcurrentHashMap<Class, Service> services = new ConcurrentHashMap<>(10);

    @Override
    public ServiceRegistry getParentServiceRegistry() {
        return null;
    }

    @Override
    public <R extends Service> R getService(Class<R> serviceRole) {
        return (R) services.get(serviceRole);
    }

    @Override
    public void close() {

    }
}
