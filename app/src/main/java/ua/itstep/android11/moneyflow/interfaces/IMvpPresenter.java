package ua.itstep.android11.moneyflow.interfaces;

/**
 * Created by Maksim Baydala on 09/03/17.
 */

public interface IMvpPresenter<V> {

    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}
