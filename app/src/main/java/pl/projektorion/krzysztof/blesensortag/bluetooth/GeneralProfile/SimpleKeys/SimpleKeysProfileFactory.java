package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ProfileFactory;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeysProfileFactory implements ProfileFactory {
    BLeGattIO gattClient;

    public SimpleKeysProfileFactory(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    @Override
    public GenericGattNotifyProfileInterface createProfile() {
        return new SimpleKeysNotifyProfile(gattClient);
    }
}
