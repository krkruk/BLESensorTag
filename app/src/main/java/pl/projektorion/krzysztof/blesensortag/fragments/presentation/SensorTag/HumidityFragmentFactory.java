package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractFragmentFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityFragmentFactory extends AbstractFragmentFactory {
    public HumidityFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new HumidityFragment();
    }
}
