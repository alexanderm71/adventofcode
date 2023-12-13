package am.home;

import jakarta.annotation.Nonnull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;

@UtilityClass
@XSlf4j
class CheckHelper {

    @SneakyThrows
    void complexCheck(MapRanges seedToSoil, MapRanges soilToFertilizer, MapRanges fertilizerToWater, MapRanges waterToLight, MapRanges lightToTemperature, MapRanges temperatureToHumidity, MapRanges humidityToLocation) {

//        checkMapRanges(seedToSoil, "seedToSoil", 100_000_000);
//        checkMapRanges(soilToFertilizer, "soilToFertilizer", 100_000_000);
//        checkMapRanges(fertilizerToWater, "fertilizerToWater", 100_000_000);
//        checkMapRanges(waterToLight, "waterToLight", 100_000_000);
//        checkMapRanges(lightToTemperature, "lightToTemperature", 100_000_000);
//        checkMapRanges(temperatureToHumidity, "temperatureToHumidity", 100_000_000);
//        checkMapRanges(humidityToLocation, "humidityToLocation", 100_000_000);

        //----------------------------------------------------------------------------------------------------------------

        ThreadFactory factory = Thread.ofVirtual().name("vt-", 0).factory();
        try(var executorService = Executors.newThreadPerTaskExecutor(factory)) {
            Future<MapRanges> futureSeedToFertilizer = executorService.submit(() -> checkComposition(seedToSoil, soilToFertilizer, "SeedToFertilizer", 50, executorService));
            Future<MapRanges> futureSoilToWater = executorService.submit(() -> checkComposition(soilToFertilizer, fertilizerToWater, "SoilToWater", 60, executorService));
            Future<MapRanges> futureFertilizerToLight = executorService.submit(() -> checkComposition(fertilizerToWater, waterToLight, "FertilizerToLight", 70, executorService));
            Future<MapRanges> futureWaterToTemperature = executorService.submit(() -> checkComposition(waterToLight, lightToTemperature, "WaterToTemperature", 80, executorService));
            Future<MapRanges> futureLightToHumidity = executorService.submit(() -> checkComposition(lightToTemperature, temperatureToHumidity, "LightToHumidity", 90, executorService));
            Future<MapRanges> futureTemperatureToLocation = executorService.submit(() -> checkComposition(temperatureToHumidity, humidityToLocation, "TemperatureToLocation", 100, executorService));

            Future<MapRanges> futureSeedToWater = executorService.submit(() -> checkComposition(futureSeedToFertilizer.get(), fertilizerToWater, "SeedToWater", 105, executorService));
            Future<MapRanges> futureSoilToLight = executorService.submit(() -> checkComposition(futureSoilToWater.get(), waterToLight, "SoilToLight", 110, executorService));
            Future<MapRanges> futureFertilizerToTemperature = executorService.submit(() -> checkComposition(futureFertilizerToLight.get(), lightToTemperature, "FertilizerToTemperature", 115, executorService));
            Future<MapRanges> futureWaterToHumidity = executorService.submit(() -> checkComposition(futureWaterToTemperature.get(), temperatureToHumidity, "WaterToHumidity", 120, executorService));
            Future<MapRanges> futureLightToLocation = executorService.submit(() -> checkComposition(futureLightToHumidity.get(), humidityToLocation, "LightToLocation", 125, executorService));
            Future<MapRanges> futureSeedToLight = executorService.submit(() -> checkComposition(futureSeedToWater.get(), waterToLight, "SeedToLight", 130, executorService));
            Future<MapRanges> futureSoilToTemperature = executorService.submit(() -> checkComposition(futureSoilToLight.get(), lightToTemperature, "SoilToTemperature", 135, executorService));
            Future<MapRanges> futureFertilizerToHumidity = executorService.submit(() -> checkComposition(futureFertilizerToTemperature.get(), temperatureToHumidity, "FertilizerToHumidity", 140, executorService));
            Future<MapRanges> futureWaterToLocation = executorService.submit(() -> checkComposition(futureWaterToHumidity.get(), humidityToLocation, "WaterToLocation", 145, executorService));
            Future<MapRanges> futureSeedToTemperature = executorService.submit(() -> checkComposition(futureSeedToLight.get(), lightToTemperature, "SeedToTemperature", 150, executorService));
            Future<MapRanges> futureSoilToHumidity = executorService.submit(() -> checkComposition(futureSoilToTemperature.get(), temperatureToHumidity, "SoilToHumidity", 151, executorService));
            Future<MapRanges> futureFertilizerToLocation = executorService.submit(() -> checkComposition(futureFertilizerToHumidity.get(), humidityToLocation, "FertilizerToLocation", 152, executorService));
            Future<MapRanges> futureSeedToHumidity = executorService.submit(() -> checkComposition(futureSeedToTemperature.get(), temperatureToHumidity, "SeedToHumidity", 153, executorService));
            Future<MapRanges> futureSoilToLocation = executorService.submit(() -> checkComposition(futureSoilToHumidity.get(), humidityToLocation, "SoilToLocation", 154, executorService));
            Future<MapRanges> futureSeedToLocation = executorService.submit(() -> checkComposition(futureSeedToHumidity.get(), humidityToLocation, "SeedToLocation", 155, executorService));

            Future<List<Future>> futureMain = executorService.submit(() -> checkComposition(
                    futureSeedToLocation.get(), seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation,"MainGoal",150, executorService));

            futureMain.get();

        }
//
//
//
//        //  seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation
//
//        log.debug("getMaxNotIntoItselfSrc():(seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation) = ({}, {}, {}, {}, {}, {}, {})", seedToSoil.getMaxNotIntoItselfSrc(), soilToFertilizer.getMaxNotIntoItselfSrc(), fertilizerToWater.getMaxNotIntoItselfSrc(), waterToLight.getMaxNotIntoItselfSrc(), lightToTemperature.getMaxNotIntoItselfSrc(), temperatureToHumidity.getMaxNotIntoItselfSrc(), humidityToLocation.getMaxNotIntoItselfSrc());
//
//        long highBoundInclusive = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(seedToSoil.getMaxNotIntoItselfSrc(), soilToFertilizer.getMaxNotIntoItselfSrc()), fertilizerToWater.getMaxNotIntoItselfSrc()), waterToLight.getMaxNotIntoItselfSrc()), lightToTemperature.getMaxNotIntoItselfSrc()), temperatureToHumidity.getMaxNotIntoItselfSrc()), humidityToLocation.getMaxNotIntoItselfSrc());
//        for (long i = 0; i <= highBoundInclusive; ++i) {
//            long resulByComposition = seedToLocation.applyAsLong(i);
//            long resultByFirstThenSecond = humidityToLocation.applyAsLong(temperatureToHumidity.applyAsLong(lightToTemperature.applyAsLong(waterToLight.applyAsLong(fertilizerToWater.applyAsLong(soilToFertilizer.applyAsLong(seedToSoil.applyAsLong(i)))))));
//            if (resulByComposition != resultByFirstThenSecond) {
//                throw new RuntimeException("Final composition: composition result %d != %d".formatted(resulByComposition, resultByFirstThenSecond));
//            }
//        }

    }

