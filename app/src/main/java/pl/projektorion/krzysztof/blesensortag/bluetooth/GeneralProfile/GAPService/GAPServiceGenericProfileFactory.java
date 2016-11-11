package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericProfileFactory;

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
