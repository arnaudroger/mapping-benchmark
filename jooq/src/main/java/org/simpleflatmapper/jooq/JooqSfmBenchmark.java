package org.simpleflatmapper.jooq;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.SelectWhereStep;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.simpleflatmapper.jooq.SfmRecordMapperProvider;
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
public class JooqSfmBenchmark {


	@Param(value = "H2")
	private DbTarget db;
	private DSLContext dsl;


	private SelectWhereStep<TestSmallBenchmarkObjectRecord> select4;
	private SelectWhereStep<TestBenchmarkObject_16Record> select16;

	private JdbcMapper<MappedObject4> mapper4 = JdbcMapperFactory.newInstance().newMapper(MappedObject4.class);
	private JdbcMapper<MappedObject16> mapper16 = JdbcMapperFactory.newInstance().newMapper(MappedObject16.class);

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
		try (ResultSet result = select4.limit(limit.limit).fetchResultSet()) {
			mapper4.forEach(result, blackhole::consume);
		}
	}

	@Benchmark
	public void _16Fields(LimitParam limit, final Blackhole blackhole, ConnectionParam connectionParam) throws Exception {
		try (ResultSet result = select16.limit(limit.limit).fetchResultSet()) {
			mapper16.forEach(result, blackhole::consume);
		}
	}

}
