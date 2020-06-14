package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 默认条件接口实现类。
 */
public class DefaultField<T> implements Field , Expression<T> {

    // 字段
    private String field;
    // 比较符
    private OP compare;
    // 比较值
    private Object value;

    // 字段前缀名，一般用于表的别名，如user.
    // private String preFieldName="";

    public DefaultField() {
    }

    public DefaultField(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public DefaultField(String field, OP compare, Object value) {
        this.field = field;
        this.compare = compare;
        this.value = value;

        if (OP.IN.equals(compare)) {
            this.value = getInValueSql();
        } else {
            this.value = value;
        }

    }

    /**
     * 针对in查询方式，根据传入的value的类型做不同的处理。 value 是 string，则分隔字符串，然后合并为符合in规范的字符串。
     * value 是 list，则直接合并为符合in规范的字符串。
     *
     * @return
     */
    private String getInValueSql() {
        if (value instanceof String) { // 字符串形式，通过逗号分隔
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            StringTokenizer st = new StringTokenizer(value.toString(), ",");
            while (st.hasMoreTokens()) {
                sb.append("\"");
                sb.append(st.nextToken());
                sb.append("\"");
                sb.append(",");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
            sb.append(")");
            return sb.toString();
        } else if (value instanceof List) { // 列表形式
            List<Object> objList = (List<Object>) value;
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (Object obj : objList) {
                sb.append("\"");
                sb.append(obj.toString());
                sb.append("\"");
                sb.append(",");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
            sb.append(")");
            return sb.toString();
        }
        return "";
    }

    @Override
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public OP getCompare() {
        return compare;
    }

    public void setCompare(OP compare) {
        this.compare = compare;
    }

    @Override
    public String getSql() {
        if (compare == null) {
            compare = OP.EQUAL;
        }
        String fieldParam;
        if (field.indexOf(".") > -1) {
            fieldParam = "#{" + field.substring(field.indexOf(".") + 1) + "}";
        } else {
            fieldParam = "#{" + field + "}";
        }
        String sql = field;
        if (sql.lastIndexOf("^") != -1) {
            sql = sql.substring(0, sql.lastIndexOf("^"));
        }
        if (OP.EQUAL.equals(compare)) {
            sql += " = " + fieldParam;
        } else if (OP.LESS.equals(compare)) {
            sql += " < " + fieldParam;
        } else if (OP.LESS_EQUAL.equals(compare)) {
            sql += " <= " + fieldParam;
        } else if (OP.GREAT.equals(compare)) {
            sql += " > " + fieldParam;
        } else if (OP.GREAT_EQUAL.equals(compare)) {
            sql += " >= " + fieldParam;
        } else if (OP.NOT_EQUAL.equals(compare)) {
            sql += " != " + fieldParam;
        } else if (OP.LEFT_LIKE.equals(compare)) {
            sql += " like " + fieldParam;
        } else if (OP.RIGHT_LIKE.equals(compare)) {
            sql += " like  " + fieldParam;
        } else if (OP.LIKE.equals(compare)) {
            sql += " like  " + fieldParam;
        } else if (OP.IS_NULL.equals(compare)) {
            sql += " is null " + fieldParam;
        } else if (OP.NOTNULL.equals(compare)) {
            sql += " is not null";
        } else if (OP.IN.equals(compare)) {
            sql += " in  " + this.value;
        } else if (OP.BETWEEN.equals(compare)) {
            sql += getBetweenSql();
        } else {
            sql += " =  " + fieldParam;
        }
        return sql;
    }

    private String getBetweenSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(" between ");
        if (this.value instanceof List) {
            List<Object> objList = (List<Object>) value;
            for (int i = 0; i < objList.size(); i++) {
                Object obj = objList.get(i);
                if (i == 1) {
                    sb.append(" and ");
                }
                if (obj instanceof Date) {
//                    String dateString = DateFormatUtil.format((Date) obj, StringPool.DATE_FORMAT_DATETIME);
//                    sb.append("\"" + dateString + "\"");
                } else {
                    sb.append("\"" + obj.toString() + "\"");
                }
            }
        }
        sb.append(" ");
        return sb.toString();
    }

    @Override
    public Predicate isNull() {
        return (Predicate) new DefaultFieldLogic().isNull(field);
    }

    @Override
    public Predicate isNotNull() {
        return (Predicate) new DefaultFieldLogic().isNotNull(field);
    }

    @Override
    public Predicate in(Object... values) {
        return (Predicate) new DefaultFieldLogic().in(field, values);
    }

    @Override
    public Expression as(Class type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate in(Expression values) {
        return null;
    }

    @Override
    public Predicate in(Collection values) {
        return (Predicate) new DefaultFieldLogic().in(field, values.toArray());
    }

    @Override
    public Predicate in(Expression[] values) {
        return null;
    }

    @Override
    public Selection alias(String name) {
        return null;
    }

    @Override
    public boolean isCompoundSelection() {
        return false;
    }

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        return null;
    }

    @Override
    public Class getJavaType() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
    }
}