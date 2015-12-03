/**
     * 生成 select count(*) 语句
     */
    public static String generateSelectSqlForCount(Class<?> entityClass, String condition) {
        StringBuilder sql = new StringBuilder("select count(1) from ").append(getTable(entityClass).getName());
        sql.append(generateWhere(condition));
        return sql.toString();
    }

    /**
     * 生成 select 分页语句（数据库类型为：mysql、oracle、mssql）
     */
    public static String generateSelectSqlForPager(int pageNumber, int pageSize, Object entity, String condition, String sort) {
        StringBuilder sql = new StringBuilder();
        String table = getTable(entity).getName();
        String where = generateWhere(condition);
        String order = generateOrder(sort);
        String dbType = null;//DbUtils.getDatabaseType();
        if (dbType.equalsIgnoreCase("mysql")) {
            int pageStart = (pageNumber - 1) * pageSize;
            appendSqlForMySql(sql, table, where, order, pageStart, pageSize);
        } else if (dbType.equalsIgnoreCase("oracle")) {
            int pageStart = (pageNumber - 1) * pageSize + 1;
            int pageEnd = pageStart + pageSize;
            appendSqlForOracle(sql, table, where, order, pageStart, pageEnd);
        } else if (dbType.equalsIgnoreCase("mssql")) {
            int pageStart = (pageNumber - 1) * pageSize;
            appendSqlForMsSql(sql, table, where, order, pageStart, pageSize);
        }
        return sql.toString();
    }

    private static Table getTable(Object entity) {
    	Table table = TableManager.getInstance().getTable(entity.getClass());
    	if (table == null) {
    		String errorMessage = String.format("Get null table object by entity : %s", entity.getClass());
    		LOGGER.error(errorMessage);
    		throw new SqlGenerateException(errorMessage);
    	}
    	return table;
    }

    private static String generateWhere(String condition) {
        String where = "";
        if (StringUtils.isNotEmpty(condition)) {
            where += " where " + condition;
        }
        return where;
    }

    private static String generateOrder(String sort) {
        String order = "";
        if (StringUtils.isNotEmpty(sort)) {
            order += " order by " + sort;
        }
        return order;
    }

    private static void appendSqlForMySql(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select * from 表名 where 条件 order by 排序 limit 开始位置, 结束位置
         */
        sql.append("select * from ").append(table);
        sql.append(where);
        sql.append(order);
        sql.append(" limit ").append(pageStart).append(", ").append(pageEnd);
    }

    private static void appendSqlForOracle(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select a.* from (
                select rownum rn, t.* from 表名 t where 条件 order by 排序
            ) a
            where a.rn >= 开始位置 and a.rn < 结束位置
        */
        sql.append("select a.* from (select rownum rn, t.* from ").append(table).append(" t");
        sql.append(where);
        sql.append(order);
        sql.append(") a where a.rn >= ").append(pageStart).append(" and a.rn < ").append(pageEnd);
    }

    private static void appendSqlForMsSql(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select top 结束位置 * from 表名 where 条件 and id not in (
                select top 开始位置 id from 表名 where 条件 order by 排序
            ) order by 排序
        */
        sql.append("select top ").append(pageEnd).append(" * from ").append(table);
        if (StringUtils.isNotEmpty(where)) {
            sql.append(where).append(" and ");
        } else {
            sql.append(" where ");
        }
        sql.append("id not in (select top ").append(pageStart).append(" id from ").append(table);
        sql.append(where);
        sql.append(order);
        sql.append(") ").append(order);
    }