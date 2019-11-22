package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.IdentifierType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.Type;

/**
 * Additional contract for a {@link Type} may be used for a discriminator.
 *
 */
public interface DiscriminatorType<T> extends IdentifierType<T>, LiteralType<T> {
}
