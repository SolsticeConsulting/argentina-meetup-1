package com.example.meetup.beaconsshow;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.estimote.sdk.Beacon;

import org.w3c.dom.Text;


public class DetailsActivity extends ActionBarActivity {

    public static final String EXTRA_BEACON = "beaconExtraData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Beacon b = getIntent().getParcelableExtra(EXTRA_BEACON);
        SetUpDetailData(b);


    }

    private void SetUpDetailData(Beacon b){
        TextView uuid = (TextView) findViewById(R.id.UUIDTextBox);
        TextView major = (TextView) findViewById(R.id.MajorTextBox);
        TextView minor = (TextView) findViewById(R.id.MinorTextBox);
        TextView rssi = (TextView) findViewById(R.id.RSSRangeTextBox);
        TextView name = (TextView) findViewById(R.id.NameTextBox);

        uuid.setText(b.getProximityUUID());
        major.setText(String.valueOf(b.getMajor()));
        minor.setText(String.valueOf(b.getMinor()));
        rssi.setText(String.valueOf(b.getRssi()));
        name.setText(String.valueOf(b.getName()));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);



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
