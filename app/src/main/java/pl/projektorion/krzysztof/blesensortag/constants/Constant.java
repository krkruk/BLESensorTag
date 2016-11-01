package pl.projektorion.krzysztof.blesensortag.constants;

import java.util.UUID;

/**
 * Created by krzysztof on 26.10.16.
 */

public interface Constant {
    String BLEDEV_ERR_TAG = "BLEDISC_ERR";
    String GATT_ERR = "GATT_SERVER_ERR";
    String BLESSS_ERR = "BLESSS_ERR";

    String CONTEXT_ERR = "No Context found";
    String NO_INTENT_DATA_ERR = "No data passed in Intent";
    String GATT_FAILURE = "GATT FAILED. #%d";

    UUID CLIENT_CHARACTERISTIC_CONFIGURATION_UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
}
