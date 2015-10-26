package org.simpleflatmapper.mybatis;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.simpleflatmapper.db.ConnectionParam;

public class SqlSessionFact {

	public static SqlSessionFactory getSqlSessionFactory(ConnectionParam connParam) {
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, connParam.dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(DbObjectMapper.class);
		SqlSessionFactory sqlSession = new SqlSessionFactoryBuilder().build(configuration);
		return sqlSession;
	}

}
