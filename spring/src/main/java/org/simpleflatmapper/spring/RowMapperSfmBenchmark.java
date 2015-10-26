package org.simpleflatmapper.spring;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.sfm.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.beans.MappedObject16;
import org.simpleflatmapper.beans.MappedObject4;
import org.simpleflatmapper.db.ConnectionParam;
import org.simpleflatmapper.db.ResultSetHandler;
import org.simpleflatmapper.param.LimitParam;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;


/*

Benchmark                                 (db)  (limit)   Mode  Cnt     Score     Error  Units
RowMapperBeanPropertyBenchmark._04Fields    H2     1000  thrpt   20   380.087 ±  24.794  ops/s
RowMapperBeanPropertyBenchmark._16Fields    H2     1000  thrpt   20    80.582 ±   3.645  ops/s
RowMapperSfmBenchmark._04Fields             H2     1000  thrpt   20  3515.758 ± 152.728  ops/s
RowMapperSfmBenchmark._16Fields             H2     1000  thrpt   20  1019.907 ±  48.610  ops/s

 */
@State(Scope.Benchmark)
public class RowMapperSfmBenchmark {
	private RowMapper<MappedObject4> mapper4;
	private RowMapper<MappedObject16> mapper16;

	@Setup
	public void init() {
		mapper4 = JdbcTemplateMapperFactory.newInstance().newRowMapper(MappedObject4.class);
		mapper16 = JdbcTemplateMapperFactory.newInstance().newRowMapper(MappedObject16.class);
	}
	
	@Benchmark
	public void _04Fields(ConnectionParam connectionHolder, LimitParam limit, final Blackhole blackhole) throws Exception {
		connectionHolder.executeStatement(
				MappedObject4.SELECT_WITH_LIMIT,
				(rs) -> {
						int i = 0;
						while (rs.next()) {
							blackhole.consume(mapper4.mapRow(rs, i));
							i++;
						}
					}
				,
				limit.limit
		);
	}

	@Benchmark
	public void _16Fields(ConnectionParam connectionHolder, LimitParam limit, final Blackhole blackhole) throws Exception {
		connectionHolder.executeStatement(
				MappedObject16.SELECT_WITH_LIMIT,
				(rs) -> {
						int i = 0;
						while (rs.next()) {
							blackhole.consume(mapper16.mapRow(rs, i));
							i++;
						}
					}
				,
				limit.limit
		);
	}

}
