package pl.projektorion.krzysztof.blesensortag.bluetooth.notify;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyProfileInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public class NullNotifyProfile implements GenericGattNotifyProfileInterface {
    public NullNotifyProfile() {
    }

    public NullNotifyProfile(BLeGattIO gattClient) {
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
    public void configurePeriod(byte input) {}

    @Override
    public String getName() { return ""; }

    /**
     * Get data {@link UUID}. This method can be utilized to detect
     * whether the profile is legit.
     * @return null if NullObject, some value defined in subclasses of
     * {@link GenericGattNotifyProfileInterface}
     */
    @Override
    public UUID getDataUuid() {
        return null;
    }
}
