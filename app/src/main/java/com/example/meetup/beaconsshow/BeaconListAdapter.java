package com.example.meetup.beaconsshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estimote.sdk.Beacon;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by AgustinScolieri on 2/22/15.
 */
public class BeaconListAdapter extends BaseAdapter{

    private  ArrayList<Beacon> values = new ArrayList<>();
    private final Context context;


    public BeaconListAdapter(Context context){

        this.context = context;
        this.values = new ArrayList<>();
    }

    public void replaceWith(Collection<Beacon> newBeacons) {
        this.values.clear();
        this.values.addAll(newBeacons);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Beacon getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        BeaconViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.beacon_item, parent, false);
            viewHolder = new BeaconViewHolder(v);
            v.setTag(viewHolder);

        }else{
            viewHolder = (BeaconViewHolder) v.getTag();
        }
        viewHolder.UUIDTextView.setText(values.get(position).getProximityUUID());
        viewHolder.MajorTextView.setText(String.valueOf(values.get(position).getMajor()));
        viewHolder.MinorTextView.setText(String.valueOf(values.get(position).getMinor()));

        return v;
    }


   static class BeaconViewHolder{
            final TextView UUIDTextView;
            final TextView MajorTextView;
            final TextView MinorTextView;
        BeaconViewHolder(View view){
            UUIDTextView = (TextView) view.findViewById(R.id.beaconUUID);
            MajorTextView = (TextView) view.findViewById(R.id.beaconMajor);
            MinorTextView = (TextView) view.findViewById(R.id.beaconMinor);
        }
    }
}
