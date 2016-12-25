package pl.projektorion.krzysztof.blesensortag.database.selects;

import android.database.Cursor;
import android.os.Parcel;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;

/**
 * Created by krzysztof on 22.12.16.
 */

public abstract class DBSelectSensorAbstract implements DBQueryWithLimitsListenerInterface {

    private static final int MIN_NO_ELEMS = 1;
    private static final int MIN_OFFSET = 0;
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
        long time = startAt * notifyPeriod;
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

    /**
     * Extract data from the cursor. The cursor should contain all required columns.
     * @param time - X-axis (time) or measurements ticks
     * @param cursor {@link Cursor} that reads data from the database.
     */
    protected abstract void parse_cursor_data(long time, Cursor cursor);

    /**
     * Enumerate all columns to be read. Should be presented in CSV format
     * @return {@link String} CSV of columns
     */
    protected abstract String select_db_columns();

    /**
     * Return name of the table data is to be read
     * @return {@link String} Name of the table
     */
    protected abstract String select_table_name();

    /**
     * Return string that contains column name the data is to be filtered.
     * Stands form >>select * from _table where {@link DBSelectSensorAbstract#select_extract_records_where()} = ? <<
     * @return {@link String} Name of the column
     */
    protected abstract String select_extract_records_where();

    protected long getStartAt() {
        return startAt;
    }

    protected long getNoElements() {
        return noElements;
    }
}
