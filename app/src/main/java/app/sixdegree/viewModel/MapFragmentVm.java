package app.sixdegree.viewModel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.databinding.Bindable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.sixdegree.BR;
import app.sixdegree.R;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.searchfriends.Datum;
import app.sixdegree.network.responses.searchfriends.SearchFriendsRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class MapFragmentVm extends BaseVm {
    //lat lng list
    public BehaviorSubject<List<LatLng>> latLngList = BehaviorSubject.create();

    public BehaviorSubject<GoogleMap> googleMapObserver = BehaviorSubject.create();
    public BehaviorSubject<Boolean> mapclear = BehaviorSubject.create();
    public BehaviorSubject<Datum> friendsdata = BehaviorSubject.create();
    public BehaviorSubject<List<Datum>> friendsDatalist = BehaviorSubject.create();
    Marker mymarker;
    @Bindable
    boolean displayMap = false;
    @Bindable
    String coverPic;
    MapView mapView;
   public GoogleMap googleMap;
    @Bindable
    String placename = "";
    @Bindable
    String latitude = "0.0";
    @Bindable
    String longitude = "0.0";


    public String getUserCurrentLat() {
        return userCurrentLat;
    }

    public void setUserCurrentLat( String userCurrentLat ) {
        this.userCurrentLat = userCurrentLat;
        notifyPropertyChanged( BR.userCurrentLat );
    }

    public String getUserCurrentLng() {
        return userCurrentLng;
    }

    public void setUserCurrentLng( String userCurrentLng ) {
        this.userCurrentLng = userCurrentLng;
        notifyPropertyChanged( BR.userCurrentLng );
    }

    @Bindable
    String userCurrentLat = "0.0";
    @Bindable
    String userCurrentLng = "0.0";
    @Bindable
    boolean loading;
    @Bindable
    String rangeunit = "";
    @Bindable
    String range = "2000";
    String token = "";
    ArrayList<LatLng> localLatLngList = new ArrayList<>();
    ArrayList<String> imagesurl = new ArrayList<>();
    ArrayList<Datum> friendsList = new ArrayList<>();
    AppSession appSession;
    TextView tv;

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
        notifyPropertyChanged( BR.id );
    }

    @Bindable
            String id="";

    Circle circle;
    //constructor
    public MapFragmentVm(String token, String radiusunit, AppSession appSession) {
        this.rangeunit = radiusunit;
        this.token = token;
        this.appSession = appSession;
    }

    public String getCoverPic() {
        String imageurl = "";
        for (int i = 0; i < friendsList.size(); i++) {
            imageurl = friendsList.get(i).getProfileImage();
        }

        Log.e("imageurl--", "--" + imageurl);
        return imageurl;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
        notifyPropertyChanged(BR.coverPic);
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
        notifyPropertyChanged(BR.placename);
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.longitude);
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public String getRangeunit() {
        return rangeunit;
    }

    public void setRangeunit(String rangeunit) {
        this.rangeunit = rangeunit;
        notifyPropertyChanged(BR.rangeunit);
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
        notifyPropertyChanged(BR.range);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void sendFriendRequest( View view,String id ) {
        setLoading(true);



        apiService.sendFriendRequest(appSession.getToken(), id).subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {
                        setErrorMsg(res.getMessage());

                        if (res.getStatus()) {
                            //  setUserStatus( res.getMessage() );
                            setErrorMsg( res.getMessage() );

                            //   setAdded( true );
                        }
                        else {
                            // setAdded( false );
                            Toast.makeText( view.getContext(), res.getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                    }

                    @Override
                    public void onComplete() {

                        setLoading(false);
                    }
                });
    }

    public void showFriends( View view,String latitude, String longitude,String range) {




    HashMap<String, String> map = new HashMap<>();
    map.put("latitude", latitude);
    map.put("longitude",longitude);
    map.put("range", range);
    map.put("unit", getRangeunit());


    setLoading(true);
    apiService.searchFriendByLocation(token, map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<SearchFriendsRes>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(SearchFriendsRes data) {
                    friendsList.clear();
                      if (data.getStatus()) {

                          for (int i = 0; i < data.getData().size(); i++) {
                              if (data.getData().get(i).getLatitude()!=null){
                                  friendsDatalist.onNext( data .getData() );
                              }
                          }



                        ArrayList<LatLng> arrayList = new ArrayList<>();
                        for (int i = 0; i < data.getData().size(); i++) {


                            friendsList.add(data.getData().get(i));
                            setCoverPic(data.getData().get(i).getProfileImage());

                            if (data.getData().get(i).getLatitude() != null) {
                                arrayList.add(new LatLng(Double.parseDouble(data.getData().get(i).getLatitude()),
                                        Double.parseDouble(data.getData().get(i).getLongitude())));
                            }
//                                tv.setText(String.valueOf(localLatLngList.size()));
                            imagesurl.add(data.getData().get(i).getProfileImage());
                            latLngList.onNext(arrayList);
                            localLatLngList.addAll(arrayList);





                        }

                        if(data.getData().size()==0){
                            mapclear.onNext( true );
                            Toast.makeText( view.getContext(), "No Friends Found !", Toast.LENGTH_SHORT ).show();
                        }else {
                            mapclear.onNext( false );
                        }
                        Log.e("data.getData--", "--" + data.getData().size());

                    } else {
                        setErrorMsg(data.getMessage());
                    }

                    setLoading(false);
                    notifyChange();

                }

                @Override
                public void onError(Throwable e) {
                    setLoading(false);


                }

                @Override
                public void onComplete() {
                    setLoading(false);

                }
            });


    }
    public void searchFriends(View view) {


if(getPlacename().equals( "" )){
    Toast.makeText( view.getContext(), "Please enter location name", Toast.LENGTH_SHORT ).show();
}else {

    HashMap<String, String> map = new HashMap<>();
    map.put("latitude", getLatitude());
    map.put("longitude", getLongitude());
    map.put("range", getRange());
    map.put("unit", getRangeunit());


    setLoading(true);
    apiService.searchFriendByLocation(token, map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<SearchFriendsRes>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(SearchFriendsRes data) {
                    friendsList.clear();
                      if (data.getStatus()) {
                        friendsDatalist.onNext( data .getData() );

                        ArrayList<LatLng> arrayList = new ArrayList<>();
                        for (int i = 0; i < data.getData().size(); i++) {


                            friendsList.add(data.getData().get(i));
                            setCoverPic(data.getData().get(i).getProfileImage());

                            if (data.getData().get(i).getLatitude() != null) {
                                arrayList.add(new LatLng(Double.parseDouble(data.getData().get(i).getLatitude()),
                                        Double.parseDouble(data.getData().get(i).getLongitude())));
                            }
//                                tv.setText(String.valueOf(localLatLngList.size()));
                            imagesurl.add(data.getData().get(i).getProfileImage());
                            latLngList.onNext(arrayList);
                            localLatLngList.addAll(arrayList);





                        }

                        if(data.getData().size()==0){
                            mapclear.onNext( true );
                            Toast.makeText( view.getContext(), "No Friends Found !", Toast.LENGTH_SHORT ).show();
                        }else {
                            mapclear.onNext( false );
                        }
                        Log.e("data.getData--", "--" + data.getData().size());

                    } else {
                        setErrorMsg(data.getMessage());
                    }

                    setLoading(false);
                    notifyChange();

                }

                @Override
                public void onError(Throwable e) {
                    setLoading(false);


                }

                @Override
                public void onComplete() {
                    setLoading(false);

                }
            });
}


    }
    ImageView imgbig;
