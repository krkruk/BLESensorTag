package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.ConnectionControl;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlObservableFragmentFactory extends AbstractObservableFragmentFactory {
    public ConnectionControlObservableFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new ConnectionControlFragment();
    }
}
