package pl.projektorion.krzysztof.blesensortag.utils.path;

/**
 * Created by krzysztof on 14.01.17.
 */

public class PathDefault implements PathInterface {
    private String dbName;

    public PathDefault(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String getName() {
        return dbName;
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public String getFull()
    {
        return dbName;
    }

}
