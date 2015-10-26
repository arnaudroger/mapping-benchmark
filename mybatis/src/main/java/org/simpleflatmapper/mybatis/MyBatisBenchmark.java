package org.simpleflatmapper.mybatis;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.db.ConnectionParam;
import org.simpleflatmapper.db.DbTarget;
import org.simpleflatmapper.param.LimitParam;


@State(Scope.Benchmark)
public class MyBatisBenchmark  {


	private SqlSessionFactory sqlSessionFactory;
	@Param(value="MOCK")
	DbTarget db;
	
	@Setup
	public void init() throws Exception  {
		ConnectionParam connParam = new ConnectionParam();
		connParam.db = db;
		connParam.init();
		this.sqlSessionFactory = SqlSessionFact.getSqlSessionFactory(connParam);
	}
	
	
	@Benchmark
	public void _04Fields(LimitParam limit, final Blackhole blackhole) throws Exception {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.select("select4Fields", limit, new ResultHandler() {
				@Override
				public void handleResult(ResultContext arg0) {
					blackhole.consume(arg0.getResultObject());
				}
			});
		} finally {
			session.close();
		}
	}

	@Benchmark
	public void _16Fields(LimitParam limit, final Blackhole blackhole) throws Exception {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.select("select16Fields", limit, new ResultHandler() {
				@Override
				public void handleResult(ResultContext arg0) {
					blackhole.consume(arg0.getResultObject());
				}
			});
		} finally {
			session.close();
		}
	}
}
