package app.sixdegree.view.activity.loginModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivityLoginBinding;
import app.sixdegree.model.login.LoginResponseData;
 import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.home_module.HomeActivity;
import app.sixdegree.viewModel.BaseVm;
import app.sixdegree.viewModel.LoginViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    private boolean isPassVisible = false;
    private LoginViewModel loginViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    String token="";

    public static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();

        ActivityLoginBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_);


      SharedPreferences sharedpreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
      token=sharedpreferences.getString( "token" ,"");
        loginViewModel = new LoginViewModel(token);
        activityMainBinding.setViewModel(loginViewModel);
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.executePendingBindings();

        EditText pass = (EditText) findViewById(R.id.password);
//init gooogle login
        initialiseGlogin();

        findViewById(R.id.btn_google).setOnClickListener(v -> {
            handleGoogleLogin();
        });

//facebook code

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
                                        Toast.makeText( context, "in---"+response , Toast.LENGTH_SHORT ).show();
                                        if (object.has("id")){

                                            try {
                                                String name=object.getString("name");


                                                String fid = object.getString("id");
                                                String email= object.getString("email");

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

                                                Toast.makeText( context, "e---"+e , Toast.LENGTH_SHORT ).show();
                                            }


                                        }

                                        else {
                                            Log.e("FBLOGIN_FAILD", object.toString());


                                            Toast.makeText( context, "loginResult"+loginResult , Toast.LENGTH_SHORT ).show();
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


        //facebook code ends

        findViewById(R.id.btn_fb).setOnClickListener(v -> {
           // handleFBLogin();



            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));








        });
        //handling password visibility
        findViewById(R.id.password).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (findViewById(R.id.password).getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (!isPassVisible) {
                            pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.lock), null,
                                    ContextCompat.getDrawable(context, R.drawable.show_p),
                                    null);
                            isPassVisible = true;
                        } else {
                            pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.lock), null,
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
        findViewById(R.id.btn_forgot).setOnClickListener(v -> {
            goToNextScreen(ForgotPassword.class);
        });


        //observe changes in view model for login response
        Disposable subscribe = loginViewModel.subject.observeOn(Schedulers.io())
                .subscribe(this::handl, throwable -> {
                    Log.e("User data ", throwable.getMessage());
                });
        addToDispose(subscribe);
    }

    //handle login response
    private void handl( LoginResponseData data) {
        if (data != null) {
            if (data.getStatus()) {
                getSession().saveUserData(data.getData());
                if (data.getData().getEmailVerifiedAt() == null) {
                    goToNextScreen(VerifyEmail.class);
                } else {
                    goToNextScreen(HomeActivity.class);

                }
               //LoginResponseData loginResponseData = new LoginResponseData(data.getData(), "", false);
              //loginViewModel.subject.onNext(loginResponseData);
                finishAffinity();
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //handling button click toast
    @BindingAdapter({"toastMessage"})
    public static void abc(View view, String message) {
        if (message != null) {
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }
        //goToNextScreen(LocationPermissionActivity.class);
    }


    public void handleBack(View view) {
        onBackPressed();
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

    public void loginAndPostToWall() {

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
