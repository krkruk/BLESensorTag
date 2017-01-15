package pl.projektorion.krzysztof.blesensortag.factories;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactory;

/**
 * Created by krzysztof on 15.01.17.
 */

public interface DBFactoryForInsertsFactoryInterface {
    DBInsertFactory create(DBRowWriter dbWriter);
}
