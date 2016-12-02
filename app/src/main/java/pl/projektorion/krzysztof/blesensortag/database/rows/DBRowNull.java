package pl.projektorion.krzysztof.blesensortag.database.rows;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBRowNull implements DBRowInterface, Observer {
    @Override
    public String getTableName() {
        return "";
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
