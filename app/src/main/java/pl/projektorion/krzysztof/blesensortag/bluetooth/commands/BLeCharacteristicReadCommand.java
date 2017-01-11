package pl.projektorion.krzysztof.blesensortag.bluetooth.commands;

import android.bluetooth.BluetoothGattCharacteristic;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;

/**
 * Created by krzysztof on 08.11.16.
 */

public class BLeCharacteristicReadCommand extends CommandAbstract {
    private BluetoothGattCharacteristic characteristicToRead;
    private BLeGattIO gattIO;

    public BLeCharacteristicReadCommand(BLeGattIO gattIO) {
        this.gattIO = gattIO;
        this.characteristicToRead = null;
    }

    public BLeCharacteristicReadCommand(BLeGattIO gattIO, BluetoothGattCharacteristic characteristicToRead) {
        this.gattIO = gattIO;
        this.characteristicToRead = characteristicToRead;
    }

    @Override
    public void execute() {
        if( gattIO == null || characteristicToRead == null )
            return;
        gattIO.readCharacteristic(characteristicToRead);
    }
}
