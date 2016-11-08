package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ProfileFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementProfileFactory implements ProfileFactory {
    private BLeGattIO gattIO;

    public MovementProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new MovementProfile(gattIO);
    }
}
