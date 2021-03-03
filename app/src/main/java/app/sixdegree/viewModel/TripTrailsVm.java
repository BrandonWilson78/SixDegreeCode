package app.sixdegree.viewModel;

import android.util.Log;
import android.widget.LinearLayout;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.network.responses.gettripcategories.GetTripCategories;
import app.sixdegree.network.responses.settings_mod.profile.UserProfile;
import app.sixdegree.network.responses.tripsinterest.Data;
import app.sixdegree.network.responses.tripsinterest.InterestTripRes;
import app.sixdegree.network.responses.viewprofilefriends.MutualFriend;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.home_module.adapter.MutualFriendsAdapter;
import app.sixdegree.view.activity.home_module.adapter.ProfileExploreAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class TripTrailsVm extends BaseVm {
    public BehaviorSubject<List<app.sixdegree.network.responses.gettripcategories.Data>> tripCat = BehaviorSubject.create();
    public List<Data> data;

    ArrayList<app.sixdegree.network.responses.gettripcategories.Data> tripcatlist=new ArrayList<>(  );
    @Bindable


    UserProfile userProfile;
    @Bindable
    Boolean isLoading;
    @Bindable
    String isTraveling;
    @Bindable
    String coverPic;

    @Bindable
    String locationName = "Click to select location";

    public String getTripname() {
        return tripname;
    }

    public void setTripname( String tripname ) {
        this.tripname = tripname;
        notifyPropertyChanged( BR.tripname );
    }

    @Bindable
    String tripname="";

    public String getTripcat() {
        return tripcat;
    }

    public void setTripcat( String tripcat ) {
        this.tripcat = tripcat;
        notifyPropertyChanged( BR.tripcat );
    }

    @Bindable
    String tripcat = "Select trip category";

    @Bindable
    String startDate = "Select Date";
    @Bindable
    String endDate = "Select Date";

    @Bindable
    String orderBy = "Select";
    @Bindable
    String user_id = "";

    public String getSearch_trips_of() {
        return search_trips_of;
    }

    public void setSearch_trips_of( String search_trips_of ) {
        this.search_trips_of = search_trips_of;
        notifyPropertyChanged( BR.search_trips_of);
    }

    @Bindable
    String search_trips_of = "3";

    public String getRecommendedBy() {
        return recommendedBy;
    }

    public void setRecommendedBy( String recommendedBy ) {
        this.recommendedBy = recommendedBy;
        notifyPropertyChanged( BR.recommendedBy );
    }

    @Bindable
    String recommendedBy = "Select";

    @Bindable
    String popular = "Select";


    String lattitude = "";
    String longitude = "";
    @Bindable
    String distance;



    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount( String pageCount ) {
        this.pageCount = pageCount;
        notifyPropertyChanged( BR.pageCount );
    }

    @Bindable
    String pageCount="1";
    @Bindable
    private
    String totalDays;
    private ProfileExploreAdapter adapter;
    List<MutualFriend> mutualFriendList;
    @Bindable

    public MutualFriendsAdapter getMutualFriendsAdapter() {
        return mutualFriendsAdapter;
    }

    public void setMutualFriendsAdapter( MutualFriendsAdapter mutualFriendsAdapter ) {
        this.mutualFriendsAdapter = mutualFriendsAdapter;
        this.notifyPropertyChanged( BR.mutualFriendsAdapter );
     }
    @Bindable
    public Boolean isVisible;

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
        notifyPropertyChanged( BR.dummytext );
    }

    @Bindable
    String dummytext="No Mutual Friends Found.";
    MutualFriendsAdapter mutualFriendsAdapter;

    public AppSession getAppSession() {
        return appSession;
    }

    public void setAppSession( AppSession appSession ) {
        this.appSession = appSession;
        notifyPropertyChanged( BR.appSession );
    }

    @Bindable
    AppSession appSession;

    public TripTrailsVm( AppSession appSession,UserProfile userProfile,String user_id, List<MutualFriend> mutualFriendList ) {
        this.userProfile = userProfile;
        this.appSession=appSession;
        this.user_id=user_id;
        this.mutualFriendList=mutualFriendList;


        Log.e( "TripTrailsVm" ,"--");
        if(userProfile!=null){
            setTraveling(userProfile.getData().getTravelling());
            setTotalDays(userProfile.getData().getTotalDays(), userProfile.getData().getCurrentDays());
            setCoverPic(userProfile.getData().getLatestTrip().getTripPicture());
        }

     //  getMutualFriendsAdapter().setData(  mutualFriendList);
        data = new ArrayList<>();
        adapter = new ProfileExploreAdapter();
        adapter.updateData(data);

        getTripCategories();
      //  getExloreList(getPageCount());
    }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(LinearLayout view, String coverPic) {
        if (coverPic != null) {

            AppUtils.setImageBg(view, coverPic);
        }


    }

    public String getDistance() {

        if(userProfile!=null){
            return userProfile.getData().getTotalDistance() == 0 ? "N/A" : String.format("%.2f", userProfile.getData().getTotalDistance() )+" KM";
        }else {
            return "N/A";
        }

    }

    public void setDistance(String distance) {
        this.distance = distance;
        notifyPropertyChanged(BR.distance);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        notifyPropertyChanged(BR.orderBy);
    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
        notifyPropertyChanged(BR.popular);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        notifyPropertyChanged(BR.endDate);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        notifyPropertyChanged(BR.startDate);
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
        notifyPropertyChanged(BR.locationName);
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id( String user_id ) {
        this.user_id = user_id;
        notifyPropertyChanged( BR.user_id );
    }

    public void onResetClick() {
        Log.e("clicekd", "clicked");
        setLocationName("Click to select location");
        setStartDate("Select Date");
        setPopular("Select");
        setOrderBy("Select");
        setEndDate("Select Date");
        setStartDate("Select Date");
        setRecommendedBy( "Select Recommended By" );
        setLattitude("");
        setSearch_trips_of("");
        setLongitude("");
        setErrorMsg("Filters Removed");
        getExloreList(getPageCount());

        setTripname( "" );
        setTripcat( "SELECT TRIP CATEGORY" );
        Log.e( "on onResetClick","" );
    }

    public void onSearchSubmit() {
        setErrorMsg("Searching...");
        getExloreList(getPageCount());

        Log.e( "on search submit","" );
    }

    public String getCoverPic() {

        if(userProfile!=null){
            return userProfile.getData().getLatestTrip().getTripPicture();
        }else {
            return "";
        }

    }

    public boolean isIslocationvisible() {
        return islocationvisible;
    }

    public void setIslocationvisible(boolean islocationvisible) {
        this.islocationvisible = islocationvisible;
        notifyPropertyChanged(BR.islocationvisible);
    }

    @Bindable
boolean islocationvisible;
    public String getLocation() {

        if(userProfile!=null){
            setIslocationvisible(false);
            if(userProfile.getData().getLatestTrip().getLocation() ==null){
                return  "";
            }else {
                setIslocationvisible(true);
                return userProfile.getData().getLatestTrip().getLocation();

            }
        }
        return location;
    }

    public void setLocation( String location ) {
        this.location = location;
    }

    @Bindable
    String location="";

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
        notifyPropertyChanged(BR.coverPic);
    }

    public Boolean getLoading() {
        return isLoading;
    }

    public void setLoading(Boolean loading) {
        isLoading = loading;
        notifyChange();
    }

    public void setTotalDays(int totalDays, int current) {

        this.totalDays =/* current + "/" +*/ totalDays + " Days";
        notifyPropertyChanged(BR.totalDays);
    }

    public String getTotalDays() {
        return totalDays;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public String getTraveling() {
        return isTraveling;
    }

    public void setTraveling(Boolean traveling) {
        if (traveling) {
            this.isTraveling = "now traveling";
        } else {
            this.isTraveling = "Home";
        }
        notifyPropertyChanged(BR.isTraveling);
    }
    public void getTripCategories() {
        //setLoading(true);
        apiService.getTripCategories(appSession.getToken())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTripCategories>() {
                    @Override
                    public void onSubscribe( Disposable d) {
                    }

                    @Override
                    public void onNext(GetTripCategories data) {

                        if (data.getStatus()) {
                            tripCat.onNext(data.getData());

                            Log.e("tripcat--", "--" + tripCat);

                            tripcatlist.addAll( data.getData());
                        } else {
                            setErrorMsg(data.getMessage());
                        }

                        //  setLoading(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        //  setLoading(false);


                    }

                    @Override
                    public void onComplete() {
                        //   setLoading(false);

                    }
                });
    }
    public void getExloreList(String page) {

//        if (!getStartDate().equalsIgnoreCase(getEndDate())) {
//            if (getStartDate().equals("Select Date") || getEndDate().equals("Select Date")) {
//                setErrorMsg("Please enter start and end date.");
//                return;
//            }
//        }
        setLoading(true);
String id ="";
        for (int i = 0; i < tripcatlist.size(); i++) {
            if (getTripcat().equalsIgnoreCase(tripcatlist.get(i).getName())) {
                id=(  String.valueOf(tripcatlist.get(i).getId()));
            }

        }

        Log.e( "loation name--","--"+getLocationName() );
        apiService.getTripsByInterest(getAppSession().getToken(), getLattitude(),
                getLongitude(),/* getStartDate().equals("Select Date") ? "" : getStartDate(), getEndDate().equals("Select Date") ? "" : getEndDate()*/
                "","",
                getOrderBy().equalsIgnoreCase("Select") ? "" : getOrderBy(),
                getPopular().equalsIgnoreCase("Select") ? "" : getPopular(),
                getLocationName().equalsIgnoreCase( "Click to select location" ) ? "" :
                        getLocationName(),getUser_id(),getTripname(),id,""/*, page*/)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InterestTripRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(InterestTripRes data) {


                         if (data.getStatus()) {
                            adapter.updateData(data.getData());
                        } else {
                            adapter.updateData(new ArrayList<>());
                            setErrorMsg("No trips found!");
                        }

                        setLoading(false);
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

    @Bindable
    public List<Data> getData() {
        return this.data;
    }

    public void setData(List<Data> data) {
        this.data = data;
        adapter.notifyDataSetChanged();

    }

    @Bindable
    public ProfileExploreAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(ProfileExploreAdapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }


}
