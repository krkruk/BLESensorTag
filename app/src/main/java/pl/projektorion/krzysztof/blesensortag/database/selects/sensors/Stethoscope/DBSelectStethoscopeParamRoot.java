package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscopeParam;

/**
 * Created by krzysztof on 15.01.17.
 */

public class DBSelectStethoscopeParamRoot implements DBQueryParcelableListenerInterface {
    private List<DBSelectStethoscopeParamRootData> stethoscopeParamRootDatas;

    public DBSelectStethoscopeParamRoot(Parcel in) {
        this.stethoscopeParamRootDatas = new ArrayList<>();
        in.readTypedList(stethoscopeParamRootDatas, DBSelectStethoscopeParamRootData.CREATOR);
    }

    public DBSelectStethoscopeParamRoot() {
        this.stethoscopeParamRootDatas = new ArrayList<>();
    }

    @Override
    public String getQuery() {
        /*
        select ID_RECORD, NotifyInterval, DATE from StethoscopeParam s join Record r on s.ID_RECORD = r._id;
         */
        return "select r." +
                DBRootTableRecord.COLUMN_ID + ", " +
                DBTableStethoscopeParam.COLUMN_ROOT_REF + ", " +
                DBTableStethoscopeParam.NOTIFY_INTERVAL + ", " +
                DBRootTableRecord.COLUMN_DATE + " from " +
                DBTableStethoscopeParam.TABLE_NAME + " s join " +
                DBRootTableRecord.TABLE_NAME + " r on s." +
                DBTableStethoscopeParam.COLUMN_ROOT_REF + " = r." +
                DBRootTableRecord.COLUMN_ID + " ORDER BY " +
                DBRootTableRecord.COLUMN_DATE + " DESC;";
    }

    @Override
    public String[] getQueryData() {
        return new String[0];
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        do{
            parse_cursor(cursor);
        }while (cursor.moveToNext());
    }

    @Override
    public DBSelectInterface getRecord() {
        return stethoscopeParamRootDatas.size() > 0
                ? stethoscopeParamRootDatas.get(0)
                : null;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        return stethoscopeParamRootDatas;
    }

    @Override
    public String getLabel() {
        return DBSelectStethoscopeParamRootData.CSV_HEADER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(stethoscopeParamRootDatas);
    }

    private void parse_cursor(Cursor cursor)
    {
        stethoscopeParamRootDatas.add(new DBSelectStethoscopeParamRootData(cursor));
    }

    public static final Parcelable.Creator<DBSelectStethoscopeParamRoot> CREATOR = new Creator<DBSelectStethoscopeParamRoot>() {
        @Override
        public DBSelectStethoscopeParamRoot createFromParcel(Parcel source) {
            return new DBSelectStethoscopeParamRoot(source);
        }

        @Override
        public DBSelectStethoscopeParamRoot[] newArray(int size) {
            return new DBSelectStethoscopeParamRoot[size];
        }
    };
}
