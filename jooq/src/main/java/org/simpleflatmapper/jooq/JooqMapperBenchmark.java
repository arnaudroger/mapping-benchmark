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

import java.sql.ResultSet;
import java.util.List;

@State(Scope.Benchmark)
public class JooqMapperBenchmark {

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
		dsl = DSL.using(new DefaultConfiguration().set(cp.dataSource).set(getSqlDialect(db)));

		select4 = dsl.selectFrom(TestSmallBenchmarkObject.TEST_SMALL_BENCHMARK_OBJECT);
		select16 = dsl.selectFrom(TestBenchmarkObject_16.TEST_BENCHMARK_OBJECT_16);

	}

	public static SQLDialect getSqlDialect(DbTarget db) {
		switch (db) {
			case MYSQL:
			case MOCK:
				return SQLDialect.MYSQL;
			case H2:
				return SQLDialect.H2;
			case HSQLDB:
				return SQLDialect.HSQLDB;
		}
		throw new IllegalArgumentException();

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
}
