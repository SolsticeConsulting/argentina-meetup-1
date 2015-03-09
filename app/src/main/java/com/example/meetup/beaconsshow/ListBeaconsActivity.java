package com.example.meetup.beaconsshow;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.utils.L;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class ListBeaconsActivity extends ActionBarActivity {


    //We define a region to monitor, this region could be use the UUID only to look for a specific Brand of retail store
    //for example, or we can pass also major and minor values in case we need to be more specific
    //if we don't pass any parameter will look for any beacon, works like a wildcard.

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);


    private final int BLUETHOOT_REQUEST_CODE = 1234;

    //We create a new beacon manager that will help managing the beacons search
    private BeaconManager beaconManager = new BeaconManager(this);
    private static final String TAG = ListBeaconsActivity.class.getSimpleName();
    private  BeaconListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_beacons);

        adapter = new BeaconListAdapter(this);
        ListView listView = (ListView) findViewById(R.id.listViewBeacons);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        L.enableDebugLogging(true);

        //We can set up the scan interval to save battery and cpu cycles, since this is for show propose
        // we set it to scan immediately
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);
        beaconManager.setForegroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override public void onBeaconsDiscovered(Region region,final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.

                        adapter.replaceWith(beacons);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });


    }

    private AdapterView.OnItemClickListener createOnItemClickListener(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListBeaconsActivity.this,DetailsActivity.class);
                i.putExtra(DetailsActivity.EXTRA_BEACON,adapter.getItem(position));
                startActivity(i);
            }
        };
    }


    @Override
    public void onStart(){
        super.onStart();

        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(enableBtIntent, BLUETHOOT_REQUEST_CODE);
        }

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });


    }

    @Override
    public void onStop(){
        super.onStop();
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // We disconnect since we no longer need the connection.
        beaconManager.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_beacons, menu);
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
}
