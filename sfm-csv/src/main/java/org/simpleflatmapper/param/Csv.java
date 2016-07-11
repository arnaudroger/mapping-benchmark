package org.simpleflatmapper.param;

import org.sfm.csv.CsvParser;

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
import java.util.zip.GZIPInputStream;

public class Csv {


    public static final String url = new String("http://www.maxmind.com/download/worldcities/worldcitiespop.txt.gz");

    public static final String fileName = System.getProperty("java.io.tmpdir") + File.separator + "worldcitiespop.txt";
    public static final String fileNameQuotes = System.getProperty("java.io.tmpdir") + File.separator + "worldcitiespop2.txt";


    public static Reader getParallelReader(ExecutorService executorService, int bufferSize) throws IOException {
        //return null;
        return new org.sfm.utils.ParallelReader(Csv.getReader(), executorService, bufferSize * 1024);
    }

    public static Reader getParallelReaderQuotes(ExecutorService executorService, int bufferSize) throws IOException {
        //return null;
        return new org.sfm.utils.ParallelReader(Csv.getReaderQuotes(), executorService, bufferSize * 1024);
    }
    public static Reader getReader() throws IOException {
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

    public static Reader getReaderQuotes() throws IOException {
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
