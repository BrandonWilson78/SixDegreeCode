package app.sixdegree.viewModel;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Bindable;

import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import app.sixdegree.BR;
import app.sixdegree.R;
import app.sixdegree.network.responses.getfriendslisting.GetFriendsListing;
import app.sixdegree.network.responses.getpendingrequests.GetPendingRequests;
import app.sixdegree.network.responses.viewfriends.ViewFriends;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.chatchat.ChatActivity;
import app.sixdegree.view.activity.chatchat.ChatDetails;
import app.sixdegree.view.activity.home_module.adapter.FriendsAdapter;
import app.sixdegree.view.activity.home_module.adapter.PendingRequestsAdapter;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import app.sixdegree.view.activity.home_module.fragments.SearchFragment;
import app.sixdegree.view.activity.loginModule.AddFriendsActivity;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ViewFriendsVm extends BaseVm {

    @Bindable
    String travellingnow = "Travelling now";
    String token = "";

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
Boolean isVisible;

    public Boolean getDummyVisible() {
        return isDummyVisible;
    }

    public void setDummyVisible( Boolean dummyVisible ) {
        isDummyVisible = dummyVisible;
        notifyPropertyChanged( BR.isDummyVisible );
    }

    @Bindable
Boolean isDummyVisible;
    @Bindable
    String dummytext="No Data Found.";

    public String getPendingdummytext() {
        return pendingdummytext;
    }

    public void setPendingdummytext( String pendingdummytext ) {
        this.pendingdummytext = pendingdummytext;
        notifyPropertyChanged( BR.pendingdummytext );
    }

    @Bindable
    String pendingdummytext="No Data Found.";
    @Bindable
    boolean isLoading;
    FriendsAdapter friendsAdapter;

    public PendingRequestsAdapter getPendingRequestsAdapter() {
        return pendingRequestsAdapter;
    }

    public void setPendingRequestsAdapter( PendingRequestsAdapter pendingRequestsAdapter ) {
        this.pendingRequestsAdapter = pendingRequestsAdapter;
    }

    PendingRequestsAdapter pendingRequestsAdapter;


    public AppSession getAppSession() {
        return appSession;
    }

    public void setAppSession( AppSession appSession ) {
        this.appSession = appSession;
        notifyPropertyChanged( BR.appSession );
    }

    @Bindable
    AppSession appSession;
    public ViewFriendsVm( String token, AppSession appSession ) {
        this.token = token;
        this.appSession=appSession;
        friendsAdapter = new FriendsAdapter(appSession);
        pendingRequestsAdapter=new PendingRequestsAdapter( appSession.getToken() );
      // fetchAllFriends();
    }

    public String getTravellingnow() {
        return travellingnow;
    }

    public void setTravellingnow(String travellingnow) {
        this.travellingnow = travellingnow;
        notifyPropertyChanged(BR.travellingnow);
    }

    public String getToken() {
        return token;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public FriendsAdapter getFriendsAdapter() {
        return friendsAdapter;
    }

    public void setFriendsAdapter(FriendsAdapter friendsAdapter) {
        this.friendsAdapter = friendsAdapter;
    }
    public void goToChatList( View view){
        Intent intent=new Intent( view.getContext(), ChatActivity.class );
        view.getContext().startActivity( intent );

    }
    public void fetchFriends(String type) {
        setLoading(true);
        apiService.getFriendsListing(getToken(), type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetFriendsListing>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetFriendsListing res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {

                                      getFriendsAdapter().setData(res.getData());




                            notifyChange();

                            if(res.getData().size()==0){
                                isVisible=true;
                                setDummytext( "No friends found." );
                            }else {
                                isVisible=false;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);

                        Log.e("eror in f f","--"+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);
                    }
                });
    }
    public void fetchPendingRequests( ) {
        setLoading(true);
        apiService.getPendingRequests(getToken()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetPendingRequests>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetPendingRequests res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {

                                      getPendingRequestsAdapter().setData(res.getData());

                            notifyChange();

                            if(res.getData().size()==0){
                                isDummyVisible=true;
                                setPendingdummytext( "No users found." );
                            }else {
                                isDummyVisible=false;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);

                        Log.e("eror in f f","--"+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);
                    }
                });
    }

    public void fetchAllFriends() {
        setTravellingnow("View all friends");
        fetchFriends("all");
    }

    public void fetchGlobalFriends() {
        setTravellingnow("Global");
        fetchFriends("global");
    }

    public void fetchHomeFriends() {
        setTravellingnow("Home");
        fetchFriends("home");
    }

    public void fetchPlanningFriends() {
        setTravellingnow("Planning a trip");
        fetchFriends("planing");
    }


    public void goToSearchFragment( View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        SearchFragment myFragment = new SearchFragment();
        activity.getSupportFragmentManager().beginTransaction().replace( R.id.frame_container, myFragment).addToBackStack(null).commit();    }







}
