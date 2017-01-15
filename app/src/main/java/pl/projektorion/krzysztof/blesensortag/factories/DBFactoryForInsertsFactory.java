package pl.projektorion.krzysztof.blesensortag.factories;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactory;

/**
 * Created by krzysztof on 15.01.17.
 */

public abstract class DBFactoryForInsertsFactory implements DBFactoryForInsertsFactoryInterface {

    private DBInsertFactory factory;
    public DBFactoryForInsertsFactory() {
        this.factory = new DBInsertFactory();
    }

    @Override
    public DBInsertFactory create(DBRowWriter dbWriter) {
        factory = init_factory(factory, dbWriter);
        return factory;
    }

    protected abstract DBInsertFactory init_factory(DBInsertFactory factory, DBRowWriter dbRowWriter);
}
