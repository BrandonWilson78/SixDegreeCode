package app.sixdegree.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.google.maps.model.LatLng;

import java.io.File;
import java.util.HashMap;

import app.sixdegree.BR;
import app.sixdegree.model.login.Data;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.Country;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.fetchcommentres.FetchCommentC;
import app.sixdegree.network.responses.fetchcomments.FetchCommentsRes;
import app.sixdegree.network.responses.getsingleTrailDetails.GetSingleTrailDetails;
import app.sixdegree.network.responses.showlikeslist.ViewLikeList;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.trailsmodule.PlanNextTripActivity;
import app.sixdegree.view.activity.trailsmodule.adapters.TrailCommentsAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class SingleTrailViewVm extends BaseVm {


    public boolean isImageVisible() {
        return isImageVisible;
    }

    public void setImageVisible( boolean imageVisible ) {
        isImageVisible = imageVisible;
        notifyPropertyChanged( BR.isImageVisible );
    }

    @Bindable
    boolean isImageVisible;



    public BehaviorSubject<LatLng> latlngs = BehaviorSubject.create();
    public BehaviorSubject<Boolean> isfinished = BehaviorSubject.create();
    public BehaviorSubject<Boolean> comentAdded = BehaviorSubject.create();
    TrailCommentsAdapter trailCommentsAdapter;
    CountriesDao dao;

    public String getOtherscount() {
        return otherscount;
    }

    public void setOtherscount(String otherscount) {
        this.otherscount = otherscount;
        notifyPropertyChanged(BR.otherscount);
    }

    @Bindable
    String otherscount="";
    @Bindable
    String description = "";
    @Bindable
    boolean likeStatus = false;
    @Bindable
    String latitude = "0.0";
    @Bindable
    String longitude = "0.0";
    @Bindable
    String address = "";
    @Bindable
    String country = "";
    @Bindable
    boolean isLoading;
    String trail_id = "";
    String token = "";
    @Bindable
    String countryname = "";
    @Bindable
    String comment = "";
    @Bindable
    String placename = "";
    @Bindable
    private String profilePic;
    @Bindable
    private String flagImage;



    public String getYouimage() {
        return sessiondata.getProfileImage();
    }

    public void setYouimage(String youimage) {
        this.youimage = youimage;
        notifyPropertyChanged(BR.youimage);
    }

    @Bindable
    String youimage;


    public String getSeconduserimage() {
        return seconduserimage;
    }

    public void setSeconduserimage(String seconduserimage) {
        this.seconduserimage = seconduserimage;
        notifyPropertyChanged(BR.seconduserimage);
    }

    @Bindable
String seconduserimage="";

    public String getFristUserImage() {
        return fristUserImage;
    }

    public void setFristUserImage(String fristUserImage) {
        this.fristUserImage = fristUserImage;
        notifyPropertyChanged(BR.fristUserImage);
    }

    @Bindable
    String fristUserImage="";






    public String getLikString() {
        return getOtherscount().equals("0")? likString +" like this pitstop." :likString +" and "+getOtherscount() +" others like this pitstop." ;
    }

    public void setLikString(String likString) {
        this.likString = likString;
        notifyPropertyChanged(BR.likString);
    }

    @Bindable
    String likString = "";
    Data sessiondata;

    public SingleTrailViewVm( Data sesiondata, CountriesDao dao, String trail_id, String token ) {
        this.trail_id = trail_id;
        this.token = token;
        this.dao = dao;
        this.sessiondata = sesiondata;
        trailCommentsAdapter = new TrailCommentsAdapter();
        Log.e( "tri id--", "singleviewm--"+trail_id );
        getSignleTrailDetails();
        //fetch comments api
        fetchComments();

        //fetch like list
        showlikeList();
    }

    @BindingAdapter({"bind:profilePic"})
    public static void loadImage(RelativeLayout view, String coverPic) {
        if (coverPic != null) {
//            if (coverPic.contains("http://")) {
//                AppUtils.picassoLoadBgHttp(view, coverPic);
//            } else {
//                Uri uri = Uri.fromFile(new File(coverPic));
//                AppUtils.picassoLoadBgUri(view, uri);
//            }

            AppUtils.setImageBg(view, coverPic);


        }


    }
    @BindingAdapter({"bind:setfirstimage"})
    public static void loadfimage(ImageView view, String coverPic) {
        if (coverPic != null) {
            AppUtils.roundImageWithGlide(view, coverPic);
        }
    }  @BindingAdapter({"bind:setyouimage"})
    public static void loadyouimage(ImageView view, String coverPic) {
        if (coverPic != null) {
            AppUtils.roundImageWithGlide(view, coverPic);
        }
    }

   @BindingAdapter({"bind:setsecondimage"})
    public static void loadsimg(ImageView view, String coverPic) {
        if (coverPic != null) {
            AppUtils.roundImageWithGlide(view, coverPic);
        }
    }







    @BindingAdapter({"bind:flag"})
    public static void loadFlag(ImageView view, String flag) {
        if (flag != null) {
            AppUtils.setFlag(view, flag);
        }
    }

    public TrailCommentsAdapter getTrailCommentsAdapter() {
        return trailCommentsAdapter;
    }

    public void setTrailCommentsAdapter(TrailCommentsAdapter trailCommentsAdapter) {
        this.trailCommentsAdapter = trailCommentsAdapter;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.longitude);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR.country);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);

    }

    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean loading) {
        likeStatus = loading;
        notifyPropertyChanged(BR.likeStatus);

    }

    public String getTrail_id() {
        return trail_id;
    }

    public String getToken() {
        return token;
    }

    public String getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(String flagImage) {
        this.flagImage = flagImage;
        notifyPropertyChanged(BR.flagImage);
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
        notifyPropertyChanged(BR.placename);
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
        notifyPropertyChanged(BR.countryname);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
        notifyPropertyChanged(BR.profilePic);
    }

    public void getSignleTrailDetails() {
        setLoading(true);
        apiService.getSingleTrailDetails(token, getTrail_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetSingleTrailDetails>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetSingleTrailDetails data) {
                        if (data.getStatus()) {
                            setDescription(data.getData().getSummary());
                            setLatitude(data.getData().getLatitude());
                            setLongitude(data.getData().getLongitude());


                            if (data.getData().getLike_status() == 0) {
                                setLikeStatus(false);
                            } else {
                                setLikeStatus(true);
                            }

                            if (data.getData().getLocation() != null) {
                                setPlacename(data.getData().getLocation());
                            }

                            setCountryname(data.getData().getCountry());
                            if (country != null) {
                                getTasks(data.getData().getCountry());
                                Log.e("countryname-", "--" + data.getData().getCountry());
                                Log.e("getLocation-", "--" + data.getData().getLocation());
                            }




                            // profile image
                            setProfilePic(data.getData().getTrail_pictures());


                            notifyChange();


                        } else {
                            setErrorMsg(data.getMessage());
                        }

                        setLoading(false);


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

    public void likeTrail() {
        setLoading(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("trail_id", getTrail_id());


        map.put("type", "like");


        apiService.likeUnlikeTrail(getToken(), map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {
                        setErrorMsg(res.getMessage());

                        if (res.getStatus()) {
                            setLikeStatus(true);
                            showlikeList();
                        }
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


    public void unlikeTrail(View v) {
        setLoading(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("trail_id", getTrail_id());
        map.put("type", "unlike");


        apiService.likeUnlikeTrail(getToken(), map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {
                        setErrorMsg(res.getMessage());

                        if (res.getStatus()) {
                            setLikeStatus(false);
                            showlikeList();
                        }
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


    private void getTasks(String country) {
        class GetTasks extends AsyncTask<Void, Void, Country> {

            @Override
            protected Country doInBackground(Void... voids) {

Country abc=null;
                if(country!=null){
                    abc=dao.findByName(country.toLowerCase());
                }
                return abc;
            }

            @Override
            protected void onPostExecute(Country tasks) {
                super.onPostExecute(tasks);
                if (tasks != null) {
                    Log.e("sdasdasdasd", "simar" + tasks.getFlag());
                    setFlagImage(tasks.getFlag());
                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void goToPlanNextTripActivity(View v) {
        Intent intent = new Intent(v.getContext(), PlanNextTripActivity.class);
        intent.putExtra("trail_id", getTrail_id());
        Log.e("trip  m.trail_id--", "--" + getTrail_id());
        v.getContext().startActivity(intent);
    }

      void fetchComments() {
        setLoading(true);
        apiService.fetchComments(getToken(), getTrail_id()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<FetchCommentsRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FetchCommentsRes res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {
                            getTrailCommentsAdapter().setData(res.getData());
                            notifyChange();
                        }
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




 public void showlikeList() {
        HashMap<String,String>map=new HashMap<>();
        map.put("trail_id",getTrail_id());
        setLoading(true);
        apiService.showLikesRes(getToken(),map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<ViewLikeList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ViewLikeList res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {


                            if (!isLikeStatus() && res.getData().getFirst_user_name().equals("") && res.getData().getSecond_user_name().equals("")) {
                                setImageVisible(false);
                            } else {
                                setImageVisible(true);
                            }

                            if (isLikeStatus()) {

                                if (res.getData().getFirst_user_name().equals("") && res.getData().getSecond_user_name().equals("")) {
                                    setLikString("You");
                                    // setLikString( "You, "+res.getData().getSecond_user_name() );
                                } else if (res.getData().getSecond_user_name().equals("")) {
                                    setLikString("You, " + res.getData().getFirst_user_name());
                                } else if (res.getData().getFirst_user_name().equals("")) {
                                    setLikString("You, " + res.getData().getSecond_user_name());

                                } else {
                                    setLikString("You, " + res.getData().getFirst_user_name() + ", and " + res.getData().getSecond_user_name());
                                }


                            } else {

                                if (res.getData().getSecond_user_name().isEmpty() ){
                                    setLikString(res.getData().getFirst_user_name() );
                                }
                              else   if (res.getData().getFirst_user_name().isEmpty() ){
                                    setLikString(res.getData().getSecond_user_name() );
                                }else {
                                    setLikString(res.getData().getFirst_user_name() + ", " + res.getData().getSecond_user_name());
                                }

                               /* if (res.getData().getFirst_user_name().equals("") && res.getData().getOtherCount() == 0) {
                                    setLikString(res.getData().getSecond_user_name());
                                } else if (res.getData().getSecond_user_name().equals("") && res.getData().getOtherCount() == 0) {


                                    setLikString(res.getData().getFirst_user_name());
                                } else {*/

                            }

                       /* } */



                            setOtherscount( String.valueOf( res.getData().getOtherCount() ) );
                            setFristUserImage( res.getData().getFirst_user_image() );
                            setSeconduserimage( res.getData().getSecond_user_image() );
                            notifyChange();
                        }
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


    public void addComment(View view) {

        if (getComment().equals("")) {
         //   setErrorMsg("Please enter Comment");
            Toast.makeText( view.getContext(),"Please enter Comment  " , Toast.LENGTH_SHORT ).show();
        } else {
            setLoading(true);
            apiService.addCommentToTrail(getToken(), getTrail_id(), getComment()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new Observer<FetchCommentC>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(FetchCommentC res) {

                             if (res.getStatus()) {
                                  setComment("");
                                fetchComments();
                                 comentAdded.onNext( true );
notifyChange();

                            }else {

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            setLoading(false);

                            Toast.makeText( view.getContext(),"e  "+ e, Toast.LENGTH_SHORT ).show();
                        }

                        @Override
                        public void onComplete() {
                            setLoading(false);
                        }
                    });
        }


    }


}
