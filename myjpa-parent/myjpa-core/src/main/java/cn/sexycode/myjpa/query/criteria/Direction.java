package cn.sexycode.myjpa.query.criteria;

/**
 * 字段排序方向。
 *
 * @author qzz
 */
public enum Direction {
    /**
     * asc
     */
    ASC,
    /**
     * description
     */
    DESC;


    public static Direction fromString(String value) {
        try {
            return Direction.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return ASC;
        }
    }
}