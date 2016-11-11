package pl.projektorion.krzysztof.blesensortag.fragments.config;

import android.app.Fragment;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 11.11.16.
 */

public class SimpleKeysNotifyConfigFragmentFactory extends AbstractNotifyProfileConfig {
    public SimpleKeysNotifyConfigFragmentFactory(GenericGattProfileInterface profile) {
        super(profile);
    }

    @Override
    protected NotifyProfileConfigInterface create_fragment() {
        return new NotifyProfileConfigFragment();
    }

    @Override
    protected Fragment configure(NotifyProfileConfigInterface frag) {
        frag.setMeasurementSwitchEnabled(false);
        frag.setPeriodSeekBarEnabled(false);
        return super.configure(frag);
    }
}
