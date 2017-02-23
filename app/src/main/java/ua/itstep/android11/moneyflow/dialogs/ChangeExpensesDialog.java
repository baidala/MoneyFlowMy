package ua.itstep.android11.moneyflow.dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Test on 16.05.2016.
 */
public class ChangeExpensesDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText etSumma;
    AutoCompleteTextView acNameOfExpense;
    Spinner spinner;
    SimpleCursorAdapter adapter;
    long id = 0;
    long categoryId = 0;
    int position = 0;
    float summa_old = 0f;
    private  static  final int EXPENSES_LOADER_ID = 4;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_expense, null, true);
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

                Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onItemSelected position: " + _position);
                Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onItemSelected categoryId: " + categoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        etSumma.setText(getArguments().getString(Prefs.FIELD_SUMMA_EXPENSES));
        acNameOfExpense.setText(getArguments().getString(Prefs.FIELD_DESC));
        id = getArguments().getLong(Prefs.FIELD_ID);

        try {
            summa_old = Float.parseFloat(etSumma.getText().toString());
        } catch (NumberFormatException ex){}

        summa_old *= -1;


        String _id = Long.toString(id);
        String whereClause = Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_ID + " = CAST (? AS INTEGER)";
        String[] whereArgs = {_id};

        Cursor cursor = getContext().getContentResolver().query(Prefs.URI_EXPENSES, new String[]{Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_ID, Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_CATG_ID }, whereClause, whereArgs, null);

        if ( cursor.moveToFirst() ) {
            categoryId = cursor.getInt(cursor.getColumnIndex(Prefs.FIELD_CATG_ID));
            Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateDialog categoryId: "+ categoryId);

        } else {
            Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateDialog categoryId is NULL !!!");
        }
        cursor.close();





        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateDialog adapter.getCount: "+ adapter.getCount());

        for ( position = 0; position < adapter.getCount() && adapter.getItemId(position) != categoryId; position++) {
            Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateDialog for-position: "+ position);
            Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateDialog adapter.getItemId: "+ adapter.getItemId(position));
        }

        spinner.setSelection(position);

        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateDialog set position: "+ position);


        builder.setView(view)
                .setTitle(R.string.title_change_expense_dialog)
                .setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeExpense();
                    }
                })
                .setNeutralButton(R.string.delete_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteExpense();
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

    private void deleteExpense() {
        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog deleteExpense summa: " + etSumma.getText().toString());
        ContentResolver cr = getContext().getContentResolver();


        String _id = Long.toString(id);
        String whereClause = Prefs.FIELD_ID + " = CAST (? AS INTEGER)";
        String[] whereArgs = {_id};

        int deletedRows = cr.delete(Prefs.URI_EXPENSES, whereClause, whereArgs);
        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog deleteExpense deletedRows: " + deletedRows);

        if (deletedRows == 0) Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog deleteExpense deletedRows == 0 !!!");




    }

    private void changeExpense() {

        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog changeExpense summa: " + etSumma.getText().toString());

        ContentResolver cr = getContext().getContentResolver();
        ContentValues cvExpense = new ContentValues();


        float summa = Float.parseFloat(etSumma.getText().toString());
        String name = acNameOfExpense.getText().toString();
        String catgId = Long.toString(categoryId);

        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog changeExpense category: " + categoryId);
        //String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //TODO

        cvExpense.put(Prefs.FIELD_SUMMA, summa);
        cvExpense.put(Prefs.FIELD_SUMMA_EXPENSES, summa_old);
        cvExpense.put(Prefs.FIELD_DESC, name);
        cvExpense.put(Prefs.FIELD_CATG_ID, catgId);
        //cvExpense.put(Prefs.FIELD_DATE, date);

        String _id = Long.toString(id);
        String whereClause = Prefs.FIELD_ID + " = CAST (? AS INTEGER)";
        String[] whereArgs = {_id};

        int updatedRows = cr.update(Prefs.URI_EXPENSES, cvExpense, whereClause, whereArgs);
        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog changeExpense updatedRows: " + updatedRows);

        if (updatedRows == 0) Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog changeExpense updated == 0 !!!");




    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateLoader ");

        if (id == EXPENSES_LOADER_ID) {

            return new ExpensesDialogCursorLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onLoadFinished " +cursor.getCount() );

        switch (loader.getId()) {
            case EXPENSES_LOADER_ID:
                adapter.swapCursor(cursor);

                //for ( position = 0; position < adapter.getCount() && adapter.getItemId(position) != categoryId; position++){}
                if(Prefs.DEBUG) {
                    //Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onLoadFinished position: " + position);
                    Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onLoadFinished  LOADER_ID");
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onLoaderReset");

        adapter.swapCursor(null);
    }



    private static class ExpensesDialogCursorLoader extends CursorLoader {


        ExpensesDialogCursorLoader( Context context ) {

            super(context, Prefs.URI_CATEGORY,
                    new String[] {Prefs.FIELD_ID, Prefs.FIELD_CATEGORY},
                    null, null, null);

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog ExpensesDialogCursorLoader() ");


        }

        @Override
        public Cursor loadInBackground() {
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog ExpensesCursorLoader loadInBackground");

            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_CATEGORY, new String[]{Prefs.FIELD_ID, Prefs.FIELD_CATEGORY}, null, null, null);

            //if(Prefs.DEBUG) logCursor(cursor);
            if (cursor != null) {
                if (Prefs.DEBUG)
                    Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog ExpensesDialogCursorLoader loadInBackground count - " + cursor.getCount());
            } else {
                Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog  loadInBackground - Cursor is NULL");
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
            Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog  logCursor - Cursor is null");
        }
    }



}
