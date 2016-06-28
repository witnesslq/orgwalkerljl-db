package org.walkerljl.db.orm;

import org.junit.Before;
import org.walkerljl.commons.config.Configurator;
import org.walkerljl.commons.config.ConfiguratorFactory;
import org.walkerljl.commons.config.impl.readonly.PropertiesReadonlyConfigurator;
import org.walkerljl.db.BaseTest;
import org.walkerljl.db.ds.DataSourceFactory;
import org.walkerljl.db.orm.entity.identity.User;
import org.walkerljl.db.orm.session.Configuration;
import org.walkerljl.db.orm.session.SqlSession;
import org.walkerljl.db.orm.session.defaults.DefaultSqlSession;

/**
 *
 * OrmTest
 *
 * @author lijunlin
 */
public class OrmTest extends BaseTest {

	private SqlSession sqlSession;
	private Configuration configuration;
	
	@Before
	public void before() {
		//注册配置管理器
		Configurator configurator = new PropertiesReadonlyConfigurator("orgwalkerljl-db.properties");
		ConfiguratorFactory.getInstance().register(configurator);
		
		DataSourceFactory dataSourceFactory = null;//new DbcpDataSourceFactory();
		
		configuration = new Configuration();
		configuration.setDataSource(dataSourceFactory.getDataSource());
		sqlSession = new DefaultSqlSession(configuration);
	}
	
	public User initEntity() {
		User user = new User();
		user.setUserId("jarvis");
		user.setUserName("JARVIS");
//		user.setSex("m");
//		user.setEmail("xxx@163.com");
//		user.setMobile("10000000");
//		user.setTelephone("xxxxxxx");
//		user.setBirthday(new Date());
//		user.setIdCardNumber("100");
//		Date date = new Date();
//		user.setLastLoginDate(date);
//		user.setRemark("测试");
//		user.setStatus(1);
//		user.setCreateDate(date);
//		user.setCreateUserId("jarvis");
//		user.setCreateUserName("JARVIS");
//		user.setLastModifyDate(date);
//		user.setLastModifyUserId("jarvis");
//		user.setLastModifyUserName("JARVIS");
		return user;
	}
	
	@Override
	public void doTest() {
		//User queriedEntity = sqlSession.selectByKey(5L, new User());
		//LOGGER.info(queriedEntity == null ? "NULL" : queriedEntity.toString());
	}
}