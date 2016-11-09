package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorModel extends Observable implements GenericGattNotifyModelInterface {
    private ProfileData opticalSensorData;

    public OpticalSensorModel() {
        super();
        opticalSensorData = new OpticalSensorData();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        opticalSensorData = new OpticalSensorData(characteristic);

        setChanged();
        notifyObservers(opticalSensorData);
        clearChanged();
    }

    @Override
    public UUID getDataUuid() {
        return OpticalSensorNotifyProfile.OPTICAL_SENSOR_DATA;
    }

    @Override
    public byte[] getRawData() {
        return opticalSensorData.getRawData();
    }

    @Override
    public Object getData() {
        return opticalSensorData;
    }
}