package org.simpleflatmapper.param;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.sfm.csv.CsvParser;
import org.sfm.utils.ParallelReader;

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
import java.util.zip.GZIPInputStream;

@State(Scope.Benchmark)
public class CsvParam {

    @Param(value={"false", "true"})
    public boolean parallel;
    @Param(value={"false", "true"})
    public boolean quotes;

    @Param(value={"64"})
    public int parallelBuffersize;
    public ExecutorService executorService;

    public static final String url = new String("http://www.maxmind.com/download/worldcities/worldcitiespop.txt.gz");

    public static final String fileName = System.getProperty("java.io.tmpdir") + File.separator + "worldcitiespop.txt";
    public static final String fileNameQuotes = System.getProperty("java.io.tmpdir") + File.separator + "worldcitiespop2.txt";


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
        Reader reader;

        reader = getSingleThreadedReader(quotes);


        if (parallel) {
            reader = new ParallelReader(reader, executorService, parallelBuffersize * 1024);
        }

        return reader;
    }

    public static Reader getSingleThreadedReader(boolean quotes) throws IOException {
        Reader reader;
        if (quotes) {
            reader = _getReaderQuotes();
        } else {
            reader = _getReader();
        }
        return reader;
    }

    private static Reader _getReader() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            byte[] buffer = new byte[4096];
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                 BufferedInputStream bis = new BufferedInputStream(new GZIPInputStream(new URL(url).openStream()))
                ) {
                int l;
                while((l = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, l);
                }
            }
        }
        return newReader(file);
    }

    private static Reader _getReaderQuotes() throws IOException {
        File file = new File(fileNameQuotes);
        if (!file.exists()) {
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                 BufferedInputStream bis = new BufferedInputStream(new GZIPInputStream(new URL(url).openStream()));
                 Writer writer = new OutputStreamWriter(bos)
            ) {
                CsvParser.reader(new InputStreamReader(bis)).read(
                        (row) -> {
                            for(int i = 0; i < row.length; i++) {
                                String cell = row[i];
                                if (i>0) {
                                    writer.write(",");
                                }
                                writer.write("\"");

                                for(int j = 0; j < cell.length(); j++) {
                                    char c = cell.charAt(j);
                                    if (c == '"') {
                                        writer.append('"');
                                    }
                                    writer.append(c);
                                }
                                writer.write("\"");
                            }
                            writer.write("\n");
                        }

                );
            }
        }
        return newReader(file);
    }

    private static Reader newReader(File file) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(file));
    }

}
