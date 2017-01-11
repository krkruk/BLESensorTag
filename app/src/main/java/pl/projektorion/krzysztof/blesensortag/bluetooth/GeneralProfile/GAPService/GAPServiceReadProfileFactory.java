package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.GenericGattReadProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.ProfileReadFactory;

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
