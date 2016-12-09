package ua.itstep.android11.moneyflow.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.HashMap;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;
import ua.itstep.android11.moneyflow.views.Graphics;

import static ua.itstep.android11.moneyflow.utils.Prefs.DEBUG;

/**
 * Created by Maksim Baydala on 04/10/16.
 */
public class CashflowFragment extends Fragment implements LoaderManager.LoaderCallbacks<HashMap<String, String>>{

    private TextView tvCashflowExpenses;
    private TextView tvCashflowExpensesSumma;
    private TextView tvCashflowIncomes;
    private TextView tvCashflowIncomesSumma;
    private Graphics graphics;
    private ViewGroup.LayoutParams layoutParams;

    private  static  final int CASHFLOW_LOADER_ID = 0;

    public static final int URI_EXPENSES_CODE = 1;
    public static final int URI_INCOMES_CODE = 2;
    public static HashMap<String, String> result;

    private ContentObserver observer;
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(Prefs.URI_EXPENSES_AUTHORITIES,
                Prefs.URI_EXPENSES_TYPE,
                URI_EXPENSES_CODE);
        uriMatcher.addURI(Prefs.URI_INCOMES_AUTHORITIES,
                Prefs.URI_INCOMES_TYPE,
                URI_INCOMES_CODE);
    }



    @Override
    public void onResume() {
        super.onResume();
        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onResume ");
        getActivity().getContentResolver().registerContentObserver(Prefs.URI_BALANCE, true, observer);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onPause ");
        getActivity().getContentResolver().unregisterContentObserver(observer);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateView ");

        View view =  inflater.inflate( R.layout.fragment_cashflow, container , false);
        tvCashflowExpenses = (TextView)view.findViewById(R.id.tvCashflowExpenses);
        tvCashflowExpensesSumma = (TextView)view.findViewById(R.id.tvCashflowSumma);
        tvCashflowIncomes = (TextView)view.findViewById(R.id.tvCashflowIncomes);
        tvCashflowIncomesSumma = (TextView)view.findViewById(R.id.tvCashflowIncomesSumma);
        graphics = (Graphics) view.findViewById(R.id.vGraphics);

        tvCashflowExpenses.setText(Prefs.URI_EXPENSES_TYPE);
        tvCashflowIncomes.setText(Prefs.URI_INCOMES_TYPE);


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutParams = graphics.getLayoutParams();

        layoutParams.width = metrics.widthPixels / 3;
        layoutParams.height = metrics.heightPixels / 2;



        Log.d(Prefs.LOG_TAG,"layoutParams.width: " + layoutParams.width);
        Log.d(Prefs.LOG_TAG,"layoutParams.height: " + layoutParams.height);


        observer = new ContentObserver(new Handler()) {

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                if(DEBUG) Log.d(Prefs.LOG_TAG, "ContentObserver CashFlow onChange selfChange ="+ selfChange +"||"+uri);

                getActivity().getSupportLoaderManager().restartLoader(CASHFLOW_LOADER_ID, null, CashflowFragment.this);

            }
        };


        getActivity().getContentResolver().registerContentObserver(Prefs.URI_BALANCE, false, observer);

        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateView observer = "+ observer);


        setHasOptionsMenu(true);

        getActivity().getSupportLoaderManager().initLoader(CASHFLOW_LOADER_ID, null, this);

        Loader<Object> loader = getActivity().getSupportLoaderManager().getLoader(CASHFLOW_LOADER_ID);
        getActivity().getSupportLoaderManager().enableDebugLogging(DEBUG);
        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateView getLoader = "+ loader);

        return view;
    }


    @Override
    public Loader<HashMap<String, String>> onCreateLoader(int id, Bundle args) {

        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateLoader ");

        if (id == CASHFLOW_LOADER_ID) {
            return new HashMapLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, String>> loader, HashMap<String, String> data) {
        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoadFinished expenses- " +data.get(Prefs.FIELD_SUMMA_EXPENSES) );
        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoadFinished incomes- " +data.get(Prefs.FIELD_SUMMA_INCOMES) );
        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoadFinished getLoader = "+ loader.getId());
        if(DEBUG) Log.d(Prefs.LOG_TAG,"CashflowFragment onLoadFinished layoutParams.width: " + layoutParams.width);
        if(DEBUG) Log.d(Prefs.LOG_TAG,"CashflowFragment onLoadFinished layoutParams.height: " + layoutParams.height);

        switch (loader.getId()) {
            case CASHFLOW_LOADER_ID:
                tvCashflowExpensesSumma.setText( data.get(Prefs.FIELD_SUMMA_EXPENSES) + Prefs.UAH);
                tvCashflowIncomesSumma.setText(data.get(Prefs.FIELD_SUMMA_INCOMES) + Prefs.UAH);

                graphics.setValues( Float.parseFloat(data.get(Prefs.FIELD_SUMMA_INCOMES)), Float.parseFloat(data.get(Prefs.FIELD_SUMMA_EXPENSES)) );
                graphics.invalidate();

                if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoadFinished observer = "+ observer.getClass().toString());

                if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoadFinished  CASHFLOW_LOADER_ID");
                break;
        }




    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, String>> loader) {
        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoaderReset");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);


        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateOptionsMenu");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_refresh:
                getActivity().getSupportLoaderManager().restartLoader(CASHFLOW_LOADER_ID, null, this);

                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onOptionsItemSelected item_refresh");
                break;
            case R.id.item_reCalc:
                recalcData(Prefs.URI_INCOMES);
                recalcData(Prefs.URI_EXPENSES);

                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onOptionsItemSelected item_reCalc");
                break;
        }
        return true;
    }

    private void recalcData(Uri uriTable) {
        ContentValues cvBalance;

        Cursor cursor = getContext().getContentResolver().query(uriTable, new String[]{Prefs.FIELD_SUMMA}, null, null, null);
        String key = uriTable.getLastPathSegment();

        if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment recalcData: " + key);

        if ( cursor != null) {
            double summa = 0.0;
            int updatedRows = 0;
            cursor.moveToFirst();

            if ( cursor.getCount() != 0 ) {

                do {
                    double value = 0;
                    value = cursor.getDouble(cursor.getColumnIndex(Prefs.FIELD_SUMMA));
                    summa += value;
                    if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment recalcData value: " + summa);
                } while (cursor.moveToNext());
                //Log.d(Prefs.LOG_TAG, "ExpensesFragment HashMapLoader onStartLoading  " + String.valueOf(value));

                cvBalance = new ContentValues();

                switch (uriMatcher.match(uriTable)) {
                    case URI_EXPENSES_CODE:
                        cvBalance.put(Prefs.FIELD_SUMMA_EXPENSES, summa);
                        Log.d(Prefs.LOG_TAG, "CashflowFragment recalcData SUMMA_EXPENSES: " + summa);
                        break;

                    case URI_INCOMES_CODE:
                        cvBalance.put(Prefs.FIELD_SUMMA_INCOMES, summa);
                        Log.d(Prefs.LOG_TAG, "CashflowFragment recalcData SUMMA_INCOMES: " + summa);
                        break;
                }


                updatedRows = getContext().getContentResolver().update(Prefs.URI_BALANCE, cvBalance, null, null);
                if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment recalcData updatedRows: " + updatedRows);

            } else {
                cvBalance = new ContentValues();

                switch (uriMatcher.match(uriTable)) {
                    case URI_EXPENSES_CODE:
                        cvBalance.put(Prefs.FIELD_SUMMA_EXPENSES, summa);
                        break;

                    case URI_INCOMES_CODE:
                        cvBalance.put(Prefs.FIELD_SUMMA_INCOMES, summa);
                        break;
                }

                updatedRows = getContext().getContentResolver().update(Prefs.URI_BALANCE, cvBalance, null, null);
                if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment recalcData cursor.getCount = 0  updatedRows: " + updatedRows);

            }
            cursor.close();

        } else {
            if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onStartLoading - Cursor is NULL");
        }
    }




    private static class HashMapLoader extends Loader<HashMap<String, String>> {

        HashMapLoader( Context context ) {
            super(context);
            if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader() ");

            result = new HashMap<>();
        }

        @Override
        protected void onStartLoading() {
            if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading");

            super.onStartLoading();

            if (takeContentChanged()) {
                forceLoad();
                if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading forseLoad");
            }

            loadData();



        }


        private void loadData() {
            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_BALANCE, new String[]{Prefs.FIELD_ID, Prefs.FIELD_SUMMA_EXPENSES, Prefs.FIELD_SUMMA_INCOMES}, null, null, null);

            String key = Prefs.URI_BALANCE.getLastPathSegment();

            if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader loadData  " + key);

            if ( cursor.moveToFirst() ) {

                    double summaExpenses = cursor.getDouble(cursor.getColumnIndex(Prefs.FIELD_SUMMA_EXPENSES));
                    double summaIncomes = cursor.getDouble(cursor.getColumnIndex(Prefs.FIELD_SUMMA_INCOMES));

                    result.put(Prefs.FIELD_SUMMA_EXPENSES, String.valueOf(summaExpenses));
                    result.put(Prefs.FIELD_SUMMA_INCOMES, String.valueOf(summaIncomes));

                    if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader loadData  " + String.valueOf(summaExpenses));
                    if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader loadData  " + String.valueOf(summaIncomes));

            } else {
                result.put(Prefs.FIELD_SUMMA_EXPENSES, "0");
                result.put(Prefs.FIELD_SUMMA_INCOMES, "0");

                if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment loadData - Cursor is NULL");
            }

            if(DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader loadData  deliverResult");

            cursor.close();
            deliverResult(result);


        }





    }
}
