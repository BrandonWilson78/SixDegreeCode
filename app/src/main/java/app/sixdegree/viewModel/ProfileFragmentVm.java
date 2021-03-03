package app.sixdegree.viewModel;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.BR;
import app.sixdegree.R;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.content_res.ContentRes;
import app.sixdegree.network.responses.getmutualfriends.GetMutualFriendsRes;
 import app.sixdegree.network.responses.getmutualfriends.X1;
import app.sixdegree.network.responses.settings_mod.profile.Data;
import app.sixdegree.network.responses.settings_mod.profile.Trail;
import app.sixdegree.network.responses.settings_mod.profile.UserProfile;
import app.sixdegree.network.responses.viewprofilefriends.MutualFriend;
import app.sixdegree.network.responses.viewprofilefriends.ViewFriendsProfile;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.chatchat.ChatDetails;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import app.sixdegree.view.activity.home_module.fragments.SearchFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static android.app.Activity.RESULT_OK;

public class ProfileFragmentVm extends BaseVm {

    public BehaviorSubject<String> behaviorSubjectTrail = BehaviorSubject.create();
    public BehaviorSubject<List<MutualFriend>> mutualfriendlist = BehaviorSubject.create();
    public BehaviorSubject<Boolean> istripexists = BehaviorSubject.create();
    public BehaviorSubject<String> nameforfriends = BehaviorSubject.create();
    public BehaviorSubject<String> followstatusapi = BehaviorSubject.create();
    public BehaviorSubject<Boolean> isLatestTrailNotExists = BehaviorSubject.create();
    public BehaviorSubject<Boolean> setSwipeRefreshFalse = BehaviorSubject.create();
    @Bindable
    ContentRes contentRes;
    @Bindable
    Data profileData;

    public app.sixdegree.network.responses.viewprofilefriends.Data getFriendProfileData() {
        return friendProfileData;
    }

    public void setFriendProfileData( app.sixdegree.network.responses.viewprofilefriends.Data friendProfileData ) {
        this.friendProfileData = friendProfileData;
        this.notifyPropertyChanged( BR.friendProfileData );
    }

    @Bindable
    app.sixdegree.network.responses.viewprofilefriends.Data friendProfileData;
    @Bindable
    String profilePic;

    public String getFollow_status_fromapi() {
        return follow_status_fromapi;
    }

    public void setFollow_status_fromapi( String follow_status_fromapi ) {
        this.follow_status_fromapi = follow_status_fromapi;
        notifyPropertyChanged( BR.follow_status_fromapi );
    }

    @Bindable
    String follow_status_fromapi="";
    @Bindable
    String coverPic;
    @Bindable
    String home;

    public String getNameoffriendid() {
        return nameoffriendid;
    }

    public void setNameoffriendid( String nameoffriendid ) {
        this.nameoffriendid = nameoffriendid;
        notifyPropertyChanged( BR.nameoffriendid );
    }

