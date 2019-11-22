package cn.sexycode.myjpa.metamodel.model.convert.spi;

import cn.sexycode.myjpa.session.Session;
import cn.sexycode.myjpa.sql.type.descriptor.java.EnumJavaTypeDescriptor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BasicValueConverter extension for enum-specific support
 *
 * @author Steve Ebersole
 */
public interface EnumValueConverter<O extends Enum, R> extends BasicValueConverter<O, R> {
    EnumJavaTypeDescriptor<O> getJavaDescriptor();

    int getJdbcTypeCode();

    O readValue(ResultSet resultSet, String name, Session session) throws SQLException;

    void writeValue(PreparedStatement statement, O value, int position, Session session)
            throws SQLException;

    String toSqlLiteral(Object value);
}
