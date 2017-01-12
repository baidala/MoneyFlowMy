package ua.itstep.android11.moneyflow.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import ua.itstep.android11.moneyflow.R;

/**
 * Created by Maksim Baydala on 12/01/17.
 */
public class ChangeCategoryActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //TODO

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }


}
