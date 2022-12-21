package com.nullpointertech.cLANConnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Author: Cameron Hozouri
 * This class creaetes a custom info window on the markers created in the map
 * fragment, shows info for the user to see what type of party it is
 */
public class CustomPartyWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;

    private Context mContext;

    public CustomPartyWindowAdapter(Context context)
    {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.lan_party_info_window, null);
    }

    /**
     * RenderWindow()
     * Binds all the data info into the custom window xml
     * @param marker
     * @param view
     */
    private void RenderWindow(Marker marker, View view)
    {
        String title = marker.getTitle();
        String stuff = marker.getSnippet();
        String[] info = stuff.split("//");
        TextView titleView = (TextView)view.findViewById(R.id.nameinfo);
        titleView.setText(title);
        TextView platform = (TextView)view.findViewById(R.id.platforminfo);
        platform.setText("Platform: " + info[0]);
        TextView games = (TextView)view.findViewById(R.id.gamesinfo);
        games.setText("Games Being Played: " + info[1]);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        RenderWindow(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        RenderWindow(marker, mWindow);
        return mWindow;
    }
}
