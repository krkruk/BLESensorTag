package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;


import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeCharacteristicReadCommand;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;

/**
 * Created by krzysztof on 08.11.16.
 */

public class GAPServiceReadProfile implements GenericGattReadProfileInterface {
    public static final UUID GAP_SERVICE =
            UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    public static final UUID GAP_DEVICE_NAME =
            UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
    public static final int ATTRIBUTE_DEVICE_NAME = 0b1;

    private static final String APP_NAME = ProfileName.GAP_SERVICE_PROFILE;

    private BLeGattIO gattIO;
    private BluetoothGattService service;


    public GAPServiceReadProfile(BLeGattIO gattIO) {
        this.gattIO = gattIO;
        this.service = null;
    }

    @Override
    public void demandReadCharacteristics(int attributeToBeRead) {
        service = getService();
        if( service == null ) return;

        switch (attributeToBeRead)
        {
            case ATTRIBUTE_DEVICE_NAME:
                write_gap_device_name();
                break;
            case ATTRIBUTE_ALL:
                write_gap_device_name();
                break;
            default: break;
        }
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    public boolean isService(UUID serviceUuid) {
        return GAP_SERVICE.equals(serviceUuid);
    }

    private BluetoothGattService getService()
    {
        return gattIO.getService(GAP_SERVICE);
    }

    private void write_gap_device_name()
    {
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(GAP_DEVICE_NAME);
        gattIO.addRead(new BLeCharacteristicReadCommand(gattIO, characteristic));
    }

}
