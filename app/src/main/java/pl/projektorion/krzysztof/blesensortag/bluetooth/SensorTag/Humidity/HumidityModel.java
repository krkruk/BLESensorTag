package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.GenericGattNotificationModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileData;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityModel extends AbstractGenericGattModel
        implements GenericGattNotificationModelInterface {
    private ProfileData humidityData;

    public HumidityModel() {
        super();
        humidityData = new HumidityData();
    }

    @Override
    public UUID getDataUuid() {
        return HumidityProfile.HUMIDITY_DATA;
    }

    @Override
    public byte[] getRawData() {
        return humidityData.getRawData();
    }

    @Override
    public Object getData() {
        return humidityData;
    }

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        humidityData = new HumidityData(characteristic);
        return humidityData;
    }
}
