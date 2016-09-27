package ua.itstep.android11.moneyflow.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.dialogs.AddNewExpencyDialog;

public class DashboardActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        dashboardPagerAdapter = new DashboardPagerAdapter(this, getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.vpDashboard);
        viewPager.setAdapter(dashboardPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabDashboard);
        tabLayout.setupWithViewPager(viewPager); 

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (tabPosition){
            case DashboardPagerAdapter.FRAGMENT_CASH_FLOW:
                getMenuInflater().inflate(R.menu.menu_cash_flow, menu);
                break;
            case DashboardPagerAdapter.FRAGMENT_INCOMES:
                getMenuInflater().inflate(R.menu.menu_incomes, menu);
                break;
            case DashboardPagerAdapter.FRAGMENT_EXPENSES:
                getMenuInflater().inflate(R.menu.menu_expences, menu);
                break;

        }

        return super.onPrepareOptionsMenu(menu);
    } 

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_list_expences:
                intent = new Intent(this, ExpensesActivity.class);
                startActivity(intent);
                //AddNewExpencyDialog expencyDialog = new AddNewExpencyDialog();
                //expencyDialog.show(getSupportFragmentManager(), "ED");
                break;
            case R.id.item_user_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);

                break;
            case R.id.item_list_incomes:
                AddNewExpencyDialog incomesDialog = new AddNewExpencyDialog();
                incomesDialog.show(getSupportFragmentManager(), "ED");
                break; 
        }
        return true;
    }
    
    public void setFragmentInfo(int position) {
        switch (position) {
            case 0:
                toolbar.setTitle(R.string.title_tab_cash_flow);

                break;
            case 1:
                toolbar.setTitle(R.string.title_tab_expences);
                break;
            case 2:
                toolbar.setTitle(R.string.title_tab_incomes);
                break;

        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(Prefs.LOG_TAG, "onPageScrolled: " +" |" + position + " | "+ positionOffset+ " | "+ positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(Prefs.LOG_TAG, "onPageSelected: " + " | " + position);

        this.tabPosition = position;

        switch (tabPosition) {
            case 0:
                toolbar.setTitle(R.string.app_name);
                break;
            case 1:
                toolbar.setTitle(R.string.title_tab_expences);
                break;
            case 2:
                toolbar.setTitle(R.string.title_tab_incomes);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(Prefs.LOG_TAG, "onPageScrollStateChanged: " + state);
    } 





}
