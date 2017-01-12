package pl.projektorion.krzysztof.blesensortag.factories;

import android.app.Fragment;

import java.util.HashMap;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.database.Barometer.DBPresentBarometerFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentNullFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.database.Humidity.DBPresentHumidityFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.database.IRTemperature.DBPresentIRTemperatureFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.database.Movement.DBPresentMovementFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.database.OpticalSensor.DBPresentOpticalSensorFragmentFactory;

/**
 * Created by krzysztof on 26.12.16.
 */

public class DBPresentSensorFactory extends HashMap<String, FragmentFactory> {
    private DBSelectInterface rootRecord = null;
    private DBSelectInterface sensorRecord = null;

    public DBPresentSensorFactory() {
    }

    public DBPresentSensorFactory(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        this.rootRecord = rootRecord;
        this.sensorRecord = sensorRecord;
        init();
    }

    public void init(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) throws NullPointerException
    {
        this.rootRecord = rootRecord;
        this.sensorRecord = sensorRecord;
        if(rootRecord == null || sensorRecord == null)
            throw new NullPointerException("Cannot init DBPresentSensorFactory - null parameters");

        init();
    }

    public Fragment create(String label)
    {
        FragmentFactory factory = get(label);
        return factory == null
                ? new DBPresentNullFragment()
                : factory.create();
    }

    private void init()
    {
        put(AppContext.getContext().getString(R.string.label_barometer_sensor),
                new DBPresentBarometerFragmentFactory(rootRecord, sensorRecord));
        put(AppContext.getContext().getString(R.string.label_humidity),
                new DBPresentHumidityFragmentFactory(rootRecord, sensorRecord));
        put(AppContext.getContext().getString(R.string.label_temperature_sensor),
                new DBPresentIRTemperatureFragmentFactory(rootRecord, sensorRecord));
        put(AppContext.getContext().getString(R.string.label_acc_sensor),
                new DBPresentMovementFragmentFactory(rootRecord, sensorRecord));
        put(AppContext.getContext().getString(R.string.label_light_intensity_sensor),
                new DBPresentOpticalSensorFragmentFactory(rootRecord, sensorRecord));
    }

}
