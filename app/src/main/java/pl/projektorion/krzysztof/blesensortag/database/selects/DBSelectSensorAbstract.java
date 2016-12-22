package pl.projektorion.krzysztof.blesensortag.database.selects;

import android.database.Cursor;
import android.os.Parcel;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;

/**
 * Created by krzysztof on 22.12.16.
 */

public abstract class DBSelectSensorAbstract implements DBQueryWithLimitsListenerInterface {

    private static final int MIN_NO_ELEMS = 0;
    private static final int MIN_OFFSET = 1;
    protected DBSelectInterface rootRecord;
    private final long notifyPeriod;

    private long startAt = -1;
    private long noElements = -1;

    public DBSelectSensorAbstract(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        this.notifyPeriod = (long) sensorRecord.getData(DBSelectGeneralSensorParamData.ATTRIBUTE_NOTIFY_PERIOD);
        this.rootRecord = rootRecord;
    }

    public DBSelectSensorAbstract(Parcel in) {
        rootRecord = in.readParcelable(DBSelectInterface.class.getClassLoader());
        notifyPeriod = in.readLong();
        startAt = in.readLong();
        noElements = in.readLong();
    }

    @Override
    public String getQuery() {
        if( startAt < MIN_OFFSET || noElements < MIN_NO_ELEMS)
            return "select " +
                    select_db_columns() + " from " +
                    select_table_name() +
                    " where " + select_extract_records_where() +" = ?;";

        return "select " +
            select_db_columns() + " from " +
            select_table_name() +
            " where " + select_extract_records_where() +" = ?" +
            " limit ? offset ?;";
    }
    @Override
    public String[] getQueryData() {
        if( startAt < MIN_OFFSET || noElements < MIN_NO_ELEMS)
            return new String[]{
                    Long.toString( (long) rootRecord.getData(DBSelectInterface.ATTRIBUTE_ID) )
            };

        return new String[] {
                Long.toString( (long)rootRecord.getData(DBSelectInterface.ATTRIBUTE_ID) ),
                Long.toString(noElements),
                Long.toString(startAt)
        };
    }

    @Override
    public synchronized void onQueryExecuted(Cursor cursor) {
        long time = 0;
        do
        {
            parse_cursor_data(time, cursor);
            time += notifyPeriod;
        } while (cursor.moveToNext());
    }

    @Override
    public void setLimit(long startAt, long noElements) {
        this.startAt = startAt;
        this.noElements = noElements;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(rootRecord, flags);
        dest.writeLong(notifyPeriod);
        dest.writeLong(startAt);
        dest.writeLong(noElements);
    }

    protected abstract void parse_cursor_data(long time, Cursor cursor);
    protected abstract String select_db_columns();
    protected abstract String select_table_name();
    protected abstract String select_extract_records_where();

    protected long getStartAt() {
        return startAt;
    }

    protected long getNoElements() {
        return noElements;
    }
}
