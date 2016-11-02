package be.appreciate.androidbasetool.models.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.database.InstallationTable;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Installation
{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("location_id")
    private int locationId;
    @SerializedName("todo")
    private List<Todo> todos;

    public ContentValues getContentValues()
    {
        ContentValues cv = new ContentValues();

        cv.put(InstallationTable.COLUMN_INSTALLATION_ID, this.id);
        cv.put(InstallationTable.COLUMN_NAME, this.name);
        cv.put(InstallationTable.COLUMN_LOCATION_ID, this.locationId);

        return cv;
    }

    public ContentValues getContentValues(int locationId)
    {
        ContentValues cv = new ContentValues();

        cv.put(InstallationTable.COLUMN_INSTALLATION_ID, this.id);
        cv.put(InstallationTable.COLUMN_NAME, this.name);
        cv.put(InstallationTable.COLUMN_LOCATION_ID, locationId);

        return cv;
    }

    public List<ContentValues> getTodosContentValues()
    {
        List<ContentValues> cvList = new ArrayList<>();

        if(this.todos != null)
        {
            for(Todo todo : this.todos)
            {
                cvList.add(todo.getContentValues(this.id));
            }
        }
        return cvList;
    }
}
