package cn.sexycode.mybatis.jpa.plus.injector.methods;

import cn.sexycode.mybatis.jpa.plus.injector.AbstractLogicMethod;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 * 根据 ID 删除
 * </p>
 *
 * @author hubin
 * @since 2018-06-13
 */
public class LogicDeleteById extends AbstractLogicMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_BY_ID;
        if (tableInfo.isLogicDelete()) {
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo),
                tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                tableInfo.getLogicDeleteSql(true, false));
        } else {
            sqlMethod = SqlMethod.DELETE_BY_ID;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                tableInfo.getKeyProperty());
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
        return addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
    }
}
