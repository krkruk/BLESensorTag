package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import android.bluetooth.BluetoothGattCharacteristic;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.GenericGattReadModelInterface;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlReadModel extends AbstractGenericGattModel
        implements GenericGattReadModelInterface {

    private ProfileData data;

    public ConnectionControlReadModel() {
        this.data = new ConnectionControlData();
    }

    @Override
    public boolean hasCharacteristic(BluetoothGattCharacteristic characteristic) {
        return ConnectionControlReadProfile.CONNECTION_CONTROL_PARAMS
                .equals(characteristic.getUuid());
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        return new ConnectionControlData(characteristic);
    }
}
