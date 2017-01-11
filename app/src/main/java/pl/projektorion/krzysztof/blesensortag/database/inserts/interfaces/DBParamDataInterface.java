package pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 16.12.16.
 */

public interface DBParamDataInterface {
    int NOTIFY_INTERVAL_PARAMETER = 0x01;


    NotifyGattProfileInterface get();

    Object getData(int parameter);
}
