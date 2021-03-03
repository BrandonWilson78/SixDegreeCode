package app.sixdegree.viewModel;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.util.HashMap;

import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.settings_mod.profile.Data;
import app.sixdegree.network.responses.settings_mod.profile.UserProfile;
import app.sixdegree.utils.AppUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class EditAccountVm extends BaseVm {
    public BehaviorSubject<Boolean> isVerified = BehaviorSubject.create();
    @Bindable
    boolean isLoading;

    @Bindable
    private
    Data userData;

    private String token;


    @Bindable
    private String email;

    @Bindable
    private String errorMsg;


    @Bindable
    String username;

    @Bindable
    String password;

    @Bindable
    String confirmPassword;


    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public String getUsername() {
        return username;
    }


    public EditAccountVm(String token) {
        this.token = token;
        getUserProfile();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    public void setUserData(Data userData) {
        this.userData = userData;
        notifyPropertyChanged(BR.userData);
    }


    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        notifyPropertyChanged(BR.errorMsg);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Data getUserData() {
        return userData;
    }


    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);

    }


    public boolean isLoading() {
        return isLoading;
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

    public String getToken() {
        return token;
    }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(RelativeLayout view, String coverPic) {


        if (coverPic != null) {
            if (coverPic.contains("http://")) {
                AppUtils.picassoLoadBgHttp(view,coverPic);
            } else {
                Uri uri = Uri.fromFile(new File(coverPic));

                    AppUtils.picassoLoadBgUri(view,uri);

            }

        }


    }

    private void getUserProfile() {
        setLoading(true);
        apiService.getProfileDetails(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserProfile>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserProfile data) {

                        if (data.getStatus()) {

                            setEmail(data.getData().getEmail());
                            setUsername(data.getData().getName());
                            setPassword("");
                            setConfirmPassword("");
                            notifyChange();
                        } else {
                            setErrorMsg(data.getMessage());
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


    public void validateData(View view) {


        if (isInputDataValid()) {


            if (!getPassword().isEmpty()) {
                if (getPassword().length() < 5) {
                    setErrorMsg("Password should be 5 character long");
                    Toast.makeText(view.getContext(), "Password should be 6 character long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!getPassword().equals(getConfirmPassword())) {
                    setErrorMsg("Password does not matched.");
                    Toast.makeText(view.getContext(), "Password does not matched", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            updateDetails();

        }


    }

    public boolean isInputDataValid() {
        return !TextUtils.isEmpty(getEmail()) && Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches() && getUsername().length() > 4;
    }


    private void updateDetails() {
        setLoading(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", getUsername());
        map.put("email", getEmail());
        map.put("password", getPassword());
        map.put("confirm_password", getConfirmPassword());

        apiService.updateUserProfileWithoutImages(token, map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {
                        setErrorMsg(res.getMessage());
                        setBaseres(res);
                        if (res.getStatus()) {

                             isVerified.onNext(true);

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
