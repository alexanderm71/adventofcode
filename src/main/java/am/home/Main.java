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

        long highBoundInclusive = seedToSoil.getMaxNotIntoItselfSrc();
        for (int i = 0; i <= highBoundInclusive; ++i) {
            if (seedToSoil.applyAsLong(i) != seedToSoil.get(i)) {
                throw new RuntimeException("seedToSoil on %d".formatted(i));
            }
        }

        highBoundInclusive = soilToFertilizer.getMaxNotIntoItselfSrc();
        for (int i = 0; i <= highBoundInclusive; ++i) {
            if (soilToFertilizer.applyAsLong(i) != soilToFertilizer.get(i)) {
                throw new RuntimeException("soilToFertilizer on %d".formatted(i));
            }
        }



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

    @Override
    public long applyAsLong(long operand) {
        log.entry(operand);

        if (sortedBySrcBegin.isEmpty()) {
            return log.exit(operand);
        }
        int indexOfRange = Collections.binarySearch(sortedBySrcBegin, MapRange.ofItem(operand), Helper.CMP_BY_SRC_BEGIN);
        log.debug("index for {} is {}", operand, indexOfRange);
        if (indexOfRange >= 0) {
            return log.exit(sortedBySrcBegin.get(indexOfRange).dstBegin());
        }

        indexOfRange = -(indexOfRange + 2);
        if (indexOfRange < 0) {
            return log.exit(operand);
        }
        MapRange range = sortedBySrcBegin.get(indexOfRange);
        log.debug("candidate range: {}", range);

        return log.exit(operand > range.getSrcEnd() ? operand : operand + range.getShift());
    }

    public long get(long val) {
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
    //  todo private
    static final Comparator<MapRange> CMP_BY_SRC_BEGIN = (o1, o2) -> Long.compare(o1.srcBegin(), o2.srcBegin());

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

}