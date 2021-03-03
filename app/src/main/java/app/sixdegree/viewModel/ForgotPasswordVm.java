package app.sixdegree.viewModel;

import android.util.Patterns;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import app.sixdegree.network.NetworkError;
import app.sixdegree.network.responses.BaseRes;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class ForgotPasswordVm extends BaseVm {

    public BehaviorSubject<Boolean> isVerified = BehaviorSubject.create();


    @Bindable
    private String email;

    @Bindable
    private BaseRes baseRes;

    @Bindable
    private String token;

    @Bindable
    private String statusMsg;

    @Bindable
    private boolean isLoading = false;


    public BaseRes getBaseRes() {
        return baseRes;
    }

    public void setBaseRes(BaseRes baseRes) {
        this.baseRes = baseRes;
        notifyPropertyChanged(BR.baseRes);
    }

public      void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }

    public boolean isLoading() {
        return isLoading;
    }


    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
        notifyPropertyChanged(BR.statusMsg);
    }


    public ForgotPasswordVm(){
        this.email="";
    }

    public void onVerifyClick() {

        if (getEmail().isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
            BaseRes baseRes = new BaseRes("Please enter valid email.", false);
            setBaseRes(baseRes);
            setStatusMsg("Please enter valid verification code");
        } else {
            setLoading(true);
            apiService.forgotPassword(getEmail())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseRes>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseRes data) {
                            setStatusMsg(data.getMessage());
                            setBaseRes(data);
                            if (data.getStatus()) {
                                isVerified.onNext(true);

                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            setLoading(false);
                            NetworkError networkError = new NetworkError(e);
                            BaseRes baseRes = new BaseRes(networkError.getAppErrorMessage(), false);
                            setStatusMsg(networkError.getAppErrorMessage());
                            setBaseRes(baseRes);
                        }

                        @Override
                        public void onComplete() {
                            setLoading(false);

                        }
                    });


        }
    }


}
