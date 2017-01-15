package pl.projektorion.krzysztof.blesensortag.database.inserts;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBParamDataInterface;

/**
 * {@link DBParamData} allows extracting data from BLE profiles.
 * Crucial part for DBInsert(sensor)Param.
 */
public class DBParamData implements DBParamDataInterface {

    private NotifyGattProfileInterface profile;

    public DBParamData(NotifyGattProfileInterface profile) {
        this.profile = profile;
    }

    @Override
    public NotifyGattProfileInterface get() {
        return profile;
    }

    @Override
    public Object getData(int parameter) {
        switch (parameter)
        {
            case NOTIFY_INTERVAL_PARAMETER:
                return profile.getPeriod();
            default:
                return null;
        }
    }
}
