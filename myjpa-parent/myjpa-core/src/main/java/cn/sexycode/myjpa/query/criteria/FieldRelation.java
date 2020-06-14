package cn.sexycode.myjpa.query.criteria;

/**
 * 条件逻辑关系
 * @author qzz
 */
public enum FieldRelation {
    /**
     * AND
     */
    AND("AND"),
    /**
     * OR
     */
    OR("OR"),
    /**
     * NOT
     */
    NOT("NOT");
    private String val;

    FieldRelation(String val) {
        this.val = val;
    }

    public String value() {
        return val;
    }
}