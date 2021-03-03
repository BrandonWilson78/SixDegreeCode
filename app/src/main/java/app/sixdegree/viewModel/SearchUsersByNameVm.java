package app.sixdegree.viewModel;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.Bindable;

import java.util.HashMap;
import java.util.List;

import app.sixdegree.BR;
import app.sixdegree.network.responses.sixdegreesearch.SixDegreeSearchRes;
import app.sixdegree.network.responses.sixdegreesearch.TripsofMatchedUser;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.home_module.adapter.SearchAdapter;
import app.sixdegree.view.activity.home_module.adapter.SearchByNameAdapter;
import app.sixdegree.view.activity.home_module.adapter.TripOfSearchedUsersAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class SearchUsersByNameVm extends BaseVm{
    AppSession appSession;


    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
        notifyPropertyChanged( BR.name );
    }

    @Bindable
String name="";




    public SearchByNameAdapter getSearchAdapter() {
        return searchAdapter;
    }

    public void setSearchAdapter( SearchByNameAdapter searchAdapter ) {
        this.searchAdapter = searchAdapter;
    }

    SearchByNameAdapter searchAdapter;





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



    public SearchUsersByNameVm( AppSession appSession){
        this.appSession=appSession;
        searchAdapter=new SearchByNameAdapter(appSession.getToken());

    }






    public  void search(View view) {
        if(getName().isEmpty()){

            Log.e("getName--","--"+getName());

              //  setErrorMsg( "Please select location" );
            Toast.makeText( view.getContext(), "Please enter name", Toast.LENGTH_SHORT ).show();

        }
        else

        {

            searchByName();        }
    }

    public void searchByName() {


        HashMap<String,String> map=new HashMap<>();
        map.put("name",getName());
         setLoading(true);
        apiService.searchUsersByName(appSession.getToken(),map).subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<SixDegreeSearchRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(SixDegreeSearchRes res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {

                          //  getSearchAdapter().data.clear();

getSearchAdapter().setData( res.getData().getSixdegreeMatched() );



setLoading( false );




                            if(res.getData().getSixdegreeMatched().size()==0){
                                isVisible=true;
                                setDummytext( "No users found." );
                            }else {
                                isVisible=false;
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
