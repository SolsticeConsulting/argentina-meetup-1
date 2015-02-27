package com.example.meetup.beaconsshow;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.connection.BeaconConnection;



public class EditActivity extends ActionBarActivity {

    public static final String EXTRA_BEACON_EDIT = "beaconExtraEdit";
    private BeaconConnection connection;
    private Beacon beacon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        beacon = getIntent().getParcelableExtra(EXTRA_BEACON_EDIT);
        connection = new BeaconConnection(this, beacon, createConnectionCallback());

    }



    private BeaconConnection.ConnectionCallback createConnectionCallback() {
        return new BeaconConnection.ConnectionCallback() {
            @Override public void onAuthenticated(final BeaconConnection.BeaconCharacteristics beaconChars) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        EditText uuidText = (EditText) findViewById(R.id.UUIDEditText);
                        EditText majorText = (EditText) findViewById(R.id.MajorTextBox);
                        EditText minorText = (EditText) findViewById(R.id.MinorTextBox);
                        EditText advertText = (EditText) findViewById(R.id.AdvertTextBox);
                        EditText broadcastText = (EditText) findViewById(R.id.BrodcastTextBox);
                        TextView batteryText = (TextView) findViewById(R.id.BatteryTextBox);
                        TextView nameText = (TextView) findViewById(R.id.NameTextBox);

                        uuidText.setText(beacon.getProximityUUID());
                        majorText.setText(String.valueOf(beacon.getMajor()));
                        minorText.setText(String.valueOf(beacon.getMinor()));
                        advertText.setText(String.valueOf(beaconChars.getAdvertisingIntervalMillis()));
                        broadcastText.setText(String.valueOf(beaconChars.getBroadcastingPower()));
                        batteryText.setText(String.valueOf(beaconChars.getBatteryPercent()));
                        nameText.setText(beacon.getName());

                    }
                });
            }

            @Override public void onAuthenticationError() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(getApplicationContext(),"unable to authenticate", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onDisconnected() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Log.d(EditActivity.class.getSimpleName(),"beacon disconnected");
                    }
                });
            }
        };
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (!connection.isConnected()) {
            connection.authenticate();
        }
    }

    @Override
    protected void onDestroy() {
        connection.close();
        super.onDestroy();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
