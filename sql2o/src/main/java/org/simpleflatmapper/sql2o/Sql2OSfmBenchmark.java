package org.simpleflatmapper.sql2o;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.sql2o.SfmResultSetHandlerFactoryBuilder;
import org.simpleflatmapper.beans.MappedObject16;
import org.simpleflatmapper.beans.MappedObject4;
import org.simpleflatmapper.db.ConnectionParam;
import org.simpleflatmapper.db.DbTarget;
import org.simpleflatmapper.param.LimitParam;
import org.sql2o.ResultSetHandlerFactory;
import org.sql2o.ResultSetIterable;
import org.sql2o.Sql2o;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@State(Scope.Benchmark)
public class Sql2OSfmBenchmark {

	private final ResultSetHandlerFactory<MappedObject4> factory4;
	private final ResultSetHandlerFactory<MappedObject16> factory16;
	private Sql2o sql2o;

	@Param(value="MOCK")
	DbTarget db;

	public Sql2OSfmBenchmark() {
		Map<String, String> columnMappings = new HashMap<>();
		columnMappings.put("YEAR_STARTED", "yearStarted");

		SfmResultSetHandlerFactoryBuilder builder = new SfmResultSetHandlerFactoryBuilder();
		builder.setColumnMappings(columnMappings);
		factory4 = builder.newFactory(MappedObject4.class);
		factory16 = builder.newFactory(MappedObject16.class);
	}

	@Setup
	public void init() throws Exception  {
		ConnectionParam connParam = new ConnectionParam();
		connParam.db = db;
		connParam.init();
		sql2o = new Sql2o(connParam.dataSource);
	}


	@Benchmark
	public void _04Fields(LimitParam limit, final Blackhole blackhole) throws Exception {

		org.sql2o.Connection conn = sql2o.open();
		try  {
			ResultSetIterable<MappedObject4> resultSetIterable = conn.createQuery(Sql2OBenchmark.SELECT_OBJECT4)
					.addParameter("limit", limit.limit)
					.executeAndFetchLazy(factory4);
			for(MappedObject4 o : resultSetIterable) {
				blackhole.consume(o);
			}
		} finally {
			conn.close();
		}
	}

	@Benchmark
	public void _16Fields(LimitParam limit, final Blackhole blackhole) throws Exception {

		org.sql2o.Connection conn = sql2o.open();
		try  {
			ResultSetIterable<MappedObject16> resultSetIterable = conn.createQuery(Sql2OBenchmark.SELECT_OBJECT16)
					.addParameter("limit", limit.limit)
					.executeAndFetchLazy(factory16);
			for(MappedObject16 o : resultSetIterable) {
				blackhole.consume(o);
			}
		} finally {
			conn.close();
		}
	}

	public static void main(String[] args) throws SQLException, NamingException {
		ConnectionParam connParam = new ConnectionParam();
		connParam.db = DbTarget.H2;
		connParam.init();
		Sql2o sql2o = new Sql2o(connParam.dataSource);
		org.sql2o.Connection conn = sql2o.open();

		Map<String, String> columnMappings = new HashMap<>();
		columnMappings.put("YEAR_STARTED", "yearStarted");
		SfmResultSetHandlerFactoryBuilder builder = new SfmResultSetHandlerFactoryBuilder();
		builder.setColumnMappings(columnMappings);
		ResultSetHandlerFactory<MappedObject16> factory16 = builder.newFactory(MappedObject16.class);

		try  {
			ResultSetIterable<MappedObject16> resultSetIterable = conn.createQuery(Sql2OBenchmark.SELECT_OBJECT16)
					.addParameter("limit", 2)
					.executeAndFetchLazy(factory16);
			for(MappedObject16 o : resultSetIterable) {
				System.out.println("o = " + o);
			}
		} finally {
			conn.close();
		}
	}
}
