package ua.itstep.android11.moneyflow.activities;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.adapters.DashboardPagerAdapter;
import ua.itstep.android11.moneyflow.dialogs.AddNewExpensesDialog;
import ua.itstep.android11.moneyflow.dialogs.AddNewIncomesDialog;
import ua.itstep.android11.moneyflow.utils.Prefs;


public class DashboardActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener  {

    DashboardPagerAdapter dashboardPagerAdapter;
    ViewPager viewPager;
    Toolbar toolbar;
    int tabPosition = 0;
    FloatingActionButton fab;
    //public List<String> fragments = new Vector<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        dashboardPagerAdapter = new DashboardPagerAdapter(this, getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.vpDashboard);
        viewPager.setAdapter(dashboardPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabDashboard);
        tabLayout.setupWithViewPager(viewPager);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);



        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        Log.d(Prefs.LOG_TAG, "DashboardActivity onCreate");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        Log.d(Prefs.LOG_TAG, "DashboardActivity onCreateOptionsMenu");
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_expenses:
                AddNewExpensesDialog addNewExpencyDialog = new AddNewExpensesDialog();
                addNewExpencyDialog.show(getSupportFragmentManager(), "addExpense");
                //Toast.makeText(this, "Click on expency", Toast.LENGTH_SHORT).show();
                Log.d(Prefs.LOG_TAG, "DashboardActivity onOptionsItemSelected");
                break;
        }
        return true;
    }

    // вывод в лог данных из курсора
    public void logCursor(Cursor c) {
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
            Log.d(Prefs.LOG_TAG, "DashboardActivity logCursor - Cursor is null");
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(Prefs.LOG_TAG, "DashboardActivity onPageScrolled: " +" |" + position + " | "+ positionOffset+ " | "+ positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(Prefs.LOG_TAG, "DashboardActivity onPageSelected: " + " | " + position);

        this.tabPosition = position;

        switch (tabPosition) {
            case DashboardPagerAdapter.FRAGMENT_CASH_FLOW:
                toolbar.setTitle(R.string.app_name);
                fab.setVisibility(View.GONE);
                Log.d(Prefs.LOG_TAG, "DashboardActivity - FRAGMENT_CASH_FLOW");
                break;

            case DashboardPagerAdapter.FRAGMENT_EXPENSES:
                toolbar.setTitle(R.string.title_tab_expenses);
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddNewExpensesDialog expenseDialog = new AddNewExpensesDialog();
                        expenseDialog.show(getSupportFragmentManager(), "ED");
                    }
                });
                Log.d(Prefs.LOG_TAG, "DashboardActivity - FRAGMENT_EXPENSES");
                break;

            case DashboardPagerAdapter.FRAGMENT_INCOMES:
                toolbar.setTitle(R.string.title_tab_incomes);
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddNewIncomesDialog incomesDialog = new AddNewIncomesDialog();
                        incomesDialog.show(getSupportFragmentManager(), "IN");
                    }
                });
                Log.d(Prefs.LOG_TAG, "DashboardActivity - FRAGMENT_INCOMES");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(Prefs.LOG_TAG, "DashboardActivity  onPageScrollStateChanged: " + state);
    }








}
