package pl.projektorion.krzysztof.blesensortag.bluetooth.notify;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by krzysztof on 03.11.16.
 */

public abstract class AbstractProfileData implements ProfileData {
    protected byte[] data;

    public AbstractProfileData() {
        this.data = new byte[0];
    }

    public AbstractProfileData(byte[] data) {
        this.data = data;
        assert_data_correct_length();
    }

    public AbstractProfileData(BluetoothGattCharacteristic characteristic) {
        this.data = characteristic.getValue();
        assert_data_correct_length();
    }

    @Override
    public byte[] getRawData()
    {
        return data;
    }

    @Override
    public void setValue(byte[] value) {
        this.data = value;
        assert_data_correct_length();
        parse();
    }

    @Override
    public void setValue(BluetoothGattCharacteristic characteristic) {
        this.data = characteristic.getValue();
        assert_data_correct_length();
        parse();
    }

    protected abstract void parse();

    protected void assert_data_correct_length() throws ArrayIndexOutOfBoundsException
    {
        if( data.length <= 0 )
            throw new ArrayIndexOutOfBoundsException("Incorrect data array");
    }
}
