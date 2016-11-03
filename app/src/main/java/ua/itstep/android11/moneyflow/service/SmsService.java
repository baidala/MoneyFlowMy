package ua.itstep.android11.moneyflow.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.itstep.android11.moneyflow.R;
import ua.itstep.android11.moneyflow.activities.DashboardActivity;
import ua.itstep.android11.moneyflow.utils.Prefs;

public class SmsService extends Service {

    String sms_body;
    String sms_from;
    double summa = 0f;
    String desc = "null";
    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public SmsService() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand "+ intent.getAction());
        String regexp = "";
        Pattern pattern;
        Matcher matcher;

        sms_from = intent.getExtras().getString("sms_from");
        sms_body = intent.getExtras().getString("sms_body");

        //showNotification(sms_from, sms_body);

        switch(sms_from) {
            case Prefs.SBERBANK_RF:
                regexp = "Oplata=[0-9]{2,7}.[0-9]{0,2} UAH";
                pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(sms_body);
                if( matcher.find() ) {
                    parceSmsBody(Prefs.SBERBANK_RF_OPLATA);
                    break;
                }

                regexp = "Zachislenie: UAH [0-9,]{1,20}.[0-9]{0,2}";
                pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(sms_body);
                if( matcher.find() ) {
                    parceSmsBody(Prefs.SBERBANK_RF_ZACHISLENIE);
                    break;
                }


                break;
        }



        //saveSmsData(summa, desc, date);

        return START_STICKY;
    }

    private void parceSmsBody(int bankOperation) {
        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody="+ bankOperation);
        String regexp = "";
        Pattern pattern;
        Matcher matcher;
        String summa = "";
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());;
        String desc = "";
        StringBuffer buffer;
        String[] stringArray;

        switch( bankOperation ) {
            case Prefs.SBERBANK_RF_OPLATA:

                regexp = "Oplata=[0-9]{2,7}.[0-9]{0,2} UAH";
                pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(sms_body);
                if( matcher.find() ) {
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.matches Oplata=" + matcher.group());

                    summa = matcher.group();
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody summa=" + summa);
                    stringArray = summa.split("Oplata=| UAH");

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody stringArray=" + stringArray.length);
                    for(int i=0; i < stringArray.length; i++) {
                        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody stringArray["+i+"=|" + stringArray[i].toString());
                    }

                    summa = stringArray[stringArray.length - 1];
                } else {

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.NotMatche SUMMA");
                    break;
                }

                regexp = "^[0-9][0-9]/[0-9][0-9] [0-9][0-9]:[0-9][0-9]";  // DD/MM HH24:MI
                pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(sms_body);
                if( matcher.find() ) {
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.matches date=" + matcher.group());

                    date = matcher.group();
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody date=" + date);

                } else {

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.NotMatches DATE");
                    break;
                }

                regexp = "[0-9][0-9][0-9][0-9][\\w\\s]{1,30}Ostatok";  // 6387 SBERBANK ONLINE UAH Ostatok
                pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(sms_body);
                if( matcher.find() ) {
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.matches desc=" + matcher.group());

                    desc = matcher.group();
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody desc=" + desc);

                    stringArray = desc.split("[0-9][0-9][0-9][0-9] | Ostatok");

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody stringArray=" + stringArray.length);
                    for(int i=0; i < stringArray.length; i++) {
                        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody stringArray["+i+"=|" + stringArray[i].toString());
                    }



                    desc = stringArray[stringArray.length - 1];

                } else {

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.NotMatche DESC:" + matcher.toString());
                    break;
                }


                saveSmsData(summa, desc, date, Prefs.EXPENSES);
                break;

            case Prefs.SBERBANK_RF_ZACHISLENIE:


                saveSmsData(summa, desc, date, Prefs.INCOMES);
                break;
        }
        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody switch end.");


    }



    private void saveSmsData(String summa, String desc, String date, int category) {
        ContentValues values = new ContentValues();
        values.put(Prefs.FIELD_SUMMA, summa);
        values.put(Prefs.FIELD_DESC, desc);
        values.put(Prefs.FIELD_DATE, date);

        switch (category) {
            case Prefs.EXPENSES:
                getContentResolver().insert(Prefs.URI_EXPENSES, values);
                break;

            case Prefs.INCOMES:
                getContentResolver().insert(Prefs.URI_INCOMES, values);
                break;
        }

    }

    private void showNotification(String from, String text) {
        Log.d(Prefs.LOG_TAG, "SmsService showNotification" + from);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, DashboardActivity.class), 0);
        Context context = getApplicationContext();

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(from)
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();

        notificationManager.notify(R.mipmap.ic_launcher, notification);
    }


}