    @Bindable
    String nameoffriendid="";
    @Bindable
    String followers;
    @Bindable
    String following;
    @Bindable
    private boolean isLoading = false;


    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
        notifyPropertyChanged( BR.friend_id);
    }

    @Bindable
    String friend_id="";

    @Bindable
    private String name;

    @Bindable String bio;

    public String getBio() {
        return !TextUtils.isEmpty(bio)?bio:"N/A";
    }

    public void setBio(String bio) {
        this.bio = bio;
        notifyPropertyChanged(BR.bio);
    }

    @Bindable
    private Trail trailDetail;


    public app.sixdegree.network.responses.viewprofilefriends.Trail getTrails() {
        return trails;
    }

    public void setTrails(app.sixdegree.network.responses.viewprofilefriends.Trail trails) {
        this.trails = trails;
        notifyPropertyChanged(BR.trails);
    }

    @Bindable
    app.sixdegree.network.responses.viewprofilefriends.Trail trails;

    public ProfileFragmentVm(AppSession appSession,String friend_id) {
        this.friend_id=friend_id;
        setFriend_id(friend_id);
        setSessionInstance(appSession);



        if (friend_id.equals("")){
            getUserProfile();
        }else {
           getFriendsProfile();

           // getMutualFriendslist();
        }

    }

    @BindingAdapter({"bind:profilePic"})
    public static void loadProfile(ShapeableImageView view, String profilePic) {
         if (profilePic != null) {
           AppUtils.loadPicasso(profilePic,view);
        }

     }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(RelativeLayout view, String coverPic) {
          if (coverPic != null) {
              AppUtils.setImageBg(view,coverPic);

        }
     }

    public Trail getTrailDetail() {
        return trailDetail;
    }

    public void setTrailDetail(Trail trailDetail) {
        this.trailDetail = trailDetail;
          notifyPropertyChanged(BR.trailDetail);
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    void setLoading(boolean loading) {
        this.isLoading = loading;
        notifyPropertyChanged(BR.loading);
      }

    public Data getProfileData() {
        return profileData;
    }

    public void setProfileData(Data profileData) {
        this.profileData = profileData;
        notifyPropertyChanged(BR.profileData);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
        notifyPropertyChanged(BR.home);
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers + " Followers";
        notifyPropertyChanged(BR.followers);
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following + " Following";
        notifyPropertyChanged(BR.following);
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
        notifyPropertyChanged(BR.coverPic);
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
        notifyPropertyChanged(BR.profilePic);
        Log.e("profile_", profilePic + "    ==");


    }

    public ContentRes getContentRes() {
        return contentRes;
    }

    public void setContentRes(ContentRes contentRes) {
        this.contentRes = contentRes;
        notifyPropertyChanged(app.sixdegree.BR.contentRes);
    }

    public void getUserProfile() {
        setLoading(true);
        apiService.getProfileDetails(getSessionInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserProfile>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(UserProfile data) {
                        Log.e("profile_", data.getData().getProfileImage() + "    ==");
                        Log.e("Profilefrag", data+ " ==");
                        if (data.getStatus()){
                            setProfilePic(data.getData().getProfileImage());
                            setCoverPic(data.getData().getCoverImage());
                            setProfileData(data.getData());
                            setName(data.getData().getName());
                            setHome(data.getData().getHome());
                            setFollowers(String.valueOf(data.getData().getFollowers()));
                            setFollowing(String.valueOf(data.getData().getFollowings()));
                            setTrailDetail(data.getData().getTrail());
                            setBio(data.getData().getBio());
                            nameforfriends.onNext( String.valueOf( data.getData().getId() ) );
                            nameoffriendid= String.valueOf( data.getData().getId());
                            if (data.getData().getLatestTrip() != null) {
                                Log.e("dataset", "set");
                                String s=new Gson().toJson(data);
                                behaviorSubjectTrail.onNext(s);
                                isLatestTrailNotExists.onNext( false );
                            } else {
                                isLatestTrailNotExists.onNext( true );
                                Log.e("dataset", "not set");
                            }

                            if(data.getData().getTripsExists()){
                                istripexists.onNext( true );
                            }else {
                                istripexists.onNext( false );
                            }

                        } else {
                            setSwipeRefreshFalse.onNext( true );
                            setErrorMsg(data.getMessage());
                        }
                        setLoading(false);
                        notifyChange();
                    }

                    @Override
                    public void onError(Throwable e) {
                       setLoading(false);
                        Log.e("error", "==" + e.toString());

setSwipeRefreshFalse.onNext( true );
                    }

                    @Override
                    public void onComplete() {
                       setLoading(false);
                        setSwipeRefreshFalse.onNext( true );

                    }
                });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==11&&resultCode==RESULT_OK){
            int count= (int) data.getExtras().get("count");
            setFollowers(String.valueOf(count));
        }else    if (requestCode==12&&resultCode==RESULT_OK){
            int count= (int) data.getExtras().get("count");
            setFollowing(String.valueOf(count));

        }

    }

    //get friends profile details
    public void getFriendsProfile() {
        setLoading(true);
        apiService.getFriendProfile(getSessionInstance().getToken(),getFriend_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ViewFriendsProfile>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ViewFriendsProfile data) {
                        Log.e("profile_", data.getData().getProfileImage() + "    ==");
                        Log.e("Profilefrag", data+ "    ==");
                        if (data.getStatus()) {
                            setProfilePic(data.getData().getProfileImage());
                            setCoverPic(data.getData().getCoverImage());
                            setFriendProfileData(data.getData());
                            setName(data.getData().getName());
                            setHome(data.getData().getHome());
                            setFollowers(String.valueOf(data.getData().getFollowers()));
                            setFollowing(String.valueOf(data.getData().getFollowings()));
                            setBio(data.getData().getBio());
                            setFollow_status_fromapi(data.getData().getFollowStatus());
                            nameforfriends.onNext(String.valueOf(   data.getData().getId() ));
                            followstatusapi.onNext( data.getData().getFollowStatus() );
                            setTrails(data.getData().getTrail());

                            nameoffriendid= String.valueOf( data.getData().getId());
                       if(data.getData().getMutualFriends() !=null){
                           mutualfriendlist.onNext( data.getData().getMutualFriends() );

                           Log.e("mutualfriendlist", "in p f vm"+data.getData().getMutualFriends().size());

                       }

                       if (data.getData().getLatestTrip() != null) {
                                Log.e("dataset", "set");

                                String s=new Gson().toJson(data);
                                behaviorSubjectTrail.onNext(s);
                            } else {
                                Log.e("dataset", "not set");

                                isLatestTrailNotExists.onNext( true );
                            }






                            if(data.getData().getTripsExists()){
                                istripexists.onNext( true );
                            }else {
                                istripexists.onNext( false );
                            }

                        }
                        else {
                            setErrorMsg(data.getMessage());
                            setSwipeRefreshFalse.onNext( true );
                        }

                        setLoading(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        Log.e("error", "==" + e);
                        setSwipeRefreshFalse.onNext( true );

                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);
                        setSwipeRefreshFalse.onNext( true );
                    }
                });
    }


