package org.walkerljl.db.orm.sql;

import java.util.Date;
import java.util.Objects;

import org.junit.Test;
import org.walkerljl.db.orm.EntityFieldValueUtils;
import org.walkerljl.db.orm.entity.identity.User;
import org.walkerljl.db.orm.sql.SqlEntry;
import org.walkerljl.db.orm.sql.SqlGenerator;

/**
 *
 * SqlGeneratorTest
 *
 * @author lijunlin
 */
public class SqlGeneratorTest {

	@Test
	public void test() {
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
		
		
		SqlEntry sqlEntry = SqlGenerator.generateInsertSql(user);
		if (sqlEntry == null) {
			return;
		}
		System.out.println("INSERT SQL - " + sqlEntry.getSql());
		Object[] values = EntityFieldValueUtils.getFieldValues(user);
		if (values != null) {
			for (Object value : values) {
				System.out.print("," + Objects.toString(value));
			}
		}
	}
}