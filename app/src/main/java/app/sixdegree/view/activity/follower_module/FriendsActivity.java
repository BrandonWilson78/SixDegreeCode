package app.sixdegree.view.activity.follower_module;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import app.sixdegree.ActivityFriendsBinding;
import app.sixdegree.R;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.home_module.HomeActivity;
import app.sixdegree.view.activity.home_module.fragments.SearchFragment;
import app.sixdegree.viewModel.DataViewModel;

public class FriendsActivity extends BaseActivity {
    boolean isFollower = false;
    DataViewModel listVm;
String nameforfriends="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFriendsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_follower);
       setContentView(binding.getRoot());
        isFollower = getIntent().getBooleanExtra("follower", false);
        nameforfriends = getIntent().getStringExtra("nameforfriends");

        View view=binding.getRoot();
        ImageView add=view.findViewById( R.id.iv_add );

//        add.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick( View v ) {
//
//                SearchFragment fragment = new SearchFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_container, fragment);
//                transaction.commit();
//            }
//        } );

        listVm = new DataViewModel(isFollower, getSession(),nameforfriends);
        binding.setViewModel(listVm);
        binding.rvFriends.setLayoutManager(new LinearLayoutManager(this));

        //FriendsAdapter friendsAdapter = new FriendsAdapter(this, AppUtils.getScreenWidth(this) / 8);
        //  binding.rvFriends.setAdapter(friendsAdapter);
        if (isFollower) {
            listVm.setToolbarTitle(getString(R.string.followers));
            listVm.getFollower();

        } else {
            listVm.setToolbarTitle(getString(R.string.following));
            listVm.getfollowings();
        }


        binding.title.setOnClickListener(v -> {
            onBackPressed();
        });


    }




    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("count", listVm.getAdapter().getItemCount());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
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
