package org.simpleflatmapper;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.sfm.csv.CsvParser;
import org.simpleflatmapper.param.Csv;

import java.io.IOException;
import java.io.Reader;

public class SfmCsvParserBenchmark {

    @Benchmark
    public void parseCsv(Blackhole blackhole) throws IOException {
        try(Reader reader = Csv.getReader()) {
            CsvParser.bufferSize(1024 * 1024).reader(reader).read(blackhole::consume);
        }
    }

    @Benchmark
    public void parseCsvQuotes(Blackhole blackhole) throws IOException {
        try(Reader reader = Csv.getReaderQuotes()) {
            CsvParser.bufferSize(1024 * 1024).reader(reader).read(blackhole::consume);
        }
    }

}
