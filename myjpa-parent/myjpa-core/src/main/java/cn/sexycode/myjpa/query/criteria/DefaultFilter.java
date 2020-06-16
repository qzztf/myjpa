package cn.sexycode.myjpa.query.criteria;

import cn.sexycode.util.core.str.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.*;

/**
 * @author qzz
 */
public class DefaultFilter<T> implements Filter, CriteriaQuery<T> {

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

    private CriteriaBuilder criteriaBuilder;

    private EntityManager entityManager;

    private Class<T> resultClass;

    /**
     * 查询字段
     */
    private String sqlSelect;

    public DefaultFilter() {
    }

    public DefaultFilter(CriteriaBuilder criteriaBuilder, EntityManager entityManager) {
        this.criteriaBuilder = criteriaBuilder;
        this.entityManager = entityManager;
    }

    public DefaultFilter(CriteriaBuilder criteriaBuilder, EntityManager entityManager, Class<T> resultClass) {
        this.criteriaBuilder = criteriaBuilder;
        this.entityManager = entityManager;
        this.resultClass = resultClass;
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
                if (OP.IS_NULL.equals(field.getCompare()) || OP.NOTNULL.equals(field.getCompare())) {
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

    @Override
    public CriteriaQuery<T> select(Selection<? extends T> selection) {
        return null;
    }

    @Override
    public CriteriaQuery<T> multiselect(Selection<?>... selections) {
        return null;
    }

    @Override
    public CriteriaQuery<T> multiselect(List<Selection<?>> selectionList) {
        return null;
    }

    @Override
    public <X> Root<X> from(Class<X> entityClass) {
        EntityType<X> entityType = entityManager.getMetamodel().entity(entityClass);
        if (entityType == null) {
            throw new IllegalArgumentException(entityClass + " is not an entity");
        }
        return from(entityType);
    }

    @Override
    public <X> Root<X> from(EntityType<X> entity) {
        return new RootImpl<>(criteriaBuilder, entity);
    }

    @Override
    public CriteriaQuery<T> where(Expression<Boolean> restriction) {
        return null;
    }

    @Override
    public CriteriaQuery<T> where(Predicate... restrictions) {
        return null;
    }

    @Override
    public CriteriaQuery<T> groupBy(Expression<?>... grouping) {
        return null;
    }

    @Override
    public CriteriaQuery<T> groupBy(List<Expression<?>> grouping) {
        return null;
    }

    @Override
    public CriteriaQuery<T> having(Expression<Boolean> restriction) {
        return null;
    }

    @Override
    public CriteriaQuery<T> having(Predicate... restrictions) {
        return null;
    }

    @Override
    public CriteriaQuery<T> orderBy(Order... o) {
        return null;
    }

    @Override
    public CriteriaQuery<T> orderBy(List<Order> o) {
        return null;
    }

    @Override
    public CriteriaQuery<T> distinct(boolean distinct) {
        return null;
    }

    @Override
    public Set<Root<?>> getRoots() {
        return null;
    }

    @Override
    public Selection<T> getSelection() {
        return null;
    }

    @Override
    public List<Expression<?>> getGroupList() {
        return null;
    }

    @Override
    public Predicate getGroupRestriction() {
        return null;
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public Class<T> getResultType() {
        return null;
    }

    @Override
    public List<Order> getOrderList() {
        return null;
    }

    @Override
    public Set<ParameterExpression<?>> getParameters() {
        return null;
    }

    @Override
    public <U> Subquery<U> subquery(Class<U> type) {
        return null;
    }

    @Override
    public Predicate getRestriction() {
        return null;
    }
}