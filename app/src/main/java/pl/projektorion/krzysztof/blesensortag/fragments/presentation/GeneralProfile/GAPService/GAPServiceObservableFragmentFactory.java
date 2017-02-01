package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.GAPService;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * Created by krzysztof on 08.11.16.
 */

public class GAPServiceObservableFragmentFactory extends AbstractObservableFragmentFactory {
    public GAPServiceObservableFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new GAPServiceFragment();
    }
}
