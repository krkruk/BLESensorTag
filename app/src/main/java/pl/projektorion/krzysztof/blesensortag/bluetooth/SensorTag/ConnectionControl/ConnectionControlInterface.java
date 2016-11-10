package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadProfileInterface;

/**
 * Created by krzysztof on 10.11.16.
 */

public interface ConnectionControlInterface extends GenericGattReadProfileInterface {
    void requestConnectionParams(ConnectionControlRequestInterface requestInterface);
    void requestDisconnect();
}
