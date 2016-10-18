package ua.itstep.android11.moneyflow.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import ua.itstep.android11.moneyflow.db.DBHelper;
import ua.itstep.android11.moneyflow.utils.Prefs;

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int URI_BALANCE_CODE = 0;
    public static final int URI_EXPENSES_CODE = 1;
    public static final int URI_INCOMES_CODE = 2;
    public static final int URI_DESCRIPTION_CODE = 3;

    static {
        uriMatcher.addURI(Prefs.URI_BALANCE_AUTHORITIES,
                Prefs.URI_BALANCE_TYPE,
                URI_BALANCE_CODE);
        uriMatcher.addURI(Prefs.URI_EXPENSES_AUTHORITIES,
                Prefs.URI_EXPENSES_TYPE,
                URI_EXPENSES_CODE);
        uriMatcher.addURI(Prefs.URI_INCOMES_AUTHORITIES,
                Prefs.URI_INCOMES_TYPE,
                URI_INCOMES_CODE);
        uriMatcher.addURI(Prefs.URI_DESCRIPTION_AUTHORITIES,
                Prefs.URI_DESCRIPTION_TYPE,
                URI_DESCRIPTION_CODE);
    }

    public MyContentProvider() {
        Log.d(Prefs.LOG_TAG, "MyContentProvider constructor");
    }

    @Override
    public boolean onCreate() {
        Log.d(Prefs.LOG_TAG, "MyContentProvider onCreate");
        dbHelper = new DBHelper(getContext(), Prefs.DB_CURRENT_VERSION);
        database = dbHelper.getWritableDatabase();
        if ( database == null ) {
            Log.d(Prefs.LOG_TAG, "MyContentProvider onCreate  database == null");
        }
        return (database == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) throws SQLException {
        Log.d(Prefs.LOG_TAG, "MyContentProvider insert");

        long id;
        Uri insertUri = null;

        database = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_BALANCE_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_BALANCE_CODE");
                id = database.insert(Prefs.TABLE_BALANCE, null, values);
                if ( id > 0 ) {
                    insertUri = ContentUris.withAppendedId(Prefs.URI_BALANCE, id);

                    if(Prefs.DEBUG) {
                        Log.d(Prefs.LOG_TAG, "MyContentProvider balance insertUri = " + uri);
                        if (insertUri.equals(Prefs.URI_BALANCE))
                            Log.d(Prefs.LOG_TAG, "MyContentProvider balance insertUri equals Prefs.URI_BALANCE- !!!!!!");
                    }

                    ContentResolver cr =getContext().getContentResolver();
                    Log.d(Prefs.LOG_TAG, "MyContentProvider balance insertUri ContentResolver=" + cr);

                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to insert row into "+ uri);
                    throw new SQLException("Failed to insert row into "+ uri);
                }
                break;

            case URI_EXPENSES_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_EXPENSES_CODE");
                id = database.insert(Prefs.TABLE_EXPENSES, null, values);
                if ( id > 0 ) {
                    insertUri = ContentUris.withAppendedId(Prefs.URI_EXPENSES, id);

                    if(Prefs.DEBUG) {
                        Log.d(Prefs.LOG_TAG, "MyContentProvider insertUri = " + uri);
                        if (insertUri.equals(Prefs.URI_EXPENSES))
                            Log.d(Prefs.LOG_TAG, "MyContentProvider insertUri equals Prefs.URI_EXPENSES");
                    }

                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to insert row into "+ uri);
                    throw new SQLException("Failed to insert row into "+ uri);
                }
                break;

            case URI_INCOMES_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_INCOMES_CODE");

                id = database.insert(Prefs.TABLE_INCOMES, null, values);
                if ( id > 0 ) {
                    insertUri = ContentUris.withAppendedId(Prefs.URI_INCOMES, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                    Log.d(Prefs.LOG_TAG, "MyContentProvider insertUri = " + uri);
                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to insert row into "+ uri);
                    throw new SQLException("Failed to insert row into "+ uri);
                }
                break;

            case URI_DESCRIPTION_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_DESCRIPTION_CODE");
                id = database.insert(Prefs.TABLE_DESCRIPTION, null, values);
                if ( id > 0 ) {
                    insertUri = ContentUris.withAppendedId(Prefs.URI_DESCRIPTION, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to insert row into "+ uri);
                    throw new SQLException("Failed to insert row into "+ uri);
                }
                break;
        }
        return insertUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) throws SQLiteException  {
        Log.d(Prefs.LOG_TAG, "MyContentProvider query");

        try {
            database = dbHelper.getWritableDatabase();
            Log.d(Prefs.LOG_TAG, "MyContentProvider query getWritableDatabase");
        } catch (SQLiteException ex) {
            database = dbHelper.getReadableDatabase();
            Log.d(Prefs.LOG_TAG, "MyContentProvider query getReadableDatabase");
        }

        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
            case URI_BALANCE_CODE:
                cursor = database.query(Prefs.TABLE_BALANCE, projection,
                        selection, selectionArgs, null, null, sortOrder);
                Log.d(Prefs.LOG_TAG, "MyContentProvider query URI_BALANCE_CODE count - "+cursor.getCount());
                break;

            case URI_EXPENSES_CODE:
                cursor = database.query(Prefs.TABLE_EXPENSES_JOINED, projection,
                        selection, selectionArgs, null, null, sortOrder);
                Log.d(Prefs.LOG_TAG, "MyContentProvider query URI_EXPENSES_CODE");
                break;

            case URI_INCOMES_CODE:
                cursor = database.query(Prefs.TABLE_INCOMES_JOINED, projection,
                        selection, selectionArgs, null, null, sortOrder);
                Log.d(Prefs.LOG_TAG, "MyContentProvider query URI_INCOMES_CODE");
                break;
            case URI_DESCRIPTION_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider query URI_DESCRIPTION_CODE -1");
                cursor = database.query(Prefs.TABLE_DESCRIPTION, projection,
                        selection, selectionArgs, null, null, sortOrder);
                Log.d(Prefs.LOG_TAG, "MyContentProvider query URI_DESCRIPTION_CODE -2");
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        Log.d(Prefs.LOG_TAG, "MyContentProvider update");

        //long id = 0;
        int updated = 0;

        database = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_BALANCE_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider update URI_BALANCE_CODE");

                updated = database.update(Prefs.TABLE_BALANCE, values, null, null);

                if ( updated > 0 ) {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider update balance uri:"+ uri);

                    if (uri.equals(Prefs.URI_BALANCE))
                        Log.d(Prefs.LOG_TAG, "MyContentProvider update equals Prefs.URI_BALANCE");

                    ContentResolver cr =getContext().getContentResolver();
                    Log.d(Prefs.LOG_TAG, "MyContentProvider update ContentResolver=" + cr);

                    getContext().getContentResolver().notifyChange(uri, null);

                    Log.d(Prefs.LOG_TAG, "MyContentProvider update notifyChange updated = "+ updated);

                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to update row in "+ uri);
                    throw new SQLException("Failed to update row in "+ uri);
                }
                break;

            case URI_EXPENSES_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider update URI_EXPENSES_CODE");

                break;

            case URI_INCOMES_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider update URI_INCOMES_CODE");

                break;

            case URI_DESCRIPTION_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider update URI_DESCRIPTION_CODE");

                break;
        }
        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to delete one or more rows.
        Log.d(Prefs.LOG_TAG, "MyContentProvider delete");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        Log.d(Prefs.LOG_TAG, "MyContentProvider getType");
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
