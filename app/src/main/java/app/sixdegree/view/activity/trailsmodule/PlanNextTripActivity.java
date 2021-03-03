package app.sixdegree.view.activity.trailsmodule;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivityPlanNextTripBinding;
import app.sixdegree.network.responses.gettripcategories.Data;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.viewModel.AddEditTrailVm;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlanNextTripActivity extends BaseActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    AddEditTrailVm addEditTrailVm;
    PowerMenu powerMenu;
    String tripid = "";
    String trail_id = "";

    Boolean whocan = false;
    Boolean mapstyle = false;
    Boolean cat = false;
    List<String> list = new ArrayList<>();
    List<String> sharingoptionslist = new ArrayList<>();
    List<Data> tripCatList = new ArrayList<>();
    GoogleMap map;
    ActivityPlanNextTripBinding binding;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    ProgressDialog pd;
    List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS);
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            String textSelected = item.getTitle();


            if (whocan) {
                addEditTrailVm.setWhoCanSee(textSelected);
            } else if (mapstyle) {
                addEditTrailVm.setMapStyle(textSelected);
            } else {
                addEditTrailVm.setCategory(textSelected);
            }
//            if (textSelected.equals("My Friends") || textSelected.equals("Public") || textSelected.equals("Friends of Friends")) {
//                addEditTrailVm.setWhoCanSee(textSelected);
//            } else if (textSelected.equals("roadmap") || textSelected.equals("satellite") || textSelected.equals("hybrid") || textSelected.equals("terrain")) {
//                addEditTrailVm.setMapStyle(textSelected);
//            } else {
//                addEditTrailVm.setCategory(textSelected);
//
//
//            }
            powerMenu.dismiss();
        }


    };

    //handling button click toast
    @BindingAdapter({"errorMsg"})
    public static void abc(View view, String message) {
        if (message != null) {
            showSnackBar((Activity) view.getContext(), view.getId(), message);
        }
        //goToNextScreen(LocationPermissionActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plan_next_trip);

        if (getIntent().hasExtra("trail_id")) {
            addEditTrailVm = new AddEditTrailVm(getSession().getToken(), getIntent().getStringExtra("trail_id"));
            binding.title.setText("Edit Pitstop");
            binding.btnSave.setText("Update Pitstop");
        } else {
            addEditTrailVm = new AddEditTrailVm(getSession().getToken(), "");
            binding.title.setText("New Pitstop");
            binding.btnSave.setText("Save");
        }
        binding.setViewModel(addEditTrailVm);


        View view = binding.getRoot();
        ImageView back = view.findViewById(R.id.ivback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent().hasExtra("id")) {
            tripid = getIntent().getStringExtra("id");
            Log.e("trip id ", "in plan next--" + tripid);
            addEditTrailVm.setTripid(tripid);
        }
        int isEdit = getIntent().getIntExtra("isEdit", 0);


        //get trail cat list
        addEditTrailVm.tripCat.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
            tripCatList = data;
            for (int i = 0; i < data.size(); i++) {
                list.add(data.get(i).getName());

            }

        }).subscribe();

        addEditTrailVm.loader.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {

            if (data) {
                createProgressDialog();
            } else {
                dismissProgressDialog();
            }

        }).subscribe();


        //get sharing options cat list
        addEditTrailVm.sharingoptionslist.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
            sharingoptionslist = data;


        }).subscribe();


        //observes api add trail to trip
        addEditTrailVm.isFinished.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {

            if (data) {
                finish();
                Toast.makeText(context, "Pitstop added successfully", Toast.LENGTH_SHORT).show();
            }
        }).subscribe();
        //observes api add trail to trip
        addEditTrailVm.isUpdatedFinished.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {

            if (data) {
                finish();
                Toast.makeText(context, "Pitstop updated successfully", Toast.LENGTH_SHORT).show();
            }
        }).subscribe();
        addEditTrailVm.getCountryFromLatLng.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
            //  showToast(PlanNextTripActivity.this, "--" + data.lat + "," +data.lng);
            com.google.maps.model.LatLng latlng = data;
            if (latlng != null) {
                addEditTrailVm.setCountry(getCountryByLatLng(latlng.lat, latlng.lng));

            }

        }).subscribe();

        // set trail picture
        findViewById(R.id.iv_trail).setOnClickListener(v -> {
            CropImage.activity()
                    .setAspectRatio(16, 9)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);


        });
   /*     switch (isEdit) {
            case 0:
                binding.title.setText("New Pitstop");
                break;
            case 1:
                binding.title.setText("Edit Pitstop");
                break;
            case 2:
                binding.title.setText("Plan Next Pitstop");
                break;
            default:
                binding.title.setText("New Pitstop");

        }*/
        binding.map.onCreate(new Bundle());
        binding.map.onResume();
         binding.map.getMapAsync(googleMap -> {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            binding.map.getContext(), R.raw.map_style));
            googleMap.setIndoorEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;





            }






            googleMap.setMyLocationEnabled(true);
            binding.map.getMapAsync(this);
             fixMapScrolling();
            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
/*

binding.scrollview.requestDisallowInterceptTouchEvent(true);
binding.scrollview.setFocusable(false);
binding.scrollview.setEnabled(false);
*/

                    map = googleMap;
                    //get latlng at the center by calling
                    LatLng midLatLng = googleMap.getCameraPosition().target;
                    Log.e("latlng", midLatLng.latitude + "," + midLatLng.longitude);
                    Log.e("ADDRESS", "--" + getAdressByLatLng(midLatLng.latitude, midLatLng.longitude));
                    addEditTrailVm.setLatitude(String.valueOf(midLatLng.latitude));
                    addEditTrailVm.setLongitude(String.valueOf(midLatLng.longitude));
                    addEditTrailVm.setAddress(getAdressByLatLng(midLatLng.latitude, midLatLng.longitude));
                    addEditTrailVm.setCountry(getCountryByLatLng(midLatLng.latitude, midLatLng.longitude));
                    getAdressByLatLng(midLatLng.latitude, midLatLng.longitude);

                    //    if (!trail_id.isEmpty()) {


