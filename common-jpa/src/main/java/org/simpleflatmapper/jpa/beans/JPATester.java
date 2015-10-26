package org.simpleflatmapper.jpa.beans;

import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.param.LimitParam;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * Created by aroger on 17/10/2015.
 */
public class JPATester {


    public static void select4Fields(EntityManagerFactory entityManagerFactory, LimitParam limitParam, Blackhole blackhole) {
        select(entityManagerFactory, limitParam, blackhole, "select s from MappedObject4 s");
    }

    public static void select16Fields(EntityManagerFactory entityManagerFactory, LimitParam limitParam, Blackhole blackhole) {
        select(entityManagerFactory, limitParam, blackhole, "select s from MappedObject16 s");
    }

    public static void select(EntityManagerFactory entityManagerFactory, LimitParam limit, Blackhole blackhole, String strQuery)  {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery(strQuery);
            query.setMaxResults(limit.limit);
            List<?> sr = query.getResultList();
            for (Object o : sr) {
                blackhole.consume(o);
            }
        } finally {
            entityManager.close();
        }
    }
}
