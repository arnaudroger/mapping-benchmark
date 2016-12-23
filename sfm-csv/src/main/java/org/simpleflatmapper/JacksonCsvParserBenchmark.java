package org.simpleflatmapper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.param.CsvParam;

import java.io.IOException;
import java.io.Reader;

@BenchmarkMode(Mode.AverageTime)
public class JacksonCsvParserBenchmark {

    @Benchmark
    public void parseCsv(Blackhole blackhole, CsvParam csvParam) throws IOException {
        CsvMapper csvMapper = new CsvMapper();

        try(Reader reader = csvParam.getReader()) {
            MappingIterator<String[]> iterator = csvMapper.readerFor(String[].class).readValues(reader);

            while (iterator.hasNext()) {
                blackhole.consume(iterator.next());
            }
        }
    }

    @Benchmark
    public void mapCsv(Blackhole blackhole, CsvParam csvParam) throws IOException {
        CsvMapper csvMapper = new CsvMapper();

        CsvSchema bootstrapSchema = CsvSchema
                .builder()
                .addColumn("country")
                .addColumn("city")
                .addColumn("accentCity")
                .addColumn("region")
                .addColumn("population")
                .addColumn("latitude")
                .addColumn("longitude")
                .setSkipFirstDataRow(true)
                .build();

        try(Reader reader = csvParam.getReader()) {
            MappingIterator<City> iterator = csvMapper.readerFor(City.class).with(bootstrapSchema).readValues(reader);

            while (iterator.hasNext()) {
                blackhole.consume(iterator.next());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        CsvParam csvParam = new CsvParam();
        csvParam.setUp();

        CsvMapper csvMapper = new CsvMapper();

        CsvSchema bootstrapSchema = CsvSchema
                .builder()
                .addColumn("country")
                .addColumn("city")
                .addColumn("accentCity")
                .addColumn("region")
                .addColumn("population")
                .addColumn("latitude")
                .addColumn("longitude")
                .setSkipFirstDataRow(true)
                .build();

        try(Reader reader = csvParam.getReader()) {
            MappingIterator<City> iterator = csvMapper.readerFor(City.class).with(bootstrapSchema).readValues(reader);

            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
    }

}
