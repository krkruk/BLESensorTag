package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;

/**
 * Created by krzysztof on 06.11.16.
 */

public class NullModel implements GenericGattObserverInterface {
    public NullModel() {}

    public NullModel(BLeGattIO gattClient) {}

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {}

    @Override
    public UUID getDataUuid() {
        return null;
    }

    @Override
    public byte[] getRawData() {
        return new byte[0];
    }

    @Override
    public Object getData() {
        return null;
    }
}
