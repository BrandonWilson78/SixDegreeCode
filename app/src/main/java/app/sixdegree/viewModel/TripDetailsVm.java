package app.sixdegree.viewModel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cz.msebera.android.httpclient.Header;
import androidx.core.content.ContextCompat;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.rrdl.gradientpoly.GradientPoly;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import app.sixdegree.R;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.network.responses.gettripdetailsnewres.GetTripDetailsResNew;
import app.sixdegree.network.responses.gettripdetailsnewres.Trail;
import app.sixdegree.network.responses.tripdetailsres.SingleTripRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.utils.DirectionsJSONParser;
import app.sixdegree.view.activity.trailsmodule.adapters.TrailAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class TripDetailsVm extends BaseVm {
    public BehaviorSubject<List<LatLng>> latLngList = BehaviorSubject.create();
    public BehaviorSubject<GoogleMap> googleMapObserver = BehaviorSubject.create();
    Circle circle;
    ArrayList<LatLng> localLatLngList = new ArrayList<>();
    ArrayList<Trail> trails_list = new ArrayList<>();
    ArrayList<LatLng> path = new ArrayList<>();
    @Bindable
    Boolean isLoading = false;
    @Bindable
    String tripid = "";
    @Bindable
    String date;

    public String getWhotag() {
        return whotag;
    }

    public void setWhotag( String whotag ) {
        this.whotag = whotag;
        notifyPropertyChanged( BR.whotag );
    }

    @Bindable
String whotag="";

    public String getTripuser() {
        return tripuser;
    }

    public void setTripuser(String tripuser) {
        this.tripuser = tripuser;
        notifyPropertyChanged(BR.tripuser);
    }

    @Bindable
String tripuser="";

    public String getTagnames() {
        return   tagnames.equals( "" ) ? "" : whotag +" With "+ tagnames;
    }

    public void setTagnames( String tagnames ) {
        this.tagnames = tagnames;
        notifyPropertyChanged( BR.tagnames );
    }

    @Bindable
    String tagnames="";
    @Bindable
    String tripName;
    @Bindable
    String trailCount;
    @Bindable
    String traildDays;
    @Bindable
    String trailKm;
    @Bindable
    String trailLikes;
    @Bindable
    String trailDesc;
    @Bindable
    String kilometer;
    @Bindable
    String likes;
    String country = "";
    TrailAdapter trailAdapter;
    CountriesDao dao;
    @Bindable
    String days;
    @Bindable
    String coverPic;

    public String getTripenddate() {
        return tripenddate;
    }

    public void setTripenddate( String tripenddate ) {
        this.tripenddate = tripenddate;
        notifyPropertyChanged( BR. tripenddate);
    }

    @Bindable
    String tripenddate="";
    MapView mapView;
    @Bindable
    LatLng latLng;
    @Bindable
    boolean displayMap = false;
    GoogleMap gmap;
    @Bindable
    String trailid="";


String token;


AppSession appSession;

    String user_id="";
Context context;
    String tagged="";

    public TripDetailsVm( CountriesDao dao, String token, String user_id, AppSession appSession,String tagged,Context context ) {
        this.dao = dao;
        this.token=token;
        this.user_id=user_id;
        this.tagged=tagged;
        this.context=context;
        this.appSession
                =appSession;
        trailAdapter = new TrailAdapter(dao, this,token,user_id,appSession,getTripenddate(),tagged,context);
    }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(View view, String coverPic) {
        if (coverPic != null) {
            AppUtils.setImageBg(view, coverPic);
        }
    }

    @BindingAdapter({"bind:displayMap"})
    public static void loadImage(LinearLayout layout, Boolean mapVisible) {
        if (mapVisible != null) {

            if (mapVisible) {
                if (layout.getVisibility() == View.VISIBLE) {
                    Animation aniFade = AnimationUtils.loadAnimation(layout.getContext(), R.anim.menu_fade_out);
                    layout.startAnimation(aniFade);
                }


            }

        }
    }

    public String getTrailid() {
        return trailid;
    }

    public void setTrailid(String trailid) {
        this.trailid = trailid;
        notifyPropertyChanged(BR.trailid);
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
        notifyPropertyChanged(BR.tripid);
    }

    /*  @BindingAdapter({"bind:latLng"})
        public static void mapss(MapView mapView, final LatLng latLng) {
            if (mapView != null) {
                mapView.onCreate(new Bundle());
                mapView.onResume();
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .add(new LatLng(-35.016, 143.321), new LatLng(-34.747, 145.592), new LatLng(-34.364, 147.891),
                                        new LatLng(-33.501, 150.217),
                                        new LatLng(-32.306, 149.248),
                                        new LatLng(-32.491, 147.309)));
                        polyline1.setJointType(1);
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(30.659370, 76.733051)).title("Marker in India"));


                    }
                });

            }
        }*/
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3).
                setApiKey("AIzaSyCd4Nh-OERmOGekc5j-HlDrzEVCs_5ad1o")
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    public void setPolyLineCoOrdinates(ArrayList<LatLng> polyLineCoOrdinates) {
    /*    if (googleMap != null) {
            Log.e("called", "caleld");
            LatLng[] latLngs = new LatLng[polyLineCoOrdinates.size()];
            for (int i = 0; i < polyLineCoOrdinates.size(); i++) {
                latLngs[i] = polyLineCoOrdinates.get(i);
            }
          *//*  Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(latLngs));*//*

        } else {
            Log.e("called", "not caleld");
        }*/

        Log.e("polyLineCoOrdinates", "--"+polyLineCoOrdinates);
        // DirectionsApiRequest req = DirectionsApi.getDirections(getGeoContext(), "30.659343,76.733081", "30.671743,76.727607");
        DirectionsApiRequest req = null;
        String latLng0="";
          String latLng1="";
        for (int i = 0; i < polyLineCoOrdinates.size(); i++) {
            
              latLng0= (polyLineCoOrdinates.get( 0 ).latitude+","+polyLineCoOrdinates.get( 0 ).longitude );
              latLng1= (polyLineCoOrdinates.get( polyLineCoOrdinates.size()-1 ).latitude+","+polyLineCoOrdinates.get( polyLineCoOrdinates.size()-1).longitude );
            Log.e("polyLineCoOrdinatesi", "--"+latLng0);
            Log.e("polyLineCoOrdinatesi-1", "--"+latLng1);

        }
        req = DirectionsApi.getDirections(getGeoContext(),latLng0,latLng1 );
        // req.waypoints("31.115885,76.090805");
      /*  for (int i = 0; i < polyLineCoOrdinates.size(); i++) {
            if (i!=0&&i!=polyLineCoOrdinates.size()-1){
                Log.e("vm","vm=="+i);
                req.waypoints(polyLineCoOrdinates.get(i).latitude+","+polyLineCoOrdinates.get(i).longitude);

            }
        }*/

        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("VM", ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(20);
            opts.jointType(0);
            gmap.addPolyline(opts);
        }

    }

    public void movemarkerToPos(int pos) {
        if (!isDisplayMap()) {
            setDisplayMap(true);
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(localLatLngList.get(pos), 16);
        gmap.animateCamera(cameraUpdate);
        //drawCircle(localLatLngList.get(pos), googleMap);
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.gmap = googleMap;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        mapView.onCreate(new Bundle());
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady( GoogleMap googleMap ) {
                setGoogleMap( googleMap );
                gmap=googleMap;

                googleMapObserver.onNext( googleMap );
              //  setPolyLineCoOrdinates( localLatLngList );
                //   for (int i = 0; i < localLatLngList.size(); i++) {
                List<String> urls = getDirectionsUrl(localLatLngList);
                if (urls.size() > 1) {
                    for (int i = 0; i < urls.size(); i++) {
                        String url = urls.get(i);
                        DownloadTask downloadTask = new DownloadTask();
                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }
                }


                for (int i = 0; i < trails_list.size(); i++) {

                    View markerView = ((LayoutInflater) mapView.getContext()
                            .getSystemService( Context.LAYOUT_INFLATER_SERVICE )).inflate( R.layout.map_marker_double, null );
                    if(!trails_list.get( i ).getLatitude().isEmpty() && !trails_list.get( i ).getLongitude().isEmpty()){

                        new CustomMarker( trails_list.get( i ).getCategoryPicture(), new LatLng( Double.parseDouble( trails_list.get( i ).getLatitude() ),
                                Double.parseDouble( trails_list.get( i ).getLongitude() ) ), markerView ,trails_list.get( i ).getTrailPictures()).execute();



                    }







                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mapView.getContext(), R.raw.map_style));



                  //  CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(localLatLngList.get(i), 10);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom( new LatLng( Double.parseDouble( trails_list.get( i ).getLatitude() ),
                            Double.parseDouble( trails_list.get( i ).getLongitude() )), 2);
                    googleMap.animateCamera(cameraUpdate);
