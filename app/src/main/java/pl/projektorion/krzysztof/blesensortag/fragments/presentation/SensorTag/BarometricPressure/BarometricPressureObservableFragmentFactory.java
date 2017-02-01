package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.BarometricPressure;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class BarometricPressureObservableFragmentFactory extends AbstractObservableFragmentFactory {
    public BarometricPressureObservableFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new BarometricPressureFragment();
    }
}
