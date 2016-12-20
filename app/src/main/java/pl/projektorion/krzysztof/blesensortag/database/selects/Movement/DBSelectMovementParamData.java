package pl.projektorion.krzysztof.blesensortag.database.selects.Movement;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectMovementParamData extends DBSelectGeneralSensorParamData {
    public DBSelectMovementParamData() {
        super();
    }

    public DBSelectMovementParamData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectMovementParamData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectMovementParamData> CREATOR = new Creator<DBSelectMovementParamData>() {
        @Override
        public DBSelectMovementParamData createFromParcel(Parcel source) {
            return new DBSelectMovementParamData(source);
        }

        @Override
        public DBSelectMovementParamData[] newArray(int size) {
            return new DBSelectMovementParamData[size];
        }
    };
}
