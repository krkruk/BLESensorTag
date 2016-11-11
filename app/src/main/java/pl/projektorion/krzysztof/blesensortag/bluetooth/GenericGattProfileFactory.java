package pl.projektorion.krzysztof.blesensortag.bluetooth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by krzysztof on 11.11.16.
 */

public class GenericGattProfileFactory {
    private Map<UUID, GenericProfileFactory> profiles;

    public GenericGattProfileFactory() {
        profiles = new HashMap<>();
    }

    public void put(UUID uuid, GenericProfileFactory profile)
    {
        profiles.put(uuid, profile);
    }

    public GenericGattProfileInterface createProfile(UUID profileUuid) {
        GenericProfileFactory profile = profiles.get(profileUuid);
        if( profile == null )
            return new GenericNullProfile();
        return profile.createProfile();
    }
}
