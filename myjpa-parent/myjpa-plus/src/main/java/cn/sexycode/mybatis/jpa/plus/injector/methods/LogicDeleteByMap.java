package cn.sexycode.mybatis.jpa.plus.injector.methods;

import cn.sexycode.mybatis.jpa.plus.injector.AbstractLogicMethod;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * <p>
 * 根据 map 条件删除
 * </p>
 *
 * @author hubin
 * @since 2018-06-13
 */
public class LogicDeleteByMap extends AbstractLogicMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_BY_MAP;
        if (tableInfo.isLogicDelete()) {
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo), sqlWhereByMap(tableInfo));
        } else {
            sqlMethod = SqlMethod.DELETE_BY_MAP;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), this.sqlWhereByMap(tableInfo));
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Map.class);
        return addUpdateMappedStatement(mapperClass, Map.class, sqlMethod.getMethod(), sqlSource);
    }
}
