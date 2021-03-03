package app.sixdegree.view.activity.home_module.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.FragmentTripTrailsBinding;
import app.sixdegree.network.responses.gettripcategories.Data;
import app.sixdegree.network.responses.gettripcategories.GetTripCategories;
import app.sixdegree.network.responses.settings_mod.profile.UserProfile;
import app.sixdegree.network.responses.viewprofilefriends.MutualFriend;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.EndlessRecyclerOnScrollListener;
import app.sixdegree.utils.EndlessRecyclerViewScrollListener;
 import app.sixdegree.view.activity.home_module.adapter.MutualFriendsAdapter;
import app.sixdegree.view.activity.savedtrips.BookmarkedTrips;
import app.sixdegree.view.activity.taggedtrips.TaggedTrips;
import app.sixdegree.view.activity.trailsmodule.TrailDetailsActivity;
import app.sixdegree.view.activity.trailsmodule.previoustrips.PreviousTrips;
import app.sixdegree.viewModel.TripTrailsVm;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TripTrailsFragment extends Fragment {
    UserProfile userProfile;
    PowerMenu powerMenu;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String friend_id="";
    String followStatus="";
      String friendStatus="";
    int pageCount=1;
    String   nameforfriends="";
    Boolean isTripExists=false;
    Boolean isLatestTrailNotExists=false;
    private boolean isLoading = false;
    private boolean isLastPage = false;
  List<MutualFriend>mutualFriendList=new ArrayList<>(  );
    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    List<String> list = new ArrayList<>();
     List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    private EndlessRecyclerViewScrollListener scrollListener;
    private TripTrailsVm tripTrailsVm;
    GridLayoutManager mLayoutManager;
    List<Data> tripCatList = new ArrayList<>();
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            String textSelected = item.getTitle();
            tripTrailsVm.setTripcat(textSelected);
           /* if (textSelected.equals("Latest") || textSelected.equals("Oldest")) {

                if(textSelected.equals( "Latest" )){
                    tripTrailsVm.setOrderBy("latest");
                }else {
                    tripTrailsVm.setOrderBy("oldest");
                }

            } else if (textSelected.equals("Most") || textSelected.equals("Least")) {


                if(textSelected.equals( "Most" )){
                    tripTrailsVm.setPopular("most");
                }else {
                    tripTrailsVm.setPopular("least");
                }
            }else if(textSelected.equals( "Friends" ) || textSelected.equals( "Friends of Friends" ) ||  textSelected.equals( "Everyone" )){
                tripTrailsVm.setRecommendedBy(textSelected);

                if(textSelected.equals( "Friends" )){
                    tripTrailsVm.setSearch_trips_of("1");
                }else if(textSelected.equals( "Friends of Friends" )){
                    tripTrailsVm.setSearch_trips_of("2");
                }else if(textSelected.equals( "Everyone" )){
                    tripTrailsVm.setSearch_trips_of("3");
                }else {
                    tripTrailsVm.setSearch_trips_of("");
                }*/

            powerMenu.dismiss();
         }
    };

    public TripTrailsFragment( String s, String friend_id, List<MutualFriend> mutualFriendsList,String followStatus,Boolean isTripExists,
                               Boolean isLatestTrailNotExists,String nameforfriends,String friendStatus) {
        if (!s.isEmpty()) {
            userProfile = new Gson().fromJson(s, UserProfile.class);
        }

        if(mutualFriendsList!=null){
            this.mutualFriendList=mutualFriendsList;
        }

        this.friend_id=friend_id;
        this.followStatus=followStatus;
        this.friendStatus=friendStatus;
        this.isTripExists=isTripExists;
        this.nameforfriends=nameforfriends;
        this.isLatestTrailNotExists=isLatestTrailNotExists;


        Log.e( "TripTrailsFragment" ,"--");
    }

    @BindingAdapter({"errorMsg"})
    public static void click(Button view, String errorMsg) {
        if (errorMsg != null && !errorMsg.isEmpty())
            Toast.makeText(view.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    public TripTrailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTripTrailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_trails, container, false);


        AppSession appSession=new AppSession( getContext() );

        if(nameforfriends.equals( "" )){
            tripTrailsVm = new TripTrailsVm(appSession,userProfile,friend_id,mutualFriendList);
        }else {
            tripTrailsVm = new TripTrailsVm(appSession,userProfile,nameforfriends,mutualFriendList);
        }
        tripTrailsVm.setSessionInstance(new AppSession(getActivity()));
        binding.setViewModel(tripTrailsVm);
        tripTrailsVm.setPageCount( String.valueOf( pageCount ) );
       tripTrailsVm.getExloreList(tripTrailsVm.getPageCount());
        View view = binding.getRoot();
        binding.executePendingBindings();

        mLayoutManager = new GridLayoutManager(getActivity(),2);
        binding.exploreRv.setLayoutManager(mLayoutManager);

      //  binding.exploreRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        RelativeLayout rl_mutualfriends = view.findViewById(R.id.rl_mutualfriends);
        RelativeLayout emptytrail = view.findViewById(R.id.emptytrail);
        RelativeLayout trail = view.findViewById(R.id.trail);
        LinearLayout ll_explore = view.findViewById(R.id.ll_explore);
        TextView tv_dummytext = view.findViewById(R.id.tv_dummytext);
        TextView tv_saved = view.findViewById(R.id.tv_saved);
        TextView tv_previoustrips = view.findViewById(R.id.tv_previoustrips);
        TextView tv_tagged_trips = view.findViewById(R.id.tv_tagged_trips);
        TextView add_trail = view.findViewById(R.id.add_trail);
        RelativeLayout rl_elp = view.findViewById(R.id.rl_elp);
        ImageView iv_gray_plus = view.findViewById(R.id.iv_gray_plus);




        binding.tvSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BookmarkedTrips.class));
            }
        });
        //get trail cat list
        tripTrailsVm.tripCat.subscribeOn( AndroidSchedulers.mainThread()).doOnNext( data -> {
            tripCatList = data;
            for (int i = 0; i < data.size(); i++) {
                list.add(data.get(i).getName());

            }

        }).subscribe();

