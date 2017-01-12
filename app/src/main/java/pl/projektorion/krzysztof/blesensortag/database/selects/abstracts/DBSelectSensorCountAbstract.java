package pl.projektorion.krzysztof.blesensortag.database.selects.abstracts;

import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 25.12.16.
 */

public abstract class DBSelectSensorCountAbstract implements DBQueryParcelableListenerInterface {

    private DBSelectInterface rootRecord;

    public DBSelectSensorCountAbstract(DBSelectInterface rootRecord) {
        this.rootRecord = rootRecord;
    }

    public DBSelectSensorCountAbstract(Parcel in)
    {
        this.rootRecord = in.readParcelable(DBSelectInterface.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(rootRecord, flags);
    }

    @Override
    public String getQuery() {
        return "select count(*) from " +
                table_name() + " where " +
                grouping_column() + " = ?;";
    }

    @Override
    public String[] getQueryData() {
        return new String[] {
                Long.toString((long) rootRecord.getData(DBSelectInterface.ATTRIBUTE_ID))
        };
    }

    @Override
    public String getLabel() {
        return "DBSelectSensorCountAbstract";
    }

    protected abstract String table_name();
    protected abstract String grouping_column();
}
