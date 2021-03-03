package app.sixdegree.view.activity.blockedusers;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import app.sixdegree.ActivityBlockedUsersViewBinding;
import app.sixdegree.ActivityFriendsBinding;
import app.sixdegree.R;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.viewModel.BlockedUsersVm;
import app.sixdegree.viewModel.DataViewModel;

public class BlockedUserActivity extends BaseActivity {
    boolean isFollower = false;
    BlockedUsersVm listVm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBlockedUsersViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_blockedusers);
        setContentView(binding.getRoot());
        isFollower = getIntent().getBooleanExtra("follower", false);

        listVm = new BlockedUsersVm(isFollower, getSession());
        binding.setViewModel(listVm);
        binding.rvFriends.setLayoutManager(new LinearLayoutManager(this));

        //FriendsAdapter friendsAdapter = new FriendsAdapter(this, AppUtils.getScreenWidth(this) / 8);
        //  binding.rvFriends.setAdapter(friendsAdapter);

        binding.title.setOnClickListener(v -> {
            onBackPressed();
        });


    }




    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
