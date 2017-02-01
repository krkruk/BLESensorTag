package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.HeartRate;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * Created by krzysztof on 01.02.17.
 */

public class HeartRateObservableFragmentFactory extends AbstractObservableFragmentFactory {
    public HeartRateObservableFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new HeartRateFragment();
    }
}
