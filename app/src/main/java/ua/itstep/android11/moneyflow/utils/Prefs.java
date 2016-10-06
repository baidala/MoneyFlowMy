package ua.itstep.android11.moneyflow.utils;

import android.net.Uri;

/**
 * Created by Test on 13.04.2016.
 */
public class Prefs {
    public static final String LOG_TAG = "MoneyFlow >>>>>>>>>>";

    public static final int    DB_CURRENT_VERSION = 1;

    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME = "last_name";
    public static final String FIELD_BIRTHDAY = "birthday";
    public static final String FIELD_EMAIL = "email";


    public static final String API_SERVER = "http://cityfinder.esy.es";

    public static final String DB_NAME = "MoneyFlowDB";

    //incomes(id, summa, date, desc_id)
    public static final String TABLE_INCOMES = "incomes";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_SUMMA = "summa";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_DESC_ID = "desc_id";

    //expenses(id, summa, date, desc_id)
    public static final String TABLE_EXPENSES = "expenses";

    //balance(id, summa)
    public static final String TABLE_BALANCE = "balance";

    //description(id, description)
    public static final String TABLE_DESCRIPTION = "description";
    public static final String FIELD_DESC = "description";




    //The provider constants:
    public static final String URI_EXPENSES_AUTHORITIES = "ua.itstep.android11.moneyflow.provider";
    public static final String URI_EXPENSES_TYPE = "expenses";
    public static final Uri    URI_EXPENSES = Uri.parse("content://" + URI_EXPENSES_AUTHORITIES + "/" + URI_EXPENSES_TYPE);

    public static final String URI_INCOMES_AUTHORITIES = "ua.itstep.android11.moneyflow.provider";
    public static final String URI_INCOMES_TYPE = "incomes";
    public static final Uri    URI_INCOMES = Uri.parse("content://" + URI_INCOMES_AUTHORITIES + "/" + URI_INCOMES_TYPE);

    public static final String URI_DESCRIPTION_AUTHORITIES = "ua.itstep.android11.moneyflow.provider";
    public static final String URI_DESCRIPTION_TYPE = "description";
    public static final Uri    URI_DESCRIPTION = Uri.parse("content://" + URI_DESCRIPTION_AUTHORITIES + "/" + URI_DESCRIPTION_TYPE);

    public static final String CURRENT_MONTH = "current";
    //The Table Expense Names
    //public static final String TABLE_NAME_EXPENSE_NAMES = "expense_names";
    //public static final String EXPENSE_NAMES_FIELD_NAME = "name";
    //public static final String EXPENSE_NAMES_FIELD_CRITICAL = "critical";
    public static final String QUERY = "SELECT expense_names.name, expenses.volume, expense_names.critical, expenses.date FROM expenses INNER JOIN expense_names ON expense_names._id = expenses.id_passive;";
}
