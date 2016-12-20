package pl.projektorion.krzysztof.blesensortag.database.selects;

import android.database.Cursor;
import android.util.Log;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectBarometerParamData extends DBSelectDataAbstract {

    public static final int ATTRIBUTE_RECORD_ID = 0x02;
    public static final int ATTRIBUTE_NOTIFY_PERIOD = 0x03;

    private static final int COLUMN_COUNT = 3;
    private long _id = -1;
    private long recordId = -1;
    private long notifyPeriod = 1;

    public DBSelectBarometerParamData() {
        super();
    }

    public DBSelectBarometerParamData(Cursor cursor) {
        super(cursor);
        parse(cursor);
    }

    @Override
    protected void parse(Cursor cursor) {
        if( cursor.getColumnCount() != COLUMN_COUNT) {
            Log.d("BarParam", "Column count invalid");
            return;
        }
        _id = cursor.getLong(0);
        recordId = cursor.getLong(1);
        notifyPeriod = cursor.getLong(2);
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_ID:
                return _id;
            case ATTRIBUTE_RECORD_ID:
                return recordId;
            case ATTRIBUTE_NOTIFY_PERIOD:
                return notifyPeriod;
            default: return 0;
        }
    }
}
