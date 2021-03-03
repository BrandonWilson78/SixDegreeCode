package app.sixdegree.view.activity.loginModule;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivitySettingsBinding;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.home_module.HomeActivity;
import app.sixdegree.view.activity.sendcontacts.AllContacts;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class AddFriendsActivity extends BaseActivity {

ImageView back;
Boolean toastp=false;
    public BehaviorSubject<Boolean> printtoast = BehaviorSubject.create();
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);


        back=findViewById( R.id.backaddfriends );


         back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        } );
      //  ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friends);


        findViewById(R.id.addFromFb).setOnClickListener(v->{

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, " Hey friends download this awesome app from this link and join me     https://play.google.com/store/apps/details?id=" + getPackageName());
            PackageManager pm = getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

             for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains("facebook")) {
                    toastp=false;
                    printtoast.onNext( false );
                     final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    startActivity(shareIntent);
                    break;
                }else
                {
                    printtoast.onNext( true );
                    toastp=true;
                 }
            }



             if(toastp){
                 Toast.makeText( context, "Facebook not installed in your device.", Toast.LENGTH_SHORT ).show();
             }else {

             }
            //get name exists from behaviour
            printtoast.observeOn( Schedulers.io())
                    .subscribeOn( AndroidSchedulers.mainThread())
                    .subscribe(nameforfriends -> {

if(nameforfriends){
    Toast.makeText( context, "Facebook not installed in your device.", Toast.LENGTH_SHORT ).show();
}else {

}
                    }, throwable -> {
                        Log.e("User data ", throwable.getMessage());
                    });


       });
        findViewById(R.id.btn_fb).setOnClickListener(v->{

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, " Hey friends download this awesome app from this link and join me     https://play.google.com/store/apps/details?id=" + getPackageName());
            PackageManager pm = getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains("facebook")) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    startActivity(shareIntent);
                    break;
                }
            }

       });

        findViewById(R.id.invite_friends).setOnClickListener(v->{

            String shareBody = " Hey friends download this awesome app from this link and join me     https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Six Degree");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Invite using"));

       });

        findViewById(R.id.btn_whatsapp).setOnClickListener(v->{

            String shareBody = " Hey friends download this awesome app from this link and join me     https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Six Degree");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Invite using"));

       });
  findViewById(R.id.btn_google).setOnClickListener(v->{

            String shareBody = " Hey friends download this awesome app from this link and join me     https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Six Degree");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Invite using"));

       });


       findViewById( R.id.addFromContact ).setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick( View v ) {
               startActivity( new Intent( AddFriendsActivity.this, AllContacts.class ) );
           }
       } );findViewById( R.id.btn_enable ).setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick( View v ) {
               startActivity( new Intent( AddFriendsActivity.this, HomeActivity.class ) );
           }
       } );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}
