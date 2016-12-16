package pl.projektorion.krzysztof.blesensortag.database.inserts;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBInsertNull implements DBInsertInterface, Observer {
    @Override
    public String getTableName() {
        return "";
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
