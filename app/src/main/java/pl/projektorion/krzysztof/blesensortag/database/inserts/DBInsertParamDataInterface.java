package pl.projektorion.krzysztof.blesensortag.database.inserts;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 16.12.16.
 */

public interface DBInsertParamDataInterface {
    int NOTIFY_INTERVAL_PARAMETER = 0x01;


    NotifyGattProfileInterface get();

    Object getData(int parameter);
}
