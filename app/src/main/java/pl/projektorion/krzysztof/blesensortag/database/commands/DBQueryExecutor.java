package pl.projektorion.krzysztof.blesensortag.database.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBQueryExecutor {

    private Queue<DBQueryInterface> queries;

    public DBQueryExecutor() {
        queries = new ConcurrentLinkedQueue<>();
    }

    public DBQueryExecutor(List<DBQueryInterface> queries) {
        this.queries = new ConcurrentLinkedQueue<>();
        extend(queries);
    }

    public void add(DBQueryInterface query)
    {
        queries.add(query);
    }

    public void extend(List<DBQueryInterface> queries)
    {
        this.queries.addAll(queries);
    }

    public void executeAll()
    {
        while ( !queries.isEmpty() )
        {
            DBQueryInterface query = queries.poll();
            if( query == null ) return;
            query.execute();
        }
    }
}
