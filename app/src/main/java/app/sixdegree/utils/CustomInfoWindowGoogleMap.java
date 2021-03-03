package app.sixdegree.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import app.sixdegree.R;
import app.sixdegree.network.responses.searchfriends.Datum;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow( Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
.inflate( R.layout.map_marker, null);


        ImageView img = view.findViewById(R.id.img);
        ImageView bgimg = view.findViewById(R.id.imgbig);


        Datum infoWindowData = (Datum) marker.getTag();




        Glide.with(context).load(  infoWindowData.getProfileImage())
                .apply(new RequestOptions().override(100 * 100))
                .apply( RequestOptions.circleCropTransform())
                .into(img);



        return view;
    }
}
 