package am.home;

import jakarta.annotation.Nonnull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.XSlf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.LongRange;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.function.LongUnaryOperator;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@XSlf4j
public class Main {
    public static void main(String[] args) {

//        checkVT();

        //  simple
        DataProvider dataProvider = new SimpleDataProvider();
//        CheckHelper.check(dataProvider, 10);

//        MapRanges seedToLocation = Helper.composition(
//                dataProvider.getSeedToSoil(),
//                dataProvider.getSoilToFertilizer(),
//                dataProvider.getFertilizerToWater(),
//                dataProvider.getWaterToLight(),
//                dataProvider.getLightToTemperature(),
//                dataProvider.getTemperatureToHumidity(),
//                dataProvider.getHumidityToLocation());
//
//        Map<Long, Long> result = new HashMap<>();
//        dataProvider.getSeeds().stream().flatMap(longRange -> LongStream.rangeClosed(longRange.getMinimum(), longRange.getMaximum()).boxed()).forEach(aLong -> result.put(aLong, seedToLocation.applyAsLong(aLong)));
//        log.info("result: {}", result);
//
//        log.info("min: {}", dataProvider.getSeeds().stream().flatMap(longRange -> LongStream.rangeClosed(longRange.getMinimum(), longRange.getMaximum()).boxed()).map(seedToLocation::applyAsLong).min(Long::compareTo));
//
//        CheckHelper.checkSeedsRanges(dataProvider.getSeeds2(), dataProvider.getSeedToSoil(), 10);
//        CheckHelper.checkSeedsRanges(dataProvider.getSeeds2(), seedToLocation, 10);



//        CheckHelper.complexCheck(dataProvider.getSeedToSoil(), dataProvider.getSoilToFertilizer(), dataProvider.getFertilizerToWater(), dataProvider.getWaterToLight(), dataProvider.getLightToTemperature(), dataProvider.getTemperatureToHumidity(), dataProvider.getHumidityToLocation());

        //  input1
        LineIterator it = IOUtils.lineIterator(
                Main.class.getClassLoader().getResourceAsStream("input1.txt"),
                StandardCharsets.UTF_8);
        try {
            dataProvider = new LinesBasedDataProvider(it);
        } finally {
            LineIterator.closeQuietly(it);
        }
//        log.info("seeds: {}", dataProvider.getSeeds());

//        CheckHelper.check(dataProvider, 100_000_000);

        List<List<MapRange>> listForSeeds = List.of(
                dataProvider.getSeeds().stream().map(longRange -> MapRange.ofSelfMap(longRange.getMinimum(), longRange.getMaximum() - longRange.getMinimum() + 1)).toList(),
                dataProvider.getSeedToSoil().getCopyOfSortedBySrcBegin(),
                dataProvider.getSoilToFertilizer().getCopyOfSortedBySrcBegin(),
                dataProvider.getFertilizerToWater().getCopyOfSortedBySrcBegin(),
                dataProvider.getWaterToLight().getCopyOfSortedBySrcBegin(),
                dataProvider.getLightToTemperature().getCopyOfSortedBySrcBegin(),
                dataProvider.getTemperatureToHumidity().getCopyOfSortedBySrcBegin(),
                dataProvider.getHumidityToLocation().getCopyOfSortedBySrcBegin()
        );
        List<MapRange> resultListForSeeds = Helper.processFirstListComposition(listForSeeds);
        MapRange mapRangeMinDst = resultListForSeeds.get(0);
        for (int i = 1; i < resultListForSeeds.size(); ++i) {
            MapRange mapRangeCandidat = resultListForSeeds.get(i);
            if (mapRangeCandidat.dstBegin() < mapRangeMinDst.dstBegin()) {
                mapRangeMinDst = mapRangeCandidat;
            }
        }
        log.info("result part1: {}", mapRangeMinDst);

        listForSeeds = List.of(
                dataProvider.getSeeds2().stream().map(longRange -> MapRange.ofSelfMap(longRange.getMinimum(), longRange.getMaximum() - longRange.getMinimum() + 1)).toList(),
                dataProvider.getSeedToSoil().getCopyOfSortedBySrcBegin(),
                dataProvider.getSoilToFertilizer().getCopyOfSortedBySrcBegin(),
                dataProvider.getFertilizerToWater().getCopyOfSortedBySrcBegin(),
                dataProvider.getWaterToLight().getCopyOfSortedBySrcBegin(),
                dataProvider.getLightToTemperature().getCopyOfSortedBySrcBegin(),
                dataProvider.getTemperatureToHumidity().getCopyOfSortedBySrcBegin(),
                dataProvider.getHumidityToLocation().getCopyOfSortedBySrcBegin()
        );
        resultListForSeeds = Helper.processFirstListComposition(listForSeeds);
        mapRangeMinDst = resultListForSeeds.get(0);
        for (int i = 1; i < resultListForSeeds.size(); ++i) {
            MapRange mapRangeCandidat = resultListForSeeds.get(i);
            if (mapRangeCandidat.dstBegin() < mapRangeMinDst.dstBegin()) {
                mapRangeMinDst = mapRangeCandidat;
            }
        }
        log.info("result part2: {}", mapRangeMinDst);


//        MapRanges seedToLocation1 = Helper.composition(
//                dataProvider.getSeedToSoil(),
//                dataProvider.getSoilToFertilizer(),
//                dataProvider.getFertilizerToWater(),
//                dataProvider.getWaterToLight(),
//                dataProvider.getLightToTemperature(),
//                dataProvider.getTemperatureToHumidity(),
//                dataProvider.getHumidityToLocation());
//
//        CheckHelper.checkSeedsRanges(dataProvider.getSeeds2(), dataProvider.getSeedToSoil(), 100_000_000);
//        CheckHelper.checkSeedsRanges(dataProvider.getSeeds2(), seedToLocation1, 100_000_000);
//
//        log.info("min: {}", dataProvider.getSeeds().stream().flatMap(longRange -> LongStream.rangeClosed(longRange.getMinimum(), longRange.getMaximum()).boxed()).map(seedToLocation1::applyAsLong).min(Long::compareTo));

//        log.info("getMaxNotIntoItselfSrc():\nSeedToSoil: {}\nSoilToFertilizer: {}\nFertilizerToWater: {}\nWaterToLight: {}\nLightToTemperature: {}\nTemperatureToHumidity: {}\nHumidityToLocation: {}",
//                dataProvider.getSeedToSoil().getMaxNotIntoItselfSrc(), dataProvider.getSoilToFertilizer().getMaxNotIntoItselfSrc(), dataProvider.getFertilizerToWater().getMaxNotIntoItselfSrc(),
//                dataProvider.getWaterToLight().getMaxNotIntoItselfSrc(), dataProvider.getLightToTemperature().getMaxNotIntoItselfSrc(), dataProvider.getTemperatureToHumidity().getMaxNotIntoItselfSrc(),
//                dataProvider.getHumidityToLocation().getMaxNotIntoItselfSrc());
//
//        CheckHelper.complexCheck(dataProvider.getSeedToSoil(), dataProvider.getSoilToFertilizer(), dataProvider.getFertilizerToWater(), dataProvider.getWaterToLight(), dataProvider.getLightToTemperature(), dataProvider.getTemperatureToHumidity(), dataProvider.getHumidityToLocation());


    }

