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

        sms_from = intent.getExtras().getString("sms_from");
        sms_body = intent.getExtras().getString("sms_body");

        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand =>"+ sms_from );
        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand =>"+ sms_body );

        //showNotification(sms_from, sms_body);


        switch(sms_from) {
            case Prefs.SBERBANK_RF:
                Log.d(Prefs.LOG_TAG, "SmsService onStartCommand sms_from = SBERBANK_RF");
                parceSmsBody(Prefs.SBERBANK_RF);

                break;

        }



        return START_STICKY;
    }


    private void parceSmsBody(String bank) {
        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody bank="+ bank);
        String regexp = "";
        Pattern pattern;
        Matcher matcher;
        String summa = "0";
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());;
        String desc = "";
        String[] stringArray;
        boolean flag = false;

        switch( bank ) {
            case Prefs.SBERBANK_RF:

                regexp = Prefs.SBERBANK_OPLATA;
                pattern = Pattern.compile(regexp, Pattern.DOTALL);
                matcher = pattern.matcher(sms_body);
                if( matcher.find() ) {
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.matches Oplata=" + matcher.group());

                    stringArray = sms_body.split("\\n+");

                    //stringArray[0]=>28/12 11:48
                    //stringArray[1]=>Oplata=60 UAH
                    //stringArray[2]=>Karta 4524-6387 PEREVOD NA SCHET
                    //stringArray[3]=>Ostatok=14760.69 UAH.
                    //stringArray[4]=>Schastlivogo Novogo goda i Rozhdestva! Vash Sberbank!

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody stringArray=" + stringArray.length);

                    for(int i=0; i < stringArray.length && !flag; i++) {
                        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody stringArray["+i+"]=>" + stringArray[i].toString());

                        matcher = pattern.matcher( stringArray[i] );
                        if( matcher.find() ) {
                            Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.matches Oplata=stringArray["+i+"]=>"+ matcher.group());
                            flag = !flag;
                            summa = stringArray[i];
                            summa = summa.replaceAll( Prefs.SBERBANK_OPLATA_SPLIT, "");

                        }

                    }

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody summa=>" + summa);


                    regexp = Prefs.SBERBANK_DATE;  // DD/MM HH24:MI
                    pattern = Pattern.compile(regexp, Pattern.DOTALL);
                    flag = false;

                    for(int i=0; i < stringArray.length && !flag; i++) {
                        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody stringArray[" + i + "]=>" + stringArray[i].toString());

                        matcher = pattern.matcher( stringArray[i] );
                        if (matcher.find()) {
                            Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.matches date=" + matcher.group());

                            flag = !flag;
                            date = stringArray[i];
                            date.trim();
                        }
                    }

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody date=>" + date);


                    regexp = Prefs.SBERBANK_DESC ;  // Karta 4524-6387 PEREVOD NA SCHET SBERBANK ONLINE
                    pattern = Pattern.compile(regexp, Pattern.DOTALL);
                    flag = false;

                    for(int i=0; i < stringArray.length && !flag; i++) {
                        Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody stringArray[" + i + "]=>" + stringArray[i].toString());

                        matcher = pattern.matcher(stringArray[i]);
                        if (matcher.find()) {
                            Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.matches desc=" + matcher.group());

                            flag = !flag;
                            desc = stringArray[i];
                            desc.trim();
                            Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody desc=" + desc);
                        }
                    }

                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody desc=>" + desc);


                    saveSmsData(summa, desc, date, Prefs.EXPENSES);
                    break;

                } else {
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand ERROR Pattern.NOTMatche SBERBANK_OPLATA");

                }


                regexp = Prefs.SBERBANK_ZACHISLENIE;
                pattern = Pattern.compile(regexp, Pattern.DOTALL);
                matcher = pattern.matcher(sms_body);
                if( matcher.find() ) {
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand parceSmsBody SBERBANK_RF_ZACHISLENIE");
                    //TODO


                    //saveSmsData(summa, desc, date, Prefs.INCOMES);
                    break;

                } else {
                    Log.d(Prefs.LOG_TAG, "SmsService onStartCommand Pattern.NotMatche SBERBANK_RF_ZACHISLENIE");
                }



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
        //Notification notification = builder.build();  api 16

        //notificationManager.notify(R.mipmap.ic_launcher, notification);
    }


}
