package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericProfileFactory;

/**
 * Created by krzysztof on 11.11.16.
 */

public class GAPServiceGenericProfileFactory implements GenericProfileFactory {
    private BLeGattIO gattIO;

    public GAPServiceGenericProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new GAPServiceReadProfile(gattIO);
    }
}
