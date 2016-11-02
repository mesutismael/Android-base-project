package be.appreciate.androidbasetool.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thijscoorevits on 4/10/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "imc.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        ClientTable.onCreate(db);
        LocationTable.onCreate(db);
        InstallationTable.onCreate(db);
        TodoTable.onCreate(db);
        DocumentTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        ClientTable.onUpgrade(db, oldVersion, newVersion);
        LocationTable.onUpgrade(db, oldVersion, newVersion);
        InstallationTable.onUpgrade(db, oldVersion, newVersion);
        TodoTable.onUpgrade(db, oldVersion, newVersion);
        DocumentTable.onUpgrade(db, oldVersion, newVersion);
    }
}
