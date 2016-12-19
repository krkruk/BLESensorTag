package pl.projektorion.krzysztof.blesensortag.database.tables;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBTableFactory {
    private Map<UUID, DBTableFactoryInterface> tableFactories;

    public DBTableFactory() {
        this.tableFactories = new HashMap<>();
    }

    public void add(UUID serviceUuid, DBTableFactoryInterface tableFactory)
    {
        tableFactories.put(serviceUuid, tableFactory);
    }

    public void extend(Map<UUID, DBTableFactoryInterface> tableFactories)
    {
        this.tableFactories.putAll(tableFactories);
    }

    public Set<UUID> getUuids()
    {
        return tableFactories.keySet();
    }

    public Collection<DBTableFactoryInterface> getFactories()
    {
        return tableFactories.values();
    }

    public DBTableInterface createTable(UUID serviceUuid)
    {
        DBTableFactoryInterface factory =  tableFactories.get(serviceUuid);
        if( factory == null ) return new DBTableNull();
        return factory.createTable();
    }
}
