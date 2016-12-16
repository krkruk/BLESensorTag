package pl.projektorion.krzysztof.blesensortag.fragments.config;

import android.app.Fragment;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;

/**
 * Created by krzysztof on 11.11.16.
 */

public class SimpleKeysNotifyConfigFragmentFactory extends AbstractNotifyProfileConfig {
    public SimpleKeysNotifyConfigFragmentFactory(GenericGattProfileInterface profile) {
        super(profile);
    }

    @Override
    protected FragmentNotifyProfileConfigInterface create_fragment() {
        return new FragmentNotifyProfileConfigFragment();
    }

    @Override
    protected Fragment configure(FragmentNotifyProfileConfigInterface frag) {
        frag.setMeasurementSwitchEnabled(false);
        frag.setPeriodSeekBarEnabled(false);
        return super.configure(frag);
    }
}
