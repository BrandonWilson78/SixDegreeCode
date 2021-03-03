package app.sixdegree.viewModel;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import java.util.HashMap;

import app.sixdegree.R;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.sixdegreesearch.Data;
import app.sixdegree.network.responses.sixdegreesearch.SixdegreeMatched;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.home_module.adapter.SearchAdapter;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import app.sixdegree.view.activity.home_module.fragments.SearchFragment;
import app.sixdegree.view.activity.home_module.fragments.adapters.SearchFraggTripAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SixDegreeSearchRowVm extends BaseVm {
    public String getToken() {
        return token;
    }

    public void setToken( String token ) {
        this.token = token;
        notifyPropertyChanged( BR.token );
    }
@Bindable
boolean added;

    public boolean isAdded() {
        return added;
    }

    public void setAdded( boolean added ) {
        this.added = added;
        notifyPropertyChanged( BR.added );
    }

    public SearchFraggTripAdapter getSearchFraggTripAdapter() {
        return searchFraggTripAdapter;
    }

    public void setSearchFraggTripAdapter( SearchFraggTripAdapter searchFraggTripAdapter ) {
        this.searchFraggTripAdapter = searchFraggTripAdapter;
    }

    SearchFraggTripAdapter searchFraggTripAdapter;

    @Bindable
        String token="";
    SixdegreeMatched data;

    public SixDegreeSearchRowVm( SixdegreeMatched data, String token) {
        this.data=data;
        this.token=token;
        searchFraggTripAdapter=new SearchFraggTripAdapter();
        //getSearchFraggTripAdapter().updateData( data. );
        Log.e("data--","in search row"+data);
    }
    @BindingAdapter({"bind:setuserimg"})
    public static void loadfimage( ImageView view, String coverPic) {
        if (coverPic != null) {
            AppUtils.roundImageWithGlide(view, coverPic);
        }
    }
    @Bindable
    String name;

    public String getName() {
        return data.getName();
    }

    public void setName( String name ) {
        this.name = name;
        notifyPropertyChanged( BR.name );
    }

    public String getLocationname() {
        return data.getHome();


    }

    public void setLocationname( String locationname ) {
        this.locationname = locationname;
        notifyPropertyChanged( BR.locationname );
    }

    public String getMutualfriends() {
        return data.getMutualFriends()==0 ?"0 mutual friends" :String.valueOf( data.getMutualFriends()) +" mutual friends";
    }

    public void setMutualfriends( String mutualfriends ) {
        this.mutualfriends = mutualfriends;
        notifyPropertyChanged( BR.mutualfriends );
    }

    public String getUserimg() {
        return data.getProfileImage();
    }

    public void setUserimg( String userimg ) {
        this.userimg = userimg;
        notifyPropertyChanged( BR.userimg );
    }

  //  no_relation, confirm_request, pending_request, friends
    public String getUserStatus() {

        if(data.getFriend_status().equals( "pending_request" )){
           setAdded( true );
            return "Request pending";
        }else if(data.getFriend_status().equals( "friends" )){
            setAdded( true );
            return "Already added";
        }else if(data.getFriend_status().equals( "confirm_request" )){
            setAdded( true );
            return "Confirm request";
        }else {
            setAdded( false );
            return "";
        }

     }

    public void setUserStatus( String userStatus ) {
        UserStatus = userStatus;
        notifyPropertyChanged( BR.UserStatus );
    }

    @Bindable
    String UserStatus="";

    @Bindable
    String locationname="";
    @Bindable
    String mutualfriends;

    @Bindable
    String userimg="";

    public String getTo_user_id() {
        return String.valueOf(data.getId());
    }

    public void setTo_user_id( String to_user_id ) {
        this.to_user_id = to_user_id;
        notifyPropertyChanged( BR.to_user_id);
    }

    @Bindable
String to_user_id="";

    public boolean isLoading() {
        return loading;
    }

    public void setLoading( boolean loading ) {
        this.loading = loading;
        notifyPropertyChanged( BR.loading );
    }

    @Bindable
boolean loading;


    public void followUser( View view) {
        setLoading(true);



        apiService.sendFriendRequest(getToken(), getTo_user_id()).subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {
                        setErrorMsg(res.getMessage());

                        if (res.getStatus()) {
                            setUserStatus( res.getMessage() );
                          setErrorMsg( res.getMessage() );

                            setAdded( true );
                        }
                        else {
                       setAdded( false );
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


    public  void setvVisImg(ImageView view){
        if(getUserStatus().equals( "Request pending" )){
            view.setVisibility( View.GONE );
        }else {
            view.setVisibility( View.VISIBLE );
        }
    }


    public  void setvVisiTxt( TextView view){
        if(getUserStatus().equals( "Request pending" )){
            view.setVisibility( View.VISIBLE );
        }else {
            view.setVisibility( View.VISIBLE );
        }
    }
    public void goToProfileFragment( View view){

  AppCompatActivity activity = (AppCompatActivity) view.getContext();
        ProfileFragment myFragment = new ProfileFragment(String.valueOf(data.getId()),data.getFollowStatus(),data.getFriend_status());

        activity.getSupportFragmentManager().beginTransaction().replace( R.id.frame_container, myFragment).addToBackStack( "search" ).commit();    }
}
