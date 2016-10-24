package org.simpleflatmapper.sfm;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.simpleflatmapper.util.CheckedConsumer;
import org.simpleflatmapper.beans.MappedObject16;
import org.simpleflatmapper.beans.MappedObject4;
import org.simpleflatmapper.db.ConnectionParam;
import org.simpleflatmapper.db.ResultSetHandler;
import org.simpleflatmapper.param.LimitParam;

import java.sql.ResultSet;

@State(Scope.Benchmark)
public class JdbcSfmDynamicBenchmark {

	private JdbcMapper<MappedObject4> mapper4;
	private JdbcMapper<MappedObject16> mapper16;
	@Setup
	public void init() {
		mapper4 = JdbcMapperFactory.newInstance().newMapper(MappedObject4.class);
		mapper16 = JdbcMapperFactory.newInstance().newMapper(MappedObject16.class);
	}

	@Benchmark
	public void _04Fields(ConnectionParam connectionParam, LimitParam limitParam, final Blackhole blackhole) throws Exception {
		connectionParam.executeStatement(MappedObject4.SELECT_WITH_LIMIT,
				new ResultSetHandler() {
					@Override
					public void handle(ResultSet rs) throws Exception {
						mapper4.forEach(rs, new CheckedConsumer<MappedObject4>() {
							@Override
							public void accept(MappedObject4 mappedObject4) throws Exception {
								blackhole.consume(mappedObject4);
							}
						});
					}
				}, limitParam.limit);
	}
	@Benchmark
	public void _16Fields(ConnectionParam connectionParam, LimitParam limitParam, final Blackhole blackhole) throws Exception {
		connectionParam.executeStatement(MappedObject16.SELECT_WITH_LIMIT,
				new ResultSetHandler() {
					@Override
					public void handle(ResultSet rs) throws Exception {
						mapper16.forEach(rs, new CheckedConsumer<MappedObject16>() {
							@Override
							public void accept(MappedObject16 mappedObject4) throws Exception {
								blackhole.consume(mappedObject4);
							}
						});
					}
				}, limitParam.limit);
	}
}
