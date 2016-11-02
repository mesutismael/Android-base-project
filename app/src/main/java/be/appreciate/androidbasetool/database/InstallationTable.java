package be.appreciate.androidbasetool.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class InstallationTable
{
    public static final String TABLE_NAME = "installation";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INSTALLATION_ID = "installation_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION_ID = "location_id";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "." + COLUMN_ID;
    public static final String COLUMN_INSTALLATION_ID_FULL = TABLE_NAME + "." + COLUMN_INSTALLATION_ID;
    public static final String COLUMN_NAME_FULL = TABLE_NAME + "." + COLUMN_NAME;
    public static final String COLUMN_LOCATION_ID_FULL = TABLE_NAME + "." + COLUMN_LOCATION_ID;

    public static final String COLUMN_ID_ALIAS = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_INSTALLATION_ID_ALIAS = TABLE_NAME + "_" + COLUMN_INSTALLATION_ID;
    public static final String COLUMN_NAME_ALIAS = TABLE_NAME + "_" + COLUMN_NAME;
    public static final String COLUMN_LOCATION_ID_ALLIAS = TABLE_NAME + "_" + COLUMN_LOCATION_ID;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(COLUMN_ID_FULL, COLUMN_ID_FULL + " AS " + COLUMN_ID_ALIAS);
        PROJECTION_MAP.put(COLUMN_INSTALLATION_ID_FULL, COLUMN_INSTALLATION_ID_FULL + " AS " + COLUMN_INSTALLATION_ID_ALIAS);
        PROJECTION_MAP.put(COLUMN_NAME_FULL, COLUMN_NAME_FULL + " AS " + COLUMN_NAME_ALIAS);
        PROJECTION_MAP.put(COLUMN_LOCATION_ID_FULL, COLUMN_LOCATION_ID_FULL + " AS " + COLUMN_LOCATION_ID_ALLIAS);
    }

    private static final String CREATE_TABLE =  "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_INSTALLATION_ID + " integer, "
            + COLUMN_NAME + " text, "
            + COLUMN_LOCATION_ID + " integer"
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
