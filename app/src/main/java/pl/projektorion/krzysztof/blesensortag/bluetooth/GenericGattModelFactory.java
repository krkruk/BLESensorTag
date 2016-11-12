package pl.projektorion.krzysztof.blesensortag.bluetooth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by krzysztof on 12.11.16.
 */

public class GenericGattModelFactory {
    private Map<UUID, GenericModelFactory> models;

    public GenericGattModelFactory() {
        models = new HashMap<>();
    }

    public void put(UUID uuid, GenericModelFactory profile)
    {
        models.put(uuid, profile);
    }

    public GenericGattModelInterface createModel(UUID modelUuid) {
        GenericModelFactory profile = models.get(modelUuid);
        if( profile == null )
            return new GenericNullModel();
        return profile.createModel();
    }
}
