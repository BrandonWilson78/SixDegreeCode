package app.sixdegree.view.activity.savedtrips;

import android.os.Bundle;
import android.util.Log;
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
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.DatabaseClient;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.getallsavedpitstops.GetAllSavedPitstops;
import app.sixdegree.network.responses.getallsavedpitstops.Pitstop;
import app.sixdegree.network.responses.gettaggedtrips.Data;
import app.sixdegree.network.responses.gettaggedtrips.GetTaggedTripsRes;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.taggedtrips.TaggedTripsAdapter;
import app.sixdegree.view.activity.trailsmodule.adapters.TrailAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookmarkedTrips extends BaseActivity {
    BookmarkedtripsAdapter bookmarkedtripsAdapter;
     RecyclerView rv_taggedtrips;
     String nameforfriends="";
  ArrayList<Pitstop> getTaggedTripslist=new ArrayList<Pitstop>(  );


    ProgressBar pbar;
    TextView tv_dummytext;
    ImageView back_btn;
String tagged="";
    app.sixdegree.network.responses.getlatesttrip.Data data;
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bookmark_pitstops );
        rv_taggedtrips=findViewById( R.id.rv_saved );
         tv_dummytext=findViewById( R.id.tv_dummytext );
        back_btn=findViewById( R.id.back_btn );
        pbar=findViewById( R.id.pbar );
        CountriesDao countriesDao = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().CountriesDao();



        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        } );


        LinearLayoutManager linearLayoutManager1=new GridLayoutManager( this,2 );
     //   bookmarkedtripsAdapter=new BookmarkedtripsAdapter(this,getTaggedTripslist,nameforfriends);

        bookmarkedtripsAdapter = new BookmarkedtripsAdapter(getTaggedTripslist,countriesDao, this,getSession().getToken(),getSession()/*user_id,*//*appSession,getTripenddate(),tagged*/);

        rv_taggedtrips.setLayoutManager( linearLayoutManager1 );
        rv_taggedtrips.setAdapter( bookmarkedtripsAdapter );

getAllBookmarksPitstop();

    }


    public void removebookmark(String id) {

        pbar.setVisibility( View.VISIBLE );
        apiService.removePitstop(getSession().getToken(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseRes data) {

                        if (data.getStatus()) {
                            refreshActivity();
                            Toast.makeText(getApplicationContext(), data.getMessage() , Toast.LENGTH_SHORT).show();



                        } else {
                            Toast.makeText(getApplicationContext(), data.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        pbar.setVisibility( View.GONE );
                        Log.e("trail size", e.toString() + "==");

                    }

                    @Override
                    public void onComplete() {
                        pbar.setVisibility( View.GONE );


                    }
                });

    }


    public void getAllBookmarksPitstop() {


        pbar.setVisibility( View.VISIBLE );
        apiService.getAllBookmarksPitstop(getSession().getToken())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetAllSavedPitstops>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetAllSavedPitstops data) {

                        if (data.getStatus()) {

   getTaggedTripslist.addAll( data.getPitstops() );
                            bookmarkedtripsAdapter.notifyDataSetChanged();


                            if(data.getPitstops().size()==0){
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
                        Log.e("error         in pit","--"+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        pbar.setVisibility( View.GONE );
                    }
                });
    }


}
