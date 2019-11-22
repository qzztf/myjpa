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
 * Descriptor for {@link Types#CLOB CLOB} handling.
 *
 * @author qzz
 * @author Gail Badner
 */
public abstract class ClobTypeDescriptor implements SqlTypeDescriptor {
    @Override
    public int getSqlType() {
        return Types.CLOB;
    }

    @Override
    public boolean canBeRemapped() {
        return true;
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicExtractor<X>(javaTypeDescriptor, this) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(rs.getClob(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options)
                    throws SQLException {
                return javaTypeDescriptor.wrap(statement.getClob(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
                    throws SQLException {
                return javaTypeDescriptor.wrap(statement.getClob(name), options);
            }
        };
    }

    protected abstract <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getClobBinder(JavaTypeDescriptor<X> javaTypeDescriptor);

    @Override
    public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {
        return getClobBinder(javaTypeDescriptor);
    }


    public static final ClobTypeDescriptor DEFAULT = new ClobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    if (options.useStreamForLobBinding()) {
                        STREAM_BINDING.getClobBinder(javaTypeDescriptor).doBind(st, value, index, options);
                    } else {
                        CLOB_BINDING.getClobBinder(javaTypeDescriptor).doBind(st, value, index, options);
                    }
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    if (options.useStreamForLobBinding()) {
                        STREAM_BINDING.getClobBinder(javaTypeDescriptor).doBind(st, value, name, options);
                    } else {
                        CLOB_BINDING.getClobBinder(javaTypeDescriptor).doBind(st, value, name, options);
                    }
                }
            };
        }
    };

    public static final ClobTypeDescriptor CLOB_BINDING = new ClobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    st.setClob(index, javaTypeDescriptor.unwrap(value, Clob.class, options));
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    st.setClob(name, javaTypeDescriptor.unwrap(value, Clob.class, options));
                }
            };
        }
    };


    public static final ClobTypeDescriptor POSTGRESQL_CLOB_BINDING = new ClobTypeDescriptor() {
        @Override
        public <X> ValueExtractor<X> getExtractor(JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicExtractor<X>(javaTypeDescriptor, this) {
                @Override
                protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(rs.getString(name), options);
                }

                @Override
                protected X doExtract(CallableStatement statement, int index, WrapperOptions options)
                        throws SQLException {
                    return javaTypeDescriptor.wrap(statement.getString(index), options);
                }

                @Override
                protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
                        throws SQLException {
                    return javaTypeDescriptor.wrap(statement.getString(name), options);
                }
            };
        }

        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    st.setString(index, javaTypeDescriptor.unwrap(value, String.class, options));
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    st.setString(name, javaTypeDescriptor.unwrap(value, String.class, options));
                }
            };
        }
    };

    public static final ClobTypeDescriptor STREAM_BINDING = new ClobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    final CharacterStream characterStream = javaTypeDescriptor.unwrap(
                            value,
                            CharacterStream.class,
                            options
                    );
                    st.setCharacterStream(index, characterStream.asReader(), characterStream.getLength());
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    final CharacterStream characterStream = javaTypeDescriptor.unwrap(
                            value,
                            CharacterStream.class,
                            options
                    );
                    st.setCharacterStream(name, characterStream.asReader(), characterStream.getLength());
                }
            };
        }
    };

    public static final ClobTypeDescriptor STREAM_BINDING_EXTRACTING = new ClobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    final CharacterStream characterStream = javaTypeDescriptor.unwrap(
                            value,
                            CharacterStream.class,
                            options
                    );
                    st.setCharacterStream(index, characterStream.asReader(), characterStream.getLength());
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    final CharacterStream characterStream = javaTypeDescriptor.unwrap(
                            value,
                            CharacterStream.class,
                            options
                    );
                    st.setCharacterStream(name, characterStream.asReader(), characterStream.getLength());
                }
            };
        }

        @Override
        public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicExtractor<X>(javaTypeDescriptor, this) {
                @Override
                protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(rs.getCharacterStream(name), options);
                }

                @Override
                protected X doExtract(CallableStatement statement, int index, WrapperOptions options)
                        throws SQLException {
                    return javaTypeDescriptor.wrap(statement.getCharacterStream(index), options);
                }

                @Override
                protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
                        throws SQLException {
                    return javaTypeDescriptor.wrap(statement.getCharacterStream(name), options);
                }
            };
        }
    };

}
