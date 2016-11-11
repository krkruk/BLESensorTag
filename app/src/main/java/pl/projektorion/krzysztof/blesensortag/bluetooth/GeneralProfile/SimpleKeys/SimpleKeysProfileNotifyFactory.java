package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileNotifyFactory;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeysProfileNotifyFactory implements ProfileNotifyFactory {
    BLeGattIO gattClient;

    public SimpleKeysProfileNotifyFactory(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    @Override
    public NotifyGattProfileInterface createProfile() {
        return new SimpleKeysProfile(gattClient);
    }
}
