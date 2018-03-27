package com.medinamobile.hackatonbcn.main.list;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.medinamobile.hackatonbcn.R;
import com.medinamobile.hackatonbcn.entities.Station;
import com.medinamobile.hackatonbcn.utils.MyUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erick on 3/11/2018.
 */

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.ViewHolder> {

    private final Location mLocation;
    private final StationsCallbacks mCallbacks;
    private ArrayList<Station> mStations;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;

    public interface StationsCallbacks{
        void onStationClicked(Station station);
    }

    public StationsAdapter(ArrayList<Station> mStations, StationsCallbacks callbacks, Location location) {
        this.mStations = mStations;
        this.mLocation = location;
        this.mCallbacks = callbacks;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_layout_short, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Context mContext = holder.itemView.getContext();
        final Station station = mStations.get(position);
        holder.name.setText(station.getName());
        holder.bikes.setText(station.getBikesString(mContext));
        holder.slots.setText(station.getSlotsString(mContext));
        holder.status.setText(station.getStatusComplete(mContext));
        if (mLocation!=null) {
            holder.distance.setText(MyUtils.getDistanceString(mContext, mLocation, station));
        }
        /*
        final boolean isExpanded = position==mExpandedPosition;
        holder.additionalLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onStationClicked(station);
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return mStations==null?0:mStations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.detail_station_name)
        TextView name;
        @BindView(R.id.detail_distance)
        TextView distance;
        @BindView(R.id.detail_status)
        TextView status;
        @BindView(R.id.detail_bikes)
        TextView bikes;
        @BindView(R.id.detail_slots)
        TextView slots;

        @Nullable @BindView(R.id.detail_description)
        TextView description;
        @Nullable @BindView(R.id.detail_graph)
        GraphView graph;
        @Nullable @BindView(R.id.detail_additional_layout)
        LinearLayout additionalLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
