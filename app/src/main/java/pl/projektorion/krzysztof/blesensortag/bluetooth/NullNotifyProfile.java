package pl.projektorion.krzysztof.blesensortag.bluetooth;

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
}
