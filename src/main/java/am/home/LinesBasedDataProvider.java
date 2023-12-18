package am.home;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.LongRange;

import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@XSlf4j
public class LinesBasedDataProvider implements DataProvider {
    private static final String TOKEN_SEEDS = "seeds:";

    private static final String TOKEN_SEED_TO_SOIL = "seed-to-soil map:";
    private static final String TOKEN_SOIL_TO_FERTILIZER = "soil-to-fertilizer map:";
    private static final String TOKEN_FERTILIZER_TO_WATER = "fertilizer-to-water map:";
    private static final String TOKEN_WATER_TO_LIGHT = "water-to-light map:";
    private static final String TOKEN_LIGHT_TO_TEMPERATURE = "light-to-temperature map:";
    private static final String TOKEN_TEMPERATURE_TO_HUMIDITY = "temperature-to-humidity map:";
    private static final String TOKEN_HUMIDITY_TO_LOCATION = "humidity-to-location map:";

    private static final Set<String> TOKENS_OF_MAP = Set.of(TOKEN_SEED_TO_SOIL, TOKEN_SOIL_TO_FERTILIZER, TOKEN_FERTILIZER_TO_WATER, TOKEN_WATER_TO_LIGHT, TOKEN_LIGHT_TO_TEMPERATURE, TOKEN_TEMPERATURE_TO_HUMIDITY, TOKEN_HUMIDITY_TO_LOCATION);

    final Map<String, MapRanges> maps = new HashMap<>();
    @Getter
    final List<LongRange> seeds = new ArrayList<>();
    @Getter
    final List<LongRange> seeds2 = new ArrayList<>();

    LinesBasedDataProvider(LineIterator lineIterator) {
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            if (TOKENS_OF_MAP.contains(line)) {
                maps.put(line, new MapRanges(readMap(lineIterator)));
            } else if (line.startsWith(TOKEN_SEEDS)) {
                String[] strings = line.substring(TOKEN_SEEDS.length()).trim().split("\s");
                if (strings.length == 0) {
                    return;
                }
                seeds.addAll(Arrays.stream(strings).map(s -> Long.parseLong(s)).mapToLong(Long::longValue).sorted().mapToObj(aLong -> LongRange.of(aLong, aLong)).toList());

                if (strings.length % 2 != 0) {
                    log.warn("an even number of seeds is expected but have {}", strings.length);
                    return;
                }
                for (int i = 0; i < strings.length; i+=2) {
                    long start = Long.parseLong(strings[i]);
                    long length = Long.parseLong(strings[i+1]);
                    if (length > 0) {
                        seeds2.add(LongRange.of(start, start + length - 1));
                    }
                }
                Collections.sort(seeds2, Comparator.comparingLong(LongRange::getMinimum));

            }
        }
    }
    @Nonnull
    private static List<MapRange> readMap(@Nonnull LineIterator lineIterator) {
        List<MapRange> rv = new ArrayList<>();
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            if ("".equals(line)) {
                break;
            }
            String[] items = line.trim().split("\s");
            rv.add(new MapRange(Long.parseLong(items[0]), Long.parseLong(items[1]), Long.parseLong(items[2])));
        }
        return rv;
    }

    @Override
    public MapRanges getSeedToSoil() {
        return maps.get(TOKEN_SEED_TO_SOIL);
    }

    @Override
    public MapRanges getSoilToFertilizer() {
        return maps.get(TOKEN_SOIL_TO_FERTILIZER);
    }

    @Override
    public MapRanges getFertilizerToWater() {
        return maps.get(TOKEN_FERTILIZER_TO_WATER);
    }

    @Override
    public MapRanges getWaterToLight() {
        return maps.get(TOKEN_WATER_TO_LIGHT);
    }

    @Override
    public MapRanges getLightToTemperature() {
        return maps.get(TOKEN_LIGHT_TO_TEMPERATURE);
    }

    @Override
    public MapRanges getTemperatureToHumidity() {
        return maps.get(TOKEN_TEMPERATURE_TO_HUMIDITY);
    }

    @Override
    public MapRanges getHumidityToLocation() {
        return maps.get(TOKEN_HUMIDITY_TO_LOCATION);
    }
}
