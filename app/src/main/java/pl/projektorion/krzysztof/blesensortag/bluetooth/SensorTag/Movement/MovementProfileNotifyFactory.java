package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileNotifyFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementProfileNotifyFactory implements ProfileNotifyFactory {
    private BLeGattIO gattIO;

    public MovementProfileNotifyFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public NotifyGattProfileInterface createProfile() {
        return new MovementProfile(gattIO);
    }
}
