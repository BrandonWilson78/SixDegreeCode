package app.sixdegree.view.activity.loginModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import app.sixdegree.R;
import app.sixdegree.databinding.ActiviyForgotPasswordBinding;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.viewModel.ForgotPasswordVm;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForgotPassword extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActiviyForgotPasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        ForgotPasswordVm vm = new ForgotPasswordVm();
        binding.setViewModel(vm);
        setContentView(binding.getRoot());

         vm.isVerified.observeOn(Schedulers.io()).subscribe(
                this::handle, throwable ->


                         Log.e("error", "some error")
        );

        binding.back.setOnClickListener(v->{
            onBackPressed();
        });
    }

    private void handle(Boolean aBoolean) {
        if (aBoolean) {
            Intent intent=new Intent( this,LoginActivity.class );
            startActivity(intent  );
            finish();
        }
    }


    @BindingAdapter({"baseRes"})
    public static void handleResChange(View view, BaseRes res) {
        if (res != null) {
            // showSnackBar((Activity) view.getContext(),view.getId(),res.getMessage());
            showToast(view.getContext(), res.getMessage());
        }

    }
}
