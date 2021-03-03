package app.sixdegree.view.activity.loginModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.model.login.LoginResponseData;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.Country;
import app.sixdegree.model.roomdb.DatabaseClient;
import app.sixdegree.model.roomdb.dbmodel.DbCountriesModel;
import app.sixdegree.network.responses.bannersres.BannerRes;
import app.sixdegree.utils.CustomSliderView;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.LocationPermissionActivity;
import app.sixdegree.view.activity.home_module.HomeActivity;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginSignup extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    public static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
String token="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_login_signup);
        getTasks();
        PagerIndicator customIndicator = findViewById(R.id.custom_indicator);
        mDemoSlider = findViewById(R.id.slider);
        if (!getIntent().hasExtra("data")) {
            Log.e("extras", "***expected***");
            return;
        }
        SharedPreferences sharedpreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        token=sharedpreferences.getString( "token" ,"");
        BannerRes data = new Gson().fromJson(getIntent().getStringExtra("data"), BannerRes.class);

        HashMap<String, String> file_maps = new HashMap<String, String>();

//init gooogle login
        initialiseGlogin();
        for (int i = 0; i < data.getData().size(); i++) {
            file_maps.put(String.valueOf(i), data.getData().get(i).getImage());
        }/*
        file_maps.put("1", R.drawable.banner_one);
        file_maps.put("2", R.drawable.banner_two);
        file_maps.put("3", R.drawable.banner_three);
        file_maps.put("4", R.drawable.banner_4);*/


        for (String name : file_maps.keySet()) {
            CustomSliderView textSliderView = new CustomSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setCustomIndicator(customIndicator);
        mDemoSlider.addOnPageChangeListener(this);




        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
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
                                                map.put( "device_token",token );

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
                    public void onError( FacebookException exception) {
                        // App code
                    }
                });



        //google login
        findViewById(R.id.g_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handleGoogleLogin();
            }
        });
        //facebook login
        findViewById(R.id.f_login).setOnClickListener(v -> {

            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        });
        //observe changes in view model for login response
        Disposable subscribe = baseVm.subject.observeOn( Schedulers.io())
                .subscribe(this::handl, throwable -> {
               //     Log.e("User data ", throwable.getMessage());
                });
        addToDispose(subscribe);


    }


    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    protected void onPause() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onPause();
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }


    public void goToSignup(View view) {
        goToNextScreen(LocationPermissionActivity.class);
    }

    public void goToLogin(View view) {
        goToNextScreen(LoginActivity.class);
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Country>> {

            @Override
            protected List<Country> doInBackground(Void... voids) {

                List<Country> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .CountriesDao()
                        .getAll();

                if (taskList.size() <= 0) {
                    String json = "";
                    try {
                        InputStream is = getAssets().open("countries.json");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        json = new String(buffer, StandardCharsets.UTF_8);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                    CountriesDao countriesDao = DatabaseClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .CountriesDao();

                    Type collectionType = new TypeToken<List<DbCountriesModel>>() {
                    }.getType();
                    List<DbCountriesModel> lcs = new Gson()
                            .fromJson(json, collectionType);


                    for (DbCountriesModel m : lcs) {
                        Country country = new Country();
                        country.setFlag(m.getFlag());
                        country.setAlpha2Code(m.getAlpha2Code());
                        country.setName(m.getName().toLowerCase());
                        countriesDao.insertAll(country);
                    }
                    //countriesDao.insertAll();
                    Log.e("size m", lcs.size() + "==");

                }

                return taskList;
            }

            @Override
            protected void onPostExecute(List<Country> tasks) {
                super.onPostExecute(tasks);

                Log.e("size", tasks.size() + "==");
                // TasksAdapter adapter = new TasksAdapter(MainActivity.this, tasks);
                // recyclerView.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


    //handle login response
    private void handl( LoginResponseData data) {
        if (data != null) {
            if (data.getStatus()) {
                getSession().saveUserData(data.getData());
                if (data.getData().getEmailVerifiedAt() == null) {
                    goToNextScreen(VerifyEmail.class);
                } else {
                    goToNextScreen( HomeActivity.class);

                }
                LoginResponseData loginResponseData = new LoginResponseData(data.getData(), "", false);
                baseVm.subject.onNext(loginResponseData);
                finishAffinity();
            }

        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }







    //handle google result

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult( ApiException.class);

            // Signed in successfully, show authenticated UI.
            Log.wtf("google result", "signInResult" + account.getDisplayName() + "==" + account.getEmail());

//social login api{}



            HashMap<String,String>map=new HashMap<>(  );
            map.put( "name",account.getGivenName() );
            map.put( "login_type","google" );
            map.put( "email",account.getEmail() );
            map.put( "social_id",account.getId() );
            map.put( "device_token",token );

            baseVm.socialLogin( map );

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.wtf("google result", "signInResult:failed code=" + e.getStatusCode());


        }
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


}
