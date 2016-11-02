package pl.projektorion.krzysztof.blesensortag.bluetooth;

/**
 * Created by krzysztof on 02.11.16.
 */

public interface GenericGattProfileInterface {

    /**
     * Enables notifications on a BLE device. See child classes for
     * implementation details
     * @param state True to enable notification. False otherwise.
     */
    void enableNotification(boolean state);

    /**
     * Enables sensor measurements.
     * @param state True to enable sensor. False otherwise.
     */
    void enableMeasurement(boolean state);

    /**
     * Configure a notification update time period.
     * @param input See child classes.
     */
    void configurePeriod(int input);

    /**
     * Get unprocessed data received from the BLE device
     * @return Byte array data set.
     */
    byte[] getRawData();

    /**
     * Get processed data from the BLE device. The data is
     * embraced into a profile data container.
     * @return Return Object. Must be then casted into
     * a proper object
     */
    Object getData();
}
