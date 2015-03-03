package com.example.meetup.beaconsshow;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class MonitoringActivity extends ActionBarActivity {


    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
    private NotificationManager notificationManager;
    private BeaconManager beaconManager;
    private final int NOTIFICATION_ID = 234;
    private final String TAG = MonitoringActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);


        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        beaconManager = new BeaconManager(this);

        // Default values are 5s of scanning and 25s of waiting time to save CPU cycles.
        // In order for this demo to be more responsive and immediate we lower down those values.
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                postNotification("Entered region");
            }

            @Override
            public void onExitedRegion(Region region) {
                postNotification("Exited region");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_monitoring, menu);
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
    protected void onResume() {
        super.onResume();

        notificationManager.cancel(NOTIFICATION_ID);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Log.d(TAG, "Error while starting monitoring");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        notificationManager.cancel(NOTIFICATION_ID);
        beaconManager.disconnect();
        super.onDestroy();
    }

    private void postNotification(String message){


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(message)
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_ALL);


        Intent resultIntent = new Intent(this, MonitoringActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivities(
                MonitoringActivity.this,
                0,
                new Intent[]{resultIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }

}
