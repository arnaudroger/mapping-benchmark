package org.simpleflatmapper;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.param.CsvParam;

import java.io.IOException;
import java.io.Reader;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class JacksonCsvParserBenchmark {


    CsvMapper csvMapperToStringArray;
    ObjectReader cityReader;
    @Setup
    public void setUp() {
        csvMapperToStringArray = new CsvMapper();
        csvMapperToStringArray.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.WRAP_AS_ARRAY);

        CsvMapper csvMapperToCity = new CsvMapper();


        csvMapperToCity.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();

        cityReader = csvMapperToCity.readerFor(City.class).with(bootstrapSchema);

    }
    @Benchmark
    public void parseCsv(Blackhole blackhole, CsvParam csvParam) throws IOException {

        try(Reader reader = csvParam.getReader()) {
            MappingIterator<String[]> iterator = csvMapperToStringArray.readerFor(String[].class).readValues(reader);

            while (iterator.hasNext()) {
                blackhole.consume(iterator.next());
            }
        }
    }

    @Benchmark
    public void mapCsv(Blackhole blackhole, CsvParam csvParam) throws IOException {

        try(Reader reader = csvParam.getReader()) {
            MappingIterator<City> iterator = cityReader.readValues(reader);

            while (iterator.hasNext()) {
                blackhole.consume(iterator.next());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        CsvParam csvParam = new CsvParam();
        csvParam.setUp();

        CsvMapper csvMapper = new CsvMapper();

        csvMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();

        try(Reader reader = csvParam.getReader()) {
            MappingIterator<City> iterator = csvMapper.readerFor(City.class).with(bootstrapSchema).readValues(reader);

            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
    }

}
