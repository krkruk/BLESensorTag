package pl.projektorion.krzysztof.blesensortag.database.path;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBPathDefault implements DBPathInterface {
    private String dbName;

    public DBPathDefault(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String getDbName()
    {
        return dbName;
    }
}
