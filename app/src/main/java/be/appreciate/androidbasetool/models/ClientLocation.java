package be.appreciate.androidbasetool.models;

import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.database.ClientTable;
import be.appreciate.androidbasetool.database.LocationTable;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class ClientLocation implements Serializable
{
    private int id;
    private String name;
    private String street;
    private String zipcode;
    private String city;
    private String clientName;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getStreet()
    {
        return street;
    }

    public String getZipcode()
    {
        return zipcode;
    }

    public String getCity()
    {
        return city;
    }

    public String getClientName()
    {
        return clientName;
    }

    public static ClientLocation constructFromCursor(Cursor cursor)
    {
        ClientLocation location = new ClientLocation();

        location.id = cursor.getInt(cursor.getColumnIndex(LocationTable.COLUMN_LOCATION_ID_ALIAS));
        location.name = cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_NAME_ALIAS));
        location.street = cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_STREET_ALIAS));
        location.zipcode = cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_ZIP_CODE_ALIAS));
        location.city = cursor.getString(cursor.getColumnIndex(LocationTable.COLUMN_CITY_ALIAS));
        location.clientName = cursor.getString(cursor.getColumnIndex(ClientTable.COLUMN_NAME_ALIAS));

        return location;
    }

    public static List<ClientLocation> constructListFromCursor(Cursor cursor)
    {
        List<ClientLocation> locations = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                locations.add(ClientLocation.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return locations;
    }
}
