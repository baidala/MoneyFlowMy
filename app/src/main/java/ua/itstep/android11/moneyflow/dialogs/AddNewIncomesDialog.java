package ua.itstep.android11.moneyflow.dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by oracle on 15/06/16.
 */
public class AddNewIncomesDialog extends DialogFragment {
    EditText etSumma;
    AutoCompleteTextView acNameOfIncome;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "AddNewIncomesDialog onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_income, null, true);
        etSumma = (EditText) view.findViewById(R.id.etSumma);
        acNameOfIncome = (AutoCompleteTextView) view.findViewById(R.id.acNameOfIncome);
        //TODO set adapter for AutocompliteTextView

        builder.setView(view)
                //.setMessage(R.string.message_add_new_income_dialog)
                .setTitle(R.string.title_add_new_income_dialog)
                .setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewIncome();
                    }
                })
                .setNegativeButton(R.string.negative_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    private void addNewIncome() {
        double summa = 0;
        Log.d(Prefs.LOG_TAG, "AddNewIncomesDialog addNewIncome: " + etSumma.getText().toString());

        ContentResolver cr = getContext().getContentResolver();
        ContentValues cvIncome = new ContentValues();

        try {
            summa = Double.parseDouble(etSumma.getText().toString());
        } catch (NumberFormatException ex){}

        String name = acNameOfIncome.getText().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        cvIncome.put(Prefs.FIELD_SUMMA, summa);
        cvIncome.put(Prefs.FIELD_DESC, name);
        cvIncome.put(Prefs.FIELD_DATE, date);



        Uri incomesId = cr.insert(Prefs.URI_INCOMES, cvIncome);

        if (incomesId == null) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog addNewIncomes incomesId  = NULL");



    }
}
