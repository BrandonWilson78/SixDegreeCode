package app.sixdegree.viewModel;

import android.content.Intent;
import android.view.View;

import androidx.databinding.Bindable;

import java.util.HashMap;

import app.sixdegree.BR;
import app.sixdegree.network.responses.followerres.FollowerRes;
import app.sixdegree.network.responses.getfollowerrequests.GetFollowerRequestsRes;
import app.sixdegree.network.responses.getinboxres.GetInboxRes;
import app.sixdegree.network.responses.sixdegreesearch.SixDegreeSearchRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.chatchat.ChatActivity;
import app.sixdegree.view.activity.chatchat.adapters.ChatAdapter;
import app.sixdegree.view.activity.home_module.adapter.FollowerRequestsAdapter;
 import app.sixdegree.view.activity.home_module.adapter.SearchAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationFragmentVm extends BaseVm {

AppSession appSession;

    public FollowerRequestsAdapter getFollowerRequestsAdapter() {
        return followerRequestsAdapter;
    }

    public void setFollowerRequestsAdapter( FollowerRequestsAdapter followerRequestsAdapter ) {
        this.followerRequestsAdapter = followerRequestsAdapter;
    }

    FollowerRequestsAdapter followerRequestsAdapter;

    public ChatAdapter getMessagesAdapter() {
        return messagesAdapter;
    }

    public void setMessagesAdapter( ChatAdapter messagesAdapter ) {
        this.messagesAdapter = messagesAdapter;
    }

    ChatAdapter messagesAdapter;
    public boolean isLoading() {
        return loading;
    }

    public void setLoading( boolean loading ) {
        this.loading = loading;
        notifyPropertyChanged( BR.loading);
    }

    @Bindable
    boolean loading;

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible( Boolean visible ) {
        isVisible = visible;
        notifyPropertyChanged( BR.isVisible );
    }

    public String getDummytext() {
        return dummytext;
    }

    public void setDummytext( String dummytext ) {
        this.dummytext = dummytext;
    }

    @Bindable
   public Boolean isVisible;
    @Bindable
    String dummytext="No Data Found.";
    @Bindable
    String dummytextmsg="No Chats yet.";
    public String getDummytextmsg() {
        return dummytextmsg;
    }

    public void setDummytextmsg( String dummytextmsg ) {
        this.dummytextmsg = dummytextmsg;
       notifyPropertyChanged( BR.dummytextmsg );
    }



    public NotificationFragmentVm( AppSession appSession){
        this.appSession=appSession;
        followerRequestsAdapter=new FollowerRequestsAdapter(appSession.getToken());
         getFollowerRequests();


    }






    public void getFollowerRequests() {
        setLoading(true);
            apiService.getFriendRequests(appSession.getToken()).subscribeOn( Schedulers.io())
                    .observeOn( AndroidSchedulers.mainThread())
                    .subscribeWith(new Observer<GetFollowerRequestsRes>() {
                        @Override
                        public void onSubscribe( Disposable d) {

                        }

                        @Override
                        public void onNext(GetFollowerRequestsRes res) {
                            setErrorMsg(res.getMessage());
                            if (res.getStatus()) {
  getFollowerRequestsAdapter().setData( res.getData() );
  notifyChange();

  if(res.getData().size()==0){
      setDummytext( "No Requests found." );
      setVisible( true );
  }else {
      setVisible( false );
  }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            setLoading(false);


                        }

                        @Override
                        public void onComplete() {
                            setLoading(false);
                        }
                    });
        }

    public void getInboxMessages() {
        setLoading(true);
        apiService.getInboxMessages(appSession.getToken()).subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetInboxRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetInboxRes res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {
                       //     getMessagesAdapter().setData( res.getData() );
                            notifyChange();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);


                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);
                    }
                });
    }



}

