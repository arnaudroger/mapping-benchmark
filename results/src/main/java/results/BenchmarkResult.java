package results;

import org.simpleflatmapper.csv.CellValueReader;
import org.simpleflatmapper.csv.CsvColumnDefinition;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.csv.ParsingContext;
import org.simpleflatmapper.db.DbTarget;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by aroger on 23/10/2015.
 */
public class BenchmarkResult {

    public static final CsvParser.MapToDSL<BenchmarkResult> MAPPER =
            CsvParser
                    .mapTo(BenchmarkResult.class)
                    .columnDefinition("unit", CsvColumnDefinition.customReaderDefinition(new CellValueReader<Unit>() {
                        @Override
                        public Unit read(char[] chars, int offset, int length, ParsingContext parsingContext) {
                            return Unit.fromString(new String(chars, offset, length));
                        }
                    }))
                    .columnDefinition("Param: db", CsvColumnDefinition.renameDefinition("dbTarget"))
                    .columnDefinition("Param: limit", CsvColumnDefinition.renameDefinition("limit"))
                    .columnDefinition("Score Error (99.9%)", CsvColumnDefinition.renameDefinition("scoreError"))
            ;

    private final String lib;
    private final int nbFields;
    private final Mode mode;
    private final int threads;
    private final long samples;
    private final double score;
    private final double scoreError;
    private final Unit unit;
    private final DbTarget dbTarget;
    private final int limit;


    public BenchmarkResult(BenchmarkKey benchmark, Mode mode, int threads, long samples, double score, double scoreError, Unit unit, DbTarget dbTarget, int limit) {
        this.lib = benchmark.getLib();
        this.nbFields = benchmark.getNbFields();
        this.mode = mode;
        this.threads = threads;
        this.samples = samples;
        this.score = score;
        this.scoreError = scoreError;
        this.unit = unit;
        this.dbTarget = dbTarget;
        this.limit = limit;
    }

    public BenchmarkResult(String lib, int nbFields, Mode mode, int threads, long samples, double score, double scoreError, Unit unit, DbTarget dbTarget, int limit) {
        this.lib = lib;
        this.nbFields = nbFields;
        this.mode = mode;
        this.threads = threads;
        this.samples = samples;
        this.score = score;
        this.scoreError = scoreError;
        this.unit = unit;
        this.dbTarget = dbTarget;
        this.limit = limit;
    }

    public String getLib() {
        return lib;
    }

    public int getNbFields() {
        return nbFields;
    }

    public Mode getMode() {
        return mode;
    }

    public int getThreads() {
        return threads;
    }

    public long getSamples() {
        return samples;
    }

    public double getScore() {
        return score;
    }

    public double getScoreError() {
        return scoreError;
    }

    public Unit getUnit() {
        return unit;
    }

    public DbTarget getDbTarget() {
        return dbTarget;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "BenchmarkResult{" +
                "lib='" + lib + '\'' +
                ", nbFields=" + nbFields +
                ", mode=" + mode +
                ", threads=" + threads +
                ", samples=" + samples +
                ", score=" + score +
                ", scoreError=" + scoreError +
                ", unit=" + unit +
                ", dbTarget=" + dbTarget +
                ", limit=" + limit +
                '}';
    }

    public static void readFile(Path path, Consumer<? super BenchmarkResult> rowHandler) {
        try (Reader reader = new FileReader(path.toFile())) {
            MAPPER.stream(reader).forEach(rowHandler);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public BenchmarkResult invert() {
        return new BenchmarkResult(lib, nbFields, mode.invert(), threads, samples, 1/score, 1/scoreError, unit.invert(), dbTarget, limit);
    }

    public BenchmarkResult scaleTo(TimeUnit tu) {

        double m;
        double d;
        if (mode != Mode.avgt) {
            m = tu.toNanos(1);
            d = unit.getTimeUnit().toNanos(1);
        } else{
            m = unit.getTimeUnit().toNanos(1);
            d = tu.toNanos(1);
        }
        return new BenchmarkResult(lib, nbFields, mode, threads, samples, (score * m) / d, (scoreError * m) / d, unit.toTimeUnit(tu), dbTarget, limit);
    }

    public BenchmarkResult asPercentOf(BenchmarkResult ref) {
        return new BenchmarkResult(lib, nbFields, mode, threads, samples, score/ref.getScore(), scoreError, unit, dbTarget, limit);
    }

    public static class BenchmarkKey {
        private final String lib;
        private final int nbFields;

        public BenchmarkKey(String lib, int nbFields) {
            this.lib = lib;
            this.nbFields = nbFields;
        }

        public String getLib() {
            return lib;
        }

        public int getNbFields() {
            return nbFields;
        }

        @Override
        public String toString() {
            return "BenchmarkKey{" +
                    "lib='" + lib + '\'' +
                    ", nbFields=" + nbFields +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BenchmarkKey that = (BenchmarkKey) o;

            if (nbFields != that.nbFields) return false;
            return lib.equals(that.lib);

        }

        @Override
        public int hashCode() {
            int result = lib.hashCode();
            result = 31 * result + nbFields;
            return result;
        }

        public static BenchmarkKey valueOf(String str) {
            int lastDot = str.lastIndexOf('.');
            int penultimateDot = str.lastIndexOf('.', lastDot - 1);
            String lib = str.substring(penultimateDot + 1, lastDot  - "Benchmark".length());
            int nbFields = Integer.parseInt(str.substring(lastDot + 2, lastDot + 4));

            return new BenchmarkKey(lib, nbFields);
        }
    }

    public static void main(String[] args) {
        Unit unit = Unit.s_per_op;
        BenchmarkResult br = new BenchmarkResult("lib", 4, unit.getMode(), 1, 1, 3.5, 1, unit,DbTarget.H2, 1);
        System.out.println("ms   = " + br.scaleTo(TimeUnit.MINUTES).getScore());
        System.out.println("mse  = " + br.scaleTo(TimeUnit.MINUTES).getScoreError());

        System.out.println("ss   = " + br.scaleTo(TimeUnit.SECONDS).getScore());
        System.out.println("sse  = " + br.scaleTo(TimeUnit.SECONDS).getScoreError());

        System.out.println("sms  = " + br.scaleTo(TimeUnit.NANOSECONDS).getScore());
        System.out.println("smse = " + br.scaleTo(TimeUnit.NANOSECONDS).getScoreError());
    }
}