//    public void setMapView(MapView mapView) {
//        this.mapView = mapView;
//        mapView.onCreate(new Bundle());
//        mapView.onResume();
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                setGoogleMap(googleMap);
//                googleMapObserver.onNext(googleMap);
//
//                View markerView1 = ((LayoutInflater) mapView.getContext()
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_withtext, null);
//                 new CustomMarker(appSession.getData().getProfileImage(), new LatLng(Double.parseDouble(getLatitude()), Double.parseDouble(getLongitude())), markerView1,"","",null).execute();
//
////                createCustomMarker(mapView.getContext(),appSession.getData().getProfileImage(),"",
////                        new LatLng(Double.parseDouble(  getLatitude()),Double.parseDouble( getLongitude() )),googleMap);
////
//                Toast.makeText( mapView.getContext(), "--"+getLatitude()+","+getLongitude(), Toast.LENGTH_SHORT ).show();
//
//                for (int i = 0; i < friendsList.size(); i++) {
//                    View markerView = ((LayoutInflater) mapView.getContext()
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
//                    imgbig=markerView.findViewById( R.id.imgbig );
//
//
//                new CustomMarker(friendsList.get(i).getProfile_image(), new LatLng(
//                           Double.parseDouble( friendsList.get(i).getLatitude()),Double.parseDouble(friendsList.get(i).getLongitude()
//                    )), markerView,friendsList.get( i ).getName(),String.valueOf(friendsList.get( i ).getId()),friendsList.get( i )).execute();
//
//
//                    boolean success = googleMap.setMapStyle(
//                            MapStyleOptions.loadRawResourceStyle(
//                                    mapView.getContext(), R.raw.map_style));
//
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(localLatLngList.get(i), 14);
//                    googleMap.animateCamera(cameraUpdate);
//drawCircle( localLatLngList.get( i ),googleMap, Integer.parseInt(  getRange() ));
//
///*
//                    MarkerOptions m = AppUtils.createMarker(mapView.getContext(), new LatLng(localLatLngList.get(i).latitude, localLatLngList.get(i).longitude));
//                    googleMap.addMarker(m);*/
//                }
//
//
//
//
//                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick( Marker marker) {
//
//
//                        Toast.makeText(mapView.getContext(), "clicked", Toast.LENGTH_SHORT ).show();
//                      //   rlFriendStaus.setVisibility(View.VISIBLE );
//                       Data data = (Data) marker.getTag();
//                        Toast.makeText(mapView.getContext(), "clickedpos-"+data.getId(), Toast.LENGTH_SHORT ).show();
//                        //Using position get Value from arraylist
//                        friendsdata.onNext( data );
//
//
//                        if(marker.equals( mymarker )){
//
//                            Log.d("position",""+ marker.getPosition());
//                            imgbig.setVisibility( View.VISIBLE );
//                        }else {
//                            imgbig.setVisibility( View.GONE );
//                        }
//
//
////                          if(marker.getPosition().equals(new
////                                  LatLng(  Double.parseDouble(  data.getLatitude()),Double.parseDouble(data.getLongitude()))))
////                          {
////                              imgbig.setVisibility( View.VISIBLE );
////                          }
//                        return true;
//                    }
//                });
//
//            }
//        });
//
//
//
//
//    }

    private void drawCircle(LatLng point, GoogleMap googleMap, int radius) {

        if (circle != null) {
            circle.remove();
        }
        circle = googleMap.addCircle(new CircleOptions()
                .center(point)
                .radius(radius)
                .strokeWidth(1)
                .strokeColor(Color.GREEN)
                .fillColor(Color.WHITE));

    }
