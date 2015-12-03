package org.walkerljl.db.orm.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.walkerljl.db.orm.entity.SqlEntry;
import org.walkerljl.db.orm.entity.identity.User;
import org.walkerljl.log.Logger;
import org.walkerljl.log.LoggerFactory;

/**
 *
 * SqlGeneratorTest
 *
 * @author lijunlin
 */
public class SqlGeneratorTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlGeneratorTest.class);

	@Test
	public void generateInsertSql() {
		User user = new User();
		user.setId(1L);
		user.setUserId("jarvis");
		user.setUserName("JARVIS");
		user.setSex("m");
		user.setEmail("xxx@163.com");
		user.setMobile("10000000");
		user.setTelephone("xxxxxxx");
		user.setBirthday(new Date());
		user.setIdCardNumber("100");
		Date date = new Date();
		user.setLastLoginDate(date);
		user.setRemark("测试");
		user.setStatus(1);
		user.setCreateDate(date);
		user.setUserId("jarvis");
		user.setUserName("JARVIS");
		user.setLastModifyDate(date);
		user.setLastModifyUserId("jarvis");
		user.setLastModifyUserName("JARVIS");

		print("generateInsertSql", SqlGenerator.generateInsertSql(user));

	}

	@Test
	public void generateBatchInsertSql() {
		List<User> users = new ArrayList<User>();
		for (int i = 1; i <= 5; i++) {
			User user = new User();
			users.add(user);
			user.setId((long) i);
			user.setUserId("jarvis" + i);
			user.setUserName("JARVIS" + i);
			user.setSex("m");
			user.setEmail("xxx@163.com" + i);
			user.setMobile("10000000" + i);
			user.setTelephone("xxxxxxx" + i);
			user.setBirthday(new Date());
			user.setIdCardNumber("100" + i);
			Date date = new Date();
			user.setLastLoginDate(date);
			user.setRemark("测试" + i);
			user.setStatus(1 + i);
			user.setCreateDate(date);
			user.setUserId("jarvis" + i);
			user.setUserName("JARVIS" + i);
			user.setLastModifyDate(date);
			user.setLastModifyUserId("jarvis" + i);
			user.setLastModifyUserName("JARVIS" + i);
		}

		print("generateBatchInsertSql", SqlGenerator.generateBatchInsertSql(users));
	}

	@Test
	public void generateDeleteByKeysSql() {
		print("generateDeleteByKeysSql", SqlGenerator.generateDeleteByKeysSql(new User(), new Integer[]{1,2,3,4,5}));
	}

	@Test
	public void generateDeleteSql() {
		User condition = new User();
		condition.setId(100L);
		condition.setUserId("userId");
		condition.setUserName("userName");
		print("generateDeleteSql", SqlGenerator.generateDeleteSql(condition));
	}

	@Test
	public void generateUpdateByKeysSql() {
		//print("generateUpdateByKeysSql", SqlGenerator.generateUpdateByKeysSql(new User(), 1));
		print("generateUpdateByKeysSql", SqlGenerator.generateUpdateByKeysSql(new User(), new Integer[]{1,2,3,4,5}));
	}

	@Test
	public void generateUpdateSql() {
		print("generateUpdateSql", SqlGenerator.generateUpdateSql(new User(), new User()));
	}

	@Test
	public void generateSelectByKeysSql() {
		print("generateSelectByKeysSql", SqlGenerator.generateSelectByKeysSql(new User(), new Integer[]{1,2,3,4,5}));
	}

	@Test
	public void generateSelectSql() {
		print("generateSelectSql", SqlGenerator.generateSelectSql(new User(), 1, 10));
	}
	
	@Test
	public void generateSelectCountSql() {
		print("generateSelectCountSql", SqlGenerator.generateSelectCountSql(new User()));
	}

	private void print(String messagePrefix, SqlEntry sqlEntry) {
		if (sqlEntry == null) {
			LOGGER.info(messagePrefix + " sqlEntry is null");
		}
		LOGGER.info(messagePrefix + " sql -> " + (sqlEntry.getSql() == null ? "NULL" : sqlEntry.getSql()));
		StringBuilder paramsString = new StringBuilder();
		for (Object param : sqlEntry.getParams()) {
			paramsString.append(param == null ? "NULL" : param.toString()).append(",");
		}
		LOGGER.info(messagePrefix + " params -> " + paramsString.toString());
	}
}