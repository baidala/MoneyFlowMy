package ua.itstep.android11.moneyflow.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.TextView;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Maksim Baydala on 21/12/16.
 */

public class SpentsByCategoryDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter scAdapter;
    ListView lvCategory;
    private  static  final int CATEGORY_LOADER_ID = 6;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_categories, null, true);

        //long id = getArguments().getLong(Prefs.FIELD_ID);

        String[] from = new String[] {Prefs.FIELD_SUMMA, Prefs.FIELD_DESC, Prefs.FIELD_DATE, Prefs.FIELD_CATEGORY};
        int[] to = new int[] {R.id.tvSummaItemExpenses, R.id.tvNameItemExpenses, R.id.tvDateItemExpenses, R.id.tvCatgItemExpenses};

        scAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                R.layout.item_expenses_adapter,
                null, from, to,
                CursorAdapter.NO_SELECTION);

        lvCategory = (ListView) view.findViewById(R.id.lvDialogCategories);

        lvCategory.setAdapter(scAdapter);

        getActivity().getSupportLoaderManager().restartLoader( CATEGORY_LOADER_ID, getArguments(), this );





        builder.setView(view)
                .setTitle(R.string.title_category_dialog)
                .setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });



        return builder.create();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog onCreateLoader id: "+ getArguments().getLong(Prefs.FIELD_ID));

        if (id == CATEGORY_LOADER_ID) {

            return new CategoryCursorLoader(getActivity(), getArguments().getLong(Prefs.FIELD_ID));
        }
        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog onLoadFinished() ");

        switch (loader.getId()) {
            case CATEGORY_LOADER_ID:
                scAdapter.swapCursor(data);
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog onLoadFinishe  CATEGORY_LOADER_ID");
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog onLoaderReset() ");
        scAdapter.swapCursor(null);
    }



    private static class CategoryCursorLoader extends CursorLoader {
        long id = 0;

        CategoryCursorLoader( Context context, long catgId ) {

            super(context, Prefs.URI_EXPENSES,
                    new String[] {Prefs.FIELD_ID, Prefs.FIELD_SUMMA, Prefs.FIELD_DESC, Prefs.FIELD_DATE, Prefs.FIELD_CATEGORY},
                    null, null, null);

            this.id = catgId;

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog CategoryCursorLoader() ");

        }

        @Override
        public Cursor loadInBackground() {
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog CategoryCursorLoader loadInBackground");

            String _id = Long.toString(id);
            String where = Prefs.FIELD_CATG_ID + " = CAST (? AS INTEGER)";
            String[] whereArgs = {_id};

            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_EXPENSES, new String[]{Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_ID, Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_SUMMA, Prefs.TABLE_DESCRIPTION+"."+Prefs.FIELD_DESC, Prefs.TABLE_EXPENSES+"."+Prefs.FIELD_DATE, Prefs.TABLE_CATEGORY+"."+Prefs.FIELD_CATEGORY}, where, whereArgs, null);

            if(Prefs.DEBUG) logCursor(cursor);

            if ( (cursor == null) || !cursor.moveToFirst() || (0 == cursor.getCount()) ) {
                Log.e(Prefs.LOG_TAG, "SpentsByCategoryDialog  CategoryCursorLoader loadInBackground - Cursor is NULL");
            }

            if (Prefs.DEBUG) Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog CategoryCursorLoader loadInBackground count - " + cursor.getCount());


            return cursor;
        }



        // вывод в лог данных из курсора
        private void logCursor(Cursor c) {
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
                Log.d(Prefs.LOG_TAG, "SpentsByCategoryDialog  logCursor - Cursor is null");
            }
        }


    }








}
