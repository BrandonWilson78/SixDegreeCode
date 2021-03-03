package app.sixdegree.view.activity.home_module.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.FragmentSearchViewBinding;
import app.sixdegree.network.responses.sixdegreesearch.TripsofMatchedUser;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.BackPressInterface;
import app.sixdegree.view.activity.home_module.adapter.SearchAdapter;
import app.sixdegree.view.activity.home_module.adapter.SearchTripsAdapter;
import app.sixdegree.view.activity.home_module.adapter.TripOfSearchedUsersAdapter;
import app.sixdegree.viewModel.SearchFragmentVm;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static app.sixdegree.view.activity.BaseActivity.showSnackBar;

public class SearchFragment extends Fragment  {
    List<TripsofMatchedUser> data = new ArrayList<>();
    SearchFragmentVm searchFragmentVm;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    AppSession appSession;
    FragmentSearchViewBinding binding;
    GridLayoutManager mLayoutManager;
TripOfSearchedUsersAdapter searchTripsAdapter;
    PowerMenu powerMenu;
    List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


          binding= DataBindingUtil.bind( inflater.inflate( R.layout.fragment_search ,container,false) );



        appSession=new AppSession(getActivity());
searchFragmentVm=new SearchFragmentVm(appSession);

        binding.setViewModel( searchFragmentVm );


        binding.searchrv.setLayoutManager(new LinearLayoutManager( getActivity() ) );
        SearchAdapter searchAdapter=new SearchAdapter(appSession.getToken());
        binding.searchrv.setAdapter( searchAdapter );

        mLayoutManager = new GridLayoutManager(getActivity(),2);
        searchTripsAdapter=new TripOfSearchedUsersAdapter(  );
        binding.rvmatchedtrips.setLayoutManager(mLayoutManager);


        //open autocomplete on search edittext click
        binding.searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openPlaceAutoComplete();
            }
        });

//        //observe changes in view model
//        searchFragmentVm.tripsofmatcheduser.observeOn( Schedulers.io())
//                .subscribeOn( AndroidSchedulers.mainThread())
//                .subscribe(trail -> {
//                   data=trail;
//                    binding.tvd.setVisibility( View.VISIBLE );
//                    Log.e("mutualfriendlist", "in p f  "+data.size());
//
//                    searchTripsAdapter=new SearchTripsAdapter( getActivity(),data );
//
//                    binding.rvmatchedtrips.setAdapter( searchTripsAdapter );
//                    searchTripsAdapter.notifyDataSetChanged();
//
//                    if(data.size()==0){
//                        binding.tvDummytext.setVisibility( View.VISIBLE );
//                        binding.rvmatchedtrips.setVisibility( View.GONE );
//                     }else {
//                        binding.tvDummytext.setVisibility( View.GONE );
//                        binding.rvmatchedtrips.setVisibility( View.VISIBLE );
//                        binding.tvd.setVisibility( View.VISIBLE );
//                    }
//
//                }, throwable -> {
//                    Log.e("User data ", throwable.getMessage());
//                });


         binding.more.setOnClickListener(v -> {
            List<String> list = new ArrayList();
            list.add("Friends");
            list.add("Friends of friends");
            list.add("Everyone");
         //   list.add("Six degree match");
            show(list, binding.more);
        });
searchFragmentVm.searchSixDegree();

        return binding.getRoot();
    }


    public void openPlaceAutoComplete() {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public void show(List<String> list, View view) {


        List<PowerMenuItem> l1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            l1.add(new PowerMenuItem(list.get(i), false));
        }

        powerMenu = new PowerMenu.Builder(getActivity())
                .addItemList(l1)
                .setAnimation( MenuAnimation.SHOWUP_TOP_LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor( ContextCompat.getColor(getActivity(), R.color.black))
                .setTextGravity( Gravity.CENTER)
                .setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setMenuColor( Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
        powerMenu.showAsDropDown(view);

    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            String textSelected = item.getTitle();
            if (textSelected.equals("Friends")) {
                searchFragmentVm.setSearchtype("1");

            } else if (textSelected.equals("Friends of friends")) {
                searchFragmentVm.setSearchtype("2");
            } else{

                searchFragmentVm.setSearchtype("3");

    }
            searchFragmentVm.search(getView());

            powerMenu.dismiss();
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);





                 Log.i("trails fragment", "Place: "+place.getName()+", "+place.getAddress());
                 if(place.getName()!=null){
                     searchFragmentVm.setPlacename( place.getName() );

                     if(place.getLatLng()!=null){
                         searchFragmentVm.search(getView());
                     }

                 }else {
                     searchFragmentVm.setPlacename("");
                 }



            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("trails fragment", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    //handling button click toast
    @BindingAdapter({"errorMsg"})
    public static void abc(View view, String message) {
        if (message != null) {
            showSnackBar((Activity) view.getContext(), view.getId(), message);
        }
        //goToNextScreen(LocationPermissionActivity.class);
    }

    @Override
    public void onSaveInstanceState( @NonNull Bundle outState ) {
        super.onSaveInstanceState( outState );
    }


}
