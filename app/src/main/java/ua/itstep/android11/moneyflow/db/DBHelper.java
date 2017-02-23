package ua.itstep.android11.moneyflow.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Test on 16.05.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    /*
    Table expencies
    - id
    - summa - volume of money
    - date - date when expense made
    - desc_id  - id of description
     */

    //incomes(id, summa, date, desc_id)

    //description(id, description)

    //balance(id, summa)

    private static final String CREATE_TABLE_EXPENSES = String.format("create table %s (%s integer primary key autoincrement, %s real, %s text, %s integer, %s integer);",
            Prefs.TABLE_EXPENSES, Prefs.FIELD_ID, Prefs.FIELD_SUMMA, Prefs.FIELD_DATE, Prefs.FIELD_DESC_ID, Prefs.FIELD_CATG_ID);
    private static final String CREATE_TABLE_INCOMES = String.format("create table %s (%s integer primary key autoincrement, %s real, %s text, %s integer, %s integer);",
            Prefs.TABLE_INCOMES, Prefs.FIELD_ID, Prefs.FIELD_SUMMA, Prefs.FIELD_DATE, Prefs.FIELD_DESC_ID, Prefs.FIELD_CATG_ID);
    private static final String CREATE_TABLE_BALANCE = String.format("create table %s (%s integer primary key autoincrement, %s real, %s real);",
            Prefs.TABLE_BALANCE, Prefs.FIELD_ID, Prefs.FIELD_SUMMA_EXPENSES, Prefs.FIELD_SUMMA_INCOMES);
    private static final String CREATE_TABLE_DESCRIPTION = String.format("create table %s (%s integer primary key autoincrement, %s text);",
            Prefs.TABLE_DESCRIPTION, Prefs.FIELD_ID, Prefs.FIELD_DESC);
    private static final String CREATE_TABLE_CATEGORY = String.format("create table %s (%s integer primary key autoincrement, %s text);",
            Prefs.TABLE_CATEGORY, Prefs.FIELD_ID, Prefs.FIELD_CATEGORY);

    private static final String INSERT_CATEGORY_FOOD = String.format("insert into %s values(1,'default category');", Prefs.TABLE_CATEGORY);
    //private static final String INSERT_CATEGORY_ELECTR = String.format("insert into %s values(2,'electric');", Prefs.TABLE_CATEGORY);
    //private static final String INSERT_CATEGORY_WATER = String.format("insert into %s values(3,'water');", Prefs.TABLE_CATEGORY);


    public DBHelper(Context context, int version) {
        super(context, Prefs.DB_NAME, null, version);
        Log.d(Prefs.LOG_TAG, "DBHelper  construct ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_INCOMES);
        db.execSQL(CREATE_TABLE_BALANCE);
        db.execSQL(CREATE_TABLE_DESCRIPTION);
        db.execSQL(CREATE_TABLE_CATEGORY);

        db.execSQL(INSERT_CATEGORY_FOOD);
        //db.execSQL(INSERT_CATEGORY_ELECTR);
        //db.execSQL(INSERT_CATEGORY_WATER);

        Log.d(Prefs.LOG_TAG, "DBHelper  onCreate ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(Prefs.LOG_TAG, "DBHelper  onUpgrade");

        db.execSQL("drop table " + Prefs.TABLE_EXPENSES);
        db.execSQL("drop table " + Prefs.TABLE_INCOMES);
        db.execSQL("drop table " + Prefs.TABLE_DESCRIPTION);
        db.execSQL("drop table " + Prefs.TABLE_BALANCE);
        db.execSQL("drop table " + Prefs.TABLE_CATEGORY);

        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_INCOMES);
        db.execSQL(CREATE_TABLE_BALANCE);
        db.execSQL(CREATE_TABLE_DESCRIPTION);
        db.execSQL(CREATE_TABLE_CATEGORY);


    }
}
