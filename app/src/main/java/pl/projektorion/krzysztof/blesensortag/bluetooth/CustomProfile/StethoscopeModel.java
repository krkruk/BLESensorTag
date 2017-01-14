package pl.projektorion.krzysztof.blesensortag.bluetooth.CustomProfile;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.GenericGattNotificationModelInterface;

/**
 * Created by krzysztof on 14.01.17.
 */

public class StethoscopeModel extends AbstractGenericGattModel
        implements GenericGattNotificationModelInterface {

    private StethoscopeData stethoscopeData;

    public StethoscopeModel() {
        super();
        stethoscopeData = new StethoscopeData();
    }

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        stethoscopeData = new StethoscopeData(characteristic);
        return stethoscopeData;
    }

    @Override
    public UUID getDataUuid() {
        return StethoscopeProfile.STETHOSCOPE_DATA;
    }

    @Override
    public byte[] getRawData() {
        return stethoscopeData.getRawData();
    }

    @Override
    public Object getData() {
        return stethoscopeData;
    }
}
