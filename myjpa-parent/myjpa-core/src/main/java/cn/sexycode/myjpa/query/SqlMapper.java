package cn.sexycode.myjpa.query;

import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;
import java.util.Map;

/**
 * 用来直接执行sql语句
 * @author qzz
 */
public interface SqlMapper {
    @InsertProvider(type = CriteriaProvider.class, method = "execute")
    Integer insert(String statement);

    Integer delete(String statement);

    Integer update(String statement);

    List<Map<String, Object>> selectList(String statement);

    Object selectOne(String statement);
}
