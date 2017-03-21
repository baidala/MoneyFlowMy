package ua.itstep.android11.moneyflow.interfaces;

/**
 * Created by Maksim Baydala on 10/03/17.
 */

/**
 * Creates a IMvpPresenter object.
 * @param <T> presenter type
 */

public interface IPresenterFactory<T extends IMvpPresenter> {
    T create();
}
