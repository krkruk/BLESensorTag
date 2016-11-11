package pl.projektorion.krzysztof.blesensortag.fragments.config;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 11.11.16.
 */

public class BarometricPressureNotifyConfigFragmentFactory extends AbstractNotifyProfileConfig {
    public BarometricPressureNotifyConfigFragmentFactory(GenericGattProfileInterface profile) {
        super(profile);
    }

    @Override
    protected NotifyProfileConfigInterface create_fragment() {
        return new NotifyProfileConfigFragment();
    }
}
