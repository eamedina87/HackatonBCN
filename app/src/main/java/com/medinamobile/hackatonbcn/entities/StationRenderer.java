package com.medinamobile.hackatonbcn.entities;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.medinamobile.hackatonbcn.R;

/**
 * Created by Erick on 3/10/2018.
 */

public class StationRenderer extends DefaultClusterRenderer<StationClusterItem> {

    private final Context mContext;

    public StationRenderer(Context context, GoogleMap map, ClusterManager<StationClusterItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(StationClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        Station station = item.getStation();



        IconGenerator iconGenerator = new IconGenerator(mContext);
        iconGenerator.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.marker_background));
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_marker, null,false);
        ((ImageView)view.findViewById(R.id.marker_image)).setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView)view.findViewById(R.id.marker_text)).setText(station.getOcuppancy());
        iconGenerator.setContentView(view);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()));
    }
}
