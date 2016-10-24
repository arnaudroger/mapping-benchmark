package org.simpleflatmapper;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.param.CsvParam;

import java.io.IOException;
import java.io.Reader;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class SfmCsvParserBenchmark {
    @Param(value = {"4"})
    public int bufferSize;
    @Benchmark
    public void parseCsvCallback(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = csvParam.getReader()) {
            CsvParser.bufferSize(bufferSize * 1024).reader(reader).read(blackhole::consume);
        }
    }

    @Benchmark
    public void parseCsvIterate(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = csvParam.getReader()) {
            for(String[] row : CsvParser.bufferSize(bufferSize * 1024).reader(reader)) {
                blackhole.consume(row);
            }
        }
    }


}
