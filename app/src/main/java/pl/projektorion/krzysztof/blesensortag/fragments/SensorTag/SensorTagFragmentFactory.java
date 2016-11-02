package pl.projektorion.krzysztof.blesensortag.fragments.SensorTag;

import android.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SensorTagFragmentFactory {
    private Map<UUID, FragmentFactory> factoryObjects;

    public SensorTagFragmentFactory() {
        this.factoryObjects = new HashMap<>();
    }

    public void put(UUID uuid, FragmentFactory fragmentFactory)
    {
        try {
            this.factoryObjects.put(uuid, fragmentFactory);
        } catch (Exception e){}
    }

    public Fragment create(UUID uuid)
    {
        FragmentFactory fragmentFactory = factoryObjects.get(uuid);
        if(fragmentFactory == null)
            return null;
        return fragmentFactory.create();
    }
}
