package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadModelInterface;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlReadModel extends Observable
        implements GenericGattReadModelInterface {

    private ProfileData data;

    public ConnectionControlReadModel() {
        this.data = new ConnectionControlData();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        data = new ConnectionControlData(characteristic);
        setChanged();
        notifyObservers(data);
        clearChanged();
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
}
