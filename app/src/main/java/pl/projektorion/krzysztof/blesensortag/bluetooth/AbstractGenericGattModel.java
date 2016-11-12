package pl.projektorion.krzysztof.blesensortag.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;

/**
 * Created by krzysztof on 12.11.16.
 */

public abstract class AbstractGenericGattModel extends Observable
        implements GenericGattModelInterface {

    public AbstractGenericGattModel() {
        super();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        final Object data = data_to_notify(characteristic);
        if( data == null ) return;
        setChanged();
        notifyObservers(data);
        clearChanged();
    }

    protected abstract Object data_to_notify(BluetoothGattCharacteristic characteristic);
}
