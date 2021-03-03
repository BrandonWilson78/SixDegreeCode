package app.sixdegree.viewModel;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Bindable;

import java.util.HashMap;
import java.util.List;

import app.sixdegree.BR;
import app.sixdegree.R;
import app.sixdegree.network.responses.showlikeslist.ViewLikeList;
import app.sixdegree.network.responses.sixdegreesearch.SixDegreeSearchRes;
import app.sixdegree.network.responses.sixdegreesearch.TripsofMatchedUser;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.home_module.adapter.SearchAdapter;
import app.sixdegree.view.activity.home_module.adapter.SearchTripsAdapter;
import app.sixdegree.view.activity.home_module.adapter.TripOfSearchedUsersAdapter;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class SearchFragmentVm  extends BaseVm{
    AppSession appSession;
    public BehaviorSubject<List<TripsofMatchedUser>> tripsofmatcheduser = BehaviorSubject.create();
    public String getPlacename() {
        return placename;
    }

    public void setPlacename( String placename ) {
        this.placename = placename;
        notifyPropertyChanged( BR.placename );


    }



@Bindable
String placename="";

    public String getSearchtype() {
        return searchtype;
    }

    public void setSearchtype( String searchtype ) {
        this.searchtype = searchtype;
        notifyPropertyChanged( BR.searchtype );
    }

    @Bindable
String searchtype="";



    public SearchAdapter getSearchAdapter() {
        return searchAdapter;
    }

    public void setSearchAdapter( SearchAdapter searchAdapter ) {
        this.searchAdapter = searchAdapter;
    }

    SearchAdapter searchAdapter;


    public TripOfSearchedUsersAdapter getSearchTripsAdapter() {
        return searchTripsAdapter;
    }

    public void setSearchTripsAdapter( TripOfSearchedUsersAdapter searchTripsAdapter ) {
        this.searchTripsAdapter = searchTripsAdapter;
    }

    TripOfSearchedUsersAdapter  searchTripsAdapter;




    public boolean isLoading() {
        return loading;
    }

    public void setLoading( boolean loading ) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
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

    @Bindable
    Boolean isVisible;

    public Boolean getTripheadingvisible() {
        return tripheadingvisible;
    }

    public void setTripheadingvisible( Boolean tripheadingvisible ) {
        this.tripheadingvisible = tripheadingvisible;
        notifyPropertyChanged( BR.tripheadingvisible );
    }

    @Bindable
    Boolean tripheadingvisible=false;

    public Boolean getIstripvisible() {
        return istripvisible;
    }

    public void setIstripvisible( Boolean istripvisible ) {
        this.istripvisible = istripvisible;
        notifyPropertyChanged( BR.istripvisible );
    }

    @Bindable
    Boolean istripvisible;

    public String getDummytext() {
        return dummytext;
    }

    public void setDummytext( String dummytext ) {
        this.dummytext = dummytext;
        notifyPropertyChanged( BR.dummytext );
    }

    @Bindable
    String dummytext="No Users Found.";

    public String getDummytrips() {
        return dummytrips;
    }

    public void setDummytrips( String dummytrips ) {
        this.dummytrips = dummytrips;
        notifyPropertyChanged( BR.dummytrips );
    }

    @Bindable
    String dummytrips="No Trips Found.";

    public SearchFragmentVm(AppSession appSession){
        this.appSession=appSession;
        searchAdapter=new SearchAdapter(appSession.getToken());
        searchTripsAdapter=new TripOfSearchedUsersAdapter();

    }






    public  void search(View view) {
        if(getPlacename().isEmpty()){

            Log.e("placenae--","--"+getPlacename());

              //  setErrorMsg( "Please select location" );
            Toast.makeText( view.getContext(), "Please select location", Toast.LENGTH_SHORT ).show();

        }
        else

        {

searchSixDegree();
        }
    }

    public void searchSixDegree() {
        HashMap<String,String> map=new HashMap<>();
        map.put("name","");
        map.put("search_type","");
        setLoading(true);
        apiService.searchSixDegree(appSession.getToken(),map).subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<SixDegreeSearchRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(SixDegreeSearchRes res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {

                            tripheadingvisible=true;

getSearchAdapter().setData( res.getData().getSixdegreeMatched() );

//tripsofmatcheduser.onNext( res.getData().getTripsofMatchedUsers() );

                            getSearchTripsAdapter().updateData( res.getData().getTripsofMatchedUsers() );

setLoading( false );




                            if(res.getData().getSixdegreeMatched().size()==0){
                                isVisible=true;
                                setDummytext( "No users found." );
                            }else {
                                isVisible=false;
                            }

                            if(res.getData().getTripsofMatchedUsers().size()==0){
                                istripvisible=true;
                                tripheadingvisible=true;
                              }else {
                                istripvisible=false;
                                tripheadingvisible=true;
                            }
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
