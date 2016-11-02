package be.appreciate.androidbasetool.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.database.DocumentTable;


/**
 * Created by thijscoorevits on 5/10/16.
 */

public class Document
{
    private int id;
    private String name;
    private String file;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file = file;
    }

    public static Document constructFromCursor(Cursor cursor)
    {
        Document document = new Document();

        document.id = cursor.getInt(cursor.getColumnIndex(DocumentTable.COLUMN_DOCUMENT_ID_ALIAS));
        document.name = cursor.getString(cursor.getColumnIndex(DocumentTable.COLUMN_DOCUMENT_NAME_ALIAS));
        document.file = cursor.getString(cursor.getColumnIndex(DocumentTable.COLUMN_DOCUMENT_FILE_ALIAS));

        return document;
    }

    public static List<Document> constructListFromCursor(Cursor cursor)
    {
        List<Document> documents = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                documents.add(Document.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return documents;
    }
}