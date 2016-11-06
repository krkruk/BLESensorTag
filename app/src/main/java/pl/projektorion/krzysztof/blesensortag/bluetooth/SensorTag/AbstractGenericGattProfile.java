package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeCharacteristicWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationDisableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationEnableWriteCommand;

/**
 * Created by krzysztof on 06.11.16.
 */

public abstract class AbstractGenericGattProfile implements GenericGattProfileInterface {
    protected static final byte[] CONFIG_ENABLE = {0x01};
    protected static final byte[] CONFIG_DISABLE = {0x00};

    protected BLeGattIO gattClient;
    private BluetoothGattService service;

    private boolean isNotifying;
    private boolean isMeasuring;

    public AbstractGenericGattProfile(BLeGattIO gattClient) {
        this.gattClient = gattClient;
        this.service = get_service();
        this.isMeasuring = false;
        this.isNotifying = false;
    }

    /**
     * Enable notification. The request is sent according to the documentation
     * of SensorTag (data UUID). This method also configures a descriptor 0x2902 specified
     * at https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.descriptor.gatt.client_characteristic_configuration.xml
     * @param state True to enable notification. False otherwise.
     */
    @Override
    public void enableNotification(boolean state) {
        if (isNotifying == state) return;
        isNotifying = state;

        service = get_service();
        if (service == null) return;
        final BluetoothGattCharacteristic notify = service.getCharacteristic(get_data_uuid());

        if (state)
            gattClient.add(new BLeNotificationEnableWriteCommand(gattClient, notify));
        else
            gattClient.add(new BLeNotificationDisableWriteCommand(gattClient, notify));
    }

    /**
     * Enable remote SensorTag sensor.
     * @param state True to enable sensor. False otherwise.
     */
    @Override
    public void enableMeasurement(boolean state) {
        if( isMeasuring == state ) return;
        isMeasuring = state;

        service = get_service();
        if( service == null ) return;

        final byte[] cmd = state ? CONFIG_ENABLE : CONFIG_DISABLE;
        final BluetoothGattCharacteristic measure = service.getCharacteristic(get_config_uuid());
        measure.setValue(cmd);
        gattClient.add(new BLeCharacteristicWriteCommand(gattClient, measure));
    }

    /**
     * Configure update time span of the notification. If a value is invalid or
     * the method cannot get a current service then returns.
     * @param input Byte value. Minimum: 10 (100ms), Maximum 255 (2.55sec)
     */
    @Override
    public void configurePeriod(byte input) {
        service = get_service();
        if( service == null ) return;
        if( input < (byte) 10) return;

        final BluetoothGattCharacteristic period = service.getCharacteristic(get_period_uuid());
        period.setValue(new byte[] { input });
        gattClient.add(new BLeCharacteristicWriteCommand(gattClient, period));
    }

    /**
     * Get {@link BluetoothGattService}
     * @return {@link BluetoothGattService}, may return null.
     * See the reference for more details.
     */
    protected abstract BluetoothGattService get_service();

    /**
     * Get UUID that is responsible for reading/writing values
     * from the device
     * @return {@link UUID} Return UUID that contains data.
     */
    protected abstract UUID get_data_uuid();

    /**
     * Get UUID that is responsible for starting/stopping the cycle
     * of measurement. The UUID should be SensorTag specific.
     * @return Return configuraion {@link UUID}
     */
    protected abstract UUID get_config_uuid();

    /**
     * Get UUID that is responsible for configuration of the
     * update time span. The UUID should be SensorTag specific.
     * The period value is written directly to {@link BluetoothGattCharacteristic}.
     * @return Return {@link UUID} responsible for configuration update time span.
     */
    protected abstract UUID get_period_uuid();
}