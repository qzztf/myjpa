package cn.sexycode.myjpa.sql.type.descriptor.java;

import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.*;
import cn.sexycode.myjpa.sql.type.descriptor.java.DataHelper;
import cn.sexycode.myjpa.sql.type.descriptor.java.MutabilityPlan;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * Descriptor for {@link Blob} handling.
 * <p/>
 * Note, {@link Blob blobs} really are mutable (their internal state can in fact be mutated).  We simply
 * treat them as immutable because we cannot properly check them for changes nor deep copy them.
 *
 * @author qzz
 */
public class BlobTypeDescriptor extends AbstractTypeDescriptor<Blob> {
    public static final BlobTypeDescriptor INSTANCE = new BlobTypeDescriptor();

    public static class BlobMutabilityPlan implements MutabilityPlan<Blob> {
        public static final BlobMutabilityPlan INSTANCE = new BlobMutabilityPlan();

        @Override
        public boolean isMutable() {
            return false;
        }

        @Override
        public Blob deepCopy(Blob value) {
            return value;
        }

        @Override
        public Serializable disassemble(Blob value) {
            throw new UnsupportedOperationException("Blobs are not cacheable");
        }

        @Override
        public Blob assemble(Serializable cached) {
            throw new UnsupportedOperationException("Blobs are not cacheable");
        }
    }

    public BlobTypeDescriptor() {
        super(Blob.class, BlobMutabilityPlan.INSTANCE);
    }

    @Override
    public String extractLoggableRepresentation(Blob value) {
        return value == null ? "null" : "{blob}";
    }

    @Override
    public String toString(Blob value) {
        final byte[] bytes;
        try {
            bytes = DataHelper.extractBytes(value.getBinaryStream());
        } catch (SQLException e) {
            throw new TypeException("Unable to access blob stream", e);
        }
        return PrimitiveByteArrayTypeDescriptor.INSTANCE.toString(bytes);
    }

    @Override
    public Blob fromString(String string) {
        return null;
//		return BlobProxy.generateProxy( PrimitiveByteArrayTypeDescriptor.INSTANCE.fromString( string ) );
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Comparator<Blob> getComparator() {
        return IncomparableComparator.INSTANCE;
    }

    @Override
    public int extractHashCode(Blob value) {
        return System.identityHashCode(value);
    }

    @Override
    public boolean areEqual(Blob one, Blob another) {
        return one == another;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(Blob value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }

		/*try {
			if ( BinaryStream.class.isAssignableFrom( type ) ) {
				if ( BlobImplementer.class.isInstance( value ) ) {
					// if the incoming Blob is a wrapper, just pass along its BinaryStream
					return (X) ( (BlobImplementer) value ).getUnderlyingStream();
				}
				else {
					// otherwise we need to build a BinaryStream...
					return (X) new BinaryStreamImpl( DataHelper.extractBytes( value.getBinaryStream() ) );
				}
			}
			else if ( byte[].class.isAssignableFrom( type )) {
				if ( BlobImplementer.class.isInstance( value ) ) {
					// if the incoming Blob is a wrapper, just grab the bytes from its BinaryStream
					return (X) ( (BlobImplementer) value ).getUnderlyingStream().getBytes();
				}
				else {
					// otherwise extract the bytes from the stream manually
					return (X) DataHelper.extractBytes( value.getBinaryStream() );
				}
			}
			else if (Blob.class.isAssignableFrom( type )) {
				final Blob blob =  WrappedBlob.class.isInstance( value )
						? ( (WrappedBlob) value ).getWrappedBlob()
						: value;
				return (X) blob;
			}
		}
		catch ( SQLException e ) {
			throw new HibernateException( "Unable to access blob stream", e );
		}*/

        throw unknownUnwrap(type);
    }

    @Override
    public <X> Blob wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        // Support multiple return types from
        // org.hibernate.type.descriptor.sql.BlobTypeDescriptor
		/*if ( Blob.class.isAssignableFrom( value.getClass() ) ) {
			return options.getLobCreator().wrap( (Blob) value );
		}
		else if ( byte[].class.isAssignableFrom( value.getClass() ) ) {
			return options.getLobCreator().createBlob( ( byte[] ) value);
		}
		else if ( InputStream.class.isAssignableFrom( value.getClass() ) ) {
			InputStream inputStream = ( InputStream ) value;
			try {
				return options.getLobCreator().createBlob( inputStream, inputStream.available() );
			}
			catch ( IOException e ) {
				throw unknownWrap( value.getClass() );
			}
		}*/

        throw unknownWrap(value.getClass());
    }
}
