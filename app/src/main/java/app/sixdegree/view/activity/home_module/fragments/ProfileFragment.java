package app.sixdegree.view.activity.home_module.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.FragmentProfileBinding;
import app.sixdegree.network.responses.getmutualfriends.X1;
import app.sixdegree.network.responses.settings_mod.profile.UserProfile;
import app.sixdegree.network.responses.viewprofilefriends.MutualFriend;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.BackPressInterface;
import app.sixdegree.view.activity.ActivityExloreCategory;
import app.sixdegree.view.activity.follower_module.FriendsActivity;
import app.sixdegree.view.activity.trailsmodule.AddNewTrip;
import app.sixdegree.view.activity.trailsmodule.PlanNextTripActivity;
import app.sixdegree.view.activity.trailsmodule.previoustrips.PreviousTrips;
import app.sixdegree.view.settings_module.EditProfile;
import app.sixdegree.view.settings_module.SettingsActivity;
import app.sixdegree.viewModel.ProfileFragmentVm;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileFragment extends Fragment implements BackPressInterface {
    ProfileFragmentVm vm;

    String TRAIL_DATA = "";
    String FRIEND_TRAIL_DATA = "";
    String friend_id="";
    String followStatus="";
    String friendStatus="";
    String nameforfriends="";
    UserProfile userProfile;
    Boolean istripexists=false;
    AppSession appSession;
    Boolean isLatestTrailNotExists=false;
    List<MutualFriend>mutualFriendList=new ArrayList<>(  ) ;

    public ProfileFragment() {
    }

    public ProfileFragment( String friend_id , String followStatus,String friendStatus) {
        this.friend_id = friend_id;
        this.followStatus = followStatus;
        this.friendStatus = friendStatus;
//
//        if (!friend_id.isEmpty()){
//            vm.setFriend_id(friend_id);
//        }else {
//            vm.setFriend_id("");
//        }

    }

/*    public static Fragment LoginFragmentInstance() {
        Log.e("Profile Fragment", "LoginFragmentInstance: ");
        Fragment fragment = new ProfileFragment();
        return fragment;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appSession=new AppSession( getActivity());
        vm = new ProfileFragmentVm(appSession,friend_id);


        FragmentProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setViewModel(vm);
        View v = binding.getRoot();
        ImageView settings = v.findViewById(R.id.settings);
        ImageView iv_message = v.findViewById(R.id.ivmessage);
        ImageView addtrip = v.findViewById(R.id.addtrip);
        ImageView ivEdit = v.findViewById(R.id.ivEdit);
        ImageView add = v.findViewById(R.id.add);
        TextView following = v.findViewById(R.id.following);
        SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.swipe);
        TextView follower = v.findViewById(R.id.follower);
        TextView tv_addfriend = v.findViewById(R.id.tv_addfriend);
        TextView tv_follow = v.findViewById(R.id.tv_follow);


        Log.e( "follow staus intent-","--"+followStatus );



        if(friend_id.equals( "" )){

            settings.setVisibility( View.VISIBLE );
            addtrip.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.VISIBLE);
            iv_message.setVisibility(View.GONE);
            add.setVisibility( View.GONE );
            tv_addfriend.setVisibility( View.GONE );
            tv_follow.setVisibility( View.GONE );
            binding.floatingActionButton.setVisibility(  View.VISIBLE);
            settings.setOnClickListener(vv -> {


                Intent iia = new Intent(getActivity(), SettingsActivity.class);
                startActivity(iia);
            });


            binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        binding.floatingActionButton.hide();
                    } else {
                        binding.floatingActionButton.show();
                    }
                }
            });
        }else{
            settings.setVisibility( View.GONE );
            addtrip.setVisibility(View.GONE);
            binding.floatingActionButton.setVisibility(  View.GONE);
            ivEdit.setVisibility(View.GONE);

            //iv_message.setVisibility(View.VISIBLE);



                    if(followStatus.equals( "Followed" )){
                         tv_follow.setVisibility( View.GONE );
                    }else {
                        tv_follow.setVisibility( View.VISIBLE );
                    }


            Log.e( "friendStatus  intent-","--"+friendStatus );
            if(friendStatus.equals( "no_relation" )){
                add.setVisibility( View.GONE );
                tv_addfriend.setVisibility( View.VISIBLE );
                tv_follow.setVisibility( View.VISIBLE );
                binding.ivRequestpendig.setVisibility( View.GONE );
                iv_message.setVisibility(View.GONE);
                addtrip.setVisibility(View.GONE);
                binding.floatingActionButton.setVisibility(  View.GONE);
                follower.setVisibility(View.GONE);
                following.setVisibility(View.GONE);
                if(followStatus.equals( "Followed" )){
                    tv_follow.setVisibility( View.GONE );
                }else {
                    tv_follow.setVisibility( View.VISIBLE );
                }
            }else if(friendStatus.equals( "pending_request")){
              //add.setVisibility( View.VISIBLE );
                tv_addfriend.setVisibility( View.GONE );
                iv_message.setVisibility(View.GONE);
                follower.setVisibility(View.GONE);
                following.setVisibility(View.GONE);
                addtrip.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                binding.ivRequestpendig.setVisibility( View.VISIBLE );
                if(followStatus.equals( "Followed" )){
                    tv_follow.setVisibility( View.GONE );
                }else {
                    tv_follow.setVisibility( View.VISIBLE );
                }
            }else if(friendStatus.equals( "confirm_request")){
                add.setVisibility( View.GONE );
                iv_message.setVisibility(View.GONE);
                binding.ivRequestpendig.setVisibility( View.VISIBLE );
                tv_addfriend.setVisibility( View.GONE );
                tv_follow.setVisibility( View.VISIBLE );
                follower.setVisibility(View.GONE);
                following.setVisibility(View.GONE);
                if(followStatus.equals( "Followed" )){
                    tv_follow.setVisibility( View.GONE );
                }else {
                    tv_follow.setVisibility( View.VISIBLE );
                }
            }else{ iv_message.setVisibility(View.VISIBLE);
                add.setVisibility( View.GONE );
                binding.ivRequestpendig.setVisibility(View.GONE);
                tv_addfriend.setVisibility( View.GONE );
                tv_follow.setVisibility( View.GONE );

            }
        }



        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (friend_id.equals("")){
                    vm.getUserProfile();
                }else {
                    vm.getFriendsProfile();

                    // getMutualFriendslist();
                }
            }
        } );


        //set swipe refresh false
        vm.setSwipeRefreshFalse.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(swiperefresh -> {
                     if(swiperefresh){
                         swipeRefreshLayout.setRefreshing(false);
                     }else {
                         swipeRefreshLayout.setRefreshing(true);
                     }

                }, throwable -> {

                });

         vm.followstatusapi.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(swiperefresh -> {
                    vm.setFollow_status_fromapi( swiperefresh );
                    Log.e( "follow staus api-","--"+vm.getFollow_status_fromapi() );

/*if(vm.getFollow_status_fromapi().equals( "Request pending" )){
binding.ivRequestpendig.setVisibility( View.GONE );
binding.add.setVisibility( View.VISIBLE );
}else if(vm.getFollow_status_fromapi().equals( "Not Followed" )){
    binding.ivRequestpendig.setVisibility( View.GONE );
    binding.add.setVisibility( View.VISIBLE );
}else {
    binding.ivRequestpendig.setVisibility( View.GONE );
    binding.add.setVisibility( View.GONE );
}*/

                }, throwable -> {

                });

        ivEdit.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick( View v ) {
        startActivity(new Intent(getContext(), EditProfile.class));
    }
} );


        addtrip.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick( View v ) {
        Intent intent = new Intent(v.getContext(), AddNewTrip.class);
        intent.putExtra("isEdit", false);
        intent.putExtra("id", "");
        v.getContext().startActivity(intent);    }
});


