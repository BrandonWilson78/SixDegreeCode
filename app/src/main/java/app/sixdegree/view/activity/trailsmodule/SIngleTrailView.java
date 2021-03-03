package app.sixdegree.view.activity.trailsmodule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.HashMap;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivitySingleTrailViewBinding;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.DatabaseClient;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.home_module.fragments.TripTrailsFragment;
import app.sixdegree.view.activity.trailsmodule.adapters.TrailCommentsAdapter;
import app.sixdegree.viewModel.SingleTrailViewVm;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SIngleTrailView extends BaseActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    ActivitySingleTrailViewBinding binding;
    SingleTrailViewVm singleTrailViewVm;
    String trail_id = "";
    String user_id = "";
    String session_id = "";
    GoogleMap map;
ImageView edit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_trail_view);


        View view=binding.getRoot();
        edit=view.findViewById( R.id.edit );

        // get countries Dao from data base
        CountriesDao countriesDao = DatabaseClient
                .getInstance(getApplicationContext())
                .getAppDatabase()
                .CountriesDao();


        if (getIntent().hasExtra("trail_id")) {
            trail_id = getIntent().getStringExtra("trail_id");
            user_id = getIntent().getStringExtra("user_id");
            session_id = getIntent().getStringExtra("session_id");
        }


if(user_id.toString().equals( session_id.toString())){
edit.setVisibility( View.VISIBLE );
}else {
    edit.setVisibility( View.GONE );
}


        singleTrailViewVm = new SingleTrailViewVm(getSessionData(),countriesDao,trail_id, getSession().getToken());
        binding.setViewModel(singleTrailViewVm);



        singleTrailViewVm.isfinished.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
           if(data){
               refreshActivityWithoutAnimation();

//Toast.makeText( this,"in si ac"+data,Toast.LENGTH_SHORT ).show();
           }

        }).subscribe();
//comment added hide keyboard
        singleTrailViewVm.comentAdded.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
           if(data){
               hideKeyboard(this);

            }

        }).subscribe();




        binding.commentsRv.setLayoutManager(new LinearLayoutManager(this));
        TrailCommentsAdapter trailCommentsAdapter = new TrailCommentsAdapter( );
        binding.commentsRv.setAdapter(trailCommentsAdapter);


        //mapview
        binding.map.onCreate(new Bundle());
        binding.map.onResume();
        binding.map.getMapAsync(googleMap -> {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            binding.map.getContext(), R.raw.map_style));
            googleMap.setIndoorEnabled(true);
            googleMap.setMyLocationEnabled(true);
             binding.map.getMapAsync(this);
//            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//                @Override
//                public void onCameraIdle() {
//
//                    map = googleMap;
//
//                }
//            });


        });


    }

    public boolean isPermitted(Context context) {
        return +ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap!=null){
            map=googleMap;
            this.map.getUiSettings().setAllGesturesEnabled(false);
        }
        if (isPermitted(context)) {




            if (singleTrailViewVm.getTrail_id().equals("")) {

                // set marker at the center
                LatLng midLatLng = googleMap.getCameraPosition().target;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(midLatLng));

            } else {
              // get latlng from api
                LatLng ny = new LatLng(Double.parseDouble(singleTrailViewVm.getLatitude()), Double.parseDouble(singleTrailViewVm.getLongitude()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));


            }


        } else {
            checkPermission();
            Toast.makeText(context, "Location permission denied .", Toast.LENGTH_SHORT).show();

        }

    }

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,

                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );

            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        } else {



            // set marker at the center
            LatLng midLatLng = map.getCameraPosition().target;
            map.moveCamera(CameraUpdateFactory.newLatLng(midLatLng));

        }
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
                    Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
//    //handling button click toast
//    @BindingAdapter({"errorMsg"})
//    public static void abc(View view, String message) {
//        if (message != null) {
//            showSnackBar((Activity) view.getContext(), view.getId(), message);
//        }
//        //goToNextScreen(LocationPermissionActivity.class);
//    }



    public void refreshActivityWithoutAnimation(){
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }

    public static void hideKeyboard( Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
