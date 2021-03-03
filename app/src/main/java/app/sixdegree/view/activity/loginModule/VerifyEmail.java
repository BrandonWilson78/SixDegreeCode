package app.sixdegree.view.activity.loginModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivityVerifyEmailBinding;
import app.sixdegree.model.login.Data;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.home_module.HomeActivity;
import app.sixdegree.viewModel.VerifyEmailVm;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static app.sixdegree.view.activity.BaseActivity.showToast;

public class VerifyEmail extends AppCompatActivity {
    private VerifyEmailVm verifyVm;
    Data sessionData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_verify_email);

        ActivityVerifyEmailBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_verify_email);
        verifyVm = new VerifyEmailVm();
        initSessionData();
        activityMainBinding.setVm(verifyVm);
        setContentView(activityMainBinding.getRoot());
        verifyVm.setUserEmail(getSessionData().getEmail());

        verifyVm.setToken(getSession().getToken());

        // ButterKnife.bind(this);
       verifyVm.isVerified.observeOn(Schedulers.io())
                .subscribe(this::handl, throwable -> {
                    Log.e("User data ", throwable.getMessage());
                });

        findViewById(R.id.back).setOnClickListener(vv->{
            finish();
        });

    }

    private void handl(Boolean verified) {
        if (verified) {
            goToNextScreen(HomeActivity.class);
            finishAffinity();
        }

    }

    //handling button click toast
    @BindingAdapter({"baseRes"})
    public static void handleResChange(View view, BaseRes res) {
        if (res != null) {
            showToast(view.getContext(), res.getMessage());
        }
    }

    public void goToNextScreen(Class aClass) {
        startActivity(new Intent(this, aClass));
    }

    void initSessionData() {
        sessionData = getSession().getData();
    }

    public Data getSessionData() {
        return sessionData;
    }
    public AppSession getSession() {
        return new AppSession(this);
    }

}
