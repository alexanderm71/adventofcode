package am.home;

import java.util.List;

public interface DataProvider {
    MapRanges getSeedToSoil();
    MapRanges getSoilToFertilizer();
    MapRanges getFertilizerToWater();
    MapRanges getWaterToLight();
    MapRanges getLightToTemperature();
    MapRanges getTemperatureToHumidity();
    MapRanges getHumidityToLocation();
}
