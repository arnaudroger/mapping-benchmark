package org.simpleflatmapper;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.AbstractRowProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.param.CsvParam;

import java.io.IOException;
import java.io.Reader;

@BenchmarkMode(Mode.AverageTime)
public class UnivocityCsvParserBenchmark {

    @Benchmark
    public void parseCsv(Blackhole blackhole, CsvParam csvParam) throws IOException {
        CsvParserSettings settings = new CsvParserSettings();

        //turning off features enabled by default
        settings.setIgnoreLeadingWhitespaces(false);
        settings.setIgnoreTrailingWhitespaces(false);
        settings.setSkipEmptyLines(false);
        settings.setColumnReorderingEnabled(false);
        settings.setReadInputOnSeparateThread(false);

        settings.setRowProcessor(new AbstractRowProcessor() {
            @Override
            public void rowProcessed(String[] row, ParsingContext context) {
                blackhole.consume(row);
            }
        });

        com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);
        try(Reader reader = csvParam.getReader()) {
            parser.parse(reader);
        }
    }
}
