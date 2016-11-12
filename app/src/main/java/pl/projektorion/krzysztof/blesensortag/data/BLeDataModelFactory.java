package pl.projektorion.krzysztof.blesensortag.data;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceReadModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceReadModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlReadModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlReadModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;

/**
 * Created by krzysztof on 12.11.16.
 */

public class BLeDataModelFactory extends GenericGattModelFactory {
    public BLeDataModelFactory() {
        super();
        init_notify_models();
        init_read_models();
    }

    private void init_notify_models()
    {
        put(SimpleKeysProfile.SIMPLE_KEY_SERVICE, new SimpleKeysModelNotifyFactory());
        put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureModelNotifyFactory());
        put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureModelNotifyFactory());
        put(MovementProfile.MOVEMENT_SERVICE, new MovementModelNotifyFactory());
        put(HumidityProfile.HUMIDITY_SERVICE, new HumidityModelNotifyFactory());
        put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new OpticalSensorModelNotifyFactory());
    }

    private void init_read_models()
    {
        put( GAPServiceReadProfile.GAP_SERVICE, new GAPServiceReadModelFactory() );
        put( DeviceInformationReadProfile.DEVICE_INFORMATION_SERVICE,
                new DeviceInformationReadModelFactory() );
        put( ConnectionControlReadProfile.CONNECTION_CONTROL_SERVICE,
                new ConnectionControlReadModelFactory() );
    }
}
