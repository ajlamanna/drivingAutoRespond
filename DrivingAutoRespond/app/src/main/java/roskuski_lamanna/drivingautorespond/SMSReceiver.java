package roskuski_lamanna.drivingautorespond;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.util.HashSet;
import java.util.Set;

public class SMSReceiver extends BroadcastReceiver {
    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        System.out.println(messages.length);

        Set<String> senders = new HashSet<String>();

        for(SmsMessage message : messages) {
            senders.add(message.getOriginatingAddress());
        }


        for(String s : senders){
            System.out.println(s);
            MainService.sendText(s, context);
        }
    }
}
