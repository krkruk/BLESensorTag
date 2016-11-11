package pl.projektorion.krzysztof.blesensortag.fragments;

import android.app.Fragment;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by krzysztof on 06.11.16.
 */

public abstract class AbstractObservableFragmentFactory implements FragmentFactory {
    protected Observable observable;

    public AbstractObservableFragmentFactory(Observable observable) {
        this.observable = observable;
    }

    @Override
    public Fragment create() {
        Fragment fragment = create_new_fragment();
        observable.addObserver((Observer) fragment);
        return fragment;
    }

    protected abstract Fragment create_new_fragment();
}
