package pl.projektorion.krzysztof.blesensortag.bluetooth.notify;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by krzysztof on 06.11.16.
 */

public class GattModelFactory {
    private Map<UUID, ModelNotifyFactory> methodFactories;

    public GattModelFactory() {
        this.methodFactories = new HashMap<>();
    }

    public void put(UUID serviceUuid, ModelNotifyFactory factory)
    {
        try {
            this.methodFactories.put(serviceUuid, factory);
        } catch (Exception e){}
    }

    public GenericGattNotifyModelInterface createObserver(UUID serviceUuid) {
        ModelNotifyFactory model = methodFactories.get(serviceUuid);
        if( model == null )
            return new NullNotifyModel();
        return model.createModel();
    }
}
