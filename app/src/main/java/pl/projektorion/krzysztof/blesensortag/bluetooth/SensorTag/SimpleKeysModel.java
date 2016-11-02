package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.Locale;
import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;

import static pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.SimpleKeysProfile.SIMPLE_KEY_DATA;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeysModel extends Observable
        implements GenericGattObserverInterface {
    private SimpleKeysData data;

    public SimpleKeysModel() {
        this.data = new SimpleKeysData();
    }

    @Override
    public byte[] getRawData() {
        return data.getRawData();
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public UUID getDataUuid() {
        return SIMPLE_KEY_DATA;
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        data = new SimpleKeysData(characteristic);
        
        setChanged();
        notifyObservers(data);
        clearChanged();
    }
}
