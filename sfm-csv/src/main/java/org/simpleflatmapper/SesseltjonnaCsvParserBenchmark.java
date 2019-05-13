package org.simpleflatmapper;

import java.io.Reader;
import java.util.Arrays;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.param.CsvParam;

import com.github.skjolber.stcsv.CsvMapper;
import com.github.skjolber.stcsv.CsvReader;
import com.github.skjolber.stcsv.builder.CsvBuilderException;
import com.github.skjolber.stcsv.sa.StringArrayCsvReader;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class SesseltjonnaCsvParserBenchmark {

    CsvMapper<City> setterWithQuotes;
    
    @Setup
    public void setUp() throws CsvBuilderException {
        setterWithQuotes = CsvMapper.builder(City.class)
                .stringField("Country")
                    .quoted()
                    .required()
                .stringField("City")
                    .quoted()
                    .required()
                .stringField("AccentCity")
                    .quoted()
                    .optional()
                .stringField("Region")
                    .quoted()
                    .optional()
                .longField("Population")
                    .quoted()
                    .optional()
                .doubleField("Latitude")
                    .quoted()
                    .optional()
                .doubleField("Longitude")
                    .quoted()
                    .optional()
                .build();
        
    }
    @Benchmark
    public void parseCsv(Blackhole blackhole, CsvParam csvParam) throws Exception {
        try(Reader reader = csvParam.getReader()) {
            CsvReader<String[]> csvReader = StringArrayCsvReader.builder().build(reader);
            String[] next;
            do {
                next = csvReader.next();
                if(next == null) {
                    break;
                }
                blackhole.consume(next);
            } while(true);
        }
    }

    @Benchmark
    public void mapCsv(Blackhole blackhole, CsvParam csvParam) throws Exception {

        try(Reader reader = csvParam.getReader()) {
            CsvReader<City> csvReader = setterWithQuotes.create(reader);

            City next;
            do {
                next = csvReader.next();
                if(next == null) {
                    break;
                }
                blackhole.consume(next);
            } while(true);
        }
    }

}
