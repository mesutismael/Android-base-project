package be.appreciate.androidbasetool.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thijscoorevits on 4/10/16.
 */

public class PreferencesHelper
{
    private static final String PREFERENCE_NAME = "Imc";

    private static final String PREFERENCE_TECHNICIAN_USERNAME = "technician_username";
    private static final String PREFERENCE_TECHNICIAN_PASSWORD = "technician_pasword";

    private static SharedPreferences getPreferences(Context context)
    {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static String getTechnicianUsername(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_TECHNICIAN_USERNAME, null);
    }

    public static void saveTechnicianUsername(Context context, String technician)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_TECHNICIAN_USERNAME, technician);
        prefs.apply();
    }

    public static String getTechnicianPassword(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_TECHNICIAN_PASSWORD, null);
    }

    public static void saveTechnicianPassword(Context context, String password)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_TECHNICIAN_PASSWORD, password);
        prefs.apply();
    }

    public static void clearUser(Context context)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();

        prefs.remove(PREFERENCE_TECHNICIAN_USERNAME);
        prefs.remove(PREFERENCE_TECHNICIAN_PASSWORD);

        prefs.apply();
    }
}
