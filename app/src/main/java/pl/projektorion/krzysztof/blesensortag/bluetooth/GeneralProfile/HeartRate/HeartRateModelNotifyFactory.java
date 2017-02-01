package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.GenericGattNotificationModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ModelNotifyFactory;

/**
 * Created by krzysztof on 01.02.17.
 */

public class HeartRateModelNotifyFactory implements GenericModelFactory {
    @Override
    public GenericGattNotificationModelInterface createModel() {
        return new HeartRateModel();
    }
}
