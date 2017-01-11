package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.GenericGattNotifyModelInterface;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementModel extends AbstractGenericGattModel implements GenericGattNotifyModelInterface {
    private MovementData movementData;
    private int accelerometerRange = 0;

    public MovementModel() {
        super();
        movementData = new MovementData();
    }

    @Override
    public UUID getDataUuid() {
        return MovementProfile.MOVEMENT_DATA;
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

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        movementData.setAccelerometerRange(accelerometerRange);
        movementData.setValue(characteristic);
        return movementData;
    }
}