    private static void checkVT() {
        ThreadFactory factory = Thread.ofVirtual().name("vt-", 0).factory();
        try(var executorService = Executors.newThreadPerTaskExecutor(factory)) {
            List<Future> futureList = new ArrayList<>();
            log.info("before start virtual threads");
            for (int i = 0; i < 5; i++) {
                int cnt = i;
                futureList.add(executorService.submit(() -> {
                    log.info("start cnt={}", cnt);
                    if (cnt % 2 == 0) {
                        throw new RuntimeException("%s: %d is even".formatted(Thread.currentThread().getName(), cnt));
                    }
                    log.info("finish cnt={}", cnt);
                }));
            }
            log.info("finish arranged {} virtual threads", futureList.size());

            //  first
            futureList.forEach(future -> {
                try {
                    future.get();
                } catch (ExecutionException executionException) {
                    Throwable cause = ExceptionUtils.getRootCause(executionException);
                    log.info("have exception ExecutionException: cause class={}, message={}", cause.getClass(), cause.getMessage());
                } catch (Exception ex) {
                    log.error("have exception", ex);
                }
            });

            log.info("second time try view on futures", futureList.size());
            futureList.forEach(future -> {
                try {
                    future.get();
                } catch (ExecutionException executionException) {
                    Throwable cause = ExceptionUtils.getRootCause(executionException);
                    log.info("have exception ExecutionException: cause class={}, message={}", cause.getClass(), cause.getMessage());
                } catch (Exception ex) {
                    log.error("have exception", ex);
                }
            });

        }
    }

}

