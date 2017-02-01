package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.GenericGattNotificationModelInterface;

/**
 * Created by krzysztof on 01.02.17.
 */

public class HeartRateModel extends AbstractGenericGattModel
        implements GenericGattNotificationModelInterface
{
    private HeartRateData heartRateData;

    public HeartRateModel() {
        super();
        this.heartRateData = new HeartRateData();
    }

    @Override
    protected Object data_to_notify(BluetoothGattCharacteristic characteristic) {
        heartRateData = new HeartRateData(characteristic);
        return heartRateData;
    }

    @Override
    public Object getData() {
        return heartRateData;
    }

    @Override
    public UUID getDataUuid() {
        return HeartRateProfile.HEART_RATE_DATA;
    }

    @Override
    public byte[] getRawData() {
        return heartRateData.getRawData();
    }
}
