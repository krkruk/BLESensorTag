package pl.projektorion.krzysztof.blesensortag.database.selects;

import android.os.Parcelable;

/**
 * Created by krzysztof on 18.12.16.
 */

public interface DBSelectInterface extends Parcelable {
    int ATTRIBUTE_ID = 0x01;
    int ATTRIBUTE_CSV_HEADER = 0xffff;

    Object getData(int recordAttribute);
}
