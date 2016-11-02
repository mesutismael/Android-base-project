package be.appreciate.androidbasetool.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class DocumentTable
{
    public static final String TABLE_NAME = "document";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DOCUMENT_ID = "document_id";
    public static final String COLUMN_DOCUMENT_NAME = "document_name";
    public static final String COLUMN_DOCUMENT_FILE = "document_file";
    public static final String COLUMN_LOCATION_ID = "location_id";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "." + COLUMN_ID;
    public static final String COLUMN_DOCUMENT_ID_FULL = TABLE_NAME + "." + COLUMN_DOCUMENT_ID;
    public static final String COLUMN_DOCUMENT_NAME_FULL = TABLE_NAME + "." + COLUMN_DOCUMENT_NAME;
    public static final String COLUMN_DOCUMENT_FILE_FULL = TABLE_NAME + "." + COLUMN_DOCUMENT_FILE;
    public static final String COLUMN_LOCATION_ID_FULL = TABLE_NAME + "." + COLUMN_LOCATION_ID;

    public static final String COLUMN_ID_ALIAS = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_DOCUMENT_ID_ALIAS = TABLE_NAME + "_" + COLUMN_DOCUMENT_ID;
    public static final String COLUMN_DOCUMENT_NAME_ALIAS = TABLE_NAME + "_" + COLUMN_DOCUMENT_NAME;
    public static final String COLUMN_DOCUMENT_FILE_ALIAS = TABLE_NAME + "_" + COLUMN_DOCUMENT_FILE;
    public static final String COLUMN_LOCATION_ID_ALIAS = TABLE_NAME + "_" + COLUMN_LOCATION_ID;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(COLUMN_ID_FULL, COLUMN_ID_FULL + " AS " + COLUMN_ID_ALIAS);
        PROJECTION_MAP.put(COLUMN_DOCUMENT_ID_FULL, COLUMN_DOCUMENT_ID_FULL + " AS " + COLUMN_DOCUMENT_ID_ALIAS);
        PROJECTION_MAP.put(COLUMN_DOCUMENT_NAME_FULL, COLUMN_DOCUMENT_NAME_FULL + " AS " + COLUMN_DOCUMENT_NAME_ALIAS);
        PROJECTION_MAP.put(COLUMN_DOCUMENT_FILE_FULL, COLUMN_DOCUMENT_FILE_FULL + " AS " + COLUMN_DOCUMENT_FILE_ALIAS);
        PROJECTION_MAP.put(COLUMN_LOCATION_ID_FULL, COLUMN_LOCATION_ID_FULL + " AS " + COLUMN_LOCATION_ID_ALIAS);
    }

    private static final String CREATE_TABLE =  "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DOCUMENT_ID + " integer, "
            + COLUMN_DOCUMENT_NAME + " text, "
            + COLUMN_DOCUMENT_FILE + " text, "
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
