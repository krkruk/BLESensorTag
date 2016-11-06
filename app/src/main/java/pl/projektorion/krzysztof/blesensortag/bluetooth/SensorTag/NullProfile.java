package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public class NullProfile implements GenericGattProfileInterface {
    public NullProfile() {
    }

    public NullProfile(BLeGattIO gattClient) {
    }

    @Override
    public void enableNotification(boolean state) {}

    @Override
    public void enableMeasurement(int state) {}

    @Override
    public void configurePeriod(byte input) {}

    @Override
    public String getName() { return ""; }
}
