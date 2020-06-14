package cn.sexycode.myjpa.query.criteria;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface Filter {

    /**
     * 返回字段组合查询逻辑
     *
     * @return
     */
    FieldLogic getFieldLogic();

    /**
     * 返回组合的参数映射
     *
     * @return
     */
    Map<String, Object> getParams();

    /**
     * 返回字段排序列表
     *
     * @return
     */
    List<FieldSort> getFieldSortList();

    /**
     * 添加自定义过滤条件（用于自动组装条件：whereSql）
     *
     * @param name
     * @param obj
     * @param queryType
     */
    Filter addFilter(String name, Object obj, OP queryType);

    Filter eq(String name, Object obj);

    Filter between(String name, List obj);

    Filter le(String name, Object obj);

    Filter lt(String name, Object obj);

    Filter ge(String name, Object obj);

    Filter gt(String name, Object obj);

    /**
     * 添加自定义过滤条件（用于手动组装条件，在MAPPING文件判断用的参数）
     *
     * @param key
     * @param obj
     */
    Filter addParamsFilter(String key, Object obj);

    Filter setSqlSelect(String sqlSelect);

    Filter setSqlSelect(String... columns);

    String getSqlSelect();
}