//                        if (addEditTrailVm.getLatitude() != null) {
//                            LatLng ny = new LatLng(Double.parseDouble(addEditTrailVm.getLatitude()), Double.parseDouble(addEditTrailVm.getLongitude()));
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
//                        }
//                    } else {
//                        showToast(PlanNextTripActivity.this, "e--" + addEditTrailVm.getLatitude() + "," + addEditTrailVm.getLongitude());
//
                    //   }

                }
            });


        });






        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        binding.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartDate();
            }
        });


        binding.endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEndDate();
            }
        });


        binding.whoCanSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<String> list = new ArrayList<>();
//                list.add("My Friends");
//                list.add("Public");
//                list.add("Friends of Friends");
//


                  whocan=true;
                  cat=false;
                  mapstyle=false;

                if(sharingoptionslist.size()!=0){
                    show(sharingoptionslist, binding.whoCanSee);
                }

            }
        });


        binding.tvMapStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whocan=false;

                 cat=false;
                mapstyle=true;
                List<String> list = new ArrayList<>();
                list.add("roadmap");
                list.add("satellite");
                list.add("hybrid");
                list.add("terrain");

                show(list, binding.tvMapStyle);
            }
        });

        binding.categoryTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
   whocan=false;
                cat=true;
                mapstyle=false;

                show(list, binding.categoryTv);

            }
        });


        binding.edLocation.setOnClickListener(vv -> {
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this)
                    ;
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

    }


    private void fixMapScrolling() {

        binding.map.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        binding.scrollview.requestDisallowInterceptTouchEvent(true);
                        binding.map.setFocusable(true);
                        binding.scrollview.setFocusable(false);

                        break;
                    case MotionEvent.ACTION_UP:
                        binding.scrollview.requestDisallowInterceptTouchEvent(false);
                        binding.map.setFocusable(false);
                        binding.scrollview.setFocusable(true);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        binding.map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                           binding.scrollview.requestDisallowInterceptTouchEvent(true);
                           binding.map.setFocusable(true);
                           binding.scrollview.setFocusable(false);

                        break;
                    case MotionEvent.ACTION_UP:
                        binding.scrollview.requestDisallowInterceptTouchEvent(false);
                        binding.map.setFocusable(false);
                        binding.scrollview.setFocusable(true);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    public void setStartDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        addEditTrailVm.setStartdate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Log.e("  end date", "--" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
      //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void setEndDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        addEditTrailVm.setEnddate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Log.e("  end date", "--" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
      //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }



    public void show(List<String> list, View view) {
        List<PowerMenuItem> l1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            l1.add(new PowerMenuItem(list.get(i), false));
        }
        powerMenu = new PowerMenu.Builder(this)
                .addItemList(l1)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(ContextCompat.getColor(this, R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create(getResources().getFont(R.font.semibold), Typeface.NORMAL))

                .setSelectedTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
        powerMenu.showAtCenter(view);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addEditTrailVm.onActivityResult(requestCode, resultCode, data);


    }
    public void createProgressDialog() {


        if (pd != null) {
            pd.show();
        } else {
            pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setMessage("Please Wait...");
            pd.show();
        }

    }


    public void dismissProgressDialog() {


       /*  if (pd != null) {
            pd.dismiss();
        }*/

        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }

        /*try {

            if (pd != null)
                if (pd.isShowing()) ;
            pd.dismiss();
        } catch (WindowManager.BadTokenException e) {

        }*/

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (isPermitted(context)) {


            if (addEditTrailVm.getTrail_id().equals("")) {
                // if gps is enabled
                //  if (gpsTracker.getIsGPSTrackingEnabled()) {
                if (SmartLocation.with(context).location().state().isNetworkAvailable()) {


                    SmartLocation.with(context).location()
                            .oneFix()
                            .start(new OnLocationUpdatedListener() {
                                @Override
                                public void onLocationUpdated(Location location) {
                                    if (location.getLatitude() == 0.0 || location.getLongitude() == 0.0) {
                                        LatLng midLatLng = googleMap.getCameraPosition().target;
                                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(midLatLng));
                                     //   Toast.makeText(context, "Unable to get current position.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //get lat lng and set location
                                        LatLng ny = new LatLng(location.getLatitude(), location.getLongitude());
                                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));


                                    }
                                }
                            });


                } else {
                 //   Toast.makeText(context, "Device Location settings (GPS) settings is turned off.", Toast.LENGTH_SHORT).show();

                }

            } else {
                //get lat lng and set location
                addEditTrailVm.latlngs.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
                    //  showToast(PlanNextTripActivity.this, "--" + data.lat + "," +data.lng);
                    LatLng ny = new LatLng(data.lat, data.lng);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ny,12));
                }).subscribe();

              //  googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude) ,4) );

            }


        } else {
            checkPermission();
       //     Toast.makeText(context, "Location permission denied .", Toast.LENGTH_SHORT).show();

        }


    }


    public boolean isPermitted(Context context) {
        return +ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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


            SmartLocation.with(context).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            //get lat lng and set location
                            addEditTrailVm.latlngs.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
                                //  showToast(PlanNextTripActivity.this, "--" + data.lat + "," +data.lng);
                                LatLng ny = new LatLng(data.lat, data.lng);
                                map.moveCamera(CameraUpdateFactory.newLatLng(ny));
                            }).subscribe();


                        }
                    });


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
    }


}
