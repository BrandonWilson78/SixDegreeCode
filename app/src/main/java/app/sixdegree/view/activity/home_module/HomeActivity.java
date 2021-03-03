package app.sixdegree.view.activity.home_module;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivityHomeBinding;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.GetNotificationBadgeCount;
import app.sixdegree.network.responses.getfriendslisting.Data;
import app.sixdegree.network.responses.searchfriends.Datum;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.ThisApp;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.Splash;
import app.sixdegree.view.activity.home_module.fragments.FriendsFragment;
import app.sixdegree.view.activity.home_module.fragments.MapFragment;
import app.sixdegree.view.activity.home_module.fragments.NotificationFragment;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import app.sixdegree.view.activity.home_module.fragments.SearchFragment;
import app.sixdegree.viewModel.HomeVm;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends BaseActivity  {
    HomeVm homeVm;
    ActivityHomeBinding binding;
    boolean openF2;
    Context context=this;
    public static final String MyPREFERENCES = "LocaPref" ;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private SearchFragment searchFragment;
    BottomNavigationView bottomNavigation;

    Timer timer;
    MyTimerTask myTimerTask;
     View  notificationBadge  ;
    BottomNavigationItemView itemView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        homeVm = new HomeVm(getSession());

        binding.setViewModel(homeVm);
        setContentView(binding.getRoot());
        View view=binding.getRoot();
        bottomNavigation = view.findViewById(R.id.navigation);


         //shared prefe
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
          editor = sharedpreferences.edit();

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            searchFragment = (SearchFragment) getSupportFragmentManager().getFragment(savedInstanceState, "search");

        }
if(getIntent().hasExtra( "from" )){
    String from=getIntent().getStringExtra( "from" );

    if(from.equals("searchbyname")){
        loadFragment( new ProfileFragment( getIntent().getStringExtra( "id" ) ,getIntent().getStringExtra( "status" ),
                getIntent().getStringExtra( "friendstatus" )) ,"profile");
    }
}else {
    loadFragment( new ProfileFragment ("","",""),"profile");
}



        bottomNavigation.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        Log.e("simar id", String.valueOf(item.getItemId()));
                        switch (item.getItemId()) {
                            case R.id.profile_menu:
                                loadFragment(new ProfileFragment("","",""),"profile");
                                //   bottomNavigation.setSelectedItemId(R.id.profile_menu);
                                break;
                            case R.id.friends_menu:
                                loadFragment(new FriendsFragment(),"friends");
                                //  bottomNavigation.setSelectedItemId(R.id.friends_menu);
                                break;
                            case R.id.maps_menu:
                                loadFragment(new MapFragment(null),"map");
                                //  bottomNavigation.setSelectedItemId(R.id.maps_menu);
                                break;
                            case R.id.notification_menu:
                                loadFragment(new NotificationFragment(),"notification");
                                //  bottomNavigation.setSelectedItemId(R.id.notification_menu);
                                break;
                            case R.id.search_menu:
                                loadFragment(new SearchFragment(),"search");

                                //  bottomNavigation.setSelectedItemId(R.id.search_menu);
                                break;

                            default:
                                Log.e("nothing","selected");
                        }

                        return true;
                    }
                });














//        int[] tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
//        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.navigation_items);
//        navigationAdapter.setupWithBottomNavigation(binding.navigation, tabColors);
//// Set listeners
//        binding.navigation.setDefaultBackgroundColor(Color.parseColor("#000000"));

//// Change colors
//        binding.navigation.setInactiveColor(Color.parseColor("#619c92"));
//        binding.navigation.setAccentColor(Color.parseColor("#ffffff"));
//        binding.navigation.isTranslucentNavigationEnabled();
//          binding.navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                if (position == 0) {
//                    loadFragment(new ProfileFragment("",""));
//                } else if (position == 1) {
//                    loadFragment(new FriendsFragment( ));
//                } else if (position == 2) {
//                    loadFragment(new MapFragment());
//                } else if (position == 3) {
//                    loadFragment(new NotificationFragment());
//                }
//                if (position == 4) {
//                    loadFragment(new SearchFragment());
//                }
//
//                return true;
//            }
//        });
//      //  binding.navigation.setBehaviorTranslationEnabled(true);
//
//        binding.navigation.setCurrentItem(0);


