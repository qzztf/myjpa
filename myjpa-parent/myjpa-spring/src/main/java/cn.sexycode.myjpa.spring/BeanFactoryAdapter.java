package cn.sexycode.myjpa.spring;

import cn.sexycode.util.core.factory.BeanFactory;

public class BeanFactoryAdapter implements BeanFactory {
    private org.springframework.beans.factory.BeanFactory beanFactory;

    public BeanFactoryAdapter(org.springframework.beans.factory.BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        if (beanFactory.containsBean("")) {
            //TODO 配置默认bean
        }
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
}
