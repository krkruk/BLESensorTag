package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * Created by krzysztof on 09.11.16.
 */

public class DeviceInformationObservableFragmentFactory extends AbstractObservableFragmentFactory {
    public DeviceInformationObservableFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new DeviceInformationFragment();
    }
}
