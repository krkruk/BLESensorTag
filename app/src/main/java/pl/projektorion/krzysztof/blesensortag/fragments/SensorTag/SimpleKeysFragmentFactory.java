package pl.projektorion.krzysztof.blesensortag.fragments.SensorTag;

import android.app.Fragment;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeysFragmentFactory implements FragmentFactory {
    private Observable observable;

    public SimpleKeysFragmentFactory(Observable observable) {
        this.observable = observable;
    }

    @Override
    public Fragment create() {
        Fragment fragment = new SimpleKeysFragment();
        observable.addObserver((Observer) fragment);
        return fragment;
    }
}
