package app.sixdegree.viewModel;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.util.StringUtil;

import org.apache.commons.lang3.StringUtils;

import app.sixdegree.network.responses.exlporeres.Data;
import app.sixdegree.utils.AppUtils;

public class InterestCategoryVm extends BaseObservable {


    @Bindable
    Data data;

    @Bindable
    String name;


    @Bindable
    String coverPic;

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
        notifyPropertyChanged(BR.coverPic);
    }

    public String getCoverPic() {
          return !TextUtils.isEmpty(data.getPicture()) ? data.getPicture() : "N/A";
    }

    public InterestCategoryVm(Data dataModel) {
        this.data = dataModel;
     }

    public String getName() {
        return !TextUtils.isEmpty(data.getName()) ? StringUtils.capitalize(data.getName()) : "N/A";
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getData() {
        return data;
    }


    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(ImageView view, String coverPic) {
        if (coverPic != null) {
            AppUtils.showcoverpic(view,coverPic);
        }


    }
}
