package org.walkerljl.db.dbutil.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.walkerljl.db.dbutil.ResultSetHandler;
import org.walkerljl.db.dbutil.RowProcessor;

/**
 * <code>ResultSetHandler</code> implementation that converts the first
 * <code>ResultSet</code> row into a <code>Map</code>. This class is thread
 * safe.
 *
 * @see org.walkerljl.db.dbutil.ResultSetHandler
 */
public class MapHandler implements ResultSetHandler<Map<String, Object>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of MapHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public MapHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of MapHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into Maps.
     */
    public MapHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Converts the first row in the <code>ResultSet</code> into a
     * <code>Map</code>.
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code> with the values from the first row or
     * <code>null</code> if there are no rows in the <code>ResultSet</code>.
     *
     * @throws SQLException if a database access error occurs
     *
     * @see org.walkerljl.db.dbutil.ResultSetHandler#handle(java.sql.ResultSet)
     */
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }

}
