package pl.projektorion.krzysztof.blesensortag.database.inserts;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertParamData implements DBInsertParamDataInterface {

    private NotifyGattProfileInterface profile;

    public DBInsertParamData(NotifyGattProfileInterface profile) {
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
