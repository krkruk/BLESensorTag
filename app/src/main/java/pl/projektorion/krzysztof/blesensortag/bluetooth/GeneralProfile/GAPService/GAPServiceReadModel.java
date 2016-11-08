package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattReadModelInterface;

/**
 * Created by krzysztof on 08.11.16.
 */

public class GAPServiceReadModel extends Observable
        implements GenericGattReadModelInterface {

    private GAPServiceData data;
    public GAPServiceReadModel() {
        super();
        data = new GAPServiceData();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        data = new GAPServiceData(characteristic);

        setChanged();
        notifyObservers(data);
        clearChanged();
    }

    @Override
    public boolean hasCharacteristic(BluetoothGattCharacteristic characteristic) {
        return characteristic.getUuid().equals(GAPServiceReadProfile.GAP_DEVICE_NAME);
    }

    @Override
    public Object getData() {
        return data;
    }
}