@XSlf4j
@Getter
@ToString
@EqualsAndHashCode
class MapRanges implements LongUnaryOperator {
    final List<MapRange> sortedBySrcBegin;
    MapRanges(Collection<MapRange> mapRanges) {
        sortedBySrcBegin = Helper.tryReduce(Helper.sortBySrcBegin(mapRanges));
    }
    public Long getMaxNotIntoItselfSrc() {
        log.entry();
        return log.exit(sortedBySrcBegin.isEmpty() ? null : sortedBySrcBegin.getLast().getSrcEnd());
    }

    public List<MapRange> getCopyOfSortedBySrcBegin() {
        return new ArrayList<>(sortedBySrcBegin);
    }

    @Override
    public long applyAsLong(long val) {
        return Helper.resolveRange(val, sortedBySrcBegin).map(mapRange -> mapRange.getShift() + val).orElse(val);
    }

    public Map<Long, Long> asMap() {
        log.entry();

        HashMap<Long, Long> hashMap = new HashMap<>();
        long cnt = 0;
        for (MapRange range: sortedBySrcBegin) {
            while(cnt < range.srcBegin()) {
                hashMap.put(cnt, cnt);
                ++cnt;
            }

            long highBoundExclusive = range.getSrcEndExclusive();
            long shift = range.getShift();
            while(cnt < highBoundExclusive) {
                hashMap.put(cnt, cnt + shift);
                ++cnt;
            }
        }

        return log.exit(hashMap);
    }

}

record MapRange(long dstBegin, long srcBegin, long length) {
    private MapRange(long item, long length) {
        this(item, item, length);
    }
    boolean isEmpty() {
        return length <= 0;
    }
    long getSrcEndExclusive() {
        return srcBegin + length;
    }
    long getSrcEnd() {
        return getSrcEndExclusive() - 1;
    }
    long getDstEndExclusive() {
        return dstBegin + length;
    }
    long getDstEnd() {
        return getDstEndExclusive() - 1;
    }
    long getShift() {
        return dstBegin - srcBegin;
    }

    boolean containsSrc(long item) {
        return srcBegin <= item && item < getSrcEndExclusive();
    }

    static MapRange ofItem(long item) {
        return ofSelfMap(item, 1);
    }
    static MapRange ofSelfMap(long item, long length) {
        return new MapRange(item, length);
    }
}

@UtilityClass
@XSlf4j
class Helper {
    private static final Comparator<MapRange> CMP_BY_SRC_BEGIN = Comparator.comparingLong(MapRange::srcBegin);

    List<MapRange> sortBySrcBegin(Collection<MapRange> mapRanges) {
        log.entry(mapRanges);

        List<MapRange> sortedBySrcBegin = new ArrayList<>();
        if (mapRanges != null && !mapRanges.isEmpty()) {
            sortedBySrcBegin.addAll(mapRanges);
            Collections.sort(sortedBySrcBegin, CMP_BY_SRC_BEGIN);
        }

        return log.exit(sortedBySrcBegin);
    }
    List<MapRange> tryReduce(@Nonnull List<MapRange> sortedRanges) {
        log.entry(sortedRanges);

        List<MapRange> rv = new ArrayList<>();

        if (sortedRanges.isEmpty()) {
            return log.exit(rv);
        }

        MapRange leftRange = sortedRanges.get(0);
        for (int cnt = 1; cnt < sortedRanges.size(); ++cnt) {
            MapRange rightRange = sortedRanges.get(cnt);
            Optional<MapRange> optionalMapRange = tryReduce(leftRange, rightRange);
            if (optionalMapRange.isPresent()) {
                leftRange = optionalMapRange.get();
            } else {
                rv.add(leftRange);
                leftRange = rightRange;
            }
        }
        rv.add(leftRange);

        log.debug("reduce ratio: {}", (double)rv.size() / sortedRanges.size());

        return log.exit(rv);
    }
    Optional<MapRange> tryReduce(@Nonnull MapRange leftRange, @Nonnull MapRange rightRange) {
        log.entry(leftRange, rightRange);
        return log.exit(
                (leftRange.getSrcEndExclusive() == rightRange.srcBegin() && leftRange.getShift() == rightRange.getShift()) ?
                    Optional.of(new MapRange(leftRange.dstBegin(), leftRange.srcBegin(), leftRange.length() + rightRange.length())) :
                    Optional.empty()
        );
    }


