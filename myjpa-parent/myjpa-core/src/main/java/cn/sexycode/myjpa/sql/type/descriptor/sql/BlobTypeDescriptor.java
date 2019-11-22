package cn.sexycode.myjpa.sql.type.descriptor.sql;

import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;
import cn.sexycode.myjpa.sql.util.BinaryStream;
import cn.sexycode.myjpa.sql.type.descriptor.ValueExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor;

import java.sql.*;

/**
 * Descriptor for {@link Types#BLOB BLOB} handling.
 *
 * @author qzz
 * @author Gail Badner
 * @author Brett Meyer
 */
public abstract class BlobTypeDescriptor implements SqlTypeDescriptor {

    private BlobTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.BLOB;
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
                return javaTypeDescriptor.wrap(rs.getBlob(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getBlob(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
                    throws SQLException {
                return javaTypeDescriptor.wrap(statement.getBlob(name), options);
            }
        };
    }

    protected abstract <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor);

    @Override
    public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return getBlobBinder(javaTypeDescriptor);
    }

    public static final BlobTypeDescriptor DEFAULT = new BlobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    BlobTypeDescriptor descriptor = BLOB_BINDING;
                    if (byte[].class.isInstance(value)) {
                        // performance shortcut for binding BLOB data in byte[] format
                        descriptor = PRIMITIVE_ARRAY_BINDING;
                    } else if (options.useStreamForLobBinding()) {
                        descriptor = STREAM_BINDING;
                    }
                    descriptor.getBlobBinder(javaTypeDescriptor).doBind(st, value, index, options);
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    BlobTypeDescriptor descriptor = BLOB_BINDING;
                    if (byte[].class.isInstance(value)) {
                        // performance shortcut for binding BLOB data in byte[] format
                        descriptor = PRIMITIVE_ARRAY_BINDING;
                    } else if (options.useStreamForLobBinding()) {
                        descriptor = STREAM_BINDING;
                    }
                    descriptor.getBlobBinder(javaTypeDescriptor).doBind(st, value, name, options);
                }
            };
        }
    };

    public static final BlobTypeDescriptor PRIMITIVE_ARRAY_BINDING = new BlobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                public void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    st.setBytes(index, javaTypeDescriptor.unwrap(value, byte[].class, options));
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    st.setBytes(name, javaTypeDescriptor.unwrap(value, byte[].class, options));
                }
            };
        }
    };

    public static final BlobTypeDescriptor BLOB_BINDING = new BlobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    st.setBlob(index, javaTypeDescriptor.unwrap(value, Blob.class, options));
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    st.setBlob(name, javaTypeDescriptor.unwrap(value, Blob.class, options));
                }
            };
        }
    };

    public static final BlobTypeDescriptor STREAM_BINDING = new BlobTypeDescriptor() {
        @Override
        public <X> cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    final BinaryStream binaryStream = javaTypeDescriptor.unwrap(
                            value,
                            BinaryStream.class,
                            options
                    );
                    st.setBinaryStream(index, binaryStream.getInputStream(), binaryStream.getLength());
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    final BinaryStream binaryStream = javaTypeDescriptor.unwrap(
                            value,
                            BinaryStream.class,
                            options
                    );
                    st.setBinaryStream(name, binaryStream.getInputStream(), binaryStream.getLength());
                }
            };
        }
    };

}
