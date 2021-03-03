package app.sixdegree.viewModel;

import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.network.responses.followerres.CommonFriendsModel;
import app.sixdegree.network.responses.followerres.FollowerRes;
import app.sixdegree.network.responses.followingres.FollowingRes;
import app.sixdegree.network.responses.getblockedusersres.GetBlockedUsersRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.blockedusers.adapter.BlockedUsersAdapter;
import app.sixdegree.view.activity.follower_module.adapter.FollowersAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BlockedUsersVm extends BaseVm {

    private static final String TAG = "BlockedUserVm";

    @Bindable
    Boolean isLoading;
    private BlockedUsersAdapter adapter;
    public List<CommonFriendsModel> data;

    public BlockedUsersVm(Boolean isFollower, AppSession appSession) {
        setSessionInstance(getSessionInstance());
        data = new ArrayList<>();
        setSessionInstance(appSession);
        adapter = new BlockedUsersAdapter(isFollower, getSessionInstance().getToken());
        adapter.updateData(data);

        //getbllocked users api
        getBlockedUsers();
    }

    public String getDummytext() {
        return dummytext;
    }

    public void setDummytext( String dummytext ) {
        this.dummytext = dummytext;
        notifyPropertyChanged( BR. dummytext);
    }

    @Bindable
    String dummytext="No Data Found.";
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
    public List<CommonFriendsModel> getData() {
        return this.data;
    }

    public void setData(List<CommonFriendsModel> data) {
        this.data = data;
        adapter.notifyDataSetChanged();

    }

    @Bindable
    public BlockedUsersAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter( BlockedUsersAdapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged( BR.adapter);
    }


    public Boolean getLoading() {
        return isLoading;
    }

    public void setLoading(Boolean isLoading) {
        this.isLoading = isLoading;
        notifyChange();
    }

    public void getBlockedUsers() {
        setLoading(true);
        apiService.getBlockedUsers(getSessionInstance().getToken())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetBlockedUsersRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetBlockedUsersRes data) {
                        ArrayList<CommonFriendsModel> list = new ArrayList<>();
                        if (data.getStatus()) {
                            for (int i = 0; i < data.getData().size(); i++) {
                                list.add(new CommonFriendsModel(data.getData().get(i).getName(),
                                        data.getData().get(i).getProfile_image(),
                                        data.getData().get(i).getHome(),"no_relation","Not Followed", data.getData().get(i).getBlocked_user_id()));
                            }
                            adapter.updateData(list);
                            adapter.notifyDataSetChanged();



                            if(data.getData().size()==0){
                                setDummytext( "No Blocked Users found." );
                                setVisible( true );
                            }else {
                                setVisible( false );
                            }
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
