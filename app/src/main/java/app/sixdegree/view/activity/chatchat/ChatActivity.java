package app.sixdegree.view.activity.chatchat;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.followingres.FollowingRes;
import app.sixdegree.network.responses.getallchats.Datum;
import app.sixdegree.network.responses.getfriendslisting.Data;
import app.sixdegree.network.responses.getfriendslisting.GetFriendsListing;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.chatchat.adapters.ChatAdapter;
import app.sixdegree.view.activity.chatchat.adapters.FriendMsgAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ChatActivity extends BaseActivity {
ProgressBar progressBar;
    RecyclerView rv_list;
    FriendMsgAdapter chatAdapter;
    List<Data> dataList = new ArrayList<>();
AppSession appSession;
    TextView txt_top_header;
 Context context=this;
 TextView dummytext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat );
        appSession=new AppSession( this );
        rv_list = (RecyclerView) findViewById( R.id.rv_list );
        dummytext = (TextView) findViewById( R.id.dummytext );
        progressBar = (ProgressBar) findViewById( R.id.pbar );
         txt_top_header = (TextView) findViewById( R.id.txt_top_header );

        LinearLayoutManager layoutManager = new LinearLayoutManager( context );
        rv_list.setLayoutManager( layoutManager );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( displayMetrics );
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        chatAdapter = new FriendMsgAdapter( dataList, context, width,appSession );
        rv_list.setAdapter( chatAdapter );
        chatAdapter.notifyDataSetChanged();
        findViewById( R.id.back ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
               onBackPressed();
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
                            chatAdapter.notifyDataSetChanged();
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
