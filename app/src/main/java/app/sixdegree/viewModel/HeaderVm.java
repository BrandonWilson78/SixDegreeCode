package app.sixdegree.viewModel;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.utils.AppUtils;

public class HeaderVm extends BaseVm {

    @Bindable
    String count;
    @Bindable
    String countryFlag;


    CountriesDao dao;


    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
        notifyPropertyChanged(BR.countryFlag);
    }

    public HeaderVm(CountriesDao dao, String flag, String count) {
        this.dao = dao;
        setCount(count);
        Log.e("flagheader", flag + "==");

    }

    @BindingAdapter({"bind:countryFlag"})
    public static void loadFlag(ImageView view, String flag) {
        if (flag != null) {
            AppUtils.setFlag(view, flag);
        }
    }


    public String getCount() {
        return count + "+";
    }

    public void setCount(String count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

}
