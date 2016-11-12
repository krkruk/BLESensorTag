package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorModel extends AbstractGenericGattModel
        implements GenericGattNotifyModelInterface {
    private ProfileData opticalSensorData;

    public OpticalSensorModel() {
        super();
        opticalSensorData = new OpticalSensorData();
    }

    @Override
    public UUID getDataUuid() {
        return OpticalSensorProfile.OPTICAL_SENSOR_DATA;
    }

    @Override
    public byte[] getRawData() {
        return opticalSensorData.getRawData();
    }

    @Override
    public Object getData() {
        return opticalSensorData;
    }

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        return new OpticalSensorData(characteristic);
    }
}
