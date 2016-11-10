package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeCharacteristicReadCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeCharacteristicWriteCommand;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlReadProfile implements ConnectionControlInterface {
    public static final int ATTRIBUTE_CONNECTION_PARAMS = 0x00;

    public static final UUID CONNECTION_CONTROL_SERVICE =
            UUID.fromString("f000ccc0-0451-4000-b000-000000000000");
    public static final UUID CONNECTION_CONTROL_PARAMS =
            UUID.fromString("f000ccc1-0451-4000-b000-000000000000");
    public static final UUID CONNECTION_CONTROL_REQUEST_PARAMS =
            UUID.fromString("f000ccc2-0451-4000-b000-000000000000");
    public static final UUID CONNECTION_CONTROL_REQUEST_DISCONNECT =
            UUID.fromString("f000ccc3-0451-4000-b000-000000000000");

    private static final String APP_NAME = ProfileName.CONNECTION_CONTROL_PROFILE;

    private BLeGattIO gattIO;
    private BluetoothGattService service;


    public ConnectionControlReadProfile(BLeGattIO gattIO) {
        this.gattIO = gattIO;
        service = null;
    }

    @Override
    public void demandReadCharacteristics(int attributeToBeRead) {
        service = get_service();
        if( service == null ) return;

        switch (attributeToBeRead)
        {
            case ATTRIBUTE_CONNECTION_PARAMS:
                read_connection_params();
                break;
            case ATTRIBUTE_ALL:
                read_connection_params();
                break;
            default:
                break;
        }
    }

    @Override
    public void requestConnectionParams(ConnectionControlRequestInterface requestInterface) {
        service = get_service();
        if( service == null ) return;
        BluetoothGattCharacteristic connReqCharacteristic =
                service.getCharacteristic(CONNECTION_CONTROL_REQUEST_PARAMS);
        if( connReqCharacteristic == null ) return;

        byte[] connParams = requestInterface.getRequest();
        connReqCharacteristic.setValue(connParams);
        gattIO.addWrite(new BLeCharacteristicWriteCommand(gattIO, connReqCharacteristic));
    }

    @Override
    public void requestDisconnect() {
        service = get_service();
        if( service == null ) return;
        BluetoothGattCharacteristic disconnReq =
                service.getCharacteristic(CONNECTION_CONTROL_REQUEST_DISCONNECT);
        if( disconnReq == null ) return;

        final short RANDOM_VALUE = 194 /* ( ͡° ͜ʖ ͡°) */;
        byte[] reqDisconnValue = { (byte) RANDOM_VALUE };
        disconnReq.setValue(reqDisconnValue);
        gattIO.addWrite(new BLeCharacteristicWriteCommand(gattIO, disconnReq));
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    public boolean isService(UUID serviceUuid) {
        return CONNECTION_CONTROL_SERVICE.equals(serviceUuid);
    }

    private BluetoothGattService get_service()
    {
        return gattIO.getService(CONNECTION_CONTROL_SERVICE);
    }

    private void read_connection_params()
    {
        BluetoothGattCharacteristic characteristic =
                service.getCharacteristic(CONNECTION_CONTROL_PARAMS);
        gattIO.addRead(new BLeCharacteristicReadCommand(gattIO, characteristic));
    }
}
