package org.simpleflatmapper.querydsl.beans;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QTestSmallBenchmarkObject is a Querydsl query type for QTestSmallBenchmarkObject
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QTestSmallBenchmarkObject extends com.mysema.query.sql.RelationalPathBase<QTestSmallBenchmarkObject> {

    private static final long serialVersionUID = 1873352012;

    public static final QTestSmallBenchmarkObject testSmallBenchmarkObject = new QTestSmallBenchmarkObject("TEST_SMALL_BENCHMARK_OBJECT");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> yearStarted = createNumber("yearStarted", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<QTestSmallBenchmarkObject> primary = createPrimaryKey(id);

    public QTestSmallBenchmarkObject(String variable) {
        super(QTestSmallBenchmarkObject.class, forVariable(variable), "null", "TEST_SMALL_BENCHMARK_OBJECT");
        addMetadata();
    }

    public QTestSmallBenchmarkObject(String variable, String schema, String table) {
        super(QTestSmallBenchmarkObject.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTestSmallBenchmarkObject(Path<? extends QTestSmallBenchmarkObject> path) {
        super(path.getType(), path.getMetadata(), "null", "TEST_SMALL_BENCHMARK_OBJECT");
        addMetadata();
    }

    public QTestSmallBenchmarkObject(PathMetadata<?> metadata) {
        super(QTestSmallBenchmarkObject.class, metadata, "null", "TEST_SMALL_BENCHMARK_OBJECT");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(email, ColumnMetadata.named("email").withIndex(3).ofType(Types.VARCHAR).withSize(100));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(100));
        addMetadata(yearStarted, ColumnMetadata.named("year_started").withIndex(4).ofType(Types.INTEGER).withSize(10));
    }

}

