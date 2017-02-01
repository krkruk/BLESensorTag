package pl.projektorion.krzysztof.blesensortag.factories;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometerParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.HeartRate.DBTableHeartRateParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidityParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperatureParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovementParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensorParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscopeParamFactory;

/**
 * Created by krzysztof on 19.12.16.
 */

public class DBFactoryParamTables extends DBTableFactory {

    public DBFactoryParamTables() {
        super();

        init_param_tables();
    }

    private void init_param_tables()
    {
        add( new DBTableBarometerParamFactory() );
        add( new DBTableHumidityParamFactory() );
        add( new DBTableIRTemperatureParamFactory() );
        add( new DBTableMovementParamFactory() );
        add( new DBTableOpticalSensorParamFactory() );
        add( new DBTableStethoscopeParamFactory() );
        add( new DBTableHeartRateParamFactory() );
    }
}
