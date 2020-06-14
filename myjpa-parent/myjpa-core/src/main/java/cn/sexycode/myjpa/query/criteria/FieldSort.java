package cn.sexycode.myjpa.query.criteria;

/**
 * 排序
 *
 * @author qzz
 */
public interface FieldSort {


    /**
     * @return 排序的字段名
     */
    String getField();

    /**
     * @return 排序的类型ASC 或 DESC
     */
    Direction getDirection();
}