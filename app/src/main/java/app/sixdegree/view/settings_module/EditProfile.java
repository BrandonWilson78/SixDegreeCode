package app.sixdegree.view.settings_module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView.Guidelines;

import java.util.Arrays;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivityEditProfileBinding;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.viewModel.EditProfileVm;

public class EditProfile extends BaseActivity {

    boolean isProfileClicked = false;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    EditProfileVm vmModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_profile);
        ActivityEditProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        vmModel = new EditProfileVm(getSession().getToken(),getSession());
        vmModel.setSessionInstance(new AppSession(this));
        binding.setViewModel(vmModel);
        setContentView(binding.getRoot());
        binding.executePendingBindings();

        findViewById(R.id.profile_pic).setOnClickListener(v -> {
            isProfileClicked = true;
            CropImage.activity()
                    .setAspectRatio(1, 1)
                    .setGuidelines(Guidelines.ON)
                    .start(this);
        });

        findViewById(R.id.edit_cover).setOnClickListener(v -> {
            isProfileClicked = false;
            CropImage.activity()
                    .setAspectRatio(16, 9)
                    .setGuidelines(Guidelines.ON)
                    .start(this);


        });

        findViewById(R.id.title).setOnClickListener(v -> {
            onBackPressed();
        });
        findViewById(R.id.home_et).setOnClickListener(v -> {
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

    }
    //handling button click toast
    @BindingAdapter({"errorMsg"})
    public static void abc(View view, String message) {
        if (message != null) {
            showSnackBar((Activity) view.getContext(),view.getId(),message);
        }
        //goToNextScreen(LocationPermissionActivity.class);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vmModel.onActivityResult(requestCode, resultCode, data, isProfileClicked);

    }




}
