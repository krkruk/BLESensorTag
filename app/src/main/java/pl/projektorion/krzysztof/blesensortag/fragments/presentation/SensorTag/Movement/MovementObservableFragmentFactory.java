package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.Movement;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementObservableFragmentFactory extends AbstractObservableFragmentFactory {
    public MovementObservableFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new MovementFragment();
    }
}
