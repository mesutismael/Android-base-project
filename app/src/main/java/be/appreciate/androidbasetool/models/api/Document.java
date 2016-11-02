package be.appreciate.androidbasetool.models.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import be.appreciate.androidbasetool.database.DocumentTable;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Document
{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("file")
    private String file;


    public ContentValues getContentValues(int locationId)
    {
        ContentValues cv = new ContentValues();

        cv.put(DocumentTable.COLUMN_DOCUMENT_ID, this.id);
        cv.put(DocumentTable.COLUMN_DOCUMENT_NAME, this.name);
        cv.put(DocumentTable.COLUMN_DOCUMENT_FILE, this.file);
        cv.put(DocumentTable.COLUMN_LOCATION_ID, locationId);

        return cv;
    }


}