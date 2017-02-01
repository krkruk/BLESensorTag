package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileNotifyFactory;

/**
 * Created by krzysztof on 01.02.17.
 */

public class HeartRateNotifyFactory implements ProfileNotifyFactory {
    private BLeGattIO gattIO;

    public HeartRateNotifyFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public NotifyGattProfileInterface createProfile() {
        return new HeartRateProfile(gattIO);
    }
}
