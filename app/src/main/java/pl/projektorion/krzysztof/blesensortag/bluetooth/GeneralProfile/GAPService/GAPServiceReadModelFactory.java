package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;

/**
 * Created by krzysztof on 08.11.16.
 */

public class GAPServiceReadModelFactory implements GenericModelFactory {
    public GAPServiceReadModelFactory() {
    }

    @Override
    public GenericGattModelInterface createModel() {
        return new GAPServiceReadModel();
    }
}
