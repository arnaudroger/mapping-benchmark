package org.simpleflatmapper;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.sfm.csv.CsvParser;
import org.sfm.utils.ParallelReader;
import org.simpleflatmapper.param.Csv;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@State(Scope.Benchmark)
public class SfmParallelCsvParserBenchmark {


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
        try(Reader reader = new ParallelReader(Csv.getReader(), executorService, 1024 * 1024)) {
            CsvParser.bufferSize(1024 * 1024).reader(reader).read(blackhole::consume);
        }
    }

    @Benchmark
    public void parseCsvQuotes(Blackhole blackhole) throws IOException {
        try(Reader reader = new ParallelReader(Csv.getReaderQuotes(), executorService, 1024 * 1024)) {
            CsvParser.bufferSize(1024 * 1024).reader(reader).read(blackhole::consume);
        }
    }

}
