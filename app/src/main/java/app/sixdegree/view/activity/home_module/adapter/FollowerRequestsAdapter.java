package app.sixdegree.view.activity.home_module.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;

import app.sixdegree.databinding.RequestRowViewBinding;
import app.sixdegree.network.responses.getfollowerrequests.Data;
import app.sixdegree.view.activity.home_module.fragments.NotificationFragment;
import app.sixdegree.viewModel.FollowerRequestRowVm;
import app.sixdegree.viewModel.SixDegreeSearchRowVm;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FollowerRequestsAdapter extends RecyclerView.Adapter<FollowerRequestsAdapter.MyViewHolder> {

    List<app.sixdegree.network.responses.getfollowerrequests.Data> data;

String token="";
    public FollowerRequestsAdapter(String token) {
        this.data = new ArrayList<>();
        this.token=token;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_row
                        , parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder viewHolder = holder;
        FollowerRequestRowVm followerRequestRowVm = new FollowerRequestRowVm(data.get(position),token);
        viewHolder.binding.setViewModel(followerRequestRowVm);



                 followerRequestRowVm.isVerified.subscribeOn( AndroidSchedulers.mainThread()).doOnNext( data -> {
                     if (data) {


                         NotificationFragment f = (NotificationFragment) NotificationFragment.myFragment;
                         f.getFragmentManager().findFragmentByTag("Your Fragment Tag");
                         if (f!= null) {
                             f.refreshFragment();
                         }
            }
        }).subscribe();


        viewHolder.binding.parent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

             /*   if(holder.add.getVisibility()==View.VISIBLE){

                }else {*/
                followerRequestRowVm.goToProfileFragment( v ,String.valueOf(  data.get( position ).getFrom_user_id()),data.get( position ).getFollow_status(),
                        data.get(position).getFriend_status());
                //  }

            }
        } );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Data> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RequestRowViewBinding binding;

        public MyViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);


        }
    }

}