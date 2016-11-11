package pl.projektorion.krzysztof.blesensortag.bluetooth.read;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;

/**
 * Created by krzysztof on 08.11.16.
 */

public interface GenericGattReadProfileInterface extends GenericGattProfileInterface{
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
}
