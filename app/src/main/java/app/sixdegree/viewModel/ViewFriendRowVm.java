package app.sixdegree.viewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.getfriendslisting.Data;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.Splash;
import app.sixdegree.view.activity.home_module.adapter.FriendsAdapter;
import app.sixdegree.view.activity.home_module.fragments.MapFragment;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ViewFriendRowVm extends BaseVm {
    FriendsAdapter friendsAdapter;

    public String getTravellingStatus() {
        return frienddata.getTravelling() ? "Now Travelling" : "Home";
    }

    public void setTravellingStatus(String travellingStatus) {
        this.travellingStatus = travellingStatus;
        notifyPropertyChanged(BR.travellingStatus);
    }

    public String getFriend_id() {
        return String.valueOf(frienddata.getFriendId());
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
        notifyPropertyChanged(BR.friend_id);
    }

    @Bindable
    String friend_id="";

    @Bindable
String travellingStatus;
    @Bindable
    String totallikes;
    @Bindable
    String totaldistance;
    @Bindable
    String currentdays;
    @Bindable
    String totaldays;
    @Bindable
    String placename;
    @Bindable
    String profilePic;
    @Bindable
    String tripname;
    @Bindable
    String coverPic;
    Data frienddata;
    @Bindable
    String name;
List<Data>frienddatalist=new ArrayList<>(  );

@Bindable
    AppSession appSession;
    public ViewFriendRowVm(app.sixdegree.network.responses.getfriendslisting.Data data,FriendsAdapter friendsAdapter,AppSession appSession) {
        this.frienddata = data;
        this.friendsAdapter=friendsAdapter;
        this.appSession=appSession;
    }

    @BindingAdapter({"bind:profilePic"})
    public static void loadProfile(ImageView view, String profilePic) {
        if (profilePic != null) {
           // AppUtils.loadPicasso(profilePic, view);
            AppUtils.roundImageWithGlide(view,profilePic);
        }

    }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(RelativeLayout view, String coverPic) {
        if (coverPic != null) {
            AppUtils.setImageBg(view, coverPic);

        }
    }

    public String getTotallikes() {
        return frienddata.getTotalLikes() == 0 ? "0" : String.valueOf(frienddata.getTotalLikes());
    }

    public void setTotallikes(String totallikes) {
        this.totallikes = totallikes;
        notifyPropertyChanged(BR.totallikes);
    }
     public String getTotaldistance() {
         return frienddata.getTotalDistance() == 0 ? "N/A" :   String.format("%.4f",frienddata.getTotalDistance() )+" KM";
    }

    public void setTotaldistance(String totaldistance) {
        this.totaldistance = totaldistance;
        notifyPropertyChanged(BR.totaldistance);
    }

    public String getCurrentdays() {
        return frienddata.getCurrentDays() == 0 ? " " : String.valueOf(frienddata.getCurrentDays());
    }

    public void setCurrentdays(String currentdays) {
        this.currentdays = currentdays;
        notifyPropertyChanged(BR.currentdays);
    }

    public String getTotaldays() {
        return frienddata.getTotalDays() == 0 ? "N/A" : "/"+String.valueOf(frienddata.getTotalDays())+" DAYS";
    }

    public void setTotaldays(String totaldays) {
        this.totaldays = totaldays;
        notifyPropertyChanged(BR.totaldays);
    }

    public String getPlacename() {
        return frienddata.getHome_location();
    }

    public void setPlacename(String placename) {
        this.placename = placename;
        notifyPropertyChanged(BR.placename);
    }

    public String getTripname() {
        return frienddata.getTripName();
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
        notifyPropertyChanged(BR.tripName);
    }

    public String getCoverpic() {
        return frienddata.getTripPicture();
    }

    public void setCoverpic(String coverpic) {
        this.coverPic = coverpic;
        notifyPropertyChanged(BR.coverPic);
    }

    public String getProfilePic() {
        return frienddata.getFriendImage();
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
        notifyPropertyChanged(BR.profilePic);
    }

    public String getName() {
        return frienddata.getFriendName();
    }

    public void setName(String name) {
        this.name = name;
    }


    public  void removefriendDialog(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.MyAlertDialogStyle);
        builder.setTitle("Remove Friend");
        builder.setMessage("Are you sure you want to remove friend from your list?");
        builder.setPositiveButton("Remove", (dialog, which) -> {
            removeFriend();

        });

        builder.setNegativeButton("cancel", (dialog, which) -> {
            dialog.dismiss();

        });
        builder.show();
    }

    private void removeFriend() {




        apiService.removeFriend(appSession.getToken(), getFriend_id())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes data) {
                        if (data.getStatus()) {
                            friendsAdapter.removeRow(frienddata);
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
    public void removenew(View v) {
        String follow_Str = "";
        String title = "";
        // if ((Boolean) v.getTag()) {
        title="Remove Friend";
        follow_Str = "Are you sure you want to remove " +getName() +" from your list? ";
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
removeFriend();
                dialog.dismiss();

            }
        } );


        //  builder.show();
    }
    private void blockuser( Dialog dialog ) {
        apiService.blockuser(appSession.getToken(), getFriend_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes data) {
                        if (data.getStatus()) {
                            friendsAdapter.removeRow(frienddata);
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

    public void goToProfileFragment(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        ProfileFragment myFragment = new ProfileFragment(getFriend_id(),"Followed","");
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();    }


 public void goToMapFragment(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        frienddatalist.add( frienddata );
        MapFragment myFragment = new MapFragment(frienddatalist);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();    }

}
