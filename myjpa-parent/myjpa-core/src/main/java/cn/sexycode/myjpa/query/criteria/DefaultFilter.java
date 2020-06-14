package cn.sexycode.myjpa.query.criteria;

import cn.sexycode.util.core.str.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author qzz
 */
public class DefaultFilter implements Filter {

    /**
     * 排序字段
     */
    private List<FieldSort> fieldSortList = new ArrayList<FieldSort>();
    /**
     * 字段参数构建列表
     */
    private Map<String, Object> params = new LinkedHashMap<String, Object>();
    /**
     * 字段参数组合关系列表
     */
    private FieldLogic fieldLogic = new DefaultFieldLogic();

    /**
     * 查询字段
     */
    private String sqlSelect;


    public DefaultFilter() {
    }

    public DefaultFilter(FieldLogic fieldLogic) {
        this.fieldLogic = fieldLogic;
        //initParams(fieldLogic);
    }

    @Override
    public Map<String, Object> getParams() {
        initParams(this.fieldLogic);
        return params;
    }

    @Override
    public FieldLogic getFieldLogic() {
        return fieldLogic;
    }

    public void setFieldLogic(FieldLogic fieldLogic) {
        this.fieldLogic = fieldLogic;
        //initParams(fieldLogic);
    }

    //初始化参数
    private void initParams(FieldLogic fedLog) {
        List<WhereClause> list = fedLog.getWhereClauses();
        for (WhereClause clause : list) {
            if (clause instanceof Field) {
                Field field = (Field) clause;
                if (OP.IS_NULL.equals(field.getCompare())
                        || OP.NOTNULL.equals(field.getCompare())) {
                    continue;
                }
                //如果查询字段包含数据库别名，参数设置去掉别名
                String fileNameString = field.getField();
                if (fileNameString.contains(".")) {
                    fileNameString = fileNameString.substring(fileNameString.indexOf(".") + 1);
                }
                this.params.put(fileNameString, field.getValue());
            } else if (clause instanceof FieldLogic) {
                FieldLogic fdTemp = (FieldLogic) clause;
                initParams(fdTemp);
            }
        }
    }

    @Override
    public List<FieldSort> getFieldSortList() {
        return fieldSortList;
    }

    public void setFieldSortList(List<FieldSort> fieldSortList) {
        this.fieldSortList = fieldSortList;
    }


    /**
     * 添加排序配置。
     *
     * @param orderField 排序字段
     * @param orderSeq   排序
     */
    public Filter addFieldSort(String orderField, String orderSeq) {
        fieldSortList.add(new DefaultFieldSort(orderField, Direction.fromString(orderSeq)));
        return this;
    }

    @Override
    public Filter addFilter(String name, Object obj, OP queryType) {
        fieldLogic.getWhereClauses().add(new DefaultField(name, queryType, obj));
        return this;
    }

    @Override
    public Filter eq(String name, Object obj) {
        fieldLogic.eq(name, obj);
        return this;
    }

    @Override
    public Filter between(String name, List obj) {
        fieldLogic.between(name, obj);
        return this;
    }

    @Override
    public Filter le(String name, Object obj) {
        fieldLogic.le(name, obj);
        return this;
    }

    @Override
    public Filter lt(String name, Object obj) {
        fieldLogic.lt(name, obj);
        return this;
    }

    @Override
    public Filter ge(String name, Object obj) {
        fieldLogic.ge(name, obj);
        return this;
    }

    @Override
    public Filter gt(String name, Object obj) {
        fieldLogic.gt(name, obj);
        return this;
    }

    @Override
    public Filter addParamsFilter(String key, Object obj) {
        this.params.put(key, obj);
        return this;
    }


    @Override
    public Filter setSqlSelect(String sqlSelect) {
        if (StringUtils.isNotEmpty(sqlSelect)) {
            this.sqlSelect = sqlSelect;
        }
        return this;
    }

    /**
     * <p>
     * 使用字符串数组封装sqlSelect
     * </p>
     *
     * @param columns 字段
     * @return
     */
    @Override
    public Filter setSqlSelect(String... columns) {
        StringBuilder builder = new StringBuilder();
        for (String column : columns) {
            if (StringUtils.isNotEmpty(column)) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(column);
            }
        }
        this.sqlSelect = builder.toString();
        return this;
    }

    @Override
    public String getSqlSelect() {
        return sqlSelect;
    }
}