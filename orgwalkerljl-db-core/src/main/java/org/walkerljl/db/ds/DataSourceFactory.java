package org.walkerljl.db.ds;

import javax.sql.DataSource;

/**
 * 
 * DataSourceFactory 
 *
 * @author lijunlin
 */
public interface DataSourceFactory {

    /**
     * 获取数据源
     *
     * @return 数据源
     */
    DataSource getDataSource();
}
