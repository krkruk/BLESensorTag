package pl.projektorion.krzysztof.blesensortag;

import android.app.Application;
import android.content.Context;

/**
 * Created by krzysztof on 12.01.17.
 */
public class AppContext extends Application {
    public static Context context;

    private static AppContext ourInstance = new AppContext();

    public static AppContext getInstance() {
        return ourInstance;
    }

    public static Context getContext()
    {
        return context;
    }

    public AppContext() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