    Optional<MapRange> resolveRange(long val, List<MapRange> sortedBySrcBegin) {
        log.entry(val);

        if (sortedBySrcBegin.isEmpty() || val < sortedBySrcBegin.get(0).srcBegin() || val > sortedBySrcBegin.get(sortedBySrcBegin.size() - 1).getSrcEnd()) {
            return log.exit(Optional.empty());
        }

        int indexOfRange = Collections.binarySearch(sortedBySrcBegin, MapRange.ofItem(val), CMP_BY_SRC_BEGIN);
        log.debug("index for {} is {}", val, indexOfRange);
        if (indexOfRange >= 0) {
            //  попали в точку начала интервала
            return log.exit(Optional.of(sortedBySrcBegin.get(indexOfRange)));
        }

        indexOfRange = -(indexOfRange + 2);
        if (indexOfRange < 0) {
            //  попали перед первым интервалом
            //  это исключено ранее
            throw new IllegalStateException("in resolveRange for val=%d detected indexOfRange=%d for sortedBySrcBegin=%s".formatted(val, -indexOfRange-2, sortedBySrcBegin.toString()));
        }
        //  ближайший интервал, точка начала которого слева от val
        MapRange range = sortedBySrcBegin.get(indexOfRange);
        log.debug("candidate range: {}", range);

        return log.exit(val > range.getSrcEnd() ? Optional.empty() : Optional.of(range));
    }

    //  todo проверить
    //  индексы начального и конечного интервалов из sortedBySrcBegin достаточных для того, чтобы замапить val
    //  null - маппинг k->k для каждого из val
    Pair<Integer, Integer> resolveIndexesSubListForMapping(LongRange val, List<MapRange> sortedBySrcBegin) {
        log.entry(val);

        if (sortedBySrcBegin.isEmpty()) {
            return log.exit(null);
        }

        MapRange firstRange = sortedBySrcBegin.get(0);
        if (val.getMaximum() < firstRange.srcBegin()) {
            //  вх. интервал слева от всех интервалов для маппинга
            return log.exit(null);
        }

        MapRange lastRange = sortedBySrcBegin.get(sortedBySrcBegin.size() - 1);
        if (lastRange.getSrcEnd() < val.getMinimum()) {
            //  вх. интервал справа от всех интервалов для маппинга
            return log.exit(null);
        }

        Integer leftIndex;
        int indexOfRange = Collections.binarySearch(sortedBySrcBegin, MapRange.ofItem(val.getMinimum()), CMP_BY_SRC_BEGIN);
        log.debug("index for {} is {}", val.getMinimum(), indexOfRange);
        if (indexOfRange >= 0) {
            //  попали в точку начала интервала
            leftIndex = indexOfRange;
        } else {
            indexOfRange = -(indexOfRange + 2);
            if (indexOfRange < 0) {
                //  попали перед первым интервалом (точно с ним пересекаемся)
                leftIndex = 0;
            } else {
                //  ближайший интервал, точка начала которого слева от val.getMinimum()
                MapRange range = sortedBySrcBegin.get(indexOfRange);
                leftIndex = indexOfRange;
                if (range.getSrcEnd() < val.getMinimum()) {
                    //  справа точно есть еще интервал
                    ++leftIndex;
                }
            }
        }

        Integer rightIndexFromLeftIndex;
        List<MapRange> listForRightIndex = sortedBySrcBegin.subList(leftIndex, sortedBySrcBegin.size());
        indexOfRange = Collections.binarySearch(listForRightIndex, MapRange.ofItem(val.getMaximum()), CMP_BY_SRC_BEGIN);
        if (indexOfRange >= 0) {
            //  попали в точку начала интервала
            rightIndexFromLeftIndex = indexOfRange;
        } else {
            indexOfRange = -(indexOfRange + 2);
            if (indexOfRange < 0) {
                //  попали перед первым интервалом (точно с ним пересекаемся)
                //  это невозможно
                throw new IllegalStateException("in resolveRange for val.getMaximum()=%d detected indexOfRange=%d for listForRightIndex=%s".formatted(val.getMaximum(), -indexOfRange-2, listForRightIndex.toString()));
            } else {
                //  ближайший интервал, точка начала которого слева от val.getMaximum()
                rightIndexFromLeftIndex = indexOfRange;
            }
        }

        return log.exit(Pair.of(leftIndex, leftIndex + rightIndexFromLeftIndex));
    }

