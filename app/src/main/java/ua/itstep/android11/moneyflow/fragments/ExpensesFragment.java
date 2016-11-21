package ua.itstep.android11.moneyflow.fragments;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.dialogs.ChangeExpensesDialog;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by oracle on 6/3/16.
 */
public class ExpensesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView lvExpenses;
    SimpleCursorAdapter scAdapter;
    private  static  final int EXPENSES_LOADER_ID = 1;


    private ContentObserver observerExpenses = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ContentObserver expenses onChange " +uri);

            getActivity().getSupportLoaderManager().restartLoader(EXPENSES_LOADER_ID, null, ExpensesFragment.this);

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onResume ");
        getActivity().getContentResolver().registerContentObserver(Prefs.URI_EXPENSES, false, observerExpenses);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onPause ");
        getActivity().getContentResolver().unregisterContentObserver(observerExpenses);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onCreateView ");

        View view =  inflater.inflate( R.layout.fragment_expenses, container , false);

        String[] from = new String[] {Prefs.FIELD_SUMMA, Prefs.FIELD_DESC, Prefs.FIELD_DATE, Prefs.FIELD_CATEGORY};
        int[] to = new int[] {R.id.tvSummaItemExpenses, R.id.tvNameItemExpenses, R.id.tvDateItemExpenses, R.id.tvCatgItemExpenses};

        scAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                R.layout.item_expenses_adapter,
                null, from, to,
                CursorAdapter.NO_SELECTION);


        lvExpenses = (ListView) view.findViewById(R.id.lvExpenses);

        lvExpenses.setAdapter(scAdapter);

        lvExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onCreateView onItemClick:" + id);



                TextView edSumma = (TextView) view.findViewById(R.id.tvSummaItemExpenses);
                String summa = edSumma.getText().toString();
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onCreateView onItemClick summa:" + summa);

                TextView edNameOfExpense = (TextView) view.findViewById(R.id.tvNameItemExpenses);
                String desc = edNameOfExpense.getText().toString();

                ChangeExpensesDialog expenseDialog = new ChangeExpensesDialog();
                Bundle args = new Bundle();
                args.putString(Prefs.FIELD_SUMMA_EXPENSES, summa);
                args.putString(Prefs.FIELD_DESC, desc);
                args.putLong(Prefs.FIELD_ID, id);

                expenseDialog.setArguments(args);
                expenseDialog.show(getFragmentManager(), "CH");
            }
        });

        setHasOptionsMenu(true);

        getActivity().getContentResolver().registerContentObserver(Prefs.URI_EXPENSES, false, observerExpenses);
        getActivity().getSupportLoaderManager().initLoader(EXPENSES_LOADER_ID, null, this);

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onCreateLoader ");

        if (id == EXPENSES_LOADER_ID) {

            return new ExpensesCursorLoader(getActivity());
        }
        return null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onLoadFinished " +cursor.getCount() );

        switch (loader.getId()) {
            case EXPENSES_LOADER_ID:
                scAdapter.swapCursor(cursor);
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onLoadFinishe  LOADER_ID");
                break;
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onLoaderReset");

        scAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);


        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onCreateOptionsMenu");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_refresh:
                getActivity().getSupportLoaderManager().restartLoader(EXPENSES_LOADER_ID, null, this);

                //Toast.makeText(this, "Click on expency", Toast.LENGTH_SHORT).show();
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment onOptionsItemSelected");
                break;
        }
        return true;
    }


    private static class ExpensesCursorLoader extends CursorLoader {


        ExpensesCursorLoader( Context context ) {

            super(context, Prefs.URI_EXPENSES,
                    new String[] {Prefs.FIELD_ID, Prefs.FIELD_SUMMA, Prefs.FIELD_DESC, Prefs.FIELD_DATE, Prefs.FIELD_CATEGORY},
                    null, null, null);

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment ExpensesCursorLoader() ");


        }

        @Override
        public Cursor loadInBackground() {
            //if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment ExpensesCursorLoader loadInBackground");

            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_EXPENSES, new String[]{Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_ID, Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_SUMMA, Prefs.TABLE_DESCRIPTION+"."+Prefs.FIELD_DESC, Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_DATE, Prefs.TABLE_CATEGORY+"."+Prefs.FIELD_CATEGORY}, null, null, null);

            //if(Prefs.DEBUG) logCursor(cursor);
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ExpensesFragment ExpensesCursorLoader loadInBackground count - " +cursor.getCount());

            return cursor;
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
                Log.d(Prefs.LOG_TAG, "ExpensesFragment  logCursor - Cursor is null");
            }
        }


    }

}
