package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @author Lasse Voss
 */
public class MyjpaMapperMethod extends MapperMethod {

    private final SqlCommand command;

    private final MethodSignature method;

    private Configuration configuration;

    private PagePlugin pagePlugin;

    public MyjpaMapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        super(mapperInterface, method, config);
        this.command = new SqlCommand(config, mapperInterface, method);
        this.method = new MethodSignature(config, mapperInterface, method);
        this.configuration = config;
    }

    @Override
    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        if (command.getType() == SqlCommandType.SELECT) {
            if (isPage(method.getReturnType(), args)) {
                //自定义的分页方式
                result = executeForPage(sqlSession, args);
            } else {
                result = super.execute(sqlSession, args);
            }
        } else {
            super.execute(sqlSession, args);
        }
        if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
            throw new BindingException("Mapper method '" + command.getName()
                    + " attempted to return null from a method with a primitive return type (" + method.getReturnType()
                    + ").");
        }
        return result;
    }

    protected  <E> Object executeForPage(SqlSession sqlSession, Object[] args) {
        PageContext.setMethod(this);
        List<E> result;
        Object param = method.convertArgsToSqlCommandParam(args);
        if (method.hasRowBounds()) {
            RowBounds rowBounds = method.extractRowBounds(args);
            result = sqlSession.<E>selectList(command.getName(), param, rowBounds);
        } else {
            result = sqlSession.<E>selectList(command.getName(), param);
        }
        if (pagePlugin != null){
            return pagePlugin.warpPage(args, method.getReturnType(), result);
        }
        return result;
    }

    public boolean isPage(Class<?> returnType, Object[] args) {
        Optional<Interceptor> interceptor = configuration.getInterceptors().stream()
                .filter((i) -> PagePlugin.class.isAssignableFrom(i.getClass())).findFirst();
        boolean page = interceptor.isPresent() && ((PagePlugin) interceptor.get()).isPage(returnType, args);
        if (page) {
            pagePlugin = (PagePlugin) interceptor.get();
        }
        return page;
    }

    public SqlCommand getCommand() {
        return command;
    }

    public MethodSignature getMethod() {
        return method;
    }
}
