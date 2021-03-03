package app.sixdegree.viewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import app.sixdegree.R;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.followerres.CommonFriendsModel;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.follower_module.adapter.FollowersAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FriendsVm extends BaseVm {
    @Bindable
    private boolean isLoading;
    String profilePic;
    FollowersAdapter followersAdapter;
    String id;
    String location;
    @Bindable
    String nameforfriends="";

    public Boolean getIsfollower() {
        return isfollower;
    }

    public void setIsfollower( Boolean isfollower ) {
        this.isfollower = isfollower;
        notifyPropertyChanged( BR.isfollower );
    }

    @Bindable
Boolean isfollower;
    @Bindable
    String name;

    private CommonFriendsModel dataModel;

    public FriendsVm(CommonFriendsModel dataModel, FollowersAdapter followersAdapter,Boolean isfollower,String nameforfriends) {
        this.dataModel = dataModel;
        this.followersAdapter = followersAdapter;
        this.isfollower=isfollower;
        this.nameforfriends=nameforfriends;
    }

    public FriendsVm() {

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
        String title = "";
       // if ((Boolean) v.getTag()) {
            title="unfollow";
            follow_Str = "Are you sure to unfollow "+getName();
       /* } else {
            title="unfriend";
            follow_Str = "Are you sure to unfriend "+getName();
        }*/



        LayoutInflater inflater = ((Activity)v.getContext()).getLayoutInflater();
        View alertLayout = inflater.inflate( R.layout.friends_dialog, null);

        final TextView title_ = alertLayout.findViewById(R.id.title);
        final TextView message = alertLayout.findViewById(R.id.message);
        final TextView cancel = alertLayout.findViewById(R.id.cancel);
        final TextView remove = alertLayout.findViewById(R.id.remove);
        final TextView block = alertLayout.findViewById(R.id.block);



title_.setText( title );
message.setText( follow_Str );

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());


        builder.setView( alertLayout );





//         builder.setPositiveButton("Remove", (dialog, which) -> {
//            Toast.makeText(v.getContext(), "Removing...", Toast.LENGTH_SHORT).show();
//            if ((Boolean) v.getTag()) {
//                remove();
//            } else {
//                removeFollowing();
//            }
//
//
//        });
//
//         builder.setNeutralButton("Block", (dialog, which) -> {
//             Toast.makeText(v.getContext(), "Blocking...", Toast.LENGTH_SHORT).show();
//           blockuser();
//
//
//        });
//



//
//          builder.setNegativeButton("Cancel", ((dialog, which) -> {
//
//        }));

        AlertDialog dialog = builder.create();
        dialog.show();
        cancel.setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                dialog.dismiss();
            }
        }



);
        block.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                blockuser(dialog);
dialog.dismiss();
            }
        } );

        remove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {


                if (getIsfollower()) {
                    remove();
                    dialog.dismiss();
                } else {
                    removeFollowing();
                    dialog.dismiss();
                }


//                if ((boolean) v.getTag()) {
//                    remove();
//                } else {
//                    removeFollowing();
//                }
            }
        } );


      //  builder.show();
    }

    /*public void unfriend(View vv) {
        Dialog view = new Dialog(vv.getContext());
        View v = vv.getContext().getLayoutInflater().inflate(R.layout.friends_dialog, null);
        view.setContentView(v);

        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView remove = view.findViewById(R.id.remove);
        TextView block = view.findViewById(R.id.block);


        if (isFollower) {
            title.setText("Unfollow");
            message.setText("Are you sure to unfollow saar &");
        } else {
            title.setText("unfriend");
            message.setText("Are you sure to unfriend saar &");
        }

        cancel.setOnClickListener(vv -> {
            view.dismiss();
        });
        remove.setOnClickListener(vv -> {
            view.dismiss();
        });
        remove.setOnClickListener(vv -> {
            view.dismiss();
        });
        view.show();
    }*/

    private void removeFollowing() {
        apiService.unFollowUser(followersAdapter.getToken(), getId())
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



    private void blockuser( Dialog dialog ) {
        apiService.blockuser(followersAdapter.getToken(), getId())
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
                            dialog.dismiss();
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

    private void remove() {
        apiService.removeFollower(followersAdapter.getToken(), getId())
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
