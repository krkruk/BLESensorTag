package pl.projektorion.krzysztof.blesensortag.bluetooth;

/**
 * Created by krzysztof on 02.11.16.
 */

public interface GenericGattProfileInterface{
    int ENABLE_ALL_MEASUREMENTS = 0xffffffff;
    int DISABLE_ALL_MEASUREMENTS = 0x00;

    /**
     * Enables notifications on a BLE device. See child classes for
     * implementation details
     * @param state True to enable notification. False otherwise.
     */
    void enableNotification(boolean state);

    /**
     * Enable sensor measurements.
     * @param state 1 to enable sensor. 0 otherwise. Other values
     *              may be specify if the device needs it.
     */
    void enableMeasurement(int state);

    /**
     * Configure a notification update time period.
     * @param input See child classes.
     */
    void configurePeriod(byte input);

    /**
     * Get a name of the Profile
     * @return String name
     */
    String getName();
}
