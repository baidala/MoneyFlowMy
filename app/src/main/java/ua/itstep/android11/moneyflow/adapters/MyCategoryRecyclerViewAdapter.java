package ua.itstep.android11.moneyflow.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;


public class MyCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MyCategoryRecyclerViewAdapter.ViewHolder> {



    private Cursor cursor;
    //private NotifyingDataSetObserver dataSetObserver;
    private final Context context;
    private int columnId;
    private boolean dataValid;


    public MyCategoryRecyclerViewAdapter(Context context, Cursor c) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" CONSTRUCT ");

        this.context = context;
        this.cursor = c;
        dataValid = cursor != null ;
        columnId = dataValid ? cursor.getColumnIndexOrThrow(Prefs.FIELD_ID) : -1 ; //cursor.getColumnIndex returns index for the given column name, or -1 if the column name does not exist.
        //dataSetObserver = new NotifyingDataSetObserver();
        /*
        if( cursor != null){
            cursor.registerDataSetObserver(dataSetObserver);
        }
        */
        setHasStableIds(true);


    }

    public Cursor getCursor(){
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" getCursor() ");

        return cursor;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" onCreateViewHolder() ");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_category_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" onBindViewHolder() "+ position);


        if(!dataValid){
            throw new IllegalStateException("This should only be called when the cursor is valid");
        }
        //move to position
        if(!cursor.moveToPosition(position)){
            throw new IllegalStateException("Couldn't move cursor to position "+position);
        }

        onBindViewHolder(holder, cursor);
    }

    private void onBindViewHolder(final ViewHolder holder, Cursor c){
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" onBindViewHolder cursor () ");

        holder.tvCategoryId.setText(c.getString(c.getColumnIndex(Prefs.FIELD_ID)));
        holder.tvContent.setText(c.getString(c.getColumnIndex(Prefs.FIELD_CATEGORY)));
    }




    @Override
    public int getItemCount() {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  getItemCount () ");
        if( dataValid && cursor != null ){
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  getItemId ( " +position+ " )  columnId =" +columnId);

        if( dataValid && cursor != null && cursor.moveToPosition(position) ){
            return cursor.getLong(columnId);
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  setHasStableIds () ");

        super.setHasStableIds(hasStableIds);
        //super.setHasStableIds(true);
    }


    private Cursor swapCursor(Cursor newCursor){
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  swapCursor () ");

        if( newCursor == cursor){
            return  null;
        }
        final Cursor oldCursor = cursor;
        /*
        if( oldCursor != null && dataSetObserver != null ){
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }
        */
        int itemCount = getItemCount();
        cursor = newCursor;
        if( cursor != null ){
            /*
            if( dataSetObserver != null ){
                cursor.registerDataSetObserver(dataSetObserver);
            }
            */
            columnId = newCursor.getColumnIndexOrThrow(Prefs.FIELD_ID);
            dataValid = true;
            notifyDataSetChanged();
        } else {
            columnId = -1;
            dataValid = false;
            notifyItemRangeRemoved(0, itemCount);
        }
        return oldCursor;


    }

    public void changeCursor(Cursor c){
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  changeCursor () ");

        Cursor oldC = swapCursor(c);
        if( oldC != null){
            oldC.close();
        }

    }

    private void delete(String id){
        if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  delete () ");

        ContentResolver cr = context.getContentResolver();
        //int position = -1;
        int position = Integer.valueOf(id);
        String whereClause = Prefs.FIELD_ID + " = CAST (? AS INTEGER)";
        String[] whereArgs = {id};

        int deletedRows = cr.delete(Prefs.URI_CATEGORY, whereClause, whereArgs);
        Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  deletedRows: " + deletedRows);

        if (deletedRows == 0) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +" deletedRows == 0 !!!");

        //notifyDataSetChanged(); //TODO
        notifyItemRemoved(position);

    }




    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView tvCategoryId;
        private final TextView tvContent;
        private Button btnDelete;

        ViewHolder(View view) {
            super(view);
            mView = view;
            tvCategoryId = (TextView) view.findViewById(R.id.tvCategoryId);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            btnDelete = (Button) view.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(tvCategoryId.getText().toString());
                }
            });

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  constructor () ");

        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvContent.getText() + "'";
        }
    }

    //Receives call backs when a data set(Cursor) has been changed, or made invalid.
    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged(); //do nothing in parent class
            dataValid = true;
            notifyDataSetChanged();

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  onChanged () ");
        }

        //on cursor close
        @Override
        public void onInvalidated() {
            super.onInvalidated(); //do nothing in parent class
            dataValid = false;
            //notifyDataSetChanged();
            notifyItemRangeRemoved(0, getItemCount());

            if(Prefs.DEBUG) Log.d(Prefs.LOG_TAG, getClass().getSimpleName() +"  onInvalidated () ");
        }
    }


} //MyCategoryRecyclerViewAdapter
