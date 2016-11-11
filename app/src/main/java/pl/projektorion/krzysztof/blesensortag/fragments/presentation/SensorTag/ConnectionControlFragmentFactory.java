package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractFragmentFactory;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlFragmentFactory extends AbstractFragmentFactory {
    public ConnectionControlFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new ConnectionControlFragment();
    }
}
