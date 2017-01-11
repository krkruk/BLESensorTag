package pl.projektorion.krzysztof.blesensortag.fragments.config;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 11.11.16.
 */

public interface FragmentNotifyProfileConfigInterface {
    public void setProfile(NotifyGattProfileInterface profile);

    public void setNotificationWidgetEnabled(boolean enabled);

    public void setMeasurementSwitchEnabled(boolean enabled);

    public void setPeriodSeekBarEnabled(boolean enabled);

    public void setPeriodMinValue(int value);
}
