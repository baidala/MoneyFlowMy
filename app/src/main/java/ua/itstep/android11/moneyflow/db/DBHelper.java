package ua.itstep.android11.moneyflow.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Test on 16.05.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    /*
    Table expencies
    - _id
    - id_passive - id from table passive
    - volume - volume of money
    - date - date when expency made
     */

    private static final String CREATE_TABLE_EXPENCIES = String.format("create table %s (%s integer primary key autoincrement, %s integer, %s integer, %s text);",
            Prefs.TABLE_NAME_EXPENCIES, Prefs.FIELD_NAME_ID, Prefs.FIELD_NAME_ID_PASSIVE, Prefs.FIELD_NAME_VOLUME, Prefs.FIELD_NAME_DATE);

    public DBHelper(Context context, int version) {
        super(context, Prefs.DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENCIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
