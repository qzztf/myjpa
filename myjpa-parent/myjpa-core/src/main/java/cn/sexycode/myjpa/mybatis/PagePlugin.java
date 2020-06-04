package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;

import java.util.List;

/**
 * @author qzz
 */
public interface PagePlugin extends Interceptor {
    /**
     * 是否分页
     * @return true 是, false 否
     * @param returnType 返回值类型
     * @param args 查询参数
     */
    default boolean isPage(Class<?> returnType, Object[] args) {
        return false;
    }

    @Override
    default Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * 将查询结果包装成page对象
     * @param args
     * @param returnType
     * @param result
     * @param <E>
     * @return
     */
    default  <E> Object warpPage(Object[] args, Class<?> returnType, List<E> result){
        return result;
    }

    /**
     * 取出Page对象中的数据
     * @param args
     * @param returnType
     * @param result
     * @param <E>
     * @return
     */
    default  <E> List<E> unWarpPage(Object[] args, Class<?> returnType, Object result){
        return (List<E>) result;
    }
}
