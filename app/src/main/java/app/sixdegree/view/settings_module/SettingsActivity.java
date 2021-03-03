package app.sixdegree.view.settings_module;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivitySettingsBinding;
import app.sixdegree.network.ApiService;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.ContentDisplayActivity;
import app.sixdegree.view.activity.Splash;
import app.sixdegree.view.activity.blockedusers.BlockedUserActivity;
import app.sixdegree.view.activity.loginModule.AddFriendsActivity;
import app.sixdegree.viewModel.SettingsVm;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    int selected = -1;


    PowerMenu powerMenu;
      SettingsVm settingsVm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        Log.e( "tok in set","--"+ getSessionData().getToken());
        settingsVm = new SettingsVm(getSession() ,getSession().getToken(),String.valueOf(  getSessionData().getId()),String.valueOf(getSessionData()
                .getAccountType()));
        settingsVm.setSessionInstance(new AppSession(context));
        settingsVm.setToolbarTitle("Settings");
         binding.setViewModel(settingsVm);
        setContentView(binding.getRoot());

        findViewById(R.id.logout).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure to logout?");
            builder.setPositiveButton("Logout", (dialog, which) -> {


                getSession().logoutUser();
                goToNextScreen(Splash.class);
                finishAffinity();
            });

            builder.setNegativeButton("cancel", (dialog, which) -> {

            });
            builder.show();
        });





        binding.btnInviteFrnds.setOnClickListener(v->{
            goToNextScreen(AddFriendsActivity.class);
        });




        findViewById(R.id.btn_story).setOnClickListener(vv -> {

            goToNextScreen(ContentDisplayActivity.class, "url", ApiService.OUR_STORY);
        });
        findViewById(R.id.btn_legal).setOnClickListener(vv -> {

            goToNextScreen(ContentDisplayActivity.class, "url", ApiService.LEGAL);
        });

        findViewById(R.id.btn_contact).setOnClickListener(vv -> {

            goToNextScreen(ContentDisplayActivity.class, "url", ApiService.CONTACT_US);
        });
        findViewById(R.id.btn_profile).setOnClickListener(vv -> {

            goToNextScreen(EditProfile.class);
        });

        findViewById(R.id.btn_account).setOnClickListener(vv -> {

            goToNextScreen(AccountActivity.class);
        });

        findViewById(R.id.btn_invite_frnds).setOnClickListener(vv -> {

            goToNextScreen( AddFriendsActivity.class);
        });

        findViewById(R.id.tv_blocked_user).setOnClickListener(vv -> {

            goToNextScreen( BlockedUserActivity.class);
        });

        findViewById(R.id.back_btn).setOnClickListener(vv -> {

            handleToolBack();
        });
  findViewById(R.id.btn_insta).setOnClickListener(vv -> {

      goToUrl ( "https://www.instagram.com/");
        });
  findViewById(R.id.btn_facebook).setOnClickListener(vv -> {
      goToUrl ( "https://www.facebook.com/");
        });  findViewById(R.id.btn_twitter).setOnClickListener(vv -> {

            goToUrl ( "https://twitter.com/");
        });

        findViewById(R.id.unit_val_tv).setOnClickListener(vv -> {


            List<String> units = Arrays.asList(getResources().getStringArray(R.array.units));
            showUnits(units, R.id.unit_val_tv);
            selected = 1;

        });

        findViewById(R.id.temp_unit).setOnClickListener(vv -> {


            List<String> units = Arrays.asList(getResources().getStringArray(R.array.temprature));
            showTemprature(units, R.id.temp_unit);
            selected = 2;
        });

        findViewById(R.id.map_style_val).setOnClickListener(vv -> {
            List<String> units = Arrays.asList(getResources().getStringArray(R.array.mapstyle));
            showUnits(units, R.id.map_style_val);
            selected = 3;
        });

    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {


            switch (selected) {
                case 1:
                    settingsVm.setUnitApi(item.getTitle());
                    Log.e("1", "1");
                    break;
                case 2:
                    if (position == 0) {
                        settingsVm.setTempratureApi("c");
                    } else {
                        settingsVm.setTempratureApi("f");
                    }
                    Log.e("2", "2");
                    break;
                case 3:
                    settingsVm.setMapApi(item.getTitle());
                    Log.e("3", "3");
                    break;
                case -1:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + selected);
            }

            selected = -1;
            powerMenu.dismiss();

        }
    };

    //handling button click toast
    @BindingAdapter({"baseRes"})
    public static void handleResChange(View view, BaseRes res) {
        if (res != null) {
            showToast(view.getContext(), res.getMessage());

        }
    }


    public void showUnits(List<String> list, int id) {

        TextView view = findViewById(id);

        List<PowerMenuItem> l1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            l1.add(new PowerMenuItem(list.get(i), false));
        }



        powerMenu = new PowerMenu.Builder(context)
                .addItemList(l1)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(ContextCompat.getColor(context, R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setSelectedTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
        powerMenu.showAsDropDown(view);

    }

    public void showTemprature(List<String> list, int id) {

        TextView view = findViewById(id);

        List<PowerMenuItem> l1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            l1.add(new PowerMenuItem(list.get(i), false));
        }



        powerMenu = new PowerMenu.Builder(context)
                .addItemList(l1)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(ContextCompat.getColor(context, R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setSelectedTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
        powerMenu.showAsDropDown(view);

    }


    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
