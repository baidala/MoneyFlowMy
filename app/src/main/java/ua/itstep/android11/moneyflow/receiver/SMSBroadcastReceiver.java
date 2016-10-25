package ua.itstep.android11.moneyflow.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import ua.itstep.android11.moneyflow.service.SmsService;
import ua.itstep.android11.moneyflow.utils.Prefs;



public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    SmsMessage[] messages;
    String sms_from;
    String body;


    public SMSBroadcastReceiver() {
        Log.d(Prefs.LOG_TAG, "SMSBroadcastReceiver construct");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Prefs.LOG_TAG, "SMSBroadcastReceiver onReceive "+ intent.getAction());

        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            messages = new SmsMessage[pduArray.length];

            for (int i = 0; i < pduArray.length; i++) {
                 messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);  //for API 15
                //messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i], Telephony.Sms.Intents.SMS_RECEIVED_ACTION);  //for API 23
                Log.d(Prefs.LOG_TAG, "SMSBroadcastReceiver onReceive pduArray.length="+ pduArray.length );

            }
        }

        sms_from = messages[0].getDisplayOriginatingAddress();
        Log.d(Prefs.LOG_TAG, "SMSBroadcastReceiver onReceive DisplayOriginatingAddress="+ sms_from );

        if (sms_from.equalsIgnoreCase(Prefs.SBERBANK_RF)) {
            StringBuilder bodyText = new StringBuilder();

            for (int i = 0; i < messages.length; i++) {
                bodyText.append(messages[i].getMessageBody());
                Log.d(Prefs.LOG_TAG, "SMSBroadcastReceiver onReceive messages[" +i+"] ="+ messages[i].getMessageBody() );
            }
            body = bodyText.toString();

            Intent mIntent = new Intent(context, SmsService.class);
            mIntent.putExtra("sms_from", sms_from);
            mIntent.putExtra("sms_body", body);


            context.startService(mIntent);

            abortBroadcast();
        }

    }
}
