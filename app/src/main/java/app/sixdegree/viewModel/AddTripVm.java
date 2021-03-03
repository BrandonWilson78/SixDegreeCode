package app.sixdegree.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.google.maps.model.LatLng;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.sixdegree.BR;
import app.sixdegree.network.responses.addeditresponses.AddEditResponse;
import app.sixdegree.network.responses.addtripres.AddTripRes;
import app.sixdegree.network.responses.edittripres.EditTripRes;
import app.sixdegree.network.responses.fetchsharingoptionsres.FetchSharingOptionsRes;
import app.sixdegree.network.responses.getSingletripDetails.SingleTripDetailsRes;
import app.sixdegree.network.responses.getsingleTrailDetails.GetSingleTrailDetails;
import app.sixdegree.network.responses.gettripcategories.Data;
import app.sixdegree.network.responses.gettripcategories.GetTripCategories;
import app.sixdegree.utils.AppUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class AddTripVm extends BaseVm {
    public BehaviorSubject<List<Data>> tripCat = BehaviorSubject.create();
    public BehaviorSubject<Boolean> isFinished = BehaviorSubject.create();
    public BehaviorSubject<Boolean> isUpdatedFinished = BehaviorSubject.create();
    public BehaviorSubject<Boolean> isTagNamesVisible = BehaviorSubject.create();
    public BehaviorSubject<LatLng> latlngs = BehaviorSubject.create();
    public BehaviorSubject<Boolean> loader = BehaviorSubject.create();
    public BehaviorSubject<List<String>> sharingoptionslist = BehaviorSubject.create();

    List<Data> tripCatList = new ArrayList<>();
    @Bindable
    String tripid = "";

    public String getFriendsid() {
        return friendsid;
    }

    public void setFriendsid( String friendsid ) {
        this.friendsid = friendsid;
        notifyPropertyChanged( BR.friendsid );
    }

    @Bindable
    String friendsid = "";

    public String getFriendsnames() {
        return friendsnames;
    }

    public void setFriendsnames( String friendsnames ) {
        this.friendsnames = friendsnames;
        notifyPropertyChanged( BR.friendsnames );
    }

    @Bindable
    String friendsnames = "";
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
    @Bindable
    String name = "";
    @Bindable
    String description = "";
    @Bindable
    String count;
    @Bindable
    String startdate = "Select Date";
    @Bindable
    String enddate = "Select Date";
    @Bindable
    String whoCanSee = "Select";
    @Bindable
    String category = "Select";
    @Bindable
    String mapStyle = "Select";

    @Bindable
    String trail_id = "";
    private String token;
    @Bindable
    private String profilePic;

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic( String coverPic ) {
        this.coverPic = coverPic;
        notifyPropertyChanged( BR.coverPic








        );
    }

    @Bindable
String coverPic="";

    public AddTripVm( String token, String trail_id,String friendsid) {
        this.token = token;
        this.trail_id = trail_id;
        this.friendsid=friendsid;
fetchSharingOptions();

        getTripCategories();
        Log.e("trail id in", "in aevm--" + getTrail_id());
        Log.e("friendsid in", "in aevm--" + friendsid);


    }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage( ImageView view, String coverPic) {
        if (coverPic != null) {

            AppUtils.showcoverpic(view, coverPic);
        }


    }





    public String getTrail_id() {
        return trail_id;
    }

    public void setTrail_id(String trail_id) {
        this.trail_id = trail_id;
        notifyPropertyChanged(BR.trail_id);
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
        notifyPropertyChanged(BR.profilePic);
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
        notifyPropertyChanged(BR.tripid);
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
    }

    public String getName() {
        return name;
    }

  

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        setCount(String.valueOf(description.length()));
        notifyPropertyChanged(BR.description);
    }

    public String getCount() {
        if (getDescription() != null) {
            if (!getDescription().isEmpty()) {
                return getDescription().length() + "/500";
            } else {
                return "0/500";
            }
        }
        return "0/500";
    }

    public void setCount(String count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
        notifyPropertyChanged(BR.startdate);
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
        notifyPropertyChanged(BR.enddate);
    }

    public String getWhoCanSee() {
        return whoCanSee;
    }

    public void setWhoCanSee(String whoCanSee) {
        this.whoCanSee = whoCanSee;
        notifyPropertyChanged(BR.whoCanSee);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        notifyPropertyChanged(BR.category);
    }

    public String getMapStyle() {
        return mapStyle;
    }

    public void setMapStyle(String mapStyle) {
        this.mapStyle = mapStyle;
        notifyPropertyChanged(BR.mapStyle);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);

    }



    public void validateData() {


        if (getName().isEmpty()) {

            setErrorMsg("Please enter Trip name");

        } else if (getDescription().isEmpty()) {
            setErrorMsg("Please enter description");
        } else if (getStartdate().equals("Select Date")) {
            setErrorMsg("Please enter Start Date");
        } else if (getEnddate().equals("Select Date")) {
            setErrorMsg("Please enter End Date");
        } else if (getWhoCanSee().equals("Select")) {
            setErrorMsg("Please select who can see my trip");
        } else if (getCategory().equals("Select")) {
            setErrorMsg("Please select category");
        }   else if (getCoverPic().isEmpty()) {
            setErrorMsg("Please add image");
        }else{


            if (getTrail_id().isEmpty()) {
                addTrip();
            } else {
                updateTrail();
            }


        }


    }

    public void addTrip() {
        setLoading(true);

loader.onNext(true);

        HashMap<String, String> map = new HashMap<>();
        map.put("name", getName());
        map.put("summary", getDescription());
        map.put("start_date", getStartdate());
        map.put("end_date", getEnddate());

        if (getWhoCanSee().equalsIgnoreCase("My Friends")) {
            map.put("who_can_see", "1");
        } else if (getWhoCanSee().equalsIgnoreCase("Public")) {
            map.put("who_can_see", "0");
        } else {
            map.put("who_can_see", "2");
        }


        for (int i = 0; i < tripCatList.size(); i++) {
            if (getCategory().equalsIgnoreCase(tripCatList.get(i).getName())) {
                map.put("category", String.valueOf(tripCatList.get(i).getId()));
            }

        }


        map.put("map_style", getMapStyle());
        if(getFriendsid()==null){
            map.put("friendsid", "");
        }else {
            map.put("friendsid", getFriendsid());
        }

        map.put("status", "1");



        MultipartBody.Part trail_pictures = null;


        if (getCoverPic() != null) {
            if (!getCoverPic().startsWith("http")) {
                File file = new File(getCoverPic());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                trail_pictures = MultipartBody.Part.createFormData("trip_picture", file.getName(), requestFile);


            }
        }


        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("name"));
        RequestBody summary = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("summary"));
        RequestBody start_date = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("start_date"));
        RequestBody end_date = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("end_date"));
        RequestBody who_can_see = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("who_can_see"));
        RequestBody category = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("category"));
        RequestBody map_style = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("map_style"));
        RequestBody status = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("status"));
        RequestBody friendsid = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("friendsid"));



        apiService.addTrip(token, name, summary, start_date, end_date, who_can_see, category,
                map_style, status,friendsid, trail_pictures).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<AddTripRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddTripRes res) {
                        setErrorMsg(res.getMessage());

                        if (res.getStatus()) {
                         isFinished.onNext(true);


                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        loader.onNext(false);
                    }

                    @Override
                    public void onComplete() {

                        setLoading(false);
                        loader.onNext(false);
                    }
                });
    }


    public void updateTrail() {
        setLoading(true);

        loader.onNext(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", getName());
        map.put("summary", getDescription());
        map.put("start_date", getStartdate());
        map.put("end_date", getEnddate());

        if (getWhoCanSee().equalsIgnoreCase("My Friends")) {
            map.put("who_can_see", "1");
        } else if (getWhoCanSee().equalsIgnoreCase("Public")) {
            map.put("who_can_see", "0");
        } else {
            map.put("who_can_see", "2");
        }


        for (int i = 0; i < tripCatList.size(); i++) {
            if (getCategory().equalsIgnoreCase(tripCatList.get(i).getName())) {
                map.put("category", String.valueOf(tripCatList.get(i).getId()));
            }

        }


        map.put("map_style", getMapStyle());
        map.put("status", "1");
        map.put("trip_id", getTripid());


      //  Log.e( "friendsid--","update-"+getFriendsid() );

        if(getFriendsid()==null){
            map.put("friendsid", "");
        }else {
            map.put("friendsid", getFriendsid());
        }




        MultipartBody.Part trail_pictures = null;


        if (getCoverPic() != null) {
            if (!getCoverPic().startsWith("http")) {
                File file = new File(getCoverPic());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                trail_pictures = MultipartBody.Part.createFormData("trip_picture", file.getName(), requestFile);

                Log.e( "getCoverPic--","update-"+getCoverPic() );
            }
        }


        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("name"));
        RequestBody summary = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("summary"));
        RequestBody start_date = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("start_date"));
        RequestBody end_date = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("end_date"));
        RequestBody who_can_see = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("who_can_see"));
        RequestBody category = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("category"));
        RequestBody map_style = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("map_style"));
        RequestBody status = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("status"));
        RequestBody trip_id = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("trip_id"));
        RequestBody friendsid = RequestBody.create(MediaType.parse("multipart/form-data"), map.get("friendsid"));


        apiService.editTrip(token, name, summary, start_date, end_date, who_can_see, category,
                map_style, status, trip_id, friendsid,trail_pictures).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<EditTripRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EditTripRes res) {
                        setErrorMsg(res.getMessage());

                        if (res.getStatus()) {
                            isUpdatedFinished.onNext(true);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        loader.onNext(false);
                    }

                    @Override
                    public void onComplete() {

                        setLoading(false);
                        loader.onNext(false);
                    }
                });
    }

    public void getTripCategories() {
        //setLoading(true);
        apiService.getTripCategories(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTripCategories>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetTripCategories data) {
                        if (data.getStatus()) {
                            tripCat.onNext(data.getData());
                            tripCatList = data.getData();
                            Log.e("tripcat--", "--" + tripCat);

                            if (!getTrail_id().isEmpty()) {
                                getSingleTripDetails();
                            }
                        } else {
                            setErrorMsg(data.getMessage());
                        }

                        //  setLoading(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        //  setLoading(false);


                    }

                    @Override
                    public void onComplete() {
                        //   setLoading(false);

                    }
                });
    }
    public void fetchSharingOptions() {
        //setLoading(true);
        apiService.fetchSharingOptions(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FetchSharingOptionsRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(FetchSharingOptionsRes data) {

                        if (data.getStatus()) {
                            sharingoptionslist.onNext( data.getData() );
                            data.getData();
                            Log.e(" data.getData();--", "--" +  data.getData());


                        } else {
                            setErrorMsg(data.getMessage());
                        }

                        //  setLoading(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        //  setLoading(false);


                    }

                    @Override
                    public void onComplete() {
                        //   setLoading(false);

                    }
                });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
               setProfilePic(resultUri.getPath());

