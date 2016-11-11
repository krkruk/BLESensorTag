package pl.projektorion.krzysztof.blesensortag.fragments.config;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 11.11.16.
 */

public interface NotifyProfileConfigInterface {
    public void setProfile(NotifyGattProfileInterface profile);

    public void setNotificationWidgetEnabled(boolean enabled);

    public void setMeasurementSwitchEnabled(boolean enabled);

    public void setPeriodSeekBarEnabled(boolean enabled);

    public void setPeriodMinValue(int value);
}
