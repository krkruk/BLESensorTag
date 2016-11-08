package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ProfileData;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityModel extends Observable implements GenericGattObserverInterface {
    private ProfileData humidityData;

    public HumidityModel() {
        super();
        humidityData = new HumidityData();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        humidityData = new HumidityData(characteristic);
        setChanged();
        notifyObservers(humidityData);
        clearChanged();
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
}
