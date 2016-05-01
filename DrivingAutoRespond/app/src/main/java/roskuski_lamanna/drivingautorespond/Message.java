package roskuski_lamanna.drivingautorespond;

import android.content.Context;
import android.telephony.SmsManager;

/**
 * Created by andrew on 5/1/16.
 */
public class Message {

    private final String text;

    private final String toAddr;

    public Message (String toAddr, String text){
        this.text = text;
        this.toAddr = toAddr;
    }


    public void send(){
        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(toAddr, null, text, null, null);
    }


}
