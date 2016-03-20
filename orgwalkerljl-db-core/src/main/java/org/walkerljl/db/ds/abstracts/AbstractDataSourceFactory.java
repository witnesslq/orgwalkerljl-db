package org.walkerljl.db.ds.abstracts;

import javax.sql.DataSource;

import org.walkerljl.config.ConfiguratorFactory;
import org.walkerljl.db.ds.DataSourceFactory;

/**
 * 
 * AbstractDataSourceFactory 
 *
 * @author lijunlin
 */
public abstract class AbstractDataSourceFactory<T extends DataSource> implements DataSourceFactory {

    protected final String driver = ConfiguratorFactory.getIns().getAsString("org.walkerljl.db.jdbc.driver");
    protected final String url = ConfiguratorFactory.getIns().getAsString("org.walkerljl.db.jdbc.url");
    protected final String username = ConfiguratorFactory.getIns().getAsString("org.walkerljl.db.jdbc.username");
    protected final String password = ConfiguratorFactory.getIns().getAsString("org.walkerljl.db.jdbc.password");

    @Override
    public final T getDataSource() {
        // 创建数据源对象
        T ds = createDataSource();
        // 设置基础属性
        setDriver(ds, driver);
        setUrl(ds, url);
        setUsername(ds, username);
        setPassword(ds, password);
        // 设置高级属性
        setAdvancedConfig(ds);
        return ds;
    }

    public abstract T createDataSource();

    public abstract void setDriver(T ds, String driver);

    public abstract void setUrl(T ds, String url);

    public abstract void setUsername(T ds, String username);

    public abstract void setPassword(T ds, String password);

    public abstract void setAdvancedConfig(T ds);
}