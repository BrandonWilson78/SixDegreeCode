package app.sixdegree.view.activity;

import android.Manifest;
import android.os.Bundle;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import app.sixdegree.R;
import app.sixdegree.view.activity.loginModule.SignupActivity;
import butterknife.ButterKnife;

public class LocationPermissionActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);
        ButterKnife.bind(this);


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (report.areAllPermissionsGranted()) {
                    goToNextScreen(SignupActivity.class);
                    finish();
                }


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                token.continuePermissionRequest();
            }
        }).check();


        findViewById(R.id.btn_enable).setOnClickListener(v -> {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION

                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {

                    if (report.areAllPermissionsGranted()) {
                        goToNextScreen(SignupActivity.class);
                    }
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        showAlert("Required", "location permision required to make this app functionality work.");
                    }


                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    token.continuePermissionRequest();
                }
            }).check();
        });


        findViewById(R.id.back).setOnClickListener(v -> {

        });

    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}
