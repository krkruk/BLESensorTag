package pl.projektorion.krzysztof.blesensortag.factories;

import android.app.Fragment;

import java.util.HashMap;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorDescriptionDefaultFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.database.Stethoscope.DBPresentStethoscopeDescriptionFragmentFactory;

/**
 * Created by krzysztof on 18.01.17.
 */

public class DBPresentSensorDescriptionFactory extends HashMap<String, FragmentFactory>
{
    private String sensorLabel;

    public DBPresentSensorDescriptionFactory() {
    }

    public DBPresentSensorDescriptionFactory(String sensorLabel) {
        super();
        this.sensorLabel = sensorLabel;
        init();
    }



    public Fragment create(String label)
    {
        FragmentFactory factory = get(label);
        return factory == null
                ? DBPresentSensorDescriptionDefaultFragment.newInstance(label)
                : factory.create();
    }

    private void init()
    {
        put(AppContext.getContext().getString(R.string.label_stethoscope_sensor),
                new DBPresentStethoscopeDescriptionFragmentFactory(sensorLabel));
    }

}
