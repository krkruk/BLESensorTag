package pl.projektorion.krzysztof.blesensortag.database.selects.Barometer;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorCountDataAbstract;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectBarometerCountData extends DBSelectSensorCountDataAbstract {

    public DBSelectBarometerCountData() {
        super();
    }

    public DBSelectBarometerCountData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectBarometerCountData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectBarometerCountData> CREATOR = new Creator<DBSelectBarometerCountData>() {
        @Override
        public DBSelectBarometerCountData createFromParcel(Parcel source) {
            return new DBSelectBarometerCountData(source);
        }

        @Override
        public DBSelectBarometerCountData[] newArray(int size) {
            return new DBSelectBarometerCountData[size];
        }
    };
}
