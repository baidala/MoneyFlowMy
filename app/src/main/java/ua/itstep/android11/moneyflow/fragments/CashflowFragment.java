package ua.itstep.android11.moneyflow.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
    private TextView tvCashflowSumma;
    private TextView tvCashflowIncomes;
    private TextView tvCashflowIncomesSumma;

    public static HashMap<String, String> result;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateView ");

        View view =  inflater.inflate( R.layout.fragment_cashflow, container , false);
        tvCashflowExpenses = (TextView)view.findViewById(R.id.tvCashflowExpenses);
        tvCashflowSumma = (TextView)view.findViewById(R.id.tvCashflowSumma);
        tvCashflowIncomes = (TextView)view.findViewById(R.id.tvCashflowIncomes);
        tvCashflowIncomesSumma = (TextView)view.findViewById(R.id.tvCashflowIncomesSumma);

        getActivity().getSupportLoaderManager().initLoader(3, null, this);

        return view;
    }

    @Override
    public Loader<HashMap<String, String>> onCreateLoader(int id, Bundle args) {

        Log.d(Prefs.LOG_TAG, "CashflowFragment onCreateLoader ");
        return new HashMapLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, String>> loader, HashMap<String, String> data) {
        Log.d(Prefs.LOG_TAG, "CashflowFragment onLoadFinished ");
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, String>> loader) {
        Log.d(Prefs.LOG_TAG, "CashflowFragment onLoaderReset");
    }

    private static class HashMapLoader extends Loader<HashMap<String, String>> {

        public HashMapLoader( Context context ) {
            super(context);
            Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader() ");
            result = new HashMap<>();
        }

        @Override
        protected void onStartLoading() {
            Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading");

            super.onStartLoading();

            loadData(Prefs.URI_EXPENSES);
            loadData(Prefs.URI_INCOMES);


        }

        private void loadData(Uri uriIncomes) {
            Cursor cursor = getContext().getContentResolver().query(uriIncomes, new String[]{Prefs.FIELD_SUMMA}, null, null, null);


            if ( cursor != null) {
                cursor.moveToFirst();

                if ( cursor.getCount() != 0 ) {
                    int value = 0;
                    do {
                        int temp = cursor.getInt(cursor.getColumnIndex(Prefs.FIELD_SUMMA));
                        value += temp;
                        Log.d(Prefs.LOG_TAG, "CashflowFragment HashMapLoader onStartLoading  " + String.valueOf(temp));
                    } while (cursor.moveToNext());
                    //Log.d(Prefs.LOG_TAG, "ExpensesFragment HashMapLoader onStartLoading  " + String.valueOf(value));
                    result.put(Prefs.CURRENT_MONTH, String.valueOf(value));

                } else {
                    result.put(Prefs.CURRENT_MONTH, "0");
                }

                deliverResult(result);
                cursor.close();

            } else {
                Log.d(Prefs.LOG_TAG, "CashflowFragment onStartLoading - Cursor is NULL");
            }
        }


    }
}
