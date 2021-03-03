package app.sixdegree.view.activity.taggedtrips;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.sixdegree.R;
import app.sixdegree.network.responses.getlatesttrip.GetLatestTripRes;
import app.sixdegree.network.responses.getprevioustrips.GetPreviousTripsRes;
import app.sixdegree.network.responses.gettaggedtrips.Data;
import app.sixdegree.network.responses.gettaggedtrips.GetTaggedTripsRes;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.home_module.PreviousTripsAdapter;
import app.sixdegree.view.activity.trailsmodule.previoustrips.LatestTripModel;
import app.sixdegree.view.activity.trailsmodule.previoustrips.LatestTripsAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaggedTrips extends BaseActivity {
    TaggedTripsAdapter taggedTripsAdapter;
     RecyclerView rv_taggedtrips;
     String nameforfriends="";
  ArrayList<Data> getTaggedTripslist=new ArrayList<>(  );


    ProgressBar pbar;
    TextView tv_dummytext;
    ImageView back_btn;
String tagged="";
    app.sixdegree.network.responses.getlatesttrip.Data data;
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_tagged_trips );
        rv_taggedtrips=findViewById( R.id.rv_taggedtrips );
         tv_dummytext=findViewById( R.id.tv_dummytext );
        back_btn=findViewById( R.id.back_btn );
        pbar=findViewById( R.id.pbar );


        if(getIntent().hasExtra( "nameforfriends" )){
            nameforfriends=getIntent().getStringExtra( "nameforfriends" );
            if(isNetworkConnected())
            {
                getTaggedTrips();
            }else {
                createInternetMsgToast();
            }
        }

        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        } );


        LinearLayoutManager linearLayoutManager1=new GridLayoutManager( this,2 );
        taggedTripsAdapter=new TaggedTripsAdapter(this,getTaggedTripslist,nameforfriends);

        rv_taggedtrips.setLayoutManager( linearLayoutManager1 );
        rv_taggedtrips.setAdapter( taggedTripsAdapter );



    }



    public void getTaggedTrips() {


        pbar.setVisibility( View.VISIBLE );
        apiService.getTaggedTripsRes(getSession().getToken(),nameforfriends)
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTaggedTripsRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetTaggedTripsRes data) {

                        if (data.getStatus()) {
                            getTaggedTripslist.addAll( data.getData() );
                            taggedTripsAdapter.notifyDataSetChanged();


                            if(data.getData().size()==0){
                                tv_dummytext.setVisibility( View.VISIBLE );
                                rv_taggedtrips.setVisibility( View.GONE

                                );



                             }else {
                                tv_dummytext.setVisibility( View.GONE );
                                rv_taggedtrips.setVisibility( View.VISIBLE );
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
