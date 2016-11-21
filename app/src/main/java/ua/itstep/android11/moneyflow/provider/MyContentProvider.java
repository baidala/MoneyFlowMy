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
    public static final int URI_CATEGORY_CODE = 4;

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
        uriMatcher.addURI(Prefs.URI_CATEGORY_AUTHORITIES,
                Prefs.URI_CATEGORY_TYPE,
                URI_CATEGORY_CODE);
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
        ContentValues cvDescription;
        ContentValues cvBalance;

        database = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_BALANCE_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_BALANCE_CODE");
                id = database.insert(Prefs.TABLE_BALANCE, null, values);
                if ( id != 0 ) {
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

                cvDescription = new ContentValues();
                cvDescription.put( Prefs.FIELD_DESC, values.getAsString(Prefs.FIELD_DESC) );

                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_EXPENSES_CODE FIELD_DESC="+ cvDescription.getAsString(Prefs.FIELD_DESC));

                id = database.insert(Prefs.TABLE_DESCRIPTION, null, cvDescription);
                if ( id != 0 ) {
                    getContext().getContentResolver().notifyChange(Prefs.URI_DESCRIPTION, null);
                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to insert row into "+ Prefs.URI_DESCRIPTION);
                    throw new SQLException("Failed to insert row into "+ Prefs.URI_DESCRIPTION);
                }

                ContentValues cvExpense = new ContentValues();
                cvExpense.put( Prefs.FIELD_SUMMA, values.getAsDouble(Prefs.FIELD_SUMMA) );
                cvExpense.put( Prefs.FIELD_DESC_ID, id );
                cvExpense.put( Prefs.FIELD_DATE, values.getAsString(Prefs.FIELD_DATE) );
                cvExpense.put( Prefs.FIELD_CATG_ID, values.getAsLong(Prefs.FIELD_CATG_ID) );
                id = database.insert(Prefs.TABLE_EXPENSES, null, cvExpense);
                if ( id != 0 ) {
                    insertUri = ContentUris.withAppendedId(Prefs.URI_EXPENSES, id);

                    if(Prefs.DEBUG) {
                        Log.d(Prefs.LOG_TAG, "MyContentProvider insertUri = " + insertUri);
                        if (insertUri.equals(Prefs.URI_EXPENSES))
                            Log.d(Prefs.LOG_TAG, "MyContentProvider insertUri equals Prefs.URI_EXPENSES!!!!!! Fail");
                    }

                    getContext().getContentResolver().notifyChange(Prefs.URI_EXPENSES, null);
                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to insert row into "+ uri);
                    throw new SQLException("Failed to insert row into "+ uri);
                }

                cvBalance = new ContentValues();
                cvBalance.put( Prefs.FIELD_SUMMA_EXPENSES, values.getAsString(Prefs.FIELD_SUMMA) );
                updateBalance( cvBalance );
                break;

            case URI_INCOMES_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_INCOMES_CODE");

                cvDescription = new ContentValues();
                cvDescription.put( Prefs.FIELD_DESC, values.getAsString(Prefs.FIELD_DESC) );

                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_INCOMES_CODE FIELD_DESC="+ cvDescription.getAsString(Prefs.FIELD_DESC));

                id = database.insert(Prefs.TABLE_DESCRIPTION, null, cvDescription);
                if ( id != 0 ) {
                    getContext().getContentResolver().notifyChange(Prefs.URI_DESCRIPTION, null);
                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to insert row into "+ Prefs.URI_DESCRIPTION);
                    throw new SQLException("Failed to insert row into "+ Prefs.URI_DESCRIPTION);
                }

                ContentValues cvIncomes = new ContentValues();
                cvIncomes.put( Prefs.FIELD_SUMMA, values.getAsDouble(Prefs.FIELD_SUMMA) );
                cvIncomes.put( Prefs.FIELD_DESC_ID, id );
                cvIncomes.put( Prefs.FIELD_DATE, values.getAsString(Prefs.FIELD_DATE) );
                id = database.insert(Prefs.TABLE_INCOMES, null, cvIncomes);
                if ( id != 0 ) {
                    insertUri = ContentUris.withAppendedId(Prefs.URI_INCOMES, id);
                    getContext().getContentResolver().notifyChange(Prefs.URI_INCOMES, null);
                    Log.d(Prefs.LOG_TAG, "MyContentProvider insertUri = " + uri);
                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to insert row into "+ uri);
                    throw new SQLException("Failed to insert row into "+ uri);
                }

                cvBalance = new ContentValues();
                cvBalance.put( Prefs.FIELD_SUMMA_INCOMES, values.getAsString(Prefs.FIELD_SUMMA) );
                updateBalance( cvBalance );
                break;

            case URI_DESCRIPTION_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider URI_DESCRIPTION_CODE");
                id = database.insert(Prefs.TABLE_DESCRIPTION, null, values);
                if ( id != 0 ) {
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

            case URI_CATEGORY_CODE:
                cursor = database.query(Prefs.TABLE_CATEGORY, projection,
                        selection, selectionArgs, null, null, sortOrder);
                Log.d(Prefs.LOG_TAG, "MyContentProvider query URI_CATEGORY_CODE count - "+cursor.getCount());
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where,
                      String[] whereArgs) {

        Log.d(Prefs.LOG_TAG, "MyContentProvider update");

        //long id = 0;
        int updated = 0;

        database = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_BALANCE_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider update URI_BALANCE_CODE");

                updated = database.update(Prefs.TABLE_BALANCE, values, where, whereArgs);

                if ( updated != 0 ) {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider update balance uri:"+ uri);

                    if (uri.equals(Prefs.URI_BALANCE))
                        Log.d(Prefs.LOG_TAG, "MyContentProvider update equals Prefs.URI_BALANCE");

                    ContentResolver cr = getContext().getContentResolver();
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

                ContentValues cvExpense = new ContentValues();
                cvExpense.put( Prefs.FIELD_SUMMA, values.getAsDouble(Prefs.FIELD_SUMMA) );
                cvExpense.put( Prefs.FIELD_CATG_ID, values.getAsLong(Prefs.FIELD_CATG_ID) );

                updated = database.update(Prefs.TABLE_EXPENSES, cvExpense, where, whereArgs);

                if ( updated != 0 ) {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider update notifyChange updated = "+ updated);
                    getContext().getContentResolver().notifyChange(uri, null);

                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to update row in "+ uri);
                    throw new SQLException("Failed to update row in "+ uri);
                }

                ContentValues cvBalance = new ContentValues();
                cvBalance.put( Prefs.FIELD_SUMMA_EXPENSES, values.getAsDouble(Prefs.FIELD_SUMMA_EXPENSES) );
                updateBalance( cvBalance );

                cvBalance.clear();
                cvBalance.put( Prefs.FIELD_SUMMA_EXPENSES, values.getAsDouble(Prefs.FIELD_SUMMA) );
                updateBalance( cvBalance );

                ContentValues cvDescription = new ContentValues();
                cvDescription.put( Prefs.FIELD_DESC, values.getAsString(Prefs.FIELD_DESC) );
                updateDescription( cvDescription, where, whereArgs );

                break;

            case URI_INCOMES_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider update URI_INCOMES_CODE");

                break;

            case URI_DESCRIPTION_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider update URI_DESCRIPTION_CODE");

                updated = database.update(Prefs.TABLE_DESCRIPTION, values, where, whereArgs);

                if ( updated != 0 ) {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider update desc uri:"+ uri);

                    getContext().getContentResolver().notifyChange(uri, null);

                    Log.d(Prefs.LOG_TAG, "MyContentProvider update notifyChange updated = "+ updated);

                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to update row in "+ uri);
                    throw new SQLException("Failed to update row in "+ uri);
                }

                break;

            case URI_CATEGORY_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider update URI_CATEGORY_CODE");

                updated = database.update(Prefs.TABLE_CATEGORY, values, where, whereArgs);

                if ( updated != 0 ) {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider update desc uri:"+ uri);

                    getContext().getContentResolver().notifyChange(uri, null);

                    Log.d(Prefs.LOG_TAG, "MyContentProvider update notifyChange updated = "+ updated);

                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to update row in "+ uri);
                    throw new SQLException("Failed to update row in "+ uri);
                }

                break;
        }
        return updated;
    }




    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        // TODO Implement this to handle requests to delete one or more rows.
        Log.d(Prefs.LOG_TAG, "MyContentProvider delete");

        int deleted = 0;

        database = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_BALANCE_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider delete URI_BALANCE_CODE");


                break;

            case URI_EXPENSES_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider delete URI_EXPENSES_CODE");
                double summa_old = 0;

                Cursor cursor = database.query(Prefs.TABLE_EXPENSES, new String[]{Prefs.FIELD_ID, Prefs.FIELD_SUMMA}, whereClause, whereArgs, null, null, null);
                if ( cursor.moveToFirst() ) {
                    summa_old = cursor.getDouble(cursor.getColumnIndex(Prefs.FIELD_SUMMA));
                }
                cursor.close();


                deleted = database.delete(Prefs.TABLE_EXPENSES, whereClause, whereArgs);

                if ( deleted != 0 ) {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider delete EXPENSES uri:"+ uri);

                    getContext().getContentResolver().notifyChange(uri, null);

                    Log.d(Prefs.LOG_TAG, "MyContentProvider delete notifyChange deleted = "+ deleted);

                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to delete row in "+ uri);
                    throw new SQLException("Failed to delete row in "+ uri);
                }

                summa_old *= -1;

                ContentValues cvBalance = new ContentValues();
                cvBalance.put( Prefs.FIELD_SUMMA_EXPENSES, summa_old );
                updateBalance( cvBalance );

                break;

            case URI_INCOMES_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider delete URI_INCOMES_CODE");
                deleted = database.delete(Prefs.TABLE_INCOMES, whereClause, whereArgs);

                if ( deleted != 0 ) {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider delete INCOMES uri:"+ uri);

                    getContext().getContentResolver().notifyChange(uri, null);

                    Log.d(Prefs.LOG_TAG, "MyContentProvider delete notifyChange deleted = "+ deleted);

                } else {
                    Log.d(Prefs.LOG_TAG, "MyContentProvider Failed to delete row in "+ uri);
                    throw new SQLException("Failed to delete row in "+ uri);
                }
                break;

            case URI_DESCRIPTION_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider delete URI_DESCRIPTION_CODE");


                break;

            case URI_CATEGORY_CODE:
                Log.d(Prefs.LOG_TAG, "MyContentProvider delete URI_CATEGORY_CODE");


                break;
        }
        return deleted;


    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        Log.d(Prefs.LOG_TAG, "MyContentProvider getType");
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void updateDescription(ContentValues cvDescription, String where, String[] whereArgs) {
        Cursor cursor;
        long id;

        cursor = database.query(Prefs.TABLE_EXPENSES, new String[]{Prefs.FIELD_ID, Prefs.FIELD_DESC_ID}, where, whereArgs, null, null, null);
        if ( cursor.moveToFirst() ) {
            id = cursor.getLong(cursor.getColumnIndex(Prefs.FIELD_DESC_ID));

            String _id = Long.toString(id);

            update(Prefs.URI_DESCRIPTION, cvDescription, where, new String[]{_id});
        }
        cursor.close();

    }

    private void updateBalance(ContentValues cvBalance) {
        Cursor cursor;
        double summa;

        if ( cvBalance.containsKey(Prefs.FIELD_SUMMA_EXPENSES) ) {
            summa = cvBalance.getAsDouble(Prefs.FIELD_SUMMA_EXPENSES);
            cursor = database.query(Prefs.TABLE_BALANCE, new String[]{Prefs.FIELD_SUMMA_EXPENSES}, null, null, null, null, null);
            if ( cursor.moveToFirst() ) {

                double summaExpenses = cursor.getDouble(cursor.getColumnIndex(Prefs.FIELD_SUMMA_EXPENSES));
                summa += summaExpenses;

                Log.d(Prefs.LOG_TAG, "MyContentProvider updateBalance update summaExpenses  = "+ summa);

                cvBalance.clear();
                cvBalance.put(Prefs.FIELD_SUMMA_EXPENSES, summa);
                update(Prefs.URI_BALANCE, cvBalance, null, null);

            } else {
                cvBalance.clear();
                cvBalance.put(Prefs.FIELD_SUMMA_EXPENSES, summa);
                cvBalance.put(Prefs.FIELD_SUMMA_INCOMES, 0);
                Log.d(Prefs.LOG_TAG, "MyContentProvider updateBalance insert summaExpenses  = "+ summa);
                insert(Prefs.URI_BALANCE, cvBalance);
            }

        } else {
            summa = cvBalance.getAsDouble(Prefs.FIELD_SUMMA_INCOMES);
            cursor = database.query(Prefs.TABLE_BALANCE, new String[]{Prefs.FIELD_SUMMA_INCOMES}, null, null, null, null, null);
            if ( cursor.moveToFirst() ) {

                double summaIncomes = cursor.getDouble(cursor.getColumnIndex(Prefs.FIELD_SUMMA_INCOMES));
                summa += summaIncomes;

                Log.d(Prefs.LOG_TAG, "MyContentProvider updateBalance update summaIncomes  = "+ summa);

                cvBalance.clear();
                cvBalance.put(Prefs.FIELD_SUMMA_INCOMES, summa);
                update(Prefs.URI_BALANCE, cvBalance, null, null);

            } else {
                cvBalance.clear();
                cvBalance.put(Prefs.FIELD_SUMMA_EXPENSES, 0);
                cvBalance.put(Prefs.FIELD_SUMMA_INCOMES, summa);
                Log.d(Prefs.LOG_TAG, "MyContentProvider updateBalance insert summaIncomes  = "+ summa);
                insert(Prefs.URI_BALANCE, cvBalance);
            }


        }
        cursor.close();
    }



}
