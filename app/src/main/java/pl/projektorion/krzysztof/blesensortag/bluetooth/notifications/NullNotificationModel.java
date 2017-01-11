package pl.projektorion.krzysztof.blesensortag.bluetooth.notifications;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.GenericGattNotificationModelInterface;

/**
 * Created by krzysztof on 06.11.16.
 */

public class NullNotificationModel extends AbstractGenericGattModel
        implements GenericGattNotificationModelInterface {
    public NullNotificationModel() {}

    public NullNotificationModel(BLeGattIO gattClient) {}

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
