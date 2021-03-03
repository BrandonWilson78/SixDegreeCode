package app.sixdegree.view;

import android.app.Application;
import android.content.Context;

import com.google.android.libraries.places.api.Places;

import app.sixdegree.BuildConfig;


public class ThisApp extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        ThisApp.context = getApplicationContext();
         Places.initialize(this, BuildConfig.google_app_id);
    }
    public static Context getAppContext() {
        return ThisApp.context;
    }
}
