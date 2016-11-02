package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by krzysztof on 02.11.16.
 */

/**
 * The class parses and returns processed values for Simple Keys Profile
 */
public class SimpleKeysData {
    /**
     * Access state of the left button.
     */
    public static final int ATTRIBUTE_LEFT_BUTTON = 0;

    /**
     * Access state of the right button.
     */
    public static final int ATTRIBUTE_RIGHT_BUTTON = 1;

    /**
     * Access state of the reed relay.
     */
    public static final int ATTRIBUTE_REED_RELAY = 2;

    /**
     * Bit mask for left button according to the docs.
     */
    private static final byte STATE_LEFT_BTN_ON = 0b1;

    /**
     * Bit mask for right button according to the docs.
     */
    private static final byte STATE_RIGHT_BTN_ON = 0b10;

    /**
     * Bit mask for reed relay according to the docs.
     */
    private static final byte STATE_REED_RELAY_ON = 0b100;

    /**
     * Array access to the byte that contains data.
     */
    private static final int DATA_AT_POINT = 0;


    private byte[] data;

    private byte leftButtonState = 0;
    private byte rightButtonState = 0;
    private byte reedRelayState = 0;


    public SimpleKeysData() {
        this.data = new byte[0];
    }

    public SimpleKeysData(byte[] data) {
        this.data = data;
        assert_data_correct_length();
        parse();
    }

    public SimpleKeysData(BluetoothGattCharacteristic characteristic) {
        this.data = characteristic.getValue();
        assert_data_correct_length();
        parse();
    }

    public int getValue(int sensorAttribute)
    {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_LEFT_BUTTON:
                return leftButtonState;
            case ATTRIBUTE_RIGHT_BUTTON:
                return rightButtonState;
            case ATTRIBUTE_REED_RELAY:
                return reedRelayState;
            default:
                return -1;
        }
    }

    public byte[] getRawData()
    {
        return data;
    }

    protected void assert_data_correct_length() throws ArrayIndexOutOfBoundsException
    {
        if( data.length <= DATA_AT_POINT)
            throw new ArrayIndexOutOfBoundsException("Incorrect data array");
    }

    protected void parse()
    {
        byte processedData = data[DATA_AT_POINT];

        /**
         * Parse data accordingly
         * http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide#Simple_Keys_Service
         */

        processedData &= 0b111;
        leftButtonState = (byte) (processedData & STATE_LEFT_BTN_ON);
        rightButtonState = (byte) ((processedData & STATE_RIGHT_BTN_ON) >> 1);
        reedRelayState = (byte) ((processedData & STATE_REED_RELAY_ON) >> 2);
    }
}
