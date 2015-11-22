package org.walkerljl.db.dbutil.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.walkerljl.db.dbutil.ResultSetHandler;

/**
 * <code>ResultSetHandler</code> implementation that converts one
 * <code>ResultSet</code> column into an Object. This class is thread safe.
 *
 * @param <T> The type of the scalar
 * @see org.walkerljl.db.dbutil.ResultSetHandler
 */
public class ScalarHandler<T> implements ResultSetHandler<T> {

    /**
     * The column number to retrieve.
     */
    private final int columnIndex;

    /**
     * The column name to retrieve.  Either columnName or columnIndex
     * will be used but never both.
     */
    private final String columnName;

    /**
     * Creates a new instance of ScalarHandler.  The first column will
     * be returned from <code>handle()</code>.
     */
    public ScalarHandler() {
        this(1, null);
    }

    /**
     * Creates a new instance of ScalarHandler.
     *
     * @param columnIndex The index of the column to retrieve from the
     * <code>ResultSet</code>.
     */
    public ScalarHandler(int columnIndex) {
        this(columnIndex, null);
    }

    /**
     * Creates a new instance of ScalarHandler.
     *
     * @param columnName The name of the column to retrieve from the
     * <code>ResultSet</code>.
     */
    public ScalarHandler(String columnName) {
        this(1, columnName);
    }

    /** Helper constructor
     * @param columnIndex The index of the column to retrieve from the
     * <code>ResultSet</code>.
     * @param columnName The name of the column to retrieve from the
     * <code>ResultSet</code>.
     */
    private ScalarHandler(int columnIndex, String columnName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    /**
     * Returns one <code>ResultSet</code> column as an object via the
     * <code>ResultSet.getObject()</code> method that performs type
     * conversions.
     * @param rs <code>ResultSet</code> to process.
     * @return The column or <code>null</code> if there are no rows in
     * the <code>ResultSet</code>.
     *
     * @throws SQLException if a database access error occurs
     * @throws ClassCastException if the class datatype does not match the column type
     *
     * @see org.walkerljl.db.dbutil.ResultSetHandler#handle(java.sql.ResultSet)
     */
    // We assume that the user has picked the correct type to match the column
    // so getObject will return the appropriate type and the cast will succeed.
    @SuppressWarnings("unchecked")
    @Override
    public T handle(ResultSet rs) throws SQLException {

        if (rs.next()) {
            if (this.columnName == null) {
                return (T) rs.getObject(this.columnIndex);
            }
            return (T) rs.getObject(this.columnName);
        }
        return null;
    }
}
