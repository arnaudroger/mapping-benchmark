package org.simpleflatmapper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.sfm.csv.CsvParser;
import org.simpleflatmapper.param.Csv;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JacksonCsvParserBenchmark {

    @Benchmark
    public void parseCsv(Blackhole blackhole) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.WRAP_AS_ARRAY);

        try(Reader reader = Csv.getReader()) {
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

        try(Reader reader = Csv.getReaderQuotes()) {
            MappingIterator<String[]> iterator = csvMapper.readerFor(String[].class).readValues(reader);

            while (iterator.hasNext()) {
                blackhole.consume(iterator.next());
            }
        }
    }
}
