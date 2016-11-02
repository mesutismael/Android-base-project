package be.appreciate.androidbasetool.models.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import be.appreciate.androidbasetool.database.TodoTable;


/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Todo
{
    @SerializedName("id")
    private int id;
    @SerializedName("todo")
    private String todo;
    @SerializedName("status")
    private int status;

    public ContentValues getContentValues(int installationId)
    {
        ContentValues cv = new ContentValues();

        cv.put(TodoTable.COLUMN_TODO_ID, this.id);
        cv.put(TodoTable.COLUMN_TODO, this.todo);
        cv.put(TodoTable.COLUMN_STATUS, this.status);
        cv.put(TodoTable.COLUMN_INSTALLATION_ID, installationId);

        return cv;
    }


}