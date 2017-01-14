package pl.projektorion.krzysztof.blesensortag.utils.path;

import android.os.Environment;

import java.io.File;

/**
 * Created by krzysztof on 14.01.17.
 */

public class PathExternal implements PathInterface {
    private String dbName;
    private String appDir;

    public PathExternal(String dbName, String appDir) {
        this.dbName = dbName;
        this.appDir = verify_app_dir(appDir);
    }

    @Override
    public String getName() {
        return dbName;
    }

    @Override
    public String getPath() {
        return appDir;
    }

    @Override
    public String getFull() {
        return appDir + File.separator + dbName;
    }

    private String verify_app_dir(String appDir)
    {
        final boolean canWriteExternalStorage = isExternalStorageMounted();
        return canWriteExternalStorage
                ? Environment.getExternalStorageDirectory() + File.separator + appDir
                : "";
    }

    private boolean isExternalStorageMounted()
    {
        final String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
