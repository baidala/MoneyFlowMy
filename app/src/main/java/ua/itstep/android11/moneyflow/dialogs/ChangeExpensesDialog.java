package ua.itstep.android11.moneyflow.dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
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
public class ChangeExpensesDialog extends DialogFragment {
    EditText etSumma;
    AutoCompleteTextView acNameOfExpense;
    long id;
    double summa_old;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_expense, null, true);
        etSumma = (EditText) view.findViewById(R.id.etSumma);
        acNameOfExpense = (AutoCompleteTextView) view.findViewById(R.id.acNameOfExpense);
        //TODO set adapter for AutocompliteTextView

        etSumma.setText(getArguments().getString(Prefs.FIELD_SUMMA_EXPENSES));
        acNameOfExpense.setText(getArguments().getString(Prefs.FIELD_DESC));
        id = getArguments().getLong(Prefs.FIELD_ID);

        summa_old = Double.parseDouble(etSumma.getText().toString());
        summa_old *= -1;

        builder.setView(view)
                .setMessage(R.string.message_change_expense_dialog)
                .setTitle(R.string.title_change_expense_dialog)
                .setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeExpense();
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

    private void changeExpense() {

        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog changeExpense: " + etSumma.getText().toString());

        ContentResolver cr = getContext().getContentResolver();
        ContentValues cvExpense = new ContentValues();


        double summa = Double.parseDouble(etSumma.getText().toString());
        String name = acNameOfExpense.getText().toString();
        //String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //TODO

        cvExpense.put(Prefs.FIELD_SUMMA, summa);
        cvExpense.put(Prefs.FIELD_SUMMA_EXPENSES, summa_old);
        cvExpense.put(Prefs.FIELD_DESC, name);
        //cvExpense.put(Prefs.FIELD_DATE, date);

        String _id = Long.toString(id);
        String whereClause = Prefs.FIELD_ID + " = CAST (? AS INTEGER)";
        String[] whereArgs = {_id};

        int updatedRows = cr.update(Prefs.URI_EXPENSES, cvExpense, whereClause, whereArgs);
        Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog changeExpense updatedRows: " + updatedRows);

        if (updatedRows == 0) Log.d(Prefs.LOG_TAG, "ChangeExpensesDialog changeExpense updated == 0 !!!");




    }
}
