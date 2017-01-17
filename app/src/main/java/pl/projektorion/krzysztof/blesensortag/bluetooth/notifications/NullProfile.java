package pl.projektorion.krzysztof.blesensortag.bluetooth.notifications;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public class NullProfile implements NotifyGattProfileInterface {
    public NullProfile() {
    }

    public NullProfile(BLeGattIO gattClient) {
    }

    @Override
    public void enableNotification(boolean state) {}

    @Override
    public boolean isNotifying() {
        return false;
    }

    @Override
    public void enableMeasurement(int state) {}

    @Override
    public int isMeasuring() {
        return 0;
    }

    @Override
    public void configurePeriod(int input) {}

    @Override
    public double getPeriod() { return 0; }

    @Override
    public String getName() { return ""; }

    @Override
    public boolean isService(UUID serviceUuid) {
        return false;
    }

    /**
     * Get data {@link UUID}. This method can be utilized to detect
     * whether the profile is legit.
     * @return null if NullObject, some value defined in subclasses of
     * {@link NotifyGattProfileInterface}
     */
    @Override
    public UUID getDataUuid() {
        return null;
    }
}
