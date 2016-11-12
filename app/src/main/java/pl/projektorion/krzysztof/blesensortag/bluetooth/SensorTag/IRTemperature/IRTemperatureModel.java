package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureModel extends AbstractGenericGattModel
        implements GenericGattNotifyModelInterface {
    private IRTemperatureData irData;

    public IRTemperatureModel() {
        super();
        irData = new IRTemperatureData();
    }

    @Override
    public UUID getDataUuid() {
        return IRTemperatureProfile.IR_TEMPERATURE_DATA;
    }

    @Override
    public byte[] getRawData() {
        return irData.getRawData();
    }

    @Override
    public Object getData() {
        return irData;
    }

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        return new IRTemperatureData(characteristic);
    }
}
