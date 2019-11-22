package cn.sexycode.myjpa.sql.mapping;

/**
 * Contract for entities (in the ERD sense) which can be exported via {@code CREATE}, {@code ALTER}, etc
 *
 */
public interface Exportable {
    /**
     * Get a unique identifier to make sure we are not exporting the same database structure multiple times.
     *
     * @return The exporting identifier.
     */
    String getExportIdentifier();
}
