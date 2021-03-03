package app.sixdegree.viewModel;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import app.sixdegree.BR;
import app.sixdegree.network.responses.sixdegreesearch.TripsofMatchedUser;
import app.sixdegree.network.responses.tripsinterest.Data;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.trailsmodule.TrailDetailsActivity;


public class TripsOfMatchedUsersVm extends BaseObservable {

    @Bindable
    TripsofMatchedUser data;
    @Bindable
    String name;
    @Bindable
    String coverPic;
    @Bindable
    String date;

    public String getTrailcount() {
        return data.getTrailsCount()==0 ? "0" :String.valueOf( data.getTrailsCount() );
    }

    public void setTrailcount( String trailcount ) {
        this.trailcount = trailcount;
        notifyPropertyChanged( BR.trailcount );
    }

    @Bindable
    String trailcount="0";

    public TripsOfMatchedUsersVm( TripsofMatchedUser dataModel) {
        this.data = dataModel;
    }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(RelativeLayout view, String coverPic) {
        if (coverPic != null) {
            AppUtils.picassoLoadBgHttp(view,coverPic);
        }


    }

    public void onclick(View view){
        Intent intent= new Intent(view.getContext(), TrailDetailsActivity.class);
        intent.putExtra("id",String.valueOf(data.getId()));
        intent.putExtra("user_id",String.valueOf(data.getUserId()));
        intent.putExtra("tagged","tagged");
        view.getContext().startActivity(intent);
    }

    public String getCoverPic() {
        return !TextUtils.isEmpty(data.getTripPicture()) ? data.getTripPicture() : "N/A";
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getName() {
        return !TextUtils.isEmpty(data.getName()) ? data.getName() : "N/A";
    }

    public void setName(String name) {
        this.name = name;
    }

    public TripsofMatchedUser getData() {
        return data;
    }

    public String getDate() {
        return AppUtils.getMonthYear(data.getStartDate());
    }

    public void setDate(String date) {
        this.date = date;
    }


}
