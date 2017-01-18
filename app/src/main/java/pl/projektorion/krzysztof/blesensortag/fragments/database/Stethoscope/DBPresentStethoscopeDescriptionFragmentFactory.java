package pl.projektorion.krzysztof.blesensortag.fragments.database.Stethoscope;

import android.app.Fragment;

import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;

/**
 * Created by krzysztof on 18.01.17.
 */

public class DBPresentStethoscopeDescriptionFragmentFactory implements FragmentFactory {
    private String sensorLabel;

    public DBPresentStethoscopeDescriptionFragmentFactory(String sensorLabel) {
        this.sensorLabel = sensorLabel;
    }

    @Override
    public Fragment create() {
        return DBPresentStethoscopeDescriptionFragment.newInstance(sensorLabel);
    }
}
