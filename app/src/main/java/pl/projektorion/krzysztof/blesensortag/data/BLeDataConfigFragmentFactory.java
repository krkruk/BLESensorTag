package pl.projektorion.krzysztof.blesensortag.data;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.fragments.BLeFragmentsFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.BarometricPressureNotifyConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.HumidityNotifyConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.IRTemperatureNotifyConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.MovementNotifyConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.OpticalSensorNotifyFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.SimpleKeysNotifyConfigFragmentFactory;

/**
 * {@link BLeDataConfigFragmentFactory} creates a set of available configuration fragments.
 * The class extends {@link BLeFragmentsFactory}. In order to populate the object
 * {@link BLeDataConfigFragmentFactory#initConfigFragmentFactory()} must be called.
 * This implementation can be also achieved by populating the parent class by
 * your own. No extra logic was added.
 */
public class BLeDataConfigFragmentFactory extends BLeFragmentsFactory {
    private BLeAvailableGattProfiles availableGattProfiles;

    public BLeDataConfigFragmentFactory() {
        super();
    }

    public BLeDataConfigFragmentFactory(BLeAvailableGattProfiles availableGattProfiles) {
        super();
        this.availableGattProfiles = availableGattProfiles;
    }

    /**
     * Create and assign appropriate config fragments to the Gatt profile.
     * @throws NullPointerException {@link BLeAvailableGattProfiles} has to be passed beforehand,
     * otherwise an exception will be thrown.
     */
    public void initConfigFragmentFactory() throws NullPointerException
    {
        if( availableGattProfiles == null )
            throw new NullPointerException("No available Gatt Profiles");

        GenericGattProfileInterface buffer
                = availableGattProfiles.get(SimpleKeysProfile.SIMPLE_KEY_SERVICE);
        put(SimpleKeysProfile.SIMPLE_KEY_SERVICE,
                new SimpleKeysNotifyConfigFragmentFactory(buffer));

        buffer = availableGattProfiles.get(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE);
        put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureNotifyConfigFragmentFactory(buffer));

        buffer = availableGattProfiles.get(IRTemperatureProfile.IR_TEMPERATURE_SERVICE);
        put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureNotifyConfigFragmentFactory(buffer));

        buffer = availableGattProfiles.get(MovementProfile.MOVEMENT_SERVICE);
        put(MovementProfile.MOVEMENT_SERVICE,
                new MovementNotifyConfigFragmentFactory(buffer));

        buffer = availableGattProfiles.get(HumidityProfile.HUMIDITY_SERVICE);
        put(HumidityProfile.HUMIDITY_SERVICE,
                new HumidityNotifyConfigFragmentFactory(buffer));

        buffer = availableGattProfiles.get(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE);
        put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new OpticalSensorNotifyFragmentFactory(buffer));
    }

    /**
     * Create and assign appropriate config fragments to the Gatt profile.
     * @param availableGattProfiles A set of all available/discovered services. Based upon this,
     *                              the fragment factories will be created
     */
    public void initConfigFragmentFactory(BLeAvailableGattProfiles availableGattProfiles)
    {
        this.availableGattProfiles = availableGattProfiles;
        initConfigFragmentFactory();
    }
}