    MapRanges composition(@Nonnull MapRanges... mapRanges) {
        log.entry(mapRanges);

        if (mapRanges.length < 2) {
            throw new IllegalArgumentException("needs 2 maps at least");
        }
        if (mapRanges.length == 2) {
            return log.exit(composition(mapRanges[0], mapRanges[1]));
        }

        return log.exit(composition(mapRanges[0], composition(ArrayUtils.subarray(mapRanges, 1, mapRanges.length))));
    }
    MapRanges composition(@Nonnull MapRanges first, @Nonnull MapRanges second) {
        log.entry(first, second);

        List<MapRange> stateList = second.getCopyOfSortedBySrcBegin();
        List<MapRange> resultList = processFirstListComposition(first.getCopyOfSortedBySrcBegin(), stateList);
        resultList.addAll(stateList);

        return log.exit(new MapRanges(resultList));
    }

    List<MapRange> processFirstListComposition(@Nonnull List<List<MapRange>> list) {
        log.entry(list);

        if (list.isEmpty()) {
            return log.exit(new ArrayList<>());
        }
        if (list.size() == 1) {
            return log.exit(new ArrayList<>(list.get(0)));
        }
        if (list.size() == 2) {
            return log.exit(processFirstListComposition(list.get(0), list.get(1)));
        }

        List<List<MapRange>> newList = new ArrayList<>();
        newList.add(processFirstListComposition(list.get(0), list.get(1)));
        newList.addAll(list.subList(2, list.size()));

        return log.exit(processFirstListComposition(newList));
    }

    List<MapRange> processFirstListComposition(@Nonnull List<MapRange> firstSortedBySrcBeginList, @Nonnull List<MapRange> stateSortedBySrcBeginList) {
        log.entry(firstSortedBySrcBeginList, stateSortedBySrcBeginList);

        List<MapRange> resultList = new ArrayList<>();
        for (MapRange range: firstSortedBySrcBeginList) {
            List<MapRange> rangeResultList = processRange(range, stateSortedBySrcBeginList);
            resultList.addAll(rangeResultList);
        }

        return log.exit(resultList);
    }

    List<MapRange> processRange(@Nonnull MapRange range, @Nonnull List<MapRange> sortedState) {
        log.entry(range, sortedState);

        if (sortedState.isEmpty()) {
            return log.exit(List.of(range));
        }

        int indexOfRange = Collections.binarySearch(sortedState, MapRange.ofItem(range.dstBegin()), CMP_BY_SRC_BEGIN);
        log.debug("index for {} is {}", range, indexOfRange);
        if (indexOfRange >= 0) {
            return processRangeWhenItStartingOf(indexOfRange, range, sortedState);
        }
        int insertionPoint = -indexOfRange - 1;
        if (insertionPoint == 0) {
            return processRangeWhenItBeforeOf(0, range, sortedState);
        }
        //  есть интервал слева
        MapRange leftRange = sortedState.get(insertionPoint - 1);
        if (leftRange.getSrcEnd() < range.dstBegin()) {
            //  с интервалом слева не пересекается
            return processRangeWhenItBeforePossibleOf(insertionPoint, range, sortedState);
        }
        //  есть пересечение с интервалом слева
        return processRangeWhenItStartingInsideOf(insertionPoint - 1, range, sortedState);
    }

