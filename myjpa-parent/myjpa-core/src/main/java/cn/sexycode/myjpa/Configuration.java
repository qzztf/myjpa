package cn.sexycode.myjpa;

import cn.sexycode.myjpa.binding.MappingException;
import cn.sexycode.util.core.str.StringUtils;
import cn.sexycode.util.core.str.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 属性配置
 *
 * @author qinzaizhen
 */
public class Configuration implements AvailableSettings {
    public static final String PREFIX = "myjpa";

    private List<Class> mappers = new ArrayList<Class>();

    private String UUID;

    private String IDENTITY;

    private boolean BEFORE;

    private String seqFormat;

    private String catalog;

    private String schema;

    //校验调用Example方法时，Example(entityClass)和Mapper<EntityClass>是否一致
    private boolean checkExampleEntityClass;

    //使用简单类型
    //3.5.0 后默认值改为 true
    private boolean useSimpleType = true;

    /**
     * @since 3.5.0
     */
    private boolean enumAsSimpleType;

    /**
     * 是否支持方法上的注解，默认false
     */
    private boolean enableMethodAnnotation;

    /**
     * 对于一般的getAllIfColumnNode，是否判断!=''，默认不判断
     */
    private boolean notEmpty;

    /**
     * 字段转换风格，默认驼峰转下划线
     */
    private Style style;

    /**
     * 处理关键字，默认空，mysql可以设置为 `{0}`, sqlserver 为 [{0}]，{0} 代表的列名
     */
    private String wrapKeyword = "";

    public String getCatalog() {
        return catalog;
    }

    /**
     * 设置全局的catalog,默认为空，如果设置了值，操作表时的sql会是catalog.tablename
     *
     * @param catalog
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * 获取主键自增回写SQL
     *
     * @return
     */
    /*public String getIDENTITY() {
        if (StringUtils.isNotEmpty(this.IDENTITY)) {
            return this.IDENTITY;
        }
        //针对mysql的默认值
        return IdentityDialect.MYSQL.getIdentityRetrievalStatement();
    }*/

    /**
     * 主键自增回写方法,默认值MYSQL,详细说明请看文档
     *
     * @param IDENTITY
     */
   /* public void setIDENTITY(String IDENTITY) {
        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(IDENTITY);
        if (identityDialect != null) {
            this.IDENTITY = identityDialect.getIdentityRetrievalStatement();
        } else {
            this.IDENTITY = IDENTITY;
        }
    }*/

    /**
     * 获取表前缀，带catalog或schema
     *
     * @return
     */
    public String getPrefix() {
        if (StringUtils.isNotEmpty(this.catalog)) {
            return this.catalog;
        }
        if (StringUtils.isNotEmpty(this.schema)) {
            return this.schema;
        }
        return "";
    }

    public String getSchema() {
        return schema;
    }

    /**
     * 设置全局的schema,默认为空，如果设置了值，操作表时的sql会是schema.tablename
     * <br>如果同时设置了catalog,优先使用catalog.tablename
     *
     * @param schema
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * 获取序列格式化模板
     *
     * @return
     */
    public String getSeqFormat() {
        if (StringUtils.isNotEmpty(this.seqFormat)) {
            return this.seqFormat;
        }
        return "{0}.nextval";
    }

    /**
     * 序列的获取规则,使用{num}格式化参数，默认值为{0}.nextval，针对Oracle
     * <br>可选参数一共3个，对应0,1,2,3分别为SequenceName，ColumnName, PropertyName，TableName
     *
     * @param seqFormat
     */
    public void setSeqFormat(String seqFormat) {
        this.seqFormat = seqFormat;
    }

    public Style getStyle() {
        return this.style == null ? Style.camelhump : this.style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    /**
     * 获取UUID生成规则
     *
     * @return
     */
    public String getUUID() {
        if (StringUtils.isNotEmpty(this.UUID)) {
            return this.UUID;
        }
        return "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")";
    }

    /**
     * 设置UUID生成策略
     * <br>配置UUID生成策略需要使用OGNL表达式
     * <br>默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")
     *
     * @param UUID
     */
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getWrapKeyword() {
        return wrapKeyword;
    }

    public void setWrapKeyword(String wrapKeyword) {
        this.wrapKeyword = wrapKeyword;
    }

    /**
     * 获取SelectKey的Order
     *
     * @return
     */
    public boolean isBEFORE() {
        return BEFORE;
    }

