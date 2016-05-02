package roskuski_lamanna.drivingautorespond;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class MainService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_ENABLE = "roskuski_lamanna.drivingautorespond.action.setEnabled";
    public static final String ACTION_ACTIVITY = "roskuski_lamanna.drivingautorespond.action.setActivity";

    public static final String ACTION_TEXT = "roskuski_lamanna.drivingautorespond.action.sendText";

    public static final String EXTRA_PARAM1 = "roskuski_lamanna.drivingautorespond.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "roskuski_lamanna.drivingautorespond.extra.PARAM2";

    public MainService() {
        super("MainService");
    }

    public static void setEnabled(int enabled, Context ctx){
        Intent i = new Intent(ctx, MainService.class);
        i.setAction(ACTION_ENABLE);
        i.putExtra(EXTRA_PARAM1, enabled);
        ctx.startService(i);
    }

    public static void setActivity(int enabled, Context ctx){
        Intent i = new Intent(ctx, MainService.class);
        i.setAction(ACTION_ACTIVITY);
        i.putExtra(EXTRA_PARAM1, enabled);
        ctx.startService(i);
    }

    public static void sendText(String address, Context ctx){
        Intent i = new Intent(ctx, MainService.class);
        i.setAction(ACTION_TEXT);
        i.putExtra(EXTRA_PARAM1, address);
        ctx.startService(i);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ENABLE.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_PARAM1, 0);
                handleActionEnable(param1);
            } else if (ACTION_ACTIVITY.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_PARAM1, 0);
                handleActionActivity(param1);
            }  else if (ACTION_TEXT.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                sendText(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionEnable(int param1) {
        Database.getInstance().updateVal(this, "pref", param1);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionActivity(int param1) {
        Database.getInstance().updateVal(this, "act", param1);
    }

    private void sendText(String address){
        if(shouldSend()) {
            System.out.println("Sending message to" + address);
            new Message(address, "The person you're trying to contact is currently driving, and should not be distracted.").send();
        }
    }

    private boolean shouldSend(){
        return Database.getInstance().getVal(this, "pref") == 0;
    }


}
