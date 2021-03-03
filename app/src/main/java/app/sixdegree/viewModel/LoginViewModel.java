package app.sixdegree.viewModel;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import app.sixdegree.model.User;
import app.sixdegree.model.login.LoginResponseData;
import app.sixdegree.network.NetworkError;
import app.sixdegree.view.activity.BaseActivity;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LoginViewModel extends BaseVm {
    public PublishSubject<LoginResponseData> subject = PublishSubject.create();
    @Bindable
    User user;

    @Bindable
    private boolean isLoading = false;

    private String errorMessage = "Email/Username/Password not valid";
    @Bindable
    private String toastMessage = null;
    @Bindable
    private LoginResponseData loginData = null;

    public String getFirebasetoken() {
        return firebasetoken;
    }

    public void setFirebasetoken( String firebasetoken ) {
        this.firebasetoken = firebasetoken;
        notifyPropertyChanged( BR.firebasetoken );
    }

    @Bindable
    String firebasetoken="";

    public LoginViewModel(String firebasetoken) {
        user = new User("", "", "");
        this.firebasetoken=firebasetoken;
        setFirebasetoken( firebasetoken );
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

public    void setLoading(boolean loading) {
        this.isLoading = loading;
        notifyPropertyChanged(BR.isLoading);

    }

    public LoginResponseData getLoginData() {
        return loginData;
    }

     public void setLoginData(LoginResponseData loginData) {
        this.loginData = loginData;
        notifyPropertyChanged(BR.loginData);
    }

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {

        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    @Bindable
    public String getUserEmail() {
        return user.getEmail();
    }

    public void setUserEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserPassword() {
        return user.getPassword();
    }

    public void setUserPassword(String password) {
        user.setPassword(password);
        notifyPropertyChanged(BR.userPassword);
    }

    public void onLoginClicked(View view) {
        if (isInputDataValid())
            validateLogin(view);
        else
            setToastMessage(errorMessage);
    }

    public boolean isInputDataValid() {
        return !TextUtils.isEmpty(getUserEmail()) && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches() && getUserPassword().length() > 4;
    }


    private void validateLogin( View view) {
        setLoading(true);
        Log.e("api hit", "hit");


      //  Toast.makeText(view.getContext()   , "f t"+getFirebasetoken(), Toast.LENGTH_SHORT).show();

        apiService.loginViaEmail(getUserEmail(), getUserPassword(),getFirebasetoken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponseData data) {
                        Log.e("api hit", "on next");
                        if (!data.getStatus()) {
                            setToastMessage(data.getMessage());
                        } else {
                            setLoginData(data);
                            subject.onNext(data);
                            setToastMessage(data.getMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Toast.makeText( view.getContext(), e.toString(), Toast.LENGTH_SHORT ).show();

                        Log.e("api Throwable", "on complete"+e);
                        setLoading(false);
                        NetworkError networkError = new NetworkError(e);
                        setToastMessage(networkError.getAppErrorMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("api hit", "on complete");
                        setLoading(false);

                    }
                });
    }


}

