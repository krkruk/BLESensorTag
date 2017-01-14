package pl.projektorion.krzysztof.blesensortag.database.tables;

import java.util.ArrayList;
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

public class DBTableFactory extends ArrayList<DBTableFactoryInterface>{

    public DBTableFactory() {
        super();
    }

    public DBTableInterface createTable(int index)
    {
        index = index < size() && index >= 0 ? index : -1;
        if( index == -1 ) return new DBTableNull();
        DBTableFactoryInterface factory = get(index);
        return factory.createTable();
    }
}
