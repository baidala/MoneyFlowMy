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
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Maksim Baydala on 04/10/16.
 */
public class CashflowFragment extends Fragment implements LoaderManager.LoaderCallbacks<HashMap<String, String>>{

    private TextView tvCashflowExpenses;
    private TextView tvCashflowExpensesSumma;
    private TextView tvCashflowIncomes;
    private TextView tvCashflowIncomesSumma;
    private  static  final int LOADER_ID = 0;

    public static HashMap<String, String> result;

    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "ContentObserver CashFlow onChange " +uri);
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, CashflowFragment.this);

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onResume ");
        getActivity().getContentResolver().registerContentObserver(Prefs.URI_BALANCE, false, observer);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onPause ");
        getActivity().getContentResolver().unregisterContentObserver(observer);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateView ");

        View view =  inflater.inflate( R.layout.fragment_cashflow, container , false);
        tvCashflowExpenses = (TextView)view.findViewById(R.id.tvCashflowExpenses);
        tvCashflowExpensesSumma = (TextView)view.findViewById(R.id.tvCashflowSumma);
        tvCashflowIncomes = (TextView)view.findViewById(R.id.tvCashflowIncomes);
        tvCashflowIncomesSumma = (TextView)view.findViewById(R.id.tvCashflowIncomesSumma);

        tvCashflowExpenses.setText(Prefs.URI_EXPENSES_TYPE);
        tvCashflowIncomes.setText(Prefs.URI_INCOMES_TYPE);

        getActivity().getContentResolver().registerContentObserver(Prefs.URI_BALANCE, false, observer);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return view;
    }

    @Override
    public Loader<HashMap<String, String>> onCreateLoader(int id, Bundle args) {

        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateLoader ");
        if (id == LOADER_ID) {
            return new HashMapLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, String>> loader, HashMap<String, String> data) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoadFinished ");

        switch (loader.getId()) {
            case LOADER_ID:
                tvCashflowExpensesSumma.setText( data.get(Prefs.FIELD_SUMMA_EXPENSES) + Prefs.UAH);
                tvCashflowIncomesSumma.setText(data.get(Prefs.FIELD_SUMMA_INCOMES) + Prefs.UAH);

                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoadFinishe  LOADER_ID");
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, String>> loader) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onLoaderReset");
    }



    private static class HashMapLoader extends Loader<HashMap<String, String>> {

        public HashMapLoader( Context context ) {
            super(context);
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader() ");
            result = new HashMap<>();
        }

        @Override
        protected void onStartLoading() {
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading");

            super.onStartLoading();

            loadData();
            //loadData(Prefs.URI_INCOMES);


        }

        private void loadData() {
            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_BALANCE, new String[]{Prefs.FIELD_ID, Prefs.FIELD_SUMMA_EXPENSES, Prefs.FIELD_SUMMA_INCOMES}, null, null, null);

            String key = Prefs.URI_BALANCE.getLastPathSegment();

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading  " + key);

            if ( cursor.moveToFirst() ) {

                    double summaExpenses = cursor.getDouble(cursor.getColumnIndex(Prefs.FIELD_SUMMA_EXPENSES));
                    double summaIncomes = cursor.getDouble(cursor.getColumnIndex(Prefs.FIELD_SUMMA_INCOMES));

                    result.put(Prefs.FIELD_SUMMA_EXPENSES, String.valueOf(summaExpenses));
                    result.put(Prefs.FIELD_SUMMA_INCOMES, String.valueOf(summaIncomes));

                    if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading  " + String.valueOf(summaExpenses));
                    if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading  " + String.valueOf(summaIncomes));

            } else {
                result.put(Prefs.FIELD_SUMMA_EXPENSES, "0");
                result.put(Prefs.FIELD_SUMMA_INCOMES, "0");

                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onStartLoading - Cursor is NULL");
            }

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading  deliverResult");

            deliverResult(result);
            cursor.close();
        }


        private void reloadData(Uri uriIncomes) {
            Cursor cursor = getContext().getContentResolver().query(uriIncomes, new String[]{Prefs.FIELD_SUMMA}, null, null, null);
            String key = uriIncomes.getLastPathSegment();

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading  " + key);

            if ( cursor != null) {
                cursor.moveToFirst();

                if ( cursor.getCount() != 0 ) {
                    int value = 0;
                    do {
                        int temp = cursor.getInt(cursor.getColumnIndex(Prefs.FIELD_SUMMA));
                        value += temp;
                        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading  " + String.valueOf(temp));
                    } while (cursor.moveToNext());
                    //Log.d(Prefs.LOG_TAG, "ExpensesFragment HashMapLoader onStartLoading  " + String.valueOf(value));
                    result.put(key, String.valueOf(value));

                } else {
                    result.put(key, "0");
                }

                deliverResult(result);
                cursor.close();

            } else {
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "CashflowFragment onStartLoading - Cursor is NULL");
            }
        }


    }
}
