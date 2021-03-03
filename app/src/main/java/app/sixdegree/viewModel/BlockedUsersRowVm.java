package app.sixdegree.viewModel;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.followerres.CommonFriendsModel;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.blockedusers.adapter.BlockedUsersAdapter;
import app.sixdegree.view.activity.follower_module.adapter.FollowersAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BlockedUsersRowVm extends BaseVm {
    @Bindable
    private boolean isLoading;
    String profilePic;
    BlockedUsersAdapter followersAdapter;
    String id;
    String location;

    @Bindable
    String name;

    private CommonFriendsModel dataModel;

    public BlockedUsersRowVm( CommonFriendsModel dataModel, BlockedUsersAdapter followersAdapter) {
        this.dataModel = dataModel;
        this.followersAdapter = followersAdapter;
    }

    public BlockedUsersRowVm() {

    }

    @BindingAdapter({"bind:profilePic"})
    public static void loadProfile(ImageView view, String profilePic) {
        if (profilePic != null) {
            AppUtils.loadPicasso(profilePic,view);


        } else {
            Log.e("profile", "null");
        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.isLoading);
        notifyPropertyChanged(BR.loading);
    }

    public String getId() {
        return !TextUtils.isEmpty(dataModel.getId().toString()) ? dataModel.getId().toString() : "N/A";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePic() {
        return !TextUtils.isEmpty(dataModel.getProfilePic()) ? dataModel.getProfilePic() : "N/A";
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLocation() {
        return !TextUtils.isEmpty(dataModel.getLocation()) ? dataModel.getLocation() : "N/A";
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return !TextUtils.isEmpty(dataModel.getUserName()) ? dataModel.getUserName() : "Six Degree User";
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommonFriendsModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(CommonFriendsModel dataModel) {
        this.dataModel = dataModel;
    }

    @Bindable
    public String getTitle() {
        return !TextUtils.isEmpty(dataModel.getUserName()) ? dataModel.getUserName() : "";
    }


    public void unFollowUser(View v) {
        String follow_Str = "";
             follow_Str = "Are you sure to Unblock "+getName();

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(follow_Str);
         builder.setPositiveButton("Unblock", (dialog, which) -> {
            Toast.makeText(v.getContext(), "Removing...", Toast.LENGTH_SHORT).show();

             unblockuser();

        });

         builder.setNegativeButton("Cancel", ((dialog, which) -> {

        }));

        builder.show();


    }


  private void unblockuser() {
        apiService.unblockUser(followersAdapter.getToken(), getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes data) {
                        if (data.getStatus()) {
                            followersAdapter.removeRow(dataModel);
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
