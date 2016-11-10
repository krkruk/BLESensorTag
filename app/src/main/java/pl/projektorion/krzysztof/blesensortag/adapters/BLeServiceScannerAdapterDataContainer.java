package pl.projektorion.krzysztof.blesensortag.adapters;

import java.util.UUID;

/**
 * Created by krzysztof on 10.11.16.
 */

public class BLeServiceScannerAdapterDataContainer {
    private UUID serviceUuid;
    private String serviceName;

    public BLeServiceScannerAdapterDataContainer(String serviceName, UUID serviceUuid) {
        this.serviceName = serviceName;
        this.serviceUuid = serviceUuid;
    }

    public UUID getServiceUuid() {
        return serviceUuid;
    }

    public void setServiceUuid(UUID serviceUuid) {
        this.serviceUuid = serviceUuid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
