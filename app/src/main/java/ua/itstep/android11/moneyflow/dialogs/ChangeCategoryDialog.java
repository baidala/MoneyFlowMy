package ua.itstep.android11.moneyflow.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Maksim Baydala on 06/02/17.
 */

public class ChangeCategoryDialog extends DialogFragment {

    SimpleCursorAdapter scAdapter;
    RecyclerView lvCategories;
    private  static  final int CATEGORY_LOADER_ID = 7;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" onCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_category_list, null, true);



        String[] from = new String[] { Prefs.FIELD_ID, Prefs.FIELD_CATEGORY};
        int[] to = new int[] {R.id.tvCategoryId, R.id.tvContent};

        scAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                R.layout.fragment_category_item,
                null, from, to,
                CursorAdapter.NO_SELECTION);

        lvCategories = (RecyclerView) view.findViewById(R.id.listCategories);

        //lvCategories.setAdapter(scAdapter);

        //getActivity().getSupportLoaderManager().restartLoader( CATEGORY_LOADER_ID, getArguments(), this );





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
}
