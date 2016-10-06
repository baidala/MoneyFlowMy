package ua.itstep.android11.moneyflow.provider;

import android.content.Context;
import org.junit.Test;

import ua.itstep.android11.moneyflow.db.DBHelper;

import static org.mockito.Mockito.mock;


/**
 * Created by oracle on 27/09/16.
 */
public class MyContentProviderTest {

    @Test
    public void testInsert() throws Exception {

    }

    @Test
    public void testQuery() throws Exception {
        Context context = mock(Context.class);
        DBHelper dbHelper = new DBHelper(context, 1);


    }
}