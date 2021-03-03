package app.sixdegree.viewModel;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import app.sixdegree.R;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.Country;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.gettripdetailsnewres.GetTripDetailsResNew;
import app.sixdegree.network.responses.gettripdetailsnewres.Trail;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.trailsmodule.PlanNextTripActivity;
import app.sixdegree.view.activity.trailsmodule.SIngleTrailView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TrailRowVm extends BaseVm {

    Trail data;
    @Bindable
    String coverPic;
    CountriesDao dao;
    @Bindable
    String name;
    @Bindable
    String flag;
    @Bindable
    String location;

    public String getPlacename() {
        return data.getLocation()==null ? "N/A" : data.getLocation();
    }

    public void setPlacename( String placename ) {
        this.placename = placename;
        notifyPropertyChanged( BR. placename);
    }

    @Bindable
    String placename="";
    @Bindable
    String commentCount;
    @Bindable
    String likesCount;
    @Bindable
    String likedByMe;
    TripDetailsVm tripDetailsVm;

    public void setTrail_id(String trail_id) {
        this.trailid = trail_id;
        Log.e("t id in trailrm--","--"+trail_id);
        notifyPropertyChanged(BR.trailid);
    }

    @Bindable
    String trailid;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id( String user_id ) {
        this.user_id = user_id;
        notifyPropertyChanged( BR. user_id);
    }

    public AppSession getAppSession() {
        return appSession;
    }

    public void setAppSession( AppSession appSession ) {
        this.appSession = appSession;
        notifyPropertyChanged( BR. appSession);
    }

    @Bindable
    String user_id="";

    @Bindable
    AppSession appSession;
    @Bindable
    boolean isLoading;
    private String token;

    public TrailRowVm( app.sixdegree.network.responses.gettripdetailsnewres.Trail item, CountriesDao dao, TripDetailsVm tripDetailsVm, String token, String user_id, AppSession appSession) {
        this.data = item;
        this.token = token;
        this.dao = dao;
        this.user_id = user_id;
        this.appSession = appSession;
        this.tripDetailsVm=tripDetailsVm;

        Log.e("t id in trailrm--", "--" + data);
        if (data.getCountry() != null) {
            getTasks(data.getCountry());
        }


    }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(LinearLayout view, String coverPic) {
        if (coverPic != null) {
            AppUtils.setImageBg(view, coverPic);
        }
    }

    @BindingAdapter({"bind:likedByMe"})
    public static void handleDrawable(TextView view, String likedByMe) {


        if (likedByMe != null) {
            if (likedByMe.endsWith("0")) {

                view.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(view.getContext(), R.drawable.heart_border), null, null, null);
            } else {
                view.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(view.getContext(), R.drawable.heart_fill_), null, null, null);

            }
        }
    }

    @BindingAdapter({"bind:flag"})
    public static void loadFlag(ImageView view, String flag) {
        if (flag != null) {
            AppUtils.setFlag(view, flag);
        }
    }

    public String getToken() {
        return token;
    }

    public String getTrail_id() {
        return String.valueOf( data.getId() );
    }


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);

    }


    public void savePitstop(String id,ImageView starborder,ImageView starfill ,View view) {
        setLoading(true);
        apiService.savePitstop(appSession.getToken(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseRes data) {

                        if (data.getStatus()) {
                            starborder.setVisibility(View.GONE);
                            starfill.setVisibility(View.VISIBLE);
                            Toast.makeText(view.getContext(), data.getMessage() , Toast.LENGTH_SHORT).show();



                        } else {

                            Toast.makeText(view.getContext(), data.getMessage() , Toast.LENGTH_SHORT).show();
                            Log.e("trail size", "--" + "==");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        Log.e("trail size", e.toString() + "==");

                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);

                    }
                });

    }

    public void removebookmark(String id,ImageView starborder,ImageView starfill ,View view) {
        setLoading(true);
        apiService.removePitstop(appSession.getToken(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseRes data) {

                        if (data.getStatus()) {
                            starborder.setVisibility(View.VISIBLE );
                            starfill.setVisibility(View.GONE);
                            Toast.makeText(view.getContext(), data.getMessage() , Toast.LENGTH_SHORT).show();



                        } else {

                            Toast.makeText(view.getContext(), data.getMessage() , Toast.LENGTH_SHORT).show();
                            Log.e("trail size", "--" + "==");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        Log.e("trail size", e.toString() + "==");

                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);

                    }
                });

    }


    public boolean onLongClickOnHeading(View v) {


        if(user_id.equals( "" )){

        }else {
            if(user_id.equals( String.valueOf(getAppSession().getData().getId() ))){
                //logic goes here
                Intent intent = new Intent(v.getContext(), PlanNextTripActivity.class);
                intent.putExtra("isEdit", 0);
                intent.putExtra("trail_id", getTrail_id());
                Log.e("trip  m.trail_id--", "--" + getTrail_id());
                v.getContext().startActivity(intent);
            }else {
                goToSingleTrailAcitvity( v );
            }
        }



       // Toast.makeText(v.getContext(), "lonnnnggg" + "---" + getTrail_id(), Toast.LENGTH_SHORT).show();
        return false;
    }

    public void onTrailItemClicked(View view) {
//
//        if(user_id.equals( "" )){
//
//        }else {
//            if(user_id.equals( String.valueOf(getAppSession().getData().getId() ))){
//                //logic goes here
//                tripDetailsVm.movemarkerToPos(((Integer) view.getTag()) - 1);
//            }else {
//             }
//        }



         tripDetailsVm.movemarkerToPos(((Integer) view.getTag()) - 1);



        /*if (tripDetailsVm.isDisplayMap()){

              tripDetailsVm.movemarkerToPos(((Integer) view.getTag())-1);
        }else{
            tripDetailsVm.setDisplayMap(true);

        }*/
    }

    public String getCommentCount() {
        return String.valueOf( data.getTotalComments() );
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
        notifyPropertyChanged(BR.commentCount);
    }

    public String getLikesCount() {
        return String.valueOf(data.getTotalLikes());
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
        notifyPropertyChanged(BR.likesCount);
    }

    public String getLikedByMe() {
        return String.valueOf(data.getLikedByYou());
    }

    public void setLikedByMe(String likedByMe) {
        this.likedByMe = likedByMe;
        notifyPropertyChanged(BR.likedByMe);
    }

    public String getName() {
        return data.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
        notifyPropertyChanged(BR.flag);
    }

    public String getLocation() {
        return data.getCountry();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoverPic() {
        return data.getTrailPictures();
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    private void getTasks(String country) {
        class GetTasks extends AsyncTask<Void, Void, Country> {

            @Override
            protected Country doInBackground(Void... voids) {
                return dao.findByName(country.toLowerCase());
            }

            @Override
            protected void onPostExecute(Country tasks) {
                super.onPostExecute(tasks);
                if (tasks != null) {
                    Log.e("sdasdasdasd", "simar" + tasks.getFlag());
                    setFlag(tasks.getFlag());
                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public  void goToSingleTrailAcitvity(View v){
        Intent intent=new Intent(v.getContext(), SIngleTrailView.class);
        intent.putExtra("trail_id", getTrail_id());
        intent.putExtra("user_id", getUser_id());
        intent.putExtra("session_id", String.valueOf(getAppSession().getData().getId()  ));
        Log.e("trip  m.trail_id--", "--" + getTrail_id());
        v.getContext().startActivity(intent);
    }




}
