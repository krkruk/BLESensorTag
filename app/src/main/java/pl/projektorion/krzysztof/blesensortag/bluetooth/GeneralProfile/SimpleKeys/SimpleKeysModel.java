package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeysModel extends Observable
        implements GenericGattObserverInterface {
    private SimpleKeysData data;

    public SimpleKeysModel() {
        super();
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
        return SimpleKeysProfile.SIMPLE_KEY_DATA;
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        data = new SimpleKeysData(characteristic);
        
        setChanged();
        notifyObservers(data);
        clearChanged();
    }
}
