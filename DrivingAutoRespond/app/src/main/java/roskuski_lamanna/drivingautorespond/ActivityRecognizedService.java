package roskuski_lamanna.drivingautorespond;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Layout;
import android.util.Log;


import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;


import java.util.List;
import java.util.logging.LogRecord;



public class ActivityRecognizedService extends IntentService{

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }
    



    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }


    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {

                        MainService.setActivity(1, this);

                        //NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        //builder.setContentText("Are you in a Vehicle");
                        //builder.setSmallIcon(R.mipmap.ic_launcher);
                        //builder.setContentTitle(getString(R.string.app_name));
                        //NotificationManagerCompat.from(this).notify(0, builder.build());
                    } else {
                        MainService.setActivity(0, this);
                    }
                    break;
                }


            }
        }
    }
}
