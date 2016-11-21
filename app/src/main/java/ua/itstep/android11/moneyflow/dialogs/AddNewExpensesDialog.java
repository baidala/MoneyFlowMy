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
 * Created by Test on 16.05.2016.
 */
public class AddNewExpensesDialog extends DialogFragment {
    EditText etSumma;
    AutoCompleteTextView acNameOfExpense;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_expense, null, true);
        etSumma = (EditText) view.findViewById(R.id.etSumma);
        acNameOfExpense = (AutoCompleteTextView) view.findViewById(R.id.acNameOfExpense);
        //TODO set adapter for AutocompliteTextView

        builder.setView(view)
                //.setMessage(R.string.message_add_new_expense_dialog)
                .setTitle(R.string.title_add_new_expense_dialog)
                .setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewExpense();
                    }
                })
                .setNegativeButton(R.string.negativ_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    private void addNewExpense() {

        Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog addNewExpense: " + etSumma.getText().toString());

        ContentResolver cr = getContext().getContentResolver();
        ContentValues cvExpense = new ContentValues();


        double summa = Double.parseDouble(etSumma.getText().toString());
        String name = acNameOfExpense.getText().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        cvExpense.put(Prefs.FIELD_SUMMA, summa);
        cvExpense.put(Prefs.FIELD_DESC, name);
        cvExpense.put(Prefs.FIELD_DATE, date);

        Uri expenseId = cr.insert(Prefs.URI_EXPENSES, cvExpense);


        if (expenseId == null) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog addNewExpense expenseId  = NULL");




    }
}
