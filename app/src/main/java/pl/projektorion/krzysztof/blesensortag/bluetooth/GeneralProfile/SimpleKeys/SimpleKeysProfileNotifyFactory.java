package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileNotifyFactory;

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
