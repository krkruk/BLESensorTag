package pl.projektorion.krzysztof.blesensortag.factories;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Barometer.DBInsertBarometerParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Humidity.DBInsertHumidityParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.IRTemperature.DBInsertIRTemperatureParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Movement.DBInsertMovementParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.OpticalSensor.DBInsertOpticalSensorParamFactory;

/**
 * {@link DBFactoryParamInserts} allows holding a map of DBInsert(sensor)ParamFactory'ies.
 * The data structure is as follows
 * {@link java.util.UUID} Service UUID of each profile
 * {@link pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertParamFactoryInterface} factory interface
 */
public class DBFactoryParamInserts extends DBInsertFactory {

    private DBRowWriter dbWriter;

    public DBFactoryParamInserts(DBRowWriter dbWriter) {
        super();
        this.dbWriter = dbWriter;

        init_param_inserts();
    }

    private void init_param_inserts()
    {
        put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new DBInsertBarometerParamFactory(dbWriter));
        put(HumidityProfile.HUMIDITY_SERVICE,
                new DBInsertHumidityParamFactory(dbWriter));
        put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new DBInsertIRTemperatureParamFactory(dbWriter));
        put(MovementProfile.MOVEMENT_SERVICE,
                new DBInsertMovementParamFactory(dbWriter));
        put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new DBInsertOpticalSensorParamFactory(dbWriter));
    }
}
