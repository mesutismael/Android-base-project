package be.appreciate.androidbasetool.models.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.database.ClientTable;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Client
{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("street")
    private String street;
    @SerializedName("zipcode")
    private String zipcode;
    @SerializedName("city")
    private String city;
    @SerializedName("location")
    private List<Location> locations;

    public ContentValues getContentValues()
    {
        ContentValues cv = new ContentValues();

        cv.put(ClientTable.COLUMN_CLIENT_ID, this.id);
        cv.put(ClientTable.COLUMN_NAME, this.name);
        cv.put(ClientTable.COLUMN_STREET, this.street);
        cv.put(ClientTable.COLUMN_ZIP_CODE, this.zipcode);
        cv.put(ClientTable.COLUMN_CITY, this.city);

        return cv;
    }

    public ContentValues[] getLocationContentValues()
    {
        List<ContentValues> cvList = new ArrayList<>();

        if(this.locations != null)
        {
            for(Location location : this.locations)
            {
                cvList.add(location.getContentValues(this.id));
            }
        }
        return cvList.toArray(new ContentValues[cvList.size()]);
    }

    public ContentValues[] getInstallationContentValues()
    {
        List<ContentValues> cvList = new ArrayList<>();
        if(this.locations != null)
        {
            for(Location location : this.locations)
            {
                cvList.addAll(location.getInstallationContentValues());
            }
        }
        return cvList.toArray(new ContentValues[cvList.size()]);
    }

    public ContentValues[] getTodoContentValues()
    {
        List<ContentValues> cvList = new ArrayList<>();
        if(this.locations != null)
        {
            for(Location location : this.locations)
            {
                cvList.addAll(location.getTodoContentValues());
            }
        }
        return cvList.toArray(new ContentValues[cvList.size()]);
    }

    public ContentValues[] getDocumentContentValues()
    {
        List<ContentValues> cvList = new ArrayList<>();
        if(this.locations != null)
        {
            for(Location location : this.locations)
            {
                cvList.addAll(location.getDocumentContentValues());
            }
        }
        return cvList.toArray(new ContentValues[cvList.size()]);
    }
}