setCoverPic( resultUri.getPath() );
                Log.e("profile", "--" + resultUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    public void getSingleTripDetails() {
        setLoading(true);
        loader.onNext(true);
        apiService.getSingleTripDetails(token, getTrail_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleTripDetailsRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SingleTripDetailsRes data) {
                        if (data.getStatus()) {
                            setName(data.getData().getName());
                            setDescription(data.getData().getSummary());
                            setStartdate(data.getData().getStartDate());
                            setEnddate(data.getData().getEndDate());
                            setTripid(String.valueOf(data.getData().getId()));
setFriendsid( data.getData().getTagIds() );

setFriendsnames( data.getData().getTagNames() );


if(data.getData().getTagNames()==null){
    isTagNamesVisible.onNext( false );
}else {
    isTagNamesVisible.onNext( true );
}




                            if(data.getData().getLatitude()!=null){
                                latlngs.onNext(new LatLng(Double.parseDouble(data.getData().getLatitude()),
                                        Double.parseDouble(data.getData().getLongitude())));
                            }

                            setLatitude(data.getData().getLatitude());
                            setLongitude(data.getData().getLongitude());
                            //set who can see
                            if (data.getData().getWhoCanSee() == 0) {
                                setWhoCanSee("Public");
                            } else if (data.getData().getWhoCanSee() == 1) {
                                setWhoCanSee("My Friends");
                            } else {
                                setWhoCanSee("Friends of Friends");
                            }
                            // set category
                            for (int i = 0; i < tripCatList.size(); i++) {
                                if (data.getData().getCategoryInfo().getId() == tripCatList.get(i).getId()) {
                                    setCategory(tripCatList.get(i).getName());
                                }
                            }
                            // set map style
                            setMapStyle(data.getData().getMapStyle());
                            // profile image
                          //  setProfilePic(data.getData().getTrip_picture());
                            setCoverPic(data.getData().getTripPicture());
                            notifyChange();
                        } else {
                            setErrorMsg(data.getMessage());
                        }

                        setLoading(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        loader.onNext(false);
Log.e( "error in fetch trip","--"+e.toString() );

                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);
                        loader.onNext(false);
                    }
                });
    }


}
