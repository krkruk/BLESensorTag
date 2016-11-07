package pl.projektorion.krzysztof.blesensortag.fragments.SensorTag;

import android.app.Fragment;

import java.util.Observable;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorFragmentFactory extends AbstractFragmentFactory {
    public OpticalSensorFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new OpticalSensorFragment();
    }
}
