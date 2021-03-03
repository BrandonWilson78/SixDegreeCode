package app.sixdegree.view.activity.trailsmodule.previoustrips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.sixdegree.R;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.getlatesttrip.GetLatestTripRes;
import app.sixdegree.network.responses.getprevioustrips.Data;
import app.sixdegree.network.responses.getprevioustrips.GetPreviousTripsRes;
import app.sixdegree.network.responses.tripsinterest.InterestTripRes;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.home_module.PreviousTripsAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PreviousTrips extends BaseActivity {
    PreviousTripsAdapter previousTripsAdapter;
    LatestTripsAdapter latestTripsAdapter;
    RecyclerView rv_previoustrips;
    RecyclerView rv_latesttrip;
    ArrayList<Data> previoustriplist=new ArrayList<>(  );
    ArrayList<LatestTripModel> latestTripModelArrayList=new ArrayList<>(  );


    ProgressBar pbar;
    TextView tv_dummytext;
    ImageView back_btn;
    String id="";
    app.sixdegree.network.responses.getlatesttrip.Data data;
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_previous_trips );
        rv_previoustrips=findViewById( R.id.rv_previoustrips );
        rv_latesttrip=findViewById( R.id.rv_latesttrip );
        tv_dummytext=findViewById( R.id.tv_dummytext );
        back_btn=findViewById( R.id.back_btn );
        pbar=findViewById( R.id.pbar );

if(getIntent().hasExtra( "id" )){
      id =getIntent().getStringExtra( "id" );
    Log.e("id in pr tr","--"+id);
}

        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        } );
        LinearLayoutManager linearLayoutManager=new GridLayoutManager( this,2 );
        previousTripsAdapter=new PreviousTripsAdapter(this,previoustriplist,id,String.valueOf(getSessionData().getId()));

        rv_previoustrips.setLayoutManager( linearLayoutManager );
        rv_previoustrips.setAdapter( previousTripsAdapter );

        LinearLayoutManager linearLayoutManager1=new GridLayoutManager( this,2 );
        latestTripsAdapter=new LatestTripsAdapter(this,latestTripModelArrayList,id,String.valueOf(getSessionData().getId()));

        rv_latesttrip.setLayoutManager( linearLayoutManager1 );
        rv_latesttrip.setAdapter( latestTripsAdapter );


        if(isNetworkConnected())
        {
            getPreviousTrips();
        }else {
            createInternetMsgToast();
        }
    }



    public void getPreviousTrips() {
    pbar.setVisibility( View.VISIBLE );
        apiService.getPreviousTrips(getSession().getToken(),id)
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetPreviousTripsRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetPreviousTripsRes data) {

                        if (data.getStatus()) {
                            previoustriplist.addAll( data.getData() );
                //  Collections.reverse( previoustriplist );
                            previousTripsAdapter.notifyDataSetChanged();


                            if(data.getData().size()==0){
                                tv_dummytext.setVisibility( View.GONE );
                                rv_previoustrips.setVisibility( View.GONE

                                );


                                rv_previoustrips.setVisibility( View.GONE );

                                 getlatesttrip();
                            }else {
                                tv_dummytext.setVisibility( View.GONE );
                                rv_previoustrips.setVisibility( View.VISIBLE
                                );
                            }

                         } else {
                            Toast.makeText( context, data.getMessage(), Toast.LENGTH_SHORT ).show();
                        }

                        pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onError(Throwable e) {
                        pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onComplete() {
                        pbar.setVisibility( View.GONE );
                    }
                });
    }


    public void deleteTrip(String trip_id) {
    pbar.setVisibility( View.VISIBLE );
        apiService.deleteTrip(getSession().getToken(),trip_id)
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

                        pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onError(Throwable e) {
                        pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onComplete() {
                        pbar.setVisibility( View.GONE );
                    }
                });
    }

    public void getlatesttrip() {
    pbar.setVisibility( View.VISIBLE );
        apiService.getLatestTrip(getSession().getToken())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetLatestTripRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {
                    }

                    @Override
                    public void onNext(GetLatestTripRes data) {
                        if (data.getStatus()) {
                            LatestTripModel latestTripModel=new LatestTripModel( data.getData().getId(),data.getData().getName(),
                                    data.getData().getStartDate(),data.getData().getTrails().size(),
                                    data.getData().getTripPicture(),data.getData().getUserId());
                            latestTripModelArrayList.add( latestTripModel );
                            latestTripsAdapter.notifyDataSetChanged();

                            if(data.getData() == null
                            ){
                                tv_dummytext.setText( "No trips found !" );
                                tv_dummytext.setVisibility( View.VISIBLE );
                                rv_latesttrip.setVisibility( View.GONE );
                                rv_previoustrips.setVisibility( View.GONE );
                            }

                        } else {
                            Toast.makeText( context, data.getMessage(), Toast.LENGTH_SHORT ).show();
                        }

                        pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onError(Throwable e) {
                        pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onComplete() {
                        pbar.setVisibility( View.GONE );
                    }
                });
    }
}
