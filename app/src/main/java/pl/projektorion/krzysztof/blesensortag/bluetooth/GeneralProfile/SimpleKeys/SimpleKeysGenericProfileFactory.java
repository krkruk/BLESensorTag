package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericProfileFactory;

/**
 * Created by krzysztof on 11.11.16.
 */

public class SimpleKeysGenericProfileFactory implements GenericProfileFactory {
    private BLeGattIO gattIO;

    public SimpleKeysGenericProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new SimpleKeysProfile(gattIO);
    }
}
