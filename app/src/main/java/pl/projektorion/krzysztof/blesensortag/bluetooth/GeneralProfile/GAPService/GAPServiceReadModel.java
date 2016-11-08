package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadModelInterface;

/**
 * Created by krzysztof on 08.11.16.
 */

public class GAPServiceReadModel extends Observable
        implements GenericGattReadModelInterface {

    private GAPServiceData data;
    private Set<UUID> characteristicUuids;

    public GAPServiceReadModel() {
        super();
        this.data = new GAPServiceData();
        this.characteristicUuids = new HashSet<>();
        this.characteristicUuids.add(GAPServiceReadProfile.GAP_DEVICE_NAME);
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
        return characteristicUuids.contains(characteristic.getUuid());
    }

    @Override
    public Object getData() {
        return data;
    }
}
