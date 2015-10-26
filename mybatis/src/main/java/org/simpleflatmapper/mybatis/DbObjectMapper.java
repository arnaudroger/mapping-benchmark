package org.simpleflatmapper.mybatis;

import org.apache.ibatis.annotations.Select;
import org.simpleflatmapper.beans.MappedObject16;
import org.simpleflatmapper.beans.MappedObject4;

import java.util.List;

public interface DbObjectMapper {
	 String SELECT_4_WITH_LIMIT = "SELECT * FROM TEST_SMALL_BENCHMARK_OBJECT LIMIT #{limit}";
	 String SELECT_16_WITH_LIMIT = "SELECT * FROM TEST_BENCHMARK_OBJECT_16 LIMIT #{limit}";


	 @Select(SELECT_4_WITH_LIMIT)
	 List<MappedObject4> select4Fields(int limit);
	 @Select(SELECT_16_WITH_LIMIT)
	 List<MappedObject16> select16Fields(int limit);
}
