package pl.projektorion.krzysztof.blesensortag.database.inserts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBInsertFactory {
    private Map<UUID, DBInsertFactoryInterface> rowFactories;

    public DBInsertFactory() {
        rowFactories = new HashMap<>();
    }

    public void add(UUID uuid, DBInsertFactoryInterface rowFactory)
    {
        rowFactories.put(uuid, rowFactory);
    }

    public DBInsertInterface createRow(UUID uuid)
    {
        DBInsertFactoryInterface factory = rowFactories.get(uuid);
        if( factory == null ) return new DBInsertNull();
        return factory.createRow();
    }
}
