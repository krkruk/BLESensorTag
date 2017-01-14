package pl.projektorion.krzysztof.blesensortag.database.path;

import android.os.Environment;

import java.io.File;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBPathExternal implements DBPathInterface {
    private String dbName;
    private String appDir;

    public DBPathExternal(String dbName, String appDir) {
        this.dbName = dbName;
        this.appDir = appDir;
    }

    @Override
    public String getDbName() {
        final boolean canWriteExternalStorage = isExternalStorageMounted();

        return canWriteExternalStorage
                ? Environment.getExternalStorageDirectory() + File.separator
                    + appDir + File.separator + dbName
                : dbName;
    }

    private boolean isExternalStorageMounted()
    {
        final String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
