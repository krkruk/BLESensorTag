package pl.projektorion.krzysztof.blesensortag.fragments.config;

import android.app.Fragment;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;

/**
 * Created by krzysztof on 01.02.17.
 */

public class HeartRateNotifyConfigFragmentFactory extends AbstractNotifyProfileConfig {
    public HeartRateNotifyConfigFragmentFactory(GenericGattProfileInterface profile) {
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
