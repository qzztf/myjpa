package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @author Lasse Voss
 */
public class MyjpaMapperMethod extends MapperMethod {

    private final SqlCommand command;

    private final MethodSignature method;

    public MyjpaMapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        super(mapperInterface, method, config);
        this.command = new SqlCommand(config, mapperInterface, method);
        this.method = new MethodSignature(config, mapperInterface, method);
    }

    @Override
    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        if (command.getType() == SqlCommandType.SELECT) {
            if (isPage()) {
                //自定义的分页方式
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.selectOne(command.getName(), param);
            } else {
                super.execute(sqlSession, args);
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

    private boolean isPage() {
        return false;
    }

}
