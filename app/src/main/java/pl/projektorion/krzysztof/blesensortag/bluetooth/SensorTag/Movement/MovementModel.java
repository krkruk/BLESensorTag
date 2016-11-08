package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementModel extends Observable implements GenericGattNotifyModelInterface {
    private MovementData movementData;
    private int accelerometerRange = 0;

    public MovementModel() {
        super();
        movementData = new MovementData();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        movementData.setAccelerometerRange(accelerometerRange);
        movementData.setValue(characteristic);

        setChanged();
        notifyObservers(movementData);
        clearChanged();
    }

    @Override
    public UUID getDataUuid() {
        return MovementNotifyProfile.MOVEMENT_DATA;
    }

    @Override
    public byte[] getRawData() {
        return movementData.getRawData();
    }

    @Override
    public Object getData() {
        return movementData;
    }

    public void setAccelerometerRange(int accelerometerRange)
    {
        this.accelerometerRange = accelerometerRange;
    }
}
