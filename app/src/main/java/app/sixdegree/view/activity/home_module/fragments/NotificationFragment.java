package app.sixdegree.view.activity.home_module.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.FragmentNotificationViewBinding;
import app.sixdegree.network.ApiFactory;
import app.sixdegree.network.ApiService;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.getinboxres.Data;
import app.sixdegree.network.responses.getinboxres.GetInboxRes;
import app.sixdegree.network.responses.gettagnotifications.GetTagNotificationRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.chatchat.adapters.ChatAdapter;
import app.sixdegree.view.activity.home_module.adapter.FollowerRequestsAdapter;
 import app.sixdegree.view.activity.home_module.adapter.SearchAdapter;
import app.sixdegree.view.activity.home_module.adapter.TagNotificationAdapter;
import app.sixdegree.viewModel.FollowerRequestRowVm;
import app.sixdegree.viewModel.NotificationFragmentVm;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationFragment extends Fragment {
FragmentNotificationViewBinding binding;
AppSession appSession;
ApiService apiService;
List<app.sixdegree.network.responses.gettagnotifications.Data> tagNotificationsList = new ArrayList<>();

    public static Fragment myFragment;
    ProgressBar pbar;
    NotificationFragmentVm notificationFragmentVm;
    TextView dummytext;
    TextView tv_no;
    TagNotificationAdapter tagNotificationAdapter;
    RecyclerView rv_tagnotification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    binding= DataBindingUtil.bind( inflater.inflate( R.layout.fragment_notification ,container,false) );
    myFragment=this;
    appSession =new AppSession( getActivity() );
    apiService= ApiFactory.getRetrofitInstance().create(ApiService.class);

          notificationFragmentVm=new NotificationFragmentVm(appSession);
        binding.setViewModel( notificationFragmentVm );
        binding.notificationRv.setLayoutManager(new LinearLayoutManager( getActivity() ) );
        FollowerRequestsAdapter followerRequestsAdapter=new FollowerRequestsAdapter(appSession.getToken());
        binding.notificationRv.setAdapter( followerRequestsAdapter );
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;




View view=binding.getRoot();
        dummytext=view.findViewById( R.id.dummytext );



        //set tag notification adapter
        rv_tagnotification=view.findViewById( R.id.rv_tagnotification);

        tagNotificationAdapter=new TagNotificationAdapter( getContext(),tagNotificationsList ,appSession,NotificationFragment.this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( getContext() );
        rv_tagnotification.setLayoutManager( linearLayoutManager );
        rv_tagnotification.setAdapter( tagNotificationAdapter );
        return binding.getRoot();
    }






    public void refreshFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).commitAllowingStateLoss();
        getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitAllowingStateLoss();
        //adapter.notifyDataSetChanged();
    }




    public void getTagNotifications() {
        binding.pbar.setVisibility( View.VISIBLE );
        apiService.getTagNoti(appSession.getToken())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetTagNotificationRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetTagNotificationRes res) {
                        tagNotificationsList.clear();
                        if (res.getStatus()) {
                            tagNotificationsList.addAll(res.getData());
                            tagNotificationAdapter.notifyDataSetChanged();
                        }




                        if (notificationFragmentVm.isVisible   && res.getData().size()==0){
                            binding.rvTagnotification.setVisibility( View.GONE );
                            binding.tvNo.setVisibility(View.VISIBLE);
                        }else {
                            binding.rvTagnotification.setVisibility( View.VISIBLE );
                            binding.tvNo.setVisibility(View.GONE);
                        }
                      /*  if(res.getData().size()==0){

                            binding.rvTagnotification.setVisibility( View.GONE );
                            binding.tvNo.setVisibility(View.VISIBLE);

                        }else {
                             binding.rvTagnotification.setVisibility( View.VISIBLE );
                            binding.tvNo.setVisibility(View.GONE);
                        }*/
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
 public void readNotification() {
         apiService.readNotification(appSession.getToken())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {

                    }

                    @Override
                    public void onError(Throwable e) {
                      }

                    @Override
                    public void onComplete() {
                      }
                });


    }
     public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame_container__, fragment);
        // transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();



       // getAllChats();

        getTagNotifications();

        readNotification();

    }
}
