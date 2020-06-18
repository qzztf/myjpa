package cn.sexycode.myjpa.query.criteria;

/**
 * 构建SQL语句中的SQL片段
 * @author qzz
 */
public interface Clause {
    /**
     * 返回条件的sql片段
     * @return SQL片段
     */
    String getSql();
}