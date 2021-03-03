package app.sixdegree.view.activity.loginModule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivitySignupBinding;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.viewModel.SignupVm;

public class SignupActivity extends BaseActivity {
    boolean isPassVisible = false;
    SignupVm signupVm;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
String token="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        ActivitySignupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        SharedPreferences sharedpreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        token=sharedpreferences.getString( "token" ,"");
        signupVm = new SignupVm(token);
        signupVm.setSessionInstance(new AppSession(context));
        binding.setViewModel(signupVm);
        setContentView(binding.getRoot());
        EditText pass = findViewById(R.id.password);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setImageResource( R.drawable.logo_blue );
        findViewById(R.id.password).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (!isPassVisible) {
                            pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable._lockicon_dgreen), null,
                                    ContextCompat.getDrawable(context, R.drawable.show_p),
                                    null);
                            isPassVisible = true;
                        } else {
                            pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable._lockicon_dgreen), null,
                                    ContextCompat.getDrawable(context, R.drawable.hide_p),
                                    null);
                            isPassVisible = false;
                        }

                        // your action here
                        return true;
                    }
                }
                return false;
            }
        });



        EditText email = findViewById(R.id.email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                        email.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable._envelopeicon_dgreen), null,
                                ContextCompat.getDrawable(context, R.drawable._checkicon_blue), null);
                    } else {
                        email.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable._envelopeicon_dgreen), null, null, null);
                    }
                } else {
                    email.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable._envelopeicon_dgreen), null, null, null);
                }
            }
        });


        EditText name = findViewById(R.id.name);
        EditText phonenumber = findViewById(R.id.phone);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 4) {

                    name.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable._usericon_dgreen), null,
                            ContextCompat.getDrawable(context, R.drawable._checkicon_blue), null);

                } else {
                    name.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable._usericon_dgreen), null, null, null);
                }
            }
        });

        phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 9) {

                    phonenumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_phone_black_24dp), null,
                            ContextCompat.getDrawable(context, R.drawable._checkicon_blue), null);

                } else {
                    phonenumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_phone_black_24dp), null, null, null);
                }
            }
        });

        findViewById(R.id.add_image).setOnClickListener(v -> {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE

                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {

                    if (report.areAllPermissionsGranted()) {
                        CropImage.activity()
                                .setAspectRatio(1, 1)
                                .start(SignupActivity.this);
                    }
                    if (report.isAnyPermissionPermanentlyDenied()) {

                        showSettingsDialog();
                    }
                 }
                 @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    token.continuePermissionRequest();
                }
            }).check();
        });


        signupVm.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.loggedIn) {
                    if (signupVm.isLoggedIn()) {
                        goToNextScreen(VerifyEmail.class);
                    }
                }
            }
        });

        binding.addAddress.setOnClickListener(vv -> {
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        signupVm.onActivityResult(requestCode, resultCode, data);

    }

    public void handleBack(View view) {
        onBackPressed();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    //handling button click toast
    @BindingAdapter({"loggedIn"})
    public static void abc(View view, boolean bb) {
        if (bb) {
            goNext(view.getContext(), VerifyEmail.class);
        }
    }


    //handling button click toast
    @BindingAdapter({"errorMsg"})
    public static void abcs(View view, String ss) {

        showSnackBar((Activity) view.getContext(), view.getId(), ss);

    }
    public  Bitmap getThumbnail(Uri uri) throws IOException {
        InputStream input =  getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 48) ? (originalSize / 48) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
}