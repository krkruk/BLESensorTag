package pl.projektorion.krzysztof.blesensortag.database.selects;

/**
 * Created by krzysztof on 18.12.16.
 */

public interface DBSelectInterface {
    int ATTRIBUTE_ID = 0x01;

    Object getData(int recordAttribute);
}
