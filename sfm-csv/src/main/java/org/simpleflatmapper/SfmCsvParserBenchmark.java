package org.simpleflatmapper;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.lightningcsv.CsvReader;
import org.simpleflatmapper.lightningcsv.parser.CellConsumer;
import org.simpleflatmapper.lightningcsv.parser.StringArrayCellConsumer;
import org.simpleflatmapper.lightningcsv.parser.StringArrayCellConsumerNoCopyFixedLength;
import org.simpleflatmapper.param.CsvParam;
import org.simpleflatmapper.util.CheckedConsumer;
import org.simpleflatmapper.util.Consumer;
import org.simpleflatmapper.util.ErrorHelper;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class SfmCsvParserBenchmark {

    @Param(value={"false"})
    public boolean trim;

    @Param(value = {"4"})
    public int bufferSize;

    CsvParser.DSL dsl;
    private CsvParser.MapToDSL<City> mapToDSL;

    @Setup
    public void setUp() {
        dsl = getDsl(bufferSize, trim);
        mapToDSL = dsl.mapTo(City.class);
    }

    @Benchmark
    public void mapCsvCallback(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = csvParam.getReader()) {
            mapToDSL.forEach(reader, blackhole::consume);
        }
    }

    @Benchmark
    public void parseCsvCallback(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = csvParam.getReader()) {
            dsl.reader(reader).read(blackhole::consume);
        }
    }

    @Benchmark
    public void parseCsvIterate(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = csvParam.getReader()) {
            for(String[] row : dsl.reader(reader)) {
                blackhole.consume(row);
            }
        }
    }

    @Benchmark
    public void parseCsvSameArray(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = csvParam.getReader()) {
            CsvReader csvReader = dsl.reader(reader);
            csvReader.parseRow(StringArrayCellConsumer.newInstance(vals -> {
                CellConsumer cellConsumer = new StringArrayCellConsumerNoCopyFixedLength<>(blackhole::consume, vals.length);
                blackhole.consume(vals);
                csvReader.parseAll(cellConsumer);
            }));
        }
    }

    @Benchmark
    public void parseCsvRaw(Blackhole blackhole, CsvParam csvParam) throws IOException {
        CellConsumer cellConsumer = (chars, offset, length) -> {
            if (length > 0)
                blackhole.consume(chars[offset + length - 1]);
        };
        try(Reader reader = csvParam.getReader()) {
            dsl.reader(reader).parseAll(cellConsumer);
        }
    }

    private static CsvParser.DSL getDsl(int bufferSize, boolean trim) {
        CsvParser.DSL dsl = CsvParser.bufferSize(bufferSize * 1024);
        if (trim) {
            dsl = dsl.trimSpaces();
        }
        return dsl;
    }

}
