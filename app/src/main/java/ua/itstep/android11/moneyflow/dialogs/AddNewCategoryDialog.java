package ua.itstep.android11.moneyflow.dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Test on 16.05.2016.
 */
public class AddNewCategoryDialog extends DialogFragment  {
    EditText etCategory;
    long categoryId = 0;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "AddNewCategoryDialog onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_category, null, true);
        etCategory = (EditText) view.findViewById(R.id.etCategory);


        builder.setView(view)
                .setTitle(R.string.title_add_new_category_dialog)
                .setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewCategory();
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



    private void addNewCategory() {

        Log.d(Prefs.LOG_TAG, "AddNewCategoryDialog addNewCategory: " + etCategory.getText().toString());

        ContentResolver cr = getContext().getContentResolver();
        ContentValues cvCategory = new ContentValues();
        String name = etCategory.getText().toString();
        cvCategory.put(Prefs.FIELD_DESC, name);
        //Uri expenseId = cr.insert(Prefs.URI_CATEGORY, cvCategory);


        //if (expenseId == null) Log.d(Prefs.LOG_TAG, "AddNewExpensesDialog addNewExpense expenseId  = NULL");

    }










}
