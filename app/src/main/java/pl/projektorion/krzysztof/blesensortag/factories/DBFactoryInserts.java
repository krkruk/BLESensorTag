package pl.projektorion.krzysztof.blesensortag.factories;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Barometer.DBInsertBarometerFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Humidity.DBInsertHumidityFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.IRTemperature.DBInsertIRTemperatureFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Movement.DBInsertMovementFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.OpticalSensor.DBInsertOpticalSensorFactory;

/**
 * Created by krzysztof on 15.01.17.
 */

public class DBFactoryInserts extends DBInsertFactory {
    private DBRowWriter dbWriter;

    public DBFactoryInserts(DBRowWriter dbWriter) {
        super();
        this.dbWriter = dbWriter;
        
        init_insert_query_factories();
    }
    
    private void init_insert_query_factories()
    {
        put(BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA,
                new DBInsertBarometerFactory(dbWriter));
        put(HumidityProfile.HUMIDITY_DATA,
                new DBInsertHumidityFactory(dbWriter));
        put(IRTemperatureProfile.IR_TEMPERATURE_DATA,
                new DBInsertIRTemperatureFactory(dbWriter));
        put(MovementProfile.MOVEMENT_DATA,
                new DBInsertMovementFactory(dbWriter));
        put(OpticalSensorProfile.OPTICAL_SENSOR_DATA,
                new DBInsertOpticalSensorFactory(dbWriter));
    }
}
