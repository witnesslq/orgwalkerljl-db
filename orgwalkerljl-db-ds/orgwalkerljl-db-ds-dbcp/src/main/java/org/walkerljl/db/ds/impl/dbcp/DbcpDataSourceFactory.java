package org.walkerljl.db.ds.impl.dbcp;

import org.apache.commons.dbcp.BasicDataSource;
import org.walkerljl.db.ds.abstracts.AbstractDataSourceFactory;

/**
 * 基于 Apache Commons DBCP 实现
 *
 * @author lijunlin
 */
public class DbcpDataSourceFactory extends AbstractDataSourceFactory<BasicDataSource> {

    @Override
    public BasicDataSource createDataSource() {
        return new BasicDataSource();
    }

    @Override
    public void setDriver(BasicDataSource ds, String driver) {
        ds.setDriverClassName(driver);
    }

    @Override
    public void setUrl(BasicDataSource ds, String url) {
        ds.setUrl(url);
    }

    @Override
    public void setUsername(BasicDataSource ds, String username) {
        ds.setUsername(username);
    }

    @Override
    public void setPassword(BasicDataSource ds, String password) {
        ds.setPassword(password);
    }

    @Override
    public void setAdvancedConfig(BasicDataSource ds) {
        // 解决 java.sql.SQLException: Already closed. 的问题（连接池会自动关闭长时间没有使用的连接）
        ds.setValidationQuery("select 1 from dual");
    }
}