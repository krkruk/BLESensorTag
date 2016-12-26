package pl.projektorion.krzysztof.blesensortag.factories;

import android.app.Fragment;

import java.util.HashMap;

import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;
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
        put(ProfileName.BAROMETRIC_PRESSURE_PROFILE,
                new DBPresentBarometerFragmentFactory(rootRecord, sensorRecord));
        put(ProfileName.HUMIDITY_PROFILE,
                new DBPresentHumidityFragmentFactory(rootRecord, sensorRecord));
        put(ProfileName.IR_TEMPERATURE_PROFILE,
                new DBPresentIRTemperatureFragmentFactory(rootRecord, sensorRecord));
        put(ProfileName.MOVEMENT_PROFILE,
                new DBPresentMovementFragmentFactory(rootRecord, sensorRecord));
        put(ProfileName.OPTICAL_SENSOR_PROFILE,
                new DBPresentOpticalSensorFragmentFactory(rootRecord, sensorRecord));
    }

}
