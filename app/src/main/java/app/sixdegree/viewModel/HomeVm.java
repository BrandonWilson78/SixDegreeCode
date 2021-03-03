package app.sixdegree.viewModel;

import android.net.Uri;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;

import app.sixdegree.network.responses.content_res.ContentRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;

public class HomeVm extends BaseVm {

    @Bindable
    ContentRes contentRes;

    @Bindable
    private boolean isLoading = false;

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
        notifyPropertyChanged(BR.profilePic);

    }

    @Bindable
    String profilePic;

    public String getProfilePic() {
        return profilePic;
    }

public    void setLoading(boolean loading) {
        this.isLoading = loading;
        notifyPropertyChanged(BR.loading);


    }

    public HomeVm(AppSession appSession) {
        setSessionInstance(appSession);
        setProfilePic(appSession.getData().getProfileImage());
    }

    public ContentRes getContentRes() {
        return contentRes;
    }

    public void setContentRes(ContentRes contentRes) {
        this.contentRes = contentRes;
        notifyPropertyChanged(app.sixdegree.BR.contentRes);
    }

    @BindingAdapter({"bind:profilePic"})
    public static void loadProfile(ShapeableImageView view, String profilePic) {


        if (profilePic != null) {
            if (profilePic.contains("http://")) {


                AppUtils.loadPicasso(profilePic,view);
            } else {

                Uri uri = Uri.fromFile(new File(profilePic));
                AppUtils.loadPicasso(uri,view);

            }
        }


    }


/*
    public void getPageContent() {
        setLoading(true);
        apiService.getPageContent("", getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ContentRes data) {
                        setContentRes(data);
                        setToolbarTitle(data.getData().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        NetworkError networkError = new NetworkError(e);
                        ContentRes baseRes = new ContentRes(new Data("", "", 0, "", ""), networkError.getAppErrorMessage(), false);
                        setContentRes(baseRes);
                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);

                    }
                });
    }*/


}
