package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.SimpleKeys;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.AbstractProfileData;

/**
 * Created by krzysztof on 02.11.16.
 */

/**
 * The class parses and returns processed values for Simple Keys Profile
 */
public class SimpleKeysData extends AbstractProfileData {
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


    private byte leftButtonState = 0;
    private byte rightButtonState = 0;
    private byte reedRelayState = 0;


    public SimpleKeysData() {
        super();
    }

    public SimpleKeysData(byte[] data) {
        super(data);
        parse();
    }

    public SimpleKeysData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
        Log.i("SKeyData", String.format("Left %d, right %d", leftButtonState, rightButtonState));
    }

    @Override
    public double getValue(int sensorAttribute)
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

    @Override
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
