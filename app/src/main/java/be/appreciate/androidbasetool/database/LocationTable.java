package be.appreciate.androidbasetool.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class LocationTable
{
    public static final String TABLE_NAME = "location";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_ZIP_CODE = "zip_code";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_CLIENT_ID = "client_id";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "." + COLUMN_ID;
    public static final String COLUMN_LOCATION_ID_FULL = TABLE_NAME + "." + COLUMN_LOCATION_ID;
    public static final String COLUMN_NAME_FULL = TABLE_NAME + "." + COLUMN_NAME;
    public static final String COLUMN_STREET_FULL = TABLE_NAME + "." + COLUMN_STREET;
    public static final String COLUMN_ZIP_CODE_FULL = TABLE_NAME + "." + COLUMN_ZIP_CODE;
    public static final String COLUMN_CITY_FULL = TABLE_NAME + "." + COLUMN_CITY;
    public static final String COLUMN_CLIENT_ID_FULL = TABLE_NAME + "." + COLUMN_CLIENT_ID;

    public static final String COLUMN_ID_ALIAS = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_LOCATION_ID_ALIAS = TABLE_NAME + "_" + COLUMN_LOCATION_ID;
    public static final String COLUMN_NAME_ALIAS = TABLE_NAME + "_" + COLUMN_NAME;
    public static final String COLUMN_STREET_ALIAS = TABLE_NAME + "_" + COLUMN_STREET;
    public static final String COLUMN_ZIP_CODE_ALIAS = TABLE_NAME + "_" + COLUMN_ZIP_CODE;
    public static final String COLUMN_CITY_ALIAS = TABLE_NAME + "_" + COLUMN_CITY;
    public static final String COLUMN_CLIENT_ID_ALLIAS = TABLE_NAME + "_" + COLUMN_CLIENT_ID;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(COLUMN_ID_FULL, COLUMN_ID_FULL + " AS " + COLUMN_ID_ALIAS);
        PROJECTION_MAP.put(COLUMN_LOCATION_ID_FULL, COLUMN_LOCATION_ID_FULL + " AS " + COLUMN_LOCATION_ID_ALIAS);
        PROJECTION_MAP.put(COLUMN_NAME_FULL, COLUMN_NAME_FULL + " AS " + COLUMN_NAME_ALIAS);
        PROJECTION_MAP.put(COLUMN_STREET_FULL, COLUMN_STREET_FULL + " AS " + COLUMN_STREET_ALIAS);
        PROJECTION_MAP.put(COLUMN_ZIP_CODE_FULL, COLUMN_ZIP_CODE_FULL + " AS " + COLUMN_ZIP_CODE_ALIAS);
        PROJECTION_MAP.put(COLUMN_CITY_FULL, COLUMN_CITY_FULL + " AS " + COLUMN_CITY_ALIAS);
        PROJECTION_MAP.put(COLUMN_CLIENT_ID_FULL, COLUMN_CLIENT_ID_FULL + " AS " + COLUMN_CLIENT_ID_ALLIAS);
    }

    private static final String CREATE_TABLE =  "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LOCATION_ID + " integer, "
            + COLUMN_NAME + " text, "
            + COLUMN_STREET + " text, "
            + COLUMN_ZIP_CODE + " text, "
            + COLUMN_CITY + " text, "
            + COLUMN_CLIENT_ID + " integer"
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        while (oldVersion < newVersion)
        {
            switch (oldVersion)
            {
                default:
                    database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    onCreate(database);
                    break;
            }
            oldVersion++;
        }
    }
}
