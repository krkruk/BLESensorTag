package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyModelInterface;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureModel extends Observable implements GenericGattNotifyModelInterface {
    private IRTemperatureData irData;

    public IRTemperatureModel() {
        super();
        irData = new IRTemperatureData();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        irData = new IRTemperatureData(characteristic);

        setChanged();
        notifyObservers(irData);
        clearChanged();
    }

    @Override
    public UUID getDataUuid() {
        return IRTemperatureNotifyProfile.IR_TEMPERATURE_DATA;
    }

    @Override
    public byte[] getRawData() {
        return irData.getRawData();
    }

    @Override
    public Object getData() {
        return irData;
    }
}
