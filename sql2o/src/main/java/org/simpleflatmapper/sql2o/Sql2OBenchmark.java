package org.simpleflatmapper.sql2o;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.beans.MappedObject16;
import org.simpleflatmapper.beans.MappedObject4;
import org.simpleflatmapper.db.ConnectionParam;
import org.simpleflatmapper.db.DbTarget;
import org.simpleflatmapper.param.LimitParam;
import org.sql2o.ResultSetIterable;
import org.sql2o.Sql2o;

@State(Scope.Benchmark)
public class Sql2OBenchmark {

	public static final String SELECT_OBJECT4  = MappedObject4.SELECT_WITH_LIMIT.replace("?", ":limit");
	public static final String SELECT_OBJECT16 = MappedObject16.SELECT_WITH_LIMIT.replace("?", ":limit");

	private Sql2o sql2o;
	
	@Param(value="MOCK")
	DbTarget db;

	public Sql2OBenchmark() {
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
			ResultSetIterable<MappedObject4> resultSetIterable = conn.createQuery(SELECT_OBJECT4)
					.addParameter("limit", limit.limit)
					.addColumnMapping("YEAR_STARTED", "yearStarted")
					.executeAndFetchLazy(MappedObject4.class);
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
			ResultSetIterable<MappedObject16> resultSetIterable = conn.createQuery(SELECT_OBJECT16)
					.addParameter("limit", limit.limit)
					.addColumnMapping("YEAR_STARTED", "yearStarted")
					.executeAndFetchLazy(MappedObject16.class);
			for(MappedObject16 o : resultSetIterable) {
				blackhole.consume(o);
			}
		} finally {
			conn.close();
		}
	}
}
