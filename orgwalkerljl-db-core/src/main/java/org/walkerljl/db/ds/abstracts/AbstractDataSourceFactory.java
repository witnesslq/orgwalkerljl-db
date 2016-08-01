package org.walkerljl.db.ds.abstracts;

import org.walkerljl.commons.config.ConfiguratorFactory;
import org.walkerljl.db.ds.DataSourceFactory;

import javax.sql.DataSource;

/**
 * 
 * AbstractDataSourceFactory 
 *
 * @author lijunlin
 */
public abstract class AbstractDataSourceFactory<T extends DataSource> implements DataSourceFactory {

    @Override
    public final T getDataSource() {
        // 创建数据源对象
        T ds = createDataSource();
        // 设置基础属性
        setDriver(ds, getPropertyAsString("org.walkerljl.db.jdbc.driver"));
        setUrl(ds, getPropertyAsString("org.walkerljl.db.jdbc.url"));
        setUsername(ds, getPropertyAsString("org.walkerljl.db.jdbc.username"));
        setPassword(ds, getPropertyAsString("org.walkerljl.db.jdbc.password"));
        // 设置高级属性
        setAdvancedConfig(ds);
        return ds;
    }

    protected String getPropertyAsString(String key) {
        return ConfiguratorFactory.getInstance().getStdConfigurator().getAsString(key);
    }

    public abstract T createDataSource();

    public abstract void setDriver(T ds, String driver);

    public abstract void setUrl(T ds, String url);

    public abstract void setUsername(T ds, String username);

    public abstract void setPassword(T ds, String password);

    public abstract void setAdvancedConfig(T ds);
}