//    public class CustomMarker extends AsyncTask<String, Void, Bitmap> {
//
//        String link;
//        LatLng latLng;
//        String title;
//        String id;
//        View markerView;
//        Data data;
//
//
//        public CustomMarker(String link, LatLng latLng, View markerView,String title,String id,Data data) {
//            this.link = link;
//            this.latLng = latLng;
//            this.id = id;
//            this.markerView = markerView;
//            this.title = title;
//            this.data=data;
//
//
//
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            Bitmap bmp = null;
//
//            try {
//                URL url = new URL(link); //url of the image parser from the xml ("http://research.cbei.psu.edu/images/uploads/research-digest/general/1.CityIcon.png")
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setDoInput(true);
//                conn.connect();
//                InputStream is = conn.getInputStream();
//                bmp = BitmapFactory.decodeStream(is);
//                bmp = AppUtils.getCircularBitmap(bmp);
//
//
//
//
//            } catch (IOException e) {
//                return bmp;
//
//            }
//            return bmp;
//        }
//
//
//
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            if (bitmap != null) {
//                ImageView image = markerView.findViewById(R.id.img);
//                ImageView imgbig = markerView.findViewById(R.id.imgbig);
//              image.setImageBitmap(bitmap);
//
//
////                Glide.with(mapView.getContext())
////                        .load(bitmap)
////                       .centerCrop()
////                        .apply(new RequestOptions().override( 150, 150))
////                        .apply( RequestOptions.circleCropTransform())
////                        .into(image);
//
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
//                markerView.setLayoutParams(layoutParams);
//  mymarker=              googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(AppUtils.loadBitmapFromView( markerView ))));
//                mymarker.setTag( data );
//            }
//
//        }
//    }
//



    public   Bitmap createCustomMarker(Context context,String url, String _name,LatLng latLng,GoogleMap map) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_withtext, null);

        ImageView markerImage = (ImageView) marker.findViewById(R.id.img);


        Glide.with(context)
                .load(url)
                .centerCrop()
                .apply(new RequestOptions().override( 150, 150))
                .apply( RequestOptions.circleCropTransform())
                .into(markerImage);



         TextView txt_name = (TextView)marker.findViewById(R.id.tv_friend_count);
        txt_name.setText(_name);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(150, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

        return bitmap;
    }

}
