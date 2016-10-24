package org.simpleflatmapper.datastax;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.simpleflatmapper.datastax.DatastaxMapper;
import org.simpleflatmapper.datastax.DatastaxMapperFactory;
import org.simpleflatmapper.beans.Object4Fields;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by aroger on 19/10/2015.
 */
public class DatastaxHelper {

    public static final int NB_ROWS = 10000;
    Cluster cluster;
    Session session;

    Mapper<Object4Fields> datastaxMapper;

    DatastaxMapper<Object4Fields> sfmMapper;
    PreparedStatement select4PreparedStatement;
    PreparedStatement select16PreparedStatement;

    public DatastaxHelper() throws InterruptedException, TTransportException, ConfigurationException, IOException {
        cluster =
                Cluster
                        .builder()
                        .addContactPointsWithPorts(
                                Arrays.asList(new InetSocketAddress("localhost", 9042)))
                        .build();

        if (cluster.getMetadata().getKeyspace("testsfm") == null) {
            cluster.connect().execute("create keyspace testsfm WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }");
        }

        this.session = cluster.connect("testsfm");

        this.session.execute("create table if not exists test_table  (id bigint primary key, year_started int, name varchar, email varchar)");
        this.session.execute("create table if not exists test_table_16  (" +
                "id bigint primary key, year_started int, name varchar, email varchar," +
                "field5 int, field6 int, field7 bigint, field8 float, field9 double," +
                "field10 int, field11 int, field12 bigint, field13 float, field14 double," +
                "field15 int, field16 int)");



        if (this.session.execute("select * from test_table").isExhausted()) {
            for( int i  = 0; i < NB_ROWS; i++) {
                this.session.execute("insert into test_table(id, year_started, name, email) values (" + i + ", 1978, 'Arnaud Roger', 'arnaud.roger@gmail.com')");
            }
        }

        if (this.session.execute("select * from test_table_16").isExhausted()) {
            for( int i  = 0; i < NB_ROWS; i++) {
                this.session.execute("insert into test_table_16(id, year_started, name, email, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16) " +
                        "values (" + i + ", 1978, 'Arnaud Roger', 'arnaud.roger@gmail.com', " +
                        "" + i + "," + i + "," + i + "," + i + "," + i + "," +
                        "" + i + "," + i + "," + i + "," + i + "," + i + "," +
                        "" + i + "," + i + ")");
            }
        }

        datastaxMapper = new MappingManager(this.session).mapper(Object4Fields.class);
        sfmMapper = DatastaxMapperFactory.newInstance().mapTo(Object4Fields.class);

        select4PreparedStatement = this.session.prepare("select id, year_started, name, email from test_table LIMIT ?");
        select16PreparedStatement = this.session.prepare("select id, year_started, name, email, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 from test_table_16 LIMIT ?");

    }

    public ResultSet select4Fields(int limit) {
        return session.execute(select4PreparedStatement.bind(limit));
    }

    public ResultSet select16Fields(int limit) {
        return session.execute(select16PreparedStatement.bind(limit));
    }

    public void close() {
        if (session != null) {
            session.close();
        }
        if (cluster != null) {
            cluster.close();
        }
    }

    public static void main(String[] args) throws InterruptedException, TTransportException, ConfigurationException, IOException {
        DatastaxHelper helper = new DatastaxHelper();

        helper.select4Fields(2).forEach(System.out::println);
        helper.select16Fields(2).forEach(System.out::println);

        helper.close();

    }
}
