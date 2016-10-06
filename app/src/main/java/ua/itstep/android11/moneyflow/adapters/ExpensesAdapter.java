package ua.itstep.android11.moneyflow.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.utils.Prefs;

public class ExpensesAdapter extends AbstractCursorRecyclerAdapter<ExpensesViewHolder> {


    private Context mContext;
    private Cursor mCursor;

    public ExpensesAdapter(Context mContext, Cursor mCursor) {
        super(mCursor);
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @Override
    public ExpensesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_expenses_adapter,parent,false);
        return new ExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpensesViewHolder holder, Cursor mCursor) {

        //InnerViewHolder innerViewHolder = (InnerViewHolder) holder;
        //mCursor.moveToPosition(position);

        holder.tvSumma.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.FIELD_SUMMA)));
        holder.tvDate.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.FIELD_DATE)));
        holder.tvDescription.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.FIELD_DESC)));
    }



    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    private class InnerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDescription;
        public TextView tvSumma;
        public TextView tvDate;

        public InnerViewHolder(View view) {
            super(view);

            tvDescription = (TextView) view.findViewById(R.id.tvNameItemExpenses);
            tvSumma = (TextView) view.findViewById(R.id.tvSummaItemExpenses);
            tvDate = (TextView) view.findViewById(R.id.tvDateItemExpenses);

        }
    }




}
