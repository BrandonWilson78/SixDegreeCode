package app.sixdegree.viewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import java.util.HashMap;

import app.sixdegree.BR;
import app.sixdegree.R;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.getfollowerrequests.Data;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class PendingRequestRowVm extends BaseVm {
    public BehaviorSubject<Boolean> isVerified = BehaviorSubject.create();

    public String getFromuserid() {
        return String.valueOf( data.getFromUserId());
    }

    public void setFromuserid( String fromuserid ) {
        this.fromuserid = fromuserid;
        notifyPropertyChanged( BR.fromuserid );
    }

    @Bindable
    String fromuserid="";
    public boolean isLoading() {
        return loading;
    }

    public void setLoading( boolean loading ) {
        this.loading = loading;
        notifyPropertyChanged( BR.loading );
    }

    @Bindable
            boolean loading;
    app.sixdegree.network.responses.getpendingrequests.Data data;

    public String getToken() {
        return token;
    }

    public void setToken( String token ) {
        this.token = token;
        notifyPropertyChanged( BR.token );
    }

    @Bindable
String token="";
    public PendingRequestRowVm( app.sixdegree.network.responses.getpendingrequests.Data data, String token) {
        this.data=data;
        this.token=token;
    }


    public String getImage() {
        return data.getFollowerPic();
    }

    public void setImage( String image ) {
        this.image = image;
        notifyPropertyChanged( BR.image );
    }

    public String getName() {
        return data.getFollowerName();
    }

    public void setName( String name ) {
        this.name = name;
        notifyPropertyChanged( BR.name );
    }

    public String getLocation() {
        return data.getHomeLocation();
    }

    public void setLocation( String location ) {
        this.location = location;
        notifyPropertyChanged( BR.location );
    }

    @Bindable
    String image;

            @Bindable
    String name;
    @Bindable
    String
     location;



    @BindingAdapter({"bind:setuserimg"})
    public static void loadfimage( ImageView view, String coverPic) {
        if (coverPic != null) {
            AppUtils.roundImageWithGlide(view, coverPic);
        }
    }





    public void goToProfileFragment( View view,String id,String followstaus,String friendstatus){

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        ProfileFragment myFragment = new ProfileFragment(id,followstaus,friendstatus);

        activity.getSupportFragmentManager().beginTransaction().replace( R.id.frame_container, myFragment).addToBackStack( "search" ).commit();    }
}
