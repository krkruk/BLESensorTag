package pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces;

/**
 * Created by krzysztof on 16.12.16.
 */

public interface DBInsertParamInterface extends DBInsertInterface {
    void insert(DBParamDataInterface data);
}
