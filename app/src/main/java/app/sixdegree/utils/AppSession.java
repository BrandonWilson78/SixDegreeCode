package app.sixdegree.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import app.sixdegree.model.login.Data;
import app.sixdegree.view.activity.Splash;

public class AppSession {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "app.sixdegree.session";


    public AppSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void saveUserData(Data data) {
        String da = new Gson().toJson(data);
        editor.putBoolean("loggedin", true);
        editor.putString("data", da);
        editor.commit();


    }

    public boolean isLoggedIn() {
        return pref.getBoolean("loggedin", false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(_context, Splash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(intent);
    }

    public Data getData() {

        String da = pref.getString("data", "");
        if (da.isEmpty()) {
            return null;
        } else {
            return new Gson().fromJson(da, Data.class);
        }

    }  public String  getToken() {

        String da = pref.getString("data", "");
        if (da.isEmpty()) {
            return "";
        } else {
            return "Bearer "+new Gson().fromJson(da, Data.class).getToken();
        }

    }

}