binding.floatingActionButton.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick( View v ) {
        Intent intent = new Intent(v.getContext(), AddNewTrip.class);
        intent.putExtra("isEdit", false);
        intent.putExtra("id", "");
        v.getContext().startActivity(intent);    }
});

        AppSession session= new AppSession(getActivity());

        if (!session.getData().getInterestAdded()) {
                 startActivity(new Intent(getContext(), ActivityExloreCategory.class));

            }

        if (friend_id!=null){
                Log.e("friendid==","--"+friend_id);
            }




            //get trip exists from behaviour
        vm.istripexists.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(trail -> {
                   if(trail){
                         istripexists=true;

                   }else {
                         istripexists=false;

                   }

//                    loadFragment(new TripTrailsFragment(TRAIL_DATA,friend_id,mutualFriendList,followStatus,
//                            istripexists,isLatestTrailNotExists,nameforfriends,friendStatus));
                }, throwable -> {
                    Log.e("User data ", throwable.getMessage());
                });
   //is latest trail exists from behaviour
        vm.isLatestTrailNotExists.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(trail -> {
                   if(trail){
                       isLatestTrailNotExists=true;

                   }else {
                       isLatestTrailNotExists=false;

                   }
//
//                    loadFragment(new TripTrailsFragment(TRAIL_DATA,friend_id,mutualFriendList,followStatus,
//                            istripexists,isLatestTrailNotExists,nameforfriends,friendStatus));
                }, throwable -> {
                    Log.e("User data ", throwable.getMessage());
                });
        //observe changes in view model
        vm.mutualfriendlist.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(trail -> {
                    mutualFriendList=trail;

                    Log.e("mutualfriendlist", "in p f  "+mutualFriendList.size());
//                    loadFragment(new TripTrailsFragment(TRAIL_DATA,friend_id,mutualFriendList,followStatus,istripexists,
//                            isLatestTrailNotExists,nameforfriends,friendStatus));
                }, throwable -> {
                    Log.e("User data ", throwable.getMessage());
                });
  //observe changes in view model
        vm.behaviorSubjectTrail.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(trail -> {
                    this.TRAIL_DATA = trail;
                    userProfile = new Gson().fromJson(trail, UserProfile.class);
                    nameforfriends= String.valueOf(userProfile.getData().getId());
                    loadFragment(new TripTrailsFragment(TRAIL_DATA,friend_id,mutualFriendList,followStatus,
                            istripexists,isLatestTrailNotExists,nameforfriends,friendStatus));
/*if(nameforfriends.equals( "" )){
    nameforfriends=String.valueOf(appSession.getData().getId());
}*/
//                    loadFragment(new TripTrailsFragment(TRAIL_DATA,friend_id,mutualFriendList,
//                            followStatus,istripexists,isLatestTrailNotExists,nameforfriends,friendStatus));
                }, throwable -> {
                    Log.e("User data ", throwable.getMessage());
                });

        //get name exists from behaviour
      /*  vm.nameforfriends.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                    nameforfriends=id;

                    loadFragment(new TripTrailsFragment(TRAIL_DATA,friend_id,mutualFriendList,followStatus,
                            istripexists,isLatestTrailNotExists,nameforfriends,friendStatus));
                }, throwable -> {
                    Log.e("User data", throwable.getMessage());
                });*/

        v.findViewById(R.id.trip_trails).setOnClickListener(vv -> {
            if (TRAIL_DATA.isEmpty()) {
                return;
            }
            loadFragment(new TripTrailsFragment(TRAIL_DATA,friend_id,mutualFriendList,followStatus,
                    istripexists,isLatestTrailNotExists,nameforfriends,friendStatus));
            v.findViewById(R.id.trails_arrow).setVisibility(View.VISIBLE);
            v.findViewById(R.id.arrow_stat).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.trip_trails).setBackgroundColor(getResources().getColor(R.color.white_regular));
            v.findViewById(R.id.statistics_).setBackgroundColor(getResources().getColor(R.color.blue_light));
        });

        v.findViewById(R.id.statistics_).setOnClickListener(vv -> {
            if (TRAIL_DATA.isEmpty()) {
                return;
            }
            loadFragment(new ProfileStatisticsFragment(TRAIL_DATA));
            v.findViewById(R.id.trails_arrow).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.arrow_stat).setVisibility(View.VISIBLE);
            v.findViewById(R.id.statistics_).setBackgroundColor(getResources().getColor(R.color.white_regular));
            v.findViewById(R.id.trip_trails).setBackgroundColor(getResources().getColor(R.color.blue_light));

        });


        binding.following.setOnClickListener(vv -> {
            Intent intent = new Intent(getActivity(), FriendsActivity.class);
            intent.putExtra("follower", false);
            intent.putExtra("nameforfriends", nameforfriends);
            startActivityForResult( new Intent(intent),12);
        });


        binding.follower.setOnClickListener(vv -> {
             Intent intent = new Intent(getActivity(), FriendsActivity.class);
            intent.putExtra("follower", true);
            intent.putExtra("nameforfriends", nameforfriends);
            startActivityForResult(new Intent(intent),11);
        });

        return binding.getRoot();
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame_container__, fragment);
       // transaction.addToBackStack(null);
        transaction.commit();
    }
/*
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vm.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResume() {
        super.onResume();

      //  vm.getUserProfile();
    }


}
