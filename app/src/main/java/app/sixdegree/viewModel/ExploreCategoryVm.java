package app.sixdegree.viewModel;


import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.exlporeres.Data;
import app.sixdegree.network.responses.exlporeres.ExlporeListRes;
import app.sixdegree.view.activity.home_module.adapter.InterestCategoryAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class ExploreCategoryVm extends BaseVm {
    public PublishSubject<Boolean> isSubmitted = PublishSubject.create();
    @Bindable
    Boolean isLoaded = false;
    @Bindable
    boolean btnVisible = false;
    private List<Data> data = new ArrayList<>();
    private InterestCategoryAdapter adapter;

    public ExploreCategoryVm() {
        adapter = new InterestCategoryAdapter();
        adapter.updateData(data);
    }

    public Boolean getLoaded() {
        return isLoaded;
    }

    public void setLoaded(Boolean isLoaded) {
        this.isLoaded = isLoaded;
        notifyChange();
    }

    public boolean isBtnVisible() {
        return btnVisible;
    }

    public void setBtnVisible(boolean btnVisible) {
        this.btnVisible = btnVisible;
        notifyPropertyChanged(BR.btnVisible);
    }

    public BehaviorSubject<List<String>> getSelected() {
        return getAdapter().selectedObs;
    }

    public void getExloreList() {
        setLoaded(true);
        apiService.getUserExplore(getSessionInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExlporeListRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ExlporeListRes data) {

                        if (data.getStatus()) {
                            adapter.updateData(data.getData());
                        } else {
                            setErrorMsg(data.getMessage());
                        }

                        setLoaded(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoaded(false);


                    }

                    @Override
                    public void onComplete() {
                        setLoaded(false);

                    }
                });
    }


    @Bindable
    public InterestCategoryAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(InterestCategoryAdapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }


    public void onSubmitClicled() {
        if (getAdapter().selectedList.size() > 0) {
            setLoaded(true);
            apiService.saveUserInterest(getSessionInstance().getToken(), TextUtils.join(",", getAdapter().selectedList))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseRes>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseRes data) {

                            if (data.getStatus()) {
                                app.sixdegree.model.login.Data data1 = getSessionInstance().getData();
                                data1.setInterestAdded(true);
                                getSessionInstance().saveUserData(data1);
                            }

                            isSubmitted.onNext(true);

                            Log.e("res", String.valueOf(data.getStatus()));
                            setLoaded(false);


                        }

                        @Override
                        public void onError(Throwable e) {
                            setLoaded(false);
                        }

                        @Override
                        public void onComplete() {
                            setLoaded(false);

                        }
                    });
        }

    }


}

