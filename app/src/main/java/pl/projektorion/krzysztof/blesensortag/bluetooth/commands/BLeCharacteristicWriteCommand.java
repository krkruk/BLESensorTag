package pl.projektorion.krzysztof.blesensortag.bluetooth.commands;

import android.bluetooth.BluetoothGattCharacteristic;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BLeCharacteristicWriteCommand extends CommandAbstract {
    private BLeGattIO gattClient;
    private BluetoothGattCharacteristic characteristic;

    public BLeCharacteristicWriteCommand(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    public BLeCharacteristicWriteCommand(BLeGattIO gattClient,
                                         BluetoothGattCharacteristic characteristic) {
        this.gattClient = gattClient;
        this.characteristic = characteristic;
    }

    public void setDataCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    @Override
    public void execute() {
        if( gattClient == null || characteristic == null )
            return;
        gattClient.writeCharacteristic(characteristic);
    }
}
