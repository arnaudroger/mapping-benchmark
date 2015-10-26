package org.simpleflatmapper.spring;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.db.ConnectionParam;
import org.simpleflatmapper.param.LimitParam;
import org.simpleflatmapper.spring.beans.MappedObject16;
import org.simpleflatmapper.spring.beans.MappedObject4;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.roma.impl.service.RowMapperService;


@State(Scope.Benchmark)
public class RowMapperRomaBenchmark {
	private RowMapper<MappedObject4> mapper4;
	private RowMapper<MappedObject16> mapper16;

	@Setup
	public void init() {
		RowMapperService rowMapperService = RomaMapperFactory.getRowMapperService();
		this.mapper4 = rowMapperService.getRowMapper(MappedObject4.class);
		this.mapper16 = rowMapperService.getRowMapper(MappedObject16.class);
	}

	@Benchmark
	public void _04Fields(ConnectionParam connectionHolder, LimitParam limit, final Blackhole blackhole) throws Exception {
		connectionHolder.executeStatement(org.simpleflatmapper.beans.MappedObject4.SELECT_WITH_LIMIT,
				(rs) -> {
						int i = 0;
						while(rs.next()) {
							Object o = mapper4.mapRow(rs, i);
							blackhole.consume(o);
							i++;
						}
					}
				, limit.limit);
	}
	@Benchmark
	public void _16Fields(ConnectionParam connectionHolder, LimitParam limit, final Blackhole blackhole) throws Exception {
		connectionHolder.executeStatement(org.simpleflatmapper.beans.MappedObject16.SELECT_WITH_LIMIT,
				(rs) -> {
						int i = 0;
						while(rs.next()) {
							Object o = mapper16.mapRow(rs, i);
							blackhole.consume(o);
							i++;
						}
					}
				, limit.limit);
	}
}
