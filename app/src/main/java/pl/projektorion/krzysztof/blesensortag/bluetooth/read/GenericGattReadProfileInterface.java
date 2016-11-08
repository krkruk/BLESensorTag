package pl.projektorion.krzysztof.blesensortag.bluetooth.read;

/**
 * Created by krzysztof on 08.11.16.
 */

public interface GenericGattReadProfileInterface {
    int ATTRIBUTE_ALL = Integer.MAX_VALUE;

    void demandReadCharacteristics(int attributeToBeRead);
    String getName();
}
