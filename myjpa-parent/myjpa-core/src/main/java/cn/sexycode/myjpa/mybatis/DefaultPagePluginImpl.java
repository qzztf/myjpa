package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * 默认分页插件
 *
 * @author qzz
 */
@Intercepts(
        { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class DefaultPagePluginImpl implements PagePlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPagePluginImpl.class);

    protected  static boolean springDataPage = false;

    protected  static Class<?> springDataPageableClass = null;

    protected  static Class<?> springDataPageClass = null;

    static {
        try {
            springDataPageableClass = Class.forName("org.springframework.data.domain.Pageable");
            springDataPageClass = Class.forName("org.springframework.data.domain.Page");
            springDataPage = true;
        } catch (ClassNotFoundException e) {
            springDataPage = false;
            springDataPageableClass = null;
            LOGGER.debug("未能加载Spring data");
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public boolean isPage(Class<?> returnType, Object[] args) {
        return springDataPage && Stream.of(args).anyMatch(a -> springDataPageableClass.isAssignableFrom(a.getClass()))
                && springDataPageClass.isAssignableFrom(returnType);
    }

    @Override
    public <E> Object warpPage(Object[] args, Class<?> returnType, List<E> result) {
        return result;
    }

}
