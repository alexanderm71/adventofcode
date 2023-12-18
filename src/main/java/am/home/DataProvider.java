package am.home;



import org.apache.commons.lang3.LongRange;
import org.apache.commons.lang3.Range;

import java.util.List;

public interface DataProvider {
    MapRanges getSeedToSoil();
    MapRanges getSoilToFertilizer();
    MapRanges getFertilizerToWater();
    MapRanges getWaterToLight();
    MapRanges getLightToTemperature();
    MapRanges getTemperatureToHumidity();
    MapRanges getHumidityToLocation();

    List<LongRange> getSeeds();
    List<LongRange> getSeeds2();
}
