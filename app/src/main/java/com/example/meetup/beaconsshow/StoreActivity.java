package com.example.meetup.beaconsshow;

import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import java.util.List;



public class StoreActivity extends ActionBarActivity {



    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);

    private BeaconManager beaconManager = new BeaconManager(this);
    private static final String TAG = StoreActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        showNoItem();
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> rangedBeacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!(rangedBeacons.isEmpty())) {
                            // Just in case if there are multiple beacons with the same uuid, major, minor.
                            //the beacons in the list are ordered by proximity.
                            Beacon closestBeacon;
                            closestBeacon = rangedBeacons.get(0);
                            if (Utils.computeProximity(closestBeacon) == Utils.Proximity.IMMEDIATE ){
                                showItem(closestBeacon);
                            }else{
                                showNoItem();
                            }
                        }else{
                                showNoItem();
                        }

                    }
                });
            }
        });

    }

    private void showNoItem(){
        ImageView itemImageView = (ImageView) findViewById(R.id.itemStoreImageView);
        itemImageView.setImageResource(R.drawable.homer_looking);
    }

    private void showItem(Beacon beacon){
        ImageView itemImageView = (ImageView) findViewById(R.id.itemStoreImageView);
        switch (beacon.getMinor()){
            case 41156:
                itemImageView.setImageResource(R.drawable.homer_buda);
                break;
            case 60416:
                itemImageView.setImageResource(R.drawable.homer_love);
                break;
            case 40119:
                itemImageView.setImageResource(R.drawable.homer_sexy);
            break;

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Toast.makeText(StoreActivity.this, "Cannot start ranging",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }
}
