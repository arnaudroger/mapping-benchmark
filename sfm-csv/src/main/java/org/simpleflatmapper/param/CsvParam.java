package org.simpleflatmapper.param;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.csv.CsvReader;
import org.simpleflatmapper.util.CheckedConsumer;
import org.simpleflatmapper.util.ParallelReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

@State(Scope.Benchmark)
public class CsvParam {

    @Param(value={"false", "true"})
    public boolean parallel;
    @Param(value={"false", "true"})
    public boolean quotes;

    @Param(value={"64"})
    public int parallelBuffersize = 64;

    @Param(value={"1", "10","1000","100000","-1"})
    public int nbRows = 10;

    public ExecutorService executorService;

    public static final String url = new String("http://www.maxmind.com/download/worldcities/worldcitiespop.txt.gz");

    public static final String fileName = getFileDirectory() + File.separator + "worldcitiespop.txt";

    private static String getFileDirectory() {
        return System.getProperty("csv.dir", System.getProperty("java.io.tmpdir"));
    }

    public static final String fileNameQuotes = getFileDirectory() + File.separator + "worldcitiespop2.txt";


    @Setup
    public void setUp() {
        executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
    }

    @TearDown
    public void tearDown() {
        executorService.shutdown();
    }

    public Reader getReader() throws IOException {
        Reader reader = getSingleThreadedReader(quotes, nbRows);

        if (parallel) {
            reader = new ParallelReader(reader, executorService, parallelBuffersize * 1024);
        }

        return reader;
    }

    public static Reader getSingleThreadedReader(boolean quotes, int nbRows) throws IOException {
        Reader reader;
        if (quotes) {
            reader = _getReaderQuotes(nbRows);
        } else {
            reader = _getReader(nbRows);
        }
        return reader;
    }

    private static Reader _getReader(int nbRows) throws IOException {
        File file = getFileName(nbRows, CsvParam.fileName);
        if (!file.exists()) {
            rewriteFile(nbRows, file, CsvParam::getRewriter);
        }
        return newReader(file);
    }

    private static File getFileName(int nbRows, String f) {
        return new File(nbRows == -1 ? f : appendNbRow(f, nbRows));
    }

    private static Reader _getReaderQuotes(int nbRows) throws IOException {
        File file = getFileName(nbRows, fileNameQuotes);
        if (!file.exists()) {
            rewriteFile(nbRows, file, CsvParam::getQuotesRewriter);
        }
        return newReader(file);
    }

    private static String appendNbRow(String fileNameQuotes, int nbRows) {
        int i = fileNameQuotes.lastIndexOf('.');
        return fileNameQuotes.substring(0, i) + "-" + nbRows + fileNameQuotes.substring(i);
    }

    private static void rewriteFile(int nbRows, File file, Function<Writer, CheckedConsumer<String[]>> rewriterFunction) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
             Writer writer = new OutputStreamWriter(bos)) {
            CheckedConsumer<String[]> rewriter = rewriterFunction.apply(writer);
            try (
                    BufferedInputStream bis = new BufferedInputStream(new GZIPInputStream(new URL(url).openStream()));

            ) {

                CsvParser.DSL dsl = CsvParser.dsl();
                CsvReader reader = dsl.reader(new InputStreamReader(bis));

                if (nbRows == -1) {
                    reader.read(rewriter);
                } else {
                    reader.read(rewriter, nbRows);
                }
            }
        }
    }

    private static CheckedConsumer<String[]> getQuotesRewriter(Writer writer) {
        return (row) -> {
                        for (int i = 0; i < row.length; i++) {
                            String cell = row[i];
                            if (i > 0) {
                                writer.write(",");
                            }
                            writer.write("\"");

                            for (int j = 0; j < cell.length(); j++) {
                                char c = cell.charAt(j);
                                if (c == '"') {
                                    writer.append('"');
                                }
                                writer.append(c);
                            }
                            writer.write("\"");
                        }
                        writer.write("\n");
                    };
    }

    private static CheckedConsumer<String[]> getRewriter(Writer writer) {
        return (row) -> {
            for (int i = 0; i < row.length; i++) {
                String cell = row[i];
                if (i > 0) {
                    writer.write(",");
                }
                writer.write(cell);
            }
            writer.write("\n");
        };
    }

    private static Reader newReader(File file) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(file));
    }

}
