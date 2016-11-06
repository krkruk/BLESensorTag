package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import android.bluetooth.BluetoothGattCharacteristic;

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
        barometricPressureData = new BarometricPressureData();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        barometricPressureData = new BarometricPressureData(characteristic);

        setChanged();
        notifyObservers(barometricPressureData);
        clearChanged();
    }

    @Override
    public UUID getDataUuid() {
        return BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA;
    }

    @Override
    public byte[] getRawData() {
        return barometricPressureData.getRawData();
    }

    @Override
    public Object getData() {
        return barometricPressureData;
    }
}
