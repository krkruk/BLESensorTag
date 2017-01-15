package pl.projektorion.krzysztof.blesensortag.database.inserts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBInsertFactory extends HashMap<UUID, DBInsertFactoryInterface> {

    public DBInsertFactory() {
        super();
    }

    public DBInsertInterface createRow(UUID uuid)
    {
        DBInsertFactoryInterface factory = get(uuid);
        if( factory == null ) return new DBInsertNull();
        return factory.create();
    }
}
