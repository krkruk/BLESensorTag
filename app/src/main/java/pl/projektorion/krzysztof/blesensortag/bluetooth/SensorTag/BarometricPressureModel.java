package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureModel extends Observable
        implements GenericGattObserverInterface {

    BarometricPressureData barometricPressureData;

    public BarometricPressureModel() {
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        barometricPressureData = new BarometricPressureData(characteristic);
    }

    @Override
    public UUID getDataUuid() {
        return BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA;
    }

    @Override
    public byte[] getRawData() {
        return new byte[0];
    }

    @Override
    public Object getData() {
        return barometricPressureData;
    }
}
