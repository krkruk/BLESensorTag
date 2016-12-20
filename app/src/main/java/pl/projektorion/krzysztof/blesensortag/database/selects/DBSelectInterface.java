package pl.projektorion.krzysztof.blesensortag.database.selects;

import android.os.Parcelable;

/**
 * Created by krzysztof on 18.12.16.
 */

public interface DBSelectInterface extends Parcelable {
    int ATTRIBUTE_ID = 0x01;

    Object getData(int recordAttribute);
}
