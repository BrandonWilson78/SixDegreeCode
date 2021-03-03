package app.sixdegree.view.activity.home_module.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.FragmentFriendsViewBinding;
import app.sixdegree.network.ApiFactory;
import app.sixdegree.network.ApiService;
import app.sixdegree.network.responses.getinboxres.Data;
import app.sixdegree.network.responses.getinboxres.GetInboxRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.SearchByNameActivity;
import app.sixdegree.view.activity.chatchat.ChatActivity;
import app.sixdegree.view.activity.chatchat.ChatDetails;
import app.sixdegree.view.activity.chatchat.adapters.ChatAdapter;
import app.sixdegree.view.activity.home_module.adapter.FriendsAdapter;
import app.sixdegree.view.activity.loginModule.AddFriendsActivity;
import app.sixdegree.viewModel.ViewFriendsVm;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class FriendsFragment extends Fragment {
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    FragmentFriendsViewBinding binding;
    ViewFriendsVm viewFriendsVm;
    List<Data> dataList = new ArrayList<>();
    ChatAdapter chatAdapter;
    ApiService apiService;
    AppSession appSession;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false);
        apiService= ApiFactory.getRetrofitInstance().create( ApiService.class);
        binding.searchByLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlaceAutoComplete();
            }
        });

          appSession = new AppSession(getActivity());

        viewFriendsVm = new ViewFriendsVm(appSession.getToken(),appSession);
        binding.setViewModel(viewFriendsVm);


        binding.friendsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPending.setLayoutManager(new LinearLayoutManager(getActivity()));
        FriendsAdapter trailCommentsAdapter = new FriendsAdapter(appSession);
        binding.friendsRv.setAdapter(trailCommentsAdapter);


        binding.ivSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                startActivity( new Intent( getContext(), SearchByNameActivity.class ) );
            }
        } );

        binding.addFriend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                startActivity( new Intent( getContext(), AddFriendsActivity.class ) );
            }
        } );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //set caht adapter

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.messageRv.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(dataList, getContext(), width,appSession);
        binding.messageRv.setAdapter(chatAdapter);


        return binding.getRoot();
    }

    public void openPlaceAutoComplete() {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

  @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("trails fragment", "Place: " + place.getName() + ", " + place.getAddress());
//                tripTrailsVm.setLongitude(String.valueOf(place.getLatLng().longitude));
//                tripTrailsVm.setLattitude(String.valueOf(place.getLatLng().latitude));
//                tripTrailsVm.setLocationName(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("trails fragment", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewFriendsVm.fetchAllFriends();
        viewFriendsVm.fetchPendingRequests();
        getAllChats();
    }

    public void getAllChats() {
        binding.pbar.setVisibility( View.VISIBLE );
        apiService.getInboxMessages(appSession.getToken())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetInboxRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetInboxRes res) {
                        dataList.clear();
                        if (res.getStatus()) {
                            dataList.addAll(res.getData());
                            chatAdapter.notifyDataSetChanged();
                        }


                        if(res.getData().size()==0){

                            binding.tvDummytext.setText( "No Chats yet." );
                            binding.tvDummytext.setVisibility( View.VISIBLE );
                            binding.messageRv.setVisibility( View.INVISIBLE );

                        }else {
                            binding.tvDummytext.setVisibility( View.INVISIBLE );
                            binding.messageRv.setVisibility( View.VISIBLE );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.pbar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onComplete() {
                        binding.pbar.setVisibility( View.GONE );
                    }
                });


    }

}
