package results;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

/**
 * Created by aroger on 25/10/2015.
 */
public class ChartFactory {


    private Predicate<BenchmarkResult> filter =
            (r) -> { return r.getNbFields() == 16
                    && (!r.getLib().toLowerCase().contains("sfm") || r.getLib().contains("Jdbc") || r.getLib().contains("Datastax")); };

    private Function<BenchmarkResult, ? extends Comparable> rowKey = (r) -> r.getLib();
    private Function<BenchmarkResult, ? extends Comparable> columnKey = (r) -> r.getLimit();

    private Comparator<BenchmarkResult> resultComparator =
            Comparator.comparing(BenchmarkResult::getNbFields)
                    .thenComparing(BenchmarkResult::getLimit).thenComparing(BenchmarkResult::getLib);


    private String title = "title";
    private String xAxisLabel = "x";
    private String yAxisLabel = "y";

    private Unit targetUnit;

    public JFreeChart createChart(Iterable<BenchmarkResult> results) {
        List<BenchmarkResult> resultList =  new ArrayList<>();

        results.forEach((b) -> resultList.add(convert(b)));

        Unit unit = targetUnit;
        if (targetUnit == null) {
            unit = resultList.get(0).getUnit();
        }

        Collections.sort(resultList, resultComparator);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Stream<BenchmarkResult> filteredStream = resultList
                .stream()
                .filter(filter);

        Consumer<BenchmarkResult> consumer;

        if (targetUnit.isPercent()) {

            Comparator<BenchmarkResult> scoring;
            if (unit.isLatency())  {
                scoring = Comparator.comparing(BenchmarkResult::getScore).reversed();
            } else {
                scoring = Comparator.comparing(BenchmarkResult::getScore);
            }
            consumer =
                    (b) -> {
                        BenchmarkResult br = resultList.stream().filter(filter).filter((bb) -> bb.getLimit() == b.getLimit()).max(scoring).get();
                        dataset.addValue(((b.getScore() * 100)/br.getScore()), rowKey.apply(b), columnKey.apply(b));
                    };
        } else {
            consumer =
                    (b) -> dataset.addValue(b.getScore(), rowKey.apply(b), columnKey.apply(b));
        }
        filteredStream
                .forEach(consumer);

        JFreeChart barChart = org.jfree.chart.ChartFactory.createBarChart(title, xAxisLabel, unit.toString(), dataset);

        BarRenderer r = (BarRenderer)barChart.getCategoryPlot().getRenderer();

        //r.setSeriesPaint();
        r.setBarPainter(new StandardBarPainter());
        return barChart;
    }

    private BenchmarkResult convert(BenchmarkResult b) {
        if (targetUnit != null && b.getMode() != targetUnit.getMode()) {
            b = b.invert();
        }
        if (targetUnit != null && b.getUnit() != targetUnit && targetUnit.getTimeUnit() != null) {
            b = b.scaleTo(targetUnit.getTimeUnit());
        }
        return b;
    }

    public static void main(String[] args) throws IOException {


        List<BenchmarkResult> list = new ArrayList<>();

        Files
                .find(FileSystems.getDefault().getPath("./results/mysql"), 3, (path, att) -> att.isRegularFile())
                .forEach(
                        (p) ->  BenchmarkResult.readFile(p, list::add)
                );

        results.ChartFactory cf = new results.ChartFactory();
        cf.targetUnit = Unit.percent_from_ref_avgt;

        JFreeChart chart = cf.createChart(list);

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
}