binding.edTripname.setSelection( binding.edTripname.length() );
        binding.llTripcat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                show(list, binding.llTripcat);

            }
        });

     //   ProgressBar bar = view.findViewById(R.id.bar);

//        //pagination
//        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//               // loadNextDataFromApi(page);
////bar.setVisibility( View.VISIBLE );
//                System.out.println("pageno----"+page+"----"+totalItemsCount);
//                tripTrailsVm.getExloreList(String.valueOf( page) );
//            }
//
//            @Override
//            public void onScrolled( RecyclerView view, int dx, int dy ) {
//                super.onScrolled( view, dx, dy );
//
//            }
//
//
//        };

        binding.exploreRv.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if(mLayoutManager.findLastVisibleItemPosition() == tripTrailsVm.data.size() -1){
                    //bottom of list!
                 //   loadMoreData();
                    Log.d("End","Sucess");
                }


            }
        });



         //pagination ends

Log.e( "friend id in in trtrfr","--"+friend_id );
Log.e( "friendStatus","--"+friendStatus );
Log.e( "nameforfriends","--"+nameforfriends );


        if (nameforfriends.equals(String.valueOf(appSession.getData().getId()))){
            tv_saved.setVisibility(View.VISIBLE);
        }else {
            tv_saved.setVisibility(View.GONE);
        }

        if(isLatestTrailNotExists){
            emptytrail.setVisibility( View.VISIBLE );
            trail.setVisibility( View.GONE );


            if(friend_id.equals( "" )){

                 iv_gray_plus.setVisibility(View.VISIBLE);
                add_trail.setText( "Add Pitstop" );
                rl_elp.setVisibility(View.VISIBLE);
                ll_explore.setVisibility( View.VISIBLE );

            }else {
                 iv_gray_plus.setVisibility( View.GONE );
                add_trail.setText( "No Latest Pitstop." );
                rl_elp.setVisibility(View.GONE);
                ll_explore.setVisibility( View.GONE );
            }


        }else {
            emptytrail.setVisibility( View.GONE );
            trail.setVisibility( View.VISIBLE );
        }


