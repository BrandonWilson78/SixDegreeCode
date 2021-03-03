package app.sixdegree.view.tagmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.followingres.FollowingRes;
import app.sixdegree.network.responses.getfriendslisting.Data;
import app.sixdegree.network.responses.getfriendslisting.GetFriendsListing;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.chatchat.adapters.FriendMsgAdapter;
import app.sixdegree.view.activity.chatchat.adapters.TagFriendListAdapter;
import app.sixdegree.view.activity.trailsmodule.AddNewTrip;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FriendListForTagActivity extends BaseActivity {
ProgressBar progressBar;
    RecyclerView rv_list;
    TagFriendListAdapter tagFriendListAdapter;
    List<Data> dataList = new ArrayList<>();
AppSession appSession;
    TextView txt_top_header;
 Context context=this;
 TextView dummytext,tv_done;

    public static ArrayList<String> friendsids = new ArrayList<>();
    public static ArrayList<String> friendsname = new ArrayList<>();
    List<String> items=new ArrayList<>(  );
    String friendsidforview="";
    String friendsid="";
    String friendsnames="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_friendslistfortag);
        appSession=new AppSession( this );
        rv_list = (RecyclerView) findViewById( R.id.rv_list );
        dummytext = (TextView) findViewById( R.id.dummytext );
         progressBar = (ProgressBar) findViewById( R.id.pbar );
         txt_top_header = (TextView) findViewById( R.id.txt_top_header );
        tv_done = (TextView) findViewById( R.id.tv_done );

        LinearLayoutManager layoutManager = new LinearLayoutManager( context );
        rv_list.setLayoutManager( layoutManager );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( displayMetrics );
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if(getIntent().hasExtra( "friendsids" )){



            friendsidforview=getIntent().getStringExtra( "friendsids" );




if(friendsidforview.equals( "" )){

}else{
    items = Arrays.asList(friendsidforview.split("\\s*,\\s*"));
            }



            Log.e( "idsss--", "in in fr"+items);
            Log.e( "friendsidforview--", "in in fr"+friendsidforview);

        }
        tagFriendListAdapter = new TagFriendListAdapter( dataList, context, width,appSession ,items);
        rv_list.setAdapter( tagFriendListAdapter );
        tagFriendListAdapter.notifyDataSetChanged();



        if(getIntent().hasExtra( "friendsids" )){
            friendsidforview=getIntent().getStringExtra( "friendsids" );
        }

friendsname.clear();
friendsids.clear();

        findViewById( R.id.back ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
               onBackPressed();
            }
        } );




        tv_done.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                friendsid = TextUtils.join(",", friendsids);
                friendsnames = TextUtils.join(",", friendsname);
                Intent intent=new Intent( FriendListForTagActivity.this, AddNewTrip.class );
                intent.putExtra( "friends_ids", friendsid);
                intent.putExtra( "friends_names", friendsnames);


                setResult(RESULT_OK, intent);
                finish();

            }
        } );

        if(isNetworkConnected()){
            getFollowing();

        }else {
            createInternetMsgToast();
        }
    }


    public void getFollowing() {
        progressBar.setVisibility( View.VISIBLE );
        apiService.getFriendsListing(appSession.getToken(), "all"  )
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetFriendsListing>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetFriendsListing res) {
                        dataList.clear();
                        if (res.getStatus()) {
                            dataList.addAll(res.getData());
                            tagFriendListAdapter.notifyDataSetChanged();
                        }



                        if(res.getData().size()==0){
                            dummytext.setVisibility( View.VISIBLE );
                            rv_list.setVisibility( View.INVISIBLE );
                        }else {
                            dummytext.setVisibility( View.INVISIBLE );
                            rv_list.setVisibility( View.VISIBLE );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility( View.GONE );
                    }
                });


    }



}
