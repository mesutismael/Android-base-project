package be.appreciate.androidbasetool.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.database.ClientTable;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Client
{
    private int id;
    private String name;
    private String street;
    private String zipcode;
    private String city;

    public static Client constructFromCursor(Cursor cursor)
    {
        Client client = new Client();

        client.id = cursor.getInt(cursor.getColumnIndex(ClientTable.COLUMN_CLIENT_ID_ALIAS));
        client.name = cursor.getString(cursor.getColumnIndex(ClientTable.COLUMN_NAME_ALIAS));
        client.street = cursor.getString(cursor.getColumnIndex(ClientTable.COLUMN_STREET_ALIAS));
        client.zipcode = cursor.getString(cursor.getColumnIndex(ClientTable.COLUMN_ZIP_CODE_ALIAS));
        client.city = cursor.getString(cursor.getColumnIndex(ClientTable.COLUMN_CITY_ALIAS));

        return client;
    }

    public static List<Client> constructListFromCursor(Cursor cursor)
    {
        List<Client> stations = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                stations.add(Client.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return stations;
    }
}
