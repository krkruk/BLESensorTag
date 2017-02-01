package pl.projektorion.krzysztof.blesensortag.factories;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate.HeartRateGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate.HeartRateProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;

/**
 * {@link BLeDataProfileFactory} creates a set of all implemented profiles.
 * In order to do so, the user has to create all profiles by calling
 * {@link BLeDataProfileFactory#initProfiles()}. Still it is possible to
 * implement/populate by your own this functionality by
 * extending/populating {@link GenericGattProfileFactory}
 */

public class BLeDataProfileFactory extends GenericGattProfileFactory {
    private BLeGattIO gattIO;

    public BLeDataProfileFactory() {
        super();
    }

    public BLeDataProfileFactory(BLeGattIO gattIO) {
        super();
        this.gattIO = gattIO;
    }

    /**
     * Populate {@link GenericGattProfileFactory} with data.
     * {@link BLeGattIO} has to be passed beforehand, otherwise
     * an exception will be thrown.
     * @throws NullPointerException
     */
    public void initProfiles() throws NullPointerException
    {
        if( gattIO == null )
            throw new NullPointerException("No BLeGattIO passed");

        init_notifiable();
        init_readable();
    }

    /**
     * Populate {@link GenericGattProfileFactory} with data.
     * @param gattIO {@link BLeGattIO} - current BLE session
     */
    public void initProfiles(BLeGattIO gattIO)
    {
        this.gattIO = gattIO;
        initProfiles();
    }

    private void init_notifiable()
    {
        put(SimpleKeysProfile.SIMPLE_KEY_SERVICE,
                new SimpleKeysGenericProfileFactory(gattIO));
        put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureGenericProfileFactory(gattIO));
        put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureGenericProfileFactory(gattIO));
        put(MovementProfile.MOVEMENT_SERVICE,
                new MovementGenericProfileFactory(gattIO));
        put(HumidityProfile.HUMIDITY_SERVICE,
                new HumidityGenericProfileFactory(gattIO));
        put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new OpticalSensorGenericProfileFactory(gattIO));
        put(HeartRateProfile.HEART_RATE_SERVICE,
                new HeartRateGenericProfileFactory(gattIO));
    }

    private void init_readable()
    {
        put( GAPServiceReadProfile.GAP_SERVICE, new GAPServiceGenericProfileFactory(gattIO) );
        put( DeviceInformationReadProfile.DEVICE_INFORMATION_SERVICE,
                new DeviceInformationGenericProfileFactory(gattIO) );
        put( ConnectionControlReadProfile.CONNECTION_CONTROL_SERVICE,
                new ConnectionControlGenericProfileFactory(gattIO));
    }
}
