package pl.projektorion.krzysztof.blesensortag.data;

import java.util.LinkedHashMap;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.GenericGattReadProfileInterface;


/**
 * {@link BLeAvailableGattProfiles} allows holding data of all discovered profiles.
 * The data structure is as follow:
 * {@link UUID} Service UUID of the profile
 * {@link GenericGattProfileInterface} Profile reference. All profiles has to be
 * derived from that interface
 */
public class BLeAvailableGattProfiles extends LinkedHashMap<UUID, GenericGattProfileInterface> {
    public void demandReadProfileValues(UUID valueUuid)
    {
        for( GenericGattProfileInterface profile : this.values() )
        {
            if( profile instanceof GenericGattReadProfileInterface
                    && profile.isService(valueUuid) ) {
                ((GenericGattReadProfileInterface)profile)
                        .demandReadCharacteristics(GenericGattReadProfileInterface.ATTRIBUTE_ALL);
            }
        }
    }

    public void enableAllNotifications()
    {
        for(GenericGattProfileInterface profile : this.values())
            if( profile instanceof NotifyGattProfileInterface)
                ((NotifyGattProfileInterface)profile).enableNotification(true);
    }

    public void enableAllMeasurements()
    {
        for(GenericGattProfileInterface profile : this.values())
            if( profile instanceof NotifyGattProfileInterface )
                ((NotifyGattProfileInterface)profile)
                        .enableMeasurement(
                                NotifyGattProfileInterface.ENABLE_ALL_MEASUREMENTS);
    }
}
