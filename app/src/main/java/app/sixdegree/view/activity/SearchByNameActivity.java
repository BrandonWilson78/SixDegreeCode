package app.sixdegree.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivitySearchByNameBinding;
import app.sixdegree.databinding.FragmentFriendsViewBinding;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.home_module.HomeActivity;
import app.sixdegree.view.activity.home_module.adapter.SearchAdapter;
import app.sixdegree.view.activity.home_module.adapter.SearchByNameAdapter;
import app.sixdegree.viewModel.SearchFragmentVm;
import app.sixdegree.viewModel.SearchUsersByNameVm;
import app.sixdegree.viewModel.ViewFriendsVm;

public class SearchByNameActivity extends BaseActivity {
    ActivitySearchByNameBinding binding;
    SearchUsersByNameVm searchUsersByNameVm;
    AppSession appSession;
    Handler handler;
    Runnable runnn;
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    // setContentView( R.layout.activity_search_by_name );

        binding= DataBindingUtil.setContentView( this, R.layout.activity_search_by_name  );



        appSession=new AppSession(this);
        searchUsersByNameVm=new SearchUsersByNameVm(appSession);

        binding.setViewModel( searchUsersByNameVm );


        binding.searchrv.setLayoutManager(new LinearLayoutManager( this) );
        SearchByNameAdapter searchAdapter=new SearchByNameAdapter(appSession.getToken());
        binding.searchrv.setAdapter( searchAdapter );
        handler = new Handler();
        runnn = new Runnable() {
            public void run() {
                if (binding.searchEdit.getText().toString().trim().length()>0){

                        searchUsersByNameVm.search(binding.searchEdit );



                }



            }
        };



        binding.searchEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Log.e("size ed ","--"+binding.searchEdit.getText().toString().trim().length());
                Log.e("size list ","--"+searchAdapter.data.size());

                if(binding.searchEdit.getText().toString().trim().length()==0){
                  searchUsersByNameVm.getSearchAdapter().setData( new ArrayList<>(  ) );
                }

                handler.removeCallbacks(runnn);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged( Editable arg0) {
                handler.postDelayed(runnn, 500);
            }
        });

    }

    //handling button click toast
    @BindingAdapter({"errorMsg"})
    public static void abc( View view, String message) {
        if (message != null) {
            showSnackBar((Activity) view.getContext(), view.getId(), message);
        }
        //goToNextScreen(LocationPermissionActivity.class);
    }


    public void loadFragment( Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
    public  void gotToHomeActvity(String from,String id,String status,String friendstatus){
        Intent intent=new Intent( this,HomeActivity.class );
        intent.putExtra( "from",from );
        intent.putExtra( "id", id) ;
        intent.putExtra( "status", status) ;
        intent.putExtra( "friendstatus", friendstatus) ;
        startActivity(intent);
    }
}
