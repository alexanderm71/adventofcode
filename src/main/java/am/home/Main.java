package am.home;

import jakarta.annotation.Nonnull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.XSlf4j;

import java.util.*;
import java.util.function.LongUnaryOperator;

@XSlf4j
public class Main {
    public static void main(String[] args) {
        MapRanges seedToSoil = new MapRanges(List.of(new MapRange(50, 98, 2), new MapRange(52, 50, 48)));
        MapRanges soilToFertilizer = new MapRanges(List.of(new MapRange(0, 15, 37), new MapRange(37, 52, 2), new MapRange(39, 0, 15)));

        MapRanges result = Helper.composition(seedToSoil, soilToFertilizer);


//        List<MapRange> sortedBySrcBeginState = soilToFertilizer.getCopyOfSortedBySrcBegin();
//        Helper.processRangeWhenItStartingOf(2, new MapRange(52, 50, 48), sortedBySrcBeginState);

//        List<Integer> list = List.of(5, 10, 17, 23, 28, 34, 39);
//        log.debug("1: {}, {}", Collections.binarySearch(list, 1), Collections.binarySearch(list, 3));
//        log.debug("2: {}, {}", Collections.binarySearch(list, 3), Collections.binarySearch(list, 7));
//        log.debug("3: {}, {}", Collections.binarySearch(list, 12), Collections.binarySearch(list, 14));
//        log.debug("4: {}, {}", Collections.binarySearch(list, 12), Collections.binarySearch(list, 30));
//        log.debug("5: {}, {}", Collections.binarySearch(list, 25), Collections.binarySearch(list, 44));
//        log.debug("6: {}, {}", Collections.binarySearch(list, 41), Collections.binarySearch(list, 44));


//        long highBoundInclusive = seedToSoil.getMaxNotIntoItselfSrc();
//        for (int i = 0; i <= highBoundInclusive; ++i) {
//            if (seedToSoil.applyAsLong(i) != seedToSoil.get(i)) {
//                throw new RuntimeException("seedToSoil on %d".formatted(i));
//            }
//        }
//
//        highBoundInclusive = soilToFertilizer.getMaxNotIntoItselfSrc();
//        for (int i = 0; i <= highBoundInclusive; ++i) {
//            if (soilToFertilizer.applyAsLong(i) != soilToFertilizer.get(i)) {
//                throw new RuntimeException("soilToFertilizer on %d".formatted(i));
//            }
//        }



//        Map<Long, Long> mapSeedToSoil = seedToSoil.asMap();
//        log.debug("mapSeedToSoil: {}", mapSeedToSoil);
//
//        Map<Long, Long> mapSoilToFertilizer = soilToFertilizer.asMap();
//        log.debug("mapSoilToFertilizer: {}", mapSoilToFertilizer);
//
//        Map<Long, Long> mapSeedToFertilizer = new HashMap<>();
//        for (Map.Entry<Long, Long> entrySeedToSoil: mapSeedToSoil.entrySet()) {
//            Long fertilizer = mapSoilToFertilizer.get(entrySeedToSoil.getValue());
//            if (fertilizer == null) {
//                fertilizer = entrySeedToSoil.getValue();
//            }
//            mapSeedToFertilizer.put(entrySeedToSoil.getKey(), fertilizer);
//        }
//        log.debug("mapSeedToFertilizer: {}", mapSeedToFertilizer);
//
//        MapRanges seedToFertilizer = Helper.fromMap(mapSeedToFertilizer);
//        log.debug("seedToFertilizer: {}", seedToFertilizer);



//        long highBoundInclusive = seedToSoil.getMaxNotIntoItselfSrc();
//        Map<Long, Long> map = new HashMap<>();
//        for (long i = 0; i <= highBoundInclusive; ++i) {
//            map.put(i, soilToFertilizer.applyAsLong(seedToSoil.applyAsLong(i)));
//        }
//        log.debug("map: {}", map);
//
//        MapRanges seedToFertilizer = MapRanges.of(map);
//        log.debug("seedToFertilizer: {}", seedToFertilizer);


    }
}

@XSlf4j
@ToString
@EqualsAndHashCode
class MapRanges implements LongUnaryOperator {
    final List<MapRange> sortedBySrcBegin;
    MapRanges(Collection<MapRange> mapRanges) {
        sortedBySrcBegin = Helper.sortBySrcBegin(mapRanges);
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
    private MapRange(long item) {
        this(item, item, 1);
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

    static MapRange ofItem(long item) {
        return new MapRange(item);
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

    MapRanges fromMap(@Nonnull Map<Long, Long> map) {
        log.entry(map);

        if (!isSuitable(map)) {
            throw new IllegalArgumentException("input map is not suitable");
        }

        List<MapRange> mapRanges = new ArrayList<>();
        long currentShift = 0;
        long srcBegin = 0;
        for (long i = 0; i < map.size(); ++i) {
            long shift = map.get(i) - i;
            if (shift != currentShift) {
                if (currentShift != 0) {
                    mapRanges.add(new MapRange(srcBegin + currentShift, srcBegin, i-srcBegin));
                }
                srcBegin = i;
                currentShift = shift;
            }
        }
        if (currentShift != 0) {
            mapRanges.add(new MapRange(srcBegin + currentShift, srcBegin, map.size()-srcBegin));
        }
        log.debug("mapRanges={}", mapRanges);

        return log.exit(new MapRanges(mapRanges));
    }

    private boolean isSuitable(@Nonnull Map<Long, Long> map) {
        log.entry(map);

        if (map.isEmpty()) {
            return log.exit(true);
        }
        Set<Long> keySet = map.keySet();

        return log.exit(Collections.min(keySet) == 0 && Collections.max(keySet) == keySet.size() - 1 && keySet.equals(new HashSet<>(map.values())));
    }

    Optional<MapRange> resolveRange(long val, List<MapRange> sortedBySrcBegin) {
        log.entry(val);

        if (sortedBySrcBegin.isEmpty()) {
            return log.exit(Optional.empty());
        }
        int indexOfRange = Collections.binarySearch(sortedBySrcBegin, MapRange.ofItem(val), CMP_BY_SRC_BEGIN);
        log.debug("index for {} is {}", val, indexOfRange);
        if (indexOfRange >= 0) {
            return log.exit(Optional.of(sortedBySrcBegin.get(indexOfRange)));
        }

        indexOfRange = -(indexOfRange + 2);
        if (indexOfRange < 0) {
            return log.exit(Optional.empty());
        }
        MapRange range = sortedBySrcBegin.get(indexOfRange);
        log.debug("candidate range: {}", range);

        return log.exit(val > range.getSrcEnd() ? Optional.empty() : Optional.of(range));
    }

    MapRanges composition(@Nonnull MapRanges first, @Nonnull MapRanges second) {
        log.entry(first, second);

        List<MapRange> firstList = first.getCopyOfSortedBySrcBegin();
        List<MapRange> stateList = second.getCopyOfSortedBySrcBegin();
        List<MapRange> resultList = new ArrayList<>();

        for (MapRange range: firstList) {
            List<MapRange> rangeResultList = processRange(range, stateList);
            resultList.addAll(rangeResultList);
        }
        resultList.addAll(stateList);

        return log.exit(new MapRanges(resultList));
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