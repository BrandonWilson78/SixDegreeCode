package app.sixdegree.viewModel;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.followerres.CommonFriendsModel;
import app.sixdegree.network.responses.followerres.FollowerRes;
import app.sixdegree.network.responses.followingres.FollowingRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.follower_module.adapter.FollowersAdapter;
import app.sixdegree.view.activity.home_module.fragments.SearchFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataViewModel extends BaseVm {
    private static final String TAG = "DataViewModel";

    public String getDummytext() {
        return dummytext;
    }

    public void setDummytext( String dummytext ) {
        this.dummytext = dummytext;
        notifyPropertyChanged( BR.dummytext );
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible( Boolean visible ) {
        isVisible = visible;
        notifyPropertyChanged( BR.isVisible );
    }

    @Bindable
    public Boolean isVisible;

    @Bindable
    String dummytext="No Data Found.";

    @Bindable
    Boolean isLoading;
    private FollowersAdapter adapter;
    public List<CommonFriendsModel> data;

    public String getNameforfriends() {
        return nameforfriends;
    }

    public void setNameforfriends( String nameforfriends ) {
        this.nameforfriends = nameforfriends;
        notifyPropertyChanged( BR.nameforfriends );
    }
    @Bindable
    String nameforfriends="";


    public DataViewModel(Boolean isFollower, AppSession appSession,String nameforfriends) {
        setSessionInstance(getSessionInstance());
        data = new ArrayList<>();
        setSessionInstance(appSession);
        adapter = new FollowersAdapter(isFollower, getSessionInstance().getToken(),nameforfriends,appSession);
        adapter.updateData(data);
        this.nameforfriends=nameforfriends;
    }

    public void tearDown() {
        // perform tear down tasks, such as removing listeners
    }

    @Bindable
    public List<CommonFriendsModel> getData() {
        return this.data;
    }

    public void setData(List<CommonFriendsModel> data) {
        this.data = data;
        adapter.notifyDataSetChanged();



    }

    @Bindable
    public FollowersAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(FollowersAdapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }


    public Boolean getLoading() {
        return isLoading;
    }

    public void setLoading(Boolean isLoading) {
        this.isLoading = isLoading;
        notifyChange();
    }

    public void getFollower() {
        setLoading(true);
        apiService.getFollowers(getSessionInstance().getToken(),"", getNameforfriends())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FollowerRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FollowerRes data) {
                        ArrayList<CommonFriendsModel> list = new ArrayList<>();
                        if (data.getStatus()) {
                            for (int i = 0; i < data.getData().size(); i++) {
                                list.add(new CommonFriendsModel(data.getData().get(i).getFollowerName(),
                                        data.getData().get(i).getFollowerPic(),
                                        data.getData().get(i).getLocation(),data.getData().get( i ).getFriend_status(),
                                        data.getData().get( i ).getFollow_status(),data.getData().get(i).getFromUserId()));
                            }

                            if(data.getData().size()==0){
                                setDummytext( "No Followers found.");
                                setVisible( true );
                            }else {
                                setVisible( false );
                            }
                            Log.e( "size f","--"+data.getData().size() );
                            adapter.updateData(list);
                            adapter.notifyDataSetChanged();


                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        Log.e("error", "==" + e);

                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);

                    }
                });
    }


    public void getfollowings() {
        setLoading(true);
        apiService.getFollowing(getSessionInstance().getToken(),"", getNameforfriends())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FollowingRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FollowingRes data) {
                        ArrayList<CommonFriendsModel> list = new ArrayList<>();
                        if (data.getStatus()) {
                            for (int i = 0; i < data.getData().size(); i++) {
                                list.add(new CommonFriendsModel(data.getData().get(i).getFollowingName(), data.getData().get(i).getFollowingPic(),
                                        data.getData().get(i).getLocation(),data.getData().get( i ).getFriend_status(),
                                        data.getData().get( i ).getFollow_status(), data.getData().get(i).getToUserId()));

                            }

                            Log.e( "size f","--"+data.getData().size() );


                            if(data.getData().size()==0){
                                setDummytext( "No Followings found." );
                                setVisible( true );
                            }else {
                                setVisible( false );
                            }
                            adapter.updateData(list);
                            adapter.notifyDataSetChanged();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        Log.e("error", "==" + e);


                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);

                    }
                });
    }



}
