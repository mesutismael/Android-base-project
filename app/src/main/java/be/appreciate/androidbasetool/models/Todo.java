package be.appreciate.androidbasetool.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.database.TodoTable;


/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Todo
{
    private int id;
    private String todo;
    private boolean status;

    public static Todo constructFromCursor(Cursor cursor)
    {
        Todo todo = new Todo();

        todo.id = cursor.getInt(cursor.getColumnIndex(TodoTable.COLUMN_TODO_ID_ALIAS));
        todo.todo = cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_TODO_ALIAS));
        todo.status = cursor.getInt(cursor.getColumnIndex(TodoTable.COLUMN_STATUS_ALIAS)) != 0;

        return todo;
    }

    public static List<Todo> constructListFromCursor(Cursor cursor)
    {
        List<Todo> todos = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                todos.add(Todo.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return todos;
    }

    public int getId()
    {
        return id;
    }

    public String getTodo()
    {
        return todo;
    }

    public boolean isStatus()
    {
        return status;
    }
}
