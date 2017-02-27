package ua.itstep.android11.moneyflow.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;
import java.util.Vector;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.fragments.CashflowFragment;
import ua.itstep.android11.moneyflow.fragments.ExpensesFragment;
import ua.itstep.android11.moneyflow.fragments.IncomesFragment;

/**
 * Created by oracle on 6/3/16.
 */
public class DashboardPagerAdapter extends FragmentPagerAdapter {

    public final static int FRAGMENT_EXPENSES = 0;
    public final static int FRAGMENT_CASH_FLOW = 1;
    public final static int FRAGMENT_INCOMES = 2;


    public List<Fragment> fragments = new Vector<Fragment>(); //Vector - thread-safe

    Context context;

    public DashboardPagerAdapter(Context context, FragmentManager fm ) {
        super(fm);
        this.context = context;

        fragments.add(ExpensesFragment.newInstance(FRAGMENT_EXPENSES));  //0
        fragments.add(CashflowFragment.newInstance(FRAGMENT_CASH_FLOW));  //1
        fragments.add(IncomesFragment.newInstance(FRAGMENT_INCOMES));   //2
    }


    // position  = list.position
    @Override
    public Fragment getItem(int position) {
        //Log.d(Prefs.LOG_TAG, "DashboardPagerAdapter getItem - " + position);

        /*
        Fragment defaultFragment = null;
        Bundle argBundle;

        switch (position) {
            case FRAGMENT_CASH_FLOW:
                defaultFragment =  new CashflowFragment();
                argBundle = new Bundle();
                argBundle.putString(DefaultFragment.KEY_NAME, context.getResources().getString(R.string.title_tab_incomes));
                defaultFragment.setArguments(argBundle);
                return defaultFragment;
                //break;

            case FRAGMENT_EXPENSES:
                return new ExpensesFragment();
                //break;

            case FRAGMENT_INCOMES:
                defaultFragment =  new DefaultFragment();
                argBundle = new Bundle();
                argBundle.putString(DefaultFragment.KEY_NAME, context.getResources().getString(R.string.title_tab_incomes));
                defaultFragment.setArguments(argBundle);
                return defaultFragment;
                //break;
            default:
                Log.d(Prefs.LOG_TAG, "Unknown position");
                break;
        }

        return defaultFragment;
        */
        //return Fragment.instantiate(context, fragments.get(position));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        //return 3;
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Log.d(Prefs.LOG_TAG, "DashboardPagerAdapter getPageTitle - " + position);

        switch (position) {
            case FRAGMENT_CASH_FLOW:
                return context.getResources().getString(R.string.title_tab_cash_flow);
            case FRAGMENT_EXPENSES:
                return context.getResources().getString(R.string.title_tab_expenses);
            case FRAGMENT_INCOMES:
                return context.getResources().getString(R.string.title_tab_incomes);
        }

        //Log.d(Prefs.LOG_TAG, "DashboardPagerAdapter getPageTitle - unknown position");

        return null;
    }



}
