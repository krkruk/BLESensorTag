package pl.projektorion.krzysztof.blesensortag.fragments;

import android.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;

/**
 * Created by krzysztof on 02.11.16.
 */

public class BLeFragmentsFactory {
    private Map<UUID, FragmentFactory> factoryObjects;

    public BLeFragmentsFactory() {
        this.factoryObjects = new HashMap<>();
    }

    public void put(UUID uuid, FragmentFactory fragmentFactory)
    {
        try {
            this.factoryObjects.put(uuid, fragmentFactory);
        } catch (Exception e){}
    }

    public FragmentFactory get(UUID uuid)
    {
        return factoryObjects.get(uuid);
    }

    public Fragment create(UUID uuid)
    {
        FragmentFactory fragmentFactory = factoryObjects.get(uuid);
        if(fragmentFactory == null)
            return null;
        return fragmentFactory.create();
    }
}
