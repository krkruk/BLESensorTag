package pl.projektorion.krzysztof.blesensortag.database.selects.Movement;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountDataAbstract;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectMovementCountData extends DBSelectSensorCountDataAbstract {

    public DBSelectMovementCountData() {
        super();
    }

    public DBSelectMovementCountData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectMovementCountData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectMovementCountData> CREATOR = new Creator<DBSelectMovementCountData>() {
        @Override
        public DBSelectMovementCountData createFromParcel(Parcel source) {
            return new DBSelectMovementCountData(source);
        }

        @Override
        public DBSelectMovementCountData[] newArray(int size) {
            return new DBSelectMovementCountData[size];
        }
    };
}
