package app.sixdegree.utils.workmanager;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import app.sixdegree.network.ApiFactory;
import app.sixdegree.network.ApiService;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.utils.AppSession;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UploadWorker extends Worker {
AppSession appSession=new AppSession(getApplicationContext());
ApiService apiService= ApiFactory.getRetrofitInstance().create(ApiService.class);

    public UploadWorker(
        @NonNull Context context,
        @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
      // Do the work here--in this case, upload the images.

      uploadLocation();

      // Indicate whether the task finished successfully with the Result
      return Result.success();
    }


    public void uploadLocation() {


        if (SmartLocation.with(getApplicationContext()).location().state().isNetworkAvailable()) {


            SmartLocation.with(getApplicationContext()).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            if (location.getLatitude() == 0.0 || location.getLongitude() == 0.0) {
//                                    LatLng midLatLng = googleMap.getCameraPosition().target;
//                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(midLatLng));


                                // Toast.makeText(getActivity(), "Unable to get current position.", Toast.LENGTH_SHORT).show();
                            } else {
                                //get lat lng and set location
                                LatLng ny = new LatLng(location.getLatitude(), location.getLongitude());


                                HashMap<String, String> map = new HashMap<>();
                                map.put("geo_location_latitude", String.valueOf(ny.latitude));


                                map.put("geo_location_longitude", String.valueOf(ny.longitude));


                                apiService.updateLocation(appSession.getToken(), map).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(new Observer<BaseRes>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(BaseRes res) {

                                                if (res.getStatus()) {
                                                    Log.e("upload location--","--"+res.getMessage());
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

                        }
                    });


        }


    }

}