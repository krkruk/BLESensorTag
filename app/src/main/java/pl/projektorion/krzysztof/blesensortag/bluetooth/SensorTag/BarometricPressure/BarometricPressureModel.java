package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.GenericGattNotificationModelInterface;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureModel extends AbstractGenericGattModel
        implements GenericGattNotificationModelInterface {

    BarometricPressureData barometricPressureData;

    public BarometricPressureModel() {
        super();
        barometricPressureData = new BarometricPressureData();
    }

    @Override
    public UUID getDataUuid() {
        return BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA;
    }

    @Override
    public byte[] getRawData() {
        return barometricPressureData.getRawData();
    }

    @Override
    public Object getData() {
        return barometricPressureData;
    }

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        return new BarometricPressureData(characteristic);
    }
}
