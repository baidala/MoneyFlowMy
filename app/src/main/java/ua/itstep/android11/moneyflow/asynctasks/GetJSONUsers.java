package ua.itstep.android11.moneyflow.asynctasks;

import android.app.Activity;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.util.Log;

import ua.itstep.android11.moneyflow.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Test on 13.04.2016.
 */

public class GetJSONUsers extends AsyncTask<String, Void, Cursor> {

    private Activity activity;
    private String email;
    private String password;

    public GetJSONUsers(Activity activity, String email, String password) {

    }

    @Override
    protected Cursor doInBackground(String... params) {
        JSONObject jsonObject = null;
        MatrixCursor matrixCursor  = new MatrixCursor(
                new String[]{
                        Prefs.FIELD_ID,
                        Prefs.FIELD_FIRST_NAME,
                        Prefs.FIELD_LAST_NAME,
                        Prefs.FIELD_BIRTHDAY,
                        Prefs.FIELD_EMAIL
                });
        Document document = null;
        try {
            document = Jsoup.connect(params[0]).get();
            Elements body = document.select("body");
            Element firstBody = body.first();
            String resElementText = firstBody.text();
            Log.d(Prefs.LOG_TAG, resElementText);

            JSONArray arrayUsersJSON = new JSONArray(resElementText);
            Log.d(Prefs.LOG_TAG, "Length - " + arrayUsersJSON.length());

            JSONObject localJson = null;
            for (int i = 0; i < arrayUsersJSON.length(); i++) {
                localJson = arrayUsersJSON.getJSONObject(i);
                localJson = localJson.getJSONObject("user_class");

                Object[] array = new Object[]{
                        localJson.getInt(Prefs.FIELD_ID),
                        localJson.getString(Prefs.FIELD_FIRST_NAME),
                        localJson.getString(Prefs.FIELD_LAST_NAME),
                        localJson.getString(Prefs.FIELD_BIRTHDAY),
                        localJson.getString(Prefs.FIELD_EMAIL)};
                Log.d(Prefs.LOG_TAG, Arrays.toString(array));

                matrixCursor.addRow(array);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return matrixCursor;
    }

}