package org.walkerljl.db.dbutil.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.walkerljl.db.dbutil.RowProcessor;

/**
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into a <code>List</code> of <code>Map</code>s.
 * This class is thread safe.
 *
 * @see org.walkerljl.db.dbutil.ResultSetHandler
 */
public class MapListHandler extends AbstractListHandler<Map<String, Object>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of MapListHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public MapListHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of MapListHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into Maps.
     */
    public MapListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Converts the <code>ResultSet</code> row into a <code>Map</code> object.
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code>, never null.
     *
     * @throws SQLException if a database access error occurs
     *
     * @see org.walkerljl.db.dbutil.handler.AbstractListHandler#handle(ResultSet)
     */
    @Override
    protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }

}
