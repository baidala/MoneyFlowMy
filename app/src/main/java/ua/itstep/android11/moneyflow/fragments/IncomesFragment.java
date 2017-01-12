package ua.itstep.android11.moneyflow.fragments;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
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
import android.widget.ListView;
import android.widget.TextView;


import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;
import ua.itstep.android11.moneyflow.dialogs.ChangeIncomesDialog;

/**
 * Created by oracle on 6/3/16.
 */
public class IncomesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView lvIncomes;
    SimpleCursorAdapter scIncomesAdapter;
    private  static  final int INCOMES_LOADER_ID = 2;

    private ContentObserver observerIncomes;



    @Override
    public void onResume() {
        super.onResume();
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onResume ");
        getActivity().getContentResolver().registerContentObserver(Prefs.URI_INCOMES, true, observerIncomes);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onPause ");
        getActivity().getContentResolver().unregisterContentObserver(observerIncomes);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "IncomesFragment onCreateView ");

        String[] from = new String[] {Prefs.FIELD_SUMMA, Prefs.FIELD_DESC, Prefs.FIELD_DATE};
        int[] to = new int[] {R.id.tvSummaItemIncomes, R.id.tvNameItemIncomes, R.id.tvDateItemIncomes};




        observerIncomes = new ContentObserver(new Handler()) {

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ContentObserver Incomes onChange " +uri);

                getActivity().getSupportLoaderManager().restartLoader(INCOMES_LOADER_ID, null, IncomesFragment.this);

            }
        };



        View view =  inflater.inflate( R.layout.fragment_incomes, container , false);

        scIncomesAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                R.layout.item_incomes,
                null, from, to,
                CursorAdapter.NO_SELECTION);

        lvIncomes = (ListView) view.findViewById(R.id.lvIncomes);
        lvIncomes.setAdapter(scIncomesAdapter);
        lvIncomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onCreateView onItemClick:" + position);

                TextView edSumma = (TextView) view.findViewById(R.id.tvSummaItemIncomes);
                String summa = edSumma.getText().toString();
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onCreateView onItemClick summa:" + summa);

                TextView edNameOfIncomes = (TextView) view.findViewById(R.id.tvNameItemIncomes);
                String desc = edNameOfIncomes.getText().toString();

                ChangeIncomesDialog incomesDialog = new ChangeIncomesDialog();
                Bundle args = new Bundle();
                args.putString(Prefs.FIELD_SUMMA_INCOMES, summa);
                args.putString(Prefs.FIELD_DESC, desc);
                args.putLong(Prefs.FIELD_ID, id);

                incomesDialog.setArguments(args);
                incomesDialog.show(getFragmentManager(), "CH");
            }
        });

        setHasOptionsMenu(true);

        getActivity().getContentResolver().registerContentObserver(Prefs.URI_INCOMES, false, observerIncomes);
        getActivity().getSupportLoaderManager().initLoader(INCOMES_LOADER_ID, null, this);

        return view;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(Prefs.LOG_TAG, "IncomesFragment onCreateLoader id - " +id);

        if (id == INCOMES_LOADER_ID) {

            return new IncomesCursorLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onLoadFinished cursors - " +cursor.getCount() );
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onLoadFinished loadId - " +loader.getId() );

        switch (loader.getId()) {
            case INCOMES_LOADER_ID:
                scIncomesAdapter.swapCursor(cursor);
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onLoadFinished  INCOMES_LOADER_ID");
                break;
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        Log.d(Prefs.LOG_TAG, "IncomesFragment onLoaderReset");

        scIncomesAdapter.swapCursor(null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.item_reCalc);
        menuItem.setVisible(false);


        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onCreateOptionsMenu");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_refresh:
                getActivity().getSupportLoaderManager().restartLoader(INCOMES_LOADER_ID, null, this);


                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment onOptionsItemSelected");
                break;
        }
        return true;
    }



    private static class IncomesCursorLoader extends CursorLoader {

        IncomesCursorLoader( Context context ) {
            super(context, Prefs.URI_INCOMES,
                    new String[] {Prefs.FIELD_ID, Prefs.FIELD_SUMMA, Prefs.FIELD_DESC, Prefs.FIELD_DATE},
                    null, null, null);
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment IncomesCursorLoader() ");
        }

        @Override
        public Cursor loadInBackground() throws SQLException {
            //if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment IncomesCursorLoader loadInBackground");

            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_INCOMES, new String[]{Prefs.TABLE_INCOMES+"."+Prefs.FIELD_ID, Prefs.TABLE_INCOMES+"."+Prefs.FIELD_SUMMA, Prefs.TABLE_DESCRIPTION+"."+Prefs.FIELD_DESC, Prefs.TABLE_INCOMES+"."+Prefs.FIELD_DATE}, null, null, null);
            //if(Prefs.DEBUG) logCursor(cursor);


            if ( (cursor == null) || !cursor.moveToFirst() || (0 == cursor.getCount()) ) {
                Log.e(Prefs.LOG_TAG, "Failed to load data from "+ Prefs.URI_INCOMES);
            }

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "IncomesFragment IncomesCursorLoader loadInBackground - " +cursor.getCount());

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
                Log.d(Prefs.LOG_TAG, "IncomesFragment  logCursor - Cursor is null");
            }
        }




    }
}
