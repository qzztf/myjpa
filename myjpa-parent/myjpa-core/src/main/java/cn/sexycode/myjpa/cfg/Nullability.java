package cn.sexycode.myjpa.cfg;

/**
 * Are the columns forced to null, not null or not forced
 *
 * @author Emmanuel Bernard
 */
public enum Nullability {
    FORCED_NULL, FORCED_NOT_NULL, NO_CONSTRAINT
}
