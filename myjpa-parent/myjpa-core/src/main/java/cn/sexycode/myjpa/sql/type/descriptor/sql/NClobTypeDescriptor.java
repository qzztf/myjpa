package cn.sexycode.myjpa.sql.type.descriptor.sql;

import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;
import cn.sexycode.myjpa.sql.util.CharacterStream;
import cn.sexycode.myjpa.sql.type.descriptor.ValueBinder;
import cn.sexycode.myjpa.sql.type.descriptor.ValueExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor;

import java.sql.*;


/**
 * Descriptor for {@link Types#NCLOB NCLOB} handling.
 *
 * @author qzz
 */
public abstract class NClobTypeDescriptor implements SqlTypeDescriptor {
    @Override
    public int getSqlType() {
        return Types.NCLOB;
    }

    @Override
    public boolean canBeRemapped() {
        return true;
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>(javaTypeDescriptor, this) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(rs.getNClob(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options)
                    throws SQLException {
                return javaTypeDescriptor.wrap(statement.getNClob(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
                    throws SQLException {
                return javaTypeDescriptor.wrap(statement.getNClob(name), options);
            }
        };
    }

    protected abstract <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getNClobBinder(JavaTypeDescriptor<X> javaTypeDescriptor);

    @Override
    public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {
        return getNClobBinder(javaTypeDescriptor);
    }


    public static final NClobTypeDescriptor DEFAULT = new NClobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getNClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    if (options.useStreamForLobBinding()) {
                        STREAM_BINDING.getNClobBinder(javaTypeDescriptor).doBind(st, value, index, options);
                    } else {
                        NCLOB_BINDING.getNClobBinder(javaTypeDescriptor).doBind(st, value, index, options);
                    }
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    if (options.useStreamForLobBinding()) {
                        STREAM_BINDING.getNClobBinder(javaTypeDescriptor).doBind(st, value, name, options);
                    } else {
                        NCLOB_BINDING.getNClobBinder(javaTypeDescriptor).doBind(st, value, name, options);
                    }
                }
            };
        }
    };

    public static final NClobTypeDescriptor NCLOB_BINDING = new NClobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getNClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    st.setNClob(index, javaTypeDescriptor.unwrap(value, NClob.class, options));
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    st.setNClob(name, javaTypeDescriptor.unwrap(value, NClob.class, options));
                }
            };
        }
    };

    public static final NClobTypeDescriptor STREAM_BINDING = new NClobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getNClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    final CharacterStream characterStream = javaTypeDescriptor.unwrap(
                            value,
                            CharacterStream.class,
                            options
                    );
                    st.setNCharacterStream(index, characterStream.asReader(), characterStream.getLength());
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    final CharacterStream characterStream = javaTypeDescriptor.unwrap(
                            value,
                            CharacterStream.class,
                            options
                    );
                    st.setNCharacterStream(name, characterStream.asReader(), characterStream.getLength());
                }
            };
        }
    };
}
