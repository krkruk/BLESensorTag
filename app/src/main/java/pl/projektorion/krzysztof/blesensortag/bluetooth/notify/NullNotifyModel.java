package pl.projektorion.krzysztof.blesensortag.bluetooth.notify;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;

/**
 * Created by krzysztof on 06.11.16.
 */

public class NullNotifyModel extends AbstractGenericGattModel
        implements GenericGattNotifyModelInterface {
    public NullNotifyModel() {}

    public NullNotifyModel(BLeGattIO gattClient) {}

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

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        return null;
    }
}
