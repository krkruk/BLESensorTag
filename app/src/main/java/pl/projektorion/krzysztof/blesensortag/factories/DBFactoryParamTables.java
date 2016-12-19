package pl.projektorion.krzysztof.blesensortag.factories;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometerParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidityParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperatureParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovementParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensorParamFactory;

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
        add(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new DBTableBarometerParamFactory());
        add(HumidityProfile.HUMIDITY_SERVICE,
                new DBTableHumidityParamFactory());
        add(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new DBTableIRTemperatureParamFactory());
        add(MovementProfile.MOVEMENT_SERVICE,
                new DBTableMovementParamFactory());
        add(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new DBTableOpticalSensorParamFactory());
    }
}
