package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeyFactory implements ProfileFactory {
    BLeGattIO gattClient;

    public SimpleKeyFactory(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    @Override
    public GenericGattProfileInterface create() {
        return new SimpleKeysProfile(gattClient);
    }
}
