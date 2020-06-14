package cn.sexycode.myjpa.query.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 字段排序。
 * <pre>
 * </pre>
 */
public class DefaultFieldSort implements FieldSort, Serializable {
    /**
     * serialVersionUID:类的序号
     *
     * @since 1.0.0
     */

    private static final long serialVersionUID = 5742164735225460363L;
    private static String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";
    private Direction direction;
    private String field;

    public DefaultFieldSort(String field) {
        this(field, Direction.ASC);
    }

    public DefaultFieldSort(String field, Direction direction) {
        this.direction = direction;
        this.field = field;
    }

    public static boolean isSQLInjection(String str) {
        return !Pattern.matches(INJECTION_REGEX, str);
    }

    /**
     * 将sql片段转化成排序对象。
     *
     * @param orderSegment ex: "id asc,code description"
     */
    public static List<DefaultFieldSort> fromString(String orderSegment) {

        if (orderSegment == null || "".equals(orderSegment.trim())) {
            return new ArrayList<>(0);
        }

        List<DefaultFieldSort> results = new ArrayList<>();
        String[] orderSegments = orderSegment.trim().split(",");
        for (String sortSegment : orderSegments) {
            DefaultFieldSort order = formatString(sortSegment);
            if (order != null) {
                results.add(order);
            }
        }
        return results;
    }

    private static DefaultFieldSort formatString(String orderSegment) {

        if (orderSegment == null || "".equals(orderSegment.trim())) {
            return null;
        }

        String[] array = orderSegment.trim().split("\\s+");
        if (array.length != 1 && array.length != 2) {
            throw new IllegalArgumentException("orderSegment pattern must be {property} {direction}, input is: " + orderSegment);
        }

        return create(array[0], array.length == 2 ? array[1] : "asc");
    }

    /**
     * @param field
     * @param direction
     * @return
     */
    public static DefaultFieldSort create(String field, String direction) {
        return new DefaultFieldSort(field, Direction.fromString(direction));
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        if (isSQLInjection(field)) {
            throw new IllegalArgumentException("SQLInjection property: " + field);
        }
        return field + (direction == null ? "" : " " + direction.name());
    }

}