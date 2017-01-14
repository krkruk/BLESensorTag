package pl.projektorion.krzysztof.blesensortag.fragments.presentation.CustomProfile;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * Created by krzysztof on 14.01.17.
 */

public class StethoscopePresentObservableFragment extends AbstractObservableFragmentFactory {
    public StethoscopePresentObservableFragment(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new StethoscopePresentFragment();
    }
}
