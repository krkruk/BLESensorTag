package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

/**
 * Created by krzysztof on 03.11.16.
 */

public interface ProfileData {
    int getValue(int sensorAttribute);
    byte[] getRawData();
}
