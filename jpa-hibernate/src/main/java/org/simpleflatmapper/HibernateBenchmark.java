package org.simpleflatmapper;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.db.ConnectionParam;
import org.simpleflatmapper.db.DbTarget;
import org.simpleflatmapper.jpa.beans.JPATester;
import org.simpleflatmapper.param.LimitParam;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@State(Scope.Benchmark)
/**
 * batoo has a dependency on a old library of asm.
 * batoo benchmark cannot run at the sane time as sfm asm.
 * needs to exclude asm from the pom to run batoo.
 * 
 *
 */

/*
Benchmark                 (db)  (limit)   Mode  Cnt       Score      Error  Units
BatooBenchmark._04Fields    H2        1  thrpt   20  424105.769 ± 7998.794  ops/s
BatooBenchmark._04Fields    H2       10  thrpt   20  184095.607 ± 2349.584  ops/s
BatooBenchmark._04Fields    H2      100  thrpt   20   26205.799 ±  371.146  ops/s
BatooBenchmark._04Fields    H2     1000  thrpt   20    2667.718 ±   56.348  ops/s
BatooBenchmark._16Fields    H2        1  thrpt   20  172638.194 ± 3858.660  ops/s
BatooBenchmark._16Fields    H2       10  thrpt   20   64638.056 ± 4692.277  ops/s
BatooBenchmark._16Fields    H2      100  thrpt   20    8910.878 ±  731.142  ops/s
BatooBenchmark._16Fields    H2     1000  thrpt   20     916.999 ±   40.375  ops/s

 */
public class HibernateBenchmark {

	private EntityManagerFactory sf;
	
	@Param(value="H2")
	DbTarget db;


	@Setup
	public void init() throws Exception  {
		ConnectionParam connParam = new ConnectionParam();
		connParam.db = db;
		connParam.init();
		sf = Persistence.createEntityManagerFactory("jpa");
	}

	@Benchmark
	public void _04Fields(LimitParam limit, final Blackhole blackhole) throws Exception {
		JPATester.select4Fields(sf, limit, blackhole);
	}

	@Benchmark
	public void _16Fields(LimitParam limit, final Blackhole blackhole) throws Exception {
		JPATester.select16Fields(sf, limit, blackhole);
	}
	
	public static void main(String[] args) throws Exception {
		ConnectionParam connParam = new ConnectionParam();
		connParam.db = DbTarget.HSQLDB;
		connParam.init();
		

		EntityManagerFactory sf = Persistence.createEntityManagerFactory("jpa" );

		EntityManager session = sf.createEntityManager();
		try {
			Query query = session.createQuery("select s from MappedObject4 s");
			query.setMaxResults(2);
			List<?> sr = query.getResultList();
			for (Object o : sr) {
				System.out.println( o.toString());
			}
		} finally {
			session.close();
		}
	}

}