    MapRanges checkComposition(@Nonnull MapRanges first, @Nonnull MapRanges second, String tip, int cntVt, ExecutorService executorService) {
        log.entry(first, second, tip);
        log.info("{}: start build composition", tip);
        Instant start = Instant.now();
        MapRanges composition = Helper.composition(first, second);
        Instant finish = Instant.now();
        log.info("{}: composition is ready. Time elapsed: {}", tip, Duration.between(start, finish));
        checkComposition(composition, first, second, tip, cntVt, executorService);
        finish = Instant.now();
        log.info("{}: check is done. Time elapsed: {}", tip, Duration.between(start, finish));
        return log.exit(composition);
    }

    List<Future> checkComposition(
            @Nonnull MapRanges seedToLocation,
            @Nonnull MapRanges seedToSoil,
            @Nonnull MapRanges soilToFertilizer,
            @Nonnull MapRanges fertilizerToWater,
            @Nonnull MapRanges waterToLight,
            @Nonnull MapRanges lightToTemperature,
            @Nonnull MapRanges temperatureToHumidity,
            @Nonnull MapRanges humidityToLocation,
            String tip,
            int cntVt,
            ExecutorService executorService) {
        log.entry(seedToLocation, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation, tip, cntVt, executorService);

        LongUnaryOperator longUnaryOperator = humidityToLocation.compose(temperatureToHumidity.compose(lightToTemperature.compose(waterToLight.compose(fertilizerToWater.compose(soilToFertilizer.compose(seedToSoil))))));
        LongSupplier maxSupplier = () -> Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(seedToSoil.getMaxNotIntoItselfSrc(), soilToFertilizer.getMaxNotIntoItselfSrc()), fertilizerToWater.getMaxNotIntoItselfSrc()), waterToLight.getMaxNotIntoItselfSrc()), lightToTemperature.getMaxNotIntoItselfSrc()), temperatureToHumidity.getMaxNotIntoItselfSrc()), humidityToLocation.getMaxNotIntoItselfSrc());

        return log.exit(checkComposition(seedToLocation, longUnaryOperator, maxSupplier, tip, cntVt, executorService));
    }

    List<Future> checkComposition(@Nonnull MapRanges composition, @Nonnull MapRanges first, @Nonnull MapRanges second, String tip, int cntVt, ExecutorService executorService) {
        log.entry(composition, first, second, tip, cntVt);
        return log.exit(checkComposition(composition, second.compose(first), () -> Math.max(first.getMaxNotIntoItselfSrc(), second.getMaxNotIntoItselfSrc()), tip, cntVt, executorService));


//        Instant start = Instant.now();
//
//        log.debug("{}: (first.getMaxNotIntoItselfSrc(), second.getMaxNotIntoItselfSrc(), composition.getMaxNotIntoItselfSrc()) = ({}, {}, {})",
//                tip, first.getMaxNotIntoItselfSrc(), second.getMaxNotIntoItselfSrc(), composition.getMaxNotIntoItselfSrc());
//
//        long highBoundExclusive = Math.max(Math.max(composition.getMaxNotIntoItselfSrc(), first.getMaxNotIntoItselfSrc()), second.getMaxNotIntoItselfSrc()) + 1;
//        long blockSize = highBoundExclusive / cntVt;
//        long remainder = highBoundExclusive % cntVt;
//        log.info("{}: cntVt={}, highBoundExclusive={}, blockSize={}, remainder={}", tip, cntVt, highBoundExclusive, blockSize, remainder);
//        List<Future> futureList = new ArrayList<>();
//        int iVt = 0;
//        long low = 0;
//        while (iVt++ < cntVt) {
//            long highBound = low + blockSize + (remainder > 0 ? 1 : 0);
//            long lowBound = low;
//            log.info("{}: lowBound={}, highBound={}", tip, lowBound, highBound);
//            if (highBound == lowBound) {
//                break;
//            }
//            low = highBound;
//            remainder--;
//            futureList.add(executorService.submit(() -> checkComposition(composition, first, second, tip, lowBound, highBound)));
//        }
//        log.info("{}: futureList size={}", tip, futureList.size());
//
//        //  check results
//        AtomicLong cntErrExecutionException = new AtomicLong(0);
//        AtomicLong cntException = new AtomicLong(0);;
//        futureList.forEach(future -> {
//            try {
//                future.get();
//            } catch (ExecutionException executionException) {
//                cntErrExecutionException.incrementAndGet();
//                Throwable cause = ExceptionUtils.getRootCause(executionException);
//                log.info("have exception ExecutionException: cause class={}, message={}", cause.getClass(), cause.getMessage());
//            } catch (Exception ex) {
//                cntException.incrementAndGet();
//                log.error("have exception", ex);
//            }
//        });
//        Instant finish = Instant.now();
//
//        log.info("finish check (tip={}): {}, {}. Time elapsed: {}", tip, cntErrExecutionException.get(), cntException.get(), Duration.between(start, finish));
//
//        return log.exit(futureList);
    }
    List<Future> checkComposition(@Nonnull MapRanges composition, @Nonnull LongUnaryOperator longUnaryOperator, @Nonnull LongSupplier maxSupplier, String tip, int cntVt, ExecutorService executorService) {
        log.entry(composition, longUnaryOperator, maxSupplier, tip, cntVt);

        Instant start = Instant.now();

        long max = maxSupplier.getAsLong();

        log.debug("{}: (max, composition.getMaxNotIntoItselfSrc()) = ({}, {})", tip, max, composition.getMaxNotIntoItselfSrc());

        long highBoundExclusive = Math.max(composition.getMaxNotIntoItselfSrc(), max) + 1;
        long blockSize = highBoundExclusive / cntVt;
        long remainder = highBoundExclusive % cntVt;
        log.info("{}: cntVt={}, highBoundExclusive={}, blockSize={}, remainder={}", tip, cntVt, highBoundExclusive, blockSize, remainder);
        List<Future> futureList = new ArrayList<>();
        int iVt = 0;
        long low = 0;
        while (iVt++ < cntVt) {
            long highBound = low + blockSize + (remainder > 0 ? 1 : 0);
            long lowBound = low;
            log.info("{}: lowBound={}, highBound={}", tip, lowBound, highBound);
            if (highBound == lowBound) {
                break;
            }
            low = highBound;
            remainder--;
            futureList.add(executorService.submit(() -> checkComposition(composition, longUnaryOperator, tip, lowBound, highBound)));
        }
        log.info("{}: futureList size={}", tip, futureList.size());

        //  check results
        AtomicLong cntErrExecutionException = new AtomicLong(0);
        AtomicLong cntException = new AtomicLong(0);;
        futureList.forEach(future -> {
            try {
                future.get();
            } catch (ExecutionException executionException) {
                cntErrExecutionException.incrementAndGet();
                Throwable cause = ExceptionUtils.getRootCause(executionException);
                log.info("have exception ExecutionException: cause class={}, message={}", cause.getClass(), cause.getMessage());
            } catch (Exception ex) {
                cntException.incrementAndGet();
                log.error("have exception", ex);
            }
        });
        Instant finish = Instant.now();

        log.info("finish check (tip={}): {}, {}. Time elapsed: {}", tip, cntErrExecutionException.get(), cntException.get(), Duration.between(start, finish));

        return log.exit(futureList);
    }

    void checkComposition(@Nonnull MapRanges composition, @Nonnull MapRanges first, @Nonnull MapRanges second, String tip, long lowBoundInclusive, long highBoundExclusive) {
        log.entry(composition, first, second, tip, lowBoundInclusive, highBoundExclusive);
        checkComposition(composition, second.compose(first), tip, lowBoundInclusive, highBoundExclusive);

//        for (long i = lowBoundInclusive; i < highBoundExclusive; ++i) {
//            long resulByComposition = composition.applyAsLong(i);
//            long resultByFirstThenSecond = second.applyAsLong(first.applyAsLong(i));
//            if (resulByComposition != resultByFirstThenSecond) {
//                throw new RuntimeException(tip + ": composition result %d != %d".formatted(resulByComposition, resultByFirstThenSecond));
//            }
//        }

        log.exit();
    }
    void checkComposition(@Nonnull MapRanges composition, @Nonnull LongUnaryOperator longUnaryOperator, String tip, long lowBoundInclusive, long highBoundExclusive) {
        log.entry(composition, longUnaryOperator, tip, lowBoundInclusive, highBoundExclusive);

        for (long i = lowBoundInclusive; i < highBoundExclusive; ++i) {
            long resulByComposition = composition.applyAsLong(i);
            long resultByFirstThenSecond = longUnaryOperator.applyAsLong(i);
            if (resulByComposition != resultByFirstThenSecond) {
                throw new RuntimeException(tip + ": composition result %d != %d".formatted(resulByComposition, resultByFirstThenSecond));
            }
        }

        log.exit();
    }


    void checkMapRanges(@Nonnull MapRanges mapRanges, String tip, long blockSize) {
        log.entry(mapRanges, tip);

        List<MapRange> sortedBySrcBegin = mapRanges.getSortedBySrcBegin();
        long cnt = 0;

        ThreadFactory factory = Thread.ofVirtual().name("vt-", 0).factory();
        try(var executorService = Executors.newThreadPerTaskExecutor(factory)) {
            List<Future> futureList = new ArrayList<>();
            log.info("start arranging executors (tip={})", tip);
            for (MapRange range: sortedBySrcBegin) {
                if (cnt < range.srcBegin()) {
                    futureList.addAll(checkMappingFor(mapRanges, tip, cnt, range.srcBegin(), 0, blockSize, executorService));
                }
                cnt = range.getSrcEndExclusive();
                futureList.addAll(checkMappingFor(mapRanges, tip, range.srcBegin(), cnt, range.getShift(), blockSize, executorService));
            }
            log.info("arranged (tip={}) {} executors", tip, futureList.size());

            //  check results
            AtomicLong cntErrExecutionException = new AtomicLong(0);
            AtomicLong cntException = new AtomicLong(0);;
            futureList.forEach(future -> {
                try {
                    future.get();
                } catch (ExecutionException executionException) {
                    cntErrExecutionException.incrementAndGet();
                    Throwable cause = ExceptionUtils.getRootCause(executionException);
                    log.info("have exception ExecutionException: cause class={}, message={}", cause.getClass(), cause.getMessage());
                } catch (Exception ex) {
                    cntException.incrementAndGet();
                    log.error("have exception", ex);
                }
            });

            log.info("finish check (tip={}): {}, {}", tip, cntErrExecutionException.get(), cntException.get());
        }

        log.exit();
    }

    List<Future> checkMappingFor(@Nonnull MapRanges mapRanges, String tip, long lowBoundInclusive, long highBoundExclusive, long shift, long blockSize, ExecutorService executorService) {
        log.info("tip:{}: start arranging for {}, {}, {}", tip, lowBoundInclusive, highBoundExclusive, shift);
        List<Future> futureList = new ArrayList<>();
        long cntBlocks = (highBoundExclusive - lowBoundInclusive) / blockSize;
        log.info("tip:{}: arranging for {}, {}, {}: cntBlocks={}", tip, lowBoundInclusive, highBoundExclusive, shift, cntBlocks);
        for (long cnt = 0; cnt < cntBlocks; ++cnt) {
            long lowBound = lowBoundInclusive;
            long highBound = lowBoundInclusive + blockSize;
            futureList.add(executorService.submit(() -> checkMappingFor(mapRanges, tip, lowBound, highBound, shift)));
            lowBoundInclusive = highBound;
        }
        if (lowBoundInclusive < highBoundExclusive) {
            long lowBound = lowBoundInclusive;
            futureList.add(executorService.submit(() -> checkMappingFor(mapRanges, tip, lowBound, highBoundExclusive, shift)));
        }

        log.info("tip:{}: arranged {} executors for {}, {}, {}", tip, futureList.size(), lowBoundInclusive, highBoundExclusive, shift);
        return futureList;
    }
    void checkMappingFor(@Nonnull MapRanges mapRanges, String tip, long lowBoundInclusive, long highBoundExclusive, long shift) {
        log.info("tip:{}: start processing for {}, {}, {}", tip, lowBoundInclusive, highBoundExclusive, shift);
        for (long i = lowBoundInclusive; i < highBoundExclusive; ++i) {
            if (i % 50_000_000 == 0) {
                log.info("tip:{}: in processing for {}, {}, {}: process {}", tip, lowBoundInclusive, highBoundExclusive, shift, i);
            }
            long cntMappedTo = mapRanges.applyAsLong(i);
            if (cntMappedTo != i + shift) {
                String errorMessage = tip + ": %d should be mapped to %d but have %d".formatted(i, i + shift, cntMappedTo);
                log.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
        log.info("tip:{}: finish processing for {}, {}, {}", tip, lowBoundInclusive, highBoundExclusive, shift);
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