////upload location
//        PeriodicWorkRequest.Builder myWorkBuilder =
//                new PeriodicWorkRequest.Builder(UploadWorker.class, 15, TimeUnit.SECONDS);
//
//        PeriodicWorkRequest myWork = myWorkBuilder.build();
//        WorkManager.getInstance()
//                .enqueueUniquePeriodicWork("jobTag", ExistingPeriodicWorkPolicy.KEEP, myWork);
//
uploadLocation();



//opensearch frag


    }

    private View  getBadge()   {
        if (notificationBadge != null){
            return notificationBadge;
        }

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigation.getChildAt(0);

         itemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(3 );
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge,
                bottomNavigationMenuView,false);
        return notificationBadge;
    }





    private void addBadge(String count ) {
        getBadge();
        TextView tv_count=notificationBadge.findViewById(R.id.counter_badge);
        tv_count.setText(count);
         removeBadge();
        itemView.addView(notificationBadge);
    }

    private void removeBadge() {
        itemView.removeView(notificationBadge);
    }

    @Override
    protected void onResume() {

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 3000);
        super.onResume();
    }

    private void loadFragment(Fragment fragment,String tagname) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        transaction.replace(R.id.frame_container, fragment,tagname);
        //transaction.addToBackStack(null);
        transaction.commit();






    }

        /*  bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.profile_menu:
                        loadFragment(new ProfileFragment());
                        bottomNavigation.setSelectedItemId(R.id.profile_menu);
                        break;
                    case R.id.friends_menu:
                        loadFragment(new ProfileFragment());
                        bottomNavigation.setSelectedItemId(R.id.friends_menu);
                        break;
                    case R.id.maps_menu:
                        loadFragment(new ProfileFragment());
                        bottomNavigation.setSelectedItemId(R.id.maps_menu);
                        break;
                    case R.id.notification_menu:
                        loadFragment(new ProfileFragment());
                        bottomNavigation.setSelectedItemId(R.id.notification_menu);
                        break;
                    case R.id.search_menu:
                        loadFragment(new SearchFragment());
                        bottomNavigation.setSelectedItemId(R.id.search_menu);
                        break;

                        default:
                            Log.e("nothing","selected");


                }


                return false;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.profile_menu);*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    //handling button click toast
    @BindingAdapter({"errorMsg"})
    public static void abc( View view, String message) {
        if (message != null) {
            showSnackBar((Activity) view.getContext(), view.getId(), message);
        }
        //goToNextScreen(LocationPermissionActivity.class);
    }

    public void getNotificationBadgeCount() {
         apiService.getNotificationBadgeCount(getSession().getToken())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetNotificationBadgeCount>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetNotificationBadgeCount res) {
                         if (res.getStatus()) {
                             if (res.getUnreadCount() == 0) {
                    /*    textView?.text =""
                        textView?.visibility=View.GONE*/
                                 getBadge();
                                 removeBadge();

                             } else {
                        /*textView?.text =d.data.toString()
                       textView?.visibility=View.VISIBLE*/

                                 addBadge(String.valueOf(res.getUnreadCount()));

                             }

                         }
                    }

                    @Override
                    public void onError(Throwable e) {
                     }

                    @Override
                    public void onComplete() {
                     }
                });


    }



    public void uploadLocation() {



        if (isPermitted(context)) {



 if(SmartLocation.with( getApplicationContext() ).location().state().isGpsAvailable() ){
     if(SmartLocation.with( getApplicationContext() ).location().state().isNetworkAvailable()){


         SmartLocation.with( getApplicationContext() ).location()
                 .oneFix()
                 .start( new OnLocationUpdatedListener() {
                     @Override
                     public void onLocationUpdated( Location location ) {
                         if(location.getLatitude() == 0.0 || location.getLongitude() == 0.0){


//                                    LatLng midLatLng = googleMap.getCameraPosition().target;
//                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(midLatLng));


                             // Toast.makeText(getActivity(), "Unable to get current position.", Toast.LENGTH_SHORT).show();
                         }else{
                             //get lat lng and set location
                             LatLng ny = new LatLng( location.getLatitude(), location.getLongitude() );

                             //save location in shared preference

                             editor.putString("lat", String.valueOf( ny.latitude ));
                             editor.putString("lng", String.valueOf( ny.longitude ));
                             editor.commit();

                         //    Toast.makeText(HomeActivity.this, "sm"+ny, Toast.LENGTH_SHORT).show();
                             HashMap<String, String> map = new HashMap<>();
                             map.put( "geo_location_latitude", String.valueOf( ny.latitude ) );
                             map.put( "geo_location_longitude", String.valueOf( ny.longitude ) );





                             apiService.updateLocation( getSession().getToken(), map ).subscribeOn( Schedulers.io() )
                                     .observeOn( AndroidSchedulers.mainThread() )
                                     .subscribeWith( new Observer<BaseRes>() {
                                         @Override
                                         public void onSubscribe( Disposable d ) {

                                         }

                                         @Override
                                         public void onNext( BaseRes res ) {

                                             if(res.getStatus()){
                                                 Log.e( "upload location--", "--"+res.getMessage() );
                                             }
                                         }

                                         @Override
                                         public void onError( Throwable e ) {
                                         }

                                         @Override
                                         public void onComplete() {

                                         }
                                     } );
                         }

                     }
                 } );


     }
 }
 else {
     showSettingAlert();
 }

    }else {
            checkPermission();
        }


}
    public void showSettingAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Permission Required!");
        alertDialog.setMessage("GPS is not enabled, Do you want to go to settings menu? ");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showAlert();
             }
        });
        alertDialog.show();
    }


    public void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Permission Required!");
        alertDialog.setMessage("GPS is disabled! ");
        alertDialog.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNeutralButton("Close the app", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onPause() {
        Log.e("onpaused", "called");
        SmartLocation.with(context).location().stop();

        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        SmartLocation.with(context).location().stop();
        /*if (gpsTracker != null) {
            MyApplication.activityPaused(gpsTracker);
            gpsTracker.stopUsingGPS(gpsTracker);
        }*/

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }



    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }


    public boolean isPermitted( Context context) {
        return +ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
               + ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if ((grantResults.length > 0) &&
                    (grantResults[0]
                     + grantResults[1]
                     == PackageManager.PERMISSION_GRANTED
                    )
                ) {
                    // Permissions are granted
                    //Run your code

                } else {
                    // Permissions are denied show alert
                    Toast.makeText(context, "Location permission denied .", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            + ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            + ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED

        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                  HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(
                        HomeActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,

                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );

            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        HomeActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        } else {


             uploadLocation();



        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
//        getSupportFragmentManager().putFragment(outState, "search", searchFragment);
    }



    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (!isNetworkConnected()) {
                        createInternetMsgToast();
                        return;
                    }

                    getNotificationBadgeCount();
                }
            });
        }

    }
    public static class CustomMarker extends AsyncTask<String, Void, Bitmap> {

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
                Log.e("relation", "in beh" + relation);
                URL url = new URL(link); //url of the image parser from the xml ("http://research.cbei.psu.edu/images/uploads/research-digest/general/1.CityIcon.png")
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmp = BitmapFactory.decodeStream(is);


                if(frienddata!=null){
                    bmp = AppUtils.getCircularBitmap(bmp
                            );
                    Log.e("relation", "in frienddata" + relation);
                }else{

                /*    if(relation.equals( "Friends of Friends" )){
                        bmp = AppUtils.getCircularBitmapMulticLR(bmp,
                                 ContextCompat.getColor(ThisApp.getAppContext(),R.color.white));
                         *//*,R.color.purple);*//*
                        Log.e("relation", "in purple" + relation);

                    }else */if(relation.equals( "Friends" )){
                       /* bmp = AppUtils.getCircularBitmapMulticLR(bmp,
                                ContextCompat.getColor(ThisApp.getAppContext(),R.color.white));*/
                        /*       R.color.colorPrimary);*/


                        bmp = AppUtils.getCircularBitmap(bmp);

                        Log.e("relation", "in colorPrimary" + relation);
                    }else {
                        bmp = AppUtils.getCircularBitmap(bmp);
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
                        image.setImageResource(R.drawable.user_thirty);
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
                                    icon(BitmapDescriptorFactory.fromResource((R.drawable.user_thirty))));
                } else {


                    mymarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(AppUtils.loadBitmapFromView(markerView))));
                }

/*
                if(frienddata!=null){
                    mymarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(AppUtils.loadBitmapFromView(markerView))));

                }*/
                if(frienddata==null){
                    mymarker.setTag(data);

                }else {
                    mymarker.setTag( frienddata );
                }


                mymarker.showInfoWindow();
            }

        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Exit app");
        builder.setMessage("Are you sure you want to exit the app?");
        builder.setPositiveButton("Exit", (dialog, which) -> {


            super.onBackPressed();
        });

        builder.setNegativeButton("cancel", (dialog, which) -> {

        });
        builder.show();
    }
}