package ua.itstep.android11.moneyflow.interfaces;

import java.util.List;

import ua.itstep.android11.moneyflow.models.Record;


/**
 * Created by Maksim Baydala on 09/03/17.
 */

public interface IMvpView {

    void showItems(List<Record> items);
}
