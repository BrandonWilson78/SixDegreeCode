package app.sixdegree.viewModel;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.profileupdatees.ProfileUpdateres;
import app.sixdegree.network.responses.settings_mod.profile.Data;
import app.sixdegree.network.responses.settings_mod.profile.UserProfile;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditProfileVm extends BaseVm {
@Bindable
    String lat = "0";

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
        notifyPropertyChanged(BR.lat);
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
        notifyPropertyChanged(BR.lng);
    }
@Bindable
    String lng = "0";

    @Bindable
    boolean isLoading;

    @Bindable
    Data userData;

    @Bindable
    String url;

    String token;

    @Bindable
    String coverPic;
    @Bindable
    String email;
    @Bindable
    String surname;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        notifyPropertyChanged(BR.mobile);
    }

    @Bindable
    String mobile = "";
    @Bindable
    String name;
    @Bindable
    String homecity;
    @Bindable
    String bio;
    @Bindable
    String errorMsg;
    @Bindable
    int count = 0;
    @Bindable
    private String profilePic;

    public AppSession getAppSession() {
        return appSession;
    }

    public void setAppSession(AppSession appSession) {
        this.appSession = appSession;
        notifyPropertyChanged(BR.appSession);
    }

    @Bindable
    AppSession appSession;

    public String getProfilepicsession() {
        return profilepicsession;
    }

    public void setProfilepicsession(String profilepicsession) {
        this.profilepicsession = profilepicsession;
        notifyPropertyChanged(BR.profilepicsession);
        app.sixdegree.model.login.Data data = getAppSession().getData();
        data.setProfileImage(profilepicsession);
        getAppSession().saveUserData(data);
    }

    @Bindable
    String profilepicsession;

    public EditProfileVm(String token, AppSession appSession) {
        this.token = token;
        this.appSession = appSession;
        getUserProfile();
    }


    @BindingAdapter({"bind:profilePic"})
    public static void loadProfile(ImageView view, String profilePic) {

        Log.wtf("ffffffffffffffffffff", profilePic + "==");

        if (profilePic != null) {
            if (profilePic.startsWith("http")) {
                AppUtils.loadPicasso(profilePic, view);

            } else {

                Uri uri = Uri.fromFile(new File(profilePic));
                Log.e("uri", String.valueOf(uri));
                AppUtils.loadPicasso(uri, view);
            }
        } else {
            Log.e("profile", "null");
        }


    }

    @BindingAdapter({"bind:coverPic"})
    public static void loadImage(RelativeLayout view, String coverPic) {
        if (coverPic != null) {
            if (coverPic.contains("http://")) {
                AppUtils.picassoLoadBgHttp(view, coverPic);
            } else {
                Uri uri = Uri.fromFile(new File(coverPic));
                AppUtils.picassoLoadBgUri(view, uri);
            }

        }


    }

    public String getCount() {
        return count + "/120";
    }

    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
        notifyPropertyChanged(BR.profilePic);

    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
        if (bio != null) {
            if (!bio.equals("")) {
                setCount(bio.length());
            } else {
                setCount(0);
            }
        } else {
            setCount(0);
        }


        notifyPropertyChanged(BR.bio);
    }

    public String getHomecity() {
        return homecity;
    }

    public void setHomecity(String homecity) {
        this.homecity = homecity;
        notifyPropertyChanged(BR.homecity);
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        notifyPropertyChanged(BR.surname);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
        notifyPropertyChanged(BR.coverPic);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        notifyPropertyChanged(BR.errorMsg);
    }

    public Data getUserData() {
        return userData;
    }

    public void setUserData(Data userData) {
        this.userData = userData;
        notifyPropertyChanged(BR.userData);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);

    }

    public String getToken() {
        return token;
    }

    private void getUserProfile() {
        setLoading(true);
        apiService.getProfileDetails(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserProfile>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserProfile data) {

                        if (data.getStatus()) {
                            setProfilePic(data.getData().getProfileImage());
                            setCoverPic(data.getData().getCoverImage());
                            setSurname(data.getData().getSurname());
                            setName(data.getData().getName());
                            setMobile(data.getData().getMobile());
                            setHomecity(data.getData().getHome());
                            setBio(data.getData().getBio());
                            setEmail(data.getData().getEmail());
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


    public void validateData() {


        if (TextUtils.isEmpty(getName())) {
            setErrorMsg("Please enter name");
        } else if (TextUtils.isEmpty(getSurname())) {
            setErrorMsg("Please enter surname");
        } else if (TextUtils.isEmpty(getMobile()) || getMobile().length() < 10) {
            setErrorMsg("Please enter Phone Number");
        } else if (TextUtils.isEmpty(getHomecity())) {
            setErrorMsg("Please enter Home City");
        } else if (TextUtils.isEmpty(getBio())) {
            setErrorMsg("Please write something about you in bio.");
        } else {
            setLoading(true);
            updateDetails();
        }
    }


    private void updateDetails() {

        MultipartBody.Part profile_image = null;
        MultipartBody.Part cover_image = null;


        if (getProfilePic() != null) {
            if (!getProfilePic().startsWith("http")) {
                File file = new File(getProfilePic());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                profile_image = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);


            }
        }
        if (getCoverPic() != null) {
            if (!getCoverPic().startsWith("http")) {
                File file = new File(getCoverPic());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                cover_image = MultipartBody.Part.createFormData("cover_image", file.getName(), requestFile);

            }
        }


        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), getName());
        RequestBody surname = RequestBody.create(MediaType.parse("multipart/form-data"), getSurname());
        RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), getMobile());
        RequestBody bio = RequestBody.create(MediaType.parse("multipart/form-data"), getBio());
        RequestBody home = RequestBody.create(MediaType.parse("multipart/form-data"), getHomecity());
        RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), getLat());
        RequestBody lng = RequestBody.create(MediaType.parse("multipart/form-data"), getLng());
        RequestBody country = RequestBody.create(MediaType.parse("multipart/form-data"), getSessionInstance().getData().getName());


        apiService.updateUserProfile(token, name, surname, mobile, bio, home, country,lat,lng, profile_image, cover_image).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<ProfileUpdateres>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ProfileUpdateres res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {

                            if (res.getData().getProfileImage() != null) {
                                setProfilepicsession(res.getData().getProfileImage());
                            }

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


    public void onActivityResult(int requestCode, int resultCode, Intent data, boolean isProfileClicked) {
        int AUTOCOMPLETE_REQUEST_CODE = 1;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                if (isProfileClicked) {
                    setProfilePic(resultUri.getPath());
                }
                if (!isProfileClicked) {
                    setCoverPic(resultUri.getPath());
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("trails fragment", "Place: " + place.getName() + ", " + place.getAddress());

                setHomecity(place.getName());
                setLat(String.valueOf(place.getLatLng().latitude));
                setLng(String.valueOf(place.getLatLng().longitude));

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("trails fragment", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

}
