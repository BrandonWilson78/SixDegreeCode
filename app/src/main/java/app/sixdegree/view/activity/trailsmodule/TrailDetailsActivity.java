package app.sixdegree.view.activity.trailsmodule;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivitySettingsBinding;
import app.sixdegree.databinding.ActivityTrailDetailsBinding;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.DatabaseClient;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.utils.SnappingHorizontalScrollView;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.viewModel.TripDetailsVm;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TrailDetailsActivity extends BaseActivity implements OnMapReadyCallback {
    TripDetailsVm vm;
    GoogleMap googleMap;
    List<LatLng> latLngList;
ImageView edit_trail;
String user_id="";
String tagged="";
    ActivityTrailDetailsBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          binding = DataBindingUtil.setContentView(this, R.layout.activity_trail_details);
        CountriesDao countriesDao = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().CountriesDao();

        if(getIntent().hasExtra( "tagged" )){
            tagged=getIntent().getStringExtra( "tagged" );
         }

        if(getIntent().hasExtra( "user_id" )){
            user_id=getIntent().getStringExtra("user_id");

            Log.e("userid--","in tripda--"+user_id);
        }
        vm = new TripDetailsVm(countriesDao, getSessionData().getToken(),user_id,getSession(),tagged,this);
        vm.setSessionInstance(getSession());
         binding.setViewModel(vm);

        View view=binding.getRoot();
        edit_trail=view.findViewById( R.id.edit_trail );

//        if(getIntent().getStringExtra("user_id").equals( String.valueOf(getSessionData().getId() ))){
//            edit_trail.setVisibility( View.VISIBLE );
//        }else {
//            edit_trail.setVisibility( View.GONE );
//        }


        if(tagged.equals( "tagged" )){
            edit_trail.setVisibility( View.GONE );
            binding.tripuser.setVisibility(View.VISIBLE);
        }else {

            binding.tripuser.setVisibility(View.GONE);
            if(getIntent().getStringExtra("user_id").equals( String.valueOf(getSessionData().getId() ))){
                edit_trail.setVisibility( View.VISIBLE );
            }else {
                edit_trail.setVisibility( View.GONE );
            }
        }

        Log.e("id--in tda--", "--" + getIntent().getStringExtra("id"));
        vm.setTripid(getIntent().getStringExtra("id"));
   RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
      //  RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.trailsRv.setLayoutManager(layoutManager);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        binding.trailsRv.setNestedScrollingEnabled(false);



        /*SnapHelper snapHelper = new PagerSnapHelper();*/
        /* LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 2;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }


                Log.e("targetPosition", targetPosition + "=");
                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;

                if (targetPosition > (lastItem - 1)) {
                    return RecyclerView.NO_POSITION;
                } else {

                    targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                    Log.e("targetPosition", "oPos" + targetPosition);
                    return targetPosition;

                }
            }
        };*/
        /*snapHelper.attachToRecyclerView(binding.trailsRv);*/
        // binding.scroll.setOnScreenSwitchListener(onScreenSwitchListener());

  /*      Picasso.with(this).load("https://www.goabroad.com/section_cloudinary/gaplabs/image/upload/images2/program_content/how-to-choose-the-best-adventure-travel-tour-companies-3-1504752803.jpg").into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                binding.topLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/


        binding.editTrail.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNewTrip.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("trip_id",    vm.getTripid());
            startActivity(intent);
        });

        vm.latLngList.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(s -> {
            this.latLngList = s;
            vm.setMapView(binding.map);
        }).doOnError(throwable -> {
            Log.e("error", "erro");
        }).subscribe();

        vm.googleMapObserver.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(s -> {
            this.googleMap = s;
        }).doOnError(throwable -> {
        }).subscribe();


    }



/*
     @OnClick(R.id.plan_next)
    public void onViewClicked() {
    goToNextScreen(PlanNextTripActivity.class);
    }
*/

    private SnappingHorizontalScrollView.OnScreenSwitchListener onScreenSwitchListener() {
        return screen -> Log.e("targetPosition", "screen" + screen);
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {

        if (vm != null) {
            vm.setLoading(false);


            if(getIntent().getStringExtra("id").equals( "" )){

            }else {
                vm.getTripDetails(getIntent().getStringExtra("id"));
            }

        }




        super.onResume();
    }


    public void handlemap() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Toast.makeText(context, "map loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void deleteTrail(String trail_id) {
        binding.pbar.setVisibility( View.VISIBLE );
        apiService.deletePitstop(getSession().getToken(),trail_id)
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes data) {

                        if (data.getStatus()) {
                            refreshActivity();
                            Toast.makeText( context, data.getMessage(), Toast.LENGTH_SHORT ).show();
                        } else {
                            Toast.makeText( context, data.getMessage(), Toast.LENGTH_SHORT ).show();
                        }

                        binding.pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onComplete() {
                        binding.pbar.setVisibility( View.GONE );
                    }
                });
    }



    public  void setFlagImage(String flag,ImageView iv_flag){
        Glide.with(getApplicationContext())
                .load(flag)
                .into(iv_flag);
    }

}