    public void setBEFORE(boolean BEFORE) {
        this.BEFORE = BEFORE;
    }

    public boolean isCheckExampleEntityClass() {
        return checkExampleEntityClass;
    }

    public void setCheckExampleEntityClass(boolean checkExampleEntityClass) {
        this.checkExampleEntityClass = checkExampleEntityClass;
    }

    public boolean isEnableMethodAnnotation() {
        return enableMethodAnnotation;
    }

    public void setEnableMethodAnnotation(boolean enableMethodAnnotation) {
        this.enableMethodAnnotation = enableMethodAnnotation;
    }

    public boolean isEnumAsSimpleType() {
        return enumAsSimpleType;
    }

    public void setEnumAsSimpleType(boolean enumAsSimpleType) {
        this.enumAsSimpleType = enumAsSimpleType;
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public boolean isUseSimpleType() {
        return useSimpleType;
    }

    public void setUseSimpleType(boolean useSimpleType) {
        this.useSimpleType = useSimpleType;
    }

    /**
     * 主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
     *
     * @param order
     */
    public void setOrder(String order) {
        this.BEFORE = "BEFORE".equalsIgnoreCase(order);
    }

   /* public String getIdentity() {
        return getIDENTITY();
    }

    public void setIdentity(String identity) {
        setIDENTITY(identity);
    }*/

    public List<Class> getMappers() {
        return mappers;
    }

    public void setMappers(List<Class> mappers) {
        this.mappers = mappers;
    }

    public String getUuid() {
        return getUUID();
    }

    public void setUuid(String uuid) {
        setUUID(uuid);
    }

    public boolean isBefore() {
        return isBEFORE();
    }

    public void setBefore(boolean before) {
        setBEFORE(before);
    }

    /**
     * 配置属性
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        if (properties == null) {
            //默认驼峰
            this.style = Style.camelhump;
            return;
        }
        String UUID = properties.getProperty("UUID");
        if (StringUtils.isNotEmpty(UUID)) {
            setUUID(UUID);
        }
        String IDENTITY = properties.getProperty("IDENTITY");
        if (StringUtils.isNotEmpty(IDENTITY)) {
            //            setIDENTITY(IDENTITY);
        }
        String seqFormat = properties.getProperty("seqFormat");
        if (StringUtils.isNotEmpty(seqFormat)) {
            setSeqFormat(seqFormat);
        }
        String catalog = properties.getProperty("catalog");
        if (StringUtils.isNotEmpty(catalog)) {
            setCatalog(catalog);
        }
        String schema = properties.getProperty("schema");
        if (StringUtils.isNotEmpty(schema)) {
            setSchema(schema);
        }
        String ORDER = properties.getProperty("ORDER");
        if (StringUtils.isNotEmpty(ORDER)) {
            setOrder(ORDER);
        }
        this.notEmpty = Boolean.valueOf(properties.getProperty("notEmpty"));
        this.enableMethodAnnotation = Boolean.valueOf(properties.getProperty("enableMethodAnnotation"));
        this.checkExampleEntityClass = Boolean.valueOf(properties.getProperty("checkExampleEntityClass"));
        //默认值 true，所以要特殊判断
        String useSimpleTypeStr = properties.getProperty("useSimpleType");
        if (StringUtils.isNotEmpty(useSimpleTypeStr)) {
            this.useSimpleType = Boolean.valueOf(useSimpleTypeStr);
        }
        this.enumAsSimpleType = Boolean.valueOf(properties.getProperty("enumAsSimpleType"));
        //注册新的基本类型，以逗号隔开，使用全限定类名
        String simpleTypes = properties.getProperty("simpleTypes");
       /* if (StringUtils.isNotEmpty(simpleTypes)) {
            SimpleTypeUtil.registerSimpleType(simpleTypes);
        }*/
        String styleStr = properties.getProperty("style");
        if (StringUtils.isNotEmpty(styleStr)) {
            try {
                this.style = Style.valueOf(styleStr);
            } catch (IllegalArgumentException e) {
                throw new MappingException(styleStr + "不是合法的Style值!");
            }
        } else {
            //默认驼峰
            this.style = Style.camelhump;
        }
        //处理关键字
        String wrapKeyword = properties.getProperty("wrapKeyword");
        if (StringUtils.isNotEmpty(wrapKeyword)) {
            this.wrapKeyword = wrapKeyword;
        }
    }
}