    //  точка начала интервала dst range точно нах-ся внутри интервала src sortedState[index]
    List<MapRange> processRangeWhenItStartingInsideOf(int index, @Nonnull MapRange range, @Nonnull List<MapRange> sortedState) {
        List<MapRange> rv = new ArrayList<>();
        MapRange stateRange = sortedState.get(index);

        long lengthBeforeStartIntersect = range.dstBegin() - stateRange.srcBegin();
        //  пересечение
        MapRange newRange = new MapRange(stateRange.dstBegin() + lengthBeforeStartIntersect, range.srcBegin(), Math.min(range.length(), stateRange.length() - lengthBeforeStartIntersect));
        rv.add(newRange);

        //  точно в состоянии останется интервал до начала пересечения
        sortedState.set(index, new MapRange(stateRange.dstBegin(), stateRange.srcBegin(), lengthBeforeStartIntersect));
        if (range.length() == newRange.length()) {
            //  весь интервал обработан
            //  в состояние возможно надо вставить остаток исходного интервала состояния
            long shiftFromStateRangeBegin = newRange.length() + lengthBeforeStartIntersect;
            MapRange tailStateRange = new MapRange(stateRange.dstBegin() + shiftFromStateRangeBegin, stateRange.srcBegin() + shiftFromStateRangeBegin, stateRange.length() - shiftFromStateRangeBegin);
            if (!tailStateRange.isEmpty()) {
                sortedState.add(index + 1, tailStateRange);
            }
            return rv;
        }

        //  сформировать новый интервал к обработке и вызов processRangeWhenItBeforePossibleOf с index+1
        range = new MapRange(range.dstBegin() + newRange.length(), range.srcBegin() + newRange.length(), range.length() - newRange.length());
        rv.addAll(processRangeWhenItBeforePossibleOf(index + 1, range, sortedState));


        return rv;
    }

    //  точка начала интервала dst range совпадает с точкой начала интервала src sortedState[index]
    List<MapRange> processRangeWhenItStartingOf(int index, @Nonnull MapRange range, @Nonnull List<MapRange> sortedState) {
        List<MapRange> rv = new ArrayList<>();

        MapRange stateRange = sortedState.get(index);

        MapRange newRange = new MapRange(stateRange.dstBegin(), range.srcBegin(), Math.min(range.length(), stateRange.length()));
        rv.add(newRange);

        if (range.length() == newRange.length()) {
            //  весь интервал обработан
            MapRange newRangeToState = new MapRange(stateRange.dstBegin() + newRange.length(), stateRange.srcBegin() + newRange.length(), stateRange.length() - newRange.length());
            if (newRangeToState.isEmpty()) {
                sortedState.remove(index);
            } else {
                sortedState.set(index, newRangeToState);
            }
            return rv;
        }
        //  у интервала остался необработанный хвост => отработан полностью интервал состояния
        sortedState.remove(index);
        //  оставшийся хвост
        range = new MapRange(range.dstBegin() + newRange.length(), range.srcBegin() + newRange.length(), range.length() - newRange.length());
        rv.addAll(processRangeWhenItBeforePossibleOf(index, range, sortedState));

        return rv;
    }

    //  точка начала интервала dst range предшествует, не совпадая с точкой начала ВОЗМОЖНОГО интервала src sortedState[index]
    List<MapRange> processRangeWhenItBeforePossibleOf(int index, @Nonnull MapRange range, @Nonnull List<MapRange> sortedState) {
        List<MapRange> rv = new ArrayList<>();

        if (index == sortedState.size()) {
            //  справа интервалов больше нет
            rv.add(range);
        } else {
            MapRange stateRange = sortedState.get(index);
            if (stateRange.srcBegin() == range.dstBegin()) {
                rv.addAll(processRangeWhenItStartingOf(index, range, sortedState));
            } else {
                rv.addAll(processRangeWhenItBeforeOf(index, range, sortedState));
            }
        }

        return rv;
    }

    //  точка начала интервала dst range предшествует, не совпадая с точкой начала ТОЧНО СУЩЕСТВУЮЩЕГО интервала src sortedState[index]
    List<MapRange> processRangeWhenItBeforeOf(int index, @Nonnull MapRange range, @Nonnull List<MapRange> sortedState) {
        List<MapRange> rv = new ArrayList<>();
        MapRange stateRange = sortedState.get(index);

        if (stateRange.srcBegin() > range.getDstEnd()) {
            //  нет пересечения
            rv.add(range);
            return rv;
        }
        //  пересечение есть
        MapRange rangePrefix = new MapRange(range.dstBegin(), range.srcBegin(), stateRange.srcBegin() - range.dstBegin());
        rv.add(rangePrefix);

        range = new MapRange(stateRange.srcBegin(), range.srcBegin() + rangePrefix.length(), range.length() - rangePrefix.length());
        rv.addAll(processRangeWhenItStartingOf(index, range, sortedState));

        return rv;
    }

}


