package cn.sexycode.myjpa.spring;

import cn.sexycode.sql.dialect.DialectFactory;
import cn.sexycode.sql.dialect.DialectFactoryImpl;
import cn.sexycode.sql.dialect.StandardDialectResolver;
import cn.sexycode.util.core.cls.classloading.ClassLoaderService;
import cn.sexycode.util.core.cls.classloading.ClassLoaderServiceImpl;
import cn.sexycode.util.core.factory.BeanFactory;
import cn.sexycode.util.core.factory.selector.StrategySelectorImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;

public class BeanFactoryAdapter implements BeanFactory, BeanDefinitionRegistryPostProcessor {
    private org.springframework.beans.factory.BeanFactory beanFactory;

    public BeanFactoryAdapter(org.springframework.beans.factory.BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public <T> T getBean(String s) {
        return (T) beanFactory.getBean(s);
    }

    @Override
    public <T> T getBean(String s, Class<T> aClass) {
        return beanFactory.getBean(s,aClass);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (!registry.containsBeanDefinition(Beans.CLASS_LOADER_SERVICE)) {
            registry.registerBeanDefinition(Beans.CLASS_LOADER_SERVICE, BeanDefinitionBuilder.genericBeanDefinition(
                    ClassLoaderServiceImpl.class).getBeanDefinition());
        }
        if (!registry.containsBeanDefinition(Beans.DIALECT_FACTORY)) {
            registry.registerBeanDefinition(Beans.DIALECT_FACTORY, BeanDefinitionBuilder.genericBeanDefinition(
                    DialectFactory.class,()-> new DialectFactoryImpl(new StandardDialectResolver(),new StrategySelectorImpl(beanFactory.getBean(Beans.CLASS_LOADER_SERVICE, ClassLoaderService.class)))).getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
