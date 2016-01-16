package results;

import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by aroger on 23/10/2015.
 */
public class Main {
    private static Predicate<BenchmarkResult> db = (r) ->  !r.getLib().toLowerCase().contains("sfm") || r.getLib().contains("Jdbc");
    private static Predicate<BenchmarkResult> jooq = (r) ->  r.getLib().contains("Jooq");

    public static void main(String[] args) throws Exception {
        generateDb("h2");
        generateDb("mysql");
    }

    private static void generateDb(String dir) throws IOException {
        BenchmarkData data = loadData(dir);

        data = data.toUnit(Unit.percent_from_ref_avgt);

        ChartFactory chartFactory = new ChartFactory();

        chartFactory.setTitle(dir);
        JFreeChart chart = chartFactory.createChart(data.filter(db));


        JFrame jFrame = new JFrame();

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(600, 800);

        JPanel jPanel = new JPanel() {

            @Override
            public void paint(Graphics g) {
                chart.draw((Graphics2D) g, getVisibleRect());
            }
        };
        jFrame.add(jPanel);
        jFrame.setVisible(true);

    }

    private static BenchmarkData loadData(String dir) throws IOException {
        List<BenchmarkResult> list = new ArrayList<>();

        Files
                .find(FileSystems.getDefault().getPath("./results/" + dir), 3, (path, att) -> att.isRegularFile())
                .forEach(
                        (p) ->  BenchmarkResult.readFile(p, list::add)
                );

        return new BenchmarkData(list);
    }
}
