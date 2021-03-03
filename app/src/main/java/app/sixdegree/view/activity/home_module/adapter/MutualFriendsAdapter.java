package app.sixdegree.view.activity.home_module.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.GlobalFriendRowViewBinding;
import app.sixdegree.databinding.MutualFriendRowViewBinding;
import app.sixdegree.databinding.MutualFriendRowViewBindingImpl;
 import app.sixdegree.network.responses.getmutualfriends.X1;
import app.sixdegree.network.responses.viewfriends.Data;
import app.sixdegree.network.responses.viewprofilefriends.MutualFriend;
import app.sixdegree.viewModel.MutualfriendRowVm;
import app.sixdegree.viewModel.ViewFriendRowVm;

public class MutualFriendsAdapter extends RecyclerView.Adapter<MutualFriendsAdapter.MyViewHolder> {

    List<MutualFriend> data=new ArrayList<>(  );


    public MutualFriendsAdapter(List<MutualFriend> mutualFriends) {
        this.data = mutualFriends;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mutualfriend_row
                        , parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder viewHolder = holder;
        MutualfriendRowVm mutualfriendRowVm = new MutualfriendRowVm(data.get( position ));

        viewHolder.binding.setViewModel(mutualfriendRowVm);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<MutualFriend> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MutualFriendRowViewBinding binding;

        public MyViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);


        }
    }

}