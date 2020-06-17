package cn.sexycode.myjpa.query.criteria;

/**
 * 操作符
 * @author qzz
 */
public enum OP implements Operation{
    /**
     * 等于
     */
    EQUAL("EQ", "=", "等于"),
    EQUAL_IGNORE_CASE("EIC", "=", "等于忽略大小写"),
    LESS("LT", "<", "小于"),
    GREAT("GT", ">", "大于"),
    LESS_EQUAL("LE", "<=", "小于等于"),
    GREAT_EQUAL("GE", ">=", "大于等于"),
    NOT_EQUAL("NE", "!=", "不等于"),
    LIKE("LK", "like", "相似"),
    LEFT_LIKE("LFK", "like", "左相似"),
    RIGHT_LIKE("RHK", "like", "右相似"),
    IS_NULL("ISNULL", "is null", "为空"),
    NOTNULL("NOTNULL", "is not null", "非空"),
    IN("IN", "in", "在...中"),
    BETWEEN("BETWEEN", "between", "在...之间");
    private String val;
    private String op;
    private String desc;

    OP(String val, String op, String desc) {
        this.val = val;
        this.op = op;
        this.desc = desc;
    }

    /**
     * 根据运算符获取QueryOp
     * @param op
     * @return
     * QueryOP
     */
    public static OP getByOP(String op) {
        for (OP OP : values()) {
            if (OP.op().equals(op)) {
                return OP;
            }
        }
        return null;
    }

    public static OP getByVal(String val) {
        for (OP OP : values()) {
            if (OP.val.equals(val)) {
                return OP;
            }
        }
        return null;
    }

    public String value() {
        return val;
    }

    public String op() {
        return op;
    }

    public String desc() {
        return desc;
    }

    /**
     * 返回条件的sql片段
     *
     * @return SQL片段
     */
    @Override
    public String getSql() {
        return op;
    }
}