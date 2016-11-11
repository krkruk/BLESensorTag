package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile;

import android.app.Fragment;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.fragments.AbstractFragmentFactory;

/**
 * Created by krzysztof on 08.11.16.
 */

public class GAPServiceFragmentFactory extends AbstractFragmentFactory {
    public GAPServiceFragmentFactory(Observable observable) {
        super(observable);
    }

    @Override
    protected Fragment create_new_fragment() {
        return new GAPServiceFragment();
    }
}