/*
                    MarkerOptions m = AppUtils.createMarker(mapView.getContext(), new LatLng(localLatLngList.get(i).latitude, localLatLngList.get(i).longitude));
                    googleMap.addMarker(m);*/
                 }
                //   Toast.makeText(mapView.getContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while  url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            MarkerOptions markerOptions = new MarkerOptions();
            try {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    // lineOptions.addAll(locations);
                    lineOptions.width(9);
                    lineOptions.geodesic( true );

                    lineOptions.color(ContextCompat.getColor( mapView.getContext() ,R.color.colorPrimary));


                                // lineOptions.addAll(punchinlat);

                            }
                // Drawing polyline in the Google Map for the i-th route
                gmap.addPolyline(lineOptions);

            } catch (Exception e) {

            }
        }
    }
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
        notifyPropertyChanged(BR.latLng);
    }

    public boolean isDisplayMap() {
        return displayMap;
    }

    public void setDisplayMap(boolean displayMap) {
        this.displayMap = displayMap;
        notifyPropertyChanged(BR.displayMap);
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
        notifyPropertyChanged(BR.likes);
    }

    public String getKilometer() {
        return kilometer;
    }

    public void setKilometer(String kilometer) {
        this.kilometer = kilometer;
        notifyPropertyChanged(BR.kilometer);
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
        notifyPropertyChanged(BR.days);
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
        notifyPropertyChanged(BR.coverPic);
    }
    public String getCountry() {
        return !TextUtils.isEmpty(country) ? country : "N/A";
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public TrailAdapter getTrailAdapter() {
        return trailAdapter;
    }

    public void setTrailAdapter(TrailAdapter trailAdapter) {
        this.trailAdapter = trailAdapter;
    }

    public String getDate() {
        return !TextUtils.isEmpty(date) ? date : "N/A";
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    public String getTripName() {
        return !TextUtils.isEmpty(tripName) ? tripName : "N/A";
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
        notifyPropertyChanged(BR.tripName);
    }

    public String getTrailCount() {
        return !TextUtils.isEmpty(trailCount) ? trailCount : "N/A";
    }

    public void setTrailCount(String trailCount) {
        this.trailCount = trailCount;
        notifyPropertyChanged(BR.trailCount);
    }

    public String getTraildDays() {
        return !TextUtils.isEmpty(traildDays) ? traildDays : "N/A";
    }

    public void setTraildDays(String traildDays) {
        this.traildDays = traildDays;
        notifyPropertyChanged(BR.traildDays);
    }

    public String getTrailKm() {
        return !TextUtils.isEmpty(trailKm) ? trailKm : "N/A";
    }

    public void setTrailKm(String trailKm) {
        this.trailKm = trailKm;
        notifyPropertyChanged(BR.trailKm);
    }

    public String getTrailLikes() {
        return !TextUtils.isEmpty(trailLikes) ? trailLikes : "N/A";
    }

    public void setTrailLikes(String trailLikes) {
        this.trailLikes = trailLikes;
        notifyPropertyChanged(BR.trailLikes);
    }

    public String getTrailDesc() {
        return !TextUtils.isEmpty(trailDesc) ? trailDesc : "N/A";
    }

    public void setTrailDesc(String trailDesc) {
        this.trailDesc = trailDesc;
        notifyPropertyChanged(BR.trailDesc);
    }

    public void setLoading(Boolean isLoading) {
        this.isLoading = isLoading;
        notifyPropertyChanged(BR.isLoading);
    }

    public Boolean getIsLoading() {
        return isLoading;
    }

    public void getTripDetails(String id) {
        setLoading(true);
        apiService.getTripDetails(getSessionInstance().getToken(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTripDetailsResNew>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetTripDetailsResNew data) {

                        if (data.getStatus()) {

                            //  if (data.getData().getId()!=null){
                            //   setTripid(String.valueOf(data.getData().getId()));
                            //  }
                            setTrailCount(data.getData().getTrails().size() + " PITSTOPS");
                            setTripName(data.getData().getName());
                            setTripid( String.valueOf(data.getData().getId()));
                            setTrailDesc(data.getData().getSummary());
                            setCoverPic(data.getData().getTripPicture());
                            setDate(AppUtils.getMonth(data.getData().getStartDate()) + "-" + AppUtils.getMonthYear(data.getData().getEndDate()));
setTripuser("Trip by: "+data.getData().getUser().getName());
               if(data.getData().getEndDate()!=null){
                   setTripenddate(data.getData().getEndDate());
                   getTrailAdapter().settripdate( data.getData().getEndDate() );
               }else{
                   setTripenddate("");
                   getTrailAdapter().settripdate("");
               }
                            if(data.getData().getTrails()!=null){
                                getTrailAdapter().setData(data.getData().getTrails());
                            }

                            getTrailAdapter().getTasks(data.getData().getCountry(), String.valueOf(data.getData().getTrails().size()));
                            setDays(/*data.getData().getTotalDays() + "/" +*/ data.getData().getTotalDays() + " Days");
                            setKilometer(data.getData().getTotalDistance() + " KM");
                            setLikes(String.valueOf(data.getData().getTotalLikes()));


                            if(data.getData().getTagNames()==null){
                                setTagnames( "" );
                            }else {
                                setTagnames( data.getData().getTagNames() );
                            }
                            if(data.getData().getUser()==null){
                                setWhotag("");
                            }else {
                                setWhotag( data.getData().getUser().getName() );
                            }
                            Log.e("setTag_names", data.getData().getTagNames() + "==");
                            ArrayList<LatLng> arrayList = new ArrayList<>();
                            for (int i = 0; i < data.getData().getTrails().size(); i++) {
                                if (!data.getData().getTrails().get(i).getLatitude().isEmpty() && !data.getData().getTrails().get(i).getLongitude().isEmpty()) {
                                    arrayList.add(new LatLng(Double.parseDouble(data.getData().getTrails().get(i).getLatitude()), Double.parseDouble(data.getData().getTrails().get(i).getLongitude())));
                                    trails_list.add( data.getData().getTrails().get( i ) );
                                }
                            }
                            latLngList.onNext(arrayList);
                            localLatLngList.addAll(arrayList);
                            Log.e("size", arrayList.size() + "==");
                            Log.e("trails_list", arrayList.size() + "==");

//                            for (int i = 0; i < data.getData().getTrails().size(); i++) {
//                                     setTrailid(String.valueOf(data.getData().getTrails().get(i).getId()));

//                                Log.e("traild id m--","--"+getTrailid());
//                             }
//



                        } else {
                            Log.e("trail size", data.getData().getTrails().size() + "==");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        Log.e("trail size", e.toString() + "==");

                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);

                    }
                });

    }

    private void drawCircle(LatLng point, GoogleMap googleMap, int radius) {

        if (circle != null) {
            circle.remove();
        }
        circle = googleMap.addCircle(new CircleOptions()
                .center(point)
                .radius(10000)
                .strokeWidth(1)
                .strokeColor(Color.GREEN)
                .fillColor(Color.WHITE));

    }


    public class CustomMarker extends AsyncTask<String, Void, Bitmap> {

        String link;
        LatLng latLng;
        View markerView;
        String catimg;


        public CustomMarker(String link, LatLng latLng, View markerView,String catimg) {
            this.link = link;
            this.latLng = latLng;
            this.markerView = markerView;
            this.catimg=catimg;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;

            try {
                URL url = new URL(link); //url of the image parser from the xml ("http://research.cbei.psu.edu/images/uploads/research-digest/general/1.CityIcon.png")
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                 bmp = AppUtils.getCircular(bmp);


            } catch (IOException e) {
                return bmp;

            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                ImageView image = markerView.findViewById(R.id.img);
                ImageView iv_catimg = markerView.findViewById(R.id.imgbig);
              image.setImageBitmap(bitmap);

/*AppUtils.picassoLoadBgUriInImg( iv_catimg, Uri.parse( catimg )
);*/


               LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
                markerView.setLayoutParams(layoutParams);
                gmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(AppUtils.loadBitmapFromView(markerView))));
            }

        }
    }

    private List<String> getDirectionsUrl(ArrayList<LatLng> markerPoints) {
        List<String> mUrls = new ArrayList<>();
        if (markerPoints.size() > 1) {
            String str_origin = markerPoints.get(0).latitude + "," + markerPoints.get(0).longitude;
            String str_dest = markerPoints.get(1).latitude + "," + markerPoints.get(1).longitude;
         //   String str_dest = markerPoints.get(1).latitude + "," + markerPoints.get(1).longitude;
            String key = "key=AIzaSyDbPyoOQ3eG2rvTSheQYrXJOMnXF5aKNR4";
            String sensor = "sensor=false";
            String parameters = "origin=" + str_origin + "&destination=" + str_dest + "&" + sensor;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

            mUrls.add(url);
            for (int i = 0; i < markerPoints.size(); i++)//loop starts from 2 because 0 and 1 are already printed
            {
                //str_origin = str_origin;
                str_dest = markerPoints.get(i).latitude + "," + markerPoints.get(i).longitude;
                parameters = "origin=" + str_origin + "&destination=" + str_dest + "&" +  key;
                url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                Log.e("url","--"+url);
                mUrls.add(url);



             }
        }

        return mUrls;
    }
    public static AsyncHttpClient getClient() {
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60 * 1000);
            client.setSSLSocketFactory(socketFactory);
            return client;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AsyncHttpClient();
    }
    public void getPolyLine(String originLat, String originLon, String destinationLat, String destinationLon) {
        AsyncHttpClient client = getClient();
        client.get(getUrl(originLat, originLon, destinationLat, destinationLon), null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject json = getJson(new String(responseBody));
                Log.e("Status", json.optString("status"));
                /*            for (NSDictionary *routes in response[@"routes"]) {
                GMSPath *path = [GMSPath pathFromEncodedPath:routes[@"overview_polyline"][@"points"]];
*/
                if (json.optString("status").equals("OK")) {
                    JSONArray jsonArray = json.optJSONArray("routes");
                    String polylineString = jsonArray.optJSONObject(0).optJSONObject("overview_polyline").optString("points");
                    System.out.print(polylineString);
                    final List<LatLng> result = decodePoly(polylineString);
                    System.out.print(result.size());

                    final float red = 244.0f;
                    final float green = 201.0f;
                    final float yellow = 19.0f;

                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            float redSteps = (red / result.size());
                            float greenSteps = (green / result.size());
                            float yellowSteps = (yellow / result.size());
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(result.get(0));
                            for (int i = 1; i < result.size(); i++) {
                                builder.include(result.get(i));
                                int redColor = (int) (red - (redSteps * i));
                                int greenColor = (int) (green - (greenSteps * i));
                                int yellowColor = (int) (yellow - (yellowSteps * i));
                                Log.e("Color", "" + redColor);
                                int color = Color.rgb(redColor, greenColor, yellowColor);

                                PolylineOptions options = new PolylineOptions().width(8).color(color).geodesic(true);
                                options.add(result.get(i - 1));
                                options.add(result.get(i));
                                Polyline line = gmap.addPolyline(options);
                                line.setEndCap(new RoundCap());
                            }
                            LatLngBounds bounds = builder.build();
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                            gmap.animateCamera(cu);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
    public String getUrl(String originLat, String originLon, String destinationLat, String destinationLon) {
        String DIRECTION_API = "https://maps.googleapis.com/maps/api/directions/json?origin=";
        return DIRECTION_API + originLat + "," + originLon + "&destination=" + destinationLat + "," + destinationLon;// + "&key=" + API_KEY;
    }
    public static JSONObject getJson(String jsonString) {
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println("JSON:" + jsonString);
            jsonObject = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            try {
                jsonObject.put("status", "FALSE");
                jsonObject.put("msg", "Invalid Json Response");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return jsonObject;
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


}
