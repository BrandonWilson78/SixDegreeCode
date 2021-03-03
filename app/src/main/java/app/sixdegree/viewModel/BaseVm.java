package app.sixdegree.viewModel;

import android.content.Intent;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import app.sixdegree.model.login.LoginResponseData;
import app.sixdegree.network.ApiFactory;
import app.sixdegree.network.ApiService;
import app.sixdegree.network.NetworkError;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.utils.AppSession;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static app.sixdegree.view.activity.BaseActivity.RC_SIGN_IN;


public class BaseVm extends BaseObservable  {
    ApiService apiService = ApiFactory.getRetrofitInstance().create(ApiService .class);
    CallbackManager callbackManager=CallbackManager.Factory.create();;
    public PublishSubject<LoginResponseData> subject = PublishSubject.create();
    @Bindable
    String toolbarTitle="Title";


    @Bindable
   public AppSession sessionInstance;



    @Bindable
    BaseRes baseres;


    @Bindable
    String errorMsg;


    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        notifyPropertyChanged(BR.errorMsg);
    }

    public String getErrorMsg() {
        return errorMsg;
    }



    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
        notifyPropertyChanged(BR.toolbarTitle);
    }

    public AppSession getSessionInstance() {
        return sessionInstance;
    }

    public void setSessionInstance(AppSession sessionInstance) {
        this.sessionInstance = sessionInstance;
        notifyPropertyChanged(BR.sessionInstance);
    }


    public BaseRes getBaseres() {
        return baseres;
    }

    public void setBaseres(BaseRes baseres) {
        this.baseres = baseres;
        notifyPropertyChanged(BR.baseres);
    }


    @Bindable
    private boolean isLoading = false;

    private String errorMessage = "Email/Username/Password not valid";
    @Bindable
    private String toastMessage = null;
    @Bindable
    private LoginResponseData loginData = null;

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    void setLoading(boolean loading) {
        this.isLoading = loading;
        notifyPropertyChanged(BR.isLoading);

    }

    public LoginResponseData getLoginData() {
        return loginData;
    }

    private void setLoginData(LoginResponseData loginData) {
        this.loginData = loginData;
        notifyPropertyChanged(BR.loginData);
    }

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {

        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }




public String changeDateFormat(String strCurrentDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date newDate = null;
    try {
        newDate = format.parse(strCurrentDate);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    format = new SimpleDateFormat("dd MMMM");
    String date = format.format(newDate);
    return date;
}
    public void socialLogin(HashMap <String,String> hashMap) {
        //setLoading(true);
        Log.e("api hit", "hit");



        apiService.socialLogin(hashMap)
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponseData>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponseData data) {
                        Log.e("api hit", "on next"+data);
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
