package pl.projektorion.krzysztof.blesensortag.bluetooth.read;

import java.util.UUID;

/**
 * Created by krzysztof on 08.11.16.
 */

public interface GenericGattReadProfileInterface {
    /**
     * Demand all attributes the service has.
     */
    int ATTRIBUTE_ALL = Integer.MAX_VALUE;

    /**
     * Demand receiving values from a remote service. See children
     * classes for more information
     * @param attributeToBeRead int - attribute(s) to be read
     */
    void demandReadCharacteristics(int attributeToBeRead);

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
