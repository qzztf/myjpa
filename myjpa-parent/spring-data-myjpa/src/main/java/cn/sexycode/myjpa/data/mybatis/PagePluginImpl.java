package cn.sexycode.myjpa.data.mybatis;

import cn.sexycode.myjpa.mybatis.DefaultPagePluginImpl;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.sql.Connection;
import java.util.List;

/**
 * @author qzz
 */
@Intercepts(
        { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PagePluginImpl extends DefaultPagePluginImpl {
    @Override
    public <E> Object warpPage(Object[] args, Class<?> returnType, List<E> result) {
        if (springDataPage) {
            return new PageImpl<>(result);
        }
        return super.warpPage(args, returnType, result);
    }

    /**
     * 取出Page对象中的数据
     *
     * @param args
     * @param returnType
     * @param result
     * @return
     */
    @Override
    public <E> List<E> unWarpPage(Object[] args, Class<?> returnType, Object result) {
        if (springDataPage){
            return ((Page) result).getContent();
        }
        return super.unWarpPage(args, returnType, result);
    }
}
