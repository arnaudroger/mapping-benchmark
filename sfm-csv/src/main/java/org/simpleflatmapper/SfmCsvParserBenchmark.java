package org.simpleflatmapper;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.sfm.csv.CsvParser;
import org.simpleflatmapper.param.Csv;

import java.io.IOException;
import java.io.Reader;

public class SfmCsvParserBenchmark {
    public static final int BUFFER_SIZE = 1024 * 32;
    @Benchmark
    public void parseCsv(Blackhole blackhole) throws IOException {
        try(Reader reader = Csv.getReader()) {
            CsvParser.bufferSize(BUFFER_SIZE).reader(reader).read(blackhole::consume);
        }
    }

    @Benchmark
    public void parseCsvQuotes(Blackhole blackhole) throws IOException {
        try(Reader reader = Csv.getReaderQuotes()) {
            CsvParser.bufferSize(BUFFER_SIZE).reader(reader).read(blackhole::consume);
        }
    }

}
