package app.sixdegree.view.settings_module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivityAccountBinding;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.loginModule.LoginActivity;
import app.sixdegree.viewModel.EditAccountVm;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AccountActivity extends BaseActivity {

    EditAccountVm editAccountVm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editAccountVm = new EditAccountVm(getSession().getToken());
        editAccountVm.setSessionInstance(new AppSession(this));
        ActivityAccountBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        binding.setViewModel(editAccountVm);
        setContentView(binding.getRoot());

        findViewById(R.id.title).setOnClickListener(v->{
            onBackPressed();
        });

        editAccountVm.isVerified.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(trail -> {
                    if(trail){
                        Intent intent=new Intent( this, LoginActivity.class );
                        startActivity(intent  );
                        finish();

                    }


                }, throwable -> {
                    Log.e("User data ", throwable.getMessage());
                });
    }

    //handling button click toast
    @BindingAdapter({"errorMsg"})
    public static void abc(View view, String message) {
        if (message != null) {
            showSnackBar((Activity) view.getContext(), view.getId(), message);
        }
        //goToNextScreen(LocationPermissionActivity.class);
    }


}
