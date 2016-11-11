package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeysObservableFragmentFactory extends AbstractObservableFragmentFactory {
    public SimpleKeysObservableFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new SimpleKeysFragment();
    }
}
