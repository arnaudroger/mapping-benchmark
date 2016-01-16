package results;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
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
    private Function<BenchmarkResult, ? extends Comparable> columnKey = (r) -> r.getLimit() +  "/" + r.getNbFields();

    private Comparator<BenchmarkResult> resultComparator =
            Comparator.comparing(BenchmarkResult::getNbFields)
                    .thenComparing(BenchmarkResult::getLimit).thenComparing(BenchmarkResult::getLib);


    private String title = "title";
    private String xAxisLabel = "x";


    public JFreeChart createChart(BenchmarkData results) {

        results = results.sort(resultComparator);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        results.forEach( (b) -> dataset.addValue(b.getScore(), rowKey.apply(b), columnKey.apply(b)));

        JFreeChart barChart = org.jfree.chart.ChartFactory.createBarChart(title, xAxisLabel, results.getUnit().toString(), dataset);

        CategoryPlot categoryPlot = barChart.getCategoryPlot();

        ValueAxis rangeAxis = categoryPlot.getRangeAxis();
        rangeAxis.setUpperBound(3);
        rangeAxis.setLowerBound(1);
        BarRenderer r = (BarRenderer) categoryPlot.getRenderer();


        r.setBarPainter(new StandardBarPainter());
        return barChart;
    }


    public static void main(String[] args) throws IOException {


        List<BenchmarkResult> list = new ArrayList<>();

        Files
                .find(FileSystems.getDefault().getPath("./results/h2"), 3, (path, att) -> att.isRegularFile())
                .forEach(
                        (p) ->  BenchmarkResult.readFile(p, list::add)
                );


        BenchmarkData bd  = new BenchmarkData(list);

        bd = bd.filter(
                (r) -> { return r.getNbFields() == 16
                && (!r.getLib().toLowerCase().contains("sfm") || r.getLib().contains("Jdbc") || r.getLib().contains("Datastax")); })
        .toUnit(Unit.percent_from_ref_avgt);



        results.ChartFactory cf = new results.ChartFactory();

        JFreeChart chart = cf.createChart(bd);

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

    public void setTitle(String title) {
        this.title = title;
    }
}
