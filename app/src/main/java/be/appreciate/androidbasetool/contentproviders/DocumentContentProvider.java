package be.appreciate.androidbasetool.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import be.appreciate.androidbasetool.database.DatabaseHelper;
import be.appreciate.androidbasetool.database.DocumentTable;

/**
 * Created by thijscoorevits on 6/10/16.
 */

public class DocumentContentProvider extends ContentProvider
{
    private DatabaseHelper databaseHelper;

    private static final String PROVIDER_NAME = "be.appreciate.imc.contentproviders.DocumentContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");
    private static final int DOCUMENTS = 1;
    private static final UriMatcher URI_MATCHER;

    static
    {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PROVIDER_NAME, null, DOCUMENTS);
    }

    @Override
    public boolean onCreate()
    {
        this.databaseHelper = new DatabaseHelper(this.getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        switch (URI_MATCHER.match(uri))
        {
            case DOCUMENTS:
                return "vnd.android.cursor.dir/" + PROVIDER_NAME;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db;
        SQLiteQueryBuilder queryBuilder;
        Cursor cursor;

        switch (URI_MATCHER.match(uri))
        {
            case DOCUMENTS:
                db = this.databaseHelper.getReadableDatabase();
                queryBuilder = new SQLiteQueryBuilder();

                queryBuilder.setTables(DocumentTable.TABLE_NAME);
                queryBuilder.setProjectionMap(DocumentTable.PROJECTION_MAP);

                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(this.getContext().getContentResolver(), CONTENT_URI);
                return cursor;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        switch (URI_MATCHER.match(uri))
        {
            case DOCUMENTS:
                SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
                long rowID = db.replace(DocumentTable.TABLE_NAME, null, values);

                if (rowID > 0)
                {
                    this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                    return CONTENT_URI;
                } else
                {
                    throw new SQLException("Failed to add a record into " + uri);
                }

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        switch (URI_MATCHER.match(uri))
        {
            case DOCUMENTS:
                SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
                int rowsDeleted = db.delete(DocumentTable.TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted > 0)
                {
                    this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                }

                return rowsDeleted;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        switch (URI_MATCHER.match(uri))
        {
            case DOCUMENTS:
                SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
                int rowsUpdated = db.update(DocumentTable.TABLE_NAME, values, selection, selectionArgs);

                if (rowsUpdated > 0)
                {
                    this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                }

                return rowsUpdated;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        switch (URI_MATCHER.match(uri))
        {
            case DOCUMENTS:
                int rowsInserted = 0;
                SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
                DatabaseUtils.InsertHelper inserter = new DatabaseUtils.InsertHelper(db, DocumentTable.TABLE_NAME);

                db.beginTransaction();

                try
                {
                    if (values != null)
                    {
                        for (ContentValues cv : values)
                        {
                            if (cv != null)
                            {
                                inserter.prepareForInsert();
                                inserter.bind(inserter.getColumnIndex(DocumentTable.COLUMN_DOCUMENT_ID), cv.getAsInteger(DocumentTable.COLUMN_DOCUMENT_ID));
                                inserter.bind(inserter.getColumnIndex(DocumentTable.COLUMN_DOCUMENT_NAME), cv.getAsString(DocumentTable.COLUMN_DOCUMENT_NAME));
                                inserter.bind(inserter.getColumnIndex(DocumentTable.COLUMN_DOCUMENT_FILE), cv.getAsString(DocumentTable.COLUMN_DOCUMENT_FILE));
                                inserter.bind(inserter.getColumnIndex(DocumentTable.COLUMN_LOCATION_ID), cv.getAsInteger(DocumentTable.COLUMN_LOCATION_ID));

                                long rowId = inserter.execute();

                                if (rowId != -1)
                                {
                                    rowsInserted++;
                                }
                            }
                        }
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e)
                {
                    rowsInserted = 0;
                } finally
                {
                    db.endTransaction();
                    inserter.close();
                }

                if (rowsInserted > 0)
                {
                    this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                }

                return rowsInserted;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
