package be.appreciate.androidbasetool.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.database.InstallationTable;


/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Installation
{
    private int id;
    private String name;

    public static Installation constructFromCursor(Cursor cursor)
    {
        Installation installation = new Installation();

        installation.id = cursor.getInt(cursor.getColumnIndex(InstallationTable.COLUMN_INSTALLATION_ID_ALIAS));
        installation.name = cursor.getString(cursor.getColumnIndex(InstallationTable.COLUMN_NAME_ALIAS));

        return installation;
    }

    public static List<Installation> constructListFromCursor(Cursor cursor)
    {
        List<Installation> installations = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                installations.add(Installation.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return installations;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
