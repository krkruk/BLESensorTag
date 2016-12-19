package pl.projektorion.krzysztof.blesensortag.factories;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometerFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidityFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperatureFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovementFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensorFactory;

/**
 * Created by krzysztof on 19.12.16.
 */

public class DBFactoryTables extends DBTableFactory {
    public DBFactoryTables() {
        super();

        init_tables();
    }

    private void init_tables()
    {
        add(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new DBTableBarometerFactory());
        add(HumidityProfile.HUMIDITY_SERVICE, new DBTableHumidityFactory());
        add(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new DBTableIRTemperatureFactory());
        add(MovementProfile.MOVEMENT_SERVICE, new DBTableMovementFactory());
        add(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new DBTableOpticalSensorFactory());
    }
}
