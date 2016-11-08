package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import android.bluetooth.BluetoothGattCharacteristic;

import pl.projektorion.krzysztof.blesensortag.bluetooth.ProfileStringData;

/**
 * Created by krzysztof on 08.11.16.
 */

public class GAPServiceData implements ProfileStringData {
    public static final int ATTRIBUTE_DEVICE_NAME = GAPServiceReadProfile.ATTRIBUTE_DEVICE_NAME;

    private BluetoothGattCharacteristic characteristic;

    private String deviceName;

    public GAPServiceData() {}

    public GAPServiceData(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
        parse();
    }

    @Override
    public void setValue(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
        parse();
    }

    @Override
    public String getValue(int dataAttribute) {
        switch (dataAttribute)
        {
            case ATTRIBUTE_DEVICE_NAME:
                return deviceName;
            default:
                return "";
        }
    }

    private void parse()
    {
        if( characteristic == null ) return;
        deviceName = get_device_name();
    }

    private String get_device_name()
    {
        try {
            return characteristic.getStringValue(0);
        } catch (NullPointerException e) {
            return "";
        }
    }
}
