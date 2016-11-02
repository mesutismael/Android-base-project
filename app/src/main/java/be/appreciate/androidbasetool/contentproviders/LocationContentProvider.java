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

import java.util.HashMap;
import java.util.Map;

import be.appreciate.androidbasetool.database.ClientTable;
import be.appreciate.androidbasetool.database.DatabaseHelper;
import be.appreciate.androidbasetool.database.LocationTable;

/**
 * Created by thijscoorevits on 6/10/16.
 */

public class LocationContentProvider extends ContentProvider
{
    private DatabaseHelper databaseHelper;

    private static final String PROVIDER_NAME = "be.appreciate.imc.contentproviders.LocationContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");
    public static final Uri CONTENT_URI_CLIENT = Uri.parse("content://" + PROVIDER_NAME + "/client/");
    private static final int LOCATIONS = 1;
    private static final int LOCATIONS_CLIENT = 2;
    private static final UriMatcher URI_MATCHER;

    static
    {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PROVIDER_NAME, null, LOCATIONS);
        URI_MATCHER.addURI(PROVIDER_NAME, "client", LOCATIONS_CLIENT);
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
            case LOCATIONS:
            case LOCATIONS_CLIENT:
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
            case LOCATIONS:
                db = this.databaseHelper.getReadableDatabase();
                queryBuilder = new SQLiteQueryBuilder();

                queryBuilder.setTables(LocationTable.TABLE_NAME);
                queryBuilder.setProjectionMap(LocationTable.PROJECTION_MAP);

                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(this.getContext().getContentResolver(), CONTENT_URI);
                return cursor;

            case LOCATIONS_CLIENT:
                db = this.databaseHelper.getReadableDatabase();
                queryBuilder = new SQLiteQueryBuilder();

                queryBuilder.setTables(LocationTable.TABLE_NAME + " left join " + ClientTable.TABLE_NAME
                        + " on " + LocationTable.COLUMN_CLIENT_ID_FULL + " = " + ClientTable.COLUMN_CLIENT_ID_FULL);

                Map<String, String> projectionMap = new HashMap<>();
                projectionMap.putAll(LocationTable.PROJECTION_MAP);
                projectionMap.putAll(ClientTable.PROJECTION_MAP);

                queryBuilder.setProjectionMap(projectionMap);

                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(this.getContext().getContentResolver(), CONTENT_URI_CLIENT);
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
            case LOCATIONS:
                SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
                long rowID = db.replace(LocationTable.TABLE_NAME, null, values);

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
            case LOCATIONS:
                SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
                int rowsDeleted = db.delete(LocationTable.TABLE_NAME, selection, selectionArgs);

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
            case LOCATIONS:
                SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
                int rowsUpdated = db.update(LocationTable.TABLE_NAME, values, selection, selectionArgs);

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
            case LOCATIONS:
                int rowsInserted = 0;
                SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
                DatabaseUtils.InsertHelper inserter = new DatabaseUtils.InsertHelper(db, LocationTable.TABLE_NAME);

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
                                inserter.bind(inserter.getColumnIndex(LocationTable.COLUMN_LOCATION_ID), cv.getAsInteger(LocationTable.COLUMN_LOCATION_ID));
                                inserter.bind(inserter.getColumnIndex(LocationTable.COLUMN_NAME), cv.getAsString(LocationTable.COLUMN_NAME));
                                inserter.bind(inserter.getColumnIndex(LocationTable.COLUMN_STREET), cv.getAsString(LocationTable.COLUMN_STREET));
                                inserter.bind(inserter.getColumnIndex(LocationTable.COLUMN_ZIP_CODE), cv.getAsString(LocationTable.COLUMN_ZIP_CODE));
                                inserter.bind(inserter.getColumnIndex(LocationTable.COLUMN_CITY), cv.getAsString(LocationTable.COLUMN_CITY));
                                inserter.bind(inserter.getColumnIndex(LocationTable.COLUMN_CLIENT_ID), cv.getAsInteger(LocationTable.COLUMN_CLIENT_ID));

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
