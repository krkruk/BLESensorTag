package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

/**
 * Created by krzysztof on 10.11.16.
 */

public interface ConnectionControlRequestInterface {
    void requestConnectionInterval(int min, int max);
    void requestSlaveLatency(int latency);
    void requestSupervisionTimeout(int timeout);

    byte[] getRequest();
}
