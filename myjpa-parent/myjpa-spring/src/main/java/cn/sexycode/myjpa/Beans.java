package cn.sexycode.myjpa.spring;

/**
 * 定义一些常量Bean的名称
 * @author qzz
 */
public interface Beans {
    /**
     * classloader
     */
    String CLASS_LOADER_SERVICE = "myjpa.internal.classLoaderService";
    /**
     * dialect工厂
     */
    String DIALECT_FACTORY = "myjpa.internal.dialectFactory";
    /**
     * standardServiceRegistry
     */
    String STANDARD_SERVICE_REGISTRY = "myjpa.internal.standardServiceRegistry";
    /**
     * queryFactory
     */
    String QUERY_FACTORY = "myjpa.internal.queryFactory";
}
