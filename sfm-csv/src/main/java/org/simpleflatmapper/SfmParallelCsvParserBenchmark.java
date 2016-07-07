package org.simpleflatmapper;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.sfm.csv.CsvParser;
import org.simpleflatmapper.param.Csv;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@State(Scope.Benchmark)
public class SfmParallelCsvParserBenchmark {

    @Param(value = {"8"})
    public int bufferSize;

    @Param(value = {"32"})
    public int parallelBufferSize;

    private ExecutorService executorService;

    @Setup
    public void setUp() {
        executorService = Executors.newFixedThreadPool(1);
    }

    @TearDown
    public void tearDown() {
        executorService.shutdown();
    }
    @Benchmark
    public void parseCsv(Blackhole blackhole) throws IOException {
        try(Reader reader = Csv.getParallelReader(executorService, parallelBufferSize)) {
            CsvParser.bufferSize(bufferSize * 1024).reader(reader).read(blackhole::consume);
        }
    }

    @Benchmark
    public void parseCsvQuotes(Blackhole blackhole) throws IOException {
        try(Reader reader = Csv.getParallelReaderQuotes(executorService, parallelBufferSize)) {
            CsvParser.bufferSize(bufferSize * 1024).reader(reader).read(blackhole::consume);
        }
    }
}
