package pl.projektorion.krzysztof.blesensortag.fragments.config;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;

/**
 * Created by krzysztof on 11.11.16.
 */

public class NullConfigFragmentFactory extends AbstractNotifyProfileConfig {
    public NullConfigFragmentFactory(GenericGattProfileInterface profile) {
        super(profile);
    }

    @Override
    protected FragmentNotifyProfileConfigInterface create_fragment() {
        return new NullConfigFragmentFragment();
    }
}
