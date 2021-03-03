package app.sixdegree.viewModel;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import app.sixdegree.network.NetworkError;
import app.sixdegree.network.responses.BaseRes;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class  VerifyEmailVm extends BaseVm {

    public BehaviorSubject<Boolean> isVerified = BehaviorSubject.create();

    private String VERIFY = "verify";
    private String NEXT = "next";

    @Bindable
    private String code="";

    @Bindable
    private BaseRes baseRes;

    @Bindable
    private String token;

    @Bindable
    private String statusMsg;

    @Bindable
    private String userEmail;

    @Bindable
    private boolean isLoading = false;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        notifyPropertyChanged(BR.token);
    }

    public String getVERIFY() {
        return VERIFY;
    }

    public String getNEXT() {
        return NEXT;
    }

    public BaseRes getBaseRes() {
        return baseRes;
    }

    public void setBaseRes(BaseRes baseRes) {
        this.baseRes = baseRes;
        notifyPropertyChanged(BR.baseRes);
    }

      void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setCode(String code) {
        this.code = code;
        notifyPropertyChanged(BR.code);

    }

    public String getCode() {
        return code;
    }


    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
        notifyPropertyChanged(BR.statusMsg);
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        notifyPropertyChanged(BR.userEmail);
    }

    public void onVerifyClick() {

        if (getBaseRes() != null && getBaseRes().getStatus()) {
            isVerified.onNext(true);
        } else {
            if (getCode().equals("")) {
                setStatusMsg("Please enter valid verification code");
            } else {
                setLoading(true);
                Log.e("api hit", "hit");
                apiService.verifyEmail(getUserEmail(), getCode(), getToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BaseRes>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(BaseRes data) {
                                Log.e("api hit", "on next");
                                setStatusMsg(data.getMessage());

                                setBaseRes(data);

                            }

                            @Override
                            public void onError(Throwable e) {
                                setLoading(false);
                                NetworkError networkError = new NetworkError(e);
                                BaseRes baseRes = new BaseRes(networkError.getAppErrorMessage(), false);
                                setBaseRes(baseRes);
                                setStatusMsg(networkError.getAppErrorMessage());
                            }

                            @Override
                            public void onComplete() {
                                setLoading(false);

                            }
                        });
            }


        }
    }


    public void resendcode( View view) {


                setLoading(true);
                Log.e("api hit", "hit");
                apiService.resendCode(getToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BaseRes>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(BaseRes data) {
                                Log.e("api hit", "on next");
                                setStatusMsg(data.getMessage());

                                Toast.makeText(view.getContext() , data.getMessage(), Toast.LENGTH_SHORT ).show();
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
