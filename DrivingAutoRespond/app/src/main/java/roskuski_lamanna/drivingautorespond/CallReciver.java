package roskuski_lamanna.drivingautorespond;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallReciver extends BroadcastReceiver {
    public CallReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            System.out.println(number);
            MainService.sendText(number, context);
        }
    }
}
