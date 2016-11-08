package pl.projektorion.krzysztof.blesensortag.bluetooth.commands;

import android.bluetooth.BluetoothGattDescriptor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;

/**
 * Created by krzysztof on 08.11.16.
 */

public class BLeDescriptorReadCommand extends CommandAbstract {
    private BluetoothGattDescriptor descriptorToRead;
    private BLeGattIO gattIO;

    public BLeDescriptorReadCommand(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    public BLeDescriptorReadCommand(BLeGattIO gattIO, BluetoothGattDescriptor descriptorToRead) {
        this.gattIO = gattIO;
        this.descriptorToRead = descriptorToRead;
    }

    public void setDescriptor(BluetoothGattDescriptor descriptor)
    {
        this.descriptorToRead = descriptor;
    }

    @Override
    public void execute() {
        if( gattIO == null || descriptorToRead == null )
            return;
        gattIO.readDescriptor(descriptorToRead);
    }
}
