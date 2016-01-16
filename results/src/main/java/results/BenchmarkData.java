package results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by aroger on 25/10/2015.
 */
public class BenchmarkData {

    private final List<BenchmarkResult> data;

    public BenchmarkData(List<BenchmarkResult> data) {
        this.data = data;
    }

    public BenchmarkData toUnit(Unit unit) {
        List<BenchmarkResult> resultList =  new ArrayList<>();

        data.forEach((b) -> resultList.add(convert(b, unit)));

        if (unit.isPercent()) {
            Comparator<BenchmarkResult> scoring;
            if (unit.isLatency())  {
                scoring = Comparator.comparing(BenchmarkResult::getScore).reversed();
            } else {
                scoring = Comparator.comparing(BenchmarkResult::getScore);
            }

            return new BenchmarkData(resultList.stream().map((b) -> {
                BenchmarkResult ref =
                        resultList
                                .stream()
                                .filter((bb) -> bb.getLimit() == b.getLimit())
                                .filter((bb) -> bb.getNbFields() == b.getNbFields())
                                .filter((bb) -> bb.getDbTarget() == b.getDbTarget())
                                .max(scoring).get();

                return b.asPercentOf(ref);
            }).collect(Collectors.toList()));


        }
        return new BenchmarkData(resultList);
    }

    public BenchmarkData filter(Predicate<BenchmarkResult> filter) {
        return new BenchmarkData(data.stream().filter(filter).collect(Collectors.toList()));
    }

    public void forEach(Consumer<BenchmarkResult> consumer) {
        data.forEach(consumer);
    }

    private BenchmarkResult convert(BenchmarkResult b, Unit targetUnit) {
        if (targetUnit != null && b.getMode() != targetUnit.getMode()) {
            b = b.invert();
        }
        if (targetUnit != null && b.getUnit() != targetUnit && targetUnit.getTimeUnit() != null) {
            b = b.scaleTo(targetUnit.getTimeUnit());
        }
        return b;
    }

    public BenchmarkData sort(Comparator<BenchmarkResult> resultComparator) {
        List<BenchmarkResult> data = new ArrayList<>(this.data);
        Collections.sort(data, resultComparator);
        return new BenchmarkData(data);
    }

    public Unit getUnit() {
        return data.stream().findFirst().map((b) -> b.getUnit()).get();
    }
}
