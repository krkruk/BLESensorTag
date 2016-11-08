package pl.projektorion.krzysztof.blesensortag.bluetooth.read;

import java.util.UUID;

/**
 * Created by krzysztof on 08.11.16.
 */

public interface GenericGattReadProfileInterface {
    int ATTRIBUTE_ALL = Integer.MAX_VALUE;

    void demandReadCharacteristics(int attributeToBeRead);
    String getName();
    boolean isService(UUID serviceUuid);
}
