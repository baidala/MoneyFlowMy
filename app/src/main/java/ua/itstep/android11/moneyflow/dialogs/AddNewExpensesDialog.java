package ua.itstep.android11.moneyflow.dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Test on 16.05.2016.
 */
public class AddNewExpensesDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText etSumma;
    AutoCompleteTextView acNameOfExpense;
    Spinner spinner;
    SimpleCursorAdapter adapter;
    long categoryId = 0;
    private  static  final int EXPENSES_LOADER_ID = 5;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_expense, null, true);
        etSumma = (EditText) view.findViewById(R.id.etSumma);
        acNameOfExpense = (AutoCompleteTextView) view.findViewById(R.id.acNameOfExpense);
        //TODO set adapter for AutocompliteTextView

        String[] from = new String[] {Prefs.FIELD_CATEGORY};
        int[] to = new int[] {android.R.id.text1};

        adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                R.layout.spinner_item,
                null, from, to,
                CursorAdapter.NO_SELECTION);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);



        spinner = (Spinner)view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        getActivity().getSupportLoaderManager().initLoader(EXPENSES_LOADER_ID, null, this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int _position, long id) {
                categoryId = spinner.getItemIdAtPosition(_position);
                //position = _position;

                Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog onItemSelected position: " + _position);
                Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog onItemSelected categoryId: " + categoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setView(view)
                //.setMessage(R.string.message_add_new_expense_dialog)
                .setTitle(R.string.title_add_new_expense_dialog)
                .setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewExpense();
                    }
                })
                .setNegativeButton(R.string.negativ_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }



    private void addNewExpense() {
        double summa = 0;
        Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog addNewExpense: " + etSumma.getText().toString());

        ContentResolver cr = getContext().getContentResolver();
        ContentValues cvExpense = new ContentValues();

        try {
            summa = Double.parseDouble(etSumma.getText().toString());
        } catch (NumberFormatException ex){}

        String name = acNameOfExpense.getText().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String catgId = Long.toString(categoryId);


        cvExpense.put(Prefs.FIELD_SUMMA, summa);
        cvExpense.put(Prefs.FIELD_DESC, name);
        cvExpense.put(Prefs.FIELD_DATE, date);
        cvExpense.put(Prefs.FIELD_CATG_ID, catgId);

        Uri expenseId = cr.insert(Prefs.URI_EXPENSES, cvExpense);


        if (expenseId == null) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog addNewExpense expenseId  = NULL");

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog onCreateLoader ");

        if (id == EXPENSES_LOADER_ID) {

            return new AddNewExpensesDialog.ExpensesDialogCursorLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog onLoadFinished " +cursor.getCount() );

        switch (loader.getId()) {
            case EXPENSES_LOADER_ID:
                adapter.swapCursor(cursor);

                //for ( position = 0; position < adapter.getCount() && adapter.getItemId(position) != categoryId; position++){}
                if(Prefs.DEBUG) {
                    //Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onLoadFinished position: " + position);
                    Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog onLoadFinished  LOADER_ID");
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog onLoaderReset");

        adapter.swapCursor(null);
    }



    private static class ExpensesDialogCursorLoader extends CursorLoader {


        ExpensesDialogCursorLoader( Context context ) {

            super(context, Prefs.URI_CATEGORY,
                    new String[] {Prefs.FIELD_ID, Prefs.FIELD_CATEGORY},
                    null, null, null);

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog ExpensesDialogCursorLoader() ");


        }

        @Override
        public Cursor loadInBackground() {
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog ExpensesCursorLoader loadInBackground");

            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_CATEGORY, new String[]{Prefs.FIELD_ID, Prefs.FIELD_CATEGORY}, null, null, null);

            //if(Prefs.DEBUG) logCursor(cursor);
            if (cursor != null) {
                if (Prefs.DEBUG)
                    Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog ExpensesDialogCursorLoader loadInBackground count - " + cursor.getCount());
            } else {
                Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog  loadInBackground - Cursor is NULL");
            }

            return cursor;
        }




    }


    // вывод в лог данных из курсора
    private void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(Prefs.LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else {
            Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog  logCursor - Cursor is null");
        }
    }


}
