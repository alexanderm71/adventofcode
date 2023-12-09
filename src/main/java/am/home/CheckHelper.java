package am.home;

import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.XSlf4j;

import java.util.*;

@UtilityClass
@XSlf4j
class CheckHelper {

    void complexCheck(MapRanges seedToSoil, MapRanges soilToFertilizer, MapRanges fertilizerToWater, MapRanges waterToLight, MapRanges lightToTemperature, MapRanges temperatureToHumidity, MapRanges humidityToLocation) {

        checkMapRanges(seedToSoil, "seedToSoil");
        checkMapRanges(soilToFertilizer, "soilToFertilizer");
        checkMapRanges(fertilizerToWater, "fertilizerToWater");
        checkMapRanges(waterToLight, "waterToLight");
        checkMapRanges(lightToTemperature, "lightToTemperature");
        checkMapRanges(temperatureToHumidity, "temperatureToHumidity");
        checkMapRanges(humidityToLocation, "humidityToLocation");

        //----------------------------------------------------------------------------------------------------------------
        MapRanges seedToFertilizer = Helper.composition(seedToSoil, soilToFertilizer);
        log.debug("seedToFertilizer: {}", seedToFertilizer);
        checkComposition(seedToFertilizer, seedToSoil, soilToFertilizer, "seedToFertilizer");

        MapRanges soilToWater = Helper.composition(soilToFertilizer, fertilizerToWater);
        log.debug("soilToWater: {}", soilToWater);
        checkComposition(soilToWater, soilToFertilizer, fertilizerToWater, "soilToWater");

        MapRanges fertilizerToLight = Helper.composition(fertilizerToWater, waterToLight);
        log.debug("fertilizerToLight: {}", fertilizerToLight);
        checkComposition(fertilizerToLight, fertilizerToWater, waterToLight, "fertilizerToLight");

        MapRanges waterToTemperature = Helper.composition(waterToLight, lightToTemperature);
        log.debug("waterToTemperature: {}", waterToTemperature);
        checkComposition(waterToTemperature, waterToLight, lightToTemperature, "waterToTemperature");

        MapRanges lightToHumidity = Helper.composition(lightToTemperature, temperatureToHumidity);
        log.debug("lightToHumidity: {}", lightToHumidity);
        checkComposition(lightToHumidity, lightToTemperature, temperatureToHumidity, "lightToHumidity");

        MapRanges temperatureToLocation = Helper.composition(temperatureToHumidity, humidityToLocation);
        log.debug("temperatureToLocation: {}", temperatureToLocation);
        checkComposition(temperatureToLocation, temperatureToHumidity, humidityToLocation, "temperatureToLocation");

        //----------------------------------------------------------------------------------------------------------------
        MapRanges seedToWater = Helper.composition(seedToFertilizer, fertilizerToWater);
        log.debug("seedToWater: {}", seedToWater);
        checkComposition(seedToWater, seedToFertilizer, fertilizerToWater, "seedToWater");

        MapRanges soilToLight = Helper.composition(soilToWater, waterToLight);
        log.debug("soilToLight: {}", soilToLight);
        checkComposition(soilToLight, soilToWater, waterToLight, "soilToLight");

        MapRanges fertilizerToTemperature = Helper.composition(fertilizerToLight, lightToTemperature);
        log.debug("fertilizerToTemperature: {}", fertilizerToTemperature);
        checkComposition(fertilizerToTemperature, fertilizerToLight, lightToTemperature, "fertilizerToTemperature");

        MapRanges waterToHumidity = Helper.composition(waterToTemperature, temperatureToHumidity);
        log.debug("waterToHumidity: {}", waterToHumidity);
        checkComposition(waterToHumidity, waterToTemperature, temperatureToHumidity, "waterToHumidity");

        MapRanges lightToLocation = Helper.composition(lightToHumidity, humidityToLocation);
        log.debug("lightToLocation: {}", lightToLocation);
        checkComposition(lightToLocation, lightToHumidity, humidityToLocation, "lightToLocation");

        //----------------------------------------------------------------------------------------------------------------
        MapRanges seedToLight = Helper.composition(seedToWater, waterToLight);
        log.debug("seedToLight: {}", seedToLight);
        checkComposition(seedToLight, seedToWater, waterToLight, "seedToLight");

        MapRanges soilToTemperature = Helper.composition(soilToLight, lightToTemperature);
        log.debug("soilToTemperature: {}", soilToTemperature);
        checkComposition(soilToTemperature, soilToLight, lightToTemperature, "soilToTemperature");

        MapRanges fertilizerToHumidity = Helper.composition(fertilizerToTemperature, temperatureToHumidity);
        log.debug("fertilizerToHumidity: {}", fertilizerToHumidity);
        checkComposition(fertilizerToHumidity, fertilizerToTemperature, temperatureToHumidity, "fertilizerToHumidity");

        MapRanges waterToLocation = Helper.composition(waterToHumidity, humidityToLocation);
        log.debug("waterToLocation: {}", waterToLocation);
        checkComposition(waterToLocation, waterToHumidity, humidityToLocation, "waterToLocation");

        //----------------------------------------------------------------------------------------------------------------
        MapRanges seedToTemperature = Helper.composition(seedToLight, lightToTemperature);
        log.debug("seedToTemperature: {}", seedToTemperature);
        checkComposition(seedToTemperature, seedToLight, lightToTemperature, "seedToTemperature");

        MapRanges soilToHumidity = Helper.composition(soilToTemperature, temperatureToHumidity);
        log.debug("soilToHumidity: {}", soilToHumidity);
        checkComposition(soilToHumidity, soilToTemperature, temperatureToHumidity, "soilToHumidity");

        MapRanges fertilizerToLocation = Helper.composition(fertilizerToHumidity, humidityToLocation);
        log.debug("fertilizerToLocation: {}", fertilizerToLocation);
        checkComposition(fertilizerToLocation, fertilizerToHumidity, humidityToLocation, "fertilizerToLocation");

        //----------------------------------------------------------------------------------------------------------------
        MapRanges seedToHumidity = Helper.composition(seedToTemperature, temperatureToHumidity);
        log.debug("seedToHumidity: {}", seedToHumidity);
        checkComposition(seedToHumidity, seedToTemperature, temperatureToHumidity, "seedToHumidity");

        MapRanges soilToLocation = Helper.composition(soilToHumidity, humidityToLocation);
        log.debug("soilToLocation: {}", soilToLocation);
        checkComposition(soilToLocation, soilToHumidity, humidityToLocation, "soilToLocation");

        //----------------------------------------------------------------------------------------------------------------

        MapRanges seedToLocation = Helper.composition(seedToHumidity, humidityToLocation);
        log.debug("seedToLocation: {}", seedToLocation);
        checkComposition(seedToLocation, seedToHumidity, humidityToLocation, "seedToLocation");
        //----------------------------------------------------------------------------------------------------------------

        //  seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation

        log.debug("getMaxNotIntoItselfSrc():(seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation) = ({}, {}, {}, {}, {}, {}, {})", seedToSoil.getMaxNotIntoItselfSrc(), soilToFertilizer.getMaxNotIntoItselfSrc(), fertilizerToWater.getMaxNotIntoItselfSrc(), waterToLight.getMaxNotIntoItselfSrc(), lightToTemperature.getMaxNotIntoItselfSrc(), temperatureToHumidity.getMaxNotIntoItselfSrc(), humidityToLocation.getMaxNotIntoItselfSrc());

        long highBoundInclusive = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(seedToSoil.getMaxNotIntoItselfSrc(), soilToFertilizer.getMaxNotIntoItselfSrc()), fertilizerToWater.getMaxNotIntoItselfSrc()), waterToLight.getMaxNotIntoItselfSrc()), lightToTemperature.getMaxNotIntoItselfSrc()), temperatureToHumidity.getMaxNotIntoItselfSrc()), humidityToLocation.getMaxNotIntoItselfSrc());
        for (long i = 0; i <= highBoundInclusive; ++i) {
            long resulByComposition = seedToLocation.applyAsLong(i);
            long resultByFirstThenSecond = humidityToLocation.applyAsLong(temperatureToHumidity.applyAsLong(lightToTemperature.applyAsLong(waterToLight.applyAsLong(fertilizerToWater.applyAsLong(soilToFertilizer.applyAsLong(seedToSoil.applyAsLong(i)))))));
            if (resulByComposition != resultByFirstThenSecond) {
                throw new RuntimeException("Final composition: composition result %d != %d".formatted(resulByComposition, resultByFirstThenSecond));
            }
        }
    }

    void checkComposition(@Nonnull MapRanges first, @Nonnull MapRanges second, String tip) {
        log.entry(first, second, tip);
        checkComposition(Helper.composition(first, second), first, second, tip);
        log.exit();
    }

    void checkComposition(@Nonnull MapRanges composition, @Nonnull MapRanges first, @Nonnull MapRanges second, String tip) {
        log.entry(composition, first, second, tip);

        log.debug("(first.getMaxNotIntoItselfSrc(), second.getMaxNotIntoItselfSrc(), composition.getMaxNotIntoItselfSrc()) = ({}, {}, {})", first.getMaxNotIntoItselfSrc(), second.getMaxNotIntoItselfSrc(), composition.getMaxNotIntoItselfSrc());

        long highBoundInclusive = Math.max(Math.max(composition.getMaxNotIntoItselfSrc(), first.getMaxNotIntoItselfSrc()), second.getMaxNotIntoItselfSrc());
        for (long i = 0; i <= highBoundInclusive; ++i) {
            long resulByComposition = composition.applyAsLong(i);
            long resultByFirstThenSecond = second.applyAsLong(first.applyAsLong(i));
            if (resulByComposition != resultByFirstThenSecond) {
                throw new RuntimeException(tip + ": composition result %d != %d".formatted(resulByComposition, resultByFirstThenSecond));
            }
        }

        log.exit();
    }

    void checkMapRanges(@Nonnull MapRanges mapRanges, String tip) {
        log.entry(mapRanges, tip);

        Map<Long, Long> mapAsMap = mapRanges.asMap();
        Map<Long, Long> mapByIterationGet = getMapByIterationGet(mapRanges);

        if (!mapAsMap.equals(mapByIterationGet)) {
            throw new RuntimeException(tip + ": !mapAsMap.equals(mapByIterationGet)");
        }

        MapRanges mapRangesFromMap = fromMap(mapAsMap);
        if (!mapRanges.equals(mapRangesFromMap)) {
            throw new RuntimeException(tip + ": !mapRanges.equals(fromMap(mapAsMap))");
        }

        log.exit();
    }

    Map<Long, Long> getMapByIterationGet(@Nonnull MapRanges mapRanges) {
        log.entry(mapRanges);

        Map<Long, Long> mapByIterationGet = new HashMap<>();
        long highBoundInclusive = mapRanges.getMaxNotIntoItselfSrc();
        for (long i = 0; i <= highBoundInclusive; ++i) {
            mapByIterationGet.put(i, mapRanges.applyAsLong(i));
        }

        return log.exit(mapByIterationGet);
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
                    mapRanges.add(new MapRange(srcBegin + currentShift, srcBegin, i - srcBegin));
                }
                srcBegin = i;
                currentShift = shift;
            }
        }
        if (currentShift != 0) {
            mapRanges.add(new MapRange(srcBegin + currentShift, srcBegin, map.size() - srcBegin));
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

}
