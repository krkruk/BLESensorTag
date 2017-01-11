package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.GenericGattNotificationModelInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeysModel extends AbstractGenericGattModel
        implements GenericGattNotificationModelInterface {
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
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        return new SimpleKeysData(characteristic);
    }
}
