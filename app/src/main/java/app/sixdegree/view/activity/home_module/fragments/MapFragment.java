package app.sixdegree.view.activity.home_module.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import app.sixdegree.R;
import app.sixdegree.databinding.FragmentMapViewBinding;
import app.sixdegree.network.responses.getfriendslisting.Data;
import app.sixdegree.network.responses.searchfriends.Datum;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.utils.CustomInfoWindowGoogleMap;
import app.sixdegree.view.ThisApp;
import app.sixdegree.view.activity.chatchat.ChatDetails;
import app.sixdegree.view.activity.home_module.HomeActivity;
import app.sixdegree.viewModel.MapFragmentVm;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.WINDOW_SERVICE;

public class MapFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    FragmentMapViewBinding fragmentMapViewBinding;
    PowerMenu powerMenu;
    AppSession appSession;
    MapFragmentVm mapFragmentVm;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<LatLng> latLngList;
    MapView mapView_;
    String relation = "";
    List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    int height=0;
int width=0;
    List<Datum> friendsList = new ArrayList<>();
    Datum friendData;
    View markerView;

    GoogleMap map;
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            String textSelected = item.getTitle();

            mapFragmentVm.setRange(textSelected);
            mapFragmentVm.showFriends(getView(), mapFragmentVm.getLatitude(), mapFragmentVm.getLongitude(), mapFragmentVm.getRange());
            powerMenu.dismiss();
        }


    };
    SharedPreferences sharedPreferences;
    List<Data> frienddata = new ArrayList<>();

    public MapFragment(List<Data> data) {
        this.frienddata = data;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMapViewBinding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_map, container, false));
        appSession = new AppSession(getActivity());
        mapFragmentVm = new MapFragmentVm(appSession.getToken(), appSession.getData().getRadiusUnit(), appSession);
        fragmentMapViewBinding.setViewModel(mapFragmentVm);

        sharedPreferences = getContext().getSharedPreferences("LocaPref", Context.MODE_PRIVATE);

        View view = fragmentMapViewBinding.getRoot();

        mapView_ = view.findViewById(R.id.map);
        RelativeLayout rlFriendStaus = view.findViewById(R.id.rlFriendStaus);
        TextView tvName = view.findViewById(R.id.tvName);
        ImageView iv_message = view.findViewById(R.id.iv_message);
        ImageView iv_add_friend = view.findViewById(R.id.iv_add_friend);
        TextView tvMutualFriendsCount = view.findViewById(R.id.tvMutualFriendsCount);
        TextView tvTravellingStatus = view.findViewById(R.id.tvTravellingStatus);
        ImageView ivSendGoTo = view.findViewById(R.id.ivSendGoTo);


        fragmentMapViewBinding.edLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlaceAutoComplete();
            }
        });

        // get screen height width

        WindowManager wm = (WindowManager) requireActivity().getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
          height = displayMetrics.heightPixels;
          width = displayMetrics.widthPixels;

        //show range list in powermenu
        fragmentMapViewBinding.ivRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new ArrayList<>();
                list.add("100");
                list.add("500");
                list.add("1000");
                list.add("2000");
                list.add("4000");
                list.add("6000");
                list.add("7000");
                list.add("8000");
                list.add("10000");
                show(list, fragmentMapViewBinding.ivRange);
            }
        });

//


        fragmentMapViewBinding.map.onCreate(new Bundle());
        fragmentMapViewBinding.map.onResume();
        fragmentMapViewBinding.map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.map_style));
                markerView = ((LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_withtext, null);


                profileimg = markerView.findViewById(R.id.img);
                tv_friend_count = markerView.findViewById(R.id.tv_friend_count);


                String lat = sharedPreferences.getString("lat", "0.0");
                String lng = sharedPreferences.getString("lng", "0.0");
                mapFragmentVm.setLatitude(lat);
                mapFragmentVm.setLongitude(lng);
                Log.e("latlat", "in beh" + lat + "," + lng);
//                View markerView1 = ((LayoutInflater) mapView.getContext()
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_withtext, null);

                new HomeActivity.CustomMarker(appSession.getData().getProfileImage(),
                        new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)),
                        markerView, "", "", null, googleMap, profileimg, "", null).execute();


                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 11);
                googleMap.animateCamera(cameraUpdate);


