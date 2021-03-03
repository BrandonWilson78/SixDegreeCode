package app.sixdegree.viewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import java.util.HashMap;

import app.sixdegree.BR;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.getfollowerrequests.Data;
 import app.sixdegree.network.responses.getmutualfriends.X1;
import app.sixdegree.network.responses.viewprofilefriends.MutualFriend;
import app.sixdegree.utils.AppUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class MutualfriendRowVm extends BaseVm {




    public boolean isLoading() {
        return loading;
    }

    public void setLoading( boolean loading ) {
        this.loading = loading;
        notifyPropertyChanged( BR.loading );
    }

    @Bindable
            boolean loading;
MutualFriend data;



 public MutualfriendRowVm( MutualFriend data) {
        this.data=data;

    }


    public String getImage() {
        return data.getProfileImage();
    }

    public void setImage( String image ) {
        this.image = image;
        notifyPropertyChanged( BR.image );
    }

    public String getName() {
        return data.getName();
    }

    public void setName( String name ) {
        this.name = name;
        notifyPropertyChanged( BR.name );
    }

    public String getLocation() {
        return data.getHome();
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






}
