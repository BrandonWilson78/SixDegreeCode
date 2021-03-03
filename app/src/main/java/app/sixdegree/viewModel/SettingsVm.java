package app.sixdegree.viewModel;

import android.util.Log;
import android.view.View;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import app.sixdegree.model.login.Data;
import app.sixdegree.network.NetworkError;
import app.sixdegree.network.responses.BaseRes;
 import app.sixdegree.utils.AppSession;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SettingsVm extends BaseVm {


    public String getTtoken() {
        return ttoken;
    }

    public void setTtoken( String ttoken ) {
        this.ttoken = ttoken;
        notifyPropertyChanged( BR.ttoken );
    }

    @Bindable
    String ttoken="";

    public String getGetaccountype() {
        return getaccountype;
    }

    public void setGetaccountype( String getaccountype ) {
        this.getaccountype = getaccountype;
        notifyPropertyChanged( BR.getaccountype );
    }

    @Bindable
    String getaccountype="";

    public String getUid() {
        return uid;
    }

    public void setUid( String uid ) {
        this.uid = uid;
        notifyPropertyChanged( BR.uid );
    }

    @Bindable
    String uid="";
    public SettingsVm(AppSession userData,String ttoken,String uid,String accountype) {
        this.userData = userData;
        this.ttoken=ttoken;
        this.getaccountype=accountype;
            this.uid=uid;
        setUserData( userData );

         this.accountType = userData.getData().getAccountType();
        Log.e("account_type", accountType + "==");
        setAccountType(accountType);


         this.mapUnit = userData.getData().getMaptype();


        Log.e("temp--","userData.getData().getTemprature() "+userData.getData().getTemprature() );




        if (userData.getData().getTemprature() != null) {
            if(userData.getData().getTemprature().equals( "f" )){
                this.temp ="Fahrenheit";
            }else if(userData.getData().getTemprature().equals( "c" )){
                this.temp ="Celsius";
            }else {
                this.temp = userData.getData().getTemprature() ;
            }
        }else {
            this.temp="Celsius";
        }

        this.units = userData.getData().getRadiusUnit();



    }

    @Bindable
    public int accountType;

    @Bindable
    public String accountTypeString = "";

    public String getAccountTypeString() {
        return accountTypeString;
    }

    public void setAccountTypeString(String accountTypeString) {
        this.accountTypeString = accountTypeString;
        notifyPropertyChanged(BR.accountTypeString);
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
        notifyPropertyChanged(BR.accountType);
        setAccountTypeString(accountType == 0 ? "Private" : "Public");
        Data data = getUserData().getData();
        data.setAccountType(accountType);
        getUserData().saveUserData(data);
    }

    public int getAccountType() {
        return accountType;
    }

    @Bindable
    public BaseRes baseRes;

    @Bindable
    public AppSession userData;

    @Bindable
    public AppSession getUserData() {
        return userData;
    }

    public void setUserData(AppSession userData) {
        this.userData = userData;
        notifyPropertyChanged(BR.userData);
    }

    @Bindable
    private String statusMsg;

    @Bindable
    public boolean isLoading = false;

    @Bindable
    public boolean isPrivate = false;


    @Bindable
    String temp;

    @Bindable
    String mapUnit;

    @Bindable
    String units;


    public String getTemp() {
        return (temp == null ? "Select" : temp);
    }

    public void setTemp(String tempa) {
        this.temp = tempa;
        Data datas=getUserData().getData();
        datas.setTemprature(tempa);
        getUserData().saveUserData(datas);
        notifyPropertyChanged(BR.temp);
    }

    public String getMapUnit() {
        return (mapUnit == null ? "Select" : mapUnit);
    }

    public void setMapUnit(String mapUnita) {
        this.mapUnit = mapUnita;

        Data data=getUserData().getData();
        data.setMaptype(mapUnita);
        getUserData().saveUserData(data);
        notifyPropertyChanged(BR.mapUnit);
    }

    public void setUnits(String unita) {
        this.units = unita;

        Data data=getUserData().getData();
        data.setRadiusUnit(unita);
        getUserData().saveUserData(data);
        notifyPropertyChanged(BR.units);
    }

    public String getUnits() {
        return (units == null ? "Select" : units);
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
        notifyPropertyChanged(BR.isPrivate);
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public BaseRes getBaseRes() {
        return baseRes;
    }

    private void setBaseRes(BaseRes baseRes) {
        this.baseRes = baseRes;
        notifyPropertyChanged(BR.baseRes);
    }

    void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.isLoading);
    }


    public boolean isLoading() {
        return isLoading;
    }


    private String getStatusMsg() {
        return statusMsg;
    }

    private void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
        notifyPropertyChanged(BR.statusMsg);
    }


    public void changeProfileMode( View view) {
        setLoading(true);
        String type = "";
        if (getAccountType() == 0) {
            type = "1";
        } else {
            type = "0";
        }

        try{

            String finalType = type;
            apiService.setProfileType( getTtoken(),type,getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseRes>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseRes data) {

                            if (data.getStatus()) {
                                setAccountType(Integer.parseInt(finalType));
                            }



                            setStatusMsg(data.getMessage());
                            setBaseRes(data);

                        }

                        @Override
                        public void onError(Throwable e) {
                            setLoading(false);
                            NetworkError networkError = new NetworkError(e);
                            setAccountType(Integer.parseInt(getGetaccountype()));
                            BaseRes baseRes = new BaseRes(networkError.getAppErrorMessage(), false);
                            setStatusMsg(networkError.getAppErrorMessage());
                            setBaseRes(baseRes);
                        }




                        @Override
                        public void onComplete() {
                            setLoading(false);

                        }
                    });
        }catch (Exception e){}


    }


    public void setTempratureApi(String temp) {
        setLoading(true);
        apiService.updateTemp(getTtoken(), temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes data) {
                        if (data.getStatus()) {



                            if (temp.equals("c")) {
                                setTemp("Celsius");

                              } else {
                                setTemp("Fahrenhiet");

                              }

                            setLoading(false);



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

    public void setUnitApi(String temp) {
        setLoading(true);
        apiService.updateUnits(getTtoken(), temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes data) {

                        if (data.getStatus()) {
                            setUnits(temp);
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

    public void setMapApi(String temp) {
        setLoading(true);
        apiService.updateMap(getTtoken(), temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes data) {

                        if (data.getStatus()) {
                            setMapUnit(temp);
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

    public void logoutUser() {
        setLoading(true);

        apiService.logOutUser(getTtoken())
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
