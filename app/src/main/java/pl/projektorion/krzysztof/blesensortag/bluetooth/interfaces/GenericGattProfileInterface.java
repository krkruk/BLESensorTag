package pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces;

import java.util.UUID;

/**
 * Created by krzysztof on 11.11.16.
 */

public interface GenericGattProfileInterface {

    /**
     * Get human-friendly name of the service
     * @return {@link String} human-friendly name
     */
    String getName();

    /**
     * Check whether {@param serviceUuid} {@link UUID }is the same as
     * one represented by the object.
     * @param serviceUuid {@link UUID} of the service to be compared
     * @return boolean - true if matches, false otherwise
     */
    boolean isService(UUID serviceUuid);
}
