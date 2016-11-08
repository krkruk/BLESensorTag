package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.ProfileReadFactory;

/**
 * Created by krzysztof on 08.11.16.
 */

public class GAPServiceReadProfileFactory implements ProfileReadFactory {
    private BLeGattIO gattIO;

    public GAPServiceReadProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattReadProfileInterface createProfile() {
        return new GAPServiceReadProfile(gattIO);
    }
}
