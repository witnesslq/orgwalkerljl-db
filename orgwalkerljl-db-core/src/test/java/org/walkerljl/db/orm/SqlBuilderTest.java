package org.walkerljl.db.orm;

import org.junit.Test;

/**
 *
 * SqlBuilderTest
 *
 * @author lijunlin
 */
public class SqlBuilderTest {

	@Test
	public void generateSql() {
		OrmTestBean bean = new OrmTestBean();
		bean.setId(1L);
		//System.out.println(SqlBuilder.generateInsertSql(bean));
		//System.out.println(SqlBuilder.generateDeleteByIdSql(bean));
	}
}
