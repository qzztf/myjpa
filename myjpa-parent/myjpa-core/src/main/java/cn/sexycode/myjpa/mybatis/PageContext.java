package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.binding.MapperMethod;

/**
 * 分页上下文信息
 * @author qzz
 */
public class PageContext {
    private static ThreadLocal<MapperMethod> method = new ThreadLocal<>();

    private static ThreadLocal<Boolean> count = new ThreadLocal<>();

    public static void setMethod(MapperMethod mapperMethod){
        method.set(mapperMethod);
    }

    public static MapperMethod getMethod() {
        return method.get();
    }

    public static Boolean isCount() {
        return count.get();
    }

    public static void clear() {
        method.remove();
        count.remove();
    }
}
