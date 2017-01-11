package pl.projektorion.krzysztof.blesensortag.bluetooth.commands;


import android.bluetooth.BluetoothGattDescriptor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;

/**
 * Created by krzysztof on 01.11.16.
 */

public class BLeDescriptorWriteCommand extends CommandAbstract {
    private BLeGattIO gattClient;
    private BluetoothGattDescriptor dataDescriptor;

    public BLeDescriptorWriteCommand(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    public BLeDescriptorWriteCommand(BLeGattIO gattClient, BluetoothGattDescriptor dataDescriptor) {
        this.gattClient = gattClient;
        this.dataDescriptor = dataDescriptor;
    }

    public void setDataDescriptor(BluetoothGattDescriptor dataDescriptor) {
        this.dataDescriptor = dataDescriptor;
    }

    @Override
    public void execute() {
        if( gattClient == null || dataDescriptor == null )
            return;
        gattClient.writeDescriptor(dataDescriptor);
    }
}
