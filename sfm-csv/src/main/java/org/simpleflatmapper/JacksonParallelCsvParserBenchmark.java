package org.simpleflatmapper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.param.Csv;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@State(Scope.Benchmark)
public class JacksonParallelCsvParserBenchmark {
    private ExecutorService executorService;


    @Param(value = {"32"})
    public int parallelBufferSize;

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
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.WRAP_AS_ARRAY);

        try(Reader reader = Csv.getParallelReader(executorService, parallelBufferSize)) {
            MappingIterator<String[]> iterator = csvMapper.readerFor(String[].class).readValues(reader);

            while (iterator.hasNext()) {
                blackhole.consume(iterator.next());
            }
        }
    }

    @Benchmark
    public void parseCsvQuotes(Blackhole blackhole) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.WRAP_AS_ARRAY);

        try(Reader reader = Csv.getParallelReaderQuotes(executorService, parallelBufferSize)) {
            MappingIterator<String[]> iterator = csvMapper.readerFor(String[].class).readValues(reader);

            while (iterator.hasNext()) {
                blackhole.consume(iterator.next());
            }
        }
    }
}
