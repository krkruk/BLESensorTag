package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.ModelReadFactory;

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
