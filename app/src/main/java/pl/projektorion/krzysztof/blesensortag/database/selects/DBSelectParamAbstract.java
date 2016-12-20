package pl.projektorion.krzysztof.blesensortag.database.selects;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryListenerInterface;

/**
 * Created by krzysztof on 20.12.16.
 */

public abstract class DBSelectParamAbstract implements DBQueryListenerInterface {
    protected final DBSelectInterface rootRecord;

    public DBSelectParamAbstract(DBSelectInterface rootRecord) {
        this.rootRecord = rootRecord;
    }

    @Override
    public String getQuery() {
        return "select " +
                column_id() + ", " +
                column_root_ref() + ", " +
                column_notify_interval() + " " +
                "from " +
                table_name() + " " +
                "where " + column_root_ref() +
                " = ? limit 1";
    }

    @Override
    public String[] getQueryData() {
        return new String[]{
                Long.toString( (long) rootRecord.getData(DBSelectInterface.ATTRIBUTE_ID) )
        };
    }

    protected abstract String column_id();
    protected abstract String column_root_ref();
    protected abstract String column_notify_interval();
    protected abstract String table_name();
}
