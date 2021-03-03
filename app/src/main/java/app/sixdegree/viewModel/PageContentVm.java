package app.sixdegree.viewModel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import app.sixdegree.network.NetworkError;
import app.sixdegree.network.responses.content_res.ContentRes;
import app.sixdegree.network.responses.content_res.Data;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PageContentVm extends BaseVm {

    @Bindable
    boolean isLoading;

    @Bindable
    ContentRes contentRes;

    @Bindable
    String url;


    public PageContentVm(String url) {
        setUrl(url);
        getPageContent();
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    public String getUrl() {
        return url;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public ContentRes getContentRes() {
        return contentRes;
    }

    public void setContentRes(ContentRes contentRes) {
        this.contentRes = contentRes;
        notifyPropertyChanged(app.sixdegree.BR.contentRes);
    }


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
                        setLoading(false);
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
    }




}
