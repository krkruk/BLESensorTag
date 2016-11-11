package pl.projektorion.krzysztof.blesensortag.bluetooth;

import java.util.UUID;

/**
 * Created by krzysztof on 11.11.16.
 */

public interface GenericProfileFactory {
    GenericGattProfileInterface createProfile();
}
