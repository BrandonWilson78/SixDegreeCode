package app.sixdegree.viewModel;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.HashMap;

import app.sixdegree.R;
import app.sixdegree.model.SignupModel;
import app.sixdegree.model.login.LoginResponseData;
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

public class SignupVm extends BaseVm {


    @Bindable
    public boolean loggedIn;

    @Bindable
    boolean isPicAvailable = false;

    @Bindable
    SignupModel signupModel;
    @Bindable
    private boolean isLoading = false;

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

public    void setLoading(boolean loading) {
        this.isLoading = loading;
        notifyPropertyChanged(BR.loading);

    }
     public String getIsImageSelected() {
        return isImageSelected;
    }

    public void setIsImageSelected(String isImageSelected) {
        this.isImageSelected = isImageSelected;
        notifyPropertyChanged(BR.isImageSelected);
    }

    @Bindable
    String isImageSelected="";

    @Bindable
    String name="";

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber( String phonenumber ) {
        this.phonenumber = phonenumber;
        signupModel.setPhonenumber(phonenumber);
    }

    @Bindable
    String phonenumber="";

    @Bindable
    String email="";

    public String getProfilePic() {
        return profilePic  ;
    }

    public void setProfilePic( String profilePic ) {
        this.profilePic = profilePic;
        notifyPropertyChanged( BR.profilePic );
    }

    @Bindable
    String profilePic="";
    @Bindable
    String pass="";

    @Bindable
    String pic="";

    @Bindable
    String home="";


    String lat="0";
    String lng="0";

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
        signupModel.setLat(lat);
    }

    public String getLng() {
        return lng;

    }

    public void setLng(String lng) {
        this.lng = lng;
        signupModel.setLng(lng);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        signupModel.setName(name);
        notifyPropertyChanged(BR.name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        signupModel.setEmail(email);
        notifyPropertyChanged(BR.email);
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
        signupModel.setPassword(pass);
        notifyPropertyChanged(BR.pass);
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
        signupModel.setProfile(pic);
        notifyPropertyChanged(BR.pic);
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
        signupModel.setHomeLoc(home);
        notifyPropertyChanged(BR.home);
    }

    public boolean isPicAvailable() {
        return isPicAvailable;
    }

    public void setPicAvailable(boolean picAvailable) {
        this.isPicAvailable = picAvailable;
        notifyPropertyChanged(BR.isPicAvailable);
    }

    public SignupModel getSignupModel() {
        return signupModel;
    }

    public void setSignupModel(SignupModel signupModel) {
        this.signupModel = signupModel;
        notifyPropertyChanged(BR.signupModel);
    }

    public String getFirebasetoken() {
        return firebasetoken;
    }

    public void setFirebasetoken( String firebasetoken ) {
        this.firebasetoken = firebasetoken;
        notifyPropertyChanged( BR.firebasetoken );
    }

    @Bindable
String firebasetoken="";


    public SignupVm(String token) {

        setSignupModel(new SignupModel("", "", "","", "", "","",""));

        this.firebasetoken=token;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        notifyPropertyChanged(BR.loggedIn);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }


    @BindingAdapter({"bind:signupModel.profile"})
    public static void loadProfile(ShapeableImageView view, String profilePic) {


        Uri uri = Uri.fromFile(new File(profilePic));
        AppUtils.loadPicasso(uri,view);


    }

    @BindingAdapter({"bind:profilePic"})
    public static void loadProfilee( ImageView view, String profilePic) {
        if (profilePic == null) {
            view.setImageResource( R.drawable.logo_blue );

        }else {
            AppUtils.roundImageWithGlidePlaceHolder(view,profilePic);
        }

    }
    public void validateData() {
        SignupModel signupModel = getSignupModel();
        if (signupModel.getName().isEmpty()) {
            setErrorMsg("Please enter valid name");
        } else if (!validateEmail()) {
            setErrorMsg("Please enter valid email");
        }else if (getPhonenumber().isEmpty() || getPhonenumber().length()< 9) {
            setErrorMsg("Please enter valid phone number");
        } else if (TextUtils.isEmpty(signupModel.getPassword()) || signupModel.getPassword().length() < 5) {
            setErrorMsg("Please enter valid minimum 5 digit password!");
        } else if (signupModel.getHomeLoc().isEmpty()) {
            setErrorMsg("Please enter Location");
        } else {
            updateDetails();
        }


    }

    public boolean validateEmail() {
        return !TextUtils.isEmpty(getSignupModel().getEmail()) && Patterns.EMAIL_ADDRESS.matcher(getSignupModel().getEmail()).matches();
    }


    private void updateDetails() {
        setLoading(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", getSignupModel().getName());
        map.put("password", getSignupModel().getPassword());
        map.put("email", getSignupModel().getEmail());
        map.put("mobile", getSignupModel().getPhonenumber());

        MultipartBody.Part profile_image = null;


        if (getPic() != null && !getPic().isEmpty()) {

            File file = new File(getProfilePic());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            profile_image = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);
        }
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), getSignupModel().getName());
        RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), getSignupModel().getPassword());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), getSignupModel().getEmail());
        RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), getSignupModel().getPhonenumber());
        RequestBody home = RequestBody.create(MediaType.parse("multipart/form-data"), getSignupModel().getHomeLoc());
        RequestBody country = RequestBody.create(MediaType.parse("multipart/form-data"), getSignupModel().getHomeLoc());
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), getSignupModel().getLat());
        RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), getSignupModel().getLng());
        RequestBody firebasetoken = RequestBody.create(MediaType.parse("multipart/form-data"), getFirebasetoken());

        apiService.registerUser(name, email,mobile, password, home, country, latitude, longitude, firebasetoken,profile_image).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<LoginResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponseData res) {
                        setErrorMsg(res.getMessage());
                        if (res.getStatus()) {
                            getSessionInstance().saveUserData(res.getData());
                            setLoggedIn(true);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        setErrorMsg(e.getMessage());
                        setLoading(false);
                    }

                    @Override
                    public void onComplete() {

                        setLoading(false);
                    }
                });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int AUTOCOMPLETE_REQUEST_CODE = 1;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                getSignupModel().setProfile(resultUri.getPath());
                setPicAvailable(true);
                setPic( String.valueOf( resultUri ) );
                setIsImageSelected("Picture Selected");
                setProfilePic( resultUri.getPath() );
                notifyChange();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }else   if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("sign lat", "Place: " + place.getName() + ", " + place.getLatLng());
                 setLng(String.valueOf(place.getLatLng().longitude));
                setLat(String.valueOf(place.getLatLng().latitude));
                setHome(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("trails fragment", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }



}
