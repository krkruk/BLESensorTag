package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractFragmentFactory;

/**
 * Created by krzysztof on 09.11.16.
 */

public class DeviceInformationFragmentFactory extends AbstractFragmentFactory {
    public DeviceInformationFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new DeviceInformationFragment();
    }
}
