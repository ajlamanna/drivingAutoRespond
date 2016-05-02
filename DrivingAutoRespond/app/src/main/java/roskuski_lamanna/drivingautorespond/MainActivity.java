package roskuski_lamanna.drivingautorespond;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.os.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public GoogleApiClient mApiClient;
    private static TextView switchStatus;
    private  Switch statSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS},
                    0);
        }

        switchStatus = (TextView) findViewById(R.id.status);
        statSwitch = (Switch) findViewById(R.id.switch1);

        statSwitch.setChecked(false);
        statSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b) {
                    statSwitch.setTextOn("Enabled");
                    statSwitch.setTextColor(Color.parseColor("#FF289520"));
                    switchStatus.setText("Getting Driving Status...");
                }else{
                    statSwitch.setTextOff("Disabled");
                    statSwitch.setTextColor(Color.parseColor("#FFFF3C44"));
                    switchStatus.setText("Currently Disabled, Please enable before driving.");
                }
            }
        });

        if(statSwitch.isChecked()){
            statSwitch.setTextOn("Enabled");
            statSwitch.setTextColor(Color.parseColor("#FF289520"));
            switchStatus.setText("Getting Driving Status...");
        }
        else{
            statSwitch.setTextOn("Disabled");
            statSwitch.setTextColor(Color.parseColor("#FFFF3C44"));
            switchStatus.setText("Currently Disabled, Please enable before driving.");
        }

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 3000, pendingIntent);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        for(Integer i : grantResults){
            if(i != PackageManager.PERMISSION_GRANTED){
                this.finish();
                return;
            }
        }
    }

    static Handler mHandler = new Handler();
    public static void activity(String actName){
        if(actName == "veh"){
            new Thread(new Runnable() {
                @Override
                public void run () {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run () {
                            switchStatus.setText("Currently Driving. Auto Respond is Active");
                        }
                    });
                }
            }).start();
        }

        if(actName == "not"){
            new Thread(new Runnable() {
                @Override
                public void run () {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run () {
                            switchStatus.setText("Currently not Driving. Auto Respond Disabled");
                        }
                    });
                }
            }).start();
        }
    }

}