package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.Type;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Optional {@link Type} contract for implementations that are aware of how to extract values from
 * store procedure OUT/INOUT parameters.
 *
 */
public interface ProcedureParameterExtractionAware<T> {
    /**
     * Can the given instance of this type actually perform the parameter value extractions?
     *
     * @return {@code true} indicates that @{link #extract} calls will not fail due to {@link IllegalStateException}.
     */
    boolean canDoExtraction();

    /**
     * Perform the extraction
     *
     * @param statement The CallableStatement from which to extract the parameter value(s).
     * @param startIndex The parameter index from which to start extracting; assumes the values (if multiple) are contiguous
     * @param session The originating session
     *
     * @return The extracted value.
     *
     * @throws SQLException Indicates an issue calling into the CallableStatement
     * @throws IllegalStateException Thrown if this method is called on instances that return {@code false} for {@link #canDoExtraction}
     */
//	T extract(CallableStatement statement, int startIndex) throws SQLException;

    /**
     * Perform the extraction
     *
     * @param statement The CallableStatement from which to extract the parameter value(s).
     * @param paramNames The parameter names.
     * @param session The originating session
     *
     * @return The extracted value.
     *
     * @throws SQLException Indicates an issue calling into the CallableStatement
     * @throws IllegalStateException Thrown if this method is called on instances that return {@code false} for {@link #canDoExtraction}
     */
//	T extract(CallableStatement statement, String[] paramNames) throws SQLException;
}
