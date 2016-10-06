package ua.itstep.android11.moneyflow.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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
 * Created by oracle on 6/3/16.
 */
public class IncomesFragment extends Fragment implements LoaderManager.LoaderCallbacks<HashMap<String, String>> {
    private static final String CURRENT_MONTH = "current";
    private TextView tvCurrentFragmentIncomes;
    //private RoundChart roundChart;
    public static HashMap<String, String> result;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "IncomesFragment onCreateView ");

        //((DashboardActivity)getActivity()).setFragmentInfo(DashboardPagerAdapter.FRAGMENT_EXPENSES);
        View view =  inflater.inflate( R.layout.fragment_incomes, container , false);
        tvCurrentFragmentIncomes = (TextView)view.findViewById(R.id.tvCurrentFragmentIncomes);
        //roundChart = (RoundChart)view.findViewById(R.id.vRCexpenses);
        if ( tvCurrentFragmentIncomes == null ) {
            Log.d(Prefs.LOG_TAG, "IncomesFragment onCreateView  tvCurrentFragmentIncomes == null ");
        }
        getActivity().getSupportLoaderManager().initLoader(2, null, this);

        return view;
    }


    @Override
    public Loader<HashMap<String, String>> onCreateLoader(int id, Bundle args) {
        Log.d(Prefs.LOG_TAG, "IncomesFragment onCreateLoader ");
        return new HashMapLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, String>> loader, HashMap<String, String> data) {
        Log.d(Prefs.LOG_TAG, "IncomesFragment onLoadFinished " + data.get(CURRENT_MONTH));
        tvCurrentFragmentIncomes.setText( data.get(CURRENT_MONTH) );
       // roundChart.setValues(10, 100);

    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, String>> loader) {
        //TODO
        Log.d(Prefs.LOG_TAG, "IncomesFragment onLoaderReset");
    }

    private static class HashMapLoader extends Loader<HashMap<String, String>> {

        public HashMapLoader( Context context ) {
            super(context);
            Log.d(Prefs.LOG_TAG, "IncomesFragment HashMapLoader() ");
            result = new HashMap<>();
        }

        @Override
        protected void onStartLoading() {
            Log.d(Prefs.LOG_TAG, "IncomesFragment HashMapLoader onStartLoading");

            super.onStartLoading();


            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_INCOMES, new String[]{Prefs.FIELD_SUMMA}, null, null, null);


            if ( cursor != null) {
                cursor.moveToFirst();

                if ( cursor.getCount() != 0 ) {
                    int value = 0;
                    do {
                        value += cursor.getInt(cursor.getColumnIndex(Prefs.FIELD_SUMMA));
                    } while (cursor.moveToNext());
                    Log.d(Prefs.LOG_TAG, "IncomesFragment HashMapLoader onStartLoading  " + String.valueOf(value));
                    result.put(CURRENT_MONTH, String.valueOf(value));

                } else {
                    result.put(CURRENT_MONTH, "0");
                }

                deliverResult(result);

            } else {
                Log.d(Prefs.LOG_TAG, "IncomesFragment onStartLoading - Cursor is NULL");
            }
        }




    }
}