emptytrail.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick( View v ) {


        if(friend_id.equals("")){
            Intent intent = new Intent(v.getContext(), PreviousTrips.class);
            intent.putExtra("isEdit", false);
            intent.putExtra("id", nameforfriends);
            v.getContext().startActivity(intent);
        }


    }
} );




        //goto previous trips
        tv_previoustrips.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                Intent intent = new Intent(v.getContext(), PreviousTrips.class);
                intent.putExtra("isEdit", false);
                intent.putExtra("id", nameforfriends);
                v.getContext().startActivity(intent);
          /*      startActivity( new Intent( getContext(), PreviousTrips.class ) );*/
            }
        } );
  //goto tagged trips
        tv_tagged_trips.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                Intent intent=new Intent( getContext(),TaggedTrips.class );

                if(nameforfriends.equals( "" )){
                    intent.putExtra( "nameforfriends",friend_id );
                }else {
                    intent.putExtra( "nameforfriends",nameforfriends );
                }

                startActivity( intent );
            }
        } );

        if(mutualFriendList.size()==0){
            tripTrailsVm.setDummytext( "No Mutual Friends found." );
            tripTrailsVm.setVisible( true );
            tv_dummytext.setVisibility( View.VISIBLE );
            rl_mutualfriends.setVisibility( View.GONE );
        }else {
            tripTrailsVm.setVisible( false );
            tv_dummytext.setVisibility( View.GONE );
            rl_mutualfriends.setVisibility( View.VISIBLE );
        }



        if(friend_id.equals("")){
            rl_mutualfriends.setVisibility(View.GONE);
            tv_previoustrips.setVisibility( View.VISIBLE );

 }else {

            if(friendStatus.equals( "no_relation" )){
                ll_explore.setVisibility( View.GONE );
                rl_mutualfriends.setVisibility(View.VISIBLE);
                rl_elp.setVisibility(View.GONE);
                tv_previoustrips.setVisibility( View.GONE );
                tv_tagged_trips.setVisibility( View.GONE );

            }

           else if(friendStatus.equals( "friends" ) || friendStatus.isEmpty()){
                 rl_elp.setVisibility(View.GONE);
                ll_explore.setVisibility( View.GONE );
                rl_mutualfriends.setVisibility( View.GONE );
            //   tv_previoustrips.setVisibility( View.VISIBLE );
                tv_tagged_trips.setVisibility( View.VISIBLE );

                if(isTripExists){
                    tv_previoustrips.setVisibility( View.VISIBLE );
                }else {
                    tv_previoustrips.setVisibility( View.GONE );
                }
            }  else {
                ll_explore.setVisibility( View.GONE );
                rl_mutualfriends.setVisibility(View.VISIBLE);
                rl_elp.setVisibility(View.GONE);
                tv_tagged_trips.setVisibility( View.GONE );
                tv_previoustrips.setVisibility( View.GONE );

            }


        }


//


        binding.mutualFriendsrv.setLayoutManager( new LinearLayoutManager( getContext() ) );
        MutualFriendsAdapter mutualFriendsAdapter=new MutualFriendsAdapter(mutualFriendList);
        binding.mutualFriendsrv.setAdapter( mutualFriendsAdapter );

        binding.trail.setOnClickListener(v -> {
            if(userProfile!=null){
                Pair statusAnim = Pair.create(binding.trailName, "name");
                Pair driverBundleAnim = Pair.create(binding.trail, "pic");
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), statusAnim, driverBundleAnim);
                Intent intent = new Intent(getActivity(), TrailDetailsActivity.class);
                intent.putExtra("id", String.valueOf(userProfile.getData().getLatestTrip().getId()));
                intent.putExtra("tagged", "");
                intent.putExtra("user_id", String.valueOf(userProfile.getData().getLatestTrip().getUserId()));
                startActivity(intent, transitionActivityOptions.toBundle());
            }



        });


        binding.startDate.setOnClickListener(v -> setDate(true, tripTrailsVm));
        binding.endDate.setOnClickListener(v -> setDate(false, tripTrailsVm));

        binding.orderBy.setOnClickListener(v -> {
            List<String> list = new ArrayList();
            list.add("Latest");
            list.add("Oldest");
            show(list, binding.orderBy);
        });
   binding.recommendedby.setOnClickListener(v -> {
            List<String> list = new ArrayList();
            list.add("Friends");
            list.add("Friends of Friends");
            list.add("Everyone");
            show(list, binding.recommendedby);
        });

        binding.popular.setOnClickListener(v -> {
            List<String> list = new ArrayList();
            list.add("Most");
            list.add("Least");
            show(list, binding.popular);
        });

        binding.locationLl.setOnClickListener(vv -> {
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getActivity());
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });


        return binding.getRoot();
    }
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();


            if (!isLoading && !isLastPage) {
                /* if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {*/
                if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0) {
                    pageCount++;
                    tripTrailsVm.setPageCount(String.valueOf(pageCount));
                 //   tripTrailsVm.getExloreList(tripTrailsVm.getPageCount());
                       /* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Log.e("handler","--");
                            }
                        }, 10000);*/


                    Log.e("end of list","--");

                }

            }
        }
    };




    public void show(List<String> list, View view) {


        List<PowerMenuItem> l1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            l1.add(new PowerMenuItem(list.get(i), false));
        }

        powerMenu = new PowerMenu.Builder(getActivity())
                .addItemList(l1)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
        powerMenu.showAtCenter(view);

    }

    private void setDate(boolean isStart, TripTrailsVm vm) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String SELECTED_DATE = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "";
                    if (isStart) {
                        vm.setStartDate(SELECTED_DATE);

                    } else {
                        vm.setEndDate(SELECTED_DATE);

                    }

                }, mYear, mMonth, mDay);
        if (isStart) {
             datePickerDialog.setMessage("Select start date");
        }  else {

            datePickerDialog.setMessage("Select end date");

        }
        //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("trails fragment", "Place: " + place.getName() + ", " + place.getAddress());
                tripTrailsVm.setLongitude(String.valueOf(place.getLatLng().longitude));
                tripTrailsVm.setLattitude(String.valueOf(place.getLatLng().latitude));
                tripTrailsVm.setLocationName(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                 Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("trails fragment", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
