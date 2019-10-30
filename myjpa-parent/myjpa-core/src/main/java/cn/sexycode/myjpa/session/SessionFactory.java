package cn.sexycode.myjpa.session;

import javax.naming.Referenceable;
import javax.persistence.EntityManagerFactory;
import java.io.Closeable;
import java.io.Serializable;

/**
 *
 * @author qzz
 */
public interface SessionFactory extends EntityManagerFactory, Referenceable, Serializable,
        Closeable {
}
