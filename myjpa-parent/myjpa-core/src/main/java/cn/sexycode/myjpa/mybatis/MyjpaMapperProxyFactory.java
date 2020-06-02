package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lasse Voss
 */
public class MyjpaMapperProxyFactory<T> extends MapperProxyFactory<T> {

    public MyjpaMapperProxyFactory(Class<T> mapperInterface) {
        super(mapperInterface);
    }

    @Override
    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MyjpaMapperProxy<T>(sqlSession, getMapperInterface(),
                getMethodCache().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, t1 -> (MyjpaMapperMethod) t1.getValue())));
        return newInstance(mapperProxy);
    }
}
