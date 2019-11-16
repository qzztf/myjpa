package cn.sexycode.myjpa;

import cn.sexycode.sql.dialect.DialectFactory;
import cn.sexycode.sql.dialect.DialectFactoryImpl;
import cn.sexycode.sql.dialect.StandardDialectResolver;
import cn.sexycode.util.core.cls.classloading.ClassLoaderService;
import cn.sexycode.util.core.cls.classloading.ClassLoaderServiceImpl;
import cn.sexycode.util.core.factory.BeanFactory;
import cn.sexycode.util.core.factory.selector.StrategySelectorImpl;
import cn.sexycode.util.core.service.Service;
import cn.sexycode.util.core.service.ServiceRegistry;
import cn.sexycode.util.core.service.StandardServiceRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultBeanFactory implements BeanFactory {

    Map<Class, Object> beans = new HashMap<Class, Object>() {{
        put(StandardServiceRegistry.class, new StandardServiceRegistry() {
            private Map<Class, Service> serviceMap = new HashMap<Class, Service>() {{
                put(ClassLoaderService.class, new ClassLoaderServiceImpl());
                put(DialectFactory.class, new DialectFactoryImpl(new StandardDialectResolver(),
                        new StrategySelectorImpl((ClassLoaderService) get(ClassLoaderService.class))));
            }};

            @Override
            public ServiceRegistry getParentServiceRegistry() {
                return null;
            }

            @Override
            public <R extends Service> R getService(Class<R> aClass) {
                return (R) serviceMap.get(aClass);
            }

            @Override
            public void close() {

            }
        });
    }};

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}
