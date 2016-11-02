package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public class NullProfile implements GenericGattProfileInterface, GenericGattObserverInterface {
    public NullProfile() {
    }

    public NullProfile(BLeGattIO gattClient) {
    }

    @Override
    public void enableNotification(boolean state) {}

    @Override
    public void enableMeasurement(boolean state) {}

    @Override
    public void configurePeriod(int input) {}

    @Override
    public byte[] getRawData() { return new byte[0]; }

    @Override
    public Object getData() { return null; }

    @Override
    public String getName() { return ""; }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) { }

    @Override
    public UUID getDataUuid() { return UUID.randomUUID(); }
}
