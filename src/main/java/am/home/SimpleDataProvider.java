package am.home;

import lombok.Getter;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.lang3.LongRange;

import java.util.List;

@Getter
@XSlf4j
public class SimpleDataProvider implements DataProvider {
    final MapRanges seedToSoil = new MapRanges(List.of(new MapRange(50, 98, 2), new MapRange(52, 50, 48)));
    final MapRanges soilToFertilizer = new MapRanges(List.of(new MapRange(0, 15, 37), new MapRange(37, 52, 2), new MapRange(39, 0, 15)));
    final MapRanges fertilizerToWater = new MapRanges(List.of(new MapRange(49, 53, 8), new MapRange(0, 11, 42), new MapRange(42, 0, 7), new MapRange(57, 7, 4)));
    final MapRanges waterToLight = new MapRanges(List.of(new MapRange(88, 18, 7), new MapRange(18, 25, 70)));
    final MapRanges lightToTemperature = new MapRanges(List.of(new MapRange(45, 77, 23), new MapRange(81, 45, 19), new MapRange(68, 64, 13)));
    final MapRanges temperatureToHumidity = new MapRanges(List.of(new MapRange(0, 69, 1), new MapRange(1, 0, 69)));
    final MapRanges humidityToLocation = new MapRanges(List.of(new MapRange(60, 56, 37), new MapRange(56, 93, 4)));
    //  79 14 55 13
    final List<LongRange> seeds = List.of(LongRange.of(13,13), LongRange.of(14,14), LongRange.of(55,55), LongRange.of(79,79));
    final List<LongRange> seeds2 = List.of(LongRange.of(55,55+13-1), LongRange.of(79,79+14-1));
}