//
//                createCustomMarker(getContext(),appSession.getData().getProfileImage(),"", new LatLng(   30.7333,76.7794),
//                        googleMap);








                if (frienddata != null) {


                    fragmentMapViewBinding.ivRange.setVisibility(View.GONE);
                    for (int i = 0; i < frienddata.size(); i++) {


                        markerView = ((LayoutInflater) getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_withtext, null);


                        profileimg = markerView.findViewById(R.id.img);
                        tv_friend_count = markerView.findViewById(R.id.tv_friend_count);
                        tv_friend_count.setVisibility(View.VISIBLE);
                        new HomeActivity.CustomMarker(appSession.getData().getProfileImage(),
                                new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)),
                                markerView, "", "", null, googleMap, profileimg, "", null).execute();


                        markerView = ((LayoutInflater) getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
                        imgbig = markerView.findViewById(R.id.imgbig);
                        new HomeActivity.CustomMarker(frienddata.get(i).getFriendImage(), new LatLng(
                                Double.parseDouble(frienddata.get(i).getFriendLatitude()), Double.parseDouble(frienddata.get(i).getFriendLongitude()
                        )), markerView, frienddata.get(i).getFriendName(), String.valueOf(frienddata.get(i).getFriendId()), null, googleMap, profileimg, "Friends", frienddata.get(i)).execute();


                        boolean success = googleMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getContext(), R.raw.map_style));


                        if (frienddata.get(i).getFriendLatitude()!=null) {
                         /*   CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom
                                    (new LatLng(Double.parseDouble(frienddata.get(i).getFriendLatitude()),
                                            Double.parseDouble(frienddata.get(i).getFriendLatitude())), 4);
                            googleMap.animateCamera(cameraUpdate1);*/


                            LatLng coordinate = new LatLng(Double.parseDouble(frienddata.get(i).getFriendLatitude()),
                                    Double.parseDouble(frienddata.get(i).getFriendLongitude())); //Store these lat lng values somewhere. These should be constant.
                            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                    coordinate, 14);
                            googleMap.animateCamera(location);
                        }


                    }
                } else {
                    mapFragmentVm.showFriends(getView(), "", "", "");

                    fragmentMapViewBinding.ivRange.setVisibility(View.VISIBLE);
                }


                mapFragmentVm.setUserCurrentLat(lat);
                mapFragmentVm.setUserCurrentLng(lng);


                Log.e("friendlistsize", "erro" + friendsList.size());
                if (friendsList.size() == 0) {
                    tv_friend_count.setVisibility(View.INVISIBLE);

                } else {
                    tv_friend_count.setVisibility(View.INVISIBLE);
                    tv_friend_count.setText(String.valueOf(friendsList.size()));
                }

                mapFragmentVm.latLngList.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(s -> {
                    latLngList = s;

                    //  mapFragmentVm.setMapView(fragmentMapViewBinding.map);
                }).doOnError(throwable -> {
                    Log.e("error", "erro");
                }).subscribe();
                mapFragmentVm.mapclear.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(s -> {
                    if (s) {
                        googleMap.clear();
                        //refreshFragment();
                        markerView = ((LayoutInflater) getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_withtext, null);
                        sharedPreferences = getContext().getSharedPreferences("LocaPref", Context.MODE_PRIVATE);
                        String lat_ = sharedPreferences.getString("lat", "0.0");
                        String lng_ = sharedPreferences.getString("lng", "0.0");

                        profileimg = markerView.findViewById(R.id.img);
                        new HomeActivity.CustomMarker(appSession.getData().getProfileImage(),
                                new LatLng(Double.parseDouble(lat_), Double.parseDouble(lng_)),
                                markerView, "", "", null, googleMap, profileimg, "", null).execute();

                    }

                    //  mapFragmentVm.setMapView(fragmentMapViewBinding.map);
                }).doOnError(throwable -> {
                    Log.e("error", "erro");
                }).subscribe();

                mapFragmentVm.friendsDatalist.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(s -> {

                    googleMap.clear();
                    friendsList = s;


                    for (int i = 0; i < friendsList.size(); i++) {

                        sharedPreferences = ThisApp.getAppContext().getSharedPreferences("LocaPref", Context.MODE_PRIVATE);
                        String lat_ = sharedPreferences.getString("lat", "0.0");
                        String lng_ = sharedPreferences.getString("lng", "0.0");
                        Log.e("lat_lng_", "in beh" + lat_ + "," + lng_);
                        markerView = ((LayoutInflater) getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_withtext, null);


                        profileimg = markerView.findViewById(R.id.img);
                        tv_friend_count = markerView.findViewById(R.id.tv_friend_count);
                        tv_friend_count.setVisibility(View.VISIBLE);
                        new HomeActivity.CustomMarker(appSession.getData().getProfileImage(),
                                new LatLng(Double.parseDouble(lat_), Double.parseDouble(lng_)),
                                markerView, "", "", null, googleMap, profileimg, "", null).execute();


                        if (friendsList.size() == 0) {
                            tv_friend_count.setVisibility(View.INVISIBLE);
                        } else {
                            Log.e("friendlistsize", "in beh" + friendsList.size());
                            tv_friend_count.setVisibility(View.VISIBLE);
                            tv_friend_count.setText(String.valueOf(friendsList.size()));
                        }
                        markerView = ((LayoutInflater) getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
                        imgbig = markerView.findViewById(R.id.imgbig);
                        new HomeActivity.CustomMarker(friendsList.get(i).getProfileImage(), new LatLng(
                                Double.parseDouble(friendsList.get(i).getLatitude()), Double.parseDouble(friendsList.get(i).getLongitude()
                        )), markerView, friendsList.get(i).getName(), String.valueOf(friendsList.get(i).getId()), friendsList.get(i), googleMap, profileimg, friendsList.get(i).getRealtion(), null).execute();


                        boolean success = googleMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getContext(), R.raw.map_style));

                        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 4);
                        googleMap.animateCamera(cameraUpdate1);


                    }


                    //  drawCircle(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), googleMap, Integer.parseInt(mapFragmentVm.getRange()));
                    //  mapFragmentVm.setMapView(fragmentMapViewBinding.map);
                }).doOnError(throwable -> {
                    Log.e("error", "erro");
                }).subscribe();


                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {


                        if (frienddata == null) {

                            //   rlFriendStaus.setVisibility(View.VISIBLE );
                            Datum data = (Datum) marker.getTag();


                            if (data != null) {


                                if (data.getFriend_status().equals("no_relation")) {
                                    iv_add_friend.setVisibility(View.VISIBLE);
                                    iv_message.setVisibility(View.GONE);

                                }else   if (data.getFriend_status().equals("pending_request")) {
                                    iv_add_friend.setVisibility(View.GONE);
                                    iv_message.setVisibility(View.GONE);

                                } else {
                                    iv_add_friend.setVisibility(View.GONE);
                                    iv_message.setVisibility(View.VISIBLE);
                                }

                                iv_add_friend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mapFragmentVm.sendFriendRequest(view, String.valueOf(data.getId()));

                                    }
                                });
                                rlFriendStaus.setVisibility(View.VISIBLE);
                                tvName.setText(data.getName());


                                iv_message.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getContext(), ChatDetails.class);
                                        intent.putExtra("name", data.getName());
                                        intent.putExtra("image", data.getProfileImage());
                                        intent.putExtra("chat_group_id", "0");
                                        intent.putExtra("from_user_id", String.valueOf(appSession.getData().getId()));
                                        intent.putExtra("to_user_id", String.valueOf(data.getId()));
                                        getContext().startActivity(intent);
                                    }
                                });
                                tvMutualFriendsCount.setText(String.valueOf(data.getMutualFriends()) + " MUTUAL FRIENDS");

                                if (data.getTravelling()) {
                                    tvTravellingStatus.setText("Now Travelling");
                                } else {
                                    tvTravellingStatus.setText("Home");
                                }


                                ivSendGoTo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                        ProfileFragment myFragment = new ProfileFragment(String.valueOf(data.getId()), data.getFollowStatus(), data.getFriend_status());
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();
                                    }


                                });


                                LatLng latLng = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));


                                //   Toast.makeText(getContext(), "" + latLng + "==" + marker.getPosition(), Toast.LENGTH_SHORT).show();


                                if (marker.getPosition() == latLng) {
                                    imgbig.setVisibility(View.VISIBLE);
                                } else {
                                    imgbig.setVisibility(View.GONE);

                                }


                                //drawCircle(marker.getPosition(), googleMap, 1500);

                            }
                            //   drawbackground(marker.getPosition(), googleMap, 70);

                        } else {
                            Data data = (Data) marker.getTag();
                            if (data != null) {

                                //     Toast.makeText(getContext(), "clickedpos-" + data.getId(), Toast.LENGTH_SHORT).show();


                                rlFriendStaus.setVisibility(View.VISIBLE);
                                tvName.setText(data.getFriendName());


                                iv_message.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getContext(), ChatDetails.class);
                                        intent.putExtra("name", data.getFriendName());
                                        intent.putExtra("image", data.getFriendImage());
                                        intent.putExtra("chat_group_id", "0");
                                        intent.putExtra("from_user_id", String.valueOf(appSession.getData().getId()));
                                        intent.putExtra("to_user_id", String.valueOf(data.getFriendId()));
                                        getContext().startActivity(intent);
                                    }
                                });

                                tvMutualFriendsCount.setText(String.valueOf(data.getMutualFriends()) + " MUTUAL FRIENDS");
                                if (data.getTravelling()) {
                                    tvTravellingStatus.setText("Now Travelling");
                                } else {
                                    tvTravellingStatus.setText("Home");
                                }


                                ivSendGoTo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                        ProfileFragment myFragment = new ProfileFragment(String.valueOf(data.getFriendId()), "", "friends");
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();
                                    }


                                });


                                LatLng latLng = new LatLng(Double.parseDouble(data.getFriendLatitude()), Double.parseDouble(data.getFriendLongitude()));


                                //   Toast.makeText(getContext(), "" + latLng + "==" + marker.getPosition(), Toast.LENGTH_SHORT).show();


                                if (marker.getPosition() == latLng) {
                                    imgbig.setVisibility(View.VISIBLE);
                                } else {
                                    imgbig.setVisibility(View.GONE);

                                }


                                //drawCircle(marker.getPosition(), googleMap, 1500);

                            }
                        }
                        return true;
                    }
                });

            }
        });


        return fragmentMapViewBinding.getRoot();
    }

    public void show(List<String> list, View view) {
        List<PowerMenuItem> l1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            l1.add(new PowerMenuItem(list.get(i), false));
        }
        powerMenu = new PowerMenu.Builder(getActivity())
                .addItemList(l1)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)

                .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setMenuColor(Color.WHITE)

                .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
        powerMenu.showAsDropDown(view);

    }


    public void openPlaceAutoComplete() {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                if (place.getLatLng().latitude == 0.0) {
                    mapFragmentVm.setLatitude("0.0");
                } else {
                    mapFragmentVm.setLatitude(String.valueOf(place.getLatLng().latitude));
                    mapFragmentVm.setLongitude(String.valueOf(place.getLatLng().longitude));
                }


                if (place.getName() != null) {
                    mapFragmentVm.setPlacename(place.getName());


                    if (place.getLatLng() != null) {
                        mapFragmentVm.searchFriends(getView());
                    }
                }

                Log.i("trails fragment", "Place: " + place.getName() + ", " + place.getAddress());
//                tripTrailsVm.setLongitude(String.valueOf(place.getLatLng().longitude));
//                tripTrailsVm.setLattitude(String.valueOf(place.getLatLng().latitude));
//                tripTrailsVm.setLocationName(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("trails fragment", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


    }

    ImageView imgbig, profileimg;
    TextView tv_friend_count;


    Circle circle;
    Circle circle1;

    private void drawCircle(LatLng point, GoogleMap googleMap, int radius) {

        if (circle != null) {
            circle.remove();
        }
        circle = googleMap.addCircle(new CircleOptions()
                .center(point)
                .radius(radius)
                .strokeWidth(2)
                .strokeColor(Color.GREEN)
                .fillColor(Color.WHITE));

    }

    private void drawbackground(LatLng point, GoogleMap googleMap, int radius) {


        if (circle != null) {

        }
        if (circle1 != null) {
            circle1.remove();
        }
        circle1 = googleMap.addCircle(new CircleOptions()
                .center(point)
                .radius(radius)
                .strokeWidth(2)

                .strokeColor(Color.WHITE)

                .fillColor(ContextCompat.getColor(getContext(), R.color.colorPrimary)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17));

    }


   /* public class CustomMarker extends AsyncTask<String, Void, Bitmap> {

        String link;
        LatLng latLng;
        String title;
        String id;
        View markerView;
        Datum data;
        ImageView view;
        GoogleMap googleMap;
        String relation="";
        Data frienddata;


        public CustomMarker(String link, LatLng latLng, View markerView, String title, String id, Datum data, GoogleMap googleMap, ImageView view,String relation,Data frienddata) {
            this.link = link;
            this.latLng = latLng;
            this.id = id;
            this.markerView = markerView;
            this.title = title;
            this.data = data;
            this.googleMap = googleMap;
            this.view = view;
            this.relation = relation;
            this.frienddata=frienddata;


        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;

            try {

                Log.e("link", "in beh" + link);
                URL url = new URL(link); //url of the image parser from the xml ("http://research.cbei.psu.edu/images/uploads/research-digest/general/1.CityIcon.png")
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmp = BitmapFactory.decodeStream(is);


                if(frienddata!=null){
                    bmp = AppUtils.getFriendBorder(bmp
                    , ContextCompat.getColor( getContext(),R.color.colorPrimary));
                }else{

                    if(relation.equals( "Friends of Friends" )){
                        bmp = AppUtils.getCircularBitmapMulticLR(bmp,
                                ContextCompat.getColor( getContext(),R.color.purple));


                    }else if(relation.equals( "Friends" )){
                        bmp = AppUtils.getCircularBitmapMulticLR(bmp,
                                ContextCompat.getColor( getContext(),R.color.colorPrimary));
                    }else {
                        bmp = AppUtils.getCircularBitmap(bmp
                        );
                    }

                }

            } catch (IOException e) {
                return bmp;

            }
            return bmp;
        }


        Marker mymarker = null;


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                ImageView image = markerView.findViewById(R.id.img);
                //  ImageView imgbig = markerView.findViewById(R.id.imgbig);
                Log.e("bitmsp", "in beh" + bitmap);
                Boolean follow;
                if (data != null) {
                    if (data.getRealtion().equals("Friends of Friends")) {
                        image.setImageResource(R.drawable.user_forty);
                        follow = true;
                    } else {
                        follow = false;
                        image.setImageBitmap(bitmap);


                    }

                } else {
                    image.setImageBitmap(bitmap);
                    follow = false;
                }



                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
                markerView.setLayoutParams(layoutParams);

                if (follow) {
                    mymarker = googleMap.addMarker
                            (new MarkerOptions().position(latLng).
                                    icon(BitmapDescriptorFactory.fromResource((R.drawable.user_forty))));
                } else {


                    mymarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(AppUtils.loadBitmapFromView(markerView))));
                }

*//*
                if(frienddata!=null){
                    mymarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(AppUtils.loadBitmapFromView(markerView))));

                }*//*
if(frienddata==null){
    mymarker.setTag(data);

}else {
    mymarker.setTag( frienddata );
}


                mymarker.showInfoWindow();
            }

        }
    }*/

    public void refreshFragment() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
