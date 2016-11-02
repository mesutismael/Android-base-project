package be.appreciate.androidbasetool.models.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.database.LocationTable;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Location
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
    @SerializedName("installation")
    private List<Installation> installations;
    @SerializedName("document")
    private List<Document> documents;

    public ContentValues getContentValues(int clientId)
    {
        ContentValues cv = new ContentValues();

        cv.put(LocationTable.COLUMN_LOCATION_ID, this.id);
        cv.put(LocationTable.COLUMN_NAME, this.name);
        cv.put(LocationTable.COLUMN_STREET, this.street);
        cv.put(LocationTable.COLUMN_ZIP_CODE, this.zipcode);
        cv.put(LocationTable.COLUMN_CITY, this.city);
        cv.put(LocationTable.COLUMN_CLIENT_ID, clientId);

        return cv;
    }

    public List<ContentValues> getInstallationContentValues()
    {
        List<ContentValues> cvList = new ArrayList<>();

        if(this.installations != null)
        {
            for(Installation installation : this.installations)
            {
                cvList.add(installation.getContentValues(this.id));
            }
        }
        return cvList;
    }

    public List<ContentValues> getTodoContentValues()
    {
        List<ContentValues> cvList = new ArrayList<>();
        if(this.installations != null)
        {
            for(Installation installation : this.installations)
            {
                cvList.addAll(installation.getTodosContentValues());
            }
        }
        return cvList;
    }

    public List<ContentValues> getDocumentContentValues()
    {
        List<ContentValues> cvList = new ArrayList<>();

        if(this.documents != null)
        {
            for(Document document : this.documents)
            {
                cvList.add(document.getContentValues(this.id));
            }
        }
        return cvList;
    }
}
