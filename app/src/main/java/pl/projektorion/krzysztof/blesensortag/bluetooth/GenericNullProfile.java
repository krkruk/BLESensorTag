package pl.projektorion.krzysztof.blesensortag.bluetooth;

import java.util.UUID;

/**
 * Created by krzysztof on 11.11.16.
 */

public class GenericNullProfile implements GenericGattProfileInterface {
    public GenericNullProfile() {}

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean isService(UUID serviceUuid) {
        return false;
    }
}
