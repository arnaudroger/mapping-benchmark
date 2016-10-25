package org.simpleflatmapper;

import com.sun.org.apache.regexp.internal.RE;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.csv.CsvReader;
import org.simpleflatmapper.csv.parser.ConfigurableCharConsumer;
import org.simpleflatmapper.csv.parser.ReaderCharBuffer;
import org.simpleflatmapper.csv.parser.TextFormat;
import org.simpleflatmapper.csv.parser.UnescapeCellTransformer;
import org.simpleflatmapper.param.CsvParam;

import java.io.IOException;
import java.io.Reader;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class SfmConfigurableCsvParserBenchmark {
    @Param(value = {"4"})
    public int bufferSize;
    @Benchmark
    public void parseCsvCallback(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = csvParam.getReader()) {
            reader(reader).read(blackhole::consume);
        }
    }

    private CsvReader reader(Reader reader) {
        ReaderCharBuffer charBuffer = new ReaderCharBuffer(bufferSize * 1024, CsvParser.DEFAULT_MAX_BUFFER_SIZE_8M, reader);
        return new CsvReader(new ConfigurableCharConsumer(charBuffer, new TextFormat(',', '"'), new UnescapeCellTransformer('"')));
    }

    @Benchmark
    public void parseCsvIterate(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = csvParam.getReader()) {
            for(String[] row : reader(reader)) {
                blackhole.consume(row);
            }
        }
    }


}
