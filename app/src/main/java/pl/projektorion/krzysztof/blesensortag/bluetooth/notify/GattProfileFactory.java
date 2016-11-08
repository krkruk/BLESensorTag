package pl.projektorion.krzysztof.blesensortag.bluetooth.notify;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by krzysztof on 02.11.16.
 */

public class GattProfileFactory {
    private Map<UUID, ProfileNotifyFactory> methodFactories;

    public GattProfileFactory() {
        this.methodFactories = new HashMap<>();
    }

    public void put(UUID serviceUuid, ProfileNotifyFactory factory)
    {
        try {
            this.methodFactories.put(serviceUuid, factory);
        } catch (Exception e){}
    }

    public GenericGattNotifyProfileInterface createProfile(UUID serviceUuid) {
        ProfileNotifyFactory factory = methodFactories.get(serviceUuid);
        if( factory != null )
            return factory.createProfile();
        else
            return new NullNotifyProfile();
    }
}
