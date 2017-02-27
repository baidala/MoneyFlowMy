package ua.itstep.android11.moneyflow.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.adapters.MyCategoryRecyclerViewAdapter;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Maksim Baydala on 06/02/17.
 */

public class ChangeCategoryDialog extends DialogFragment  implements LoaderManager.LoaderCallbacks<Cursor> {

    MyCategoryRecyclerViewAdapter scAdapter;
    RecyclerView lvCategories;
    private  static  final int CATEGORY_LOADER_ID = 7;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_category_list, null, true);

        lvCategories = (RecyclerView) view.findViewById(R.id.listCategories);
        scAdapter = new MyCategoryRecyclerViewAdapter(getContext(), null);
        scAdapter.setHasStableIds(true);
        lvCategories.setAdapter(scAdapter);

        getActivity().getSupportLoaderManager().restartLoader( CATEGORY_LOADER_ID, getArguments(), this );

        builder.setView(view)
                .setTitle(R.string.title_change_categories_dialog)
                .setPositiveButton(R.string.close_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNeutralButton(R.string.add_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCategory();
                    }
                });



        return builder.create();
    }



    private void addCategory(){
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() + " addCategory  ");

        AddNewCategoryDialog categoryDialog = new AddNewCategoryDialog();
        categoryDialog.show(getActivity().getSupportFragmentManager(), "ED");

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() + " onCreateLoader id: ");

        if (id == CATEGORY_LOADER_ID) {

            return new CategoryCursorLoader( getActivity() );
        }
        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" onLoadFinished() ");

        switch (loader.getId()) {
            case CATEGORY_LOADER_ID:
                scAdapter.changeCursor(data);
                if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" onLoadFinishe  CATEGORY_LOADER_ID");
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" onLoaderReset() ");
        scAdapter.swapCursor(null);
    }



    private static class CategoryCursorLoader extends CursorLoader {
        long id = 0;

        CategoryCursorLoader( Context context ) {

            super(context, Prefs.URI_CATEGORY,
                    new String[] {Prefs.FIELD_ID, Prefs.FIELD_CATEGORY},
                    null, null, null);

            //this.id = catgId;

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" CategoryCursorLoader() ");

        }

        @Override
        public Cursor loadInBackground() {
            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" CategoryCursorLoader loadInBackground");

            //String _id = Long.toString(id);
            //String where = Prefs.FIELD_CATG_ID + " = CAST (? AS INTEGER)";
            //String[] whereArgs = {_id};

            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_CATEGORY, new String[]{Prefs.TABLE_CATEGORY+"."+Prefs.FIELD_ID, Prefs.TABLE_CATEGORY+"."+Prefs.FIELD_CATEGORY}, null, null, null);

            if(Prefs.DEBUG) logCursor(cursor);

            if ( (cursor == null) || !cursor.moveToFirst() || (0 == cursor.getCount()) ) {
                Log.e(Prefs.LOG_TAG, getClass().getSimpleName() +"  CategoryCursorLoader loadInBackground - Cursor is NULL");
            }

            if (Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" CategoryCursorLoader loadInBackground count - " + cursor.getCount());


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
                Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  logCursor - Cursor is null");
            }
        }


    }


}//ChangeCategoryDialog
