package pl.projektorion.krzysztof.blesensortag.database.rows;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBRowFactory {
    private Map<UUID, DBRowFactoryInterface> rowFactories;

    public DBRowFactory() {
        rowFactories = new HashMap<>();
    }

    public void add(UUID uuid, DBRowFactoryInterface rowFactory)
    {
        rowFactories.put(uuid, rowFactory);
    }

    public DBRowInterface createRow(UUID uuid)
    {
        DBRowFactoryInterface factory = rowFactories.get(uuid);
        if( factory == null ) return new DBRowNull();
        return factory.createRow();
    }
}
