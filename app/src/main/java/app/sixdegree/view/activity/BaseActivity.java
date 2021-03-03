package app.sixdegree.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import app.sixdegree.R;
import app.sixdegree.model.login.Data;
import app.sixdegree.model.login.LoginResponseData;
import app.sixdegree.network.ApiFactory;
import app.sixdegree.network.ApiService;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.home_module.HomeActivity;
import app.sixdegree.view.activity.loginModule.VerifyEmail;
import app.sixdegree.viewModel.BaseVm;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class BaseActivity extends AppCompatActivity {
    public Context context = this;
 public   ApiService apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
    Data sessionData;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    //facebook call manager
    public CallbackManager callbackManager;
    String token="";
 public    BaseVm baseVm=new BaseVm();
    public static final String MyPREFERENCES = "token" ;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
   public static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    public static void goNext(Context context, Class aClass) {
        context.startActivity(new Intent(context, aClass));
        ((Activity) context).finish();
    }

    public static void showSnackBar(Activity context, int id, String msg) {
        Snackbar.make(context.findViewById(id), msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void setFullscreen() {
        new AppUtils().setFullScreen(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSessionData();
        FirebaseApp.initializeApp( this );

        //google login
      //  initialiseGlogin();


//getFirebase token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "getInstanceId failed", task.getException());
                            return;
                        }
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        editor = sharedpreferences.edit();
                        // Get new Instance ID token
                        token = task.getResult().getToken();
editor.putString( "token",token );
editor.commit();
                        // Log and toast

                        //   String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("token:", token);
                        //  Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        //facebook login initialise
    //    facebookLoginInitialise();
      /*   if (getSessionData() != null) {
            if (!getSessionData().getInterestAdded()) {
                goToNextScreen(ActivityExloreCategory.class);
                finishAffinity();
            }
        }
*/
//
//        //observe changes in view model for login response
//        Disposable subscribe = baseVm.subject.observeOn( Schedulers.io())
//                .subscribe(this::handl, throwable -> {
//                    Log.e("User data ", throwable.getMessage());
//                });
//        addToDispose(subscribe);







    }

    public void goToNextScreen(Class aClass) {
        startActivity(new Intent(this, aClass));
    }

    public void goToNextScreen(Class aClass, String key, String value) {
        Intent intent = new Intent(this, aClass);
        intent.putExtra(key, value);

        startActivity(intent);
    }

    public void setToolbarTitle(String title) {
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText(title);
    }

    public void handleToolBack() {
        onBackPressed();
    }

    void initSessionData() {
        sessionData = getSession().getData();
    }

    public Data getSessionData() {
        return sessionData;
    }

    public AppSession getSession() {
        return new AppSession(context);
    }

    @Override
    protected void onResume() {
        initSessionData();
        super.onResume();
    }

    public void showMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void showAlert(String title, String msg) {
        AlertDialog.Builder dia = new AlertDialog.Builder(this);
        dia.setMessage(msg);
        dia.setTitle(title);
        dia.setPositiveButton("Dismiss", (dialog, which) -> {
        });
        dia.show();
    }

    @Override
    public void onStop() {
        compositeDisposable.dispose();
        super.onStop();
    }
    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public void addToDispose(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
public void refreshActivity(){
    finish();
    overridePendingTransition( 0, 0);
    startActivity(getIntent());
    overridePendingTransition( 0, 0);
}

    public void handleGoogleLogin() {
        signOut();
        signIn();
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        /*Toast.makeText(Login.this, "User logged out", Toast.LENGTH_SHORT).show();*/
                    }
                });
    }


    public void initialiseGlogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }




    //initialize facebook login
    public void facebookLoginInitialise() {

        /**
         * facebook login
         */

        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        // AppEventsLogger.activateApp(MainActivity.this);
        callbackManager = CallbackManager.Factory.create();



        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();

                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                System.out.println("res--" + response);

                                if (object.has("id")){

                                    try {
                                        String name=object.getString("name");


                                        String fid = object.getString("id");
                                        String email= object.getString("email");
                                        Log.d("image--", "https://graph.facebook.com/" + fid + "/picture?type=large");

                                        //Social login api



                                        HashMap<String,String>map=new HashMap<>(  );
                                        map.put( "name",name );
                                        map.put( "login_type","facebook" );
                                        map.put( "email",email );
                                        map.put( "social_id",fid );

 baseVm.socialLogin(map);
                                        //Social login api ends

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                else {
                                    Log.e("FBLOGIN_FAILD", object.toString());
                                }

                                LoginManager.getInstance().logOut();
                            }





                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields","name,email,id,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


/**
 * facebook login
 */


    }




    public void handleFBLogin(){
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
    }



    public  String getAdressByLatLng(double latitude,double longitude){
        Geocoder geocoder;
        String address="";
          String knownName="";
        List<Address> addresses=new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            if (latitude==0.0){

            }else {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

               if (addresses.size()>0){
                   address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                   String city = addresses.get(0).getLocality();
                   String state = addresses.get(0).getAdminArea();
                   String country = addresses.get(0).getCountryName();
                   String postalCode = addresses.get(0).getPostalCode();
                     knownName = addresses.get(0).getFeatureName();


                   Log.e("known name--","--"+knownName);

               }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return knownName;
    }

    public  String getCountryByLatLng(double latitude,double longitude){
        Geocoder geocoder;
        String address="";
        String country="";
        String knownName="";
        List<Address> addresses=new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            if (latitude==0.0){

            }else {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

               if (addresses.size()>0){
                   address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                   String city = addresses.get(0).getLocality();
                   String state = addresses.get(0).getAdminArea();
                     country = addresses.get(0).getCountryName();
                   String postalCode = addresses.get(0).getPostalCode();
                     knownName = addresses.get(0).getFeatureName();
               }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

if(country==null){
    return knownName;
}else{
    return country;
}
    }


    public void createInternetMsgToast() {
        Toast.makeText(context, "Please provide internet", Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }





//
//    //handle login response
//    private void handl(LoginResponseData data) {
//        if (data != null) {
//            if (data.getStatus()) {
//
//           getSession().saveUserData(data.getData());
//                if (data.getData().getEmailVerifiedAt() == null) {
//                    goToNextScreen( VerifyEmail.class);
//                } else {
//                    goToNextScreen( HomeActivity.class);
//
//                }
//                LoginResponseData loginResponseData = new LoginResponseData(data.getData(), "", false);
//                baseVm.subject.onNext(loginResponseData);
//                finishAffinity();
//            }
//
//        }
//
//    }

}