//
//    //get mutual friends list details
//    public void getMutualFriendslist() {
//        setLoading(true);
//        apiService.getMutualFriends(getSessionInstance().getToken(),getFriend_id())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<GetMutualFriendsRes>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(GetMutualFriendsRes data) {
//                         if (data.getStatus()) {
//
//
//
//                       if(data.getData().getMutual_friends() !=null){
//                           mutualfriendlist.onNext( data.getData().getMutual_friends().get1() );
//
//                           Log.e( "mutu fr lis coi--","--"+data.getData().getMutual_friends()  );
//                       }
//
//
//
//
//                        }
//                        else {
//                            setErrorMsg(data.getMessage());
//                        }
//
//                        setLoading(false);
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        setLoading(false);
//                        Log.e("error", "==" + e);
//
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        setLoading(false);
//
//                    }
//                });
//    }


    public void followUser( View view ) {
        setLoading(true);



        apiService.followuser(getSessionInstance().getToken(), getFriend_id()).subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {
                        setErrorMsg(res.getMessage());

                        if (res.getStatus()) {
view.setVisibility( View.GONE );
                             Toast.makeText( view.getContext(), res.getMessage(), Toast.LENGTH_SHORT ).show();

                        }
                        else {
                             Toast.makeText( view.getContext(), res.getMessage(), Toast.LENGTH_SHORT ).show();
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
    public void sendFriendRequest( View view) {
        setLoading(true);



        apiService.sendFriendRequest(getSessionInstance().getToken(), getFriend_id()).subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {
                        setErrorMsg(res.getMessage());

                        if (res.getStatus()) {
                          //  setUserStatus( res.getMessage() );
                            setErrorMsg( res.getMessage() );

                         //   setAdded( true );
                        }
                        else {
                           // setAdded( false );
                            Toast.makeText( view.getContext(), res.getMessage(), Toast.LENGTH_SHORT ).show();
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



    public void goToChatDetailActivity( View v){
    Intent intent = new Intent(v.getContext(), ChatDetails.class);
    intent.putExtra("name",getName());
    intent.putExtra("image",getProfilePic());
    intent.putExtra("chat_group_id", "0");
    intent.putExtra("from_user_id",String.valueOf(sessionInstance.getData().getId()));
    intent.putExtra("to_user_id", getFriend_id());
    v.getContext().startActivity(intent);
}


    public void goToSearchFragment(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        SearchFragment myFragment = new SearchFragment( );
        activity.getSupportFragmentManager().beginTransaction().replace( R.id.frame_container, myFragment).addToBackStack(null).commit();    }




}
