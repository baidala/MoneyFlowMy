package ua.itstep.android11.moneyflow.provider;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * Created by Maksim Baydala on 07/10/16.
 */

public class ExpensesObserver extends ContentObserver {
    public ExpensesObserver(Handler handler) {
        super(handler);
    }

       @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

    }
}
