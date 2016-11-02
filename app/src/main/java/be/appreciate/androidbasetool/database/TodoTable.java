package be.appreciate.androidbasetool.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class TodoTable
{
    public static final String TABLE_NAME = "todo";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TODO_ID = "todo_id";
    public static final String COLUMN_TODO = "todo";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_INSTALLATION_ID = "installation_id";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "." + COLUMN_ID;
    public static final String COLUMN_TODO_ID_FULL = TABLE_NAME + "." + COLUMN_TODO_ID;
    public static final String COLUMN_TODO_FULL = TABLE_NAME + "." + COLUMN_TODO;
    public static final String COLUMN_STATUS_FULL = TABLE_NAME + "." + COLUMN_STATUS;
    public static final String COLUMN_INSTALLATION_ID_FULL = TABLE_NAME + "." + COLUMN_INSTALLATION_ID;

    public static final String COLUMN_ID_ALIAS = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_TODO_ID_ALIAS = TABLE_NAME + "_" + COLUMN_TODO_ID;
    public static final String COLUMN_TODO_ALIAS = TABLE_NAME + "_" + COLUMN_TODO;
    public static final String COLUMN_STATUS_ALIAS = TABLE_NAME + "_" + COLUMN_STATUS;
    public static final String COLUMN_INSTALLATION_ID_ALLIAS = TABLE_NAME + "_" + COLUMN_INSTALLATION_ID;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(COLUMN_ID_FULL, COLUMN_ID_FULL + " AS " + COLUMN_ID_ALIAS);
        PROJECTION_MAP.put(COLUMN_TODO_ID_FULL, COLUMN_TODO_ID_FULL + " AS " + COLUMN_TODO_ID_ALIAS);
        PROJECTION_MAP.put(COLUMN_TODO_FULL, COLUMN_TODO_FULL + " AS " + COLUMN_TODO_ALIAS);
        PROJECTION_MAP.put(COLUMN_STATUS_FULL, COLUMN_STATUS_FULL + " AS " + COLUMN_STATUS_ALIAS);
        PROJECTION_MAP.put(COLUMN_INSTALLATION_ID_FULL, COLUMN_INSTALLATION_ID_FULL + " AS " + COLUMN_INSTALLATION_ID_ALLIAS);

    }

    private static final String CREATE_TABLE =  "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TODO_ID + " integer, "
            + COLUMN_TODO + " text, "
            + COLUMN_STATUS + " integer, "
            + COLUMN_INSTALLATION_ID + " integer"
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
