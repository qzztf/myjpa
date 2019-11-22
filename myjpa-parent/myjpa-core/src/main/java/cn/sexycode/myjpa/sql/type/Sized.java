package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.UserType;
import cn.sexycode.myjpa.sql.util.Size;

/**
 * Extends dictated/default column size declarations from {@link org.hibernate.type.Type} to the {@link UserType}
 * hierarchy as well via an optional interface.
 *
 * @author qzz
 */
public interface Sized {
    /**
     * Return the column sizes dictated by this type.  For example, the mapping for a {@code char}/{@link Character} would
     * have a dictated length limit of 1; for a string-based {@link java.util.UUID} would have a size limit of 36; etc.
     *
     * @return The dictated sizes.
     * @todo Would be much much better to have this aware of Dialect once the service/metamodel split is done
     * @see org.hibernate.type.Type#dictatedSizes
     */
    public Size[] dictatedSizes();

    /**
     * Defines the column sizes to use according to this type if the user did not explicitly say (and if no
     * {@link #dictatedSizes} were given).
     *
     * @return The default sizes.
     * @todo Would be much much better to have this aware of Dialect once the service/metamodel split is done
     * @see org.hibernate.type.Type#defaultSizes
     */
    public Size[] defaultSizes();
}
