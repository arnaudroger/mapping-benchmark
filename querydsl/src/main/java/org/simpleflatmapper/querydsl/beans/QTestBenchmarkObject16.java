package org.simpleflatmapper.querydsl.beans;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QTestBenchmarkObject16 is a Querydsl query type for QTestBenchmarkObject16
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QTestBenchmarkObject16 extends com.mysema.query.sql.RelationalPathBase<QTestBenchmarkObject16> {

    private static final long serialVersionUID = 214851178;

    public static final QTestBenchmarkObject16 testBenchmarkObject16 = new QTestBenchmarkObject16("TEST_BENCHMARK_OBJECT_16");

    public final StringPath email = createString("email");

    public final NumberPath<Short> field10 = createNumber("field10", Short.class);

    public final NumberPath<Integer> field11 = createNumber("field11", Integer.class);

    public final NumberPath<Long> field12 = createNumber("field12", Long.class);

    public final NumberPath<Float> field13 = createNumber("field13", Float.class);

    public final NumberPath<Double> field14 = createNumber("field14", Double.class);

    public final NumberPath<Integer> field15 = createNumber("field15", Integer.class);

    public final NumberPath<Integer> field16 = createNumber("field16", Integer.class);

    public final NumberPath<Short> field5 = createNumber("field5", Short.class);

    public final NumberPath<Integer> field6 = createNumber("field6", Integer.class);

    public final NumberPath<Long> field7 = createNumber("field7", Long.class);

    public final NumberPath<Float> field8 = createNumber("field8", Float.class);

    public final NumberPath<Double> field9 = createNumber("field9", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> yearStarted = createNumber("yearStarted", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<QTestBenchmarkObject16> primary = createPrimaryKey(id);

    public QTestBenchmarkObject16(String variable) {
        super(QTestBenchmarkObject16.class, forVariable(variable), "null", "test_benchmark_object_16");
        addMetadata();
    }

    public QTestBenchmarkObject16(String variable, String schema, String table) {
        super(QTestBenchmarkObject16.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTestBenchmarkObject16(Path<? extends QTestBenchmarkObject16> path) {
        super(path.getType(), path.getMetadata(), "null", "test_benchmark_object_16");
        addMetadata();
    }

    public QTestBenchmarkObject16(PathMetadata<?> metadata) {
        super(QTestBenchmarkObject16.class, metadata, "null", "test_benchmark_object_16");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(email, ColumnMetadata.named("email").withIndex(3).ofType(Types.VARCHAR).withSize(100));
        addMetadata(field10, ColumnMetadata.named("field10").withIndex(10).ofType(Types.SMALLINT).withSize(5));
        addMetadata(field11, ColumnMetadata.named("field11").withIndex(11).ofType(Types.INTEGER).withSize(10));
        addMetadata(field12, ColumnMetadata.named("field12").withIndex(12).ofType(Types.BIGINT).withSize(19));
        addMetadata(field13, ColumnMetadata.named("field13").withIndex(13).ofType(Types.REAL).withSize(12));
        addMetadata(field14, ColumnMetadata.named("field14").withIndex(14).ofType(Types.DOUBLE).withSize(22));
        addMetadata(field15, ColumnMetadata.named("field15").withIndex(15).ofType(Types.INTEGER).withSize(10));
        addMetadata(field16, ColumnMetadata.named("field16").withIndex(16).ofType(Types.INTEGER).withSize(10));
        addMetadata(field5, ColumnMetadata.named("field5").withIndex(5).ofType(Types.SMALLINT).withSize(5));
        addMetadata(field6, ColumnMetadata.named("field6").withIndex(6).ofType(Types.INTEGER).withSize(10));
        addMetadata(field7, ColumnMetadata.named("field7").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(field8, ColumnMetadata.named("field8").withIndex(8).ofType(Types.REAL).withSize(12));
        addMetadata(field9, ColumnMetadata.named("field9").withIndex(9).ofType(Types.DOUBLE).withSize(22));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(100));
        addMetadata(yearStarted, ColumnMetadata.named("year_started").withIndex(4).ofType(Types.INTEGER).withSize(10));
    }

}

