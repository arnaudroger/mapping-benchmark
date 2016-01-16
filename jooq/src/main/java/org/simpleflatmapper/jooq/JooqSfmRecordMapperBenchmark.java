package org.simpleflatmapper.jooq;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.infra.Blackhole;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.JdbcMapperFactory;
import org.sfm.jooq.SfmRecordMapperProvider;
import org.sfm.utils.RowHandler;
import org.simpleflatmapper.beans.MappedObject16;
import org.simpleflatmapper.beans.MappedObject4;
import org.simpleflatmapper.beans.tables.TestBenchmarkObject_16;
import org.simpleflatmapper.beans.tables.TestSmallBenchmarkObject;
import org.simpleflatmapper.beans.tables.records.TestBenchmarkObject_16Record;
import org.simpleflatmapper.beans.tables.records.TestSmallBenchmarkObjectRecord;
import org.simpleflatmapper.db.ConnectionParam;
import org.simpleflatmapper.db.DbTarget;
import org.simpleflatmapper.param.LimitParam;

import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@State(Scope.Benchmark)
public class JooqSfmRecordMapperBenchmark {


	@Param(value = "H2")
	private DbTarget db;
	private DSLContext dsl;


	private SelectWhereStep<TestSmallBenchmarkObjectRecord> select4;
	private SelectWhereStep<TestBenchmarkObject_16Record> select16;

	@Setup
	public void init() throws Exception {
		ConnectionParam cp = new ConnectionParam();
		cp.db = db;
		cp.init();
		dsl = DSL.using(new DefaultConfiguration().set(cp.dataSource).set(JooqMapperBenchmark.getSqlDialect(db)).set(new SfmRecordMapperProvider()));

		select4 = dsl.selectFrom(TestSmallBenchmarkObject.TEST_SMALL_BENCHMARK_OBJECT);
		select16 = dsl.selectFrom(TestBenchmarkObject_16.TEST_BENCHMARK_OBJECT_16);

	}

	@Benchmark
	public void _04Fields(LimitParam limit, final Blackhole blackhole, ConnectionParam connectionParam) throws Exception {
		List<MappedObject4> result = select4.limit(limit.limit).fetchInto(MappedObject4.class);
		for(MappedObject4 o : result) {
			blackhole.consume(o);
		}
	}

	@Benchmark
	public void _16Fields(LimitParam limit, final Blackhole blackhole, ConnectionParam connectionParam) throws Exception {
		List<MappedObject16> result = select16.limit(limit.limit).fetchInto(MappedObject16.class);
		for(MappedObject16 o : result) {
			blackhole.consume(o);
		}
	}

	public static void main(String[] args) throws SQLException, NamingException {
		ConnectionParam cp = new ConnectionParam();
		cp.db = DbTarget.H2;
		cp.init();
		DSLContext dsl = DSL.using(new DefaultConfiguration().set(cp.dataSource).set(JooqMapperBenchmark.getSqlDialect(cp.db)).set(new SfmRecordMapperProvider()));

		SelectOffsetStep<TestSmallBenchmarkObjectRecord> query = dsl.selectFrom(TestSmallBenchmarkObject.TEST_SMALL_BENCHMARK_OBJECT).limit(1);

		for(MappedObject4 o : query.fetchInto(MappedObject4.class)) {
			System.out.println("o = " + o);
		}
		for(MappedObject4 o : query.fetchInto(MappedObject4.class)) {
			System.out.println("o = " + o);
		}
	}
}
